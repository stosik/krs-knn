package logic.parser;

import logic.model.Article;
import lombok.NoArgsConstructor;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
public class XmlParser
{
    public List<Article> parseDir(File dir)
    {
        List<Article> articlesList = new ArrayList<>();
        List<File> files = Arrays.asList(Objects.requireNonNull(dir.listFiles()));
        
        for(File file : files)
        {
            articlesList.addAll(Objects.requireNonNull(parse(file)));
        }
        return articlesList;
    }
    
    private List<Article> parse(File file)
    {
        List<Article> articlesList = new ArrayList<>();
        SAXReader reader = new SAXReader();
        
        try
        {
            Document data = reader.read(file);
            Element root = data.getRootElement();
            List<Element> articles = root.elements("reuters");
            
            for(Element e : articles)
            {
                Article a = new Article();
                if(e.element("text").element("body") != null)
                {
                    a.setBody(e.element("text").element("body").getText().toLowerCase());
                }
                else
                {
                    continue;
                }
                a.setTopics(getList(e.elements("topics")));
                a.setPlaces(getList(e.elements("places")));
                a.setPeople(getList(e.elements("people")));
                if(e.element("text").element("title") != null)
                {
                    a.setTextTitle(e.element("text").element("title").getText().toLowerCase());
                }
                articlesList.add(a);
            }
        }
        catch(Exception ex)
        {
            return null;
        }
        
        return articlesList;
    }
    
    private List<String> getList(List<Element> listOfElements)
    {
        List<String> list = new ArrayList<String>();
        if(!listOfElements.isEmpty())
        {
            if((listOfElements.get(0).elements("d").size() == 0))
            {
                list.add(listOfElements.get(0).getText());
            }
            else
            {
                List<Element> dlist = listOfElements.get(0).elements("d");
                for(Element e : dlist)
                {
                    list.add(e.getText());
                }
            }
        }
        return list;
    }
    
    public List<Article> parseFilesFromDirectory(File dir)
    {
        List<File> files = Arrays
            .asList(Objects.requireNonNull(dir.listFiles((dir1, name) -> name.endsWith(".xml"))));
        List<Article> articles = new ArrayList<>();
        
        for(File f : files)
        {
            articles.addAll(Objects.requireNonNull(parse(f)));
        }
        
        return articles;
    }
}