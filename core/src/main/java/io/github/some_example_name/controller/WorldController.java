package io.github.some_example_name.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.model.*;
import io.github.some_example_name.model.animal.Animal;
import io.github.some_example_name.model.craft.Craft;
import io.github.some_example_name.model.dto.SpriteComponent;
import io.github.some_example_name.model.dto.SpriteHolder;
import io.github.some_example_name.model.products.TreesAndFruitsAndSeeds.Tree;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.model.relations.NPC;
import io.github.some_example_name.model.relations.NPCHouse;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.shelter.FarmBuilding;
import io.github.some_example_name.model.source.Crop;
import io.github.some_example_name.model.structure.Lightening;
import io.github.some_example_name.model.structure.Structure;
import io.github.some_example_name.model.structure.farmInitialElements.Lake;
import io.github.some_example_name.model.structure.farmInitialElements.GreenHouse;
import io.github.some_example_name.service.GameService;
import io.github.some_example_name.service.RelationService;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.GameAsset;
import io.github.some_example_name.view.GameNotifier;
import io.github.some_example_name.view.GameView;
import io.github.some_example_name.view.Menu;
import io.github.some_example_name.view.mainMenu.ArtisanMenu;
import io.github.some_example_name.view.mainMenu.CraftPopUp;
import io.github.some_example_name.view.mainMenu.FridgeMenu;
import io.github.some_example_name.view.mainMenu.FriendPopUp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldController {
    private final GameNotifier notifier;
    private GameService gameService = new GameService();
    private ArrayList<SpriteContainer> rainDrops = new ArrayList<>();
    private ArrayList<SpriteContainer> storms = new ArrayList<>();
    private ArrayList<Sprite> snowDrops = new ArrayList<>();
    private SpriteHolder flower;
    private SpriteHolder heart;
    private Direction flowerDirection;
    float delta = 0f;
    private final OrthographicCamera camera = MainGradle.getInstance().getCamera();

    {
        Random rand = new Random();
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

    public void update(float delta) {
        this.delta += delta;
        printMap(delta);
        switch (App.getInstance().getCurrentGame().getVillage().getWeather()) {
            case SNOWY -> handleSnowDrops();
            case RAINY -> handleRainDrops();
            case STORMY -> handleStorms();
        }
        if (GameView.captureInput) {
            handleInput();
        }
    }

    public void showResponse(Response response) {
        if (response.shouldBeBack()) notifier.showMessage(response.message(), GameNotifier.MessageType.SUCCESS);
        else notifier.showMessage(response.message(), GameNotifier.MessageType.ERROR);
    }

    private void handleRainDrops() {
        Random rand = new Random();
        if (!GameView.screenshotting && rand.nextInt(25) == 4) {
            Sprite sprite = new Sprite(GameAsset.RAIN[0][0]);
            sprite.setPosition(
                MainGradle.getInstance().getCamera().position.x - camera.viewportWidth / 2f + rand.nextFloat(camera.viewportWidth + 1),
                MainGradle.getInstance().getCamera().position.y + camera.viewportHeight / 2f
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
        float offset = MainGradle.getInstance().getCamera().position.x - camera.viewportWidth / 2f;
        for (Sprite snowDrop : snowDrops.stream().toList()) {
            snowDrop.setY(snowDrop.getY() - 400 * Gdx.graphics.getDeltaTime());
            if (snowDrop.getY() + snowDrop.getHeight() < MainGradle.getInstance().getCamera().position.y - camera.viewportHeight / 2f - 20) {
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
        if (rand.nextInt(2000) == 4) {
            gameService.C_Thor("" + rand.nextInt(160), "" + rand.nextInt(120));
        }
        if (!GameView.screenshotting && rand.nextInt(200) == 4) {
            Sprite sprite = new Sprite(GameAsset.STORM[rand.nextInt(0, 2)][rand.nextInt(0, 4)]);
            sprite.setPosition(
                MainGradle.getInstance().getCamera().position.x - camera.viewportWidth / 2f + rand.nextFloat(camera.viewportWidth),
                MainGradle.getInstance().getCamera().position.y + camera.viewportHeight / 2f
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
                            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                                Response resp = gameService.greenhouseBuild();
                                if (!resp.shouldBeBack()) {
                                    showResponse(resp);
                                }
                            }
                        }
                    }
                }
                if (App.getInstance().getCurrentGame().getCurrentPlayer().getCurrentMenu() == Menu.COTTAGE) {
                    if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                        if (distanceFromClick(farm.getFridge().getTiles().get(0)).isOrigin()) {
                            FridgeMenu fridgeMenu = new FridgeMenu();
                            fridgeMenu.createMenu(GameView.stage, GameAsset.SKIN, this);
                        }
                    }
                }
                for (Structure structure : farm.getStructures()) {
                    if (structure instanceof Craft) {
                        if (distanceFromClick(structure.getTiles().get(0)).isOrigin()) {
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
            for (Player player : App.getInstance().getCurrentGame().getPlayers()) {
                if (App.getInstance().getCurrentGame().getCurrentPlayer() == player) continue;
                if (distanceFromClick(player.getTiles().get(0)).isOrigin()) {
                    if (RelationService.getInstance().twoActorsAreNeighbors(
                        App.getInstance().getCurrentGame().getCurrentPlayer(), player, 1)) {
                        FriendPopUp friendPopUp = new FriendPopUp();
                        friendPopUp.setPlayer(player);
                        friendPopUp.createMenu(GameView.stage, GameAsset.SKIN, this);
                    }
                }
            }
        }
    }

    private Farm currentFarm() {
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            if (farm.getTiles().contains(App.getInstance().getCurrentGame().getCurrentPlayer().getTiles().getFirst())) {
                return farm;
            }
        }
        return null;
    }

    private Pair distanceFromClick(Tile tile) {
        Vector3 mouse = new Vector3(GameView.screenX, GameView.screenY, 0);
        MainGradle.getInstance().getCamera().unproject(mouse);
        Vector2 mouseWorld = new Vector2(mouse.x, mouse.y);
        int mouseTileX = (int) Math.floor(mouseWorld.x / App.tileWidth);
        int mouseTileY = (int) Math.floor(mouseWorld.y / App.tileHeight);
        return new Pair(mouseTileX - tile.getX(), mouseTileY - tile.getY());
    }

    private void drawRawSprite(float delta, Sprite sprite, Structure structure) {

        sprite.setPosition(structure.getTiles().get(0).getX() * App.tileWidth,
            structure.getTiles().get(0).getY() * App.tileHeight);
        int width = structure.getWidth() == null ? 1 : structure.getWidth();
        int height = structure.getHeight() == null ? 1 : structure.getHeight();
        sprite.setPosition((width/2f + structure.getTiles().getFirst().getX()) * App.tileWidth - sprite.getWidth()/2,
            (height/2f + structure.getTiles().getFirst().getY()) * App.tileHeight - sprite.getHeight()/2 );
        sprite.draw(MainGradle.getInstance().getBatch());
        if (sprite instanceof AnimatedSprite animatedSprite) {
            animatedSprite.update(delta);
        }
    }

    private void drawSpriteHolder(float delta, SpriteHolder sprite, Structure structure) {
        sprite.getSprite().setPosition(
            (sprite.getOffset().getX() + structure.getTiles().getFirst().getX()) * App.tileWidth,
            (sprite.getOffset().getY() + structure.getTiles().getFirst().getY()) * App.tileHeight);
        sprite.getSprite().draw(MainGradle.getInstance().getBatch());
        if (sprite.getSprite() instanceof AnimatedSprite animatedSprite) {
            animatedSprite.update(delta);
        }
    }

    private void drawSpriteComponent(float delta, SpriteComponent sprite, Structure structure) {
        ArrayList<SpriteHolder> spriteHolders = structure.getSpriteComponent().getSprites(delta);
        for (SpriteHolder spriteHolder : spriteHolders) {
            spriteHolder.getSprite().setPosition(
                (sprite.getOffset().getX() + spriteHolder.getOffset().getX() + structure.getTiles().getFirst().getX()) * App.tileWidth,
                (sprite.getOffset().getY() + spriteHolder.getOffset().getY() + structure.getTiles().getFirst().getY()) * App.tileHeight
            );
            spriteHolder.getSprite().draw(MainGradle.getInstance().getBatch());
        }
    }

    public void printMap(float delta) {
        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
        Game game = App.getInstance().getCurrentGame();
        Tile[][] tiles = game.tiles;
        OrthographicCamera camera = MainGradle.getInstance().getCamera();

        for (int i = 0; i < 160; i++) {
            for (int j = 0; j < 120; j++) {
                if (camera.frustum.pointInFrustum(i * App.tileWidth, j * App.tileHeight, 0)) {
                    MainGradle.getInstance().getBatch().draw(tiles[i][j].getTileType().getTexture(),
                        i * App.tileWidth, j * App.tileHeight, App.tileWidth, App.tileHeight);
                }
            }
        }

        for (Farm farm : game.getVillage().getFarms()) {
            for (Structure structure : farm.getStructures()) {
                if (isStructureInBond(structure)) {
                    if (structure.getSprite() != null) {
                        if (structure instanceof Lake lake) {
                            for (Tile tile : structure.getTiles()) {
                                structure.getSprite().setSize(App.tileWidth, App.tileHeight);
                                structure.getSprite().setPosition(tile.getX() * App.tileWidth,
                                    tile.getY() * App.tileHeight);
                                structure.getSprite().draw(MainGradle.getInstance().getBatch());
                            }
                        } else if (structure instanceof Animal animal) {

                            if (isAnimalBuildingCollision(farm.getStructures(), animal, player)) {
                                structure.getSprite().setPosition(structure.getTiles().get(0).getX() * App.tileWidth,
                                    structure.getTiles().get(0).getY() * App.tileHeight);
                                structure.getSprite().draw(MainGradle.getInstance().getBatch());
                            }else if (animal.getIsAnimalStayOutAllNight()){
                                structure.getSprite().setPosition(structure.getTiles().get(0).getX() * App.tileWidth,
                                    structure.getTiles().get(0).getY() * App.tileHeight);
                                structure.getSprite().draw(MainGradle.getInstance().getBatch());
                            }

                        } else if (structure instanceof FarmBuilding farmBuilding) {
                            if (collision(player, farmBuilding)) {
                                drawRawSprite(delta, ((FarmBuilding) structure).getSpriteInterior(), structure);
                            } else {
                                drawRawSprite(delta, structure.getSprite(), structure);
                            }
                        } else {
                            drawRawSprite(delta, structure.getSprite(), structure);
                        }
                    }
                    if (structure.getSprites() != null) {
                        for (SpriteHolder sprite : structure.getSprites()) {
                            drawSpriteHolder(delta, sprite, structure);
                        }
                    }
                    if (structure.getSpriteComponent() != null) {
                        drawSpriteComponent(delta, structure.getSpriteComponent(), structure);
                    }
                }
            }
        }
        for (Structure structure : App.getInstance().getCurrentGame().getVillage().getStructures()) {
            if (structure.getSprite() != null) {
                if (structure instanceof NPC npc) {
                    drawRawSprite(delta, structure.getSprite(), structure);
                    if (npc.isHaveDialog()) {
                        npc.getSpriteDialogBox().setPosition(structure.getTiles().get(0).getX() * App.tileWidth,
                            structure.getTiles().get(0).getY() * App.tileHeight + npc.getSprite().getHeight());
                        npc.getSpriteDialogBox().draw(MainGradle.getInstance().getBatch());
                    }
                } else {
                    drawRawSprite(delta, structure.getSprite(), structure);
                }
            }
            if (structure.getSprites() != null) {
                for (SpriteHolder sprite : structure.getSprites()) {
                    drawSpriteHolder(delta, sprite, structure);
                }
            }
            if (structure.getSpriteComponent() != null) {
                drawSpriteComponent(delta, structure.getSpriteComponent(), structure);
            }
        }
        if (flower != null) {
            flower.getSprite().setX(flower.getSprite().getX() + flowerDirection.getXTransmit() * App.tileWidth * delta / 4);
            flower.getSprite().setY(flower.getSprite().getY() + flowerDirection.getYTransmit() * App.tileHeight * delta / 4);
            flower.getSprite().draw(MainGradle.getInstance().getBatch());
        }
        if (heart != null) {
            heart.getSprite().setY(heart.getSprite().getY() + App.tileHeight * delta / 5);
            heart.getSprite().draw(MainGradle.getInstance().getBatch());
            ((AnimatedSprite) heart.getSprite()).update(delta);
        }
    }

    private boolean isStructureInBond(Structure structure) {
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        for (Tile tile : structure.getTiles()) {
            if (camera.frustum.pointInFrustum(tile.getX() * App.tileWidth, tile.getY() * App.tileHeight, 0))
                return true;
        }
        return false;
    }

    private boolean collision(Player player, FarmBuilding farmBuilding) {
        return player.getTiles().get(0).getX() >= farmBuilding.getTiles().get(0).getX() &&
            player.getTiles().get(0).getX() <= farmBuilding.getTiles().get(0).getX() + farmBuilding.getFarmBuildingType().getWidth() &&
            player.getTiles().get(0).getY() >= farmBuilding.getTiles().get(0).getY() &&
            player.getTiles().get(0).getY() <= farmBuilding.getTiles().get(0).getY() + farmBuilding.getFarmBuildingType().getWidth();
    }

    private boolean isAnimalBuildingCollision(List<Structure> structures, Animal animal, Player player) {
        for (Structure structure : structures) {
            if (structure instanceof FarmBuilding && ((FarmBuilding) structure).getAnimals().contains(animal)) {
                if (collision(player, (FarmBuilding) structure)) return true;
            }
        }
        return false;
    }

    public void drawFlower(Direction direction) {
        flower = new SpriteHolder(new AnimatedSprite(
            new Animation<>(0.1f, new TextureRegion(GameAsset.FLOWER))), new Tuple<>(0.5f,0f));
        flower.getSprite().setScale(0.4f);
        ((AnimatedSprite) flower.getSprite()).setLooping(false);
        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
        flower.getSprite().setPosition(
            (flower.getOffset().getX() + player.getTiles().get(0).getX()) * App.tileWidth,
            (flower.getOffset().getY() + player.getTiles().get(0).getY()) * App.tileHeight);
        flowerDirection = direction;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                flower = null;
            }
        }, 2);
    }

    public void drawHeart() {
        heart = new SpriteHolder(new AnimatedSprite(new Animation<>(0.15f, GameAsset.BEATING_HEARTS[0][4],
            GameAsset.BEATING_HEARTS[0][3], GameAsset.BEATING_HEARTS[0][2], GameAsset.BEATING_HEARTS[0][1],
            GameAsset.BEATING_HEARTS[0][0], GameAsset.BEATING_HEARTS[0][1], GameAsset.BEATING_HEARTS[0][2],
            GameAsset.BEATING_HEARTS[0][3])), new Tuple<>(0f,0f));
        heart.getSprite().setSize(App.tileWidth/2f, App.tileHeight/2f);
        Animation<Float> scale = new Animation<>(0.1f, 0.3f, 0.4f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1f);
        ((AnimatedSprite) heart.getSprite()).setScaleAnimation(scale);
        ((AnimatedSprite) heart.getSprite()).setScaleLooping(false);
        ((AnimatedSprite) heart.getSprite()).setLooping(true);
        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
        heart.getSprite().setPosition(
            (heart.getOffset().getX() + player.getTiles().get(0).getX()) * App.tileWidth,
            (heart.getOffset().getY() + player.getTiles().get(0).getY()) * App.tileHeight);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                heart = null;
            }
        }, 20);
    }
}
