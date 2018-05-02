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
        
        for(int i = 0; i < content.get(0).size(); i++)
        {
            for(int j = 0; j < content.size(); j++)
            {
                val z = content.get(i).get(j) - minInColumn.get(i) / maxInColumn.get(i) - minInColumn.get(i);
                content.set(i, content.get(i)).set(j, z);
            }
        }
        
        return IntStream
            .range(0, dataEntry.size())
            .mapToObj(i -> new NumberVector(dataEntry.get(i).getLabel(), content.get(i)))
            .collect(Collectors.toList());
    }
    
    private static List<Double> findMaxInColumn(List<List<Double>> content)
    {
        List<Double> columnMax = new ArrayList<>();
        for(int i = 0; i < content.get(0).size(); i++)
        {
            List<Double> temp = new ArrayList<>();
            for(int j = 0; j < content.size() - 1; j++)
            {
                temp.add(content.get(i).get(j));
            }
            columnMax.add(Collections.max(temp));
        }
        
        return columnMax;
    }
    
    private static List<Double> findMinInColumn(List<List<Double>> content)
    {
        List<Double> columnMin = new ArrayList<>();
        for(int i = 0; i < content.get(0).size(); i++)
        {
            List<Double> temp = new ArrayList<>();
            for(int j = 0; j < content.size() - 1; j++)
            {
                temp.add(content.get(i).get(j));
            }
            columnMin.add(Collections.min(temp));
        }
        
        return columnMin;
    }
}
