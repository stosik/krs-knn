package logic.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Delegate;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class WordVector {

    @Delegate
    private List<Double> wordValues;

    private String label;
    private double distance;

    public WordVector(String place) {
        this.label = place;
        this.wordValues = new ArrayList<Double>();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(label + ": (");
        for (Double d : wordValues) {
            result.append(d.toString()).append(",");
        }
        result = new StringBuilder(result.substring(0, result.length() - 1) + ')');
        return result.toString();
    }
}
