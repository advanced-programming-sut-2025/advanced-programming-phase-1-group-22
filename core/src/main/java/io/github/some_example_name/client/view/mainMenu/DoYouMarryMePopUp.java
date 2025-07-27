package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Timer;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.client.controller.WorldController;
import io.github.some_example_name.common.model.Direction;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.Tile;
import io.github.some_example_name.common.model.Tuple;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.server.service.RelationService;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.client.view.GameView;
import lombok.Setter;

@Setter
public class DoYouMarryMePopUp extends PopUp {
    private Player player;
    private Window window;

    public void createMenu(Stage stage, Skin skin, WorldController worldController) {
        super.createMenu(stage, skin, worldController);
        createInventory(skin, getMenuGroup(), stage);
    }

    @Override
    protected void handleDragRelease(InputEvent event, float x, float y, int pointer, Image itemImage, Salable item, Image dragImage, Boolean flag) {}

    private void close(Window window) {
        GameView.captureInput = true;
        window.remove();
    }

    private void createInventory(Skin skin, Group menuGroup, Stage stage) {
        OrthographicCamera camera = MainGradle.getInstance().getCamera();

        window = new Window("", skin);
        window.setSize(camera.viewportWidth * 0.3f, camera.viewportHeight * 0.35f);
        window.setMovable(false);

        Table info = new Table();

        info.pack();
        info.add(new Image(player.getAvatar())).size(70).padRight(10);
        info.add(new Label(player.getUser().getNickname(), skin)).width(100).row();
        info.add(new Label("Do you marry me?", skin)).colspan(2).row();
        TextButton yes = new TextButton("OH MY GOD! YES YES YES!", skin);
        TextButton no = new TextButton("NOOOOOOO!", skin);
        info.add(yes).colspan(2).expandX().row();
        info.add(no).colspan(2).expandX().row();
        yes.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                window.remove();
                GameClient.getInstance().respondMarriage(true, player.getUser().getUsername());
                return true;
            }
        });

        no.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                window.remove();
                GameClient.getInstance().respondMarriage(false, player.getUser().getUsername());
                return true;
            }
        });

        Table content = new Table();
        content.setFillParent(true);
        content.add(info).width(window.getWidth()).height(window.getHeight() - 100).padBottom(20).padTop(50).row();


        window.add(content).fill().pad(10);
        Group group = new Group() {
            @Override
            public void act(float delta) {
                window.setPosition(
                    (player.getTiles().get(0).getX() + 1) * App.tileWidth,
                    player.getTiles().get(0).getY() * App.tileHeight
                );

                super.act(delta);
            }
        };
        group.addActor(window);
        menuGroup.addActor(group);
    }
}
