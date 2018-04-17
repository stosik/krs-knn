package logic.model.entity;

import logic.model.Base;

public class Article extends Base<String>
{
    public Article(String label, String content)
    {
        super(label, content);
    }
}

