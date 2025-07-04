package io.github.some_example_name.view.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.controller.PlayerController;
import io.github.some_example_name.model.Farm;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.Tile;
import io.github.some_example_name.model.abilitiy.Ability;
import io.github.some_example_name.model.cook.FoodType;
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

public class InventoryMenu {
    private static final Group menuGroup = new Group();
    private static final Table tabs = new Table();
    private static Integer selectedIndex;
    private static final GameService gameService = new GameService();
    private static PlayerController controller;

    public static void createMenu(Stage stage, Skin skin, PlayerController playerController, int tabIndex) {
        controller = playerController;
        if (!stage.getActors().contains(menuGroup, true)) {
            stage.addActor(menuGroup);
        }
        menuGroup.clear();
        tabs.clear();

        tabs.top().left();
        tabs.defaults().size(80, 80).padRight(4);
        Image tab1 = new Image(GameAsset.INVENTORY_TAB);
        tab1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuGroup.clear();
                createInventory(skin, tabs, menuGroup,stage);
            }
        });
        Image tab2 = new Image(GameAsset.SKILL_TAB);
        tab2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuGroup.clear();
                createSkillMenu(skin,menuGroup,tabs);
            }
        });
        Image tab3 = new Image(GameAsset.SOCIAL_TAB);
        tab3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createSocialMenu();
            }
        });
        Image tab4 = new Image(GameAsset.MAP_TAB);
        tab4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuGroup.clear();
                createMapMenu(skin,menuGroup,tabs);
            }
        });
        Image tab5 = new Image(GameAsset.CRAFTING_TAB);
        tab5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuGroup.clear();
                createCraftingMenu(skin, tabs, menuGroup, stage);            }
        });
        Image tab6 = new Image(GameAsset.COOKING_TAB);
        tab6.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuGroup.clear();
                createCookingMenu(skin, tabs, menuGroup, stage);            }
        });

        tabs.add(tab1);
        tabs.add(tab2);
        tabs.add(tab3);
        tabs.add(tab4);
        tabs.add(tab5);
        tabs.add(tab6);

        switch (tabIndex) {
            case 0 -> createInventory(skin, tabs, menuGroup, stage);
            case 1 -> createSkillMenu(skin,menuGroup,tabs);
            case 2 -> createSkillMenu(skin,menuGroup,tabs);
            case 3 -> createMapMenu(skin,menuGroup,tabs);
            case 4 -> createCraftingMenu(skin, tabs, menuGroup, stage);
            case 5 -> createCookingMenu(skin, tabs, menuGroup, stage);
        }
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

    private static void refreshInventory(Stage stage, Table inventory, Player player, Texture slotTexture, Player currentPlayer, ImageButton trashCan, ScrollPane scrollPane) {
        inventory.clear();
        java.util.List<Salable> items = new ArrayList<>(player.getInventory().getProducts().keySet());

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                Image slot = new Image(slotTexture);
                int index = row * 9 + col;

                if (index < items.size()) {
                    Salable item = items.get(index);
                    Image itemImage = new Image(item.getTexture());
                    itemImage.setSize(90, 90);
                    addDrag(itemImage, stage, currentPlayer, trashCan, item, inventory, slotTexture, scrollPane);
                    itemImage.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            if (getTapCount() == 2) {
                                currentPlayer.setCurrentCarrying(item);
                                refreshInventory(stage, inventory, currentPlayer, slotTexture, currentPlayer, trashCan, scrollPane);
                            }
                        }
                    });
                    int count = currentPlayer.getInventory().getProducts().get(item);
                    Label countLabel = new Label(String.valueOf(count), GameAsset.SKIN);
                    countLabel.setFontScale(0.7f);
                    countLabel.setAlignment(Align.right);
                    countLabel.setColor(Color.GREEN);
                    Container<Label> labelContainer = new Container<>(countLabel);
                    labelContainer.setFillParent(false);
                    labelContainer.setSize(30, 20);
                    labelContainer.setPosition(66, 5);
                    if (item == currentPlayer.getCurrentCarrying()) itemImage.setColor(1, 1, 1, 1f);
                    else itemImage.setColor(1, 1, 1, 0.5f);
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
    }

    private static void addDrag(Image itemImage, Stage stage, Player currentPlayer, ImageButton trashCan, Salable item, Table inventory, Texture slotTexture, ScrollPane scrollPane) {
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
                    if (isOverTrashCan(dragImage, trashCan)) {
                        currentPlayer.getInventory().deleteProductFromBackPack(item, currentPlayer, 1);
                    }

                    dragImage.remove();
                    dragImage = null;
                    scrollPane.setTouchable(Touchable.enabled);
                    refreshInventory(stage, inventory, currentPlayer, slotTexture, currentPlayer, trashCan, scrollPane);
                    scrollPane.layout();
                }
            }
        });
    }

    private static void createInventory(Skin skin, Table tabs, Group menuGroup,Stage stage) {
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        Texture slotTexture = GameAsset.BUTTON;
        Window window = new Window("", skin);
        window.setSize(700, 500);
        window.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);

        ImageButton trashCan = new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.WORM_BIN)));
        Table inventory = new Table();
        ScrollPane scrollPane = new ScrollPane(inventory, skin);
        inventory.pack();
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollbarsOnTop(true);
        scrollPane.setScrollingDisabled(false, false);
        scrollPane.setScrollBarPositions(true, true);
        scrollPane.setForceScroll(false, true);
        scrollPane.layout();
        scrollPane.setTouchable(Touchable.enabled);

        int maxCol = 9;
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

                int index = row * 9 + col;
                if (index < currentPlayer.getInventory().getProducts().size()) {
                    java.util.List<Salable> items = new ArrayList<>(currentPlayer.getInventory().getProducts().keySet());
                    Salable item = items.get(index);
                    Image itemImage = new Image(item.getTexture());
                    addDrag(itemImage, stage, currentPlayer, trashCan, item, inventory, slotTexture, scrollPane);
                    itemImage.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            if (getTapCount() == 2) {
                                currentPlayer.setCurrentCarrying(item);
                                refreshInventory(stage, inventory, currentPlayer, slotTexture, currentPlayer, trashCan, scrollPane);
                            }
                        }
                    });
                    int count = currentPlayer.getInventory().getProducts().get(item);
                    Label countLabel = new Label(String.valueOf(count), skin);
                    countLabel.setFontScale(0.7f);
                    countLabel.setAlignment(Align.right);
                    countLabel.setColor(Color.GREEN);
                    Container<Label> labelContainer = new Container<>(countLabel);
                    labelContainer.setFillParent(false);
                    labelContainer.setSize(30, 20);
                    labelContainer.setPosition(66, 5);
                    if (item == currentPlayer.getCurrentCarrying()) itemImage.setColor(1, 1, 1, 1f);
                    else itemImage.setColor(1, 1, 1, 0.5f);
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
        Table finances = new Table();
        finances.left();
        finances.add(new Label(App.getInstance().getCurrentGame().getCurrentPlayer().getFarmType().getName(), skin)).row();
        finances.add(new Label("Current Funds: " + App.getInstance().getCurrentGame().getCurrentPlayer().getAccount().getGolds() + "g", skin)).row();
        finances.add(new Label("Current earning: " + App.getInstance().getCurrentGame().getCurrentPlayer().getAccount().getEarned() + "g", skin)).row();

        ArrayList<Table> array = new ArrayList<>();
        array.add(window);
        array.add(tabs);
        array.add(trashCan);
        ImageButton exitButton = provideExitButton(array);


        Table content = new Table();
        content.setFillParent(true);
        content.add(scrollPane).width(600).height(220).padBottom(20).padTop(50).row();
        content.add(finances).padTop(60).top().row();

        window.add(content).expand().fill().pad(10);
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
        menuGroup.addActor(group);
    }

    private static void createCraftingMenu(Skin skin, Table tabs, Group menuGroup,Stage stage) {
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        Window window = new Window("", skin);
        window.setSize(camera.viewportWidth * 0.7f, camera.viewportHeight * 0.5f);
        window.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);


        Table craftingRecipes = new Table();
        craftingRecipes.pack();
        java.util.List<Map.Entry<CraftingRecipe, Boolean>> recipes = App.getInstance().getCurrentGame().
            getCurrentPlayer().getCraftingRecipes().entrySet().stream().toList();
        int i = 0;
        for (Map.Entry<CraftingRecipe, Boolean> entry : recipes) {
            int row = i / 7, col = i % 7;
            Image itemImage = new Image(entry.getKey().getTexture());
            int finalI = i;
            itemImage.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (button == Input.Buttons.RIGHT) {
                        selectedIndex = finalI;
                        menuGroup.clear();
                        createCraftingMenu(skin, tabs, menuGroup, stage);
                        return true;
                    }
                    if (button == Input.Buttons.LEFT) {
                        Response resp = gameService.craftingCraft(entry.getKey().getCraft().getName());
                        if (!resp.shouldBeBack()) {
                            controller.getWorldController().showResponse(resp);
                        }
                        return true;
                    }
                    return false;
                }
            });
            itemImage.setSize(90, 90);
            if (!entry.getValue()) {
                itemImage.setColor(0.3f, 0.3f, 0.3f, 0.6f);
            }
            craftingRecipes.add(itemImage);
            craftingRecipes.add().expandX();
            if (col == 6) craftingRecipes.row().expandY();
            i++;
        }

        ArrayList<Table> array = new ArrayList<>();
        array.add(window);
        array.add(tabs);
        ImageButton exitButton = provideExitButton(array);


        Table info = new Table();
        info.left();
        if (selectedIndex == null) {
            Label label = new Label("Right Click An Item To See Info", skin);
            info.add(label).expandX().fillX().row();
        } else {
            Map.Entry<CraftingRecipe, Boolean> recipe = recipes.get(selectedIndex);
            Label name = new Label(recipe.getKey().getName(), skin);
            Label type = new Label("Crafting Recipe", skin);
            info.add(name).colspan(3).expandX().fillX().row();
            info.add(type).colspan(3).expandX().fillX().row();
            info.row();
            if (!recipe.getValue()) {
                Label noInfo = new Label("You've not learnt this recipe", skin);
                info.add(noInfo).expandX().fillX().row();
            } else {
                Label ingredients = new Label("Ingredients", skin);
                info.add(ingredients).colspan(3).expandX().fillX().row();
                recipe.getKey().addInfo(info, skin);
            }
        }



        Table content = new Table();
        content.setFillParent(true);
        content.add(craftingRecipes).width(window.getWidth()*0.6f).height(window.getHeight()*0.8f).padBottom(20).padTop(50);
        content.add(info).width(window.getWidth()*0.3f).height(window.getHeight()*0.8f).padBottom(20).padTop(50).row();

        window.add(content).expand().fill().pad(10);
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
                tabs.setPosition(
                    window.getX(),
                    window.getY() + window.getHeight() - tabs.getHeight() / 2f + 70
                );

                super.act(delta);
            }
        };
        group.addActor(window);
        group.addActor(exitButton);
        group.addActor(tabs);
        menuGroup.addActor(group);
    }

    private static void createCookingMenu(Skin skin, Table tabs, Group menuGroup,Stage stage) {
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        Window window = new Window("", skin);
        window.setSize(camera.viewportWidth * 0.7f, camera.viewportHeight * 0.5f);
        window.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);


        Table cookingRecipes = new Table();
        cookingRecipes.pack();
        java.util.List<Map.Entry<CookingRecipe, Boolean>> recipes = App.getInstance().getCurrentGame().
            getCurrentPlayer().getCookingRecipes().entrySet().stream().toList();
        int i = 0;
        for (Map.Entry<CookingRecipe, Boolean> entry : recipes) {
            int row = i / 7, col = i % 7;
            Image itemImage = new Image(entry.getKey().getTexture());
            int finalI = i;
            itemImage.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (button == Input.Buttons.RIGHT) {
                        selectedIndex = finalI;
                        menuGroup.clear();
                        createCookingMenu(skin, tabs, menuGroup, stage);
                        return true;
                    }
                    if (button == Input.Buttons.LEFT) {
                        Response resp = gameService.cookingPrepare(entry.getKey().getName().replace(" recipe", ""));
                        if (!resp.shouldBeBack()) {
                            controller.getWorldController().showResponse(resp);
                        }
                        return true;
                    }
                    return false;
                }
            });
            itemImage.setSize(130, 130);
            if (!entry.getValue()) {
                itemImage.setColor(0.3f, 0.3f, 0.3f, 0.6f);
            }
            cookingRecipes.add(itemImage);
            cookingRecipes.add().expandX();
            if (col == 6) cookingRecipes.row().expandY();
            i++;
        }

        ArrayList<Table> array = new ArrayList<>();
        array.add(window);
        array.add(tabs);
        ImageButton exitButton = provideExitButton(array);


        Table info = new Table();
        info.left();
        if (selectedIndex == null) {
            Label label = new Label("Right Click An Item To See Info", skin);
            info.add(label).expandX().fillX().row();
        } else {
            Map.Entry<CookingRecipe, Boolean> recipe = recipes.get(selectedIndex);
            Label name = new Label(recipe.getKey().getName(), skin);
            Label type = new Label("Cooking Recipe", skin);
            info.add(name).colspan(3).expandX().fillX().row();
            info.add(type).colspan(3).expandX().fillX().row();
            info.row();
            if (!recipe.getValue()) {
                Label noInfo = new Label("You've not learnt this recipe", skin);
                info.add(noInfo).expandX().fillX().row();
            } else {
                Label ingredients = new Label("Ingredients", skin);
                info.add(ingredients).colspan(3).expandX().fillX().row();
                recipe.getKey().addInfo(info, skin);
            }
        }



        Table content = new Table();
        content.setFillParent(true);
        content.add(cookingRecipes).width(window.getWidth()*0.6f).height(window.getHeight()*0.8f).padBottom(20).padTop(50);
        content.add(info).width(window.getWidth()*0.3f).height(window.getHeight()*0.8f).padBottom(20).padTop(50).row();

        window.add(content).expand().fill().pad(10);
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
                tabs.setPosition(
                    window.getX(),
                    window.getY() + window.getHeight() - tabs.getHeight() / 2f + 70
                );

                super.act(delta);
            }
        };
        group.addActor(window);
        group.addActor(exitButton);
        group.addActor(tabs);
        menuGroup.addActor(group);
    }

    private static ImageButton provideExitButton(ArrayList<Table> array) {
        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.EXIT_BUTTON)));
        exitButton.setSize(32, 32);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (Table o : array) {
                    o.remove();
                }
                exitButton.remove();
                selectedIndex = null;
            }
        });
        return exitButton;
    }

    private static void createSkillMenu(Skin skin,Group menuGroup,Table tabs) {
        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
        Window window = new Window("Skills", skin);
        window.setSize(900, 600);
        window.setMovable(false);
        window.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f
        );

        Table skillTable = new Table();
        skillTable.defaults().pad(10).left();

        for (Map.Entry<Ability, Integer> abilityIntegerEntry : player.getAbilities().entrySet()) {
            TextureRegionDrawable icon = new TextureRegionDrawable(abilityIntegerEntry.getKey().getTexture());

            Image iconImage = new Image(icon);
            Label nameLabel = new Label(abilityIntegerEntry.getKey().getName(), skin);
            final Label tooltipLabel = new Label(abilityIntegerEntry.getKey().getDescription(), skin);
            tooltipLabel.setVisible(false);
            tooltipLabel.setColor(1, 1, 1, 0.9f);
            tooltipLabel.setZIndex(1000);
            menuGroup.addActor(tooltipLabel);
            iconImage.addListener(new InputListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    tooltipLabel.setText(abilityIntegerEntry.getKey().getDescription());
                    tooltipLabel.pack();
                    Vector2 stageCoords = iconImage.localToStageCoordinates(new Vector2(iconImage.getWidth(), iconImage.getHeight()));
                    tooltipLabel.setPosition(stageCoords.x, stageCoords.y);
                    tooltipLabel.setVisible(true);
                    tooltipLabel.addAction(Actions.alpha(1f));
                    tooltipLabel.toFront();
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    tooltipLabel.addAction(Actions.sequence(
                        Actions.fadeOut(0.2f),
                        Actions.visible(false)
                    ));
                }
            });

            float currentXP = abilityIntegerEntry.getValue();
            float maxXP = Math.min(player.getAbilityLevel(abilityIntegerEntry.getKey()) + 1,4) * 100 + 50;
            int level = player.getAbilityLevel(abilityIntegerEntry.getKey());

            ProgressBar xpBar = new ProgressBar(0, maxXP, 1, false, skin);
            xpBar.setValue(currentXP);
            xpBar.setAnimateDuration(0.2f);
            xpBar.setWidth(300);

            Label levelLabel = new Label("Level: " + level, skin);

            Table row = new Table();
            row.defaults().pad(10).align(Align.left);
            row.add(iconImage).size(64).padRight(20);
            row.add(nameLabel).width(100).padRight(20);
            row.add(xpBar).width(300).padRight(20);
            row.add(levelLabel).width(80).align(Align.right);

            skillTable.add(row).row();
        }

        Image iconHeart = new Image(GameAsset.SECRET_HEART);
        Label heart = new Label("energy",skin);
        ProgressBar xpBar = new ProgressBar(0, player.getMaxEnergy(), 1, false, skin);
        xpBar.setValue(player.getEnergy());
        xpBar.setAnimateDuration(0.2f);
        xpBar.setWidth(300);
        Label infinite = new Label("",skin);
        if (player.getEnergyIsInfinite()) infinite.setText("infinite");
        Table row = new Table();
        row.defaults().pad(10).align(Align.left);
        row.add(iconHeart).size(64).padRight(20);
        row.add(heart).width(100).padRight(20);
        row.add(xpBar).width(300).padRight(20);
        row.add(infinite).width(80).align(Align.right);

        skillTable.add(row).row();

        ArrayList<Table> array = new ArrayList<>();
        array.add(window);
        array.add(tabs);
        ImageButton exitButton = provideExitButton(array);

        Table content = new Table();
        content.setFillParent(true);
        content.add(skillTable).expand().fill().pad(20);

        window.add(content).expand().fill();
        window.top();

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
                tabs.setPosition(
                    window.getX(),
                    window.getY() + window.getHeight() - tabs.getHeight() / 2f + 70
                );
                super.act(delta);
            }
        };

        group.addActor(window);
        group.addActor(exitButton);
        group.addActor(tabs);
        menuGroup.addActor(group);
    }

    private static void createSocialMenu() {

    }

    private static void createMapMenu(Skin skin,Group menuGroup,Table tabs) {
        float scale = 0.03125f;
        int minimapWidth = (int) (160 * 160 * scale);
        int minimapHeight = (int) (120 * 160 * scale);

        MiniMapRenderer miniMap = new MiniMapRenderer(minimapWidth, minimapHeight);

        miniMap.render(() -> {
            drawMiniTiles(miniMap.getBatch(), scale);
            drawMiniStructures(miniMap.getBatch(), scale);
        });

        Window window = new Window("Mini Map", skin);
        window.setSize(900, 600);
        window.setMovable(false);
        window.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f
        );

        ArrayList<Table> array = new ArrayList<>();
        array.add(window);
        array.add(tabs);
        ImageButton exitButton = provideExitButton(array);

        Image mapImage = new Image(miniMap.getTexture());
        mapImage.setSize(minimapWidth, minimapHeight);


        Table content = new Table();
        content.setFillParent(true);
        content.add(mapImage).center().expand();
        window.add(content).expand().fill();

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
                tabs.setPosition(
                    window.getX(),
                    window.getY() + window.getHeight() - tabs.getHeight() / 2f + 70
                );

                super.act(delta);
            }
        };
        group.addActor(window);
        group.addActor(exitButton);
        group.addActor(tabs);
        menuGroup.addActor(group);
    }

    private static void drawMiniTiles(Batch batch, float scale) {
        Tile[][] tiles = App.getInstance().getCurrentGame().tiles;

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                Texture tex = tiles[i][j].getTileType().getTexture();
                if (tex != null) {
                    batch.draw(tex,
                        i * App.tileWidth * scale,
                        j * App.tileHeight * scale,
                        App.tileWidth * scale,
                        App.tileHeight * scale);
                }
            }
        }
    }

    private static void drawMiniStructures(Batch batch, float scale) {
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            for (Structure structure : farm.getStructures()) {
                if (structure.getSprite() != null) {
                    if (structure instanceof Lake) {
                        for (Tile tile : structure.getTiles()) {
                            Sprite sprite = new Sprite(structure.getSprite());
                            sprite.setSize(App.tileWidth * scale, App.tileHeight * scale);
                            sprite.setPosition(tile.getX() * App.tileWidth * scale,
                                tile.getY() * App.tileHeight * scale);
                            sprite.draw(batch);
                        }
                    } else {
                        Sprite sprite = new Sprite(structure.getSprite());
                        sprite.setSize(App.tileWidth * scale, App.tileHeight * scale);
                        sprite.setPosition(structure.getTiles().get(0).getX() * App.tileWidth * scale,
                            structure.getTiles().get(0).getY() * App.tileHeight * scale);
                        sprite.draw(batch);
                    }
                }
            }
        }
    }
}
