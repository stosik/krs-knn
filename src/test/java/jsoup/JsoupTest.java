package jsoup;

import logic.sgm.SgmReader;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javax.swing.text.html.parser.DTD;
import javax.swing.text.html.parser.Parser;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsoupTest
{
    
    private SgmReader xmlReader = new SgmReader();
    
    @Test
    @Ignore
    public void parseTest()
    {
        try
        {
            File file = new File("../ksr/src/test/resources/reut2-000.sgm");
            DTD dtd = DTD.getDTD("../ksr/src/test/resources/lewis.dtd");
            FileReader fileReader = new FileReader(file);
            Assert.assertNotNull(dtd);
            Assert.assertTrue(file.exists());
            Assert.assertNotNull(fileReader);
            
            Parser parser = new Parser(dtd);
            System.out.println("");
        }
        catch(IOException e)
        {
            Assert.fail("as");
        }
    }
    
    @Test
    @Ignore
    public void jsoup()
    {
        File file = new File("../ksr/src/test/resources/reut2-000.sgm");
        try
        {
            Document document = Jsoup.parse(file, "UTF-8");
            
            Elements elements = document.select("REUTERS");
            
            for(Element element : elements)
            {
                
                for(org.jsoup.nodes.Node child : element.childNodes())
                {
                    if(child instanceof Element && child.childNodeSize() == 1)
                    {
                    
                    }
                    else if(child instanceof Element && child.childNodeSize() > 1)
                    {
                    
                    }
                }
            }
            
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testSGMToXML()
    {
        new SgmReader().getData(new File("./data/sgm"), "data/xml/");
    }
    
    @Test
    @Ignore
    public void regexpTests()
    {
        String text = "F Y &#22;&#22;&#1;f0708&#31;reute d f BC-STANDARD-OIL-&lt;SRD&gt;-TO 02-26 0082";
        Pattern defaultWordsToRemove = Pattern.compile("([&]*[#]*[;]*)|(?:an|a|and|about|the|are|is|not|have|has|had|of|it|in|mln|\\d)\\b\\.?");
        Matcher matcher = defaultWordsToRemove.matcher(text);
    }
    
    @Test
    @Ignore
    public void xmlRead()
    {
        try
        {
            SAXReader reader = new SAXReader();
            org.dom4j.Document document = reader.read(new File("reut2-000.xml"));
        }
        catch(DocumentException e)
        {
            e.printStackTrace();
        }
    }
    
}

