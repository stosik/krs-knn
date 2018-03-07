package logic.metrics.impl;

import logic.metrics.Distance;
import logic.model.WordVector;

import java.util.stream.IntStream;

public class EuclideanDistance implements Distance
{
    
    @Override
    public double distance(WordVector wv1, WordVector wv2)
    {
        return IntStream
            .range(0, wv1.getWordValues().size())
            .mapToDouble(i -> (wv1.getWordValues().get(i) - wv2.getWordValues().get(i))
                              * (wv1.getWordValues().get(i) - wv2.getWordValues().get(i)))
            .sum();
    }
}
