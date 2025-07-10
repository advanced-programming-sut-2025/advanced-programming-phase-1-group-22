package io.github.some_example_name.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.model.Farm;
import io.github.some_example_name.model.AnimatedSprite;
import io.github.some_example_name.model.Direction;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.shelter.ShippingBin;
import io.github.some_example_name.model.source.Seed;
import io.github.some_example_name.model.structure.Structure;
import io.github.some_example_name.service.GameService;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.GameAsset;
import io.github.some_example_name.view.GameView;
import io.github.some_example_name.view.mainMenu.FridgeMenu;
import io.github.some_example_name.view.mainMenu.InventoryMenu;
import io.github.some_example_name.view.mainMenu.NotificationMenu;
import io.github.some_example_name.view.mainMenu.TerminateMenu;
import io.github.some_example_name.view.mainMenu.ToolMenu;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PlayerController {
    GameService gameService = new GameService();
    WorldController worldController;
    private float timeSinceLastMove = 0;
    private InventoryMenu inventoryMenu = new InventoryMenu();
    private final ToolMenu toolMenu = new ToolMenu();

    public PlayerController(WorldController worldController) {
        this.worldController = worldController;
    }

    public void update(float delta){
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        Sprite playerSprite = currentPlayer.getSprites().get(0).getSprite();
        if (playerSprite instanceof AnimatedSprite) {
            ((AnimatedSprite) playerSprite).update(delta);
        }
        if (GameView.captureInput && !GameView.positionChoosing) {
            handlePlayerMovement(currentPlayer);
            handleInputs();
        }
        if (!GameView.positionChoosing){
            setCameraPosition(currentPlayer);
        }
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
                App.getInstance().getCurrentGame().getCurrentPlayer().setDirection(Direction.NORTH);
                timeSinceLastMove = 0f;
            } else
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                gameService.walk(playerX - 1, playerY);
                App.getInstance().getCurrentGame().getCurrentPlayer().setDirection(Direction.WEST);
                timeSinceLastMove = 0f;
            } else
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                gameService.walk(playerX, playerY - 1);
                App.getInstance().getCurrentGame().getCurrentPlayer().setDirection(Direction.SOUTH);
                timeSinceLastMove = 0f;
            } else
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                gameService.walk(playerX + 1, playerY);
                App.getInstance().getCurrentGame().getCurrentPlayer().setDirection(Direction.EAST);
                timeSinceLastMove = 0f;
            } else {
                ((AnimatedSprite)App.getInstance().getCurrentGame().getCurrentPlayer()
                    .getSprites().get(0).getSprite()).setLooping(false);
            }
        }
    }

    private void handleInputs(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.T)){
            inventoryMenu.setTabIndex(2);
            inventoryMenu.createMenu(GameView.stage,GameAsset.SKIN, getWorldController());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            GameView.Console.handleGlobalKey(GameView.stage);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.X) || Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            //TODO Investigate?
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            TerminateMenu terminateMenu = new TerminateMenu();
            terminateMenu.createMenu(GameView.stage,GameAsset.SKIN, getWorldController());
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
            NotificationMenu notificationMenu = new NotificationMenu();
            notificationMenu.createMenu(GameView.stage,GameAsset.SKIN, getWorldController());
            //TODO OPEN JOURNAL?
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            inventoryMenu.setTabIndex(3);
            inventoryMenu.createMenu(GameView.stage,GameAsset.SKIN, getWorldController());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            inventoryMenu.setTabIndex(4);
            inventoryMenu.createMenu(GameView.stage,GameAsset.SKIN, getWorldController());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
            inventoryMenu.setTabIndex(5);
            inventoryMenu.createMenu(GameView.stage,GameAsset.SKIN, getWorldController());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)){
            toolMenu.createMenu(GameView.stage,GameAsset.SKIN,worldController);
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            Vector3 worldCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            MainGradle.getInstance().getCamera().unproject(worldCoords);
            float worldX = worldCoords.x;
            float worldY = worldCoords.y;
            for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
                List<Seed> toRemove = new ArrayList<>();
                for (Structure structure : new ArrayList<>(farm.getStructures())) {
                    if (structure instanceof Seed seed) {
                        if (collision(seed, worldX, worldY)) {
                            toRemove.add(seed);
                        }
                    }
                }
                for (Seed seed : toRemove) {
                    worldController.showResponse(gameService.pickFromFloor(seed));
                }
            }
        }
    }

    private boolean collision(Structure structure, float worldX, float worldY) {
        Sprite sprite = structure.getSprite();
        sprite.setPosition(structure.getTiles().get(0).getX() * App.tileWidth, structure.getTiles().get(0).getY() * App.tileHeight);
        return worldX >= sprite.getX() && worldX <= sprite.getX() + sprite.getWidth() && worldY >= sprite.getY() && worldY <= sprite.getY() + sprite.getHeight();
    }
}
