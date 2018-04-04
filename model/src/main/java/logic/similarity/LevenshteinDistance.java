package logic.similarity;

public class LevenshteinDistance extends SimilarityDistance
{
    public LevenshteinDistance()
    {
        maxDistance = 6;
    }
    
    @Override
    public double getDistance(String text1, String text2)
    {
        if(text1.length() == 0)
        {
            return text2.length();
        }
        else if(text2.length() == 0)
        {
            return text1.length();
        }
        else
        {
            int[] v0 = new int[text2.length() + 1];
            int[] v1 = new int[text2.length() + 1];
            
            for(int i = 0; i < v0.length; i++)
            {
                v0[i] = i;
            }
            
            for(int i = 0; i < text1.length(); i++)
            {
                v1[0] = i + 1;
                
                for(int j = 0; j < text2.length(); j++)
                {
                    int cost = (text1.charAt(i) == text2.charAt(j)) ? 0 : 1;
                    v1[j + 1] = Math.min(Math.min(v1[j] + 1, v0[j + 1] + 1), v0[j] + cost);
                }
                System.arraycopy(v1, 0, v0, 0, v0.length);
            }
            
            return v1[text2.length()];
        }
    }
}
