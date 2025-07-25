package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import io.github.some_example_name.client.MainGradle;
import lombok.Getter;

@Getter
public abstract class PreGameMenu implements Screen {
    protected Stage stage;
    protected final Table table;
    protected final Skin skin;

    public PreGameMenu(Skin skin) {
        this.table = new Table();
        this.skin = skin;
    }

    protected abstract void showStage();

    protected abstract void renderStage(float delta);

    @Override
    public void show() {
        stage = new Stage(MainGradle.getInstance().getViewport(), MainGradle.getInstance().getBatch());
        Gdx.input.setInputProcessor(stage);

        table.setFillParent(true);
        table.center();
        showStage();
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        MainGradle.getInstance().getBatch().begin();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        MainGradle.getInstance().getBatch().end();
        renderStage(delta);
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) stage.getViewport().update(width, height, true);
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
        if (stage != null) stage.dispose();
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

    public void setScreen(Screen screen) {
        this.dispose();
        MainGradle.getInstance().setScreen(screen);
    }

}

