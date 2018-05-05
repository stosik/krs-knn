package logic.utils;

import logic.model.entity.NumberVector;
import lombok.val;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataUtils
{
    public static List<NumberVector> normalize(List<NumberVector> dataEntry)
    {
        val content = dataEntry
            .stream()
            .map(NumberVector::getContent)
            .collect(Collectors.toList());
        
        val minInColumn = findMinInColumn(content);
        val maxInColumn = findMaxInColumn(content);
        
        for(int i = 0; i < content.size(); i++)
        {
            for(int j = 0; j < content.get(i).size(); j++)
            {
                val z = (content.get(i).get(j) - minInColumn.get(j)) / (maxInColumn.get(j) - minInColumn.get(j));
                content.set(i, content.get(i)).set(j, z);
            }
        }
        
        return IntStream
            .range(0, dataEntry.size())
            .mapToObj(i -> new NumberVector(dataEntry.get(i).getLabel(), content.get(i)))
            .collect(Collectors.toList());
    }
    
    private static List<Double> findMinInColumn(List<List<Double>> content)
    {
        List<Double> columnMin = new ArrayList<>();
        for(int i = 0; i < content.get(0).size(); i++)
        {
            List<Double> temp = new ArrayList<>();
            for(List<Double> aContent : content)
            {
                temp.add(aContent.get(i));
            }
            columnMin.add(Collections.min(temp));
        }
        
        return columnMin;
    }
    
    private static List<Double> findMaxInColumn(List<List<Double>> content)
    {
        List<Double> columnMax = new ArrayList<>();
        for(int i = 0; i < content.get(0).size(); i++)
        {
            List<Double> temp = new ArrayList<>();
            for(List<Double> aContent : content)
            {
                temp.add(aContent.get(i));
            }
            columnMax.add(Collections.max(temp));
        }
        
        return columnMax;
    }
}
