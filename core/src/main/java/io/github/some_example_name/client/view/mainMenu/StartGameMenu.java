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
    private final TextButton enterGameButton;
    private final TextButton wait;
    private final TextButton back;
    private final SelectBox<String> farmSelection;
    private final SelectBox<String> playerSelection;
    private final Image farmPreview;
    private final Integer state;
    private final StartGameMenuController controller;

    public StartGameMenu(Skin skin, Integer state) {
        super(skin);
        this.title.setText("PreGame Menu");
        this.controller = StartGameMenuController.getInstance();
        this.enterGameButton = new TextButton("Enter!", skin);
        this.wait = new TextButton("Wait for server response!", skin);
        this.back = new TextButton("Back", skin);
        farmSelection = new SelectBox<>(skin);
        Array<String> farmNames = new Array<>();
        for (FarmType value : FarmType.values()) {
            farmNames.add(value.getName());
        }
        farmSelection.setItems(farmNames);
        this.farmPreview = new Image(FarmType.values()[farmSelection.getSelectedIndex()].getTexture());
        playerSelection = new SelectBox<>(skin);
        this.state = state;
        controller.setView(this);
    }

    @Override
    protected void showStage() {
        switch (state) {
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
                table.row().pad(10, 0, 10, 0);
                table.add(farmPreview).width(1.2f * farmPreview.getWidth());
            }
            break;
            case 2: {
                table.add(wait).width(900);
            }
            break;
            case 3: {
                table.add(wait).width(400);
                table.add(back).width(400);
            }
            break;
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

