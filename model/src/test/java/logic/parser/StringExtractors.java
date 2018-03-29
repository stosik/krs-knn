package logic.parser;

import logic.utils.WordRemoval;
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
    
    private static final String BODY = "Showers continued throughout the week in\n" +
                                       "the Bahia cocoa zone, alleviating the drought since early\n" +
                                       "January and improving prospects for the coming temporao,\n" +
                                       "although normal humidity levels have not been restored,\n" +
                                       "Comissaria Smith said in its weekly review.\n" +
                                       "    The dry period means the temporao will be late this year.\n" +
                                       "    Arrivals for the week ended February 22 were 155,221 bags\n" +
                                       "of 60 kilos making a cumulative total for the season of 5.93\n" +
                                       "mln against 5.81 at the same stage last year. Again it seems\n" +
                                       "that cocoa delivered earlier on consignment was included in the\n" +
                                       "arrivals figures.\n" +
                                       "    Comissaria Smith said there is still some doubt as to how\n" +
                                       "much old crop cocoa is still available as harvesting has\n" +
                                       "practically come to an end. With total Bahia crop estimates\n" +
                                       "around 6.4 mln bags and sales standing at almost 6.2 mln there\n" +
                                       "are a few hundred thousand bags still in the hands of farmers,\n" +
                                       "middlemen, exporters and processors.\n" +
                                       "    There are doubts as to how much of this cocoa would be fit\n" +
                                       "for export as shippers are now experiencing dificulties in\n" +
                                       "obtaining +Bahia superior+ certificates.\n" +
                                       "    In view of the lower quality over recent weeks farmers have\n" +
                                       "sold a good part of their cocoa held on consignment.\n" +
                                       "    Comissaria Smith said spot bean prices rose to 340 to 350\n" +
                                       "cruzados per arroba of 15 kilos.\n" +
                                       "    Bean shippers were reluctant to offer nearby shipment and\n" +
                                       "only limited sales were booked for March shipment at 1,750 to\n" +
                                       "1,780 dlrs per tonne to ports to be named.\n" +
                                       "    New crop sales were also light and all to open ports with\n" +
                                       "June/July going at 1,850 and 1,880 dlrs and at 35 and 45 dlrs\n" +
                                       "under New York july, Aug/Sept at 1,870, 1,875 and 1,880 dlrs\n" +
                                       "per tonne FOB.\n" +
                                       "    Routine sales of butter were made. March/April sold at\n" +
                                       "4,340, 4,345 and 4,350 dlrs.\n" +
                                       "    April/May butter went at 2.27 times New York May, June/July\n" +
                                       "at 4,400 and 4,415 dlrs, Aug/Sept at 4,351 to 4,450 dlrs and at\n" +
                                       "2.27 and 2.28 times New York Sept and Oct/Dec at 4,480 dlrs and\n" +
                                       "2.27 times New York Dec, Comissaria Smith said.\n" +
                                       "    Destinations were the U.S., Covertible currency areas,\n" +
                                       "Uruguay and open ports.\n" +
                                       "    Cake sales were registered at 785 to 995 dlrs for\n" +
                                       "March/April, 785 dlrs for May, 753 dlrs for Aug and 0.39 times\n" +
                                       "New York Dec for Oct/Dec.\n" +
                                       "    Buyers were the U.S., Argentina, Uruguay and convertible\n" +
                                       "currency areas.\n" +
                                       "    Liquor sales were limited with March/April selling at 2,325\n" +
                                       "and 2,380 dlrs, June/July at 2,375 dlrs and at 1.25 times New\n" +
                                       "York July, Aug/Sept at 2,400 dlrs and at 1.25 times New York\n" +
                                       "Sept and Oct/Dec at 1.25 times New York Dec, Comissaria Smith\n" +
                                       "said.\n" +
                                       "    Total Bahia sales are currently estimated at 6.13 mln bags\n" +
                                       "against the 1986/87 crop and 1.06 mln bags against the 1987/88\n" +
                                       "crop.\n" +
                                       "    Final figures for the period to February 28 are expected to\n" +
                                       "be published by the Brazilian Cocoa Trade Commission after\n" +
                                       "carnival which ends midday on February 27.\n" +
                                       " Reuter";
    
    private static final String ALPHANUMERIC =  "Liquor sales were limited with March/April selling at 2,325\n" +
                                                "and 2,380 dlrs, June/July at 2,375 dlrs and at 1.25 times New\n" +
                                                "York July, Aug/Sept at 2,400 dlrs and at 1.25 times New York\n" +
                                                "Sept and Oct/Dec at 1.25 times New York Dec, Comissaria Smith\n" +
                                                "said.\n";
    
    @Test
    public void removeStopWords() throws Exception
    {
        StandardTokenizer stdToken = new StandardTokenizer();
        stdToken.setReader(new StringReader(BODY));
        TokenStream tokenStream = new StopFilter(
            new ASCIIFoldingFilter
                (new ClassicFilter
                     (new LowerCaseFilter(stdToken))),
            EnglishAnalyzer.getDefaultStopSet()
        );
        tokenStream.reset();
        CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
        while(tokenStream.incrementToken())
        {
            System.out.println(token.toString());
        }
        tokenStream.close();
    }
    
    @Test
    public void removeNonAlphabetStrings()
    {
        WordRemoval.removeNumericCharacters(ALPHANUMERIC);
    }
}
