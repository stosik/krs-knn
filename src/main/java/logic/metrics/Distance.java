package logic.metrics;

import logic.model.WordVector;

public interface Distance {

    double distance(WordVector wv1, WordVector wv2);
}