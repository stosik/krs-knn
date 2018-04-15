import logic.classifier.Classifier;
import logic.classifier.KNN;
import logic.classifier.result.ClassificationResult;
import logic.classifier.result.ResultCreator;
import logic.extraction.Extractor;
import logic.extraction.impl.CountVectorizer;
import logic.extraction.impl.TermFrequencyMatrixExtractor;
import logic.extraction.impl.TfidfVectorizer;
import logic.metrics.Distance;
import logic.metrics.distance.ChebyshevDistance;
import logic.metrics.distance.EuclideanDistance;
import logic.metrics.distance.ManhattanDistance;
import logic.metrics.similarity.CosineSimilarity;
import logic.metrics.similarity.Ngram;
import logic.model.Base;
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
    private static final String EXTRACTOR_NAME = "count";
    private static final String MEASURE_NAME = "euclidean";
    private static final int TRAINING_SET_PERCENTAGE = 60;
    private static final int K_VALUE = 10;
    
    public static void main(String[] args)
    {
        //Preprocess data from sgm files
        List<Article> entities = FileUtils.loadReutersData("places");
        entities = PreprocessUtils.preprocessTextEntities(entities, AFFIX_PATH, DICTIONARY_PATH);
        Collections.shuffle(entities, new Random(System.nanoTime()));
        FileUtils.saveReutersDataToFile(entities, OUTPUT_PATH);
        
        //Load saved preprocessed data to article model
        List<Article> articles = FileUtils.loadXmlData(INPUT_PATH);
        
        //create test and training set
        int trainingSetElementsNumber = (int) (articles.size() * TRAINING_SET_PERCENTAGE * 0.01);
        List<Article> textTrainingSet = articles.subList(0, trainingSetElementsNumber);
        List<Article> textTestSet = articles.subList(trainingSetElementsNumber, articles.size());
        Extractor featureExtractor = MainModel.getFeatureExtractor(EXTRACTOR_NAME, textTrainingSet);
        
        //Extract features from training and test set
        List<Base> trainingSet = featureExtractor.extractFeatures(textTrainingSet);
        List<Base> testSet = featureExtractor.extractFeatures(textTestSet);
        
        textTrainingSet = null;
        textTestSet = null;
        
        Distance<Base> measurer = MainModel.getMeasure(MEASURE_NAME);
        Classifier<Base> classifier = new KNN<>(K_VALUE, measurer);
        List<String> classifiedLabels;
        try
        {
            classifiedLabels = classifier.classify(trainingSet, testSet);
        }
        catch(ClassCastException e)
        {
            throw new RuntimeException("Invalid feature extractor and measurer combination.");
        }
        ResultCreator resultCreator = new ResultCreator();
        ClassificationResult result = resultCreator.createResult(testSet, classifiedLabels);
        System.out.println(result + "\n");
    }
    
    private static Extractor getFeatureExtractor(String name, List<Article> trainingSet)
    {
        switch(name)
        {
            case "c":
            case "count":
                return new CountVectorizer(trainingSet);
            case "t":
            case "tfidf":
                return new TfidfVectorizer(trainingSet);
            case "f":
            case "freq":
                return new TermFrequencyMatrixExtractor();
            default:
                throw new RuntimeException("Invalid feature extractor name.");
        }
    }
    
    public static Distance getMeasure(String name)
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
}