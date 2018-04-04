package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import general.AppState;
import javafx.util.Pair;
import logic.extractors.WordModel;
import logic.extractors.processors.Processor;
import logic.model.Article;
import logic.parser.XmlParser;

public class MainWindowController
{
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
    
    private Stage stage;
    
    @FXML
    public void initialize() {
        this.stage = AppState.getInstance().getPrimaryStage();
        elementCombo.getItems().addAll(new String("Places"), new String("People"));
        elementCombo.setPromptText("Select element");
        
        wordColumn.setCellValueFactory(new PropertyValueFactory<WordModel, String>("word"));
        occurencesColumn.setCellValueFactory(new PropertyValueFactory<WordModel, Integer>("occurences"));
    
        tableView.setItems(AppState.getInstance().getFreqList());
    }
    
    @FXML
    private void directoryButtonClicked() {
        System.out.println("Directory button clicked");
        DirectoryChooser chooser = new DirectoryChooser();
        File selectedDirectory = chooser.showDialog(this.stage);
        if (selectedDirectory == null)
        {
            directoryLabel.getStyleClass().add("error");
            directoryLabel.setText("Select valid directory");
            return;
        } else {
            directoryLabel.getStyleClass().remove("error");
            directoryLabel.setText(selectedDirectory.getPath());
        }
    
        XmlParser parser = new XmlParser();
        File xmlDir = new File(selectedDirectory.getAbsolutePath());
        AppState.getInstance().setArticles(parser.parseDir(xmlDir));
        this.updateList();
        this.updateStatistics();
    }
    
    private List<WordModel> mapToPairList(Map<String, Integer> freqMap) {
            return freqMap.entrySet()
                           .stream()
                           .map(e -> new WordModel(e.getKey(), e.getValue()))
                           .collect(Collectors.toList());
    }
    
    private void updateList() {
        ObservableList<Pair<String, Integer>> freqList = AppState.getInstance().getFreqList();
        freqList.clear();
        freqList.addAll(mapToPairList(Processor.getFreqMap(AppState.getInstance().getArticles(), elementCombo.getSelectionModel().getSelectedIndex())));
    }
    
    private void updateStatistics() {
        Integer words = AppState.getInstance().getFreqList().size();
        Integer articles = AppState.getInstance().getArticles().size();
        statisticsArea.setText(articles + " articles analyzed\n" + words + " words extracted");
    }
    
    @FXML
    private void extractButtonClicked() {
        System.out.println("Extract button clicked");
    }
    
    @FXML
    private void elementComboChanged() {
        this.updateList();
        this.updateStatistics();
    }
    
    @FXML
    private void extractionTypeComboChanged() {
        System.out.println("Extraction combo changed");
    }
    
    @FXML
    private void similarityComboChanged() {
        System.out.println("similarity combo changed");
    }
}
