package logic.classifier.result;

import logic.model.Base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ResultCreator
{
    public ClassificationResult createResult(List<? extends Base> testEntities, List<String> classifiedLabels)
    {
        Map<String, Map<String, Integer>> confusionMatrix = new HashMap<>();
        Map<String, Integer> initialInnerMap = new HashMap<>();
        testEntities.forEach(entity -> initialInnerMap.put(entity.getLabel(), 0));
        
        IntStream
            .range(0, testEntities.size())
            .forEachOrdered(i -> confusionMatrix.computeIfAbsent(testEntities.get(i).getLabel(), k -> new HashMap<>(initialInnerMap))
                                                .merge(classifiedLabels.get(i), 1, (a, b) -> b + a));
        
        return new ClassificationResult(confusionMatrix);
    }
}
