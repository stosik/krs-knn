package logic.io.parser;

import logic.model.entity.Article;
import logic.utils.PropertiesUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class XmlParser implements Parser
{
    private static final String PROPERTIES_FILE = "./model/config/reuters.properties";
    
    private final String LABEL_OUTER_NAME;
    private final String LABEL_INNER_NAME;
    private final String CONTENT_OUTER_NAME;
    private final String CONTENT_INNER_NAME;
    
    public XmlParser(String label)
    {
        System.out.println(System.getProperty("user.dir"));
        Properties properties = PropertiesUtils.load(PROPERTIES_FILE);
        LABEL_OUTER_NAME = label.toUpperCase();
        LABEL_INNER_NAME = properties.getProperty("LABEL_INNER_NAME");
        CONTENT_OUTER_NAME = properties.getProperty("CONTENT_OUTER_NAME");
        CONTENT_INNER_NAME = properties.getProperty("CONTENT_INNER_NAME");
    }
    
    @Override
    public List<Article> parse(File file, String label)
    {
        SAXBuilder saxBuilder = new SAXBuilder();
        try
        {
            Document document = saxBuilder.build(file);
            return fillEntitiesList(document);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public List<Article> parse(String string)
    {
        InputStream inputStream = new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
        SAXBuilder saxBuilder = new SAXBuilder();
        try
        {
            Document document = saxBuilder.build(inputStream);
            return fillEntitiesList(document);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    private List<Article> fillEntitiesList(Document document)
    {
        List<Article> entities = new ArrayList<>();
        Element rootXML = document.getRootElement();
        List<Element> entityElements = rootXML.getChildren();
        for(Element entityElement : entityElements)
        {
            Element labelOuterElement = entityElement.getChild(LABEL_OUTER_NAME);
            if(labelOuterElement != null)
            {
                Element labelInnerElement;
                if(LABEL_INNER_NAME.isEmpty())
                {
                    labelInnerElement = labelOuterElement;
                }
                else
                {
                    labelInnerElement = labelOuterElement.getChild(LABEL_INNER_NAME);
                }
                if(labelInnerElement != null && labelInnerElement.getChildren().size() <= 1)
                {
                    Element contentOuterElement = entityElement.getChild(CONTENT_OUTER_NAME);
                    if(contentOuterElement != null)
                    {
                        Element contentInnerElement;
                        if(CONTENT_INNER_NAME.isEmpty())
                        {
                            contentInnerElement = contentOuterElement;
                        }
                        else
                        {
                            contentInnerElement = contentOuterElement.getChild(CONTENT_INNER_NAME);
                        }
                        
                        if(contentInnerElement != null && contentInnerElement.getChildren().size() <= 1)
                        {
                            String label = labelInnerElement.getValue();
                            String content = contentInnerElement.getValue();
                            if(!label.isEmpty() && !content.isEmpty())
                            {
                                entities.add(new Article(label, content));
                            }
                        }
                    }
                }
            }
        }
        
        return entities;
    }
}
