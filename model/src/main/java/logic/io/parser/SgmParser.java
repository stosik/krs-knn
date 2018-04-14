package logic.io.parser;

import logic.model.entity.Article;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SgmParser implements Parser
{
    private final XmlParser xmlReutersParser = new XmlParser();
    
    @Override
    public List<Article> parse(File file)
    {
        String xmlString = convertSgmFileToXmlString(file);
        return xmlReutersParser.parse(xmlString);
    }
    
    @Override
    public List<Article> parse(String string)
    {
        throw new UnsupportedOperationException();
    }
    
    private String convertSgmFileToXmlString(File sgmFile)
    {
        try
        {
            String sgmString = FileUtils.readFileToString(sgmFile, "UTF-8");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<ROOT>");
            stringBuilder.append(sgmString.substring(sgmString.indexOf('\n') + 1));
            stringBuilder.append("</ROOT>");
            deleteInvalidCharacters(stringBuilder);
            return stringBuilder.toString();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    private void deleteInvalidCharacters(StringBuilder stringBuilder)
    {
        int ampersandFound = stringBuilder.indexOf("&#");
        while(ampersandFound != -1)
        {
            int semicolonFound = stringBuilder.indexOf(";", ampersandFound);
            stringBuilder.delete(ampersandFound, semicolonFound + 1);
            ampersandFound = stringBuilder.indexOf("&#", ampersandFound);
        }
    }
}
