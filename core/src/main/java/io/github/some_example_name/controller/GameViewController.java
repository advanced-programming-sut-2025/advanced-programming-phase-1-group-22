package io.github.some_example_name.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
    private final PlayerController playerController = new PlayerController();
    private final ToolController toolController = new ToolController();
    private final CarryingController carryingController = new CarryingController();
    private final CameraViewController cameraViewController = new CameraViewController();
    private final GameView view;
    private Group inventoryBar;
    private final List<Stack> itemStacks = new ArrayList<>();
    private float nightAlpha = 0.0f;

    public GameViewController(GameView view) {
        initInventoryBar();
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

    private void updateInventoryBar(){
        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
        List<Salable> items = new ArrayList<>(player.getInventory().getProducts().keySet());

        for (int i = 0; i < itemStacks.size(); i++) {
            Stack stack = itemStacks.get(i);
            if (stack.getChildren().size > 1) {
                stack.removeActorAt(1, true);
            }

            if (i < items.size()) {
                Salable item = items.get(i);
                Image itemImage = new Image(item.getTexture());
                itemImage.setSize(90, 90);
                if (item.equals(player.getCurrentCarrying())) itemImage.setColor(1f,1f, 1f,1f);
                else itemImage.setColor(1f,1f,1f,0.5f);
                stack.add(itemImage);
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
        updateInventoryBar();
        switch (App.getInstance().getCurrentGame().getFadingInTheNight()) {
            case 1: {
                nightAlpha += Gdx.graphics.getDeltaTime()/2.5f;
                if (nightAlpha >= 1f) {
                    nightAlpha = 1f;
                }
                GameAsset.NIGHT_SPRITE.setAlpha(nightAlpha);
                GameAsset.NIGHT_SPRITE.setPosition(
                    MainGradle.getInstance().getCamera().position.x - 3*Gdx.graphics.getWidth()/2f,
                    MainGradle.getInstance().getCamera().position.y - 3*Gdx.graphics.getHeight()/2f);
                GameAsset.NIGHT_SPRITE.draw(MainGradle.getInstance().getBatch());
            } break;
            case 2: {
                nightAlpha -= Gdx.graphics.getDeltaTime()/2.5f;
                if (nightAlpha <= 0) {
                    nightAlpha = 0;
                }
                GameAsset.NIGHT_SPRITE.setPosition(
                    MainGradle.getInstance().getCamera().position.x - 3*Gdx.graphics.getWidth()/2f,
                    MainGradle.getInstance().getCamera().position.y - 3*Gdx.graphics.getHeight()/2f);
                GameAsset.NIGHT_SPRITE.setAlpha(nightAlpha);
                GameAsset.NIGHT_SPRITE.draw(MainGradle.getInstance().getBatch());
            }
        }
        if (!GameView.screenshotting) {
            cameraViewController.update();
        }
    }
}
