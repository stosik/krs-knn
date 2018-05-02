package logic.utils;

import logic.io.SgmReader;
import logic.io.XmlWriter;
import logic.model.entity.Article;
import logic.model.entity.NumberVector;
import lombok.SneakyThrows;
import lombok.val;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FileUtils
{
    
    private static final String COMMA = ",";
    
    public static List<Article> loadReutersData(String label)
    {
        SgmReader sgmReader = new SgmReader(label);
        return sgmReader.loadReutersEntities(label);
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
    
    @SneakyThrows
    public static List<NumberVector> loadNumbers(String path)
    {
        List<NumberVector> numberVectors = new ArrayList<>();
        extractLines(path).forEach(createNextEntry(numberVectors));
        
        return numberVectors;
    }
    
    private static Consumer<List<String>> createNextEntry(List<NumberVector> numberVectors)
    {
        return line -> {
            val vector = IntStream
                .range(1, line.size())
                .mapToObj(j -> Double.valueOf(line.get(j)))
                .collect(Collectors.toList());
            
            numberVectors.add(new NumberVector(line.get(0), vector));
        };
    }
    
    @SneakyThrows
    private static List<List<String>> extractLines(String path)
    {
        try(Stream<String> stream = Files.lines(Paths.get(path)))
        {
            return stream
                .map(string -> string.split(COMMA))
                .map(Arrays::asList)
                .collect(Collectors.toList());
        }
    }
}
