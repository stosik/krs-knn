package logic.model;

import lombok.Getter;

@Getter
public abstract class Base<T>
{
    private final String label;
    private final T content;
    
    protected Base(String label, T content)
    {
        this.label = label;
        this.content = content;
    }
}
