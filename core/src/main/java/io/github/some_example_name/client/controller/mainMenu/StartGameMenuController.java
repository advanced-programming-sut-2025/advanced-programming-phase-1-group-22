package io.github.some_example_name.client.controller.mainMenu;


import com.badlogic.gdx.Gdx;
import com.google.gson.JsonArray;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.common.model.Farm;
import io.github.some_example_name.common.model.FarmType;
import io.github.some_example_name.common.model.PlayerType;
import io.github.some_example_name.common.model.User;
import io.github.some_example_name.common.model.enums.Gender;
import io.github.some_example_name.common.model.enums.SecurityQuestion;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.server.repository.UserRepo;
import io.github.some_example_name.server.service.GameInitService;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.InitialGame;
import io.github.some_example_name.common.variables.Session;
import io.github.some_example_name.client.view.GameView;
import io.github.some_example_name.client.view.mainMenu.StartGameMenu;
import lombok.Setter;

import java.util.Optional;

@Setter
public class StartGameMenuController {
    private StartGameMenu view;
    private static StartGameMenuController instance;

    public static StartGameMenuController getInstance() {
        if (instance == null) {
            instance = new StartGameMenuController();
        }
        return instance;
    }

    public void handleMainMenuButtons() {
        if (view != null) {
            switch (view.getState()) {
                case 0: {
                    if (view.getNewGameButton().isChecked()) {
                        { //todo delete hardcode
                            if (Session.getCurrentUser() == null) {
                                UserRepo userRepo = new UserRepo();
//                                Session.setCurrentUser(new User("Roham1234", "pass", "a@a.a", "Roham", Gender.MALE, SecurityQuestion.QUESTION1, "snf"));
                                Session.setCurrentUser(userRepo.findByUsername("Roham1234").get());
                            }
                        }
                        if (Session.getCurrentUser().getIsPlaying() == null) {
                            InitialGame initialGame = new InitialGame();
                            initialGame.initial();
                            view.setState(1);
                            return;
                        } else {
                            view.alert("You've got an unfinished game.",5);
                        }
                        view.getNewGameButton().setChecked(false);
                    }
                    if (view.getLoadGameButton().isChecked()) {
                        { //todo delete hardcode
                            UserRepo userRepo = new UserRepo();
//                        Session.setCurrentUser(new User("Clara1234", "noPass", "a@b.c", "Claire", Gender.FEMALE, SecurityQuestion.QUESTION1,"snf"));
                            Session.setCurrentUser(userRepo.findByUsername("Clara1234").get());
                        }
                        view.alert(GameInitService.getInstance().loadGame().message(), 5);
                        view.getLoadGameButton().setChecked(false);
                    }
                }
                break;
                case 1: {
                    if (view.getEnterGameButton().isChecked()) {
                        int farm = view.getFarmSelection().getSelectedIndex();
                        String character = view.getPlayerSelection().getSelected();
                        GameClient.getInstance().chooseFarm(farm, character);
                        view.getEnterGameButton().setChecked(false);
                    }
                }
                break;
            }
        }
    }

    public void responseToChooseFarm(String response) {
        if (response.equals("Good!")) {
            GameClient.getInstance().readyForGame();
            Gdx.app.postRunnable(() -> view.setState(2));
        } else {
            Gdx.app.postRunnable(() -> view.alert(response, 5));
        }
    }

    public void startGame(JsonArray players, JsonArray farms, JsonArray characters) {
        UserRepo userRepository = new UserRepo();
        Player current = null;
        for (int i = 0; i < players.size(); i++) {
            Optional<User> user = userRepository.findByUsername(players.get(i).getAsString());
            if (user.isEmpty()) {
                view.alert("Player " + players.get(i).getAsString() + " not found.", 5);
                return;
            }
            Player player = new Player(user.get());
            if (user.get().getUsername().equals(Session.getCurrentUser().getUsername())) current = player;
            App.getInstance().getCurrentGame().addPlayer(player);
            player.setFarmType(FarmType.values()[farms.get(i).getAsInt()]);
            player.setPlayerType(PlayerType.findInstance(characters.get(i).getAsString()));
        }
        App.getInstance().getCurrentGame().setCurrentPlayer(current);
        GameInitService.getInstance().initGame();
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            if (!farm.getPlayers().isEmpty() && farm.getPlayers().get(0).equals(App.getInstance().getCurrentGame().getCurrentPlayer()))
                farm.generateRandomStructures();
        }
        Gdx.app.postRunnable(() -> view.setScreen(new GameView()));
    }
}

