package io.github.some_example_name.view.mainMenu;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.controller.WorldController;
import io.github.some_example_name.model.Farm;
import io.github.some_example_name.model.Fridge;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.tools.BackPack;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.GameAsset;

import java.util.ArrayList;

public class FridgeMenu extends PopUp {
    private Fridge refrigerator;
    private Table inventory;
    private Table fridge;
    private Window window, window2;
    private Skin skin = GameAsset.SKIN;

    public void createMenu(Stage stage, Skin skin, WorldController worldController) {
        super.createMenu(stage, skin, worldController);
        Farm farm = currentFarm();
        if (farm == null) {
            worldController.showResponse(new Response("Enter your cottage to access the refrigerator."));
            return;
        }
        refrigerator = farm.getFridge();
        createInventory(skin, getMenuGroup(), stage);
    }

    @Override
    protected void handleDragRelease(InputEvent event, float x, float y, int pointer, Image itemImage, Salable item, Image dragImage, Boolean flag) {
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        if (isOverActor(dragImage, trashCan)) {
            if (flag) {
                currentPlayer.getInventory().deleteProductFromBackPack(item, currentPlayer, 1);
            } else {
                Farm farm = currentFarm();
                if (farm == null) {
                    getController().showResponse(new Response("Enter your cottage to access the refrigerator"));
                } else {
                    farm.getFridge().deleteProduct(item, farm.getFridge().countProduct(item));
                }
            }
        } else if (flag && isOverActor(dragImage, window2)) {
            Response resp = getGameService().cookingRefrigeratorPut(item.getName());
            if (!resp.shouldBeBack()) {
                getController().showResponse(resp);
            }
        } else if (!flag && isOverActor(dragImage, window)) {
            Response resp = getGameService().cookingRefrigeratorPick(item.getName());
            if (!resp.shouldBeBack()) {
                getController().showResponse(resp);
            }
        }

        dragImage.remove();
        createMenu(stage, skin, getController());

    }

    private void createInventory(Skin skin, Group menuGroup, Stage stage) {
        Farm currentFarm = currentFarm();
        if (currentFarm == null) {
            getController().showResponse(new Response("Enter a cottage to see it's refrigerator."));
            return;
        }
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        Texture slotTexture = GameAsset.BUTTON;
        OrthographicCamera camera = MainGradle.getInstance().getCamera();

        Image tab1 = new Image(GameAsset.INVENTORY_TAB);
        Image tab2 = new Image(GameAsset.REFRIGERATOR);
        tab1.rotateBy(90);
        tab2.rotateBy(90);

        window = new Window("", skin);
        window2 = new Window("", skin);
        window.setSize(camera.viewportWidth*0.7f, camera.viewportHeight*0.35f);
        window.setPosition(
            (camera.viewportWidth - window.getWidth()) / 2f,
            (camera.viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);

        trashCan = new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.WORM_BIN)));
        inventory = new Table();
        fridge = new Table();
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
            maxRow = (int)Math.ceil((double) backPack.getBackPackType().getCapacity() / maxCol);
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
                    addDrag(itemImage, stage, currentPlayer, item, list, true);
                    Container<Label> labelContainer = getLabelContainer(backPack.getProducts(), item);
                    labelContainer.setFillParent(false);
                    labelContainer.setSize(30, 20);
                    labelContainer.setPosition(66, 5);
                    itemImage.setSize(90, 90);
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

        window2.setSize(camera.viewportWidth*0.7f, camera.viewportHeight*0.35f);
        window2.setPosition(
            (camera.viewportWidth - window2.getWidth()) / 2f,
            (camera.viewportHeight - window2.getHeight()) / 2f
        );
        window2.setMovable(false);

        ScrollPane scrollPane2 = new ScrollPane(fridge, skin);
        fridge.pack();
        scrollPane2.setFadeScrollBars(false);
        scrollPane2.setScrollbarsOnTop(true);
        scrollPane2.setScrollingDisabled(false, false);
        scrollPane2.setScrollBarPositions(true, true);
        scrollPane2.setForceScroll(false, true);
        scrollPane2.layout();
        scrollPane2.setTouchable(Touchable.enabled);


        refrigerator = currentFarm.getFridge();
        maxRow = Math.max(5, refrigerator.getProducts().size() / maxCol + 1);


        for (int row = 0; row < maxRow; row++) {
            for (int col = 0; col < maxCol; col++) {
                Image slot = new Image(slotTexture);

                int index = row * maxCol + col;
                if (index < refrigerator.getProducts().size()) {
                    java.util.List<Salable> items = new ArrayList<>(refrigerator.getProducts().keySet());
                    Salable item = items.get(index);
                    Image itemImage = new Image(item.getTexture());
                    ArrayList<ScrollPane> list = new ArrayList<>();
                    list.add(scrollPane);
                    addDrag(itemImage, stage, currentPlayer, item, list,false);
                    itemImage.setSize(90, 90);
                    Container<Label> labelContainer = getLabelContainer(refrigerator.getProducts(), item);
                    Stack stack = new Stack();
                    stack.add(slot);
                    stack.add(itemImage);
                    stack.add(labelContainer);
                    fridge.add(stack).size(96, 96);
                } else {
                    fridge.add(slot).size(96, 96);
                }
            }
            fridge.row();
        }



        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        array.add(window2);
        array.add(trashCan);
        array.add(tab1);
        array.add(tab2);
        ImageButton exitButton = provideExitButton(array);


        Table content = new Table();
        content.setFillParent(true);
        content.add(scrollPane).width(window.getWidth()).height(window.getHeight() - 100).padBottom(20).padTop(50).row();


        Table content2 = new Table();
        content2.setFillParent(true);
        content2.add(scrollPane2).width(window2.getWidth()).height(window2.getHeight() - 100).padBottom(20).padTop(50).row();

        window.add(content).expand().fill().pad(10);
        window2.add(content2).expand().fill().pad(10);
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
                tab1.setPosition(
                    window.getX(),
                    window.getY() + window.getHeight() / 2f
                );
                tab2.setPosition(
                    window2.getX(),
                    window2.getY() + window2.getHeight() / 2f
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
        group.addActor(tab1);
        group.addActor(tab2);
        menuGroup.addActor(group);
    }

    private Farm currentFarm() {
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            if (farm.getTiles().contains(App.getInstance().getCurrentGame().getCurrentPlayer().getTiles().get(0))) {
                return farm;
            }
        }
        return null;
    }


}
