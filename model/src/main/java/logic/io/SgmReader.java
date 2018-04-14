package logic.io;

import logic.io.parser.SgmParser;
import logic.model.entity.Article;
import logic.model.enums.Place;
import logic.model.enums.Topic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SgmReader
{
    private final SgmParser sgmParser = new SgmParser();
    private final Set<String> filters = new HashSet<>();
    private static final String REUTERS_DIR = "./model/data/text/sgm";
    
    public SgmReader(String label)
    {
        loadFiltersFromFile(label);
    }
    
    private void loadFiltersFromFile(String label)
    {
        if(label.equals("topics"))
        {
            Arrays.stream(Topic.values())
                  .map(Topic::getTopic)
                  .forEach(filters::add);
        }
        else if(label.equals("places"))
        {
            Arrays.stream(Place.values())
                  .map(Place::getPlace)
                  .forEach(filters::add);
        }
    }
    
    public List<Article> loadReutersEntities()
    {
        List<Article> entities = new ArrayList<>();
        try
        {
            Files.walk(Paths.get(REUTERS_DIR))
                 .filter(Files::isRegularFile)
                 .map(Path::toFile)
                 .forEach(reuterFile -> entities.addAll(sgmParser.parse(reuterFile)));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    
        if(filters.size() > 0)
        {
            return entities
                .stream()
                .filter(e -> filters.contains(e.getLabel()))
                .collect(Collectors.toList());
        }
        else
        {
            return entities;
        }
    }
}