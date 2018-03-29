package logic.parser;

import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;

import java.io.StringReader;

public class StringExtractors
{
    
    @Test
    public void removeStopWords() throws Exception
    {
        StandardTokenizer stdToken = new StandardTokenizer();
        stdToken.setReader(new StringReader("Some stuff that is in need of analysis"));
        TokenStream tokenStream = new StopFilter(new ASCIIFoldingFilter
                                                     (new ClassicFilter
                                                          (new LowerCaseFilter(stdToken))),
                                                 EnglishAnalyzer.getDefaultStopSet());
        tokenStream.reset();
        CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
        while(tokenStream.incrementToken())
        {
            System.out.println(token.toString());
        }
        tokenStream.close();
    }
}
