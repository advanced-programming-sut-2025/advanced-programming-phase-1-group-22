package service;

import jakarta.persistence.EntityManager;
import model.*;
import model.records.Response;
import model.relations.Player;
import repository.UserRepository;
import repository.UserRepositoryImpl;
import utils.App;
import utils.HibernateUtil;
import utils.InitialGame;
import variables.Session;
import view.Menu;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameInitService {
    EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
    UserRepository userRepository = new UserRepositoryImpl(em);
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
        if (user.getIsPlaying()) return 2;
        for (User addedUser : users) {
            if (user.getUsername().equals(addedUser.getUsername())) return 3;
        }
        users.add(user);
        return 0;
    }

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

    public Response loadGame() {
        //TODO not mine to do
        return null;
    }

    public Response gameMap(String number) {
        int mapNumber = Integer.parseInt(number) - 1;
        if (mapNumber < 0 || mapNumber > 4) return new Response("""
                Farm not found:\s
                 1 : Grassy Farm\s
                 2 : Flower Farm\s
                 3 : Blue Farm\s
                 4 : Rocky Farm\s
                 5 : Desert Farm""");
        Player player = app.getCurrentGame().getCurrentPlayer();
        player.setFarmType(FarmType.values()[mapNumber]);
        app.getCurrentGame().nextPlayer();
        if (app.getCurrentGame().getCurrentPlayer().getFarmType() != null) {
            Session.setCurrentMenu(Menu.COTTAGE);
            completeMap();
        }
        return new Response("Farm " + FarmType.values()[mapNumber] + " chosen.");
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
                Random random = new Random();
                farm = new Farm(null, FarmType.values()[random.nextInt(0, 4)]);
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
