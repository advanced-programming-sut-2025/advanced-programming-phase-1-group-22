package io.github.some_example_name.view.mainMenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.controller.WorldController;
import io.github.some_example_name.model.Farm;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.craft.Craft;
import io.github.some_example_name.model.craft.CraftType;
import io.github.some_example_name.model.products.TreesAndFruitsAndSeeds.MadeProductType;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.model.relations.Friendship;
import io.github.some_example_name.model.relations.NPC;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.source.Mineral;
import io.github.some_example_name.model.source.MineralType;
import io.github.some_example_name.model.tools.BackPack;
import io.github.some_example_name.service.RelationService;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.GameAsset;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Setter
public class GiftMenu extends PopUp {
    private Friendship friendship;
    private Player friend;
    private Salable item;
    private Integer count = 1;
    private Window window, window2;

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
    protected void handleDragRelease(InputEvent event, float x, float y, int pointer, Image itemImage, Salable item, Image dragImage, Boolean flag) {
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        if (isOverActor(dragImage, trashCan)) {
            if (flag) {
                currentPlayer.getInventory().deleteProductFromBackPack(item, currentPlayer, 1);
            } else {
                this.item = null;
                count = 0;
            }
        } else if (flag && isOverActor(dragImage, window2)) {
            count = 1;
            this.item = item;
        } else if (!flag && isOverActor(dragImage, window)) {
            this.item = null;
            count = 0;
        }
        createMenu(stage, skin, getController());
    }

