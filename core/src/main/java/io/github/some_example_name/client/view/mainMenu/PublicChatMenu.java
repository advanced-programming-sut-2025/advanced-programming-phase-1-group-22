package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Timer;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.client.controller.WorldController;
import io.github.some_example_name.common.model.Entry;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.model.relations.Friendship;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.server.service.RelationService;
import lombok.Setter;

import java.util.ArrayList;

@Setter
public class PublicChatMenu extends PopUp {
    private Window window;
    private Float scrollY;
    private int lastMessage = 0;

    public void createMenu(Stage stage, Skin skin, WorldController worldController) {
        super.createMenu(stage, skin, worldController);
        createInventory(skin, getMenuGroup(), stage);
    }

    @Override
    protected void handleDragRelease(InputEvent event, float x, float y, int pointer, Image itemImage, Salable item, Image dragImage, Boolean flag) {}

    private void createInventory(Skin skin, Group menuGroup, Stage stage) {
        synchronized (App.getInstance().getCurrentGame().getDialogsUpdated()) {
            App.getInstance().getCurrentGame().getDialogsUpdated().set(false);
        }
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

        for (Entry<String, io.github.some_example_name.common.model.Actor> dialog : App.getInstance().getCurrentGame().getDialogs()) {
            addMessage(dialog, currentPlayer, skin, inventory);
            lastMessage++;
        }

        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        ImageButton exitButton = provideExitButton(array);


        Table content = new Table();
        content.setFillParent(true);
        content.center();
        content.add(scrollPane).width(window.getWidth() * 0.8f).height(window.getHeight() * 0.8f - 100).padBottom(20).padTop(50).row();


        TextArea input = new TextArea("", skin);
        content.add(input).width(window.getWidth() * 0.65f).height(window.getHeight()*0.1f);
        TextButton send = new TextButton("Send", skin);
        content.add(send).width(window.getWidth() * 0.15f).height(window.getHeight()*0.1f).row();

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
                        input.setText("");
                        GameClient.getInstance().sendPublic(message);
                    }
                    send.setChecked(false);
                }

                synchronized (App.getInstance().getCurrentGame()) {
                    if (App.getInstance().getCurrentGame().getDialogsUpdated().get()) {
                        for (; lastMessage <  App.getInstance().getCurrentGame().getDialogs().size(); lastMessage++) {
                            Entry<String, io.github.some_example_name.common.model.Actor> dialog = App.getInstance().getCurrentGame().getDialogs().get(lastMessage);
                            addMessage(dialog, currentPlayer, skin, inventory);
                        }
                    }
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

    private void addMessage(Entry<String, io.github.some_example_name.common.model.Actor> dialog, Player currentPlayer, Skin skin, Table inventory) {
        boolean isGifting = dialog.getValue() == currentPlayer;
        Table giftTable = new Table(skin);
        Table rowWrapper = new Table();
        for (String line : wrapString(dialog.getKey(), 20)) {
            giftTable.add(new Label(line, skin));
            if (!isGifting) giftTable.right();
            giftTable.row();
        }

        if (isGifting) {
            rowWrapper.add(giftTable).left().expandX();
            rowWrapper.add();
            rowWrapper.add();
        } else {
            rowWrapper.add();
            rowWrapper.add(giftTable).right().expandX();
            rowWrapper.add(new Image(dialog.getValue().getAvatar())).size(64);
        }
        inventory.add(rowWrapper).padRight(10).expandX().fillX().row();
    }
}
