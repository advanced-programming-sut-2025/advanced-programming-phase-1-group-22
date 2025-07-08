package io.github.some_example_name.view.mainMenu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.some_example_name.controller.mainMenu.MainController;
import io.github.some_example_name.variables.Session;

public class Main extends Menu {
    private final MainController controller = new MainController(this);
    private final TextButton profileMenu;
    private final TextButton preGameMenu;
    private final TextButton logout;
    private final TextButton back;

    public Main(Skin skin) {
        super(skin);
        this.title.setText("Main Menu");
        this.profileMenu = new TextButton("Profile Menu", skin);
        this.preGameMenu = new TextButton("PreGame Menu", skin);
        this.logout = new TextButton("Logout", skin);
        this.back = new TextButton("Back", skin);
    }

    @Override
    protected void showStage() {
        this.profileMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setScreen(new Profile(skin));
            }
        });
        table.add(profileMenu).width(400).row();
        this.preGameMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setScreen(new StartGameMenu(skin, 0));
            }
        });
        table.add(preGameMenu).width(400).row();
        this.logout.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.logout();
                setScreen(new FirstMenu(skin));
            }
        });
        table.add(logout).width(400).row();
        this.back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!Session.isStayedLoggedIn()) {
                    controller.logout();
                }
                setScreen(new FirstMenu(skin));
            }
        });
        table.add(back).width(400).row();
    }
}
