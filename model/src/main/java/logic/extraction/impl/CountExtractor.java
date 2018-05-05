package logic.extraction.impl;

import logic.extraction.Extractor;
import logic.model.entity.Article;
import logic.model.entity.WordVector;
import logic.utils.TextUtils;
import lombok.Getter;
import lombok.val;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class CountExtractor implements Extractor<Article, WordVector>
{
    private final Map<String, Integer> dictionary = new HashMap<>();
    
    public CountExtractor(List<Article> entities)
    {
        fillDictionary(entities);
    }
    
    private void fillDictionary(List<Article> trainingArticles)
    {
        Integer totalWordsCount = 0;
        for(Article article : trainingArticles)
        {
            Set<String> entityUniqueWords = TextUtils.getUniqueWords(article);
            for(String word : entityUniqueWords)
            {
                if(!dictionary.containsKey(word))
                {
                    dictionary.put(word, totalWordsCount++);
                }
            }
        }
    }
    
    @Override
    public List<WordVector> extractFeatures(List<Article> testEntities)
    {
        return testEntities
            .stream()
            .map(this::extractFeatures)
            .collect(Collectors.toList());
    }
    
    private WordVector extractFeatures(Article article)
    {
        val features = TextUtils
            .getAllWords(article)
            .stream()
            .map(dictionary::get)
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(wordId -> wordId, wordId -> 1D, Double::sum));
        
        return new WordVector(article.getLabel(), features);
    }
}
