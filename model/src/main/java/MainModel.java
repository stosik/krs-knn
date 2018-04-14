import logic.model.entity.Article;
import logic.utils.FileUtils;
import logic.utils.PreprocessUtils;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainModel
{
    private static final String DICTIONARY_PATH = "./model/dict/en_US-chromium/en_US.dic";
    private static final String AFFIX_PATH = "./model/dict/en_US-chromium/en_US.aff";
    private static final String OUTPUT_PATH = "./model/data/text/reuters.xml";
    private static final String INPUT_PATH = "./model/data/text/reuters.xml";
    
    public static void main(String[] args)
    {
        List<Article> entities = FileUtils.loadReutersData("topics");
        entities = PreprocessUtils.preprocessTextEntities(entities, AFFIX_PATH, DICTIONARY_PATH);
        Collections.shuffle(entities, new Random(System.nanoTime()));
        FileUtils.saveReutersDataToFile(entities, OUTPUT_PATH);
        List<Article> articles = FileUtils.loadXmlData(INPUT_PATH);
    }
}
