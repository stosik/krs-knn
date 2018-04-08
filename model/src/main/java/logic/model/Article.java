package logic.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Article
{
    private List<String> topics;
    private List<String> places;
    private String body;
}

