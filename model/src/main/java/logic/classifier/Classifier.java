package logic.classifier;

import logic.model.Base;

import java.util.List;

public interface Classifier<EntityT extends Base>
{
    List<String> classify(List<EntityT> trainingSet, List<EntityT> testSet);
}
