package io.github.some_example_name.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.controller.gameMenu.GameMenuController;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.Tile;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.tools.Tool;
import io.github.some_example_name.service.GameService;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.GameAsset;
import io.github.some_example_name.view.GameView;

public class CameraViewController {
    GameService gameMenuController = new GameService();
    public void update(){
        App.getInstance().getCurrentGame().getTimeAndDate().updateBatch(MainGradle.getInstance().getBatch());
        handleInput();
    }

    public void handleInput(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
            gameMenuController.C_WeatherSet("STORMY");
            gameMenuController.C_AdvanceDate("2");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            gameMenuController.nextTurn();
        }
    }
}
