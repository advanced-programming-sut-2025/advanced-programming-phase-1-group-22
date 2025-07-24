package io.github.some_example_name.common.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandClass {
    private final Pattern pattern;

    CommandClass(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    public boolean matches(String input) {
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

    public String[] getParameters(String input) {
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            int groupCount = matcher.groupCount();
            var params = new String[groupCount];
            for (int i = 1; i <= groupCount; i++)
                params[i - 1] = matcher.group(i);

            return params;
        }
        return new String[0];
    }
}
