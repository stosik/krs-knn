package logic.classifier;

import javafx.util.Pair;
import logic.metrics.Distance;
import logic.model.Base;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class KNN<T extends Base> implements Classifier<T>
{
    private static int counter = 1;
    private final int K;
    private final Distance<T> measurer;
    
    public KNN(int k, Distance<T> measurer)
    {
        K = k;
        this.measurer = measurer;
    }
    
    /**
     * Returns map of test entities with their new labels after classification process.
     */
    @Override
    public List<String> classify(List<T> trainingSet, List<T> testSet)
    {
        List<String> classifiedLabels = new ArrayList<>();
        for(T testEntity : testSet)
        {
            System.out.println(counter++);
            String classifiedLabel = classifyOneEntity(trainingSet, testEntity);
            classifiedLabels.add(classifiedLabel);
        }
        return classifiedLabels;
    }
    
    /**
     * Returns new label for test entity after classification process.
     */
    private String classifyOneEntity(List<T> trainingSet, T testEntity)
    {
        List<Pair<Integer, Double>> similarities = findSimilarities(trainingSet, testEntity);
        Map<String, Integer> labelsFrequency = findLabelsFrequencies(trainingSet, similarities);
        return findClassifiedLabel(labelsFrequency);
    }
    
    /**
     * Returns list of pairs, where Integer is the index of entity in trainingSet and Double is the
     * similarity of test entity to that training entity. The list is sorted by similarity.
     */
    private List<Pair<Integer, Double>> findSimilarities(List<T> trainingSet, T testEntity)
    {
        return IntStream
            .range(0, trainingSet.size())
            .mapToObj(i -> new Pair<>(i, measurer.distance(trainingSet.get(i), testEntity)))
            .sorted(measurer.getComparator()).collect(Collectors.toList());
    }
    
    /**
     * Returns map, where String is one of the K most similar labels and Integer is it's frequency.
     */
    private Map<String, Integer> findLabelsFrequencies(List<T> trainingSet, List<Pair<Integer, Double>> similarities)
    {
        return IntStream
            .range(0, K)
            .mapToObj(i -> trainingSet.get(similarities.get(i).getKey()).getLabel())
            .collect(Collectors.toMap(label -> label, label -> 1, (a, b) -> a + b, LinkedHashMap::new));
    }
    
    /**
     * Returns new label for test entity, which is the most frequent one.
     */
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
