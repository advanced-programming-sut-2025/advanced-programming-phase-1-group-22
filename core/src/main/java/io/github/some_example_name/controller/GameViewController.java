package io.github.some_example_name.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.GameAsset;
import io.github.some_example_name.view.GameView;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GameViewController {
    private final WorldController worldController = new WorldController();
    private final PlayerController playerController = new PlayerController(worldController);
    private final ToolController toolController = new ToolController();
    private final CarryingController carryingController = new CarryingController();
    private final CameraViewController cameraViewController = new CameraViewController();
    private final AnimalController animalController = new AnimalController();
    private final GameView view;
    private Group inventoryBar;
    private Group energyBar;
    private Image energyBackground;
    private Image energyFill;
    private final List<Stack> itemStacks = new ArrayList<>();
    private float nightAlpha = 0.0f;

    public GameViewController(GameView view) {
        initInventoryBar();
        initEnergyBar();
        this.view = view;
    }

    private void initInventoryBar() {
        Table bar = new Table(GameAsset.SKIN);
        bar.setFillParent(true);
        bar.bottom().center().padBottom(20);

        for (int i = 0; i < 12; i++) {
            Image slot = new Image(new Texture(Gdx.files.internal("bar.png")));
            Stack stack = new Stack();
            stack.add(slot);
            itemStacks.add(stack);
            bar.add(stack).size(96, 96);
        }
        inventoryBar = new Group(){
            @Override
            public void act(float delta) {
                OrthographicCamera camera = MainGradle.getInstance().getCamera();
                bar.setPosition(camera.position.x - bar.getWidth() / 2f,camera.position.y - camera.viewportHeight / 2f + 40f);
                super.act(delta);
            }
        };
        inventoryBar.addActor(bar);
        GameView.stage.addActor(inventoryBar);
    }

    private void initEnergyBar(){
        Texture texture = GameAsset.ENERGY_BAR;
        Texture texture1 = GameAsset.ENERGY_BAR_EMPTY;
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        TextureRegion backgroundRegion = new TextureRegion(texture1, 0, 0, 101, 580);
        TextureRegion fillRegion = new TextureRegion(texture, 9, 26, 18, 160);
        energyBackground = new Image(backgroundRegion);
        energyFill = new Image(fillRegion);
        energyFill.setSize(camera.viewportWidth * 0.03f,
            camera.viewportHeight * 0.2f / 160 * 200);
        energyFill.setPosition(9, 10);

        Stack energyStack = new Stack();
        energyStack.setSize(50, 260);
        energyStack.add(energyBackground);
        energyStack.add(energyFill);

        energyBar = new Group() {
            @Override
            public void act(float delta) {
                float screenRight = camera.position.x + camera.viewportWidth / 2f;
                float screenBottom = camera.position.y - camera.viewportHeight / 2f;

                setPosition(camera.position.x + camera.viewportWidth/2f - energyBar.getWidth()*1.1f,
                    camera.position.y - camera.viewportHeight/2f + energyBar.getHeight()*0.3f);
                super.act(delta);
            }
        };

        energyBar.setSize(50, 260);
        energyBar.addActor(energyStack);
        GameView.stage.addActor(energyBar);
    }

    private void updateEnergyBar(){
        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
        float energyPercent = (float) player.getEnergy() / player.getMaxEnergy();

        float fullHeight = 160f;
        float currentHeight = fullHeight * energyPercent;

        energyFill.setSize(30, currentHeight);
        energyBackground.setScale(1, player.getMaxEnergy()/200f);

        energyFill.setPosition(9,10);
    }

    private void updateInventoryBar(){
        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
        List<Salable> items = new ArrayList<>(player.getInventory().getProducts().keySet());

        for (int i = 0; i < itemStacks.size(); i++) {
            Stack stack = itemStacks.get(i);
            while (stack.getChildren().size > 1) {
                stack.removeActorAt(1, true);
            }
            if (i < items.size()) {
                Salable item = items.get(i);
                Image itemImage = new Image(item.getTexture());
                itemImage.setSize(90, 90);
                int count = player.getInventory().getProducts().get(item);
                Label countLabel = new Label(String.valueOf(count), GameAsset.SKIN);
                countLabel.setFontScale(0.7f);
                countLabel.setAlignment(Align.right);
                countLabel.setColor(Color.GREEN);
                Container<Label> labelContainer = new Container<>(countLabel);
                labelContainer.setFillParent(false);
                labelContainer.setSize(30, 20);
                labelContainer.setPosition(66, 5);
                if (item.equals(player.getCurrentCarrying())) itemImage.setColor(1f,1f, 1f,1f);
                else itemImage.setColor(1f,1f,1f,0.5f);
                stack.add(itemImage);
                stack.add(labelContainer);
            }
        }
    }

    public void update(){
        if (GameView.screenshotting) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                GameView.screenshotting = false;
            }
        }
        worldController.update();
        playerController.update();
        toolController.update();
        carryingController.update();
        animalController.update();
        updateInventoryBar();
        updateEnergyBar();
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        switch (App.getInstance().getCurrentGame().getFadingInTheNight()) {
            case 1: {
                nightAlpha += Gdx.graphics.getDeltaTime()/2.5f;
                if (nightAlpha >= 1f) {
                    nightAlpha = 1f;
                }
                GameAsset.NIGHT_SPRITE.setAlpha(nightAlpha);
                GameAsset.NIGHT_SPRITE.setPosition(
                    camera.position.x - camera.viewportWidth/2f,
                    camera.position.y - camera.viewportHeight/2f);
                GameAsset.NIGHT_SPRITE.draw(MainGradle.getInstance().getBatch());
            } break;
            case 2: {
                nightAlpha -= Gdx.graphics.getDeltaTime()/2.5f;
                if (nightAlpha <= 0) {
                    nightAlpha = 0;
                }
                GameAsset.NIGHT_SPRITE.setPosition(
                    camera.position.x - camera.viewportWidth/2f,
                    camera.position.y - camera.viewportHeight/2f);
                GameAsset.NIGHT_SPRITE.setAlpha(nightAlpha);
                GameAsset.NIGHT_SPRITE.draw(MainGradle.getInstance().getBatch());
            }
        }
        if (!GameView.screenshotting) {
            cameraViewController.update();
        }
    }
}
