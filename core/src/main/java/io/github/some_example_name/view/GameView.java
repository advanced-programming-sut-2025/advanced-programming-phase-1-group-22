package io.github.some_example_name.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.controller.GameViewController;
import io.github.some_example_name.model.*;
import io.github.some_example_name.model.animal.Animal;
import io.github.some_example_name.model.animal.AnimalType;
import io.github.some_example_name.model.enums.Gender;
import io.github.some_example_name.model.gameSundry.Sundry;
import io.github.some_example_name.model.gameSundry.SundryType;
import io.github.some_example_name.model.products.Hay;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.source.Seed;
import io.github.some_example_name.model.source.SeedType;
import io.github.some_example_name.model.tools.MilkPail;
import io.github.some_example_name.service.GameService;
import io.github.some_example_name.service.RelationService;
import io.github.some_example_name.utils.App;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView implements Screen, InputProcessor {
    private final GameViewController controller = new GameViewController(this);
    public static Stage stage =  new Stage(MainGradle.getInstance().getViewport(), MainGradle.getInstance().getBatch());
    public static int screenX;
    public static int screenY;
    public static Console Console = new Console(stage);
    public static boolean screenshotting;
    public static boolean captureInput = true;
    public static boolean positionChoosing = false;
    public static String building;


    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        if (positionChoosing){
            Vector3 worldCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            MainGradle.getInstance().getCamera().unproject(worldCoords);
            float worldX = worldCoords.x;
            float worldY = worldCoords.y;
            positionChoosing = false;
            controller.getStoreController().build(building, (int) (worldX / App.tileWidth), (int) (worldY / App.tileHeight));
        }
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        screenX = i;
        screenY = i1;
        controller.getToolController().handleToolRotation(i,i1);
        return false;
    }


    public static float scrollY = 0;

    @Override
    public boolean scrolled(float amountX, float amountY) {
        scrollY += amountY;
        return true;
    }

    @Override
    public void show() {
        //Gdx.input.setInputProcessor(this);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0, 1);
        MainGradle.getInstance().getBatch().begin();
        controller.update(v);
        MainGradle.getInstance().getBatch().end();
        stage.act(v);
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {
        MainGradle.getInstance().getCamera().viewportWidth = i;
        MainGradle.getInstance().getCamera().viewportHeight = i1;
        MainGradle.getInstance().getCamera().update();
        stage.getViewport().update(i, i1, true);
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
        if (stage!=null) stage.dispose();
    }
}
