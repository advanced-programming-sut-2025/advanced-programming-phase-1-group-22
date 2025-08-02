package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.client.controller.mainMenu.StartGameMenuController;
import io.github.some_example_name.common.model.FarmType;
import io.github.some_example_name.common.model.PlayerType;
import io.github.some_example_name.common.variables.Session;
import lombok.Getter;

@Getter
public class StartGameMenu extends PreGameMenu {
    private final TextButton newGameButton;
    private final TextButton loadGameButton;
    private final TextButton continueDC;
    private final TextButton loadButton;
    private final TextButton enterGameButton;
    private final TextButton wait;
    private final SelectBox<String> farmSelection;
    private final SelectBox<String> playerSelection;
    private final Integer state;
    private final StartGameMenuController controller;

    public StartGameMenu(Skin skin, Integer state) {
        super(skin);
        this.controller = StartGameMenuController.getInstance();
        this.newGameButton = new TextButton("New Game!", skin);
        this.loadGameButton = new TextButton("Load Game!", skin);
        this.continueDC = new TextButton("Continue DC game", skin);
        this.loadButton = new TextButton("Load Saved Game",skin);
        this.enterGameButton = new TextButton("Enter!", skin);
        this.wait = new TextButton("Wait for server response!", skin);
        farmSelection = new SelectBox<>(skin);
        Array<String> farmNames = new Array<>();
        for (FarmType value : FarmType.values()) {
            farmNames.add(value.getName());
        }
        farmSelection.setItems(farmNames);
        playerSelection = new SelectBox<>(skin);
//        Array<String> playerNames = new Array<>();
//        for (PlayerType value : PlayerType.values()) {
//            if (value.getGender() == Session.getCurrentUser().getGender())
//                playerNames.add(value.getName());
//        }
//        playerSelection.setItems(playerNames);
        this.state = state;
        controller.setView(this);
    }

    @Override
    protected void showStage() {
        switch (state) {
            case 0: {
                table.add(newGameButton).width(400).row();
                table.add(loadGameButton).width(400).row();
                table.add(continueDC).width(400).row();
                table.add(loadButton).width(400).row();
            }
            break;
            case 1: {
                { //todo move this part on initializing
                    Array<String> playerNames = new Array<>();
                    for (PlayerType value : PlayerType.values()) {
                        if (value.getGender() == Session.getCurrentUser().getGender())
                            playerNames.add(value.getName());
                    }
                    playerSelection.setItems(playerNames);
                }
                table.add(farmSelection).width(600);
                table.row().pad(10, 0, 10, 0);
                table.add(playerSelection).width(600);
                table.row().pad(10, 0, 10, 0);
                table.add(enterGameButton).width(600);
            }
            break;
            case 2:
                table.add(wait).width(900);
        }
    }

    @Override
    public void renderStage(float delta) {
        stage.draw();
        controller.handleMainMenuButtons();
    }

    public void setState(int i) {
        setScreen(new StartGameMenu(skin, i));
    }
}

