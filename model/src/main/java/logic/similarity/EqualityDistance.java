package logic.similarity;

public class EqualityDistance extends SimilarityDistance
{
    @Override
    public double getDistance(String text1, String text2) {
        if (text1.equals(text2)) {
            return 0.0;
        } else {
            return 1.0;
        }
    }
}
