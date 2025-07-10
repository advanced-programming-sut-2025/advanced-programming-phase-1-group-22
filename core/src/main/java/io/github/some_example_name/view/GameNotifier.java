package io.github.some_example_name.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import io.github.some_example_name.MainGradle;

public class GameNotifier {
    private final Table container;
    private final Skin skin;
    private final OrthographicCamera camera;

    public GameNotifier(Stage stage, Skin skin) {
        this.skin = skin;
        container = new Table();
        this.camera = MainGradle.getInstance().getCamera();
        Group group = new Group() {
            @Override
            public void act(float delta) {
                super.act(delta);
                float x = camera.position.x - camera.viewportWidth / 2f + 10;
                float y = camera.position.y + camera.viewportHeight / 2f - 10;
                container.setPosition(x, y, Align.topLeft);
            }
        };
        container.top().left();
        container.setFillParent(true);
        group.addActor(container);
        stage.addActor(group);
    }

    public void showMessage(String message, MessageType type) {
        Label.LabelStyle style = new Label.LabelStyle(skin.get(Label.LabelStyle.class));
        if (type == MessageType.SUCCESS)
            style.fontColor = Color.GREEN;
        else if (type == MessageType.ERROR)
            style.fontColor = Color.RED;
        else
            style.fontColor = Color.WHITE;

        Label label = new Label(message, style);
        label.setZIndex(100);
        label.setAlignment(Align.left);
        label.getColor().a = 0f;

        label.addAction(Actions.sequence(
            Actions.fadeIn(0.3f),
            Actions.delay(3f),
            Actions.fadeOut(1f),
            Actions.run(() -> container.removeActor(label))
        ));

        container.row();
        container.add(label).left().pad(4);
    }

    public enum MessageType {
        SUCCESS,
        ERROR,
        INFO
    }
}
