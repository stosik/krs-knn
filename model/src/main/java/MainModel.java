import logic.model.Article;
import logic.parser.XmlParser;

import java.io.File;
import java.util.List;

public class MainModel
{
    
    public static void main(String[] args)
    {
        XmlParser parser = new XmlParser();
        File xmlDir = new File("data/text/xml");
        List<Article> articles = parser.parseDir(xmlDir);
        System.out.println(articles.size());
    }
}
