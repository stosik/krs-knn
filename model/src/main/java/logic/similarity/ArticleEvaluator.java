package logic.similarity;

public class ArticleEvaluator
{
    private SimilarityDistance similarityDistance;
    
    public enum SimilarityMeasureType
    {
        LEVENSHTEIN, WAGNERFISCHER
    }
    
    public ArticleEvaluator(SimilarityMeasureType similarityMeasureType)
    {
        this.similarityDistance = getSimilarityMeasure(similarityMeasureType);
    }
    
    public double comptuteDissimilarity(String text1, String text2)
    {
        int result = 0;
        
        String[] text1Words = text1.split(" ");
        String[] text2Words = text2.split(" ");
        
        result += similarityDistance.getDistance(text1, text2);
        
        return 1 - (double) result / (2 * Math.max(text1.length(), text2.length()));
    }
    
    public double computeSimilarity(String text1, String text2)
    {
        int result = 0;
        String[] text1Words = text1.split(" ");
        String[] text2Words = text2.split(" ");
    
        for(final String text1Word : text1Words)
        {
            for(final String text2Word : text2Words)
            {
                int temp = similarityDistance.compare(text1Word, text2Word) ? 1 : 0;
                if(temp == 1)
                {
                    result++;
                    break;
                }
            }
        }
        return 1.0 / Math.max(text1Words.length, text2Words.length) * result;
    }
    
    private SimilarityDistance getSimilarityMeasure(SimilarityMeasureType measureType)
    {
        if(SimilarityMeasureType.LEVENSHTEIN == measureType)
        {
            return new LevenshteinDistance();
        }
        else if(SimilarityMeasureType.WAGNERFISCHER == measureType)
        {
            return new WagnerFischerDistance();
        }
        
        return null;
    }
}
