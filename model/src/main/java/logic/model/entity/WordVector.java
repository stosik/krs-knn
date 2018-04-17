package logic.model.entity;

import logic.model.Base;

import java.util.Map;

public class WordVector extends Base<Map<Integer, Double>>
{
    public WordVector(String label, Map<Integer, Double> content)
    {
        super(label, content);
    }
}
