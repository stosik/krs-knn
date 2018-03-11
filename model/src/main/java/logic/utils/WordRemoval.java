package logic.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordRemoval
{
    
    private static Pattern defaultWordsToRemove = Pattern.compile(
        "[ ](?:an|a|and|about|the|are|is|not|have|has|had|of|it|in|mln|said|for|will|its|with|from|that|was|would|which|this|last|REUTER)\\b\\.?",
        Pattern.CASE_INSENSITIVE
                                                                 );
    
    private static Pattern invalidWordsToRemove = Pattern.compile("([&]*[#]*[;]*[]*[]*)");
    
    private static Pattern invalidWordsToRemve1 = Pattern.compile("[^a-zA-Z\\s]");
    
    public static String removeDefaultWords(String text)
    {
        Matcher matcher = defaultWordsToRemove.matcher(text);
        return matcher.replaceAll("");
    }
    
    public static String removeWordsByPattern(String text, Pattern pattern)
    {
        Matcher matcher = pattern.matcher(text);
        return matcher.replaceAll("");
    }
    
    public static String removeInvalidWords(String text)
    {
        Matcher matcher = invalidWordsToRemove.matcher(text);
        return matcher.replaceAll("");
    }
    
    public static String removeInvalidWords1(String text)
    {
        Matcher matcher = invalidWordsToRemve1.matcher(text);
        return matcher.replaceAll(" ");
    }
}