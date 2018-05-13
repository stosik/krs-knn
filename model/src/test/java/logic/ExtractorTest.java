package logic;

import logic.classifier.KnnNumber;
import logic.classifier.KnnText;
import logic.classifier.result.ClassificationResult;
import logic.classifier.result.ResultCreator;
import logic.metrics.Distance;
import logic.metrics.distance.ChebyshevDistance;
import logic.metrics.distance.EuclideanDistance;
import logic.metrics.distance.ManhattanDistance;
import logic.metrics.similarity.CosineSimilarity;
import logic.metrics.similarity.Ngram;
import logic.model.Base;
import logic.model.entity.Article;
import logic.model.entity.NumberVector;
import logic.utils.FileUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExtractorTest
{
    
    private static final String MEASURE_NAME = "e";
    private static final int TRAINING_SET_PERCENTAGE = 70;
    private static final int K_VALUE = 3;
    
    @Test
    public void shouldLimitMostCommonOccurences()
    {
    
        List<Article> articles = FileUtils.loadReutersData("places");
        Collections.shuffle(articles, new Random(System.nanoTime()));
        
        int trainingSetElementsNumber = (int) (articles.size() * TRAINING_SET_PERCENTAGE * 0.01);
        List<Article> trainingSet = articles.subList(0, trainingSetElementsNumber);
        List<Article> testSet = articles.subList(trainingSetElementsNumber, articles.size());
        
        Distance<Base> measurer = getDistance(MEASURE_NAME);
        KnnText classifier = new KnnText(K_VALUE, measurer);
        List<String> classifiedLabels = classifier.classify(trainingSet, testSet);
        
        ResultCreator resultCreator = new ResultCreator();
        ClassificationResult result = resultCreator.createResult(testSet, classifiedLabels);
        System.out.println(result.toString());
    }
    
    private static Distance getDistance(String name)
    {
        switch(name)
        {
            case "e":
            case "euclidean":
                return new EuclideanDistance();
            case "ch":
            case "chebyshev":
                return new ChebyshevDistance();
            case "m":
            case "manhattan":
                return new ManhattanDistance();
            case "cos":
            case "cosine":
                return new CosineSimilarity();
            case "n":
            case "ngram":
                return new Ngram(3);
            default:
                throw new RuntimeException("Invalid measure name.");
        }
    }
    
    @Test
    public void shouldReduceValues()
    {
        List<String> list = Arrays.asList("jeden", "jeden/dwa", "jeden");
        
        List<String> result = list
            .stream()
            .flatMap((p1) -> Stream.of(p1.split("/")))
            .collect(Collectors.toList());
        
        result.forEach(System.out::println);
    }
}
