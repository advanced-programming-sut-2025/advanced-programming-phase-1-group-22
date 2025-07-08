package io.github.some_example_name.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.utils.GameAsset;
import io.github.some_example_name.view.gameMenu.CheatCodeMenu;
import io.github.some_example_name.view.mainMenu.PopUp;
import lombok.Getter;

@Getter
public class Console extends PopUp {
    private final Group root;
    private final Table logTable;
    private final ScrollPane logPane;
    private final TextField input;
    private final TextButton sendBtn;
    private final OrthographicCamera camera = MainGradle.getInstance().getCamera();
    private final Skin skin = GameAsset.SKIN;
    private final CommandProcessor commandProcessor = CheatCodeMenu.getInstance();

    private boolean visible = false;

    public Console(Stage stage) {
        Table box = new Table(skin);
        box.setBackground("window");
        box.defaults().pad(4);

        logTable = new Table(skin);
        logTable.top().left();
        logPane = new ScrollPane(logTable, skin);
        logPane.setFadeScrollBars(false);
        logPane.setScrollingDisabled(true, false);
        logPane.setScrollbarsOnTop(true);

        input = new TextField(">>", skin);

        sendBtn = new TextButton("Send", skin);
        sendBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                dispatch();
            }
        });

        input.setTextFieldListener((f, c) -> {
        });

        Table bottom = new Table();
        bottom.add(input).growX().padRight(4);
        bottom.add(sendBtn).width(70);

        box.add(logPane).width(420).height(160).row();
        box.add(bottom).growX();

        box.pack();
        root = new Group() {
            @Override
            public void act(float d) {
                box.setPosition(camera.position.x - camera.viewportWidth / 2f + 12,
                    camera.position.y - camera.viewportHeight / 2f + 100);
                if (visible && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                    handleGlobalKey(stage);
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                    dispatch();
                }
                super.act(d);
            }
        };
        root.addActor(box);
        root.setVisible(false);
        stage.addActor(root);
    }

    public void handleGlobalKey(Stage stage) {
        visible = !visible;
        GameView.captureInput = !GameView.captureInput;
        root.setVisible(visible);
        if (visible) {
            stage.setKeyboardFocus(input);
        } else {
            stage.unfocus(input);
        }
    }

    private void dispatch() {
        String txt = input.getText().trim();
        if (txt.isEmpty()) return;
        addLine(new Response(txt,true));
        input.setText("");
        handleCommand(txt);
    }

    public void addLine(Response response) {
        Label l = new Label(response.message(), skin);
        if (response.shouldBeBack()) l.setColor(Color.GREEN);
        else l.setColor(Color.RED);
        logTable.add(l).left().row();
        logPane.layout();
        logPane.setScrollPercentY(100);
    }

    private void handleCommand(String cmd) {
        commandProcessor.processCommand(cmd,response -> addLine(response));
    }

    @Override
    protected void handleDragRelease(InputEvent event, float x, float y, int pointer, Image itemImage, Salable item, Image dragImage, Boolean flag) {}
}
