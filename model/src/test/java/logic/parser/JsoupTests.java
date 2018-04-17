package logic.parser;

import jdk.nashorn.internal.ir.annotations.Ignore;
import logic.io.SgmReader;
import org.junit.Assert;
import org.junit.Test;

import javax.swing.text.html.parser.DTD;
import javax.swing.text.html.parser.Parser;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JsoupTests
{
    @Test
    @Ignore
    public void parseTest()
    {
        try
        {
            File file = new File("../ksr/src/test/resources/reut2-000.io");
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
    
}
