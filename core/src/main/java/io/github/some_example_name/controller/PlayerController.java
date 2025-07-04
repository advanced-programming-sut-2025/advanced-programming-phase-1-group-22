package io.github.some_example_name.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.service.GameService;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.GameAsset;
import io.github.some_example_name.view.GameView;
import io.github.some_example_name.view.mainMenu.FridgeMenu;
import io.github.some_example_name.view.mainMenu.InventoryMenu;
import lombok.Getter;

@Getter
public class PlayerController {
    GameService gameService = new GameService();
    WorldController worldController;
    private float timeSinceLastMove = 0;
    private InventoryMenu inventoryMenu = new InventoryMenu();

    public PlayerController(WorldController worldController) {
        this.worldController = worldController;
    }

    public void update(){
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        currentPlayer.getSprite().setPosition(currentPlayer.getTiles().get(0).getX() * App.tileWidth,
            currentPlayer.getTiles().get(0).getY() * App.tileHeight);
        currentPlayer.getSprite().draw(MainGradle.getInstance().getBatch());
        if (GameView.captureInput) {
            handlePlayerMovement(currentPlayer);
            handleInputs();
        }
        setCameraPosition(currentPlayer);
    }

    private void setCameraPosition(Player currentPlayer){
        float cameraX = currentPlayer.getTiles().get(0).getX() * App.tileWidth;
        float cameraY = currentPlayer.getTiles().get(0).getY() * App.tileHeight;
        float viewportWidth = MainGradle.getInstance().getCamera().viewportWidth;
        float viewportHeight = MainGradle.getInstance().getCamera().viewportHeight;
        cameraX = MathUtils.clamp(cameraX, viewportWidth / 2f,160 * App.tileWidth - viewportWidth / 2f);
        cameraY = MathUtils.clamp(cameraY, viewportHeight / 2f,120 * App.tileHeight - viewportHeight / 2f);
        MainGradle.getInstance().centerCameraOnPlayer(cameraX,cameraY);
    }

    private void handlePlayerMovement(Player player){
        int playerX = player.getTiles().get(0).getX();
        int playerY = player.getTiles().get(0).getY();
        float delta = Gdx.graphics.getDeltaTime();
        timeSinceLastMove += delta;
        if (timeSinceLastMove >= 0.1f) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                gameService.walk(playerX, playerY + 1);
                timeSinceLastMove = 0f;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                gameService.walk(playerX - 1, playerY);
                timeSinceLastMove = 0f;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                gameService.walk(playerX, playerY - 1);
                timeSinceLastMove = 0f;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                gameService.walk(playerX + 1, playerY);
                timeSinceLastMove = 0f;
            }
        }
    }

    private void handleInputs(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.T)){
            //TODO Talk Menu
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            GameView.Console.handleGlobalKey(GameView.stage);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.X) || Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            //TODO Investigate?
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.E) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            inventoryMenu.setTabIndex(0);
            inventoryMenu.createMenu(GameView.stage,GameAsset.SKIN, getWorldController());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            FridgeMenu fridgeMenu = new FridgeMenu();
            fridgeMenu.createMenu(GameView.stage,GameAsset.SKIN, getWorldController());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            //TODO open Journal?
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            inventoryMenu.setTabIndex(3);
            inventoryMenu.createMenu(GameView.stage,GameAsset.SKIN, getWorldController());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            inventoryMenu.setTabIndex(4);
            inventoryMenu.createMenu(GameView.stage,GameAsset.SKIN, getWorldController());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            inventoryMenu.setTabIndex(5);
            inventoryMenu.createMenu(GameView.stage,GameAsset.SKIN, getWorldController());
        }
    }
}
