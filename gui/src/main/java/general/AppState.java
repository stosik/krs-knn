package general;

import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppState
{
    private static AppState instance;
    
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
