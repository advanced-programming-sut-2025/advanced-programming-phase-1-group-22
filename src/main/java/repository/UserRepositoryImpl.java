package repository;

import model.User;

import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public Optional save(User user) {
        return Optional.empty();
    }

    @Override
    public Optional findById(int id) {
        return Optional.empty();
    }

    @Override
    public Optional findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Optional deleteByUsername(int id) {
        return Optional.empty();
    }

    @Override
    public Optional changePassword(String newPassword) {
        return Optional.empty();
    }

    @Override
    public Optional changeEmail(String newEmail) {
        return Optional.empty();
    }

    @Override
    public Optional changeUsername(String newUsername) {
        return Optional.empty();
    }

    @Override
    public Optional changeNickname(String newNickname) {
        return Optional.empty();
    }
}
