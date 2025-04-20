package controller.mainMenu;

import controller.MenuController;
import model.User;
import model.exception.InvalidInputException;
import utils.App;
import utils.InitialGame;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainMenuController extends MenuController {
    private String newGameEmpty = "\\S+";
    private String newGamePlayers1 = "^(?<player1>\\S+)$";
    private String newGamePlayers2 = "^(?<player1>\\S+)\\s+(?<player2>\\S+)$";
    private String newGamePlayers3 = "^(?<player1>\\S+)\\s+(?<player2>\\S+)\\s+(?<player3>\\S+)$";
    private String newGamePlayers4 = "^(?<player1>\\S+)\\s+(?<player2>\\S+)\\s+(?<player3>\\S+)\\s+(?<player4>\\S+)";

    protected Matcher isMatched(String input, String pattern) {
        Matcher matcher = Pattern.compile(pattern).matcher(input);

        if (matcher.matches()) {
            return matcher;
        }
        return null;
    }

    private int isPlayerAvailable(String username, ArrayList<User> users) {
        User user = App.getInstance().findUserByUsername(username);
        if (user == null) return 1;
        if (user.isPlaying()) return 2;
        for (User addedUser : users) {
            if (user == addedUser) return 3;
        }
        users.add(user);
        return 0;
    }
    public void newGame(String players) {
        Matcher matcher;
        ArrayList<String> usernames = new ArrayList<>();
        ArrayList<User> users = new ArrayList<>();
        usernames.add(App.getInstance().getCurrentUser().getUsername());
        if (isMatched(players, newGameEmpty) != null) {
            throw new InvalidInputException("No players added");
        }
        if (isMatched(players, newGamePlayers4) != null) {
            throw new InvalidInputException("Too many players");
        }
        if ((matcher = isMatched(players, newGamePlayers1)) != null
                || (matcher = isMatched(players, newGamePlayers2)) != null
                || (matcher = isMatched(players, newGamePlayers3)) != null) {
            usernames.add(matcher.group("player1"));
        }
        if ((matcher = isMatched(players, newGamePlayers2)) != null
                || (matcher = isMatched(players, newGamePlayers3)) != null) {
            usernames.add(matcher.group("player2"));
        }
        if ((matcher = isMatched(players, newGamePlayers3)) != null) {
            usernames.add(matcher.group("player3"));
        }
        for (String username : usernames) {
            switch (isPlayerAvailable(username, users)) {
                case 1 -> throw new InvalidInputException("No user with username: " + username + " is found");
                case 2 -> throw new InvalidInputException("Username: " + username + " has already an active game");
                case 3 -> throw new InvalidInputException("Username: " + username + " has already added");
            }
        }
        if (users.isEmpty()) {
            throw new InvalidInputException("No players added");
        }
        InitialGame initialGame = new InitialGame();
        initialGame.initial(users);
    }

    public void loadGame() {
        //TODO not mine to do
    }
}
