package logic.metrics.similarity;

import javafx.util.Pair;
import logic.metrics.Distance;
import logic.model.entity.FrequencyMatrix;
import logic.model.entity.NumberVector;
import lombok.val;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class TFMCosine implements Distance<FrequencyMatrix>
{
    
    @Override
    public Comparator<Pair<Integer, Double>> getComparator()
    {
        return Comparator.comparing(Pair<Integer, Double>::getValue).reversed();
    }
    
    @Override
    public double distance(FrequencyMatrix first, FrequencyMatrix second)
    {
        long productsSum = 0L, firstSquaresSum = 0L, secondSquaresSum = 0L;
        val firstMap = first.getContent();
        val secondMap = second.getContent();
        Set<String> allKeys = new HashSet<>();
        allKeys.addAll(firstMap.keySet());
        allKeys.addAll(secondMap.keySet());
        for(String key : allKeys)
        {
            val firstValue = firstMap.getOrDefault(key, 0L);
            val secondValue = secondMap.getOrDefault(key, 0L);
            productsSum += firstValue * secondValue;
            firstSquaresSum += firstValue * firstValue;
            secondSquaresSum += secondValue * secondValue;
        }
        return (double) productsSum / Math.sqrt(firstSquaresSum * secondSquaresSum);
    }
    
    @Override
    public double distanceNumber(NumberVector first, NumberVector second)
    {
        return 0;
    }
}
