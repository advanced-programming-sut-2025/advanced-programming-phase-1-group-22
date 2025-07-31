package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Timer;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.client.controller.WorldController;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.relations.Trade;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.server.service.TradeService;
import lombok.Setter;

import java.util.ArrayList;

@Setter
public class TradeHistoryMenu extends PopUp {
    private Window window;
    private ScrollPane scrollPane;
    private Float scrollY;

    public void createMenu(Stage stage, Skin skin, WorldController worldController) {
        super.createMenu(stage, skin, worldController);
        createInventory(skin, getMenuGroup(), stage);
    }

    @Override
    protected void handleDragRelease(InputEvent event, float x, float y, int pointer, Image itemImage, Salable item, Image dragImage, Boolean flag) {}

    private void createInventory(Skin skin, Group menuGroup, Stage stage) {
        OrthographicCamera camera = MainGradle.getInstance().getCamera();

        window = new Window("", skin);
        window.setSize(camera.viewportWidth * 0.7f, camera.viewportHeight * 0.7f);

        window.setPosition(
            camera.viewportWidth - window.getWidth() / 2f,
            camera.viewportHeight - window.getHeight() / 2f
        );
        window.setMovable(false);

        Table inventory = new Table();
        scrollPane = new ScrollPane(inventory, skin);
        inventory.pack();
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollbarsOnTop(false);
        scrollPane.setScrollingDisabled(false, false);
        scrollPane.setScrollBarPositions(true, true);
        scrollPane.setForceScroll(false, true);
        scrollPane.layout();
        scrollPane.setTouchable(Touchable.enabled);
        inventory.add(new Label("Trader", skin)).width(60).padRight(20);
        inventory.add(new Label("Customer", skin)).width(60).padRight(20);
        inventory.add(new Label("Trader Offer", skin)).width(200).padRight(20);
        inventory.add(new Label("Customer Offer", skin)).width(200).padRight(20);
        inventory.add(new Label("Status", skin)).colspan(2).width(300).row();
        for (Trade trade : App.getInstance().getCurrentGame().getCurrentPlayer().getGootenTradeList()) {
            inventory.add(new Image(trade.getTrader().getAvatar()));
            inventory.add(new Image(trade.getCustomer().getAvatar()));
            inventory.add(new Label(trade.getSalable() + " *" + trade.getQuantity(), skin));
            inventory.add(new Label(
                trade.getPrice() == null ?
                    trade.getRequiredItem() + " *" + trade.getQuantityRequired() :
                    "Gold *" + trade.getPrice()
                , skin)
            );
            if (trade.getIsAnswered()) {
                inventory.add(new Label(trade.getIsSuccessfulled() == null ? "Unknown" : trade.getIsSuccessfulled() ? "Accepted" : "Rejected", skin)).colspan(2).row();
            } else {
                if (trade.getIShouldAnswer()) {
                    TextButton accept = new TextButton("Accept", skin);
                    TextButton reject = new TextButton("Reject", skin);
                    inventory.add(accept).width(130).padRight(30);
                    inventory.add(reject).width(130).row();
                    accept.addListener(new InputListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            getController().showResponse(TradeService.getInstance().tradeResponse(true, trade.getId()));
                            return true;
                        }
                    });
                    reject.addListener(new InputListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            getController().showResponse(TradeService.getInstance().tradeResponse(false, trade.getId()));
                            return true;
                        }
                    });
                } else {
                    inventory.add(new Label("Pending...", skin)).colspan(2).row();
                }
            }
        }

        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        ImageButton exitButton = provideExitButton(array);

        Table content = new Table();
        content.setFillParent(true);
        content.center();
        content.add(scrollPane).width(window.getWidth() * 0.9f).height(window.getHeight() - 100).padBottom(20).padTop(50).row();


        window.add(content).fill().pad(10);
        Group group = new Group() {
            @Override
            public void act(float delta) {
                window.setPosition(
                    (camera.viewportWidth - window.getWidth()) / 2f +
                        camera.position.x - camera.viewportWidth / 2,
                    (camera.viewportHeight - window.getHeight()) / 2f +
                        camera.position.y - camera.viewportHeight / 2
                );
                exitButton.setPosition(
                    window.getX() + window.getWidth() - exitButton.getWidth() / 2f + 16 - exitButton.getWidth(),
                    window.getY() + window.getHeight()
                );

                super.act(delta);
            }
        };
        group.addActor(window);
        group.addActor(exitButton);
        menuGroup.addActor(group);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                scrollPane.setScrollY(scrollY == null ? scrollPane.getMaxY() : Math.max(0, scrollY));
            }
        }, 0.2f);
    }
}
