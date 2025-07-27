package io.github.some_example_name.client.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.common.model.TimeAndDate;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;
import io.github.some_example_name.client.view.GameView;
import lombok.Getter;

@Getter
public class GameViewController {
    private final WorldController worldController = WorldController.getInstance();
    private final PlayerController playerController = new PlayerController(worldController);
    private final ToolController toolController = new ToolController();
    private final CarryingController carryingController = new CarryingController();
    private final CameraViewController cameraViewController = new CameraViewController(worldController);
    private final AnimalController animalController = new AnimalController();
    private final StoreController storeController = new StoreController();
    private final ShippingBinController shippingBinController = new ShippingBinController();
    private final NPCController npcController = new NPCController();
    private final FishingController fishingController = new FishingController();
    private final CrowAttackController crowAttackController = new CrowAttackController();
    private final GameView view;
    private float nightAlpha = 0.0f;

    public GameViewController(GameView view) {
        this.view = view;
    }

    public void update(float delta) {
        if (GameView.screenshotting) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                GameView.screenshotting = false;
                GameView.captureInput = true;
            }
        }
        boolean isNight = App.getInstance().getCurrentGame().getTimeAndDate().compareDailyTime(new TimeAndDate(0, 18)) <= 0;
        if (isNight) MainGradle.getInstance().getBatch().setShader(GameAsset.SHADER);
        App.getInstance().getCurrentGame().getTimeAndDate().updateTime(delta);
        worldController.update(delta);
        playerController.update(delta);
        toolController.update();
        carryingController.update();
        animalController.update();
        storeController.update();
        shippingBinController.update();
        npcController.update();
        fishingController.update();
        crowAttackController.update();
        if (GameView.positionChoosing) {
            float cameraSpeed = 300 * Gdx.graphics.getDeltaTime();

            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                MainGradle.getInstance().getCamera().translate(0, cameraSpeed);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                MainGradle.getInstance().getCamera().translate(0, -cameraSpeed);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                MainGradle.getInstance().getCamera().translate(-cameraSpeed, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                MainGradle.getInstance().getCamera().translate(cameraSpeed, 0);
            }
            MainGradle.getInstance().getCamera().update();
        }
        if (isNight) MainGradle.getInstance().getBatch().setShader(null);
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        switch (App.getInstance().getCurrentGame().getFadingInTheNight()) {
            case 1: {
                nightAlpha += Gdx.graphics.getDeltaTime() / 2.5f;
                if (nightAlpha >= 1f) {
                    nightAlpha = 1f;
                }
                GameAsset.NIGHT_SPRITE.setAlpha(nightAlpha);
                GameAsset.NIGHT_SPRITE.setPosition(
                    camera.position.x - camera.viewportWidth / 2f,
                    camera.position.y - camera.viewportHeight / 2f);
                GameAsset.NIGHT_SPRITE.draw(MainGradle.getInstance().getBatch());
            }
            break;
            case 2: {
                nightAlpha -= Gdx.graphics.getDeltaTime() / 2.5f;
                if (nightAlpha <= 0) {
                    nightAlpha = 0;
                }
                GameAsset.NIGHT_SPRITE.setPosition(
                    camera.position.x - camera.viewportWidth / 2f,
                    camera.position.y - camera.viewportHeight / 2f);
                GameAsset.NIGHT_SPRITE.setAlpha(nightAlpha);
                GameAsset.NIGHT_SPRITE.draw(MainGradle.getInstance().getBatch());
            }
        }
        if (!GameView.screenshotting) {
            cameraViewController.update();
        }
    }
}
