package logic.model.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum People
{
    ARTHUR("arthur"),
    JORDAN("jordan"),
    LEONIDAS("leonidas"),
    RAMSAY("ramsay"),
    SPOCK("spock"),
    STALIN("stalin");
    
    private final String people;
    
    People(String people) {
        this.people = people;
    }
}
