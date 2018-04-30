package logic.preprocessing.filtering;

import lombok.SneakyThrows;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

public class IrregularVerbsFilter extends TokenFilter
{
    private static final String HYPHEN = "-";
    private static final String IRREGULAR_VERBS_FILE = "./model/dict/irregular-verbs.txt";
    private static final Map<String, String> dictionary = initializeDictionary();
    
    private final CharTermAttribute termAtt;
    
    public IrregularVerbsFilter(TokenStream input)
    {
        super(input);
        termAtt = input.getAttribute(CharTermAttribute.class);
    }
    
    @SneakyThrows
    private static Map<String, String> initializeDictionary()
    {
        Path path = FileSystems.getDefault().getPath(IRREGULAR_VERBS_FILE);
        Map<String, String> dictionary;
        
        dictionary = Files
            .lines(path)
            .filter(s -> s.matches("^\\w+-\\w+"))
            .collect(Collectors.toMap(k -> k.split(HYPHEN)[0], v -> v.split(HYPHEN)[1]));
        
        return dictionary;
    }
    
    @Override
    public boolean incrementToken() throws IOException
    {
        if(input.incrementToken())
        {
            char[] buffer = termAtt.buffer();
            int length = termAtt.length();
            char[] tokenCharArr = new char[length];
            System.arraycopy(buffer, 0, tokenCharArr, 0, length);
            String token = new String(tokenCharArr);
            if(dictionary.containsKey(token))
            {
                token = dictionary.get(token);
                tokenCharArr = token.toCharArray();
                System.arraycopy(tokenCharArr, 0, buffer, 0, token.length());
                termAtt.setLength(token.length());
            }
            return true;
        }
        return false;
    }
}
