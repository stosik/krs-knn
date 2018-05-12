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
import java.util.stream.Collectors;

@Getter
public class CountExtractor implements Extractor<Article, WordVector>
{
    private final Map<String, Integer> dictionary = new HashMap<>();
    
    public CountExtractor(List<Article> articles)
    {
        fillDictionary(articles);
    }
    
    private void fillDictionary(List<Article> articles)
    {
        Integer totalWordsCount = 0;
        for(Article article : articles)
        {
            val uniqueWords = TextUtils.getUniqueWords(article);
            for(String word : uniqueWords)
            {
                if(!dictionary.containsKey(word))
                {
                    dictionary.put(word, totalWordsCount++);
                }
            }
        }
    }
    
    @Override
    public List<WordVector> extractFeatures(List<Article> articles)
    {
        return articles
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
