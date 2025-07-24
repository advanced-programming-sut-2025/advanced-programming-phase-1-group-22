package io.github.some_example_name.server.repository;

import io.github.some_example_name.common.model.User;

import java.util.Optional;

public interface UserRepository<T extends User> {
    Optional<T> save(T user);

    Optional<T> findById(int id);

    Optional<T> findByUsername(String username);

    Optional<T> deleteById(int id);

    Optional<T> changePassword(String username, String newPassword);

    Optional<T> changeEmail(String username, String newEmail);

    Optional<T> changeUsername(String username, String newUsername);

    Optional<T> changeNickname(String username, String newNickname);

    Optional<Integer> getHIghestEarnedMoney(String username);

    Optional<Integer> getPlayedGamse(String username);
}
