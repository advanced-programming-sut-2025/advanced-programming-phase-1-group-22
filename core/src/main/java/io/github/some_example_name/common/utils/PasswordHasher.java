package io.github.some_example_name.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHasher {
    private final int ITERATIONS = 100000;
    private final int SALT_LENGTH = 16;

    public String hashPassword(String password) throws NoSuchAlgorithmException {
        byte[] salt = generateSalt();

        byte[] hash = calculateHash(password, salt);

        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
    }

    public boolean verifyPassword(String password, String storedHash) throws NoSuchAlgorithmException {
        String[] parts = storedHash.split(":");
        if (parts.length != 2) {
            return false;
        }

        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] expectedHash = Base64.getDecoder().decode(parts[1]);

        byte[] actualHash = calculateHash(password, salt);

        return MessageDigest.isEqual(expectedHash, actualHash);
    }

    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    private byte[] calculateHash(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = null;
        digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(salt);

        byte[] hash = digest.digest(password.getBytes());

        for (int i = 0; i < ITERATIONS - 1; i++) {
            digest.reset();
            hash = digest.digest(hash);
        }

        return hash;
    }
}
