package logic.extractors;

import logic.model.Article;
import logic.similarity.SimilarityDistance;

public class WordCountExtractor extends Extractor
{
    public WordCountExtractor(SimilarityDistance distance)
    {
        super(distance);
    }
    
    @Override
    public Double getValue(Article art, String word_pattern)
    {
        return null;
    }
}
