package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.common.utils.GameAsset;
import io.github.some_example_name.common.variables.Session;

public class FirstMenu extends Menu {
    private final TextButton registerMenu;
    private final TextButton loginMenu;
    private final TextButton mainMenu;
    private final TextButton exit;

    public FirstMenu(Skin skin) {
        super(skin);
        this.registerMenu = new TextButton("Register Menu", skin);
        this.loginMenu = new TextButton("Login Menu", skin);
        this.mainMenu = new TextButton("Main Menu", skin);
        this.exit = new TextButton("Exit", skin);
        this.title.setText("Chose Menu");
    }

    @Override
    protected void showStage() {
        this.registerMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setScreen(new RegisterMenu(GameAsset.SKIN_MENU));
            }
        });
        this.table.add(registerMenu).width(400).row();
        this.loginMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setScreen(new LoginMenu(GameAsset.SKIN_MENU));
            }
        });
        this.table.add(loginMenu).width(400).row();
        this.mainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Session.getCurrentUser() != null && Session.isStayedLoggedIn()) {
                    setScreen(new MainMenu(GameAsset.SKIN_MENU));
                } else {
                    alert("You should login first", 5);
                }
            }
        });
        this.table.add(mainMenu).width(400).row();
        this.exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameClient.getInstance().stopListening();
                MainGradle.getInstance().getScreen().dispose();
                Gdx.app.exit();
            }
        });
        this.table.add(exit).width(400).row();
    }
}
