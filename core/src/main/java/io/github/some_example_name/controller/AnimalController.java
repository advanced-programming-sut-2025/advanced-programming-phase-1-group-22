package io.github.some_example_name.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.model.animal.Animal;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.service.GameService;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.GameAsset;
import io.github.some_example_name.view.GameView;

public class AnimalController {
    private final Group menuGroup = new Group();
    private final GameService gameService = new GameService();
    private final WorldController worldController = new WorldController();

    public void update(){
        handleInputs();
    }

    private void handleInputs(){
        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)){
            Vector3 worldCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            MainGradle.getInstance().getCamera().unproject(worldCoords);
            float worldX = worldCoords.x;
            float worldY = worldCoords.y;
            for (Animal animal : player.getAnimals()) {
                if (collision(animal,worldX,worldY)) createAnimalMenu(animal);
            }
        }
    }

    private boolean collision(Animal animal,float worldX,float worldY){
        Sprite sprite = animal.getSprite();
        sprite.setPosition(animal.getTiles().get(0).getX() * App.tileWidth, animal.getTiles().get(0).getY() * App.tileHeight);
        return worldX >= sprite.getX() && worldX <= sprite.getX() + sprite.getWidth() && worldY >= sprite.getY() && worldY <= sprite.getY() + sprite.getHeight();
    }

    private void createAnimalMenu(Animal animal){
        if (!GameView.stage.getActors().contains(menuGroup, true)) {
            GameView.stage.addActor(menuGroup);
        }
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

        Label feed = new Label("isFeedToday: " + animal.getIsFeed(),GameAsset.SKIN);
        TextButton feedButton = new TextButton("feedAnimal",GameAsset.SKIN);
        feedButton.addListener(new ClickListener(){
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

        Label pet = new Label("isPetToday: " + animal.getPet(),GameAsset.SKIN);
        TextButton petButton = new TextButton("petAnimal",GameAsset.SKIN);
        petButton.addListener(new ClickListener(){
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
            produce = new Label("ProduceToday: " + animal.getTodayProduct().getName() + " " + animal.getTodayProduct().getProductQuality(),GameAsset.SKIN);
        }else {
            produce = new Label("ProduceToday: None",GameAsset.SKIN);
        }
        TextButton produceButton = new TextButton("collectProduct",GameAsset.SKIN);
        produceButton.addListener(new ClickListener(){
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

        Label shepherd = new Label("isAnimalOut: " + animal.getIsAnimalStayOutAllNight(),GameAsset.SKIN);
        TextField shepherdPositionX = new TextField("x",GameAsset.SKIN);
        TextField shepherdPositionY = new TextField("y",GameAsset.SKIN);
        TextButton shepherdButton = new TextButton("ShepherdAnimal",GameAsset.SKIN);
        shepherdButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    int positionX = Integer.parseInt(shepherdPositionX.getText());
                    int positionY = Integer.parseInt(shepherdPositionY.getText());
                   worldController.showResponse(gameService.shepherdAnimals(animal,positionX,positionY));
                    menuGroup.clear();
                    createAnimalMenu(animal);
                } catch (Exception ignored) {}
            }
        });
        table.add(shepherd).colspan(2);
        table.row();
        table.add(shepherdPositionX).width(60);
        table.add(shepherdPositionY).width(60);
        table.add(shepherdButton).right();
        table.row();

        Label sellPrice = new Label("sellPrice: " + animal.getSellPrice(),GameAsset.SKIN);
        Label friendShip = new Label("friendShip: " + animal.getRelationShipQuality(),GameAsset.SKIN);
        table.add(sellPrice);
        table.add(friendShip).right();
        table.row();

        TextButton sell = new TextButton("sellAnimal",GameAsset.SKIN);
        sell.addListener(new ClickListener(){
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
