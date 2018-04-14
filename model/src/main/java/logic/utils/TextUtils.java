package logic.utils;

import logic.model.entity.Article;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor
public class TextUtils
{
    public static List<String> getAllWords(Article entity)
    {
        return Arrays.asList(entity.getContent().toLowerCase().split("\\W+"));
    }
    
    public static Set<String> getUniqueWords(Article entity)
    {
        return new HashSet<>(getAllWords(entity));
    }
    
    public static Map<String, Long> getAllWordsCounts(Article entity)
    {
        List<String> words = getAllWords(entity);
        return words.stream().collect(
//        Collectors.groupingBy(Function.identity(), Collectors.counting()));
            Collectors.groupingBy(Function.identity(), TreeMap::new, Collectors.counting()));
    }
}
