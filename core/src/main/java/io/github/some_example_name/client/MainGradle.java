package io.github.some_example_name.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.some_example_name.client.view.mainMenu.StartGameMenu;
import io.github.some_example_name.common.utils.GameAsset;
import io.github.some_example_name.client.view.GameView;
import io.github.some_example_name.client.view.mainMenu.FirstMenu;
import lombok.Getter;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
@Getter
public class MainGradle extends Game {
    private SpriteBatch batch;
    @Getter
    private static MainGradle instance;
    private OrthographicCamera camera;
    private Viewport viewport;


    public void centerCameraOnPlayer(float playerX, float playerY) {
        camera.position.set(playerX, playerY, 0);
        camera.update();
    }

    @Override
    public void create() {
        instance = this;
        GameClient.getInstance().connectToServer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new FitViewport(1920, 1080, camera);
        viewport.apply();
        batch = new SpriteBatch();
        initialMenu();
    }

    public void initialMenu() {
        GameView.captureInput = true;
        instance.setScreen(new StartGameMenu(GameAsset.SKIN_MENU, 0));
    }

    @Override
    public void render() {
        camera.update();
       // camera.zoom = 3f;
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
