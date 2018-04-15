package logic.preprocessing;

import logic.model.entity.Article;
import logic.preprocessing.filtering.IrregularVerbsFilter;
import logic.utils.WordRemoval;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.hunspell.Dictionary;
import org.apache.lucene.analysis.hunspell.HunspellStemFilter;
import org.apache.lucene.analysis.pattern.PatternReplaceFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.store.RAMDirectory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ArticlePreprocessor
{
    public List<Article> removeStopWords(List<Article> entities)
    {
        List<Article> result = new ArrayList<>();
        for(Article entity : entities)
        {
            StandardTokenizer tokenizer = new StandardTokenizer();
            CharTermAttribute attr = tokenizer.addAttribute(CharTermAttribute.class);
            TokenStream tokenStream = new StopFilter(tokenizer, StopFilter.makeStopSet(WordRemoval.readFromFileStopWords()));
            tokenizer.setReader(new StringReader(entity.getContent()));
            StringBuilder sb = new StringBuilder();
            try
            {
                tokenizer.reset();
                while(tokenStream.incrementToken())
                {
                    sb.append(attr.toString()).append(' ');
                }
            }
            catch(IOException e)
            {
                throw new RuntimeException("Error while removing stop words", e);
            }
            sb.deleteCharAt(sb.length() - 1);
            Article newEntity = new Article(entity.getLabel(), sb.toString());
            result.add(newEntity);
        }
        return result;
    }
    
    public List<Article> extractStems(List<Article> entities, String affixPath, String dictPath)
    {
        List<Article> result = new ArrayList<>();
        FileInputStream affixFileInputStream, dictionaryFileInputStream;
        try
        {
            affixFileInputStream = new FileInputStream(affixPath);
        }
        catch(FileNotFoundException e)
        {
            throw new RuntimeException(
                "Error while reading hunspell dictionary affix file: " + affixPath,
                e
            );
        }
        try
        {
            dictionaryFileInputStream = new FileInputStream(dictPath);
        }
        catch(FileNotFoundException e)
        {
            throw new RuntimeException("Error while reading hunspell dictionary file: " + dictPath, e);
        }
        
        Dictionary dictionary;
        try
        {
            dictionary = new Dictionary(new RAMDirectory(), "tmp", affixFileInputStream, dictionaryFileInputStream);
        }
        catch(IOException | ParseException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Error while constructing new Dictionary");
        }
        StandardTokenizer tokenizer = new StandardTokenizer();
        CharTermAttribute attr = tokenizer.addAttribute(CharTermAttribute.class);
        TokenStream tokenStream = new HunspellStemFilter(tokenizer, dictionary, true, true);
        for(Article entity : entities)
        {
            StringBuilder sb = new StringBuilder();
            Scanner contentScanner = new Scanner(entity.getContent());
            while(contentScanner.hasNext())
            {
                StringReader singleWordReader = new StringReader(contentScanner.next());
                tokenizer.setReader(singleWordReader);
                try
                {
                    tokenizer.reset();
                    ArrayList<String> stems = new ArrayList<>();
                    while(tokenStream.incrementToken())
                    {
                        stems.add(attr.toString());
                    }
                    String stem = stems.stream().min(Comparator.comparing(String::length)).orElse("");
                    sb.append(stem).append(" ");
                    tokenStream.close();
                }
                catch(IOException e)
                {
                    throw new RuntimeException("Error while extracting stems", e);
                }
            }
            
            sb.deleteCharAt(sb.length() - 1);
            Article newEntity = new Article(entity.getLabel(), sb.toString());
            result.add(newEntity);
        }
        return result;
    }
    
    public List<Article> removeNumbers(List<Article> entities)
    {
        List<Article> result = new ArrayList<>();
        StandardTokenizer tokenizer = new StandardTokenizer();
        CharTermAttribute attr = tokenizer.addAttribute(CharTermAttribute.class);
        TokenStream tokenStream = new PatternReplaceFilter(tokenizer, Pattern.compile("-?\\d+\\.?\\d*\\$?"), "", true);
        entities.forEach(entity -> {
            StringBuilder sb = new StringBuilder();
            StringReader reader = new StringReader(entity.getContent());
            tokenizer.setReader(reader);
            try
            {
                tokenizer.reset();
                while(tokenStream.incrementToken())
                {
                    sb.append(attr.toString()).append(" ");
                }
                tokenStream.close();
            }
            catch(IOException e)
            {
                throw new RuntimeException("Error while removing numbers", e);
            }
            sb.deleteCharAt(sb.length() - 1);
            Article newEntity = new Article(entity.getLabel(), sb.toString());
            result.add(newEntity);
        });
        return result;
    }
    
    public List<Article> replaceIrregularVerbsWithInfinitives(List<Article> entities)
    {
        List<Article> result = new ArrayList<>();
        StandardTokenizer tokenizer = new StandardTokenizer();
        CharTermAttribute attr = tokenizer.addAttribute(CharTermAttribute.class);
        TokenStream tokenStream = new IrregularVerbsFilter(tokenizer);
        
        entities.forEach(entity -> {
            StringBuilder sb = new StringBuilder();
            StringReader reader = new StringReader(entity.getContent());
            tokenizer.setReader(reader);
            try
            {
                tokenizer.reset();
                while(tokenStream.incrementToken())
                {
                    sb.append(attr.toString()).append(" ");
                }
                tokenStream.close();
            }
            catch(IOException e)
            {
                throw new RuntimeException("Error while replacing irregular verbs with infinitives", e);
            }
            sb.deleteCharAt(sb.length() - 1);
            Article newEntity = new Article(entity.getLabel(), sb.toString());
            result.add(newEntity);
        });
        return result;
    }
}
