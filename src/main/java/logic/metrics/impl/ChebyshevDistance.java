package logic.metrics.impl;

import logic.metrics.Distance;
import logic.model.WordVector;

import java.util.OptionalDouble;
import java.util.stream.IntStream;

public class ChebyshevDistance implements Distance
{
    
    @Override
    public double distance(WordVector wv1, WordVector wv2)
    {
        OptionalDouble max = IntStream
            .range(0, wv1.getWordValues().size())
            .mapToDouble(i -> Math.abs(wv1.getWordValues().get(i) - wv2.getWordValues().get(i)))
            .max();
    
        if(max.isPresent())
        {
            return max.getAsDouble();
        }
        else
        {
            return max.orElse(0.0);
        }
    }
}