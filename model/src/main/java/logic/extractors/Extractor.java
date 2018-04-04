package logic.extractors;

import logic.model.Article;
import logic.similarity.SimilarityDistance;

public abstract class Extractor
{
    protected SimilarityDistance distance;
    
    public Extractor(SimilarityDistance distance)
    {
        this.distance = distance;
    }
    
    public abstract Double getValue(Article art, String word_pattern);
}
