package logic.parser;

import jdk.nashorn.internal.ir.annotations.Ignore;
import logic.model.Article;
import logic.sgm.SgmReader;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.junit.Assert;
import org.junit.Test;

import javax.swing.text.html.parser.DTD;
import javax.swing.text.html.parser.Parser;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class JsoupTests
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
    public void testSGMToXML()
    {
        new SgmReader().getData(new File("./data/text/sgm"), "./data/text/xml/");
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
