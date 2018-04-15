package logic.extraction;

import logic.model.Base;

import java.util.List;

public interface Extractor<InputT extends Base, OutputT extends Base> {
    
    List<OutputT> extractFeatures(List<InputT> entities);
}
