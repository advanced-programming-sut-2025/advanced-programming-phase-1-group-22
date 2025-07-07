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
import io.github.some_example_name.controller.WorldController;
import io.github.some_example_name.model.Farm;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.Tile;
import io.github.some_example_name.model.abilitiy.Ability;
import io.github.some_example_name.model.cook.FoodType;
import io.github.some_example_name.model.receipe.CookingRecipe;
import io.github.some_example_name.model.receipe.CraftingRecipe;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.model.relations.Friendship;
import io.github.some_example_name.model.relations.NPC;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.structure.Structure;
import io.github.some_example_name.model.structure.farmInitialElements.Lake;
import io.github.some_example_name.model.tools.BackPack;
import io.github.some_example_name.service.GameService;
import io.github.some_example_name.service.RelationService;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.GameAsset;
import io.github.some_example_name.view.MiniMapActor;
import io.github.some_example_name.view.MiniMapRenderer;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Map;

@Setter
public class InventoryMenu extends PopUp {
    private Integer selectedIndex;
    private Integer tabIndex = 0;
    private Table inventory;
    private ScrollPane scrollPane;
    private ArrayList<ImageButton> gifts = new ArrayList<>();
    private ArrayList<ImageButton> histories = new ArrayList<>();
    private ArrayList<ImageButton> chats = new ArrayList<>();
    private ArrayList<Friendship> friendships = new ArrayList<>();
    {
        gifts.add(new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.GIFT))));
        histories.add(new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.CHEST))));
        chats.add(new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.CHAT))));
    }
    private static final Table tabs = new Table();

    public void createMenu(Stage stage, Skin skin, WorldController worldController) {
        super.createMenu(stage, skin, worldController);
        tabs.clear();
        tabs.top().left();
        tabs.defaults().size(80, 80).padRight(4);
        Image tab1 = new Image(GameAsset.INVENTORY_TAB);
        tab1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getMenuGroup().clear();
                createInventory(skin, tabs, getMenuGroup(), stage);
            }
        });
        Image tab2 = new Image(GameAsset.SKILL_TAB);
        tab2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getMenuGroup().clear();
                createSkillMenu(skin, getMenuGroup(), tabs);
            }
        });
        Image tab3 = new Image(GameAsset.SOCIAL_TAB);
        tab3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getMenuGroup().clear();
                createSocialMenu(skin, getMenuGroup(), tabs);
            }
        });
        Image tab4 = new Image(GameAsset.MAP_TAB);
        tab4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getMenuGroup().clear();
                createMapMenu(skin, getMenuGroup(), tabs);
            }
        });
        Image tab5 = new Image(GameAsset.CRAFTING_TAB);
        tab5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getMenuGroup().clear();
                createCraftingMenu(skin, tabs, getMenuGroup(), stage);
            }
        });
        Image tab6 = new Image(GameAsset.COOKING_TAB);
        tab6.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getMenuGroup().clear();
                createCookingMenu(skin, tabs, getMenuGroup(), stage);
            }
        });

        tabs.add(tab1);
        tabs.add(tab2);
        tabs.add(tab3);
        tabs.add(tab4);
        tabs.add(tab5);
        tabs.add(tab6);

        switch (tabIndex) {
            case 0 -> createInventory(skin, tabs, getMenuGroup(), stage);
            case 1 -> createSkillMenu(skin, getMenuGroup(), tabs);
            case 2 -> createSocialMenu(skin, getMenuGroup(), tabs);
            case 3 -> createMapMenu(skin, getMenuGroup(), tabs);
            case 4 -> createCraftingMenu(skin, tabs, getMenuGroup(), stage);
            case 5 -> createCookingMenu(skin, tabs, getMenuGroup(), stage);
        }
    }

    private void refreshInventory(Stage stage, Table inventory, Player player, Texture slotTexture, Player currentPlayer, ImageButton trashCan, ScrollPane scrollPane) {
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
                    ArrayList<ScrollPane> list = new ArrayList<>();
                    list.add(scrollPane);
                    addDrag(itemImage, stage, currentPlayer, item, list, true);
                    itemImage.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            if (getTapCount() == 2) {
                                currentPlayer.setCurrentCarrying(item);
                                refreshInventory(stage, inventory, currentPlayer, slotTexture, currentPlayer, trashCan, scrollPane);
                            }
                        }
                    });
                    Container<Label> labelContainer = getLabelContainer(currentPlayer.getInventory().getProducts(), item);
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

    @Override
    protected void handleDragRelease(InputEvent event, float x, float y, int pointer, Image itemImage, Salable item, Image dragImage, Boolean flag) {
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        if (isOverActor(dragImage, trashCan)) {
            currentPlayer.getInventory().deleteProductFromBackPack(item, currentPlayer, 1);
        }

        dragImage.remove();
        refreshInventory(stage, inventory, currentPlayer, slotTexture, currentPlayer, trashCan, scrollPane);
    }

    private void createInventory(Skin skin, Table tabs, Group menuGroup, Stage stage) {
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        Window window = new Window("", skin);
        window.setSize(700, 500);
        window.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);

        if (currentPlayer.getCurrentTrashCan() != null) {
            trashCan = new ImageButton(new TextureRegionDrawable(currentPlayer.getCurrentTrashCan().getTexture()));
        } else {
            trashCan = new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.WORM_BIN)));
        }
        inventory = new Table();
        scrollPane = new ScrollPane(inventory, skin);
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
            maxRow = (int) Math.ceil((double) backPack.getBackPackType().getCapacity() / maxCol);
        }

        for (int row = 0; row < maxRow; row++) {
            for (int col = 0; col < maxCol; col++) {
                Image slot = new Image(slotTexture);

                int index = row * 9 + col;
                if (index < currentPlayer.getInventory().getProducts().size()) {
                    java.util.List<Salable> items = new ArrayList<>(currentPlayer.getInventory().getProducts().keySet());
                    Salable item = items.get(index);
                    Image itemImage = new Image(item.getTexture());
                    ArrayList<ScrollPane> list = new ArrayList<>();
                    list.add(scrollPane);
                    addDrag(itemImage, stage, currentPlayer, item, list, true);
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

        ArrayList<Actor> array = new ArrayList<>();
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

    private void createCraftingMenu(Skin skin, Table tabs, Group menuGroup,Stage stage) {
        getGameService().updateRecipes();
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
                        Response resp = getGameService().craftingCraft(entry.getKey().getCraft().getName());
                        if (!resp.shouldBeBack()) {
                            getController().showResponse(resp);
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

        ArrayList<Actor> array = new ArrayList<>();
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
        content.add(craftingRecipes).width(window.getWidth() * 0.6f).height(window.getHeight() * 0.8f).padBottom(20).padTop(50);
        content.add(info).width(window.getWidth() * 0.3f).height(window.getHeight() * 0.8f).padBottom(20).padTop(50).row();

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

    private void createCookingMenu(Skin skin, Table tabs, Group menuGroup,Stage stage) {
        getGameService().updateRecipes();
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
                        Response resp = getGameService().cookingPrepare(entry.getKey().getName().replace(" recipe", ""));
                        if (!resp.shouldBeBack()) {
                            getController().showResponse(resp);
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

        ArrayList<Actor> array = new ArrayList<>();
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
        content.add(cookingRecipes).width(window.getWidth() * 0.6f).height(window.getHeight() * 0.8f).padBottom(20).padTop(50);
        content.add(info).width(window.getWidth() * 0.3f).height(window.getHeight() * 0.8f).padBottom(20).padTop(50).row();

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

    @Override
    protected void close() {
        selectedIndex = null;
        super.close();
    }

    private void createSkillMenu(Skin skin, Group menuGroup, Table tabs) {
        friendships.clear();
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
            float maxXP = Math.min(player.getAbilityLevel(abilityIntegerEntry.getKey()) + 1, 4) * 100 + 50;
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
        Label heart = new Label("energy", skin);
        ProgressBar xpBar = new ProgressBar(0, player.getMaxEnergy(), 1, false, skin);
        xpBar.setValue(player.getEnergy());
        xpBar.setAnimateDuration(0.2f);
        xpBar.setWidth(300);
        Label infinite = new Label("", skin);
        if (player.getEnergyIsInfinite()) infinite.setText("infinite");
        Table row = new Table();
        row.defaults().pad(10).align(Align.left);
        row.add(iconHeart).size(64).padRight(20);
        row.add(heart).width(100).padRight(20);
        row.add(xpBar).width(300).padRight(20);
        row.add(infinite).width(80).align(Align.right);

        skillTable.add(row).row();

        ArrayList<Actor> array = new ArrayList<>();
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

    private void createSocialMenu(Skin skin, Group menuGroup, Table tabs) {
        friendships.clear();
        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        Window window = new Window("Social", skin);
        window.setSize(camera.viewportWidth * 0.7f, camera.viewportHeight * 0.5f);
        window.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);

        Table table = new Table();
        table.defaults().pad(10).left();


        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        array.add(tabs);
        ImageButton exitButton = provideExitButton(array);

        int i = -1;
        for (Friendship friendship : App.getInstance().getCurrentGame().getFriendships()) {
            Player friend;
            if (friendship.getSecondPlayer() instanceof NPC || friendship.getFirstPlayer() instanceof NPC) {
                continue;
            } else if (friendship.getSecondPlayer() == player) {
                friend = (Player) friendship.getFirstPlayer();
            } else if (friendship.getFirstPlayer() == player) {
                friend = (Player) friendship.getSecondPlayer();
            } else continue;
            i++;
            Image iconImage = new Image(friend.getAvatar());
            Label nameLabel = new Label(friend.getUser().getNickname(), skin);
            final Label tooltipLabel = new Label(friend.getUser().getUsername(), skin);
            tooltipLabel.setVisible(false);
            tooltipLabel.setColor(1, 1, 1, 0.9f);
            tooltipLabel.setZIndex(1000);
            menuGroup.addActor(tooltipLabel);
            iconImage.addListener(new InputListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
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

            RelationService relationService = RelationService.getInstance();

            float currentXP = friendship.getXp();
            float maxXP = relationService.xpNeededForChangeLevel(friendship);
            int level = friendship.getFriendShipLevel();


            ProgressBar xpBar = new ProgressBar(0, maxXP, 1, false, skin);
            xpBar.setValue(currentXP);
            xpBar.setAnimateDuration(0.2f);
            xpBar.setWidth(300);

            Label levelLabel = new Label("Level: " + level, skin);

            ImageButton gift = gifts.get(i);
            ImageButton history = histories.get(i);
            ImageButton chat = chats.get(i);
            friendships.add(friendship);

            Table row = new Table();
            row.defaults().pad(10).align(Align.left);
            row.add(iconImage).size(64).padRight(20);
            row.add(nameLabel).width(100).padRight(20);
            row.add(xpBar).width(300).padRight(20);
            row.add(levelLabel).width(80).align(Align.right).padRight(20);
            row.add(gift).size(64).padRight(20);
            row.add(history).size(64).padRight(20);
            row.add(chat).size(64);

            table.add(row).row();
        }

        Table content = new Table();
        content.setFillParent(true);
        content.add(table).expand().fill().pad(20);

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
                for (int j = 0; j < friendships.size(); j++) {
                    Player friend;
                    Friendship friendship = friendships.get(j);
                    if (friendship.getSecondPlayer() == player) {
                        friend = (Player) friendship.getFirstPlayer();
                    } else {
                        friend = (Player) friendship.getSecondPlayer();
                    }

                    if (gifts.get(j).isChecked()) {
                        endTask(array, exitButton);
                        GiftMenu giftMenu = new GiftMenu();
                        giftMenu.setFriendship(friendship);
                        giftMenu.createMenu(stage, skin, getController());
                        friendships.clear();
                        gifts.get(j).setChecked(false);
                        break;
                    }
                    if (histories.get(j).isChecked()) {
                        endTask(array, exitButton);
                        GiftHistoryMenu giftMenu = new GiftHistoryMenu();
                        giftMenu.setFriendship(friendship);
                        giftMenu.createMenu(stage, skin, getController());
                        friendships.clear();
                        histories.get(j).setChecked(false);
                        break;
                    }
                    if (chats.get(j).isChecked()) {
                        endTask(array, exitButton);
                        getController().showResponse(new Response(friend.getUser().getNickname()));
                        TalkMenu giftMenu = new TalkMenu();
                        giftMenu.setFriendship(friendship);
                        giftMenu.createMenu(stage, skin, getController());
                        friendships.clear();
                        chats.get(j).setChecked(false);
                        break;
                    }
                }
                super.act(delta);
            }
        };

        group.addActor(window);
        group.addActor(exitButton);
        group.addActor(tabs);
        menuGroup.addActor(group);
    }

    private void createMapMenu(Skin skin,Group menuGroup,Table tabs) {
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

        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        array.add(tabs);
        ImageButton exitButton = provideExitButton(array);

        Group group = new Group() {
            @Override
            public void act(float delta) {
                window.setPosition(
                    (camera.viewportWidth - window.getWidth()) / 2f + camera.position.x - camera.viewportWidth / 2,
                    (camera.viewportHeight - window.getHeight()) / 2f + camera.position.y - camera.viewportHeight / 2
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

    private void createMapMenu(Skin skin, Group menuGroup, Table tabs) {
        Window window = new Window("Mini Map", skin);
        window.setSize(900, 600);
        window.setMovable(false);
        OrthographicCamera camera = MainGradle.getInstance().getCamera();

        float scale = 0.3f;
        MiniMapActor miniMapActor = new MiniMapActor(scale);

        ScrollPane scrollPane = new ScrollPane(miniMapActor, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(false, false);
        scrollPane.setScrollbarsOnTop(true);

        Table content = new Table();
        content.setFillParent(true);
        content.add(scrollPane).expand().fill().pad(10);

        window.add(content).expand().fill();

        ImageButton exitButton = provideExitButton(new ArrayList<>(java.util.List.of(window, tabs)));

        Group group = new Group() {
            @Override
            public void act(float delta) {
                window.setPosition(
                    (camera.viewportWidth - window.getWidth()) / 2f + camera.position.x - camera.viewportWidth / 2,
                    (camera.viewportHeight - window.getHeight()) / 2f + camera.position.y - camera.viewportHeight / 2
                );
                exitButton.setPosition(
                    window.getX() + window.getWidth() - exitButton.getWidth() / 2f + 16,
                    window.getY() + window.getHeight() - exitButton.getHeight() / 2f
                );
                tabs.setPosition(window.getX(), window.getY() + window.getHeight() + 70);
                super.act(delta);
            }
        };

        group.addActor(window);
        group.addActor(exitButton);
        group.addActor(tabs);
        menuGroup.addActor(group);
    }
}
