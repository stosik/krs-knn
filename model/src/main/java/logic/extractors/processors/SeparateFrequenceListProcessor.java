package logic.extractors.processors;

import logic.extractors.utils.FreqListBuilder;
import logic.model.Article;
import logic.model.WordVector;

import java.util.ArrayList;
import java.util.List;

public class SeparateFrequenceListProcessor extends Processor
{
    public SeparateFrequenceListProcessor(List<Article> articles, int vectorLength, String distanceName, String extractorName)
    {
        super(articles, vectorLength, distanceName, extractorName);
    }
    
    @Override
    public List<WordVector> run(int opt)
    {
        List<WordVector> result = new ArrayList<>();
        frequentWords = getWordList(opt);
        extractVectors(opt, result);
        
        return result;
    }
    
    @Override
    public List<WordVector> runWithoutRepetition(int opt)
    {
        List<WordVector> result = new ArrayList<>();
        List<String> frequentRepeatedWords = getWordList(opt);
        frequentWords = removeRepetition(frequentRepeatedWords);
        extractVectors(opt, result);
        
        return result;
    }
    
    private List<String> getWordList(int opt)
    {
        frequentWords = new ArrayList<>();
        
        List<Article> list1 = new ArrayList<>();
        List<Article> list2 = new ArrayList<>();
        List<Article> list3 = new ArrayList<>();
        List<Article> list4 = new ArrayList<>();
        List<Article> list5 = new ArrayList<>();
        List<Article> list6 = new ArrayList<>();
        
        if(opt == 0)
        {
            for(Article art : articles)
            {
                String test = checkPlace(art);
                if(test != null)
                {
                    switch(test)
                    {
                        case "west germany":
                            list1.add(art);
                            break;
                        case "usa":
                            list2.add(art);
                            break;
                        case "france":
                            list3.add(art);
                            break;
                        case "uk":
                            list4.add(art);
                            break;
                        case "canada":
                            list5.add(art);
                            break;
                        case "japan":
                            list6.add(art);
                            break;
                    }
                }
            }
        }
        else if(opt == 1)
        {
            for(Article art : articles)
            {
                String test = checkTopic(art);
                if(test != null)
                {
                    switch(test)
                    {
                        case "earn":
                            list1.add(art);
                            break;
                        case "acq":
                            list2.add(art);
                            break;
                        case "crude":
                            list3.add(art);
                            break;
                        case "trade":
                            list4.add(art);
                            break;
                        case "money fx":
                            list5.add(art);
                            break;
                        case "interest":
                            list6.add(art);
                            break;
                    }
                }
            }
        }
        
        FreqListBuilder flb = new FreqListBuilder();
        frequentWords.addAll(flb.getWordList(list1, vectorLength, opt));
        frequentWords.addAll(flb.getWordList(list2, vectorLength, opt));
        frequentWords.addAll(flb.getWordList(list3, vectorLength, opt));
        frequentWords.addAll(flb.getWordList(list4, vectorLength, opt));
        frequentWords.addAll(flb.getWordList(list5, vectorLength, opt));
        frequentWords.addAll(flb.getWordList(list6, vectorLength, opt));
        
        return frequentWords;
    }
    
    private List<String> removeRepetition(List<String> inputList)
    {
        List<String> result = new ArrayList<>();
        
        for(String word : inputList)
        {
            if(!result.contains(word))
            {
                result.add(word);
            }
        }
        
        return result;
    }
}
