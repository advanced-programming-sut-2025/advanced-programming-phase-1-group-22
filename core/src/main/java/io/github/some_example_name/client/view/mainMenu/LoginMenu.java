package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.client.controller.mainMenu.LoginMenuController;
import io.github.some_example_name.common.utils.GameAsset;
import io.github.some_example_name.common.variables.Session;
import lombok.Getter;

@Getter
public class LoginMenu extends Menu {
    private final LoginMenuController controller = new LoginMenuController(this);
    private final TextField username;
    private final TextField password;
    private final CheckBox stayLoggedIn;
    private final TextButton login;
    private final TextButton back;
    private final TextButton register;
    private final TextButton forgetPassword;
    private Dialog forgetPasswordDialog;
    private final TextField answer;
    private final TextField usernameForDialog;
    private final TextField newPassword;
    private final TextButton randomPassword;
    private final TextButton backFromForgetPasswordWindow;
    private final TextButton confirm;


    public LoginMenu(Skin skin) {
        super(skin);
        this.title.setText("Login Menu");
        this.username = new TextField("", skin);
        this.password = new TextField("", skin);
        this.stayLoggedIn = new CheckBox("Stay logged in", skin);
        this.login = new TextButton("Login", skin);
        this.back = new TextButton("Back", skin);
        this.register = new TextButton("Register", skin);
        this.forgetPassword = new TextButton("Forget Password", skin);
        this.usernameForDialog = new TextField("", skin);
        this.answer = new TextField("", skin);
        this.newPassword = new TextField("", skin);
        this.backFromForgetPasswordWindow = new TextButton("Back", skin);
        this.confirm = new TextButton("Confirm", skin);
        this.randomPassword = new TextButton("Random Password", skin);
    }

    @Override
    protected void showStage() {
        this.table.add(new Label("Username: ", skin)).padRight(10);
        this.table.add(username).width(400).row();
        this.table.add(new Label("Password: ", skin)).padRight(10);
        this.table.add(password).width(400).padRight(10);
        this.forgetPassword.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                forgetPasswordDialog.show(stage);
            }
        });
        this.table.add(forgetPassword).width(400).row();
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
                if (controller.login()) {
                    GameClient.getInstance().loggedIn(stayLoggedIn.isChecked(), password.getText());
                    setScreen(new MainMenu(skin));
                }
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
        this.table.add(back).width(400).padRight(10);
        this.register.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setScreen(new RegisterMenu(GameAsset.SKIN_MENU));
            }
        });
        this.table.add(register).width(400).row();
        createForgetPasswordWindow();
    }

    private void createForgetPasswordWindow() {
        forgetPasswordDialog = new Dialog("Security Question", skin);
        Table forgetPasswordTable = new Table();
        forgetPasswordTable.defaults().pad(10);

        forgetPasswordTable.add(new Label("Username: ", skin)).padRight(10);
        forgetPasswordTable.add(usernameForDialog).width(400).row();
        forgetPasswordTable.add(new Label("Answer: ", skin)).padRight(10);
        forgetPasswordTable.add(answer).width(400).row();
        forgetPasswordTable.add(new Label("New Password: ", skin)).padRight(10);
        forgetPasswordTable.add(newPassword).width(400).padRight(10);
        this.randomPassword.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                newPassword.setText(controller.getRandomPassword());
            }
        });
        forgetPasswordTable.add(randomPassword).width(400).row();

        forgetPasswordDialog.getContentTable().add(forgetPasswordTable).pad(20);
        forgetPasswordDialog.getButtonTable().defaults().pad(10);
        this.confirm.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (controller.forgetPassword()) {
                    forgetPasswordDialog.hide();
                }
            }
        });
        forgetPasswordDialog.button(confirm);
        this.backFromForgetPasswordWindow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                forgetPasswordDialog.hide();
            }
        });
        forgetPasswordDialog.button(backFromForgetPasswordWindow);
    }
}
