package logic.extractors;

import logic.model.Article;
import logic.similarity.SimilarityDistance;

public class WordFrequencyExtractor extends Extractor
{
    public WordFrequencyExtractor(SimilarityDistance distance)
    {
        super(distance);
    }
    
    @Override
    public Double getValue(Article art, String word_pattern)
    {
        return null;
    }
}
