package logic.model.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum Place
{
    WEST_GERMANY("west-germany"),
    USA("usa"),
    FRANCE("france"),
    UK("uk"),
    CANADA("canada"),
    JAPAN("japan");
    
    private final String place;
    
    Place(String place)
    {
        this.place = place;
    }
}
