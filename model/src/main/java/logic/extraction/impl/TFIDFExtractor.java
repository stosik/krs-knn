package logic.extraction.impl;

import logic.extraction.Extractor;
import logic.model.entity.Article;
import logic.model.entity.WordVector;
import logic.utils.TextUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TFIDFExtractor implements Extractor<Article, WordVector>
{
    
    private final int N;
    private final Map<String, Integer> dictionary = new HashMap<>();
    private Map<String, Integer> wordOccurencesCounts = new HashMap<>();
    private boolean truncateDictionary = true;
    
    public TFIDFExtractor(List<Article> articles)
    {
        fillDictionary(articles);
        N = articles.size();
    }
    
    private static String apply(Map.Entry<String, Integer> entry)
    {
        return (entry.getKey());
    }
    
    private void fillDictionary(List<Article> articles)
    {
        Integer totalWordsCount = 0;
        for(Article entity : articles)
        {
            Set<String> entityUniqueWords = TextUtils.getUniqueWords(entity);
            for(String word : entityUniqueWords)
            {
                wordOccurencesCounts.merge(word, 1, Integer::sum);
            }
        }
        if(truncateDictionary)
        {
            System.out.println(wordOccurencesCounts.size());
            wordOccurencesCounts = wordOccurencesCounts
                .entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed())
                .limit((int) (wordOccurencesCounts.size() * 0.01))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        System.out.println(wordOccurencesCounts.size());
    
        for(Map.Entry<String, Integer> entry : wordOccurencesCounts.entrySet())
        {
            dictionary.put(entry.getKey(), totalWordsCount++);
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
        Map<Integer, Double> features = new HashMap<>();
        
        Map<String, Long> allWordsCounts = TextUtils.getAllWordsCounts(article);
        Long maxWordCount = Collections.max(allWordsCounts.values());
        
        for(String word : allWordsCounts.keySet())
        {
            Integer wordId = dictionary.get(word);
            if(wordId != null)
            {
                double tf = calculateTermFrequency(allWordsCounts.get(word), maxWordCount);
                double idf = calculateInverseDocumentFrequency(word);
                double tfidf = tf * idf;
                features.put(wordId, tfidf);
            }
        }
        
        return new WordVector(article.getLabel(), features);
    }
    
    private double calculateTermFrequency(Long wordCount, Long maxWordCount)
    {
        return 0.5 + 0.5 * (wordCount / maxWordCount);
    }
    
    private double calculateInverseDocumentFrequency(String word)
    {
        return Math.log(N / wordOccurencesCounts.get(word));
    }
}
