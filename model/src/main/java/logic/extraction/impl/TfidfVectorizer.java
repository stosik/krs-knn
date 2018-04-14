package logic.extraction.impl;

import logic.extraction.Extractor;
import logic.model.entity.Article;
import logic.model.entity.WordVector;
import logic.utils.TextUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TfidfVectorizer implements Extractor<Article, WordVector>
{
    
    private final int N;
    private final Map<String, Integer> dictionary = new HashMap<>();
    private final Map<String, Integer> wordOccurencesCounts = new HashMap<>();
    
    public TfidfVectorizer(List<Article> trainingEntities)
    {
        fillDictionary(trainingEntities);
        N = trainingEntities.size();
    }
    
    private void fillDictionary(List<Article> trainingEntities)
    {
        Integer totalWordsCount = 0;
        for(Article entity : trainingEntities)
        {
            Set<String> entityUniqueWords = TextUtils.getUniqueWords(entity);
            for(String word : entityUniqueWords)
            {
                if(!dictionary.containsKey(word))
                {
                    dictionary.put(word, totalWordsCount++);
                }
                wordOccurencesCounts.merge(word, 1, Integer::sum);
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
    
    private WordVector extractFeatures(Article testEntity)
    {
        Map<Integer, Double> features = new HashMap<>();
        
        Map<String, Long> allWordsCounts = TextUtils.getAllWordsCounts(testEntity);
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
        
        return new WordVector(testEntity.getLabel(), features);
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
