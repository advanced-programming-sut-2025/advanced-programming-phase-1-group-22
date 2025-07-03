package io.github.some_example_name.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
    private Sprite energyContainer = new Sprite(GameAsset.BUTTON);
    private Sprite energy = new Sprite(GameAsset.GREEN_SQUARE);
    private Sprite eSprite = new Sprite(GameAsset.FILLED_BUTTON);
    private OrthographicCamera camera = MainGradle.getInstance().getCamera();
    public void update() {
        App.getInstance().getCurrentGame().getTimeAndDate().updateBatch(MainGradle.getInstance().getBatch());
        handleEnergyBar();
        handleInput();
    }

    private void handleEnergyBar() {
        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
        energyContainer.setSize(camera.viewportWidth * 0.03f,
                camera.viewportHeight * 0.2f / 200 * player.getMaxEnergy());
        energyContainer.setPosition(camera.position.x + camera.viewportWidth/2f - energyContainer.getWidth()*1.1f,
                camera.position.y - camera.viewportHeight/2f + energyContainer.getHeight()*0.3f);
        energy.setSize(energyContainer.getWidth() * 0.8f,
                energyContainer.getHeight() * player.getEnergy() / player.getMaxEnergy() * 0.94f);
        energy.setPosition(energyContainer.getX() + 0.1f * energyContainer.getWidth(),
                energyContainer.getY() + 0.03f * energyContainer.getHeight());
        eSprite.setSize(energyContainer.getWidth(), energyContainer.getWidth());
        eSprite.setPosition(energyContainer.getX(), energyContainer.getY() + energyContainer.getHeight()*0.90f);

        energyContainer.draw(MainGradle.getInstance().getBatch());
        energy.draw(MainGradle.getInstance().getBatch());
        eSprite.draw(MainGradle.getInstance().getBatch());
        GameAsset.MAIN_FONT.draw(MainGradle.getInstance().getBatch(), "E",
                eSprite.getX() + eSprite.getWidth()*0.2f, eSprite.getY() + eSprite.getHeight()*0.8f);
    }

    public void handleInput(){
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                gameMenuController.C_WeatherSet("STORMY");
                gameMenuController.C_AdvanceDate("2");
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
                gameMenuController.C_WeatherSet("RAINY");
                gameMenuController.C_AdvanceDate("1");
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
                gameMenuController.C_WeatherSet("SNOWY");
                gameMenuController.C_AdvanceDate("1");
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                gameMenuController.nextTurn();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
                gameMenuController.C_AddItem("cherry bomb recipe", "1");
                gameMenuController.C_AddItem("bomb recipe", "1");
                gameMenuController.C_AddItem("mega bomb recipe", "1");
                gameMenuController.C_AddItem("Sprinklers recipe", "1");
                gameMenuController.C_AddItem("quality Sprinklers recipe", "1");
                gameMenuController.C_AddItem("iridium Sprinklers recipe", "1");
                gameMenuController.C_AddItem("charcoal klin recipe", "1");
                gameMenuController.C_AddItem("furnace recipe", "1");
                gameMenuController.C_AddItem("scare crow recipe", "1");
                gameMenuController.C_AddItem("deluxe scarecrow recipe", "1");
                gameMenuController.C_AddItem("bee house recipe", "1");
                gameMenuController.C_AddItem("cheese press recipe", "1");
                gameMenuController.C_AddItem("keg recipe", "1");
                gameMenuController.C_AddItem("loom recipe", "1");
                gameMenuController.C_AddItem("mayonnaise machine recipe", "1");
                gameMenuController.C_AddItem("oil maker recipe", "1");
                gameMenuController.C_AddItem("preserves jar recipe", "1");
                gameMenuController.C_AddItem("dehydrator recipe", "1");
                gameMenuController.C_AddItem("grass started recipe", "1");
                gameMenuController.C_AddItem("fish smoker recipe", "1");
                gameMenuController.C_AddItem("mystic tree seed recipe", "1");
                gameMenuController.C_AddItem("fiber", "60");
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
                gameMenuController.C_AddItem("fried egg recipe", "1");
                gameMenuController.C_AddItem("baked fish recipe", "1");
                gameMenuController.C_AddItem("salad recipe", "1");
                gameMenuController.C_AddItem("salmon dinner recipe", "1");
                gameMenuController.C_AddItem("hashbrowns recipe", "1");
                gameMenuController.C_AddItem("omelet recipe", "1");
                gameMenuController.C_AddItem("pancakes recipe", "1");
                gameMenuController.C_AddItem("bread recipe", "1");
                gameMenuController.C_AddItem("tortilla recipe", "1");
                gameMenuController.C_AddItem("pizza recipe", "1");
                gameMenuController.C_AddItem("maki roll recipe", "1");
                gameMenuController.C_AddItem("triple shot espresso recipe", "1");
                gameMenuController.C_AddItem("cookie recipe", "1");
                gameMenuController.C_AddItem("vegetable medley recipe", "1");
                gameMenuController.C_AddItem("farmer's lunch recipe", "1");
                gameMenuController.C_AddItem("survival burger recipe", "1");
                gameMenuController.C_AddItem("dish O' the Sea recipe", "1");
                gameMenuController.C_AddItem("seafoam pudding recipe", "1");
                gameMenuController.C_AddItem("miner's treat recipe", "1");
                gameMenuController.C_AddItem("triple shot espresson recipe", "1");
                gameMenuController.C_AddItem("triple shot espresson recipe", "1");

            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) {
                gameMenuController.C_AddItem("pizza", "1");
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) {
                gameMenuController.C_AddItem("bee_house", "1");
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) {
                gameMenuController.placeItem("bee_house", "south");
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F4)){
            GameView.screenshotting = true;
        }
    }
}
