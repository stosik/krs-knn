package logic.extractors;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Setter;

@Setter
public class WordModel
{
    private final SimpleStringProperty word;
    private final SimpleIntegerProperty occurences;
    
    public WordModel(String word, Integer occurences) {
        this.word = new SimpleStringProperty(word);
        this.occurences = new SimpleIntegerProperty(occurences);
    }
    
    public String getWord() {
        return this.word.get();
    }
    
    public Integer getOccurences() {
        return this.occurences.get();
    }
}
