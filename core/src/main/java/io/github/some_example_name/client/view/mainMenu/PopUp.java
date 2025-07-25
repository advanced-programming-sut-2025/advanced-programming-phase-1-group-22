package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import io.github.some_example_name.client.controller.WorldController;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.server.service.GameService;
import io.github.some_example_name.common.utils.GameAsset;
import io.github.some_example_name.client.view.GameView;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Map;

@Setter
@Getter
public abstract class PopUp {
    private final Group menuGroup = new Group();
    private final Table tabs = new Table();
    private final GameService gameService = new GameService();
    private WorldController controller;
    protected ImageButton trashCan;
    protected Stage stage;
    protected Skin skin = GameAsset.SKIN;
    protected Texture slotTexture = GameAsset.BUTTON;

    public void createMenu(Stage stage, Skin skin, WorldController playerController) {
        GameView.captureInput = false;
        controller = playerController;
        this.stage = stage;
        if (!stage.getActors().contains(menuGroup, true)) {
            stage.addActor(menuGroup);
        }
        menuGroup.clear();
    }

    protected boolean isOverActor(Image item, Actor actor) {
        float itemX = item.getX();
        float itemY = item.getY();
        float itemWidth = item.getWidth();
        float itemHeight = item.getHeight();

        float trashX = actor.getX();
        float trashY = actor.getY();
        float trashWidth = actor.getWidth();
        float trashHeight = actor.getHeight();

        return itemX < trashX + trashWidth &&
            itemX + itemWidth > trashX &&
            itemY < trashY + trashHeight &&
            itemY + itemHeight > trashY;
    }


    protected void addDrag(Image itemImage, Stage stage, Player currentPlayer, Salable item, ArrayList<ScrollPane> scrollPanes, boolean flag) {
        itemImage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                event.stop();
                return true;
            }
        });
        itemImage.addListener(new DragListener() {
            private Stack originalStack;
            private final Vector2 originalPos = new Vector2();
            private Image dragImage;

            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                for (ScrollPane scrollPane : scrollPanes) {
                   scrollPane.setTouchable(Touchable.disabled);
                }
                originalStack = (Stack) itemImage.getParent();
                originalPos.set(itemImage.getX(), itemImage.getY());

                dragImage = new Image(itemImage.getDrawable());
                dragImage.setSize(itemImage.getWidth(), itemImage.getHeight());

                Vector2 stagePos = originalStack.localToStageCoordinates(new Vector2(originalPos.x, originalPos.y));
                dragImage.setPosition(stagePos.x, stagePos.y);
                stage.addActor(dragImage);
                dragImage.toFront();
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                if (dragImage != null) {
                    Vector2 localPos = new Vector2(x, y);
                    Vector2 stagePos = originalStack.localToStageCoordinates(localPos);
                    dragImage.setPosition(
                        stagePos.x - dragImage.getWidth() / 2,
                        stagePos.y - dragImage.getHeight() / 2
                    );
                }
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                if (dragImage != null) {
                    dragImage.remove();
                    for (ScrollPane scrollPane : scrollPanes) {
                        scrollPane.setTouchable(Touchable.enabled);
                        scrollPane.layout();
                    }
                    handleDragRelease(event, x, y, pointer, itemImage, item, dragImage, flag);
                    dragImage = null;
                }
            }
        });
    }

    abstract protected void handleDragRelease(InputEvent event, float x, float y, int pointer, Image itemImage, Salable item, Image dragImage, Boolean flag);

    protected void close() {
        GameView.captureInput = true;
    }

    protected ImageButton provideExitButton(ArrayList<Actor> array) {
        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.EXIT_BUTTON)));
        exitButton.setSize(32, 32);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                endTask(array, exitButton);
            }
        });
        return exitButton;
    }

    protected void endTask(ArrayList<Actor> array, ImageButton exitButton) {
        for (Actor o : array) {
            o.remove();
        }
        exitButton.remove();
        close();
    }

    public static <T> Container<Label> getLabelContainer(Map<T, Integer> list, T item) {
        int count = list.get(item);
        Label countLabel = new Label(String.valueOf(count), GameAsset.SKIN);
        countLabel.setFontScale(0.7f);
        countLabel.setAlignment(Align.right);
        countLabel.setColor(Color.GREEN);
        Container<Label> labelContainer = new Container<>(countLabel);
        labelContainer.setFillParent(false);
        labelContainer.setSize(30, 20);
        labelContainer.setPosition(66, 5);
        return labelContainer;
    }
    public static ArrayList<String> wrapString(String string, int length) {
        ArrayList<String> lines = new ArrayList<>();
        if (string == null || string.isEmpty() || length <= 0) return lines;

        String[] words = string.split("\\s+");
        StringBuilder currentLine = new StringBuilder();
        for (String word : words) {
            if (currentLine.length() + word.length() + 1 > length) {
                lines.add(currentLine.toString().trim());
                currentLine.setLength(0);
            }
            currentLine.append(word).append(" ");
        }
        if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString().trim());
        }
        return lines;
    }
}
