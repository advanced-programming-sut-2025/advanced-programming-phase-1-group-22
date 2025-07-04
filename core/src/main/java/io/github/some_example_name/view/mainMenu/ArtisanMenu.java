package io.github.some_example_name.view.mainMenu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.controller.PlayerController;
import io.github.some_example_name.controller.WorldController;
import io.github.some_example_name.model.Farm;
import io.github.some_example_name.model.Fridge;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.Tile;
import io.github.some_example_name.model.abilitiy.Ability;
import io.github.some_example_name.model.craft.Craft;
import io.github.some_example_name.model.craft.CraftType;
import io.github.some_example_name.model.products.TreesAndFruitsAndSeeds.MadeProductType;
import io.github.some_example_name.model.receipe.CookingRecipe;
import io.github.some_example_name.model.receipe.CraftingRecipe;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.source.Mineral;
import io.github.some_example_name.model.source.MineralType;
import io.github.some_example_name.model.structure.Structure;
import io.github.some_example_name.model.structure.farmInitialElements.Lake;
import io.github.some_example_name.model.tools.BackPack;
import io.github.some_example_name.service.GameService;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.GameAsset;
import io.github.some_example_name.view.MiniMapRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArtisanMenu {
    private static final Group menuGroup = new Group();
    private static final GameService gameService = new GameService();
    private static WorldController controller;
    private static Craft craft;

    public static void createMenu(Craft craft, Stage stage, Skin skin, WorldController worldController) {
        ArtisanMenu.craft = craft;
        controller = worldController;
        if (!stage.getActors().contains(menuGroup, true)) {
            stage.addActor(menuGroup);
        }
        menuGroup.clear();
        createInventory(skin, menuGroup, stage);
    }

    private static boolean isOverActor(Image item, Actor actor) {
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

    private static void addDrag(Image itemImage, Stage stage, Player currentPlayer, ImageButton trashCan, Salable item,
                                Window inventory, Window fridge, Skin skin, ScrollPane scrollPane, ScrollPane scrollPane2, boolean inBackPack) {
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
                scrollPane.setTouchable(Touchable.disabled);
                scrollPane2.setTouchable(Touchable.disabled);
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
                    if (isOverActor(dragImage, trashCan)) {
                        if (inBackPack) {
                            currentPlayer.getInventory().deleteProductFromBackPack(item, currentPlayer, 1);
                        } else {
                            Farm farm = currentFarm();
                            if (farm == null) {
                                controller.showResponse(new Response("Enter your farm to use artisans"));
                            } else {
                                farm.getFridge().deleteProduct(item, farm.getFridge().countProduct(item));
                            }
                        }
                    } else if (inBackPack && isOverActor(dragImage, fridge)) {
                        int count = currentPlayer.getInventory().countProductFromBackPack(item.getName());
                        craft.getIngredients().put(item, count);
//                        gameService.removeFromPlayerInventory(item.getName(), false);
                    } else if (!inBackPack && isOverActor(dragImage, inventory)) {
//                        currentPlayer.getInventory().addProductToBackPack(item, craft.getIngredients().get(item));
                        craft.getIngredients().remove(item);
                    }

                    dragImage.remove();
                    dragImage = null;
                    scrollPane.setTouchable(Touchable.enabled);
                    scrollPane2.setTouchable(Touchable.enabled);
                    createMenu(craft, stage, skin, controller);
                }
            }
        });
    }


    private static void createInventory(Skin skin, Group menuGroup, Stage stage) {
        Farm currentFarm = currentFarm();
        if (currentFarm == null) {
            controller.showResponse(new Response("Enter your farm to artisan."));
            return;
        }
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        Texture slotTexture = GameAsset.BUTTON;
        OrthographicCamera camera = MainGradle.getInstance().getCamera();

        Window window = new Window("", skin);
        Window window2 = new Window("", skin);
        window.setSize(camera.viewportWidth * 0.7f, camera.viewportHeight * 0.35f);
        window.setPosition(
            (camera.viewportWidth - window.getWidth()) / 2f,
            (camera.viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);

        ImageButton trashCan = new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.WORM_BIN)));
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
                    addDrag(itemImage, stage, currentPlayer, trashCan, item, window, window2, skin, scrollPane, scrollPane2, true);
                    itemImage.setSize(90, 90);
                    Stack stack = new Stack();
                    stack.add(slot);
                    stack.add(itemImage);
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
                    addDrag(itemImage, stage, currentPlayer, trashCan, item, window, window2, skin, scrollPane, scrollPane2, false);
                    itemImage.setSize(90, 90);
                    Stack stack = new Stack();
                    stack.add(slot);
                    stack.add(itemImage);
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
                    resp = gameService.artisanUse("bee_house", null, null);
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
                    resp = gameService.artisanUse(craft, salable1, count1, salable2, count2);
                }
                controller.showResponse(resp);
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

    private static ImageButton provideExitButton(ArrayList<Actor> array) {
        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.EXIT_BUTTON)));
        exitButton.setSize(32, 32);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (Actor o : array) {
                    o.remove();
                }
                exitButton.remove();
            }
        });
        return exitButton;
    }

    private static Farm currentFarm() {
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            if (farm.getPlayers().get(0).equals(App.getInstance().getCurrentGame().getCurrentPlayer())) {
                return farm;
            }
        }
        return null;
    }
}
