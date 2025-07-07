package io.github.some_example_name.controller.mainMenu;


import io.github.some_example_name.MainGradle;
import io.github.some_example_name.model.FarmType;
import io.github.some_example_name.model.PlayerType;
import io.github.some_example_name.model.User;
import io.github.some_example_name.model.enums.Gender;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.service.GameInitService;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.InitialGame;
import io.github.some_example_name.variables.Session;
import io.github.some_example_name.view.GameView;
import io.github.some_example_name.view.mainMenu.StartGameMenu;
import lombok.Setter;

import java.util.ArrayList;

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
                        if (Session.getCurrentUser().getIsPlaying() == null) {
                            ArrayList<User> users = new ArrayList<>();
                            users.add(Session.getCurrentUser());
                            users.add(new User("Clara1234", "noPass", "a@b.c", "Claire", Gender.FEMALE));
                            InitialGame initialGame = new InitialGame();
                            initialGame.initial(users);
                            view.setState(1);
                            return;
                        } else {
                            view.alert("You've got an unfinished game.",5);
                        }
                        view.getNewGameButton().setChecked(false);
                    }
                    if (view.getLoadGameButton().isChecked()) {
                        view.alert(GameInitService.getInstance().loadGame().message(), 5);
                        view.getLoadGameButton().setChecked(false);
                    }
                }
                break;
                case 1: {
                    if (view.getEnterGameButton().isChecked()) {
                        int farm = view.getFarmSelection().getSelectedIndex();
                        if (farm == -1) {//TODO check if the farm is not unique
                            view.alert("Another person has chosen this farm.", 5);
                        }
                        String character = view.getPlayerSelection().getSelected();
                        if (character == null) {//TODO check if the farm is not unique
                            view.alert("Another person has chosen this player.", 5);
                        }
                        Player player = null;
                        for (Player player1 : App.getInstance().getCurrentGame().getPlayers()) {
                            if (player1.getUser().getUsername().equals(Session.getCurrentUser().getUsername())) {
                                player = player1;
                                break;
                            }
                        }
                        if (player == null) {
                            view.alert("Something went wrong", 5);
                        } else {
                            player.setFarmType(FarmType.values()[farm]);
                            player.setPlayerType(PlayerType.findInstance(character));
                            view.setState(2);
                        }
                    }
                }
                break;
                case 2: {
                    { //TODO REMOVE THE HARDCODING IN PHASE 3
                        App.getInstance().getCurrentGame().getPlayers().get(1).setPlayerType(PlayerType.EMILY);
                        App.getInstance().getCurrentGame().getPlayers().get(1).setFarmType(FarmType.FLOWER_FARM);
                    }
                    GameInitService.getInstance().initGame();
                    view.setScreen(new GameView());
                }
            }
        }
    }
}

