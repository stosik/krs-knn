package logic.utils;

import lombok.NoArgsConstructor;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@NoArgsConstructor
public class PropertiesUtils
{
    public static Properties load(String path)
    {
        Properties properties = new Properties();
        try
        {
            properties.load(new FileInputStream(path));
        }
        catch(IOException e)
        {
            throw new RuntimeException("Properties file not found or not accessible");
        }
        return properties;
    }
}
