package io.github.some_example_name.model.enums;

import lombok.Getter;

@Getter
public enum SecurityQuestion {
    QUESTION1("Your favorite movie?"),
    QUESTION2("Your first school?"),
    QUESTION3("Your favorite song?"),
    QUESTION4("Your favorite sport?"),
    QUESTION5("Your collage name?");

    private final String question;

    SecurityQuestion(String question) {
        this.question = question;
    }

    public static SecurityQuestion getFromQuestion(String question) {
        for (SecurityQuestion value : SecurityQuestion.values()) {
            if (value.question.equals(question)) return value;
        }
        return null;
    }
}
