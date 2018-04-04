package logic.similarity;

public class HammingDistance extends SimilarityDistance
{
    public HammingDistance()
    {
        maxDistance = 5;
    }
    
    @Override
    public double getDistance(String text1, String text2)
    {
        if(text1.length() != text2.length())
        {
            return -1.0;
        }
        else if(text1.compareTo(text2) == 0)
        {
            return 0.0;
        }
        else
        {
            double count = 0.0;
            for(int i = 0; i < text1.length(); i++)
            {
                if(text1.charAt(i) != text2.charAt(i))
                {
                    count++;
                }
            }
            return count;
        }
    }
}
