package io.github.some_example_name.server.repository;

import io.github.some_example_name.common.model.User;
import io.github.some_example_name.common.model.enums.Gender;
import io.github.some_example_name.common.model.enums.SecurityQuestion;

import java.io.*;
import java.util.*;

public class UserRepo implements UserRepository {

    private static final UserDatabase database = new UserDatabase();

    @Override
    public Optional<User> save(User user) {
        Optional<User> existing = database.getUserByUsername(user.getUsername());
        if (existing.isPresent()) {
            database.updateFullUser(user);
            return Optional.of(user);
        } else {
            return database.insertUser(user);
        }
    }

    @Override
    public Optional findById(int id) {
        return database.getUserById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return database.getUserByUsername(username);
    }

    @Override
    public Optional<User> deleteById(int id) {
        return database.deleteUserById(id);
    }

    @Override
    public Optional<User> changePassword(String username, String newPassword) {
        return updateField(username, "password", newPassword);
    }

    @Override
    public Optional<User> changeEmail(String username, String newEmail) {
        return updateField(username, "email", newEmail);
    }

    @Override
    public Optional<User> changeUsername(String username, String newUsername) {
        return updateField(username, "username", newUsername);
    }

    @Override
    public Optional<User> changeNickname(String username, String newNickname) {
        return updateField(username, "nickname", newNickname);
    }

    @Override
    public Optional<Integer> getHIghestEarnedMoney(String username) {
        return findByUsername(username).map(User::getHighestMoneyEarned);
    }

    @Override
    public Optional<Integer> getPlayedGamse(String username) {
        return findByUsername(username).map(User::getNumberOfPlayedGames);
    }

    private Optional<User> updateField(String username, String field, String newValue) {
        boolean updated = database.updateUserField(username, field, newValue);
        if (updated) {
            return database.getUserByUsername(username);
        }
        return Optional.empty();
    }
}
