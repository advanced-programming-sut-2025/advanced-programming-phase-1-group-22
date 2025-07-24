package io.github.some_example_name.common.utils;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class GeneratePassword {
    public static String generatePassword() {
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";
        String symbols = "?><,\"';;:\\/|[]}{+=)(*&^%$#!";

        String allChars = lowercase + uppercase + digits + symbols;

        Random random = new Random();
        List<Character> passwordChars = new ArrayList<>();

        passwordChars.add(lowercase.charAt(random.nextInt(lowercase.length())));
        passwordChars.add(uppercase.charAt(random.nextInt(uppercase.length())));
        passwordChars.add(digits.charAt(random.nextInt(digits.length())));
        passwordChars.add(symbols.charAt(random.nextInt(symbols.length())));

        int remainingLength = 4 + random.nextInt(5);
        for (int i = 0; i < remainingLength; i++) {
            passwordChars.add(allChars.charAt(random.nextInt(allChars.length())));
        }

        Collections.shuffle(passwordChars);

        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }

        return password.toString();
    }
}
