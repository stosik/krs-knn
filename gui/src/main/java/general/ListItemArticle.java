package general;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ListItemArticle
{
    private SimpleStringProperty word;
    private SimpleIntegerProperty occurence;

    public ListItemArticle(String word, Integer occurence)
    {
        this.word = new SimpleStringProperty(word);
        this.occurence = new SimpleIntegerProperty(occurence);
    }
    
    public String getWord()
    {
        return this.word.getValue();
    }
    
    public Integer getOccurence()
    {
        return this.occurence.getValue();
    }
}
