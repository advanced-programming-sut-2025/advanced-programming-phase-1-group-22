package view;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Menu {
    protected Matcher isMatched(String input, String pattern) {
        Matcher matcher = Pattern.compile(pattern).matcher(input);

        if (matcher.matches()) {
            return matcher;
        }
        return null;
    }

    public void checkCommand(Scanner scanner) {

    }

}
