package general;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.util.Pair;
import logic.model.Article;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class AppState
{
    private static AppState instance;
    
    private List<Article> articles = new ArrayList<>();
    final private ObservableList<Pair<String, Integer>> freqList = FXCollections.observableArrayList();
    
    private Stage primaryStage;
    
    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return instance;
    }
    
    private void constructor() {
    }
}
