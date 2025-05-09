package repository;

import model.User;
import model.enums.Gender;

import java.io.*;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private static final String FILE_PATH = "users.txt"; // مسیر فایل برای ذخیره‌سازی کاربران

    @Override
    public Optional<User> save(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            // ذخیره اطلاعات کاربر در فایل
            writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getEmail() + "\n");
            return Optional.of(user);
        } catch (IOException e) {
            System.err.println("Error while saving user: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findById(int id) {
        // برای شبیه‌سازی، فرض کنیم که هر کاربر یک شناسه (ID) منحصر به فرد دارد و در فایل ذخیره شده است.
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (Integer.parseInt(userData[0]) == id) { // فرض بر این است که شناسه در ابتداست
                    User user = new User("mahdi","Mahdi1384&","mahdi@gmail.com","mahdi", Gender.MALE); // به‌روزرسانی با جزئیات کاربر
                    user.setIsPlaying(false);
                    return Optional.of(user);
                }
            }
        } catch (IOException e) {
            System.err.println("Error while finding user: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData[0].equals(username)) { // بررسی نام‌کاربری
                    User user = new User(username,"Mahdi1384&","mahdi@gmail.com","mahdi", Gender.MALE); // ایجاد شی کاربر
                    user.setIsPlaying(false);
                    return Optional.of(user);
                }
            }
        } catch (IOException e) {
            System.err.println("Error while finding user by username: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> deleteById(int id) {
        File tempFile = new File("users_temp.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean deleted = false;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (Integer.parseInt(userData[0]) == id) {
                    deleted = true; // حذف کاربر از فایل
                } else {
                    writer.write(line + "\n");
                }
            }

            // اگر کاربر پیدا و حذف شده بود، فایل اصلی را با فایل جدید جایگزین می‌کنیم
            if (deleted) {
                if (tempFile.renameTo(new File(FILE_PATH))) {
                    return Optional.of(new User("mahdi","Mahdi1384&","mahdi@gmail.com","mahdi", Gender.MALE)); // به‌عنوان نماینده موفقیت‌آمیز بودن حذف
                }
            }
        } catch (IOException e) {
            System.err.println("Error while deleting user: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional changePassword(String username, String newPassword) {
        return updateUserInfo(username, "password", newPassword);
    }

    @Override
    public Optional changeEmail(String username, String newEmail) {
        return updateUserInfo(username, "email", newEmail);
    }

    @Override
    public Optional changeUsername(String username, String newUsername) {
        return updateUserInfo(username, "username", newUsername);
    }

    @Override
    public Optional changeNickname(String username, String newNickname) {
        return updateUserInfo(username, "nickname", newNickname);
    }

    private Optional updateUserInfo(String username, String field, String newValue) {
        File tempFile = new File("users_temp.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean updated = false;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData[1].equals(username)) {
                    switch (field) {
                        case "password":
                            userData[2] = newValue;
                            break;
                        case "email":
                            userData[3] = newValue;
                            break;
                        case "username":
                            userData[1] = newValue;
                            break;
                        case "nickname":
                            userData[4] = newValue;
                            break;
                    }
                    updated = true;
                }
                writer.write(String.join(",", userData) + "\n");
            }

            if (updated) {
                if (tempFile.renameTo(new File(FILE_PATH))) {
                    return Optional.of(new User("mahdi","Mahdi1384&","mahdi@gmail.com","mahdi", Gender.MALE)); // به‌عنوان نماینده موفقیت‌آمیز بودن تغییر
                }
            }
        } catch (IOException e) {
            System.err.println("Error while updating user: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getHIghestEarnedMoney(String username) {
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getPlayedGamse(String username) {
        return Optional.empty();
    }
}
