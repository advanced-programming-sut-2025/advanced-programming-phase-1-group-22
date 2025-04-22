package utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHasher {
        private static final int ITERATIONS = 100000;
        private static final int SALT_LENGTH = 16; // 16 bytes = 128 bits

        // Hash a password with a random salt
        public static String hashPassword(String password) throws NoSuchAlgorithmException {
            // Generate a random salt
            byte[] salt = generateSalt();

            // Hash the password
            byte[] hash = calculateHash(password, salt);

            // Combine salt and hash for storage
            return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
        }

        // Verify a password against a stored hash
        public static boolean verifyPassword(String password, String storedHash) throws NoSuchAlgorithmException {
            // Split the stored hash into salt and hash
            String[] parts = storedHash.split(":");
            if (parts.length != 2) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[1]);

            // Compute the hash of the provided password
            byte[] actualHash = calculateHash(password, salt);

            // Compare the hashes
            return MessageDigest.isEqual(expectedHash, actualHash);
        }

        private static byte[] generateSalt() {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);
            return salt;
        }

        private static byte[] calculateHash(String password, byte[] salt) throws NoSuchAlgorithmException {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(salt);

            byte[] hash = digest.digest(password.getBytes());

            // Apply iterations
            for (int i = 0; i < ITERATIONS - 1; i++) {
                digest.reset();
                hash = digest.digest(hash);
            }

            return hash;
        }
}
