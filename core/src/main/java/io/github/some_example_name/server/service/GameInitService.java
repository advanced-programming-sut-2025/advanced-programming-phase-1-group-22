package io.github.some_example_name.server.service;

import io.github.some_example_name.common.model.*;
import io.github.some_example_name.server.repository.UserRepo;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.server.repository.UserRepository;
import io.github.some_example_name.server.saveGame.GameSerializer;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.InitialGame;
import io.github.some_example_name.common.variables.Session;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameInitService {
    //EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
    //UserRepository userRepository = new UserRepositoryImpl(em);
    UserRepository userRepository = new UserRepo();
    private static GameInitService instance;
    App app = App.getInstance();


    private GameInitService() {
    }

    public static GameInitService getInstance() {
        if (instance == null) {
            instance = new GameInitService();
        }
        return instance;
    }

    private Matcher isMatched(String input, String pattern) {
        Matcher matcher = Pattern.compile(pattern).matcher(input);

        if (matcher.matches()) {
            return matcher;
        }
        return null;
    }

    private int isPlayerAvailable(String username, ArrayList<User> users) {
        Optional byUsername = userRepository.findByUsername(username);
        if (byUsername.isEmpty()) {
            return 1;
        }
        User user = (User) byUsername.get();
        if (user.getIsPlaying() != null) return 2;
        for (User addedUser : users) {
            if (user.getUsername().equals(addedUser.getUsername())) return 3;
        }
        users.add(user);
        return 0;
    }

    /*

    public Response newGame(String players) {
        Matcher matcher;
        String newGameEmpty = "^\\s*$";
        String newGamePlayers1 = "^(?<player1>\\S+)$";
        String newGamePlayers2 = "^(?<player1>\\S+)\\s+(?<player2>\\S+)$";
        String newGamePlayers3 = "^(?<player1>\\S+)\\s+(?<player2>\\S+)\\s+(?<player3>\\S+)$";
        String newGamePlayers4 = "^(?<player1>\\S+)\\s+(?<player2>\\S+)\\s+(?<player3>\\S+)\\s+(?<player4>\\S+)";

        ArrayList<String> usernames = new ArrayList<>();
        ArrayList<User> users = new ArrayList<>();
        usernames.add(Session.getCurrentUser().getUsername());
        if (players == null || isMatched(players, newGameEmpty) != null) {
            return new Response("No players added");
        }
        if (isMatched(players, newGamePlayers4) != null) {
            return new Response("Too many players");
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
                case 1 -> {
                    return new Response("No user with username: " + username + " is found");
                }
                case 2 -> {
                    return new Response("Username: " + username + " has already an active game");
                }
                case 3 -> {
                    return new Response("Username: " + username + " has been already added");
                }
            }
        }
        if (users.isEmpty()) {
            return new Response("No players added");
        }
        InitialGame initialGame = new InitialGame();
        initialGame.initial(users);
        return new Response("The game is starting, please players choose their maps", true);
    }

     */

    public Response loadGame() {
        Game game = null;
        String savedGamePath = Session.getCurrentUser().getIsPlaying();
        if (savedGamePath == null) {
            return new Response("You don't have Saved game!");
        }
        game = GameSerializer.loadGame(savedGamePath);
        App.getInstance().setCurrentGame(game);
        return new Response("The game has been loaded");
    }

    public void initGame() {
        completeMap();
    }


    private void completeMap() {
        Game game = App.getInstance().getCurrentGame();
        Village village = game.getVillage();
        for (int i = 0; i < 4; i++) {
            Player player = game.getPlayers().size() <= i ? null : game.getPlayers().get(i);
            Farm farm;
            if (player != null) {
                farm = new Farm(player, player.getFarmType());
                village.getStructures().add(player);
            } else {
                farm = new Farm(null, FarmType.values()[game.getPlayers().get(i - 2).getUser().getUsername().length() % 4]);
            }
            village.getFarms().add(farm);
        }
        village.fillFarms();
        for (Farm farm : village.getFarms()) {
            if (farm.getPlayers().isEmpty()) continue;
            Tile tile = farm.getCottage().getTiles().get(0);
            farm.getPlayers().get(0).getTiles().add(tile);
        }
    }
}
