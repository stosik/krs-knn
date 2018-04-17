package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTabPane;
import general.AppState;
import general.ListItemArticle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class MainWindowController
{
    
    private static final String DICTIONARY_PATH = "./model/dict/en_US-chromium/en_US.dic";
    private static final String AFFIX_PATH = "./model/dict/en_US-chromium/en_US.aff";
    private static final String OUTPUT_PATH = "./model/data/text/reuters.xml";
    private static final String INPUT_PATH = "./model/data/text/reuters.xml";
    private static final String EXTRACTOR_NAME = "count";
    private static final String MEASURE_NAME = "euclidean";
    private static final int TRAINING_SET_PERCENTAGE = 60;
    private static final int K_VALUE = 10;
    
    private List<Article> articles = null;
    private ObservableList<ListItemArticle> listArticles = FXCollections.observableArrayList();
    
    /**
     * TextArea that display statistics
     */
    @FXML
    private TextArea statisticsArea;
    
    /**
     * TextArea that display main content on screen.
     */
    @FXML
    private TextArea mainTextArea;
    
    /**
     * Button used to extract characteristics.
     */
    @FXML
    private JFXButton extractButton;
    
    /**
     * Label that display current chosen directory
     */
    @FXML
    private Label directoryLabel;
    
    /**
     * Combo box used to select element.
     */
    @FXML
    private JFXComboBox<String> elementCombo;
    
    /**
     * Combo box used to select extraction type.
     */
    @FXML
    private JFXComboBox<String> extractionTypeCombo;
    
    /**
     * Combo used to select similarity measure.
     */
    @FXML
    private JFXComboBox<String> similarityCombo;
    
    /**
     * word column
     */
    @FXML
    private TableColumn wordColumn;
    
    /**
     * occurences column
     */
    @FXML
    private TableColumn occurencesColumn;
    
    @FXML
    private TableView tableView;
    
    @FXML
    private JFXTabPane wordsTabPane;
    
    @FXML
    private Tab unifiedTab;
    
    @FXML
    private Tab separateTab;
    
    @FXML
    private JFXComboBox<String> countryComboBox;
    
    private Stage stage;
    
    @FXML
    public void initialize()
    {
        this.stage = AppState.getInstance().getPrimaryStage();
        elementCombo.getItems().addAll(new String("Places"), new String("Topics"));
        elementCombo.getSelectionModel().select(0);
        
        extractionTypeCombo.getItems().addAll(new String("count"), new String("tfidf"), new String("freq"));
        extractionTypeCombo.getSelectionModel().select(0);
    
        similarityCombo.getItems().addAll(new String("euclidean"), new String("chebyshev"), new String("manhattan"), new String("cosine"), new String("ngram"));
        similarityCombo.getSelectionModel().select(0);
        
        wordColumn.setCellValueFactory(new PropertyValueFactory<ListItemArticle, String>("word"));
        occurencesColumn.setCellValueFactory(new PropertyValueFactory<ListItemArticle, Integer>("occurence"));

        tableView.setItems(this.listArticles);

        unifiedTab.setUserData(0);
        separateTab.setUserData(1);

        addListenerToTabPane();
    }
    
    private void addListenerToTabPane()
    {
        wordsTabPane.getSelectionModel().selectedItemProperty().addListener(
            (ov, previous, next) -> {
                countryComboBoxChanged();
            }
                                                                           );
    }
    
    private void generateFreqList(List<Article> articles)
    {
        Map<String, Integer> words = generateCountMap(articles);
        this.updateList(words);
        int wordsCount = words
            .values()
            .stream()
            .mapToInt(Integer::intValue)
            .sum();

        this.updateStatistics(wordsCount, articles.size());
    }
    
    private Map<String, Integer> generateCountMap(List<Article> articles)
    {
        Map<String, Integer> wordsCount = new HashMap<>();
        for (Article article : articles)
        {
            for (String word: article.getContent().split(" "))
            {
                if (wordsCount.containsKey(word)) {
                    wordsCount.put(word, wordsCount.get(word) + 1);
                } else {
                    wordsCount.put(word, 1);
                }
            }
        }
        return wordsCount;
    }
    
    private void addCountriesToComboBox()
    {
        List<String> labels = articles
            .stream()
            .map(Article::getLabel)
            .distinct()
            .collect(Collectors.toList());
        countryComboBox.getItems().addAll(labels);
        countryComboBox.getSelectionModel().select(0);
    }
    
    @FXML
    private void processSgmToXml()
    {
        String label = elementCombo.getValue();
        List<Article> entities = FileUtils.loadReutersData(label.toLowerCase());
        entities = PreprocessUtils.preprocessTextEntities(entities, AFFIX_PATH, DICTIONARY_PATH);
        Collections.shuffle(entities, new Random(System.nanoTime()));
        FileUtils.saveReutersDataToFile(entities, OUTPUT_PATH);
    }
    
    @FXML
    private void loadArticles()
    {
        articles = FileUtils.loadXmlData(INPUT_PATH);
        generateFreqList(articles);
        addCountriesToComboBox();
    }
    
    private List<ListItemArticle> mapToPairList(Map<String, Integer> freqMap)
    {
        List<ListItemArticle> list = freqMap.entrySet()
                      .stream()
                      .map(e -> new ListItemArticle(e.getKey(), e.getValue()))
                      .collect(Collectors.toList());
        
        list.sort((p, n) -> {
                if(p.getOccurence() > n.getOccurence()) return -1;
                else if(p.getOccurence() < n.getOccurence()) return 1;
                else return 0;
            });
        
        return list;
    }
    
    private void updateList(Map<String, Integer> words)
    {
        listArticles.clear();
        listArticles.addAll(mapToPairList(words));
    }
    
    private void updateStatistics(int wordsCount, int articlesCount)
    {
        statisticsArea.setText(articlesCount + " articles analyzed\n" + wordsCount + " words extracted");
    }
    
    @FXML
    private void extractButtonClicked()
    {
        //create test and training set
        int trainingSetElementsNumber = (int) (articles.size() * TRAINING_SET_PERCENTAGE * 0.01);
        List<Article> textTrainingSet = articles.subList(0, trainingSetElementsNumber);
        List<Article> textTestSet = articles.subList(trainingSetElementsNumber, articles.size());
        Extractor featureExtractor = getFeatureExtractor(EXTRACTOR_NAME, textTrainingSet);
    
        //Extract features from training and test set
        List<Base> trainingSet = featureExtractor.extractFeatures(textTrainingSet);
        System.out.println("Przed");
        trainingSet
            .stream()
            .map(e -> e.getContent().toString())
            .forEach(e -> {
                System.out.println(e);
                mainTextArea.appendText(e);
            });
        System.out.println("Po");
        
        List<Base> testSet = featureExtractor.extractFeatures(textTestSet);

        Distance<Base> measurer = getMeasure(MEASURE_NAME);
        Classifier<Base> classifier = new KNN<>(K_VALUE, measurer);
        List<String> classifiedLabels = classifier.classify(trainingSet, testSet);

        
        ResultCreator resultCreator = new ResultCreator();
        ClassificationResult result = resultCreator.createResult(testSet, classifiedLabels);
        System.out.println(result + "\n");
    }
    
    @FXML
    private void elementComboChanged()
    {

    }
    
    @FXML
    private void countryComboBoxChanged()
    {
        if((int) wordsTabPane.getSelectionModel().getSelectedItem().getUserData() == 0)
        {
            this.generateFreqList(articles);
            return;
        }
        String value = countryComboBox.getValue();
        List<Article> filtered = articles
            .stream()
            .filter(e -> e.getLabel().equals(value))
            .collect(Collectors.toList());
        this.generateFreqList(filtered);
    }
    
    @FXML
    private void extractionTypeComboChanged()
    {
    }
    
    @FXML
    private void similarityComboChanged()
    {
        System.out.println("similarity combo changed");
    }
    
    private Extractor getFeatureExtractor(String name, List<Article> trainingSet)
    {
        switch(name)
        {
            case "count":
                return new CountVectorizer(trainingSet);
            case "tfidf":
                return new TfidfVectorizer(trainingSet);
            case "freq":
                return new TermFrequencyMatrixExtractor();
            default:
                throw new RuntimeException("Invalid feature extractor name.");
        }
    }
    
    private Distance getMeasure(String name)
    {
        switch(name)
        {
            case "euclidean":
                return new EuclideanDistance();
            case "chebyshev":
                return new ChebyshevDistance();
            case "manhattan":
                return new ManhattanDistance();
            case "cosine":
                return new CosineSimilarity();
            case "ngram":
                return new Ngram(3);
            default:
                throw new RuntimeException("Invalid measure name.");
        }
    }
}
