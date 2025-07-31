package io.github.some_example_name.server.repository;

import io.github.some_example_name.common.model.User;
import io.github.some_example_name.common.model.enums.Gender;
import io.github.some_example_name.common.model.enums.SecurityQuestion;

import java.sql.*;
import java.util.Optional;

public class UserDatabase {

    private static final String DB_URL = "jdbc:sqlite:users.db";

    public UserDatabase() {
        createUsersTableIfNotExists();
    }

    private void createUsersTableIfNotExists() {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    email TEXT,
                    nickname TEXT,
                    gender TEXT,
                    security_question TEXT,
                    answer TEXT,
                    highest_money_earned INTEGER,
                    number_of_played_games INTEGER,
                    is_playing TEXT
                )
            """;

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<User> insertUser(User user) {
        String sql = """
                INSERT INTO users (username, password, email, nickname, gender, security_question, answer,
                                   highest_money_earned, number_of_played_games, is_playing)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
            setUserParams(prepareStatement, user);
            prepareStatement.executeUpdate();
            return Optional.of(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<User> getUserByUsername(String username) {
        return queryUser("SELECT * FROM users WHERE username = ?", username);
    }

    public boolean updateUserField(String username, String field, String newValue) {
        String sql = "UPDATE users SET " + field + " = ? WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
            prepareStatement.setString(1, newValue);
            prepareStatement.setString(2, username);
            return prepareStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Optional<User> deleteUserById(String username) {
        Optional<User> user = getUserByUsername(username);
        if (user.isPresent()) {
            String sql = "DELETE FROM users WHERE username = ?";
            try (Connection connection = DriverManager.getConnection(DB_URL);
                 PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
                prepareStatement.setString(1, username);
                prepareStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    private Optional<User> queryUser(String query, Object param) {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement prepareStatement = connection.prepareStatement(query)) {

            if (param instanceof Integer) {
                prepareStatement.setInt(1, (Integer) param);
            } else {
                prepareStatement.setString(1, param.toString());
            }

            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(fromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private void setUserParams(PreparedStatement preparedStatement, User user) throws SQLException {
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getEmail());
        preparedStatement.setString(4, user.getNickname());
        preparedStatement.setString(5, user.getGender().name());
        preparedStatement.setString(6, user.getSecurityQuestion().getQuestion());
        preparedStatement.setString(7, user.getAnswer());
        preparedStatement.setInt(8, user.getHighestMoneyEarned());
        preparedStatement.setInt(9, user.getNumberOfPlayedGames());
        preparedStatement.setString(10, user.getIsPlaying());
    }

    private User fromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setEmail(resultSet.getString("email"));
        user.setNickname(resultSet.getString("nickname"));
        user.setGender(Gender.valueOf(resultSet.getString("gender")));
        user.setSecurityQuestion(SecurityQuestion.getFromQuestion(resultSet.getString("security_question")));
        user.setAnswer(resultSet.getString("answer"));
        user.setHighestMoneyEarned(resultSet.getInt("highest_money_earned"));
        user.setNumberOfPlayedGames(resultSet.getInt("number_of_played_games"));
        user.setIsPlaying(resultSet.getString("is_playing"));
        return user;
    }
}
