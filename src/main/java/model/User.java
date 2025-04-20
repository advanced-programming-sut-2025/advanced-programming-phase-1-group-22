package model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import model.enums.Gender;
import repository.UserRepository;
import repository.UserRepositoryImpl;

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
    private Integer highestMoneyEarned;
    private Integer numberOfPlayedGames;
    private boolean isPlaying;

    public User(String username, String password, String email, String nickname, Gender gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
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
