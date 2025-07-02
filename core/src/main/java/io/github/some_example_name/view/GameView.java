package io.github.some_example_name.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.controller.GameViewController;
import io.github.some_example_name.model.*;
import io.github.some_example_name.model.enums.Gender;
import io.github.some_example_name.model.gameSundry.Sundry;
import io.github.some_example_name.model.gameSundry.SundryType;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.source.Seed;
import io.github.some_example_name.model.source.SeedType;
import io.github.some_example_name.model.tools.Hoe;
import io.github.some_example_name.model.tools.Scythe;
import io.github.some_example_name.service.GameService;
import io.github.some_example_name.utils.App;

import java.util.Random;

public class GameView implements Screen, InputProcessor {
    private final GameViewController controller = new GameViewController(this);
    public static Stage stage =  new Stage(MainGradle.getInstance().getViewport(), MainGradle.getInstance().getBatch());
    public static int screenX;
    public static int screenY;
    public static Console Console = new Console(stage);

    public GameView() {
        Game game = new Game();
        App app = App.getInstance();
        app.setCurrentGame(game);
        game.start();
        Village village = new Village();
        village.initAfterLoad();
        game.setVillage(village);
        Player player = new Player(new User("mahdi","","d","mahdi", Gender.MALE));
        player.getInventory().addProductToBackPack(new Seed(SeedType.JAZZ_SEEDS),1);
        player.getInventory().addProductToBackPack(new Sundry(SundryType.DELUXE_RETAINING_SOIL),1);
        game.addPlayer(player);
        Player player2 = new Player(new User("ali","","d","ali", Gender.MALE));
        player2.setCurrentCarrying(new Seed(SeedType.JAZZ_SEEDS));
        game.addPlayer(player2);
        FarmType.BLUE_FARM.initial();
        player.setFarmType(FarmType.BLUE_FARM);
        FarmType.FLOWER_FARM.initial();
        player2.setFarmType(FarmType.FLOWER_FARM);
        game.setCurrentPlayer(game.getPlayers().get(0));
        completeMap();

        GameService gameService = new GameService();
        gameService.C_AddDollars("1000");
        gameService.C_AddItem("wood", "1000");
//        stage = new Stage(MainGradle.getInstance().getViewport(), MainGradle.getInstance().getBatch());
    }

    private void completeMap() {
        Game game = App.getInstance().getCurrentGame();
        Village village = game.getVillage();
        for (int i = 0; i < 4; i++) {
            Player player = game.getPlayers().size() <= i ? null : game.getPlayers().get(i);
            Farm farm;
            if (player != null) {
                farm = new Farm(player, player.getFarmType());
                village.getStructures().add(player);
            } else {
                Random random = new Random();
                farm = new Farm(null, FarmType.values()[random.nextInt(0, 4)]);
            }
            village.getFarms().add(farm);
        }
        village.fillFarms();
        for (Farm farm : village.getFarms()) {
            if (farm.getPlayers().isEmpty()) continue;
            Tile tile = farm.getCottage().getTiles().get(0);
            farm.getPlayers().get(0).getTiles().add(tile);
        }
    }

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

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
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
        controller.update();
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
