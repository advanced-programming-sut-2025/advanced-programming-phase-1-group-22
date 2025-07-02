package io.github.some_example_name.controller;

import com.badlogic.gdx.Gdx;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.GameAsset;
import io.github.some_example_name.view.GameView;
import lombok.Getter;

@Getter
public class GameViewController {
    private final WorldController worldController = new WorldController();
    private final PlayerController playerController = new PlayerController();
    private final ToolController toolController = new ToolController();
    private final CarryingController carryingController = new CarryingController();
    private final CameraViewController cameraViewController = new CameraViewController();
    private final GameView view;
    private float nightAlpha = 0.0f;

    public GameViewController(GameView view) {
        this.view = view;
    }

    public void update(){
        worldController.update();
        playerController.update();
        toolController.update();
        carryingController.update();
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
        cameraViewController.update();
    }
}
