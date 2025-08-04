package io.github.some_example_name.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.tools.javac.Main;
import io.github.some_example_name.client.view.mainMenu.FirstMenu;
import io.github.some_example_name.client.view.mainMenu.LobbyMenu;
import io.github.some_example_name.client.view.mainMenu.LoginMenu;
import io.github.some_example_name.client.view.mainMenu.MainMenu;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;
import io.github.some_example_name.client.view.GameView;
import io.github.some_example_name.common.variables.Session;
import lombok.Getter;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
@Getter
public class MainGradle extends Game {
    private SpriteBatch batch;
    private BitmapFont font;
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
        font = new BitmapFont();
        initialMenu();
        {//todo delete hardcode
            if (App.PORT == 5001) {
                if (screen != null) screen.dispose();
                LoginMenu loginMenu = new LoginMenu(GameAsset.SKIN_MENU);
                MainGradle.getInstance().setScreen(loginMenu);
                loginMenu.getUsername().setText("Roham1234");
                loginMenu.getPassword().setText("pass");
                if (loginMenu.getController().login()) {
                    GameClient.getInstance().loggedIn(false, "");
                    screen.dispose();
                    LobbyMenu lobbyMenu = new LobbyMenu(GameAsset.SKIN_MENU);
                    MainGradle.getInstance().setScreen(lobbyMenu);

                    String lobbyName = "1";
                    boolean isVisible = true;
                    boolean isPrivate = false;
                    String password = "";
                    lobbyMenu.getController().createLobby(Session.getCurrentUser(), lobbyName, isPrivate, password, isVisible);
                }
            } else if (App.PORT == 5002) {
                if (screen != null) screen.dispose();
                LoginMenu loginMenu = new LoginMenu(GameAsset.SKIN_MENU);
                MainGradle.getInstance().setScreen(loginMenu);
                loginMenu.getUsername().setText("Clara1234");
                loginMenu.getPassword().setText("noPass");
                if (loginMenu.getController().login()) {
                    GameClient.getInstance().loggedIn(false, "");
                    screen.dispose();
                    LobbyMenu lobbyMenu = new LobbyMenu(GameAsset.SKIN_MENU);
                    MainGradle.getInstance().setScreen(lobbyMenu);
                }
            }
        }
    }

    public void initialMenu() {
        GameView.captureInput = true;
        instance.setScreen(new FirstMenu(GameAsset.SKIN_MENU));
    }

    @Override
    public void render() {
        camera.update();
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
