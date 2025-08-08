package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.client.controller.WorldController;
import io.github.some_example_name.common.model.Farm;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.craft.Craft;
import io.github.some_example_name.common.model.craft.CraftType;
import io.github.some_example_name.common.model.products.TreesAndFruitsAndSeeds.MadeProductType;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.source.Mineral;
import io.github.some_example_name.common.model.source.MineralType;
import io.github.some_example_name.common.model.tools.BackPack;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Map;

@Setter
public class ArtisanMenu extends PopUp {
    private Craft craft;
    private Window window, window2;

    public void createMenu(Stage stage, Skin skin, WorldController worldController) {
        super.createMenu(stage, skin, worldController);
        createInventory(skin, getMenuGroup(), stage);
    }

    @Override
    protected void handleDragRelease(InputEvent event, float x, float y, int pointer, Image itemImage, Salable item, Image dragImage, Boolean flag) {
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        if (isOverActor(dragImage, trashCan)) {
            if (flag) {
                currentPlayer.getInventory().deleteProductFromBackPack(item, currentPlayer, 1);
                GameClient.getInstance().updatePlayerDeleteFromInventory(currentPlayer,item,1);
            } else {
                Farm farm = currentFarm();
                if (farm == null) {
                    getController().showResponse(new Response("Enter your farm to use artisans"));
                } else {
                    farm.getFridge().deleteProduct(item, farm.getFridge().countProduct(item));
                }
            }
        } else if (flag && isOverActor(dragImage, window2)) {
            int count = currentPlayer.getInventory().countProductFromBackPack(item.getName());
            craft.getIngredients().put(item, count);
        } else if (!flag && isOverActor(dragImage, window)) {
            craft.getIngredients().remove(item);
        }
        createMenu(stage, skin, getController());

    }

    private void createInventory(Skin skin, Group menuGroup, Stage stage) {
        Farm currentFarm = currentFarm();
        if (currentFarm == null) {
            getController().showResponse(new Response("Enter your farm to artisan."));
            return;
        }
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
        ScrollPane scrollPane2 = new ScrollPane(info, skin);
        inventory.pack();
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollbarsOnTop(true);
        scrollPane.setScrollingDisabled(false, false);
        scrollPane.setScrollBarPositions(true, true);
        scrollPane.setForceScroll(false, true);
        scrollPane.layout();
        scrollPane.setTouchable(Touchable.enabled);
        scrollPane2.setFadeScrollBars(false);
        scrollPane2.setScrollbarsOnTop(true);
        scrollPane2.setScrollingDisabled(false, false);
        scrollPane2.setScrollBarPositions(true, true);
        scrollPane2.setForceScroll(true, true);
        scrollPane2.layout();
        scrollPane2.setTouchable(Touchable.enabled);

        int maxCol = 12;
        int maxRow;
        BackPack backPack = App.getInstance().getCurrentGame().getCurrentPlayer().getInventory();
        if (backPack.getBackPackType().getIsInfinite()) {
            synchronized (backPack.getProducts()){
                maxRow = Math.max(5, backPack.getProducts().size() / maxCol + 1);
            }
        } else {
            maxRow = (int) Math.ceil((double) backPack.getBackPackType().getCapacity() / maxCol);
        }

        for (int row = 0; row < maxRow; row++) {
            for (int col = 0; col < maxCol; col++) {
                Image slot = new Image(slotTexture);

                int index = row * maxCol + col;
                synchronized (backPack.getProducts()){
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

        maxCol = 2;
        maxRow = 2;


        for (int row = 0; row < maxRow; row++) {
            for (int col = 0; col < maxCol; col++) {
                Image slot = new Image(slotTexture);

                int index = row * maxCol + col;
                if (index < craft.getIngredients().size()) {
                    java.util.List<Salable> items = new ArrayList<>(craft.getIngredients().keySet());
                    Salable item = items.get(index);
                    Image itemImage = new Image(item.getTexture());
                    ArrayList<ScrollPane> list = new ArrayList<>();
                    list.add(scrollPane);
                    addDrag(itemImage, stage, currentPlayer, item, list, false);
                    itemImage.setSize(90, 90);
                    int count = craft.getIngredients().get(item);
                    Label countLabel = new Label(String.valueOf(count), skin);
                    countLabel.setFontScale(0.7f);
                    countLabel.setAlignment(Align.right);
                    countLabel.setColor(Color.GREEN);
                    Container<Label> labelContainer = new Container<>(countLabel);
                    labelContainer.setFillParent(false);
                    labelContainer.setSize(30, 20);
                    labelContainer.setPosition(96, 0);
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

        info.pack();
        info.add(new Image(craft.getTexture())).height(100).padRight(10);
        info.add(new Label(craft.getName(), skin)).left().colspan(8).row();
        info.add(new Label(craft.getCraftType().getDescription(), skin)).colspan(9).row();
        for (MadeProductType madeProduct : MadeProductType.findByCraft(craft.getCraftType())) {
            madeProduct.addInfo(info, skin);
        }
        TextButton button = new TextButton("Use artisan", skin);
        info.add(button).padTop(50).colspan(9).row();
        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Response resp;
                if (craft.getCraftType() == CraftType.BEE_HOUSE) {
                    resp = getGameService().artisanUse(craft, null, 0, null, 0);
                } else if (craft.getIngredients().isEmpty()) {
                    resp = new Response("This craft needs ingredients.");
                } else {
                    boolean flag = false;
                    Salable salable1 = null, salable2 = null;
                    int count1 = 0, count2 = 0;
                    int i = 0;
                    ArrayList<Map.Entry<Salable, Integer>> list = new ArrayList<>(craft.getIngredients().entrySet());
                    for (Map.Entry<Salable, Integer> entry : list) {
                        if (entry.getKey() instanceof Mineral) {
                            if (((Mineral)entry.getKey()).getForagingMineralType() == MineralType.COAL) {
                                flag = true;
                                if (i == 0) {
                                    salable2 = list.get(0).getKey();
                                    salable1 = list.get(1).getKey();
                                    count2 = list.get(0).getValue();
                                    count1 = list.get(1).getValue();
                                } else {
                                    salable1 = list.get(0).getKey();
                                    salable2 = list.get(i).getKey();
                                    count1 = list.get(0).getValue();
                                    count2 = list.get(i).getValue();
                                }
                                break;
                            }
                        }
                        i++;
                    }
                    if (!flag) {
                        salable1 = list.get(0).getKey();
                        count1 = list.get(0).getValue();
                        salable2 = null;
                    }
                    resp = getGameService().artisanUse(craft, salable1, count1, salable2, count2);
                }
                getController().showResponse(resp);
                return resp.shouldBeBack();
            }
        });

        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        array.add(window2);
        array.add(trashCan);
        ImageButton exitButton = provideExitButton(array);


        Table content = new Table();
        content.setFillParent(true);
        content.add(scrollPane).width(window.getWidth()).height(window.getHeight() - 100).padBottom(20).padTop(50).row();


        Table content2 = new Table();
        content2.setFillParent(true);
        info.padLeft(0.1f * window2.getWidth());
        scrollPane2.setWidth(0.4f * window2.getWidth());
        content2.add(fridge).width(0.3f * window2.getWidth()).height(window.getHeight() - 100).padRight(70).padBottom(20).padTop(50);
        content2.add(scrollPane2).width(0.7f * window2.getWidth()).height(window2.getHeight() - 100).padBottom(20).padTop(50).row();


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
