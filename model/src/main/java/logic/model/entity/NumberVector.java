package logic.model.entity;

import logic.model.Base;

import java.util.List;

public class NumberVector extends Base<List<Double>>
{
    public NumberVector(String label, List<Double> content)
    {
        super(label, content);
    }
}
