package io.github.some_example_name.client.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.common.model.Farm;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.animal.AnimalType;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.shelter.FarmBuildingType;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.common.model.structure.stores.CarpenterShopFarmBuildings;
import io.github.some_example_name.common.model.structure.stores.Item;
import io.github.some_example_name.common.model.structure.stores.Store;
import io.github.some_example_name.common.model.tools.MilkPail;
import io.github.some_example_name.common.model.tools.Shear;
import io.github.some_example_name.common.model.tools.Tool;
import io.github.some_example_name.server.service.GameService;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;
import io.github.some_example_name.client.view.GameView;

import java.util.List;
import java.util.Map;

public class StoreController {
    private final GameService gameService = new GameService();
    private final Group menuGroup = new Group();
    private Runnable updatePrice;
    private final WorldController worldController = new WorldController();

    public StoreController() {
        if (!GameView.stage.getActors().contains(menuGroup, true)) {
            GameView.stage.addActor(menuGroup);
        }
    }

    public void update() {
        if (GameView.captureInput) {
            handleInputs();
        }
    }

    private void handleInputs() {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector3 worldCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            MainGradle.getInstance().getCamera().unproject(worldCoords);
            float worldX = worldCoords.x;
            float worldY = worldCoords.y;
            for (Structure structure : App.getInstance().getCurrentGame().getVillage().getStructures()) {
                if (structure instanceof Store) {
                    if (collision((Store) structure, worldX, worldY)) createStoreMenu((Store) structure);
                }
            }
        }
    }

    private boolean collision(Store store, float worldX, float worldY) {
        Sprite sprite = store.getSprite();
        sprite.setPosition(store.getTiles().get(0).getX() * App.tileWidth, store.getTiles().get(0).getY() * App.tileHeight);
        return worldX >= sprite.getX() && worldX <= sprite.getX() + sprite.getWidth() && worldY >= sprite.getY() && worldY <= sprite.getY() + sprite.getHeight();
    }

    private void createStoreMenu(Store store) {
        menuGroup.clear();

        Window window = new Window("", GameAsset.SKIN);
        window.setSize(1200, 700);
        window.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);

        Table table = new Table();
        table.setFillParent(true);
        table.top().pad(20).defaults().pad(10).left();

        boolean isStoreOpen = gameService.isStoreOpen(store.getStoreType()).shouldBeBack();
        if (!isStoreOpen) {
            createCloseMenu(store, table, window);
        } else {
            createStuffMenu(store, table, window);
        }
    }

    private void createCloseMenu(Store store, Table table, Window window) {
        window.clear();
        table.clear();
        Label close = new Label(store.getStoreType().getStoreName() + " is close now \n\n"
            + "Open Door Time: " + store.getStoreType().getOpenDoorTime() + "\n\n"
            + "Close Door Time: " + store.getStoreType().getCloseDoorTime(), GameAsset.SKIN);
        Image shopper = new Image(store.getStoreType().getShopperTexture());
        table.add(close).center().row();
        table.row();
        table.row();
        table.row();
        table.row();
        table.row();
        shopper.setSize(256, 256);
        table.add(shopper).center();
        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.EXIT_BUTTON)));
        exitButton.setSize(32, 32);
        window.add(table).expand().fill();

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                window.remove();
                exitButton.remove();
            }
        });
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
        menuGroup.addActor(group);
    }

    private void createStuffMenu(Store store, Table table, Window window) {
        window.clear();
        table.clear();
        List<Item> items = store.getStoreType().getItems();

        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.EXIT_BUTTON)));
        exitButton.setSize(32, 32);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                window.remove();
                exitButton.remove();
            }
        });

        final boolean[] showOnlyAvailable = {false};

        Table filterRow = new Table();
        TextButton showAllButton = new TextButton("All Products", GameAsset.SKIN);
        TextButton availableButton = new TextButton("Available Products", GameAsset.SKIN);

        Table itemTable = new Table();

        showAllButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showOnlyAvailable[0] = false;
                updateItemList(itemTable, items, showOnlyAvailable, store, window, exitButton);
            }
        });

        availableButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showOnlyAvailable[0] = true;
                updateItemList(itemTable, items, showOnlyAvailable, store, window, exitButton);
            }
        });

        filterRow.add(showAllButton).pad(10);
        filterRow.add(availableButton).pad(10).left();
        filterRow.add().expandX();

        Image sellerImage = new Image(store.getStoreType().getShopperTexture());
        sellerImage.setSize(128, 128);
        filterRow.add(sellerImage).pad(10).right();

        table.add(filterRow).expandX().fillX().row();

        ScrollPane scrollPane = new ScrollPane(itemTable, GameAsset.SKIN);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        table.add(scrollPane).expand().fill().pad(10).row();

        updateItemList(itemTable, items, showOnlyAvailable, store, window, exitButton);

        window.add(table).expand().fill();

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
        menuGroup.addActor(group);
    }

    private void updateItemList(Table itemTable, List<Item> items, boolean[] showOnlyAvailable, Store store, Window window, ImageButton exitButton) {
        itemTable.clear();
        for (Item item : items) {
            if (showOnlyAvailable[0] && !item.isAvailable()) continue;

            Table row = new Table();

            Image itemImage = new Image(item.getProduct().getTexture());
            itemImage.setSize(48, 48);
            itemImage.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (item.isAvailable()) {
                        if (item.getProduct() instanceof Tool && !(item.getProduct() instanceof Shear) && !(item.getProduct() instanceof MilkPail)) {
                            createUpgradeMenu(item, store);
                            window.remove();
                            exitButton.remove();
                        } else if (item.getProduct() instanceof AnimalType) {
                            createBuyAnimalMenu(item, store);
                            window.remove();
                            exitButton.remove();
                        } else if (item.getProduct() instanceof FarmBuildingType) {
                            createBuildMenu(item, store);
                            window.remove();
                            exitButton.remove();
                        } else {
                            createSellMenu(item, store);
                            window.remove();
                            exitButton.remove();
                        }
                    }
                }
            });

            Label nameLabel = new Label(item.getProduct().getName(), GameAsset.SKIN);

            Label priceLabel = new Label("Price: " + item.getPrice(), GameAsset.SKIN);

            Label countLabel = (item.getDailyLimit() == -1)
                ? new Label("Remain: No limit", GameAsset.SKIN)
                : new Label("Remain: " + (item.getDailyLimit() - item.getDailySold()), GameAsset.SKIN);

            if (!item.isAvailable()) {
                itemImage.setColor(1f, 1f, 1f, 0.3f);
                priceLabel.setColor(Color.GRAY);
                countLabel.setColor(Color.GRAY);
                nameLabel.setColor(Color.GRAY);
            }

            row.add(itemImage).pad(5).left();
            row.add(nameLabel).expandX().pad(5).left();
            row.add(priceLabel).pad(25).center();
            row.add(countLabel).pad(5).right();

            itemTable.add(row).expandX().fillX().pad(5).row();
        }
    }

    private void createSellMenu(Item item, Store store) {
        final Window buyWindow = new Window("Buy Item", GameAsset.SKIN);
        buyWindow.setSize(700, 500);
        buyWindow.setModal(true);
        buyWindow.setMovable(false);

        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.EXIT_BUTTON)));
        exitButton.setSize(32, 32);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buyWindow.remove();
                exitButton.remove();
                createStoreMenu(store);
            }
        });

        Table content = new Table();

        Image image = new Image(item.getProduct().getTexture());
        image.setSize(64, 64);
        Label name = new Label(item.getProduct().getName(), GameAsset.SKIN);

        Table topRow = new Table();
        topRow.add(image).pad(10);
        topRow.add(name).pad(10);
        content.add(topRow).pad(10).row();

        Table amountRow = new Table();
        Label amountLabel = new Label("1", GameAsset.SKIN);
        final int[] amount = {1};

        TextButton minus = new TextButton("-", GameAsset.SKIN);
        TextButton plus = new TextButton("+", GameAsset.SKIN);

        minus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (amount[0] > 1) {
                    amount[0]--;
                    amountLabel.setText(String.valueOf(amount[0]));
                    updatePrice.run();
                }
            }
        });

        plus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                amount[0]++;
                amountLabel.setText(String.valueOf(amount[0]));
                updatePrice.run();
            }
        });

        amountRow.add(minus).pad(5);
        amountRow.add(amountLabel).pad(5);
        amountRow.add(plus).pad(5);

        content.add(amountRow).pad(10).row();

        Label totalPrice = new Label("Total Price: " + item.getPrice() * amount[0], GameAsset.SKIN);
        content.add(totalPrice).pad(10).row();

        TextButton buyButton = new TextButton("Buy", GameAsset.SKIN);
        buyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Response response = store.getStoreType().purchase(item.getProduct().getName(), amount[0]);
                worldController.showResponse(response);
                if (response.shouldBeBack()) GameClient.getInstance().updateStoreDailySold(item.getShop(), amount[0]);
                buyWindow.remove();
                exitButton.remove();
                createStoreMenu(store);
            }
        });

        content.add(buyButton).pad(10).row();

        this.updatePrice = () -> totalPrice.setText("Total: " + (item.getPrice() * amount[0]));

        buyWindow.add(content).pad(20);

        Group group = new Group() {
            @Override
            public void act(float delta) {
                buyWindow.setPosition(
                    (MainGradle.getInstance().getCamera().viewportWidth - buyWindow.getWidth()) / 2f +
                        MainGradle.getInstance().getCamera().position.x - MainGradle.getInstance().getCamera().viewportWidth / 2,
                    (MainGradle.getInstance().getCamera().viewportHeight - buyWindow.getHeight()) / 2f +
                        MainGradle.getInstance().getCamera().position.y - MainGradle.getInstance().getCamera().viewportHeight / 2
                );
                exitButton.setPosition(
                    buyWindow.getX() + buyWindow.getWidth() - exitButton.getWidth() / 2f + 16,
                    buyWindow.getY() + buyWindow.getHeight() - exitButton.getHeight() / 2f
                );
                super.act(delta);
            }
        };
        group.addActor(buyWindow);
        group.addActor(exitButton);
        menuGroup.addActor(group);
    }

    private void createUpgradeMenu(Item item, Store store) {
        final Window buyWindow = new Window("Buy Item", GameAsset.SKIN);
        buyWindow.setSize(700, 500);
        buyWindow.setModal(true);
        buyWindow.setMovable(false);

        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.EXIT_BUTTON)));
        exitButton.setSize(32, 32);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buyWindow.remove();
                exitButton.remove();
                createStoreMenu(store);
            }
        });

        Table content = new Table();

        Image image = new Image(item.getProduct().getTexture());
        image.setSize(64, 64);
        Label name = new Label(item.getProduct().getName(), GameAsset.SKIN);
        Label price = new Label("Price: " + item.getPrice(), GameAsset.SKIN);

        Table topRow = new Table();
        topRow.add(image).pad(10);
        topRow.add(name).pad(10);
        content.add(topRow).pad(10).row();
        content.add(price).pad(10).row();
        if (item.getIngredient() != null) {
            for (Map.Entry<Salable, Integer> salableIntegerEntry : item.getIngredient().entrySet()) {
                Table row = new Table();
                Image itemImage = new Image(salableIntegerEntry.getKey().getTexture());
                itemImage.setSize(48, 48);
                Label description = new Label("name: " + salableIntegerEntry.getKey().getName() + " count: " + salableIntegerEntry.getValue(), GameAsset.SKIN);
                row.add(itemImage).pad(5);
                row.add(description).pad(5);
                content.add(row).expandX().fillX().pad(5).row();
            }
        }

        TextButton buyButton = new TextButton("Upgrade", GameAsset.SKIN);
        buyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Response response = gameService.upgradeTool((Tool) item.getProduct());
                worldController.showResponse(response);
                if (response.shouldBeBack()) GameClient.getInstance().updateStoreDailySold(item.getShop(), 1);
                buyWindow.remove();
                exitButton.remove();
                createStoreMenu(store);
            }
        });

        content.add(buyButton).pad(10).row();

        buyWindow.add(content).pad(20);

        Group group = new Group() {
            @Override
            public void act(float delta) {
                buyWindow.setPosition(
                    (MainGradle.getInstance().getCamera().viewportWidth - buyWindow.getWidth()) / 2f +
                        MainGradle.getInstance().getCamera().position.x - MainGradle.getInstance().getCamera().viewportWidth / 2,
                    (MainGradle.getInstance().getCamera().viewportHeight - buyWindow.getHeight()) / 2f +
                        MainGradle.getInstance().getCamera().position.y - MainGradle.getInstance().getCamera().viewportHeight / 2
                );
                exitButton.setPosition(
                    buyWindow.getX() + buyWindow.getWidth() - exitButton.getWidth() / 2f + 16,
                    buyWindow.getY() + buyWindow.getHeight() - exitButton.getHeight() / 2f
                );
                super.act(delta);
            }
        };
        group.addActor(buyWindow);
        group.addActor(exitButton);
        menuGroup.addActor(group);
    }

    private void createBuyAnimalMenu(Item item, Store store) {
        GameView.captureInput = false;
        final Window buyWindow = new Window("Buy Item", GameAsset.SKIN);
        buyWindow.setSize(700, 500);
        buyWindow.setModal(true);
        buyWindow.setMovable(false);

        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.EXIT_BUTTON)));
        exitButton.setSize(32, 32);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buyWindow.remove();
                exitButton.remove();
                GameView.captureInput = true;
                createStoreMenu(store);
            }
        });

        Table content = new Table();

        Image image = new Image(item.getProduct().getTexture());
        image.setSize(64, 64);
        Label name = new Label(item.getProduct().getName(), GameAsset.SKIN);

        Table topRow = new Table();
        topRow.add(image).pad(10);
        topRow.add(name).pad(10);
        content.add(topRow).pad(10).row();

        Label totalPrice = new Label("Total Price: " + item.getPrice(), GameAsset.SKIN);
        content.add(totalPrice).pad(10).row();

        TextField animalName = new TextField("name", GameAsset.SKIN);
        content.add(animalName).pad(10).row();

        TextButton buyButton = new TextButton("Buy", GameAsset.SKIN);
        buyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (animalName.getText() == null) return;
                Response response = gameService.buyAnimal(item.getProduct().getName(), animalName.getText());
                worldController.showResponse(response);
                if (response.shouldBeBack()) GameClient.getInstance().updateStoreDailySold(item.getShop(), 1);
                buyWindow.remove();
                exitButton.remove();
                GameView.captureInput = true;
                createStoreMenu(store);
            }
        });

        content.add(buyButton).pad(10).row();
        buyWindow.add(content).pad(20);

        Group group = new Group() {
            @Override
            public void act(float delta) {
                buyWindow.setPosition(
                    (MainGradle.getInstance().getCamera().viewportWidth - buyWindow.getWidth()) / 2f +
                        MainGradle.getInstance().getCamera().position.x - MainGradle.getInstance().getCamera().viewportWidth / 2,
                    (MainGradle.getInstance().getCamera().viewportHeight - buyWindow.getHeight()) / 2f +
                        MainGradle.getInstance().getCamera().position.y - MainGradle.getInstance().getCamera().viewportHeight / 2
                );
                exitButton.setPosition(
                    buyWindow.getX() + buyWindow.getWidth() - exitButton.getWidth() / 2f + 16,
                    buyWindow.getY() + buyWindow.getHeight() - exitButton.getHeight() / 2f
                );
                super.act(delta);
            }
        };
        group.addActor(buyWindow);
        group.addActor(exitButton);
        menuGroup.addActor(group);
    }

    private void createBuildMenu(Item item, Store store) {
        final Window buyWindow = new Window("Buy Item", GameAsset.SKIN);
        buyWindow.setSize(700, 800);
        buyWindow.setModal(true);
        buyWindow.setMovable(false);

        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(GameAsset.EXIT_BUTTON)));
        exitButton.setSize(32, 32);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buyWindow.remove();
                exitButton.remove();
                createStoreMenu(store);
            }
        });

        Table content = new Table();

        Image image = new Image(item.getProduct().getTexture());
        Label name = new Label(item.getProduct().getName(), GameAsset.SKIN);

        Table topRow = new Table();
        topRow.add(image).pad(10);
        topRow.add(name).pad(10);
        content.add(topRow).pad(10).row();

        Label totalPrice = new Label("Total Price: " + item.getPrice(), GameAsset.SKIN);
        content.add(totalPrice).pad(10).row();
        if (item.getIngredient() != null) {
            for (Map.Entry<Salable, Integer> salableIntegerEntry : item.getIngredient().entrySet()) {
                Table row = new Table();
                Image itemImage = new Image(salableIntegerEntry.getKey().getTexture());
                itemImage.setSize(48, 48);
                Label description = new Label("name: " + salableIntegerEntry.getKey().getName() + " count: " + salableIntegerEntry.getValue(), GameAsset.SKIN);
                row.add(itemImage).pad(5);
                row.add(description).pad(5);
                content.add(row).expandX().fillX().pad(5).row();
            }
        }

        TextButton chosePosition = new TextButton("Chose Position", GameAsset.SKIN);
        chosePosition.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameView.positionChoosing = true;
                GameView.building = item.getProduct().getName();
                chosePosition();
                buyWindow.remove();
                exitButton.remove();
            }
        });

        content.add(chosePosition).pad(10).row();

        buyWindow.add(content).pad(20);

        Group group = new Group() {
            @Override
            public void act(float delta) {
                buyWindow.setPosition(
                    (MainGradle.getInstance().getCamera().viewportWidth - buyWindow.getWidth()) / 2f +
                        MainGradle.getInstance().getCamera().position.x - MainGradle.getInstance().getCamera().viewportWidth / 2,
                    (MainGradle.getInstance().getCamera().viewportHeight - buyWindow.getHeight()) / 2f +
                        MainGradle.getInstance().getCamera().position.y - MainGradle.getInstance().getCamera().viewportHeight / 2
                );
                exitButton.setPosition(
                    buyWindow.getX() + buyWindow.getWidth() - exitButton.getWidth() / 2f + 16,
                    buyWindow.getY() + buyWindow.getHeight() - exitButton.getHeight() / 2f
                );
                super.act(delta);
            }
        };
        group.addActor(buyWindow);
        group.addActor(exitButton);
        menuGroup.addActor(group);
    }

    private void chosePosition() {
        Farm farm = getPlayerMainFarm(App.getInstance().getCurrentGame().getCurrentPlayer());
        MainGradle.getInstance().getCamera().position.set(farm.getXCenter() * App.tileWidth, farm.getYCenter() * App.tileHeight, 0);
        MainGradle.getInstance().getCamera().update();
    }

    public void build(String name, int x, int y) {
        Response response = gameService.build(name, x, y);
        worldController.showResponse(response);
        if (response.shouldBeBack()) {
            CarpenterShopFarmBuildings carpenterShopFarmBuildings = CarpenterShopFarmBuildings.getFromName(name);
            if (carpenterShopFarmBuildings == null) return;
            GameClient.getInstance().updateStoreDailySold(carpenterShopFarmBuildings, 1);
        }
    }

    private Farm getPlayerMainFarm(Player player) {
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            if (!farm.getPlayers().isEmpty() && farm.getPlayers().get(0).equals(player)) {
                return farm;
            }
        }
        return null;
    }
}
