package io.github.some_example_name.view.mainMenu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.some_example_name.controller.mainMenu.LoginController;
import io.github.some_example_name.variables.Session;
import lombok.Getter;

@Getter
public class Login extends Menu {
    private final LoginController controller = new LoginController(this);
    private final TextField username;
    private final TextField password;
    private final CheckBox stayLoggedIn;
    private final TextButton login;
    private final TextButton back;


    public Login(Skin skin) {
        super(skin);
        this.title.setText("Login Menu");
        this.username = new TextField("", skin);
        this.password = new TextField("", skin);
        this.stayLoggedIn = new CheckBox("Stay logged in", skin);
        this.login = new TextButton("Login", skin);
        this.back = new TextButton("Back", skin);
    }

    @Override
    protected void showStage() {
        this.table.add(new Label("Username: ", skin)).padRight(10);
        this.table.add(username).width(400).row();
        this.table.add(new Label("Password: ", skin)).padRight(10);
        this.table.add(password).width(400).row();
        this.stayLoggedIn.setChecked(Session.isStayedLoggedIn());
        this.stayLoggedIn.addListener(e -> {
            Session.setStayedLoggedIn(stayLoggedIn.isChecked());
            return false;
        });
        this.table.add(new Label("Stay logged in: ", skin)).padRight(10);
        this.table.add(stayLoggedIn).row();
        this.login.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (controller.login()) setScreen(new Main(skin));
            }
        });
        this.table.add(login).width(400).padRight(10);
        this.back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Session.setStayedLoggedIn(false);
                setScreen(new FirstMenu(skin));
            }
        });
        this.table.add(back).width(400).row();
    }
}
