package logic.metrics;

import javafx.util.Pair;
import logic.model.Base;
import logic.model.entity.NumberVector;
import logic.model.entity.WordVector;

import java.util.Comparator;

public interface Distance<EntityT extends Base> extends NumberDistance {
    
    Comparator<Pair<Integer, Double>> getComparator();
    
    double distance(EntityT first, EntityT second);
}