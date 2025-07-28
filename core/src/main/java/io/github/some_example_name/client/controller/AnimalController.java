package io.github.some_example_name.client.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.common.model.Direction;
import io.github.some_example_name.common.model.Farm;
import io.github.some_example_name.common.model.StructureUpdateState;
import io.github.some_example_name.common.model.Tile;
import io.github.some_example_name.common.model.animal.Animal;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.server.service.GameService;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;
import io.github.some_example_name.client.view.GameView;
import io.github.some_example_name.client.view.HeartEffect;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class AnimalController {
    private final Group menuGroup = new Group();
    private final Array<HeartEffect> heartEffects = new Array<>();
    private final GameService gameService = new GameService();
    private final WorldController worldController = WorldController.getInstance();

    public void update() {
        if (GameView.captureInput) {
            handleInputs();
        }
        float delta = Gdx.graphics.getDeltaTime();
        for (int i = heartEffects.size - 1; i >= 0; i--) {
            HeartEffect heartEffect = heartEffects.get(i);
            heartEffect.update(delta);
            if (heartEffect.isFinished()) {
                heartEffects.removeIndex(i);
            }
        }
        for (HeartEffect heartEffect : heartEffects) {
            heartEffect.draw(MainGradle.getInstance().getBatch());
        }
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            farm.applyPendingChanges();
            farm.forEachStructure(structure -> {
                if (structure instanceof Animal animal && isStructureInBond(structure)) {
                    if (animal.getIsAnimalStayOutAllNight()) {
                        handleAnimalMovement(animal);
                        updateAnimalMovement(Gdx.graphics.getDeltaTime(), animal);
                    }
                }
            });
        }
    }

    private void handleInputs() {
        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            Vector3 worldCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            MainGradle.getInstance().getCamera().unproject(worldCoords);
            float worldX = worldCoords.x;
            float worldY = worldCoords.y;
            for (Animal animal : player.getAnimals()) {
                if (collision(animal, worldX, worldY)) createAnimalMenu(animal);
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            Animal animal = getAnimalAroundPlayer(player);
            if (animal != null) {
                Response response = gameService.pet(animal);
                if (response.shouldBeBack()) {
                    heartEffects.add(new HeartEffect(animal.getTiles().get(0).getX() * App.tileWidth,
                        animal.getTiles().get(0).getY() * App.tileHeight));
                }
                worldController.showResponse(response);
            }
        }
    }

    private void handleAnimalMovement(Animal animal) {
        if (animal.getIsAnimalStayOutAllNight()) {
            Random random = new Random();
            if (random.nextInt(1, 1000) == 1) {
                moveAnimal(animal);
            }
        }
    }

    private void moveAnimal(Animal animal) {
        Random random = new Random();
        int totalDistance = 5;
        int dx = random.nextInt(-1, 2);
        int dy = random.nextInt(-1, 2);
        if (dx == 0 && dy == 0) return;
        Queue<Vector2> path = new LinkedList<>();
        int currentX = animal.getTiles().get(0).getX();
        int currentY = animal.getTiles().get(0).getY();
        for (int i = 1; i <= totalDistance; i++) {
            int stepX = currentX + dx * i;
            int stepY = currentY + dy * i;
            Tile tile = getTileByXAndY(stepX, stepY);
            if (tile != null && !tile.getIsFilled() &&
                getPlayerMainFarm(App.getInstance().getCurrentGame().getCurrentPlayer()).getTiles().contains(tile)) {
                path.add(new Vector2(stepX, stepY));
            } else {
                break;
            }
        }
        if (!path.isEmpty()) {
            animal.setMovementPath(path);
        }
    }

    private void updateAnimalMovement(float delta, Animal animal) {
        animal.setTimeSinceLastMove(animal.getTimeSinceLastMove() + delta);

        if (animal.getMovementPath() != null && !animal.getMovementPath().isEmpty()) {
            if (animal.getTimeSinceLastMove() > 0.3f) {
                animal.setTimeSinceLastMove(0);
                Vector2 nextStep = animal.getMovementPath().poll();
                if (nextStep != null){
                    Tile tile = getTileByXAndY((int) nextStep.x,(int) nextStep.y);
                    Tile priviousTile = animal.getTiles().get(0);
                    if (tile!= null){
                        animal.getTiles().clear();
                        animal.getTiles().add(tile);
                        GameClient.getInstance().updateStructureState(animal, StructureUpdateState.UPDATE,true,priviousTile);
                    }
                }
            }
        }
    }

    private Farm getPlayerMainFarm(Player player) {
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            if (!farm.getPlayers().isEmpty() && farm.getPlayers().get(0).equals(player)) {
                return farm;
            }
        }
        return null;
    }

    private Tile getTileByXAndY(int x, int y) {
        for (Tile[] tile : App.getInstance().getCurrentGame().tiles) {
            for (Tile tile1 : tile) {
                if (tile1.getX() == x && tile1.getY() == y) {
                    return tile1;
                }
            }
        }
        return null;
    }

    private Animal getAnimalAroundPlayer(Player player) {
        for (Animal animal : player.getAnimals()) {
            for (Direction value : Direction.values()) {
                if (player.getTiles().get(0).getX() + value.getXTransmit() == animal.getTiles().get(0).getX() &&
                    player.getTiles().get(0).getY() + value.getYTransmit() == animal.getTiles().get(0).getY()) {
                    return animal;
                }
            }
        }
        return null;
    }

    private boolean collision(Animal animal, float worldX, float worldY) {
        Sprite sprite = animal.getSprite();
        sprite.setPosition(animal.getTiles().get(0).getX() * App.tileWidth, animal.getTiles().get(0).getY() * App.tileHeight);
        return worldX >= sprite.getX() && worldX <= sprite.getX() + sprite.getWidth() && worldY >= sprite.getY() && worldY <= sprite.getY() + sprite.getHeight();
    }

    private boolean isStructureInBond(Structure structure) {
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        for (Tile tile : structure.getTiles()) {
            if (camera.frustum.pointInFrustum(tile.getX() * App.tileWidth, tile.getY() * App.tileHeight, 0))
                return true;
        }
        return false;
    }

    private void createAnimalMenu(Animal animal) {
        if (!GameView.stage.getActors().contains(menuGroup, true)) {
            GameView.stage.addActor(menuGroup);
        }
        menuGroup.clear();

        Window window = new Window("", GameAsset.SKIN);
        window.setSize(1200, 700);
        window.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);

        Table table = new Table();
        table.setFillParent(true);
        table.top().pad(20).defaults().pad(10).left();

        Label feed = new Label("isFeedToday: " + animal.getIsFeed(), GameAsset.SKIN);
        TextButton feedButton = new TextButton("feedAnimal", GameAsset.SKIN);
        feedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                worldController.showResponse(gameService.feedHay(animal));
                menuGroup.clear();
                createAnimalMenu(animal);
            }
        });
        table.add(feed);
        table.add(feedButton).right();
        table.row();

        Label pet = new Label("isPetToday: " + animal.getPet(), GameAsset.SKIN);
        TextButton petButton = new TextButton("petAnimal", GameAsset.SKIN);
        petButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                worldController.showResponse(gameService.pet(animal));
                menuGroup.clear();
                createAnimalMenu(animal);
            }
        });
        table.add(pet);
        table.add(petButton).right();
        table.row();

        Label produce;
        if (animal.getTodayProduct() != null) {
            produce = new Label("ProduceToday: " + animal.getTodayProduct().getName() + " " + animal.getTodayProduct().getProductQuality(), GameAsset.SKIN);
        } else {
            produce = new Label("ProduceToday: None", GameAsset.SKIN);
        }
        TextButton produceButton = new TextButton("collectProduct", GameAsset.SKIN);
        produceButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                worldController.showResponse(gameService.collectProduce(animal));
                menuGroup.clear();
                createAnimalMenu(animal);
            }
        });
        table.add(produce);
        table.add(produceButton).right();
        table.row();

        Label shepherd = new Label("isAnimalOut: " + animal.getIsAnimalStayOutAllNight(), GameAsset.SKIN);
        TextField shepherdPositionX = new TextField("x", GameAsset.SKIN);
        TextField shepherdPositionY = new TextField("y", GameAsset.SKIN);
        TextButton shepherdButton = new TextButton("ShepherdAnimal", GameAsset.SKIN);
        shepherdButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    int positionX = Integer.parseInt(shepherdPositionX.getText());
                    int positionY = Integer.parseInt(shepherdPositionY.getText());
                    worldController.showResponse(gameService.shepherdAnimals(animal, positionX, positionY));
                    menuGroup.clear();
                    createAnimalMenu(animal);
                } catch (Exception ignored) {
                }
            }
        });
        table.add(shepherd).colspan(2);
        table.row();
        table.add(shepherdPositionX).width(60);
        table.add(shepherdPositionY).width(60);
        table.add(shepherdButton).right();
        table.row();

        Label sellPrice = new Label("sellPrice: " + animal.getSellPrice(), GameAsset.SKIN);
        Label friendShip = new Label("friendShip: " + animal.getRelationShipQuality(), GameAsset.SKIN);
        table.add(sellPrice);
        table.add(friendShip).right();
        table.row();

        TextButton sell = new TextButton("sellAnimal", GameAsset.SKIN);
        sell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                worldController.showResponse(gameService.sellAnimal(animal));
                menuGroup.clear();
            }
        });
        table.add(sell).colspan(2).center();
        table.row();


        window.add(table).expand().fill();

        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.EXIT_BUTTON)));
        exitButton.setSize(32, 32);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                window.remove();
                exitButton.remove();
            }
        });

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
        menuGroup.addActor(group);
    }
}
