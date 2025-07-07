package io.github.some_example_name.view.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.controller.mainMenu.StartGameMenuController;
import io.github.some_example_name.model.FarmType;
import io.github.some_example_name.model.PlayerType;
import io.github.some_example_name.variables.Session;
import lombok.Getter;

@Getter
public class StartGameMenu implements Screen {
    private Stage stage;
    private final TextButton newGameButton;
    private final TextButton loadGameButton;
    private final TextButton enterGameButton;
    private final SelectBox<String> farmSelection;
    private final SelectBox<String> playerSelection;
    private final Integer state;
    private final Table table;
    private final Skin skin;
    private final StartGameMenuController controller;

    public StartGameMenu(StartGameMenuController controller, Skin skin, Integer state) {
        this.controller = controller;
        this.newGameButton = new TextButton("New Game!", skin);
        this.loadGameButton = new TextButton("Load Game!", skin);
        this.enterGameButton = new TextButton("Enter!", skin);
        farmSelection = new SelectBox<>(skin);
        Array<String> farmNames = new Array<>();
        for (FarmType value : FarmType.values()) {
            farmNames.add(value.getName());
        }
        farmSelection.setItems(farmNames);
        playerSelection = new SelectBox<>(skin);
        Array<String> playerNames = new Array<>();
        for (PlayerType value : PlayerType.values()) {
            if (value.getGender() == Session.getCurrentUser().getGender())
                playerNames.add(value.getName());
        }
        playerSelection.setItems(playerNames);
        this.table = new Table();
        this.skin = skin;
        this.state = state;
        controller.setView(this);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table.setFillParent(true);
        table.center();
        switch (state) {
            case 0: {
                table.add(newGameButton).width(400).row();
                table.add(loadGameButton).width(400).row();
            }
            break;
            case 1: {
                table.add(farmSelection).width(600);
                table.row().pad(10, 0, 10, 0);
                table.add(playerSelection).width(600);
                table.row().pad(10, 0, 10, 0);
                table.add(enterGameButton).width(600);
            }
            break;
        }
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        MainGradle.getInstance().getBatch().begin();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        MainGradle.getInstance().getBatch().end();
        controller.handleMainMenuButtons();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }


    public void alert(String message, Integer timer) {
        final Table alertBox = new Table(skin);
        alertBox.setSize(1000, 50);
        alertBox.setPosition(Gdx.graphics.getWidth() / 2f, 0, Align.bottom);

        Label label = new Label(message, skin);
        alertBox.add(label);
        alertBox.setBackground("progress-bar-health-knob");
        stage.addActor(alertBox);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                alertBox.remove();
            }
        }, timer);

    }

    public void setState(int i) {
        this.dispose();
        MainGradle.getInstance().setScreen(new StartGameMenu(controller, skin, i));
    }

}

