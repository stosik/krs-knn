package logic.utils;

import logic.model.entity.Article;
import logic.preprocessing.ArticlePreprocessor;

import java.util.List;

public class PreprocessUtils
{
    
    public static List<Article> preprocessTextEntities(List<Article> entities, String affPath, String dictPath)
    {
        ArticlePreprocessor preprocessor = new ArticlePreprocessor();
        entities = preprocessor.removeStopWords(entities);
        entities = preprocessor.removeNumbers(entities);
        entities = preprocessor.replaceIrregularVerbsWithInfinitives(entities);
        
        return preprocessor.extractStems(entities, affPath, dictPath);
    }
}
