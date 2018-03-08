package logic.sgm;

import logic.utils.WordRemoval;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SgmReader
{
    
    public void getData(File dir, String destination)
    {
        List<File> files = Arrays.asList(Objects.requireNonNull(dir.listFiles()));

        for(File file : files)
        {
            Document sgm = readSgm(file);
            org.dom4j.Document xml = createXmlBase();
            
            Elements elements = Objects.requireNonNull(sgm).select("REUTERS");
            for(Element element : elements)
            {
                elementExplorer(element, xml.getRootElement());
            }
            
            try
            {
                XMLWriter writer;
                OutputFormat format = OutputFormat.createPrettyPrint();
                format.setEncoding("UTF-8");
                FileOutputStream fos = new FileOutputStream(destination + "/" + file.getName().substring(0, file.getName().lastIndexOf('.')) + ".xml");
                writer = new XMLWriter(fos, format);
                writer.write(xml);
                writer.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    private static Document readSgm(File file)
    {
        try
        {
            return Jsoup.parse(file, "UTF-8");
        }
        catch(IOException e)
        {
            return null;
        }
    }
    
    private static void elementExplorer(Element element, org.dom4j.Element xml)
    {
        org.dom4j.Element xmlElement = xml.addElement(element.tagName());
        
        for(org.jsoup.nodes.Node child : element.childNodes())
        {
            if(child instanceof Element && (child.childNodeSize() == 1 || child.childNodeSize() == 0))
            {
                org.dom4j.Element newXmlElement = xmlElement.addElement(((Element) child).tagName());
                newXmlElement.addText(WordRemoval.removeInvalidWords1(((Element) child).text()));
            }
            else if(child instanceof Element && child.childNodeSize() > 1)
            {
                elementExplorer(((Element) child), xmlElement);
            }
            else if(child instanceof TextNode && ((Element) child.parent()).tagName().equalsIgnoreCase("TEXT") && !((TextNode) child).isBlank() && ((TextNode) child).text().length() > 11)
            {
                xmlElement.addElement("body").addText(WordRemoval.removeInvalidWords1(WordRemoval.removeDefaultWords(((TextNode) child).text())));
            }
        }
    }
    
    private static org.dom4j.Document createXmlBase()
    {
        org.dom4j.Document document = DocumentHelper.createDocument();
        org.dom4j.Element root = document.addElement("root");
        return document;
    }
}