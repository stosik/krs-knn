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
    private String date;
    private List<String> topics;
    private List<String> places;
    private List<String> people;
    private List<String> orgs;
    private List<String> exchanges;
    private List<String> companies;
    private String unknown;
    private String textTitle;
    private String textAuthor;
    private String textDateline;
    private String body;
}

