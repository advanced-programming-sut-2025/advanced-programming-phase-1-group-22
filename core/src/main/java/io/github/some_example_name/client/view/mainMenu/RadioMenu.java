package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.client.controller.WorldController;
import io.github.some_example_name.common.model.Salable;

import java.util.ArrayList;

public class RadioMenu extends PopUp {

    @Override
    public void createMenu(Stage stage, Skin skin, WorldController playerController) {
        super.createMenu(stage, skin, playerController);
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        Window window = new Window("Radio Menu", skin);
        window.setSize(camera.viewportWidth * 0.9f, camera.viewportHeight * 0.6f);
        window.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);

        Table content = new Table(skin);
        content.setFillParent(true);
        content.defaults().pad(10);

        Table buttonContainer = new Table(skin);
        buttonContainer.defaults().pad(8).fillX();

        TextButton addMusic = new TextButton("Add", skin);
        TextButton connectToOther = new TextButton("📡Connect to other", skin);
        TextButton disconnect = new TextButton("Disconnect", skin);

        buttonContainer.add(addMusic).row();
        buttonContainer.add(connectToOther).row();
        buttonContainer.add(disconnect).row();

        Table musics = new Table(skin);
        musics.defaults().pad(5);

        Table musicRow = new Table(skin);
        Label label = new Label("🎵 track1", skin);
        TextButton play = new TextButton("Play", skin);
        TextButton stop = new TextButton("Stop", skin);
        TextButton remove = new TextButton("Remove", skin);

        musicRow.add(label).left().padRight(10);
        musicRow.add(play).padRight(5);
        musicRow.add(stop).padRight(5);
        musicRow.add(remove);

        musics.add(musicRow).left().row();

        ScrollPane scrollPane = new ScrollPane(musics, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setScrollbarsOnTop(true);

        content.add(buttonContainer).width(180).top().left().padRight(30).padTop(50).padLeft(50);
        content.add(scrollPane).padLeft(60).expand().fill();

        window.add(content).expand().fill().pad(10);

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
                super.act(delta);
            }
        };
        group.addActor(window);
        group.addActor(exitButton);
        getMenuGroup().addActor(group);
    }

    @Override
    protected void handleDragRelease(InputEvent event, float x, float y, int pointer, Image itemImage, Salable item, Image dragImage, Boolean flag) {
    }
}
