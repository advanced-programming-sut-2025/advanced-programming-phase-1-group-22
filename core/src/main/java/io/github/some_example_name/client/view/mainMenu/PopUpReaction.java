package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.client.controller.WorldController;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;

import java.util.ArrayList;
import java.util.List;

public class PopUpReaction extends PopUp {
    private final List<Texture> defaultEmoji = new ArrayList<>();

    public PopUpReaction() {
        for (int i = 0; i <= 10; i++) {
            defaultEmoji.add(GameAsset.emojiTextures.get(i));
        }
    }

    @Override
    public void createMenu(Stage stage, Skin skin, WorldController playerController) {
        super.createMenu(stage, skin, playerController);
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        Window window = new Window("Reaction", skin);
        window.setSize(camera.viewportWidth * 0.5f, camera.viewportHeight * 0.6f);
        window.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);

        Table content = new Table();
        content.setFillParent(true);
        window.add(content).expand().fill().pad(10);

        Table mainTable = new Table();

        Table emojiTable = new Table();
        emojiTable.defaults().pad(4).width(40).height(40);
        int emojiColumns = 5;

        for (int i = 0; i < defaultEmoji.size(); i++) {
            Image emojiImg = new Image(defaultEmoji.get(i));
            int finalI = i;
            emojiImg.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    App.getInstance().getCurrentGame().getCurrentPlayer().setEmojiReactionIndex(GameAsset.emojiTextures.indexOf(defaultEmoji.get(finalI)));
                    GameClient.getInstance().updatePlayerReaction(App.getInstance().getCurrentGame().getCurrentPlayer());
                    App.getInstance().getCurrentGame().getCurrentPlayer().setLastReaction(0f);
                }
            });
            emojiTable.add(emojiImg);
            if ((i + 1) % emojiColumns == 0) emojiTable.row();
        }

        VerticalGroup emojiGroup = new VerticalGroup();
        emojiGroup.space(10);
        emojiGroup.addActor(new Label("Default Emojis", skin));
        emojiGroup.addActor(emojiTable);

        Table textTable = new Table();
        textTable.defaults().pad(5);

        for (String text : GameAsset.defaultText) {
            TextButton btn = new TextButton(text, skin);
            btn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    App.getInstance().getCurrentGame().getCurrentPlayer().setTextReaction(text);
                    GameClient.getInstance().updatePlayerReaction(App.getInstance().getCurrentGame().getCurrentPlayer());
                    App.getInstance().getCurrentGame().getCurrentPlayer().setLastReaction(0f);
                }
            });
            textTable.add(btn).left().row();
        }

        VerticalGroup textGroup = new VerticalGroup();
        textGroup.space(10);
        textGroup.addActor(new Label("Default Messages", skin));
        textGroup.addActor(textTable);

        mainTable.add(emojiGroup).left().padRight(20).top();
        mainTable.add(textGroup).right().top();

        content.add(mainTable).expand().fill().row();

        TextButton editButton = new TextButton("Edit Emojis", skin);
        editButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showEmojiEditor(stage, skin);
            }
        });
        content.add(editButton).center().padTop(20).colspan(2);

        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        ImageButton exitButton = provideExitButton(array);

        Group group = new Group() {
            @Override
            public void act(float delta) {
                window.setPosition(
                    (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f +
                        MainGradle.getInstance().getCamera().position.x - MainGradle.getInstance().getCamera().viewportWidth / 2,
                    (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f +
                        MainGradle.getInstance().getCamera().position.y - MainGradle.getInstance().getCamera().viewportHeight / 2
                );
                exitButton.setPosition(
                    window.getX() + window.getWidth() - exitButton.getWidth() / 2f + 16,
                    window.getY() + window.getHeight() - exitButton.getHeight() / 2f
                );

                super.act(delta);
            }
        };
        group.addActor(window);
        group.addActor(exitButton);
        getMenuGroup().addActor(group);
    }

    private void showEmojiEditor(Stage stage, Skin skin) {
        Window editor = new Window("Select Emoji", skin);
        editor.setSize(500, 400);

        Table emojiGrid = new Table();
        emojiGrid.defaults().pad(4).width(40).height(40);

        int columns = 8;
        for (int i = 0; i < GameAsset.emojiTextures.size(); i++) {
            Texture tex = GameAsset.emojiTextures.get(i);
            Image img = new Image(tex);
            int finalI = i;
            img.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    showReplaceDialog(finalI, skin);
                    editor.remove();
                }
            });
            emojiGrid.add(img);
            if ((i + 1) % columns == 0) emojiGrid.row();
        }

        ScrollPane scrollPane = new ScrollPane(emojiGrid, skin);
        scrollPane.setFadeScrollBars(false);

        editor.add(scrollPane).expand().fill().pad(10);
        editor.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - editor.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - editor.getHeight()) / 2f
        );

        getMenuGroup().addActor(editor);
    }

    private void showReplaceDialog(int selectedEmojiIndex, Skin skin) {
        Window replaceWindow = new Window("Replace Which Emoji?", skin);
        Table table = new Table();
        table.defaults().pad(5).width(40).height(40);

        for (int i = 0; i < defaultEmoji.size(); i++) {
            Texture tex = defaultEmoji.get(i);
            Image img = new Image(tex);
            int finalI = i;
            img.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    defaultEmoji.set(finalI, GameAsset.emojiTextures.get(selectedEmojiIndex));
                    replaceWindow.remove();
                    getMenuGroup().clear();
                    createMenu(stage, skin, getController());
                }
            });
            table.add(img);
            if ((i + 1) % 5 == 0) table.row();
        }

        replaceWindow.add(table).pad(10);
        replaceWindow.pack();
        replaceWindow.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - replaceWindow.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - replaceWindow.getHeight()) / 2f
        );

        getMenuGroup().addActor(replaceWindow);
    }

    @Override
    protected void handleDragRelease(InputEvent event, float x, float y, int pointer, Image itemImage, Salable item, Image dragImage, Boolean flag) {
    }
}
