package logic.io.parser;

import logic.model.entity.Article;

import java.io.File;
import java.util.List;

public interface Parser
{
    List<Article> parse(File file, String label);
    
    List<Article> parse(String string);
}
