package io.github.some_example_name.client.controller.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.google.gson.JsonArray;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.client.view.mainMenu.LobbyMenu;
import io.github.some_example_name.common.model.Farm;
import io.github.some_example_name.common.model.FarmType;
import io.github.some_example_name.common.model.PlayerType;
import io.github.some_example_name.common.model.User;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.utils.GameAsset;
import io.github.some_example_name.server.repository.UserRepo;
import io.github.some_example_name.server.service.GameInitService;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.variables.Session;
import io.github.some_example_name.client.view.GameView;
import io.github.some_example_name.client.view.mainMenu.StartGameMenu;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Setter
public class StartGameMenuController {
    private StartGameMenu view;
    private static StartGameMenuController instance;
    @Getter
    private boolean reconnect = false;
    @Getter
    private boolean load = false;
    private int selectedIndex = 0;

    public static StartGameMenuController getInstance() {
        if (instance == null) {
            instance = new StartGameMenuController();
        }
        return instance;
    }

    public void handleMainMenuButtons() {
        if (view != null) {
            switch (view.getState()) {
                case 1: {
                    if (view.getFarmSelection().getSelectedIndex() != selectedIndex) {
                        selectedIndex = view.getFarmSelection().getSelectedIndex();
                        view.getFarmPreview().setDrawable(new TextureRegionDrawable(new TextureRegion(
                            FarmType.values()[selectedIndex].getTexture()
                        )));
                    }
                    if (view.getEnterGameButton().isChecked()) {
                        int farm = view.getFarmSelection().getSelectedIndex();
                        String character = view.getPlayerSelection().getSelected();
                        GameClient.getInstance().chooseFarm(farm, character);
                        view.getEnterGameButton().setChecked(false);
                    }
                }
                break;
                case 3: {
                    if (view.getBack().isChecked()) {
                        GameClient.getInstance().giveUpLoad(App.getInstance().getCurrentLobby());
                        App.getInstance().setCurrentLobby(null);
                        reconnect = false;
                        load = false;
                        Gdx.app.postRunnable(() -> MainGradle.getInstance().setScreen(new LobbyMenu(GameAsset.SKIN_MENU)));
                        view.getBack().setChecked(false);
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
        if (!reconnect) {
            for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
                if (!farm.getPlayers().isEmpty() && farm.getPlayers().get(0).equals(App.getInstance().getCurrentGame().getCurrentPlayer()))
                    farm.generateRandomStructures();
            }
        }
        Gdx.app.postRunnable(() -> {
            MainGradle.getInstance().setScreen(new GameView());
        });
    }
}
