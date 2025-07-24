package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Timer;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.client.controller.WorldController;
import io.github.some_example_name.common.model.Entry;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.model.relations.Friendship;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.server.service.RelationService;
import io.github.some_example_name.common.utils.App;
import lombok.Setter;

import java.util.ArrayList;

@Setter
public class TalkMenu extends PopUp {
    private Friendship friendship;
    private Player friend;
    private Window window;
    private Float scrollY;

    public void createMenu(Stage stage, Skin skin, WorldController worldController) {
        super.createMenu(stage, skin, worldController);
        if (friendship.getSecondPlayer() == App.getInstance().getCurrentGame().getCurrentPlayer()) {
            friend = (Player) friendship.getFirstPlayer();
        } else {
            friend = (Player) friendship.getSecondPlayer();
        }
        createInventory(skin, getMenuGroup(), stage);
    }

    @Override
    protected void handleDragRelease(InputEvent event, float x, float y, int pointer, Image itemImage, Salable item, Image dragImage, Boolean flag) {}

    private void createInventory(Skin skin, Group menuGroup, Stage stage) {
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        OrthographicCamera camera = MainGradle.getInstance().getCamera();

        window = new Window("", skin);
        window.setSize(camera.viewportWidth * 0.7f, camera.viewportHeight * 0.7f);

        window.setPosition(
            camera.viewportWidth - window.getWidth() / 2f,
            camera.viewportHeight - window.getHeight() / 2f
        );
        window.setMovable(false);

        Table inventory = new Table();
        ScrollPane scrollPane = new ScrollPane(inventory, skin);
        inventory.pack();
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollbarsOnTop(true);
        scrollPane.setScrollingDisabled(false, false);
        scrollPane.setScrollBarPositions(true, true);
        scrollPane.setForceScroll(false, true);
        scrollPane.setScrollY(scrollY == null ? 0 : scrollY);
        scrollPane.layout();
        scrollPane.setTouchable(Touchable.enabled);

        Table otherPlayer = new Table();

        otherPlayer.add(new Image(friend.getAvatar())).size(200).row();
        otherPlayer.add(new Label(friend.getUser().getNickname(), skin)).width(200).expandX().row();
        for (Entry<String, io.github.some_example_name.common.model.Actor> dialog : friendship.getDialogs()) {
            boolean isGifting = dialog.getValue() == currentPlayer;
            Table giftTable = new Table(skin);
            for (String line : wrapString(dialog.getKey(), 20)) {
                giftTable.add(new Label(line, skin));
                if (!isGifting) giftTable.right();
                giftTable.row();
            }
            Table rowWrapper = new Table();

            if (isGifting) {
                rowWrapper.add(giftTable).left().expandX();
                rowWrapper.add();
            } else {
                rowWrapper.add();
                rowWrapper.add(giftTable).right().expandX();
            }
            inventory.add(rowWrapper).padRight(10).expandX().fillX().row();
        }

        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        ImageButton exitButton = provideExitButton(array);


        Table content = new Table();
        content.setFillParent(true);
        content.center();
        content.add(scrollPane).width(window.getWidth() * 0.7f).height(window.getHeight() * 0.8f - 100).padBottom(20).padTop(50);
        content.add(otherPlayer).width(window.getWidth() * 0.15f).row();


        TextArea input = new TextArea("", skin);
        content.add(input).fillX().height(window.getHeight()*0.1f);
        TextButton send = new TextButton("Send", skin);
        content.add(send).fillX().height(window.getHeight()*0.1f).row();

        window.add(content).fill().pad(10);
        Group group = new Group() {
            @Override
            public void act(float delta) {
                window.setPosition(
                    (camera.viewportWidth - window.getWidth()) / 2f +
                        camera.position.x - camera.viewportWidth / 2,
                    (camera.viewportHeight - window.getHeight()) / 2f +
                        camera.position.y - camera.viewportHeight / 2
                );
                exitButton.setPosition(
                    window.getX() + window.getWidth() - exitButton.getWidth() / 2f + 16 - exitButton.getWidth(),
                    window.getY() + window.getHeight()
                );

                if (send.isChecked() || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                    String message = input.getText();
                    if (!"".equals(message) && !wrapString(message, 100).isEmpty()) {
                        Response resp = RelationService.getInstance().talkToAnotherPlayer(friend, message);
                        if (!resp.shouldBeBack()) getController().showResponse(resp);
                        else {
                            Table giftTable = new Table(skin);
                            for (String line : wrapString(message, 20)) {
                                giftTable.add(new Label(line, skin)).row();
                            }
                            Table rowWrapper = new Table();
                            rowWrapper.add(giftTable).left().expandX();
                            rowWrapper.add();
                            inventory.add(rowWrapper).expandX().fillX().row();
                            Timer.schedule(new Timer.Task() {
                                @Override
                                public void run() {
                                    scrollPane.setScrollY(scrollPane.getMaxY());
                                }
                            }, 0.2f);
                        }
                    }
                    send.setChecked(false);
                }

                super.act(delta);
            }
        };
        group.addActor(window);
        group.addActor(exitButton);
        menuGroup.addActor(group);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                scrollPane.setScrollY(scrollY == null ? scrollPane.getMaxY() : Math.max(0, scrollY));
            }
        }, 0.2f);
    }
}
