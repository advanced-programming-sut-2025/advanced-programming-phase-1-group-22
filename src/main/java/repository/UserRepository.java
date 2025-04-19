package repository;

import model.User;

import java.util.Optional;

public interface UserRepository<T extends User> {
    Optional<T> save(T user);

    Optional<T> findById(int id);

    Optional<T> findByEmail(String email);

    Optional<T> findByUsername(String username);

    Optional<T> deleteByUsername(int id);

    Optional<T> changePassword(String newPassword);

    Optional<T> changeEmail(String newEmail);

    Optional<T> changeUsername(String newUsername);

    Optional<T> changeNickname(String newNickname);
}
