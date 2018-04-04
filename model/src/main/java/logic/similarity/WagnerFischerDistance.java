package logic.similarity;

public class WagnerFischerDistance extends SimilarityDistance
{
    public WagnerFischerDistance()
    {
        maxDistance = 5;
    }
    
    @Override
    public double getDistance(String text1, String text2)
    {
        int m = text1.length();
        int n = text2.length();
        int d[][] = new int[m + 1][n + 1];
        
        for(int i = 0; i <= m; i++)
        {
            d[i][0] = i;
        }
        for(int j = 0; j <= n; j++)
        {
            d[0][j] = j;
        }
        for(int j = 1; j <= n; j++)
        {
            for(int i = 1; i <= m; i++)
            {
                if(text1.charAt(i - 1) == text2.charAt(j - 1))
                {
                    d[i][j] = d[i - 1][j - 1];
                }
                else
                {
                    d[i][j] = Math.min(Math.min(d[i - 1][j] + 1, d[i][j - 1] + 1), d[i - 1][j - 1] + 1);
                }
            }
        }
        
        return d[m][n];
    }
}
