package logic.extractors.processors;

import logic.extractors.utils.FreqListBuilder;
import logic.model.Article;
import logic.model.WordVector;

import java.util.ArrayList;
import java.util.List;

public class UnifiedFreqListProcessor extends Processor
{
    public UnifiedFreqListProcessor(List<Article> articles, int vectorLength, String distanceName, String extractorName)
    {
        super(articles, vectorLength, distanceName, extractorName);
    }
    
    @Override
    public List<WordVector> run(int opt)
    {
        List<WordVector> result = new ArrayList<>();
        frequentWords = new FreqListBuilder().getWordList(articles, vectorLength, opt);
        extractVectors(opt, result);
        
        return result;
    }
    
    @Override
    public List<WordVector> runWithoutRepetition(int opt)
    {
        throw new UnsupportedOperationException("Not supported in this class.");
    }
}
