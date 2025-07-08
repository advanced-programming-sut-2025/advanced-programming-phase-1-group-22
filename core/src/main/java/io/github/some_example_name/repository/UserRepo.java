package io.github.some_example_name.repository;

import io.github.some_example_name.model.User;
import io.github.some_example_name.model.enums.Gender;
import io.github.some_example_name.model.enums.SecurityQuestion;

import java.io.*;
import java.util.*;

public class UserRepo implements UserRepository {

	private static final File file = new File("users.txt");

	@Override
	public Optional<User> save(User user) {
		List<User> users = loadAllUsers();
		users.add(user);
		writeAllUsers(users);
		return Optional.of(user);
	}

    @Override
	public Optional<User> findById(int id) {
		return loadAllUsers().stream()
				.filter(u -> u.getId() == id)
				.findFirst();
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return loadAllUsers().stream()
				.filter(u -> u.getUsername().equals(username))
				.findFirst();
	}

	@Override
	public Optional<User> deleteById(int id) {
		List<User> users = loadAllUsers();
		Optional<User> userToDelete = users.stream()
				.filter(u -> u.getId() == id)
				.findFirst();
		userToDelete.ifPresent(users::remove);
		writeAllUsers(users);
		return userToDelete;
	}

	@Override
	public Optional<User> changePassword(String username, String newPassword) {
		return updateField(username, u -> u.setPassword(newPassword));
	}

	@Override
	public Optional<User> changeEmail(String username, String newEmail) {
		return updateField(username, u -> u.setEmail(newEmail));
	}

	@Override
	public Optional<User> changeUsername(String username, String newUsername) {
		return updateField(username, u -> u.setUsername(newUsername));
	}

	@Override
	public Optional<User> changeNickname(String username, String newNickname) {
		return updateField(username, u -> u.setNickname(newNickname));
	}

	@Override
	public Optional<Integer> getHIghestEarnedMoney(String username) {
		return findByUsername(username).map(User::getHighestMoneyEarned);
	}

	@Override
	public Optional<Integer> getPlayedGamse(String username) {
		return findByUsername(username).map(User::getNumberOfPlayedGames);
	}

	private List<User> loadAllUsers() {
		List<User> users = new ArrayList<>();
		if (!file.exists()) return users;
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				users.add(fromCSV(line));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (User user : users) {
			user.setIsPlaying(null);
		}
		return users;
	}

	private void writeAllUsers(List<User> users) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
			for (User user : users) {
				bw.write(toCSV(user));
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Optional<User> updateField(String username, java.util.function.Consumer<User> updater) {
		List<User> users = loadAllUsers();
		for (User user : users) {
			if (user.getUsername().equals(username)) {
				updater.accept(user);
				writeAllUsers(users);
				return Optional.of(user);
			}
		}
		return Optional.empty();
	}
	public String toCSV(User user) {
		return user.getUsername() + "," + user.getPassword() + "," + user.getEmail() + "," + user.getNickname() + ","
            + user.getGender() + "," + user.getSecurityQuestion().getQuestion() + "," + user.getAnswer() + "," +
            user.getHighestMoneyEarned() + "," + user.getNumberOfPlayedGames() + "," + user.getIsPlaying();
	}

	public static User fromCSV(String line) {
		String[] parts = line.split(",");
		User user = new User();
		user.setUsername(parts[0]);
		user.setPassword(parts[1]);
		user.setEmail(parts[2]);
        user.setNickname(parts[3]);
        user.setGender(Gender.valueOf(parts[4]));
        user.setSecurityQuestion(SecurityQuestion.getFromQuestion(parts[5]));
        user.setAnswer(parts[6]);
        user.setHighestMoneyEarned(Integer.parseInt(parts[7]));
        user.setNumberOfPlayedGames(Integer.parseInt(parts[8]));
        user.setIsPlaying(parts[9]);
		return user;
	}
}
