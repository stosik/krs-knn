package logic.metrics.distance;

import javafx.util.Pair;
import logic.metrics.Distance;
import logic.model.entity.NumberVector;
import logic.model.entity.WordVector;
import lombok.val;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class EuclideanDistance implements Distance<WordVector>
{
    
    @Override
    public Comparator<Pair<Integer, Double>> getComparator()
    {
        return Comparator.comparing(Pair<Integer, Double>::getValue);
    }
    
    @Override
    public double distance(WordVector first, WordVector second)
    {
        double sum = 0d;
        val firstValues = first.getContent();
        val secondValues = second.getContent();
        Set<Integer> allKeys = new HashSet<>();
        allKeys.addAll(firstValues.keySet());
        allKeys.addAll(secondValues.keySet());
        
        for(Integer key : allKeys)
        {
            double firstValue = firstValues.getOrDefault(key, 0d);
            double secondValue = secondValues.getOrDefault(key, 0d);
            sum += pow(firstValue - secondValue, 2);
        }
        
        return sqrt(sum);
    }
    
    @Override
    public double distanceNumber(NumberVector first, NumberVector second)
    {
        double sum = 0d;
        val firstVector = first.getContent();
        val secondVector = second.getContent();
        
        for(int i = 0; i < firstVector.size(); i++)
        {
            double firstValue = firstVector.get(i);
            double secondValue = secondVector.get(i);
            sum += pow(firstValue - secondValue, 2);
        }
        
        return sqrt(sum);
    }
}