    private void createInventory(Skin skin, Group menuGroup, Stage stage) {
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        OrthographicCamera camera = MainGradle.getInstance().getCamera();

        window = new Window("", skin);
        window2 = new Window("", skin);
        window.setSize(camera.viewportWidth * 0.7f, camera.viewportHeight * 0.35f);
        window.setPosition(
            (camera.viewportWidth - window.getWidth()) / 2f,
            (camera.viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);

        trashCan = new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.WORM_BIN)));
        Table inventory = new Table();
        Table fridge = new Table();
        Table info = new Table();
        ScrollPane scrollPane = new ScrollPane(inventory, skin);
        inventory.pack();
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollbarsOnTop(true);
        scrollPane.setScrollingDisabled(false, false);
        scrollPane.setScrollBarPositions(true, true);
        scrollPane.setForceScroll(false, true);
        scrollPane.layout();
        scrollPane.setTouchable(Touchable.enabled);

        int maxCol = 12;
        int maxRow;
        BackPack backPack = App.getInstance().getCurrentGame().getCurrentPlayer().getInventory();
        if (backPack.getBackPackType().getIsInfinite()) {
            maxRow = Math.max(5, backPack.getProducts().size() / maxCol + 1);
        } else {
            maxRow = (int) Math.ceil((double) backPack.getBackPackType().getCapacity() / maxCol);
        }

        for (int row = 0; row < maxRow; row++) {
            for (int col = 0; col < maxCol; col++) {
                Image slot = new Image(slotTexture);

                int index = row * maxCol + col;
                if (index < backPack.getProducts().size()) {
                    java.util.List<Salable> items = new ArrayList<>(backPack.getProducts().keySet());
                    Salable item = items.get(index);
                    Image itemImage = new Image(item.getTexture());
                    ArrayList<ScrollPane> list = new ArrayList<>();
                    list.add(scrollPane);
                    addDrag(itemImage, stage, currentPlayer, item, list,true);
                    itemImage.setSize(90, 90);
                    Container<Label> labelContainer = getLabelContainer(backPack.getProducts(), item);
                    Stack stack = new Stack();
                    stack.add(slot);
                    stack.add(itemImage);
                    stack.add(labelContainer);
                    inventory.add(stack).size(96, 96);
                } else {
                    inventory.add(slot).size(96, 96);
                }
            }
            inventory.row();
        }

        window2.setSize(camera.viewportWidth * 0.7f, camera.viewportHeight * 0.35f);
        window2.setPosition(
            (camera.viewportWidth - window2.getWidth()) / 2f,
            (camera.viewportHeight - window2.getHeight()) / 2f
        );
        fridge.pack();
        fridge.setWidth(window2.getWidth()*0.7f);
        Image slot = new Image(slotTexture);

        if (item != null) {
            Image itemImage = new Image(item.getTexture());
            ArrayList<ScrollPane> list = new ArrayList<>();
            Map<Salable, Integer> list2 = new HashMap<>();
            list.add(scrollPane);
            list2.put(item, count);
            addDrag(itemImage, stage, currentPlayer, item, list, false);
            itemImage.setSize(90, 90);
            Container<Label> labelContainer = getLabelContainer(list2, item);
            Stack stack = new Stack();
            stack.add(slot);
            stack.add(itemImage);
            stack.add(labelContainer);
            fridge.add(stack).size(96, 96);
        } else {
            fridge.add(slot).size(96, 96);
        }
        fridge.row();

        info.pack();
        info.add(new Label(item == null ? "Select an item!" : item.getName(), skin)).colspan(3).center().row();
        TextButton increment = new TextButton("+", skin), decrement = new TextButton("-", skin);
        info.add(increment).width(40).center();
        info.add(new TextButton(count.toString(), skin)).width(80).center();
        info.add(decrement).width(40).center().row();
        TextButton button = new TextButton("Send as gift to " + friend.getUser().getNickname(), skin);
        info.add(button).colspan(3).padTop(20).center().row();

        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        array.add(window2);
        array.add(trashCan);
        ImageButton exitButton = provideExitButton(array);

        decrement.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                if (--count <= 0) {
                    item = null;
                    count = 0;
                }
                endTask(array, exitButton);
                createInventory(skin, menuGroup, stage);
                return true;
            }
        });
        increment.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                if (item == null) return true;
                ++count;
                endTask(array, exitButton);
                createInventory(skin, menuGroup, stage);
                return true;
            }
        });
        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (item == null) {
                    getController().showResponse(new Response("Select an item to give!"));
                    return true;
                }
                Response resp = RelationService.getInstance().giveGift(
                    friend,
                    item, count
                );
                getController().showResponse(resp);
                item = null;
                count = 0;
                if (resp.shouldBeBack()) {
                    endTask(array, exitButton);
                    createInventory(skin, menuGroup, stage);
                }
                return resp.shouldBeBack();
            }
        });


        Table content = new Table();
        content.setFillParent(true);
        content.add(scrollPane).width(window.getWidth()).height(window.getHeight() - 100).padBottom(20).padTop(50).row();


        Table content2 = new Table();
        content2.setFillParent(true);
        content2.add(fridge).width(0.3f * window2.getWidth()).height(window.getHeight() - 100).padRight(70).padBottom(20).padTop(50);
        content2.add(info).width(0.7f * window2.getWidth()).height(window2.getHeight() - 100).padBottom(20).padTop(50).row();


        window.add(content).fill().pad(10);
        window2.add(content2).fill().pad(10);
        Group group = new Group() {
            @Override
            public void act(float delta) {
                window.setPosition(
                    (camera.viewportWidth - window.getWidth()) / 2f +
                        camera.position.x - camera.viewportWidth / 2,
                    (camera.viewportHeight - window.getHeight()) / 2f +
                        camera.position.y - camera.viewportHeight / 2 - 300
                );
                window2.setPosition(
                    (camera.viewportWidth - window.getWidth()) / 2f +
                        camera.position.x - camera.viewportWidth / 2,
                    (camera.viewportHeight - window.getHeight()) / 2f +
                        camera.position.y - camera.viewportHeight / 2 + 300
                );
                exitButton.setPosition(
                    window2.getX() + window2.getWidth() - exitButton.getWidth() / 2f + 16 - exitButton.getWidth(),
                    window2.getY() + window2.getHeight()
                );
                trashCan.setPosition(
                    window.getX() + window.getWidth() - trashCan.getWidth() / 2f + 50,
                    window.getY() + window.getHeight() - trashCan.getHeight() / 2f - 300
                );

                super.act(delta);
            }
        };
        group.addActor(window);
        group.addActor(window2);
        group.addActor(exitButton);
        group.addActor(trashCan);
        menuGroup.addActor(group);
    }

    private Farm currentFarm() {
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            if (farm.getPlayers().get(0).equals(App.getInstance().getCurrentGame().getCurrentPlayer())) {
                return farm;
            }
        }
        return null;
    }
}
