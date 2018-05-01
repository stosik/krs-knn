package logic.classifier;

import javafx.util.Pair;
import logic.metrics.Distance;
import logic.model.Base;
import logic.model.entity.NumberVector;
import lombok.val;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class KnnNumber implements Classifier<NumberVector>
{
    private static int counter = 1;
    private final int K;
    private final Distance<Base> measurer;
    
    public KnnNumber(int k, Distance<Base> measurer)
    {
        K = k;
        this.measurer = measurer;
    }
    
    @Override
    public List<String> classify(List<NumberVector> trainingSet, List<NumberVector> testSet)
    {
        List<String> classifiedLabels = new ArrayList<>();
        
        testSet
            .stream()
            .map(testEntity -> classifyOneEntity(trainingSet, testEntity))
            .forEach(classifiedLabels::add);
        
        return classifiedLabels;
    }
    
    private String classifyOneEntity(List<NumberVector> trainingSet, NumberVector testEntity)
    {
        val similarities = findSimilarities(trainingSet, testEntity);
        val labelsFrequency = findLabelsFrequencies(trainingSet, similarities);
        
        return findClassifiedLabel(labelsFrequency);
    }
    
    private List<Pair<Integer, Double>> findSimilarities(List<NumberVector> trainingSet, NumberVector testEntity)
    {
        return IntStream
            .range(0, trainingSet.size())
            .mapToObj(i -> new Pair<>(i, measurer.distanceNumber(trainingSet.get(i), testEntity)))
            .sorted(measurer.getComparator()).collect(Collectors.toList());
    }
    
    private Map<String, Integer> findLabelsFrequencies(List<NumberVector> trainingSet, List<Pair<Integer, Double>> similarities)
    {
        return IntStream
            .range(0, K)
            .mapToObj(i -> trainingSet.get(similarities.get(i).getKey()).getLabel())
            .collect(Collectors.toMap(label -> label, label -> 1, (a, b) -> a + b, LinkedHashMap::new));
    }
    
    private String findClassifiedLabel(Map<String, Integer> labelsFrequency)
    {
        Integer maxFrequency = 0;
        String classifiedLabel = "";
        for(String currentLabel : labelsFrequency.keySet())
        {
            Integer currentFrequency = labelsFrequency.get(currentLabel);
            if(currentFrequency > maxFrequency)
            {
                maxFrequency = currentFrequency;
                classifiedLabel = currentLabel;
            }
        }
        return classifiedLabel;
    }
}
