package logic.extractors.processors;

import logic.extractors.Extractor;
import logic.extractors.WordCountExtractor;
import logic.extractors.WordFrequencyExtractor;
import logic.extractors.utils.FreqListBuilder;
import logic.model.Article;
import logic.model.WordVector;
import logic.similarity.EqualityDistance;
import logic.similarity.HammingDistance;
import logic.similarity.LevenshteinDistance;
import logic.similarity.SimilarityDistance;
import logic.similarity.WagnerFischerDistance;

import java.util.List;
import java.util.Map;

public abstract class Processor
{
    protected SimilarityDistance distance;
    protected Extractor extractor;
    protected List<Article> articles;
    protected int vectorLength;
    protected List<String> frequentWords;
    
    public Processor(List<Article> articles, int vectorLength, String distanceName, String extractorName)
    {
        this.articles = articles;
        this.vectorLength = vectorLength;
        switch(distanceName)
        {
            case "levenshtein":
                distance = new LevenshteinDistance();
                break;
            case "wagner-fisher":
                distance = new WagnerFischerDistance();
                break;
            case "hamming":
                distance = new HammingDistance();
                break;
            default:
                distance = new EqualityDistance();
                break;
        }
        switch(extractorName)
        {
            case "frequency":
                extractor = new WordFrequencyExtractor(distance);
                break;
            case "count":
                extractor = new WordCountExtractor(distance);
                break;
        }
    }
    
    void extractVectors(int opt, List<WordVector> result)
    {
        for(Article art : articles)
        {
            if(opt == 0 && checkPlace(art) != null)
            {
                WordVector vector = new WordVector(checkPlace(art));
                for(String word : frequentWords)
                {
                    vector.add(extractor.getValue(art, word));
                }
                result.add(vector);
            }
            else if(opt == 1 && checkTopic(art) != null)
            {
                WordVector vector = new WordVector(checkTopic(art));
                for(String word : frequentWords)
                {
                    vector.add(extractor.getValue(art, word));
                }
                result.add(vector);
            }
        }
    }
    
    public static String checkPlace(Article art)
    {
        if(art.getPlaces().contains("west-germany"))
        {
            return "west germany";
        }
        else if(art.getPlaces().contains("usa"))
        {
            return "usa";
        }
        else if(art.getPlaces().contains("france"))
        {
            return "france";
        }
        else if(art.getPlaces().contains("uk"))
        {
            return "uk";
        }
        else if(art.getPlaces().contains("canada"))
        {
            return "canada";
        }
        else if(art.getPlaces().contains("japan"))
        {
            return "japan";
        }
        else
        {
            return null;
        }
    }
    
    static String checkTopic(Article art)
    {
        if(art.getTopics().size() == 1)
        {
            if(art.getTopics().contains("earn"))
            {
                return "earn";
            }
            else if(art.getTopics().contains("acq"))
            {
                return "acq";
            }
            else if(art.getTopics().contains("crude"))
            {
                return "crude";
            }
            else if(art.getTopics().contains("trade"))
            {
                return "trade";
            }
            else if(art.getTopics().contains("money fx"))
            {
                return "money fx";
            }
            else if(art.getTopics().contains("interest"))
            {
                return "interest";
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
    
    public List<String> getFrequentWords()
    {
        return frequentWords;
    }
    
    public abstract List<WordVector> run(int opt);
    
    public abstract List<WordVector> runWithoutRepetition(int opt);
    
    public static Map getFreqMap(List<Article> articles, int opt)
    {
        Map<String, Integer> freqMap = new FreqListBuilder().getWordCount(articles, opt);
        return FreqListBuilder.sort(freqMap);
    }
}
