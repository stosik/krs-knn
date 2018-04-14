package logic.utils;

import logic.io.SgmReader;
import logic.io.XmlWriter;
import logic.model.entity.Article;

import java.io.File;
import java.util.List;

public class FileUtils
{
    
    public static List<Article> loadReutersData(String label)
    {
        SgmReader sgmReader = new SgmReader(label);
        return sgmReader.loadReutersEntities();
    }
    
    public static void saveReutersDataToFile(List<Article> entities, String path)
    {
        XmlWriter xmlSerializer = new XmlWriter();
        xmlSerializer.serializeToFile(entities, new File(path));
    }
    
    public static List<Article> loadXmlData(String path)
    {
        XmlWriter xmlSerializer = new XmlWriter();
        return xmlSerializer.deserializeFromFile(new File(path));
    }
}
