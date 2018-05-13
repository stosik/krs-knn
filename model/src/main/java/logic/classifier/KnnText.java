package logic.classifier;

import javafx.util.Pair;
import logic.metrics.Distance;
import logic.model.Base;
import lombok.val;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class KnnText<T extends Base> implements Classifier<T>
{
    private final int K;
    private final Distance<T> measurer;
    
    public KnnText(int k, Distance<T> measurer)
    {
        K = k;
        this.measurer = measurer;
    }
    
    @Override
    public List<String> classify(List<T> trainingSet, List<T> testSet)
    {
        List<String> classifiedLabels = new ArrayList<>();
        
        for(T testEntity : testSet)
        {
            String classifiedLabel = classifyOneEntity(trainingSet, testEntity);
            classifiedLabels.add(classifiedLabel);
        }
        return classifiedLabels;
    }
    
    private String classifyOneEntity(List<T> trainingSet, T testEntity)
    {
        val similarities = findSimilarities(trainingSet, testEntity);
        val labelsFrequency = findLabelsFrequencies(trainingSet, similarities);
        
        return findClassifiedLabel(labelsFrequency);
    }
    
    private List<Pair<Integer, Double>> findSimilarities(List<T> trainingSet, T testEntity)
    {
        return IntStream
            .range(0, trainingSet.size())
            .mapToObj(i -> new Pair<>(i, measurer.distance(trainingSet.get(i), testEntity)))
            .sorted(measurer.getComparator()).collect(Collectors.toList());
    }
    
    private Map<String, Integer> findLabelsFrequencies(List<T> trainingSet, List<Pair<Integer, Double>> similarities)
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
