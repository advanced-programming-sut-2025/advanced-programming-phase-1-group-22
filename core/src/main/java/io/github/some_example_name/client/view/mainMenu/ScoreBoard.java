package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.client.controller.WorldController;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.utils.App;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScoreBoard extends PopUp {
    private Runnable updateTable;

    @Override
    public void createMenu(Stage stage, Skin skin, WorldController playerController) {
        super.createMenu(stage, skin, playerController);
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        Window window = new Window("ScoreBoard", skin);
        window.setSize(camera.viewportWidth * 0.7f, camera.viewportHeight * 0.8f);
        window.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);

        Table content = new Table();
        content.setFillParent(true);
        window.add(content).expand().fill().pad(10);

        Table buttonsTable = new Table();
        Label sortLabel = new Label("Sort by:", skin);
        TextButton goldButton = new TextButton("Gold", skin);
        TextButton skillButton = new TextButton("Average Skill", skin);
        TextButton missionButton = new TextButton("Complete Missions", skin);

        buttonsTable.add(sortLabel).padRight(10);
        buttonsTable.add(goldButton).padRight(5);
        buttonsTable.add(skillButton).padRight(5);
        buttonsTable.add(missionButton);
        content.add(buttonsTable).left().padTop(100).row();

        Table playersTable = new Table();
        playersTable.defaults().pad(5);

        ScrollPane scrollPane = new ScrollPane(playersTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setScrollbarsOnTop(true);

        content.add(scrollPane).colspan(2).expand().fill().row();

        goldButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                App.getInstance().getCurrentGame().getPlayers()
                    .sort(Comparator.comparingInt(Player::getGold).reversed());
                updateTable.run();
            }
        });

        skillButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                App.getInstance().getCurrentGame().getPlayers()
                    .sort(Comparator.comparingInt(Player::getAverageAbilityLevel).reversed());
                updateTable.run();
            }
        });

        missionButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                App.getInstance().getCurrentGame().getPlayers()
                    .sort(Comparator.comparingInt(Player::getNumberOfCompleteMission).reversed());
                updateTable.run();
            }
        });

        updateTable = () -> updatePlayersTable(playersTable, App.getInstance().getCurrentGame().getPlayers(), skin);
        updateTable.run();

        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        ImageButton exitButton = provideExitButton(array);

        Group group = new Group() {
            @Override
            public void act(float delta) {
                window.setPosition(
                    (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f +
                        MainGradle.getInstance().getCamera().position.x - MainGradle.getInstance().getCamera().viewportWidth / 2,
                    (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f +
                        MainGradle.getInstance().getCamera().position.y - MainGradle.getInstance().getCamera().viewportHeight / 2
                );
                exitButton.setPosition(
                    window.getX() + window.getWidth() - exitButton.getWidth() / 2f + 16,
                    window.getY() + window.getHeight() - exitButton.getHeight() / 2f
                );
                updateTable.run();
                super.act(delta);
            }
        };
        group.addActor(window);
        group.addActor(exitButton);
        getMenuGroup().addActor(group);
    }

    private void updatePlayersTable(Table table, List<Player> players, Skin skin) {
        table.clearChildren();
        table.add(new Label("Name", skin)).left().width(250).pad(5);
        table.add(new Label("Gold", skin)).left().width(250).pad(5);
        table.add(new Label("AverageSkill", skin)).left().width(250).pad(5);
        table.add(new Label("Complete Missions", skin)).left().width(250).row();

        for (Player p : players) {
            table.add(new Label(p.getName(), skin)).left().width(250).pad(5);
            table.add(new Label(String.valueOf(p.getGold()), skin)).left().width(250).pad(5);
            table.add(new Label(String.valueOf(p.getAverageAbilityLevel()), skin)).left().width(250).pad(5);
            table.add(new Label(String.valueOf(p.getNumberOfCompleteMission()), skin)).left().width(250).row();
        }
    }

    @Override
    protected void handleDragRelease(InputEvent event, float x, float y, int pointer, Image itemImage, Salable item, Image dragImage, Boolean flag) {
    }
}
