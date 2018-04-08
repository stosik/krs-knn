package logic.sgm;

import logic.utils.WordRemoval;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
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
    
    private static org.dom4j.Document createXmlBase()
    {
        org.dom4j.Document document = DocumentHelper.createDocument();
        document.addElement("root");
        return document;
    }
    
    private static void elementExplorer(Element element, org.dom4j.Element xml)
    {
        org.dom4j.Element xmlElement = xml.addElement(element.tagName());
        
        for(org.jsoup.nodes.Node child : element.childNodes())
        {
            if (child instanceof Element)
            {
                Element c = (Element) child;
                String tagName = c.tagName();
                String text = c.text();
                if (tagName.equals("places") || tagName.equals("topics")) {
                    org.dom4j.Element newXmlElement = xmlElement.addElement(tagName);
                    List<Node> e = c.childNodes();
                    if (e.size() != 0 && e.size() == 1) {
                        newXmlElement.addText(text);
                    } else {
                        for(Node anE : e)
                        {
                            if(anE instanceof Element)
                            {
                                org.dom4j.Element newD = newXmlElement.addElement("d");
                                newD.addText(((Element) anE).text());
                            }
                        }
                    }
                } else if (tagName.equals("text")) {
                    for (Element ch : c.children()) {
                        if (ch instanceof Element) {
                            Element maybeBody = ch;
                            if (maybeBody.tagName().equals("content")) {
                                org.dom4j.Element newXmlElement = xmlElement.addElement("body");
                                String body = WordRemoval.removeAllEnglishStopWords(WordRemoval.removeNumericCharacters(WordRemoval.removeSpecialCharacters(maybeBody.text())));
                                newXmlElement.addText(body);
                            }
                        }
                    }
                }
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
}