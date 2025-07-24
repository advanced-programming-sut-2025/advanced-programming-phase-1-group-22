package io.github.some_example_name.common.model;

import io.github.some_example_name.common.model.enums.SecurityQuestion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.github.some_example_name.common.model.enums.Gender;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = User.TABLE_NAME)
public class User {
    public static final String TABLE_NAME = "users";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private String email;
    private String nickname;
    private Gender gender;
    private SecurityQuestion securityQuestion;
    private String answer;
    private Integer highestMoneyEarned = 0;
    private Integer numberOfPlayedGames = 0;
    private String isPlaying;

    public User(String username, String password, String email, String nickname, Gender gender,SecurityQuestion securityQuestion,String answer) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
        this.securityQuestion = securityQuestion;
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "User Information:\n" +
                "• Username: " + username + "\n" +
                "• Nickname: " + nickname + "\n" +
                "• Highest money earned in a game: " + String.valueOf(highestMoneyEarned) + "\n" +
                "• Number of games played: " + String.valueOf(numberOfPlayedGames);
    }
}
