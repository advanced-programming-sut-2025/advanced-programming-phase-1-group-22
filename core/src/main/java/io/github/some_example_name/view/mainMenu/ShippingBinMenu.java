package io.github.some_example_name.view.mainMenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.controller.WorldController;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.shelter.ShippingBin;
import io.github.some_example_name.model.tools.BackPack;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.GameAsset;

import java.util.ArrayList;

public class ShippingBinMenu extends PopUp {
    private Runnable updatePrice;
    private final ShippingBin shippingBin;

    public ShippingBinMenu(ShippingBin shippingBin) {
        this.shippingBin = shippingBin;
    }

    @Override
    public void createMenu(Stage stage, Skin skin, WorldController playerController) {
        super.createMenu(stage, skin, playerController);
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        Window window = new Window("", skin);
        window.setSize(camera.viewportWidth * 0.5f, camera.viewportHeight * 0.3f);
        window.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();

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
                    itemImage.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            if (item.getSellPrice() == 0)
                                playerController.showResponse(new Response("you can't sell this Item"));
                            else {
                                createSellMenu(stage, inventory, currentPlayer, slotTexture, currentPlayer, scrollPane, item, currentPlayer.getInventory().getProducts().get(item));
                                refreshInventory(stage, inventory, currentPlayer, slotTexture, currentPlayer, scrollPane);
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
                    itemImage.setColor(1, 1, 1, 1f);
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

        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        ImageButton exitButton = provideExitButton(array);


        Table content = new Table();
        content.setFillParent(true);
        content.add(scrollPane).width(600).height(220).padBottom(20).padTop(50).row();

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

                super.act(delta);
            }
        };
        group.addActor(window);
        group.addActor(exitButton);
        getMenuGroup().addActor(group);
    }

    private void refreshInventory(Stage stage, Table inventory, Player player, Texture slotTexture, Player currentPlayer, ScrollPane scrollPane) {
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
                    itemImage.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            if (item.getSellPrice() == 0)
                                getController().showResponse(new Response("you can't sell this Item"));
                            else {
                                createSellMenu(stage, inventory, currentPlayer, slotTexture, currentPlayer, scrollPane, item, currentPlayer.getInventory().getProducts().get(item));
                                refreshInventory(stage, inventory, currentPlayer, slotTexture, currentPlayer, scrollPane);
                            }
                        }
                    });
                    Container<Label> labelContainer = getLabelContainer(currentPlayer.getInventory().getProducts(), item);
                    itemImage.setColor(1, 1, 1, 1f);
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

    private void createSellMenu(Stage stage, Table inventory, Player player, Texture slotTexture, Player currentPlayer, ScrollPane scrollPane, Salable salable, Integer maxAmount) {
        final Window buyWindow = new Window("Sell", GameAsset.SKIN);
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
                refreshInventory(stage, inventory, player, slotTexture, currentPlayer, scrollPane);
            }
        });

        Table content = new Table();

        Image image = new Image(salable.getTexture());
        image.setSize(64, 64);
        Label name = new Label(salable.getName(), GameAsset.SKIN);

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
                if (amount[0] < maxAmount) {
                    amount[0]++;
                    amountLabel.setText(String.valueOf(amount[0]));
                    updatePrice.run();
                }
            }
        });

        amountRow.add(minus).pad(5);
        amountRow.add(amountLabel).pad(5);
        amountRow.add(plus).pad(5);

        content.add(amountRow).pad(10).row();

        Label totalPrice = new Label("Total Price: " + salable.getSellPrice() * amount[0], GameAsset.SKIN);
        content.add(totalPrice).pad(10).row();

        TextButton buyButton = new TextButton("Sell", GameAsset.SKIN);
        buyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                player.getInventory().justDelete(salable, amount[0]);
                shippingBin.add(salable, amount[0]);
                buyWindow.remove();
                exitButton.remove();
                refreshInventory(stage, inventory, player, slotTexture, currentPlayer, scrollPane);
            }
        });

        content.add(buyButton).pad(10).row();

        this.updatePrice = () -> totalPrice.setText("Total: " + (salable.getSellPrice() * amount[0]));

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
        getMenuGroup().addActor(group);
    }

    @Override
    protected void handleDragRelease(InputEvent event, float x, float y, int pointer, Image itemImage, Salable item, Image dragImage, Boolean flag) {
    }
}
