package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.client.controller.mainMenu.MainMenuController;
import io.github.some_example_name.common.variables.Session;

import java.util.Set;

public class MainMenu extends Menu {
    private final MainMenuController controller = new MainMenuController(this);
    private final Label nickname;
    private final TextButton profileMenu;
    private final TextButton lobbyMenu;
    private final TextButton logout;
    private final TextButton back;
    private final TextButton exit;

    public MainMenu(Skin skin) {
        super(skin);
        this.title.setText("Main Menu");
        this.nickname = new Label("nickname: " + Session.getCurrentUser().getNickname(), skin);
        this.profileMenu = new TextButton("Profile Menu", skin);
        this.lobbyMenu = new TextButton("Lobby Menu", skin);
        this.logout = new TextButton("Logout", skin);
        this.back = new TextButton("Back", skin);
        this.exit = new TextButton("Exit", skin);
    }

    @Override
    protected void showStage() {
        table.add(nickname).row();
        this.profileMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setScreen(new ProfileMenu(skin));
            }
        });
        table.add(profileMenu).width(400).row();
        this.lobbyMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setScreen(new LobbyMenu(skin));
            }
        });
        this.table.add(lobbyMenu).width(400).row();
        this.logout.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameClient.getInstance().logout();
                controller.logout();
                setScreen(new FirstMenu(skin));
            }
        });
        table.add(logout).width(400).row();
        this.back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!Session.isStayedLoggedIn()) {
                    GameClient.getInstance().logout();
                    controller.logout();
                }
                setScreen(new FirstMenu(skin));
            }
        });
        table.add(back).width(400).row();
        this.exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameClient.getInstance().stopListening();
                MainGradle.getInstance().getScreen().dispose();
                Gdx.app.exit();
            }
        });
        table.add(exit).width(400).row();
    }
}
