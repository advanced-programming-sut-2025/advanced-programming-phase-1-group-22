package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.client.controller.WorldController;
import io.github.some_example_name.common.model.Farm;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.craft.CraftType;
import io.github.some_example_name.common.model.products.TreesAndFruitsAndSeeds.MadeProductType;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.model.relations.Friendship;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.relations.Trade;
import io.github.some_example_name.common.model.source.Mineral;
import io.github.some_example_name.common.model.source.MineralType;
import io.github.some_example_name.common.model.tools.BackPack;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Setter
public class TradeMenu extends PopUp {
    private static TradeMenu tradeMenu;
    private Friendship friendship;
    private Player friend;
    private Window window, window2;
    private boolean trader = true;
    private Table inventory = new Table();
    private Table proposedTable = new Table();
    private Table answeredTable = new Table();
    ScrollPane inventoryScroll = new ScrollPane(inventory, skin);
    ScrollPane proposedScroll = new ScrollPane(proposedTable, skin);
    ScrollPane answeredScroll = new ScrollPane(answeredTable, skin);
    ArrayList<Actor> array = new ArrayList<>();
    private Map<Salable, Integer> proposed = new HashMap<>();
    @Getter
    private Map<Salable, Integer> answered = new HashMap<>();

    private TradeMenu() {}

    public static TradeMenu getTradeMenu() {
        if (tradeMenu == null) tradeMenu = new TradeMenu();
        return tradeMenu;
    }

    public static void uninit() {
        tradeMenu.endTask(tradeMenu.array, tradeMenu.trashCan);
        tradeMenu = null;
    }

