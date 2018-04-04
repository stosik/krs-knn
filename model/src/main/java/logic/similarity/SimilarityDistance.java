package logic.similarity;

public abstract class SimilarityDistance
{
    protected static int maxDistance;
    
    public SimilarityDistance() {
        maxDistance = 1;
    }
    
    public boolean compare(String word1, String word2) {
        double distance = getDistance(word1, word2);
        return distance < maxDistance;
    }
    
    abstract double getDistance(String text1, String text2);
}
