package logic.metrics;

import logic.model.entity.NumberVector;

public interface NumberDistance
{
    double distanceNumber(NumberVector first, NumberVector second);
}
