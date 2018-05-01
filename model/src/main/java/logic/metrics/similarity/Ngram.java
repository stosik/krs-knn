package logic.metrics.similarity;

import javafx.util.Pair;
import logic.metrics.Distance;
import logic.model.entity.FrequencyMatrix;
import logic.model.entity.NumberVector;

import java.util.Comparator;
import java.util.Map;

public class Ngram implements Distance<FrequencyMatrix>
{
    private final int n;
    
    public Ngram(int n)
    {
        this.n = n;
    }
    
    @Override
    public Comparator<Pair<Integer, Double>> getComparator()
    {
        return Comparator.comparing(Pair<Integer, Double>::getValue).reversed();
    }
    
    @Override
    public double distance(FrequencyMatrix first, FrequencyMatrix second)
    {
        StringBuilder firstSb = new StringBuilder();
        StringBuilder secondSb = new StringBuilder();
        for(Map.Entry<String, Long> entry : first.getContent().entrySet())
        {
            for(int i = 0; i < entry.getValue(); ++i)
            {
                firstSb.append(entry.getKey()).append(" ");
            }
        }
        for(Map.Entry<String, Long> entry : second.getContent().entrySet())
        {
            for(int i = 0; i < entry.getValue(); ++i)
            {
                secondSb.append(entry.getKey()).append(" ");
            }
        }
        
        String firstStr = firstSb.toString();
        String secondStr = secondSb.toString();
        int possibleNgramsCount = firstStr.length() - n + 1;
        int actualNgramsCount = 0;
        for(int i = 0; i < possibleNgramsCount; ++i)
        {
            CharSequence firstSubseq = firstStr.subSequence(i, i + n);
            if(secondStr.contains(firstSubseq))
            {
                ++actualNgramsCount;
            }
        }
        
        return (double) actualNgramsCount / (double) possibleNgramsCount;
    }
    
    @Override
    public double distanceNumber(NumberVector first, NumberVector second)
    {
        return 0;
    }
}
