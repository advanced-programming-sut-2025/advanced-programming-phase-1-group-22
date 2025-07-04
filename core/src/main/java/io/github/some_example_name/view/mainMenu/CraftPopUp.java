package io.github.some_example_name.view.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.controller.WorldController;
import io.github.some_example_name.model.craft.Craft;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.service.GameService;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.view.GameView;

public class CraftPopUp {
    private final Group menuGroup = new Group();
    private final GameService gameService = new GameService();
    private WorldController controller;
    private Craft craft;

    public void createMenu(Craft craft, Stage stage, Skin skin, WorldController worldController) {
        this.craft = craft;
        controller = worldController;
        if (!stage.getActors().contains(menuGroup, true)) {
            stage.addActor(menuGroup);
        }
        menuGroup.clear();
        createInventory(skin, menuGroup, stage);
    }

    private void close(Window window) {
        window.remove();
    }

    private void createInventory(Skin skin, Group menuGroup, Stage stage) {
        OrthographicCamera camera = MainGradle.getInstance().getCamera();

        Window window = new Window("", skin);
        window.setSize(camera.viewportWidth * 0.3f, camera.viewportHeight * 0.35f);
        window.setMovable(false);

        Table info = new Table();

        info.pack();
        info.add(new Label(craft.getName(), skin)).colspan(2).padRight(10);
        info.add(new Image(craft.getMadeProduct().getTexture())).width(70).height(70).padRight(10);
        info.add(new Label(craft.getMadeProduct().getName(), skin)).left().row();
        info.add(new Label(craft.getCraftType().getDescription(), skin)).colspan(2).row();
        TextButton cancel = new TextButton("Cancel!", skin);
        cancel.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                craft.setMadeProduct(null);
                craft.setETA(null);
                close(window);
                return true;
            }
        });
        TextButton cheat = new TextButton("Don't Touch!", skin);
        cheat.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) return false;
                craft.setETA(App.getInstance().getCurrentGame().getTimeAndDate().copy());
                close(window);
                createMenu(craft, stage, skin, controller);
                return true;
            }
        });
        info.add(cheat).padTop(20).colspan(2).row();
        if (craft.getETA().compareTime(App.getInstance().getCurrentGame().getTimeAndDate()) < 0) {
            long differ = Math.abs(craft.getETA().difference(App.getInstance().getCurrentGame().getTimeAndDate()));
            info.add(new Label((differ / (60 * 24)) + "d " + (differ / 60 % 24) + "h " + (differ % 60) + "m", skin))
                .colspan(2).row();
        } else {
            TextButton button = new TextButton("Get product!", skin);
            info.add(button).padTop(50).colspan(2).row();
            button.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    Response resp = gameService.artisanGet(craft);
                    controller.showResponse(resp);
                    if (resp.shouldBeBack()) {
                        close(window);
                        return true;
                    }
                    return false;
                }
            });
        }

        Table content = new Table();
        content.setFillParent(true);
        content.add(info).width(window.getWidth()).height(window.getHeight() - 100).padBottom(20).padTop(50).row();


        window.add(content).fill().pad(10);
        Group group = new Group() {
            @Override
            public void act(float delta) {
                window.setPosition(
                    (craft.getTiles().get(0).getX() + 1) * App.tileWidth,
                    craft.getTiles().get(0).getY() * App.tileHeight
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
}
