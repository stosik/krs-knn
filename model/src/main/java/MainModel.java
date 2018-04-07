import logic.model.Article;
import logic.parser.XmlParser;
import logic.sgm.SgmReader;

import java.io.File;
import java.util.List;

public class MainModel
{
    
    public static void main(String[] args)
    {
        new SgmReader().getData(new File("data/text/sgm"), "data/text/xml/");
        XmlParser parser = new XmlParser();
        File xmlDir = new File("data/text/xml");
        List<Article> articles = parser.parseDir(xmlDir);
        System.out.println(articles.size());
    }
}
