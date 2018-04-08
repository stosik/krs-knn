package logic.extractors.utils;

import logic.model.Article;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Wyodrebnia z tekstu zbior n najczestrzych wyrazow
 */
public class FreqListBuilder
{
    private static final int LENGTH_LIMIT = 2;
    
    public List<String> getWordList(List<Article> articles, int n, int opt)
    {
        List<String> wordList = new ArrayList<>(n);
        Map<String, Integer> wordCount = getWordCount(articles, opt);
        
        Map<String, Integer> sortedWordList = sort(wordCount);
        Iterator<Map.Entry<String, Integer>> iter = sortedWordList.entrySet().iterator();
        for(int i = 0; i < n; i++)
        {
            wordList.add(iter.next().getKey());
        }
        return wordList;
    }
    
    public Map<String, Integer> getWordCount(List<Article> articles, int option)
    {
        Map<String, Integer> wordCount = new HashMap<>();
        for(Article art : articles)
        {
            if(option == 0)
            {
                if(art.getPlaces().size() == 1
                   && (art.getPlaces().contains("west-germany") || art.getPlaces().contains("usa")
                       || art.getPlaces().contains("france") || art.getPlaces().contains("uk")
                       || art.getPlaces().contains("canada") || art.getPlaces().contains("japan")))
                {
                    countWordsIfBodyNotEmpty(wordCount, art);
                }
            }
            else if(option == 1)
            {
                if(art.getTopics().size() == 1
                   && (art.getTopics().contains("earn") || art.getTopics().contains("acq")
                       || art.getTopics().contains("crude") || art.getTopics().contains("trade")
                       || art.getTopics().contains("money fx") || art.getTopics().contains("interest")))
                {
                    countWordsIfBodyNotEmpty(wordCount, art);
                }
            }
        }
        
        return wordCount;
    }
    
    private void countWordsInExpression(Map<String, Integer> wordCount, String[] words_table)
    {
        for(String word : words_table)
        {
            if(word.length() > LENGTH_LIMIT)
            {
                if(!wordCount.containsKey(word))
                {
                    wordCount.put(word, 1);
                }
                else
                {
                    wordCount.put(word, wordCount.get(word) + 1);
                }
            }
        }
    }
    
    private void countWordsIfBodyNotEmpty(Map<String, Integer> wordCount, Article art)
    {
        final String[] words_table;
        if(art.getBody() != null)
        {
            words_table = art.getBody().split(" ");
            countWordsInExpression(wordCount, words_table);
        }
    }
    
    public static Map sort(Map map)
    {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, (Comparator) (o1, o2) -> ((Comparable) ((Map.Entry) (o2)).getValue())
            .compareTo(((Map.Entry) (o1)).getValue()));
        
        Map result = new LinkedHashMap();
        for(final Object aList : list)
        {
            Map.Entry entry = (Map.Entry) aList;
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
