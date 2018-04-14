package logic.extraction;

import logic.model.Base;

import java.util.List;

public interface Extractor<InputEntityT extends Base, OutputEntityT extends Base> {
    
    List<OutputEntityT> extractFeatures(List<InputEntityT> entities);
}
