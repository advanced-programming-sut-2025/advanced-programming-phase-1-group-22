package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.client.controller.WorldController;
import io.github.some_example_name.common.model.Notification;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.server.service.RelationService;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;
import io.github.some_example_name.client.view.GameView;
import lombok.Setter;


@Setter
public class NotificationMenu extends PopUp {
    private float scrollX = 0, scrollY = 0;


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

        Window window = new Window("", skin);
        window.setSize(camera.viewportWidth * 0.35f, camera.viewportHeight);
        window.setMovable(false);

        Table info = new Table();
        ScrollPane scrollPane = new ScrollPane(info, skin);
        info.pack();
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollbarsOnTop(true);
        scrollPane.setScrollingDisabled(false, false);
        scrollPane.setScrollBarPositions(true, true);
        scrollPane.setForceScroll(false, true);
        scrollPane.setScrollX(scrollX);
        scrollPane.setScrollY(scrollY);
        scrollPane.layout();
        scrollPane.setTouchable(Touchable.enabled);

        for (Notification<Actor, io.github.some_example_name.common.model.Actor> notification :
            App.getInstance().getCurrentGame().getCurrentPlayer().getNotifications()) {
            Table row = new Table(skin);
            Image avatar = new Image(notification.source().getAvatar());
            Image type = new Image(notification.type().getTexture());
            row.add(avatar).size(50).padRight(20);
            row.add(type).size(50).padRight(20);
            row.add(notification.data()).width(camera.viewportWidth * (0.35f) - 250).padRight(20);
            ImageButton readButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.EXIT_BUTTON)));
            row.add(readButton).size(30).row();
            info.add(row).row();
            row.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    row.remove();
                    if (x < readButton.getX() || x > readButton.getX() + readButton.getWidth() ||
                        y < readButton.getY() || y > readButton.getY() + readButton.getHeight()) {
                        switch (notification.type()) {
                            case GIFT: {
                                GiftHistoryMenu menu = new GiftHistoryMenu();
                                menu.setFriendship(RelationService.getInstance().getFriendShipBetweenTwoActors(notification.source()));
                                menu.createMenu(stage, skin, getController());
                            } break;
                            case TALK: {
                                TalkMenu menu = new TalkMenu();
                                menu.setFriendship(RelationService.getInstance().getFriendShipBetweenTwoActors(notification.source()));
                                menu.createMenu(stage, skin, getController());
                            } break;
                            case TRADE, MARRIAGE: {
                                //todo
                            } break;
                        }
                        close(window);
                    }
                    App.getInstance().getCurrentGame().getCurrentPlayer().getNotifications().remove(notification);
                    return true;
                }
            });
        }

        Table content = new Table();
        content.setFillParent(true);
        content.add(scrollPane).width(window.getWidth()).height(window.getHeight() - 100).padBottom(20).padTop(50).row();


        window.add(content).fill().pad(10);
        Group group = new Group() {
            @Override
            public void act(float delta) {
                window.setPosition(
                    camera.position.x - camera.viewportWidth/2f + 30,
                    camera.position.y - camera.viewportHeight/2f + 30
                );
                if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
                    if (!Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) &&
                        !Gdx.input.isKeyPressed(Input.Keys.F)) close(window);
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