    public static boolean isInitialized() {
        return tradeMenu != null;
    }

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
                GameClient.getInstance().updatePlayerDeleteFromInventory(currentPlayer,item,1);
            } else {
                proposed.remove(item);
                GameClient.getInstance().removeTrade(friend.getName(), item);
                updateProposed();
            }
        } else if (flag && isOverActor(dragImage, window2)) {
            Dialog dialog = new Dialog("How Many?", skin);
            dialog.setWidth(500);
            dialog.setHeight(400);
            dialog.center();
            TextArea count = new TextArea("", skin);
            TextButton confirm = new TextButton("Confirm", skin);
            dialog.add(count).width(300).row();
            dialog.add(confirm).width(300);
            confirm.addListener(new  ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    try {
                        int cou = Integer.parseInt(count.getText());
                        if (cou <= 0) throw new NumberFormatException();
                        if (cou > App.getInstance().getCurrentGame().getCurrentPlayer().getInventory().getProducts().get(item)) {
                            getController().showResponse(new Response("You have not that many."));
                        } else {
                            dialog.remove();
                            proposed.put(item, cou);
                            GameClient.getInstance().addTrade(friend.getName(), item, cou);
                            updateProposed();
                        }
                    } catch (NumberFormatException e) {
                        getController().showResponse(new Response("Enter a valid number"));
                    }
                    return true;
                }
            });
            stage.addActor(dialog);
        } else if (!flag && isOverActor(dragImage, window)) {
            proposed.remove(item);
            GameClient.getInstance().removeTrade(friend.getName(), item);
            updateProposed();
        }
        createMenu(stage, skin, getController());
    }

    public void createInventory(Skin skin, Group menuGroup, Stage stage) {
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
        Table doIt = new Table();
        inventory.pack();
        inventoryScroll.setFadeScrollBars(false);
        inventoryScroll.setScrollbarsOnTop(true);
        inventoryScroll.setScrollingDisabled(false, false);
        inventoryScroll.setScrollBarPositions(true, true);
        inventoryScroll.setForceScroll(false, true);
        inventoryScroll.layout();
        inventoryScroll.setTouchable(Touchable.enabled);
        answeredScroll.setFadeScrollBars(false);
        answeredScroll.setScrollbarsOnTop(true);
        answeredScroll.setScrollingDisabled(false, false);
        answeredScroll.setScrollBarPositions(true, true);
        answeredScroll.setForceScroll(true, true);
        answeredScroll.layout();
        proposedScroll.setTouchable(Touchable.enabled);
        proposedScroll.setFadeScrollBars(false);
        proposedScroll.setScrollbarsOnTop(true);
        proposedScroll.setScrollingDisabled(false, false);
        proposedScroll.setScrollBarPositions(true, true);
        proposedScroll.setForceScroll(true, true);
        proposedScroll.layout();
        proposedScroll.setTouchable(Touchable.enabled);

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
                        list.add(inventoryScroll);
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

        array.add(window);
        array.add(window2);
        array.add(trashCan);
        ImageButton exitButton = provideExitButton(array);

        proposedTable.pack();
        answeredTable.pack();
        doIt.pack();
        updateProposed();
        updateAnswered();
        TextButton cancel = new TextButton("Leave Negotiation.", skin);
        cancel.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                uninit();
                GameClient.getInstance().rejectTrade(friend.getName(), currentPlayer.getNickname() + " left.");
                return true;
            }
        });
        doIt.add(cancel).row();
        if (trader) {
            TextButton confirm = new TextButton("Confirm Negotiation.", skin);
            confirm.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    uninit();
                    GameClient.getInstance().acceptTrade(friend.getName());
                    accept();
                    return true;
                }
            });
            doIt.add(confirm).row();
        }

        window2.setSize(camera.viewportWidth * 0.7f, camera.viewportHeight * 0.35f);
        window2.setPosition(
            (camera.viewportWidth - window2.getWidth()) / 2f,
            (camera.viewportHeight - window2.getHeight()) / 2f
        );



        Table content = new Table();
        content.setFillParent(true);
        content.add(inventoryScroll).width(window.getWidth()).height(window.getHeight() - 100).padBottom(20).padTop(50).row();


        Table content2 = new Table();
        content2.setFillParent(true);
        content2.add(proposedScroll).width(0.35f * window2.getWidth()).height(window.getHeight() - 100).padRight(30).padBottom(20).padTop(50);
        content2.add(doIt).width(0.2f * window2.getWidth()).height(window.getHeight() - 100).padRight(30).padBottom(20).padTop(50);
        content2.add(answeredScroll).width(0.35f * window2.getWidth()).height(window.getHeight() - 100).padBottom(20).padTop(50).row();


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

    public void updateProposed() {
        proposedTable.clearChildren();
        int maxCol = 4;
        int maxRow = (int) Math.ceil((double) proposed.size() /4);
        Map<Salable, Integer> products = proposed;
        for (int row = 0; row < maxRow; row++) {
            for (int col = 0; col < maxCol; col++) {
                Image slot = new Image(slotTexture);

                int index = row * maxCol + col;
                synchronized (products){
                    if (index < products.size()) {
                        java.util.List<Salable> items = new ArrayList<>(products.keySet());
                        Salable item = items.get(index);
                        Image itemImage = new Image(item.getTexture());
                        ArrayList<ScrollPane> list = new ArrayList<>();
                        list.add(proposedScroll);
                        addDrag(itemImage, stage, App.getInstance().getCurrentGame().getCurrentPlayer(), item, list,false);
                        itemImage.setSize(90, 90);
                        Container<Label> labelContainer = getLabelContainer(products, item);
                        Stack stack = new Stack();
                        stack.add(slot);
                        stack.add(itemImage);
                        stack.add(labelContainer);
                        proposedTable.add(stack).size(96, 96);
                    } else {
                        proposedTable.add(slot).size(96, 96);
                    }
                }
            }
            proposedTable.row();
        }
    }

    public void updateAnswered() {
        answeredTable.clearChildren();
        int maxCol = 4;
        int maxRow = (int) Math.ceil((double) answered.size() /4);
        Map<Salable, Integer> products = answered;
        for (int row = 0; row < maxRow; row++) {
            for (int col = 0; col < maxCol; col++) {
                Image slot = new Image(slotTexture);

                int index = row * maxCol + col;
                synchronized (products){
                    if (index < products.size()) {
                        java.util.List<Salable> items = new ArrayList<>(products.keySet());
                        Salable item = items.get(index);
                        Image itemImage = new Image(item.getTexture());
                        ArrayList<ScrollPane> list = new ArrayList<>();
                        itemImage.setSize(90, 90);
                        Container<Label> labelContainer = getLabelContainer(products, item);
                        Stack stack = new Stack();
                        stack.add(slot);
                        stack.add(itemImage);
                        stack.add(labelContainer);
                        answeredTable.add(stack).size(96, 96);
                    } else {
                        answeredTable.add(slot).size(96, 96);
                    }
                }
            }
            answeredTable.row();
        }
    }

    public void accept() {
        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
        player.getGootenTradeList().add(new Trade(
            trader ? player : friend,
            trader ? friend : player,
            proposed,
            answered
        ));
        for (Map.Entry<Salable, Integer> entry : proposed.entrySet()) {
            if (entry.getValue() >= player.getInventory().getProducts().get(entry.getKey())) {
                synchronized (player.getInventory()) {
                    Integer amount = player.getInventory().getProducts().get(entry.getKey());
                    player.getInventory().getProducts().remove(entry.getKey());
                    GameClient.getInstance().updatePlayerDeleteFromInventory(player, entry.getKey(), amount);
                }
            } else {
                player.getInventory().deleteProductFromBackPack(entry.getKey(), player, entry.getValue());
                GameClient.getInstance().updatePlayerDeleteFromInventory(player, entry.getKey(),entry.getValue());
            }
        }
        for (Map.Entry<Salable, Integer> entry : answered.entrySet()) {
            player.getInventory().addProductToBackPack(entry.getKey(), entry.getValue());
            GameClient.getInstance().updatePlayerAddToInventory(player, entry.getKey(), entry.getValue());
        }
        tradeMenu = this;
        uninit();
    }
}
