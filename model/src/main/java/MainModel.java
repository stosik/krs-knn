import logic.model.Article;
import logic.parser.XmlParser;
import logic.sgm.SgmReader;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class MainModel
{
    
    public static void main(String[] args)
    {
        XmlParser parser = new XmlParser();
        File xmlDir = new File("./model/data/text/xml/");
        List<Article> articles = parser.parseDir(xmlDir);
        articles
            .stream()
            .map(Article::getPlaces)
            .filter(name -> name.contains("west-germany"))
            .collect(Collectors.toList())
            .forEach(System.out::println);
    }
}
