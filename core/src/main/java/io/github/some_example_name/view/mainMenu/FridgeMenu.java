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
import io.github.some_example_name.model.receipe.CookingRecipe;
import io.github.some_example_name.model.receipe.CraftingRecipe;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.structure.Structure;
import io.github.some_example_name.model.structure.farmInitialElements.Lake;
import io.github.some_example_name.model.tools.BackPack;
import io.github.some_example_name.service.GameService;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.GameAsset;
import io.github.some_example_name.view.MiniMapRenderer;

import java.util.ArrayList;
import java.util.Map;

public class FridgeMenu {
    private static final Group menuGroup = new Group();
    private static final GameService gameService = new GameService();
    private static WorldController controller;

    public static void createMenu(Stage stage, Skin skin, WorldController worldController) {
        controller = worldController;
        if (!stage.getActors().contains(menuGroup, true)) {
            stage.addActor(menuGroup);
        }
        menuGroup.clear();
        createInventory(skin, menuGroup,stage);
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
                                Window inventory, Window fridge, Skin skin, ScrollPane scrollPane, boolean inBackPack) {
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
                                controller.showResponse(new Response("Enter your cottage to access the refrigerator"));
                            } else {
                                farm.getFridge().deleteProduct(item, farm.getFridge().countProduct(item));
                            }
                        }
                    } else if (inBackPack && isOverActor(dragImage, fridge)) {
                        Response resp = gameService.cookingRefrigeratorPut(item.getName());
                        if (!resp.shouldBeBack()) {
                            controller.showResponse(resp);
                        }
                    } else if (!inBackPack && isOverActor(dragImage, inventory)) {
                        Response resp = gameService.cookingRefrigeratorPick(item.getName());
                        if (!resp.shouldBeBack()) {
                            controller.showResponse(resp);
                        }
                    }

                    dragImage.remove();
                    dragImage = null;
                    scrollPane.setTouchable(Touchable.enabled);
                    createMenu(stage, skin, controller);
                }
            }
        });
    }


    private static void createInventory(Skin skin, Group menuGroup,Stage stage) {
        Farm currentFarm = currentFarm();
        if (currentFarm == null) {
            controller.showResponse(new Response("Enter a cottage to see it's refrigerator."));
            return;
        }
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        Texture slotTexture = GameAsset.BUTTON;
        OrthographicCamera camera = MainGradle.getInstance().getCamera();

        Image tab1 = new Image(GameAsset.INVENTORY_TAB);
        Image tab2 = new Image(GameAsset.REFRIGERATOR);
        tab1.rotateBy(90);
        tab2.rotateBy(90);

        Window window = new Window("", skin);
        Window window2 = new Window("", skin);
        window.setSize(camera.viewportWidth*0.7f, camera.viewportHeight*0.35f);
        window.setPosition(
            (camera.viewportWidth - window.getWidth()) / 2f,
            (camera.viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);

        ImageButton trashCan = new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.WORM_BIN)));
        Table inventory = new Table();
        Table fridge = new Table();
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
                    addDrag(itemImage, stage, currentPlayer, trashCan, item, window, window2, skin, scrollPane, true);
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


        Fridge refrigerator = currentFarm.getFridge();
        maxRow = Math.max(5, refrigerator.getProducts().size() / maxCol + 1);


        for (int row = 0; row < maxRow; row++) {
            for (int col = 0; col < maxCol; col++) {
                Image slot = new Image(slotTexture);

                int index = row * maxCol + col;
                if (index < refrigerator.getProducts().size()) {
                    java.util.List<Salable> items = new ArrayList<>(refrigerator.getProducts().keySet());
                    Salable item = items.get(index);
                    Image itemImage = new Image(item.getTexture());
                    addDrag(itemImage, stage, currentPlayer, trashCan, item, window, window2, skin, scrollPane,false);
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
