package logic.metrics.similarity;

import javafx.util.Pair;
import logic.metrics.Distance;
import logic.model.entity.NumberVector;
import logic.model.entity.WordVector;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CosineSimilarity implements Distance<WordVector>
{
    @Override
    public Comparator<Pair<Integer, Double>> getComparator() {
        return Comparator.comparing(Pair<Integer, Double>::getValue).reversed();
    }
    
    @Override
    public double distance(WordVector first, WordVector second) {
        double productsSum = 0d, firstSquaresSum = 0d, secondSquaresSum = 0d;
        Map<Integer, Double> firstValues = first.getContent();
        Map<Integer, Double> secondValues = second.getContent();
        
        Set<Integer> allKeys = new HashSet<>();
        allKeys.addAll(firstValues.keySet());
        allKeys.addAll(secondValues.keySet());
        
        for (Integer key : allKeys) {
            double firstValue = firstValues.getOrDefault(key, 0d);
            double secondValue = secondValues.getOrDefault(key, 0d);
            productsSum += firstValue * secondValue;
            firstSquaresSum += firstValue * firstValue;
            secondSquaresSum += secondValue * secondValue;
        }
        
        return Math.abs(productsSum) / Math.sqrt(firstSquaresSum * secondSquaresSum);
    }
    
    @Override
    public double distanceNumber(NumberVector first, NumberVector second)
    {
        return 0;
    }
}
