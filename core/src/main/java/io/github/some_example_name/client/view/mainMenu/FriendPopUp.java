package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
import io.github.some_example_name.common.model.enums.Gender;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.server.ClientHandler;
import io.github.some_example_name.server.service.RelationService;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.client.view.GameView;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Setter
public class FriendPopUp extends PopUp {
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
        TextButton hug = new TextButton("Hug!", skin);
        TextButton flower = new TextButton("Give Flower!", skin);
        TextButton marry = new TextButton("Ask Marriage!", skin);
        info.add(flower).colspan(2).expandX().row();
        info.add(hug).colspan(2).expandX().row();
        if (App.getInstance().getCurrentGame().getCurrentPlayer().getUser().getGender() == Gender.MALE &&
            player.getUser().getGender() == Gender.FEMALE &&
            App.getInstance().getCurrentGame().getCurrentPlayer().getCouple() == null) {
            info.add(marry).colspan(2).expandX().row();
        }
        hug.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                handleHug(player);
                return true;
            }
        });

        flower.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                handleFlower(player);
                return true;
            }
        });

        marry.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                handleAskMarriage(player);
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
                if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
                    if (!Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) close(window);
                }
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    Vector3 mouse = new Vector3(GameView.screenX, GameView.screenY,0);
                    MainGradle.getInstance().getCamera().unproject(mouse);
                    Vector2 mouseWorld = new Vector2(mouse.x,mouse.y);
                    if (mouseWorld.x < window.getX() || mouseWorld.x > window.getX() + window.getWidth() ||
                        mouseWorld.y < window.getY() || mouseWorld.y > window.getY() + window.getHeight()) close(window);
                }

                super.act(delta);
            }
        };
        group.addActor(window);
        menuGroup.addActor(group);
    }

    private void handleAskMarriage(Player player) {
        Response resp = RelationService.getInstance().marry(player.getUser().getUsername(), "Wedding Ring");
        initialHandle(resp);
        if (resp.shouldBeBack()) {
            GameClient.getInstance().askMarriage(player.getUser().getUsername());
        }
    }

    private void initialHandle(Response resp) {
        window.remove();
        if (!resp.shouldBeBack()) {
            getController().showResponse(resp);
            return;
        }
        GameView.captureInput = false;
    }

    private void handleFlower(Player player) {
        Response resp =  RelationService.getInstance().giveGift(player.getUser().getUsername(), "flower", 1);
        initialHandle(resp);
        if (resp.shouldBeBack()) {
            GameClient.getInstance().handleFlower(player.getUser().getUsername());
        }
    }

    private void handleHug(Player player) {
        Response resp = RelationService.getInstance().hug(player.getUser().getUsername());
        initialHandle(resp);
        if (resp.shouldBeBack()) {
            GameClient.getInstance().handleHug(player.getUser().getUsername());
        }
    }
}
