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
    public static Set<String> getUniqueWords(Article article)
    {
        return new HashSet<>(getAllWords(article));
    }
    
    public static Map<String, Long> getAllWordsCounts(Article article)
    {
        return getAllWords(article)
            .stream()
            .collect(Collectors.groupingBy(Function.identity(), TreeMap::new, Collectors.counting()));
    }
    
    public static List<String> getAllWords(Article entity)
    {
        return Arrays
            .asList(entity.getContent().toLowerCase().split("\\W+"));
    }
}
