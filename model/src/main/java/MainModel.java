import logic.classifier.Classifier;
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
import logic.model.entity.NumberVector;
import logic.utils.DataUtils;
import logic.utils.FileUtils;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainModel
{
    
    private static final String INPUT_PATH = "./model/data/number/iris.data";
    private static final String MEASURE_NAME = "chebyshev";
    private static final int TRAINING_SET_PERCENTAGE = 70;
    private static final int K_VALUE = 5;
    
    public static void main(String[] args)
    {
        List<NumberVector> numberVectors = DataUtils.normalize(FileUtils.loadNumbers(INPUT_PATH));
        Collections.shuffle(numberVectors, new Random(System.nanoTime()));
        
        int trainingSetElementsNumber = (int) (numberVectors.size() * TRAINING_SET_PERCENTAGE * 0.01);
        List<NumberVector> trainingSet = numberVectors.subList(0, trainingSetElementsNumber);
        List<NumberVector> testSet = numberVectors.subList(trainingSetElementsNumber, numberVectors.size());
        
        Distance<Base> measurer = MainModel.getMeasure(MEASURE_NAME);
        KnnNumber classifier = new KnnNumber(K_VALUE, measurer);
        List<String> classifiedLabels = classifier.classify(trainingSet, testSet);
        
        ResultCreator resultCreator = new ResultCreator();
        ClassificationResult result = resultCreator.createResult(testSet, classifiedLabels);
        System.out.println(result + "\n");
    }
    
    private static Distance getMeasure(String name)
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