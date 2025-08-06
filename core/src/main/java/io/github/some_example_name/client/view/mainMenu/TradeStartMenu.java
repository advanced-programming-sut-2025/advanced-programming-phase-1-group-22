package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.client.controller.WorldController;
import io.github.some_example_name.common.model.NotificationType;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.server.service.RelationService;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
public class TradeStartMenu extends PopUp {
    private Window window, window2;
    private boolean toggled = false;

    public void createMenu(Stage stage, Skin skin, WorldController worldController) {
        if (TradeMenu.isInitialized()) {
            TradeMenu tradeMenu = TradeMenu.getTradeMenu();
            tradeMenu.createMenu(stage, skin, worldController);
        } else {
            super.createMenu(stage, skin, worldController);
            createInventory(skin, getMenuGroup(), stage);
        }
    }

    @Override
    protected void handleDragRelease(InputEvent event, float x, float y, int pointer, Image itemImage, Salable item, Image dragImage, Boolean flag) {}

    private void createInventory(Skin skin, Group menuGroup, Stage stage) {
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        OrthographicCamera camera = MainGradle.getInstance().getCamera();

        window = new Window("", skin);
        window.setSize(camera.viewportWidth * 0.7f, camera.viewportHeight * 0.6f);
        window.setPosition(
            (camera.viewportWidth - window.getWidth()) / 2f,
            (camera.viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);
        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        ImageButton exitButton = provideExitButton(array);

        Table request = new Table();
        TextButton tradeHistory = new TextButton("Trade History", skin);
        tradeHistory.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                endTask(array, exitButton);
                TradeHistoryMenu tradeHistoryMenu = new TradeHistoryMenu();
                tradeHistoryMenu.createMenu(stage, skin, getController());
                return true;
            }
        });
        List<Player> players = App.getInstance().getCurrentGame().getPlayers();
        request.add(tradeHistory).width(300).colspan(players.size() - 1).row();
        request.add(new Label("Start New Trade", skin)).width(300).colspan(players.size() - 1).row();
        for (Player player : players) {
            if (player.equals(currentPlayer)) continue;
            ImageButton button = new ImageButton(new TextureRegionDrawable(player.getAvatar()));
            request.add(button).width(300f / players.size() - 1);
            button.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    endTask(array, exitButton);
                    TradeMenu tradeMenu = TradeMenu.getTradeMenu();
                    tradeMenu.setFriendship(RelationService.getInstance().getFriendShipBetweenTwoActors(currentPlayer, player));
                    tradeMenu.createMenu(stage, skin, getController());
                    GameClient.getInstance().setTrade(player.getName());
                    player.notify(
                        new Response(currentPlayer.getNickname() + " proposes a negotiation.", true),
                        NotificationType.TRADE, currentPlayer
                    );
                    return true;
                }
            });
        }
        request.pack();
        request.setWidth(window.getWidth()*0.9f);

        Table content = new Table();
        content.setFillParent(true);
        content.add(request).width(window.getWidth()).height(window.getHeight() - 100).padBottom(20).padTop(50).row();


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
    }
}
