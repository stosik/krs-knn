package logic.extraction.impl;

import logic.extraction.Extractor;
import logic.model.entity.Article;
import logic.model.entity.FrequencyMatrix;
import logic.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TermFrequencyMatrixExtractor implements Extractor<Article, FrequencyMatrix>
{
    
    @Override
    public List<FrequencyMatrix> extractFeatures(List<Article> entities)
    {
        return entities
            .stream()
            .map(entity -> new FrequencyMatrix(entity.getLabel(), TextUtils.getAllWordsCounts(entity)))
            .collect(Collectors.toCollection(ArrayList::new));
    }
}
