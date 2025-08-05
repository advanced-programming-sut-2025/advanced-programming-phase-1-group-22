package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.client.controller.mainMenu.RegisterMenuController;
import io.github.some_example_name.common.model.enums.SecurityQuestion;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;

@Getter
public class RegisterMenu extends Menu {
    private final RegisterMenuController controller = new RegisterMenuController(this);
    private final TextField username;
    private final TextField password;
    private final TextField confirmPassword;
    private final TextField nickName;
    private final TextField email;
    private String gender;
    private final SelectBox<String> genderBox;
    private final TextButton randomPassword;
    private final TextButton securityQuestion;
    private final TextButton back;
    private final TextButton login;
    private SecurityQuestion securityQuestionValue;
    private final SelectBox<String> securityQuestions;
    private final TextField answer;
    private final TextField confirmAnswer;
    private final TextButton register;
    private final TextButton backFromSecurityWindow;
    private Dialog securityDialog;

    public RegisterMenu(Skin skin) {
        super(skin);
        this.title.setText("Register Menu");
        this.username = new TextField("", skin);
        this.password = new TextField("", skin);
        this.confirmPassword = new TextField("", skin);
        this.nickName = new TextField("", skin);
        this.email = new TextField("", skin);
        this.answer = new TextField("", skin);
        this.confirmAnswer = new TextField("", skin);
        this.randomPassword = new TextButton("Random Password", skin);
        this.securityQuestion = new TextButton("Security Question", skin);
        this.back = new TextButton("Back", skin);
        this.login = new TextButton("Login", skin);
        this.register = new TextButton("Register", skin);
        this.backFromSecurityWindow = new TextButton("Back", skin);
        this.genderBox = new SelectBox<>(skin);
        this.securityQuestions = new SelectBox<>(skin);
    }

    @Override
    protected void showStage() {
        table.add(new Label("Username: ", skin)).padRight(10);
        table.add(username).width(400).row();
        table.add(new Label("Password: ", skin)).padRight(10);
        table.add(password).width(400).padRight(10);
        this.randomPassword.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String randomPassword = controller.getRandomPassword();
                password.setText(randomPassword);
                confirmPassword.setText(randomPassword);
            }
        });
        table.add(randomPassword).width(500).row();
        table.add(new Label("Confirm Password: ", skin)).padRight(10);
        table.add(confirmPassword).width(400).row();
        table.add(new Label("Nickname: ", skin)).padRight(10);
        table.add(nickName).width(400).row();
        table.add(new Label("Email: ", skin)).padRight(10);
        table.add(email).width(400).row();
        Array<String> genders = new Array<>();
        genders.add("gender");
        genders.add("MALE");
        genders.add("FEMALE");
        this.genderBox.setItems(genders);
        this.genderBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!genderBox.getSelected().equals("gender")) gender = genderBox.getSelected();
            }
        });
        table.add(new Label("Gender: ", skin)).padRight(10);
        table.add(genderBox).width(400).height(100).row();
        this.securityQuestion.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (controller.handleRegisterBeforeSecurityQuestion()) {
                    securityDialog.show(stage);
                }
            }
        });
        table.add(securityQuestion).width(400).padRight(10);
        this.back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setScreen(new FirstMenu(GameAsset.SKIN_MENU));
            }
        });
        table.add(back).width(400).padRight(10);
        this.login.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setScreen(new LoginMenu(GameAsset.SKIN_MENU));
            }
        });
        table.add(login).width(400).row();
        createSecurityQuestionWindow();
    }

    private void createSecurityQuestionWindow() {
        securityDialog = new Dialog("Security Question", skin);
        Table securityTable = new Table();
        securityTable.defaults().pad(10);

        securityTable.add(new Label("Question: ", skin)).padRight(10);
        Array<String> questions = new Array<>();
        questions.add("Question");
        questions.add(SecurityQuestion.QUESTION1.getQuestion());
        questions.add(SecurityQuestion.QUESTION2.getQuestion());
        questions.add(SecurityQuestion.QUESTION3.getQuestion());
        questions.add(SecurityQuestion.QUESTION4.getQuestion());
        questions.add(SecurityQuestion.QUESTION5.getQuestion());
        this.securityQuestions.setItems(questions);
        this.securityQuestions.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!securityQuestions.getSelected().equals("Question"))
                    securityQuestionValue = SecurityQuestion.getFromQuestion(securityQuestions.getSelected());
            }
        });
        securityTable.add(securityQuestions).width(400).height(100).row();
        securityTable.add(new Label("Answer: ", skin)).padRight(10);
        securityTable.add(answer).width(400).row();
        securityTable.add(new Label("Confirm Answer: ", skin)).padRight(10);
        securityTable.add(confirmAnswer).width(400).row();

        securityDialog.getContentTable().add(securityTable).pad(20);
        securityDialog.getButtonTable().defaults().pad(10);
        this.register.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (controller.register()) {
                    securityDialog.hide();
                    setScreen(new MainMenu(GameAsset.SKIN_MENU));
                }
            }
        });
        securityDialog.button(register);
        this.backFromSecurityWindow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                securityDialog.hide();
            }
        });
        securityDialog.button(backFromSecurityWindow);
    }
}
