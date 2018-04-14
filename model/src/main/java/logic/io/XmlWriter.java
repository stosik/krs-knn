package logic.io;

import logic.model.entity.Article;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlWriter
{
    public void serializeToFile(List<Article> entities, File file)
    {
        Element root = new Element("ROOT");
        Document document = new Document();
        for(Article entity : entities)
        {
            Element labelElement = new Element("LABEL");
            labelElement.addContent(entity.getLabel());
            Element contentElement = new Element("CONTENT");
            contentElement.addContent(entity.getContent());
            Element entityElement = new Element("ENTITY");
            entityElement.addContent(labelElement);
            entityElement.addContent(contentElement);
            root.addContent(entityElement);
        }
        document.setRootElement(root);
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.setFormat(Format.getPrettyFormat());
        try
        {
            xmlOutputter.output(document, new FileWriter(file));
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public List<Article> deserializeFromFile(File file)
    {
        List<Article> entities = new ArrayList<>();
        SAXBuilder saxBuilder = new SAXBuilder();
        try
        {
            Document document = saxBuilder.build(file);
            Element rootXML = document.getRootElement();
            List<Element> entityElements = rootXML.getChildren();
            for(Element entityElement : entityElements)
            {
                Element labelElement = entityElement.getChild("LABEL");
                Element contentElement = entityElement.getChild("CONTENT");
                String label = labelElement.getValue();
                String content = contentElement.getValue();
                if(!label.isEmpty() && !content.isEmpty())
                {
                    entities.add(new Article(label, content));
                }
            }
            return entities;
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
