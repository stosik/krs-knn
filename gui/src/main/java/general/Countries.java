package general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Countries
{
    public static List<String> getCountries() {
        return new ArrayList<>(Arrays.asList("West-Germany", "USA", "France", "UK", "Canada", "Japan"));
    }
}
