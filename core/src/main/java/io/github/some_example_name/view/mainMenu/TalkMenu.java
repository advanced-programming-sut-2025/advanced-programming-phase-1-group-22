package io.github.some_example_name.view.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.controller.WorldController;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.model.relations.Dialog;
import io.github.some_example_name.model.relations.Friendship;
import io.github.some_example_name.model.relations.Gift;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.saveGame.ObjectMapWrapper;
import io.github.some_example_name.service.RelationService;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.GameAsset;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

@Setter
public class TalkMenu extends PopUp {
    private Friendship friendship;
    private Player friend;
    private Window window;
    private float scrollX = 0, scrollY = 0;

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
        scrollPane.setScrollX(scrollX);
        scrollPane.setScrollY(scrollY);
        scrollPane.layout();
        scrollPane.setTouchable(Touchable.enabled);

        inventory.add(new Image(friend.getAvatar())).size(100).padRight(20);
        inventory.add(new Label(friend.getUser().getNickname(), skin)).colspan(3).width(80).expandX().row();
        int i = 0;
        for (Map.Entry<String, io.github.some_example_name.model.Actor> dialog : friendship.getDialogs().entrySet()) {
            boolean isGifting = dialog.getValue() == currentPlayer;
            Table giftTable = new Table(skin);
            if (isGifting) giftTable.left();
            else {
                inventory.add();
                inventory.add();
                giftTable.right();
            }
            for (String line : wrapString(dialog.getKey(), 100)) {
                giftTable.add(new Label(line, skin)).width(100).expandX().row();
            }
            inventory.add(giftTable).colspan(2).expandX();
            if (isGifting) {
                inventory.add();
                inventory.add();
            }
            inventory.row();
            i++;
        }

        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        ImageButton exitButton = provideExitButton(array);

        Table content = new Table();
        content.setFillParent(true);
        content.add(scrollPane).colspan(2).width(window.getWidth()*0.9f).height(window.getHeight()*0.8f - 100).padBottom(20).padTop(50).row();

        TextArea input = new TextArea("", skin);
        content.add(input).height(window.getHeight()*0.2f).width(window.getWidth() * 0.8f);
        TextButton send = new TextButton("Send", skin);
        content.add(send).expandX().row();

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
                            scrollX = scrollPane.getScrollX();
                            scrollY = scrollPane.getScrollY();
                            endTask(array, exitButton);
                            createInventory(skin, menuGroup, stage);
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
    }
}
