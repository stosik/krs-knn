package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

import general.AppState;

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
    
    private Stage stage;
    
    @FXML
    public void initialize() {
        this.stage = AppState.getInstance().getPrimaryStage();
        elementCombo.getItems().addAll(new String("Places"), new String("People"));
        elementCombo.setPromptText("Select element");
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
        } else {
            directoryLabel.getStyleClass().remove("error");
            directoryLabel.setText(selectedDirectory.getPath());
        }
    }
    
    @FXML
    private void extractButtonClicked() {
        System.out.println("Extract button clicked");
    }
    
    @FXML
    private void elementComboChanged() {
        System.out.println("element combo changed");
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
