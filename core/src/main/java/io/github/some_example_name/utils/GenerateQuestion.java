package io.github.some_example_name.utils;

import lombok.Getter;

@Getter
public enum GenerateQuestion {
    Q1("2+8=?", "10"), Q2("opposite of left?", "right"), Q3("12-7=?", "5");
    private String question;
    private String answer;

    GenerateQuestion(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }
}
