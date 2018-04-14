package logic.model.entity;

import logic.model.Base;

import java.util.Map;

public class FrequencyMatrix extends Base<Map<String, Long>>
{
    public FrequencyMatrix(String label, Map<String, Long> content)
    {
        super(label, content);
    }
}
