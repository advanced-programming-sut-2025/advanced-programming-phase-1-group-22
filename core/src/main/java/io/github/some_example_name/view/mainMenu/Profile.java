package io.github.some_example_name.view.mainMenu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.some_example_name.controller.mainMenu.ProfileController;
import io.github.some_example_name.model.User;
import io.github.some_example_name.variables.Session;

public class Profile extends Menu {
    private final ProfileController controller = new ProfileController(this);
    private final TextButton changeUsername;
    private final TextButton changePassword;
    private final TextButton changeEmail;
    private final TextButton changeNickname;
    private final TextButton back;
    private final User user = Session.getCurrentUser();
    private final Label username;
    private final Label nickname;
    private final Label highestMoneyEarned;
    private final Label numberOfPlayedGames;
    private Runnable updateLabels;
    private Dialog changeUsernameDialog;
    private Dialog changePasswordDialog;
    private Dialog changeEmailDialog;
    private Dialog changeNicknameDialog;

    public Profile(Skin skin) {
        super(skin);
        this.title.setText("Profile Menu");
        this.changeUsername = new TextButton("Change Username", skin);
        this.changePassword = new TextButton("Change Password", skin);
        this.changeEmail = new TextButton("Change Email", skin);
        this.changeNickname = new TextButton("Change Nickname", skin);
        this.back = new TextButton("Back", skin);
        this.username = new Label("Username: " + user.getUsername(), skin);
        this.nickname = new Label("Nickname: " + user.getNickname(), skin);
        this.highestMoneyEarned = new Label("Highest Money Earn: " + user.getHighestMoneyEarned(), skin);
        this.numberOfPlayedGames = new Label("Number Of Played Games: " + user.getNumberOfPlayedGames(), skin);
    }

    @Override
    protected void showStage() {
        table.add(username).row();
        table.add(nickname).row();
        table.add(highestMoneyEarned).row();
        table.add(numberOfPlayedGames).row();
        this.changeUsername.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeUsernameDialog.show(stage);
            }
        });
        table.add(changeUsername).width(450).padRight(10);
        this.changePassword.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changePasswordDialog.show(stage);
            }
        });
        table.add(changePassword).width(450).padRight(10);
        this.changeEmail.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeEmailDialog.show(stage);
            }
        });
        table.add(changeEmail).width(450).padRight(10);
        this.changeNickname.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeNicknameDialog.show(stage);
            }
        });
        table.add(changeNickname).width(450).row();
        this.back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setScreen(new Main(skin));
            }
        });
        table.add(back).width(450).center().row();
        updateLabels = () -> {
            this.username.setText("Username: " + user.getUsername());
            this.nickname.setText("Nickname: " + user.getNickname());
        };
        createChangeUsernameWindow();
        createChangeNicknameWindow();
        createChangePasswordWindow();
        createChangeEmailWindow();
    }

    private void createChangeUsernameWindow() {
        changeUsernameDialog = new Dialog("Change username", skin);
        Table changeUsernameTable = new Table();
        changeUsernameTable.defaults().pad(10);

        changeUsernameTable.add(new Label("New Username: ", skin)).padRight(10);
        TextField changeUsernameField = new TextField("", skin);
        changeUsernameTable.add(changeUsernameField).width(400).row();

        TextButton confirm = new TextButton("Change Username", skin);

        changeUsernameDialog.getContentTable().add(changeUsernameTable).pad(20);
        changeUsernameDialog.getButtonTable().defaults().pad(10);
        confirm.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (controller.changeUsername(changeUsernameField.getText())) {
                    updateLabels.run();
                    changeUsernameDialog.hide();
                }
            }
        });
        changeUsernameDialog.button(confirm);
        TextButton back = new TextButton("Back", skin);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeUsernameDialog.hide();
            }
        });
        changeUsernameDialog.button(back);
    }

    private void createChangeNicknameWindow() {
        changeNicknameDialog = new Dialog("Change nickname", skin);
        Table changeNicknameTable = new Table();
        changeNicknameTable.defaults().pad(10);

        changeNicknameTable.add(new Label("New Nickname: ", skin)).padRight(10);
        TextField changeNicknameField = new TextField("", skin);
        changeNicknameTable.add(changeNicknameField).width(400).row();

        TextButton confirm = new TextButton("Change Nickname", skin);

        changeNicknameDialog.getContentTable().add(changeNicknameTable).pad(20);
        changeNicknameDialog.getButtonTable().defaults().pad(10);
        confirm.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (controller.changeNickname(changeNicknameField.getText())) {
                    updateLabels.run();
                    changeNicknameDialog.hide();
                }
            }
        });
        changeNicknameDialog.button(confirm);
        TextButton back = new TextButton("Back", skin);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeNicknameDialog.hide();
            }
        });
        changeNicknameDialog.button(back);
    }

    private void createChangeEmailWindow() {
        changeEmailDialog = new Dialog("Change Email", skin);
        Table changeEmailTable = new Table();
        changeEmailTable.defaults().pad(10);

        changeEmailTable.add(new Label("New Email: ", skin)).padRight(10);
        TextField changeEmailField = new TextField("", skin);
        changeEmailTable.add(changeEmailField).width(400).row();

        TextButton confirm = new TextButton("Change Email", skin);

        changeEmailDialog.getContentTable().add(changeEmailTable).pad(20);
        changeEmailDialog.getButtonTable().defaults().pad(10);
        confirm.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (controller.changeEmail(changeEmailField.getText())) {
                    changeEmailDialog.hide();
                }
            }
        });
        changeEmailDialog.button(confirm);
        TextButton back = new TextButton("Back", skin);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeEmailDialog.hide();
            }
        });
        changeEmailDialog.button(back);
    }

    private void createChangePasswordWindow() {
        changePasswordDialog = new Dialog("Change password", skin);
        Table changePasswordTable = new Table();
        changePasswordTable.defaults().pad(10);

        changePasswordTable.add(new Label("New Password: ", skin)).padRight(10);
        TextField newPasswordField = new TextField("", skin);
        changePasswordTable.add(newPasswordField).width(400).row();
        changePasswordTable.add(new Label("Old Password: ", skin)).padRight(10);
        TextField oldPasswordField = new TextField("", skin);
        changePasswordTable.add(oldPasswordField).width(400).row();


        TextButton confirm = new TextButton("Change Password", skin);

        changePasswordDialog.getContentTable().add(changePasswordTable).pad(20);
        changePasswordDialog.getButtonTable().defaults().pad(10);
        confirm.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (controller.changePassword(newPasswordField.getText(), oldPasswordField.getText())) {
                    changePasswordDialog.hide();
                }
            }
        });
        changePasswordDialog.button(confirm);
        TextButton back = new TextButton("Back", skin);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changePasswordDialog.hide();
            }
        });
        changePasswordDialog.button(back);
    }
}
