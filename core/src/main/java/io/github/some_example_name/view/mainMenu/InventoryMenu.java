package io.github.some_example_name.view.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.GameAsset;

import java.util.ArrayList;

public class InventoryMenu {
    public static void createMenu(Stage stage, Skin skin){
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        Texture slotTexture = new Texture(Gdx.files.internal("button-.png"));
        Window window = new Window("", skin);
        window.setSize(700, 500);
        window.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);

        Table tabs = new Table();
        tabs.top().left();
        tabs.defaults().size(80, 80).padRight(4);
        Image tab1 = new Image(GameAsset.INVENTORY_TAB);
        Image tab2 = new Image(GameAsset.SKILL_TAB);
        Image tab3 = new Image(GameAsset.SOCIAL_TAB);
        Image tab4 = new Image(GameAsset.MAP_TAB);
        tabs.add(tab1);
        tabs.add(tab2);
        tabs.add(tab3);
        tabs.add(tab4);

        ImageButton trashCan = new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.WORM_BIN)));
        trashCan.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        Table inventory = new Table();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                Image slot = new Image(slotTexture);

                int index = row * 9 + col;
                if (index < currentPlayer.getInventory().getProducts().size()) {
                    java.util.List<Salable> items = new ArrayList<>(currentPlayer.getInventory().getProducts().keySet());
                    Salable item = items.get(index);
                    Image itemImage = new Image(item.getTexture());
                    addDrag(itemImage,stage,currentPlayer,trashCan,item,inventory,slotTexture);
                    itemImage.setSize(60, 60);
                    Stack stack = new Stack();
                    stack.add(slot);
                    stack.add(itemImage);
                    inventory.add(stack).size(64,64);
                } else {
                    inventory.add(slot).size(64,64);
                }
            }
            inventory.row();
        }
        ScrollPane scrollPane = new ScrollPane(inventory);
        scrollPane.setFadeScrollBars(true);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setScrollbarsOnTop(false);
        scrollPane.setForceScroll(false, false);
        scrollPane.layout();

        Table finances = new Table();
        finances.left();
        finances.add(new Label(App.getInstance().getCurrentGame().getCurrentPlayer().getFarmType().getName(), skin)).row();
        finances.add(new Label("Current Funds: " + App.getInstance().getCurrentGame().getCurrentPlayer().getAccount().getGolds() + "g", skin)).row();

        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.EXIT_BUTTON)));
        exitButton.setSize(32, 32);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                window.remove();
                exitButton.remove();
                trashCan.remove();
                tabs.remove();
            }
        });


        Table content = new Table();
        content.setFillParent(true);
        content.add(scrollPane).width(600).height(220).padBottom(20).row();
        content.add(finances).padTop(100).top().row();

        window.add(content).expand().fill().pad(10);
        Group group = new Group() {
            @Override
            public void act(float delta) {
                window.setPosition(
                    (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f +
                        MainGradle.getInstance().getCamera().position.x - MainGradle.getInstance().getCamera().viewportWidth/2,
                    (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f +
                        MainGradle.getInstance().getCamera().position.y - MainGradle.getInstance().getCamera().viewportHeight/2
                );
                exitButton.setPosition(
                    window.getX() + window.getWidth() - exitButton.getWidth() / 2f + 16,
                    window.getY() + window.getHeight() - exitButton.getHeight() / 2f
                );
                trashCan.setPosition(
                    window.getX() + window.getWidth() - trashCan.getWidth() / 2f + 50,
                    window.getY() + window.getHeight() - trashCan.getHeight() / 2f - 300
                );
                tabs.setPosition(
                    window.getX(),
                    window.getY() + window.getHeight() - tabs.getHeight() / 2f + 70
                );

                super.act(delta);
            }
        };
        group.addActor(window);
        group.addActor(exitButton);
        group.addActor(trashCan);
        group.addActor(tabs);
        stage.addActor(group);
    }

    private static boolean isOverTrashCan(Image item, ImageButton trashCan) {
        float itemX = item.getX();
        float itemY = item.getY();
        float itemWidth = item.getWidth();
        float itemHeight = item.getHeight();

        float trashX = trashCan.getX();
        float trashY = trashCan.getY();
        float trashWidth = trashCan.getWidth();
        float trashHeight = trashCan.getHeight();

        return itemX < trashX + trashWidth &&
            itemX + itemWidth > trashX &&
            itemY < trashY + trashHeight &&
            itemY + itemHeight > trashY;
    }

    private static void refreshInventory(Stage stage, Table inventory, Player player, Texture slotTexture,Player currentPlayer,ImageButton trashCan) {
        inventory.clear();
        java.util.List<Salable> items = new ArrayList<>(player.getInventory().getProducts().keySet());

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                Image slot = new Image(slotTexture);
                int index = row * 9 + col;

                if (index < items.size()) {
                    Salable item = items.get(index);
                    Image itemImage = new Image(item.getTexture());
                    itemImage.setSize(60, 60);
                    addDrag(itemImage,stage,currentPlayer,trashCan,item,inventory,slotTexture);
                    Stack stack = new Stack();
                    stack.add(slot);
                    stack.add(itemImage);
                    inventory.add(stack).size(64,64);
                } else {
                    inventory.add(slot).size(64,64);
                }
            }
            inventory.row();
        }
    }

    private static void addDrag(Image itemImage,Stage stage,Player currentPlayer,ImageButton trashCan,Salable item,Table inventory,Texture slotTexture){
        itemImage.addListener(new DragListener() {
            private Stack originalStack;
            private final Vector2 originalPos = new Vector2();
            private Image dragImage;

            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
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
                        stagePos.x - dragImage.getWidth()/2,
                        stagePos.y - dragImage.getHeight()/2
                    );
                }
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                if (dragImage != null) {
                    if (isOverTrashCan(dragImage, trashCan)) {
                        currentPlayer.getInventory().deleteProductFromBackPack(item, currentPlayer, 1);
                    }

                    dragImage.remove();
                    dragImage = null;
                    refreshInventory(stage, inventory, currentPlayer, slotTexture,currentPlayer,trashCan);
                }
            }
        });
    }
}
