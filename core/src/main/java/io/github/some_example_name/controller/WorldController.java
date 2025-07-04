package io.github.some_example_name.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.model.*;
import io.github.some_example_name.model.craft.Craft;
import io.github.some_example_name.model.dto.SpriteHolder;
import io.github.some_example_name.model.products.TreesAndFruitsAndSeeds.Tree;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.source.Crop;
import io.github.some_example_name.model.structure.Structure;
import io.github.some_example_name.model.structure.farmInitialElements.Cottage;
import io.github.some_example_name.model.structure.farmInitialElements.Lake;
import io.github.some_example_name.model.structure.farmInitialElements.GreenHouse;
import io.github.some_example_name.service.GameService;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.GameAsset;
import io.github.some_example_name.view.GameNotifier;
import io.github.some_example_name.view.GameView;
import io.github.some_example_name.view.Menu;
import io.github.some_example_name.view.mainMenu.ArtisanMenu;
import io.github.some_example_name.view.mainMenu.CraftPopUp;
import io.github.some_example_name.view.mainMenu.FridgeMenu;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class WorldController {
    private final GameNotifier notifier;
    GameService gameService = new GameService();
    ArrayList<SpriteContainer> rainDrops = new ArrayList<>();
    ArrayList<SpriteContainer> storms = new ArrayList<>();
    ArrayList<Sprite> snowDrops = new ArrayList<>();
    float delta = 0f;
    private final OrthographicCamera camera = MainGradle.getInstance().getCamera();
    {
        Random rand  = new Random();
        for (int i = 0; i < camera.viewportWidth / GameAsset.SNOW.getWidth(); i++) {
            for (int j = 0; j <= 6 * camera.viewportHeight / GameAsset.SNOW.getHeight(); j++) {
                Sprite sprite = new Sprite(GameAsset.SNOW);
                sprite.setScale(1);
                sprite.setPosition(i * sprite.getWidth(), j * sprite.getHeight() + rand.nextInt((int) sprite.getHeight()));
                snowDrops.add(sprite);
            }
        }
    }

    public WorldController() {
        notifier = new GameNotifier(GameView.stage, GameAsset.SKIN);
    }

    public void update() {
        delta += Gdx.graphics.getDeltaTime();
        printMap();
        switch (App.getInstance().getCurrentGame().getVillage().getWeather()) {
            case SNOWY -> handleSnowDrops();
            case RAINY -> handleRainDrops();
            case STORMY -> handleStorms();
        }
        if (!GameView.screenshotting) {
            handleInput();
        }
    }

    public void showResponse(Response response){
        if (response.shouldBeBack()) notifier.showMessage(response.message(), GameNotifier.MessageType.SUCCESS);
        else notifier.showMessage(response.message(), GameNotifier.MessageType.ERROR);
    }

    private void handleRainDrops() {
        Random rand = new Random();
        if (!GameView.screenshotting && rand.nextInt(25) == 4) {
            Sprite sprite = new Sprite(GameAsset.RAIN[0][0]);
            sprite.setPosition(
                MainGradle.getInstance().getCamera().position.x - camera.viewportWidth/2f + rand.nextFloat(camera.viewportWidth + 1),
                MainGradle.getInstance().getCamera().position.y + camera.viewportHeight/2f
            );
            sprite.setScale(1.875f);
            rainDrops.add(new SpriteContainer(sprite));
        }
        for (SpriteContainer rainDrop : rainDrops.stream().toList()) {
            if (!GameView.screenshotting) {
                if (rand.nextInt(300) == 4) {
                    rainDrop.setMoving(false);
                    Tuple<Float> pair = new Tuple<>(rainDrop.getSprite().getX(), rainDrop.getSprite().getY());
                    for (int i = 0; i < 10; i++) {
                        int finalI = i;
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                rainDrop.setSprite(new Sprite(GameAsset.RAIN[0][finalI + 1]));
                                rainDrop.getSprite().setPosition(pair.getX(), pair.getY());
                                rainDrop.getSprite().setScale(1.875f);
                            }
                        }, 0.3f * i + 0.1f);
                    }
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            rainDrops.remove(rainDrop);
                        }
                    }, 3.1f);
                } else if (rainDrop.getSprite().getY() + rainDrop.getSprite().getHeight() < MainGradle.getInstance().getCamera().position.y - camera.viewportHeight / 2f - 20) {
                    rainDrops.remove(rainDrop);
                } else if (rainDrop.isMoving()) {
                    rainDrop.getSprite().setY(rainDrop.getSprite().getY() - 1000 * Gdx.graphics.getDeltaTime());
                }
            }
            rainDrop.getSprite().draw(MainGradle.getInstance().getBatch());
        }
    }

    private void handleSnowDrops() {
        float offset = MainGradle.getInstance().getCamera().position.x - camera.viewportWidth/2f;
        for (Sprite snowDrop : snowDrops.stream().toList()) {
            snowDrop.setY(snowDrop.getY() - 400*Gdx.graphics.getDeltaTime());
            if (snowDrop.getY() + snowDrop.getHeight() < MainGradle.getInstance().getCamera().position.y - camera.viewportHeight/2f - 20) {
                if (!GameView.screenshotting) {
                    snowDrop.setY(MainGradle.getInstance().getCamera().position.y + camera.viewportHeight / 2f);
                }
            }
            snowDrop.setX(snowDrop.getX() + offset);
            snowDrop.draw(MainGradle.getInstance().getBatch());
            snowDrop.setX(snowDrop.getX() - offset);
        }
    }

    private void handleStorms() {
        Random rand = new Random();
        if (!GameView.screenshotting && rand.nextInt(200) == 4) {
            Sprite sprite = new Sprite(GameAsset.STORM[rand.nextInt(0,2)][rand.nextInt(0,4)]);
            sprite.setPosition(
                MainGradle.getInstance().getCamera().position.x - camera.viewportWidth/2f + rand.nextFloat(camera.viewportWidth),
                MainGradle.getInstance().getCamera().position.y + camera.viewportHeight/2f
            );
            sprite.setScale(0.938f);
            storms.add(new SpriteContainer(sprite));
        }
        for (SpriteContainer storm : storms.stream().toList()) {
            if (!GameView.screenshotting) {
                if (delta > 0.1f) {
                    storm.getSprite().setY(storm.getSprite().getY() - storm.getSprite().getHeight() / 2.5f);
                    if (storm.getSprite().getY() + storm.getSprite().getHeight() < MainGradle.getInstance().getCamera().position.y - camera.viewportHeight / 2f - 20) {
                        storms.remove(storm);
                    } else {
                        Tuple<Float> pair = new Tuple<>(storm.getSprite().getX(), storm.getSprite().getY());
                        Sprite sprite = new Sprite(GameAsset.STORM[rand.nextInt(0, 2)][rand.nextInt(0, 4)]);
                        sprite.setPosition(pair.getX(), pair.getY());
                        sprite.setScale(0.938f);
                        storm.setSprite(sprite);
                    }
                }
            }
            storm.getSprite().draw(MainGradle.getInstance().getBatch());
        }
        if (delta > 0.3f) {
            delta = 0;
        }
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            Farm farm = currentFarm();
            if (farm != null) {
                GreenHouse greenHouse = farm.getGreenHouse();
                if (!greenHouse.isBuilt()) {
                    for (Tile tile : greenHouse.getTiles()) {
                        if (distanceFromClick(tile).isOrigin()) {
                            Response resp = gameService.greenhouseBuild();
                            if (!resp.shouldBeBack()) {
                                showResponse(resp);
                            }
                        }
                    }
                }
                if (App.getInstance().getCurrentGame().getCurrentPlayer().getCurrentMenu() == Menu.COTTAGE) {
                    if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                        if (distanceFromClick(farm.getFridge().getTiles().getFirst()).isOrigin()) {
                            FridgeMenu fridgeMenu = new FridgeMenu();
                            fridgeMenu.createMenu(GameView.stage, GameAsset.SKIN, this);
                        }
                    }
                }
                for (Structure structure : farm.getStructures()) {
                    if (structure instanceof Craft) {
                        if (distanceFromClick(structure.getTiles().getFirst()).isOrigin()) {
                            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                                ArtisanMenu artisanMenu = new ArtisanMenu();
                                artisanMenu.setCraft((Craft) structure);
                                artisanMenu.createMenu(GameView.stage, GameAsset.SKIN, this);
                            } else if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                                if (((Craft) structure).getETA() != null) {
                                    CraftPopUp craftPopUp = new CraftPopUp();
                                    craftPopUp.setCraft((Craft) structure);
                                    craftPopUp.createMenu(GameView.stage, GameAsset.SKIN, this);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Farm currentFarm() {
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            if (farm.getPlayers().get(0).equals(App.getInstance().getCurrentGame().getCurrentPlayer())) {
                return farm;
            }
        }
        return null;
    }

    private Pair distanceFromClick(Tile tile) {
        Vector3 mouse = new Vector3(GameView.screenX, GameView.screenY,0);
        MainGradle.getInstance().getCamera().unproject(mouse);
        Vector2 mouseWorld = new Vector2(mouse.x,mouse.y);
        int mouseTileX = (int) Math.floor(mouseWorld.x / App.tileWidth);
        int mouseTileY = (int) Math.floor(mouseWorld.y / App.tileHeight);
        return new Pair(mouseTileX - tile.getX(), mouseTileY - tile.getY());
    }

    public void printMap() {
        Game game = App.getInstance().getCurrentGame();
        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
        Tile[][] tiles = game.tiles;
        OrthographicCamera camera = MainGradle.getInstance().getCamera();

        for (int i = 0; i < 160; i++) {
            for (int j = 0; j < 120; j++) {
                if (camera.frustum.pointInFrustum(i * App.tileWidth,j * App.tileHeight,0)){
                    MainGradle.getInstance().getBatch().draw(tiles[i][j].getTileType().getTexture(),
                        i * App.tileWidth, j * App.tileHeight,App.tileWidth,App.tileHeight);
                }
            }
        }

        for (Farm farm : game.getVillage().getFarms()) {
            for (Structure structure : farm.getStructures()) {
                if (isStructureInBond(structure)){
                    if (structure.getSprite() != null){
                        if (structure instanceof Lake){
                            for (Tile tile : structure.getTiles()) {
                                structure.getSprite().setSize(App.tileWidth,App.tileHeight);
                                structure.getSprite().setPosition(tile.getX() * App.tileWidth,
                                    tile.getY() * App.tileHeight);
                                structure.getSprite().draw(MainGradle.getInstance().getBatch());
                            }
                        }else if (structure instanceof Crop crop){
                            Sprite sprite = crop.getSprite();
                            sprite.setPosition(structure.getTiles().get(0).getX() * App.tileWidth,
                                structure.getTiles().get(0).getY() * App.tileHeight);
                            sprite.draw(MainGradle.getInstance().getBatch());
                        }
                        else if (structure instanceof Tree tree){
                            Sprite sprite = tree.getSprite();
                            sprite.setPosition(structure.getTiles().get(0).getX() * App.tileWidth,
                                structure.getTiles().get(0).getY() * App.tileHeight);
                            sprite.draw(MainGradle.getInstance().getBatch());
                        }
                        else {
                            structure.getSprite().setPosition(structure.getTiles().get(0).getX() * App.tileWidth,
                                structure.getTiles().get(0).getY() * App.tileHeight);
                            structure.getSprite().draw(MainGradle.getInstance().getBatch());
                        }
                    }
                    if (structure.getSprites() != null){
                        for (SpriteHolder sprite : structure.getSprites()) {
                            sprite.getSprite().setPosition(
                                (sprite.getOffset().getX() + structure.getTiles().get(0).getX()) * App.tileWidth,
                                (sprite.getOffset().getY() + structure.getTiles().get(0).getY()) * App.tileHeight );
                            sprite.getSprite().draw(MainGradle.getInstance().getBatch());
                        }
                    }
                }
            }
        }
//
//        for (Farm farm : farms) {
//            for (Structure structure : farm.getStructures()) {
//                String emoji = switch (structure) {
//                    case Cottage c          -> "🏡";
//                    case Lake l             -> "🌊";
//                    case Quarry q           ->  "🗿";
//                    case Craft c            -> "🔨";
//                    case FarmBuilding fb    -> "🏚️";
//                    default                 -> "  ";
//                };
//                emoji = pad2.apply(emoji);
//                if (!emoji.isBlank()) {
//                    for (Tile tile : structure.getTiles()) {
//                        str[tile.getX()][tile.getY()] = emoji;
//                    }
//                }
//            }
//        }
//
//        for (Structure structure : structures) {//                    case AnimalProduct ap   -> "🥚";

//            String emoji = switch (structure) {
//                case NPC n          -> "👴";
//                case Player p       -> "🧍";
//                case Store s        -> "🏬";
//                case NPCHouse h     -> "🏡";
//                case Fountain f     -> "🌊";
//                default             -> "  ";
//            };
//            emoji = pad2.apply(emoji);
//            if (!emoji.isBlank()) {
//                for (Tile tile : structure.getTiles()) {
//                    str[tile.getX()][tile.getY()] = emoji;
//                }
//            }
//        }

//        Player player = game.getCurrentPlayer();
//        str[player.getTiles().get(0).getX()][player.getTiles().get(0).getY()] = pad2.apply("🧍");
//
//        int xStart = Math.max(0, x - size / 2);
//        int yStart = Math.max(0, y - size / 2);
//        int xEnd   = Math.min(160, x + size / 2);
//        int yEnd   = Math.min(120, y + size / 2);
//
//        for (int i = yEnd - 1; i >= yStart; i--) {
//            for (int j = xStart; j < xEnd; j++) {
//                System.out.print(str[j][i]);
//            }
//            System.out.println();
//        }
    }

    private boolean isStructureInBond(Structure structure){
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        for (Tile tile : structure.getTiles()) {
            if (camera.frustum.pointInFrustum(tile.getX() * App.tileWidth,tile.getY() * App.tileHeight,0)) return true;
        }
        return false;
    }
}
