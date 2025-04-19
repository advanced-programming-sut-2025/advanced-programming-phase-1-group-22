package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import model.enums.Gender;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@Table(name = User.TABLE_NAME)
public class User {
    public static final String TABLE_NAME = "users";
    @Id
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String nickname;
    private Gender gender;

    public User(String username, String password, String email, String nickname, Gender gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
    }
}
