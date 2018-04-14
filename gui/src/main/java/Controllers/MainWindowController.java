package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTabPane;
import general.AppState;
import general.Countries;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import logic.extractors.WordModel;
import logic.extractors.processors.Processor;
import logic.model.entity.Article;
import logic.parser.XmlParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainWindowController
{
    
    private static final String STOP_WORDS_FILE = "../../../extractor/stopwords.txt";
    
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
     * Button used to choose directory
     */
    @FXML
    private JFXButton directoryButton;
    
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
        elementCombo.getItems().addAll(new String("Places"), new String("People"));
        elementCombo.getSelectionModel().select(0);
        
        wordColumn.setCellValueFactory(new PropertyValueFactory<WordModel, String>("word"));
        occurencesColumn.setCellValueFactory(new PropertyValueFactory<WordModel, Integer>("occurences"));
        
        tableView.setItems(AppState.getInstance().getFreqList());
        
        unifiedTab.setUserData(0);
        separateTab.setUserData(1);
        
        addListenerToTabPane();
        
        addCountriesToComboBox();
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
        this.updateList(articles);
        this.updateStatistics();
    }
    
    private void addCountriesToComboBox()
    {
        countryComboBox.getItems().addAll(Countries.getCountries());
        countryComboBox.getSelectionModel().select(0);
    }
    
    @FXML
    private void directoryButtonClicked()
    {
        System.out.println("Directory button clicked");
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File selectedDirectory = chooser.showDialog(this.stage);
        if(selectedDirectory == null)
        {
            directoryLabel.getStyleClass().add("error");
            directoryLabel.setText("Select valid directory");
            return;
        }
        else
        {
            directoryLabel.getStyleClass().remove("error");
            directoryLabel.setText(selectedDirectory.getPath());
        }
        
        XmlParser parser = new XmlParser();
        File xmlDir = new File(selectedDirectory.getAbsolutePath());
        AppState.getInstance().setArticles(parser.parseDir(xmlDir));
        generateFreqList(AppState.getInstance().getArticles());
    }
    
    private List<WordModel> mapToPairList(Map<String, Integer> freqMap)
    {
        return freqMap.entrySet()
                      .stream()
                      .map(e -> new WordModel(e.getKey(), e.getValue()))
                      .collect(Collectors.toList());
    }
    
    private void updateList(List<Article> articles)
    {
        ObservableList<Pair<String, Integer>> freqList = AppState.getInstance().getFreqList();
        freqList.clear();
        freqList.addAll(mapToPairList(Processor.getFreqMap(articles, elementCombo.getSelectionModel().getSelectedIndex())));
        AppState.getInstance().setCurrentArticles(articles);
    }
    
    private void updateStatistics()
    {
        Integer words = AppState.getInstance().getFreqList().size();
        Integer articles = AppState.getInstance().getCurrentArticles().size();
        statisticsArea.setText(articles + " articles analyzed\n" + words + " words extracted");
    }
    
    @FXML
    private void extractButtonClicked()
    {
        System.out.println("Extract button clicked");
    }
    
    @FXML
    private void elementComboChanged()
    {
        this.generateFreqList(AppState.getInstance().getCurrentArticles());
    }
    
    @FXML
    private void countryComboBoxChanged()
    {
        if((int) wordsTabPane.getSelectionModel().getSelectedItem().getUserData() == 0)
        {
            this.generateFreqList(AppState.getInstance().getArticles());
            return;
        }
        List<Article> filtered = new ArrayList<>();
        for(Article article : AppState.getInstance().getArticles())
        {
            if(article.getPlaces().contains(countryComboBox.getValue().toLowerCase()))
            {
                filtered.add(article);
            }
        }
        this.generateFreqList(filtered);
    }
    
    @FXML
    private void extractionTypeComboChanged()
    {
        System.out.println("Extraction combo changed");
    }
    
    @FXML
    private void similarityComboChanged()
    {
        System.out.println("similarity combo changed");
    }
    
    private List<String> retrieveStopWordsToRemove()
    {
        List<String> stopWords = new ArrayList<>();
        try(Stream<String> stream = Files.lines(Paths.get("./model/extractor/stopwords.txt")))
        {
            stopWords = stream
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return stopWords;
    }
}
