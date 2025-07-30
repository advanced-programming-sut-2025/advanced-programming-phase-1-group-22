package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.client.controller.WorldController;
import io.github.some_example_name.common.model.Farm;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.dto.TradePriceDto;
import io.github.some_example_name.common.model.dto.TradeProductDto;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.model.relations.Friendship;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.tools.BackPack;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;
import io.github.some_example_name.server.service.RelationService;
import io.github.some_example_name.server.service.TradeService;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Setter
public class TradeMenu extends PopUp {
    private Friendship friendship;
    private Player friend;
    private Window window, window2;
    private boolean toggled = false;

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
    protected void handleDragRelease(InputEvent event, float x, float y, int pointer, Image itemImage, Salable item, Image dragImage, Boolean flag) {}

    private void createInventory(Skin skin, Group menuGroup, Stage stage) {
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

        Table request = new Table();
        Table fridge = new Table();
        Table info = new Table();
        request.pack();
        request.setWidth(window.getWidth()*0.9f);


        Label requestLabel = new Label(toggled ? "Offer" : "Request", skin);
        TextArea reqProduct = new TextArea("", skin);
        TextArea reqProductCount = new TextArea("0", skin);
        TextArea reqGold = new TextArea("Gold", skin);
        reqGold.setDisabled(true);
        TextArea reqGoldCount = new TextArea("0", skin);
        request.add(requestLabel).colspan(4).row();
        request.add(reqProduct).width(250).padRight(50);
        request.add(reqProductCount).width(50).padRight(300);
        request.add(reqGold).width(250).padRight(50);
        request.add(reqGoldCount).width(50).row();


        window2.setSize(camera.viewportWidth * 0.7f, camera.viewportHeight * 0.35f);
        window2.setPosition(
            (camera.viewportWidth - window2.getWidth()) / 2f,
            (camera.viewportHeight - window2.getHeight()) / 2f
        );
        fridge.pack();
        fridge.setWidth(window2.getWidth()*0.9f);


        Label offerLabel = new Label(toggled ? "Request" : "Offer", skin);
        TextArea offerProduct = new TextArea("", skin);
        TextArea offerProductCount = new TextArea("0", skin);
        fridge.add(offerLabel).colspan(2).row();
        fridge.add(offerProduct).width(250).padRight(50);
        fridge.add(offerProductCount).width(50).row();

        info.pack();
        TextButton type = new TextButton("Toggle Offer/Request", skin);
        TextButton apply = new TextButton("Send trade request!", skin);
        info.add(type).width(400).row();
        info.add(apply).width(400).row();

        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        array.add(window2);
        ImageButton exitButton = provideExitButton(array);

        type.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                toggled = !toggled;
                endTask(array, exitButton);
                createInventory(skin, menuGroup, stage);
                return true;
            }
        });
        apply.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                int reqProCount = 0, reqGolCount = 0, offerProCount = 0;
                try {
                    reqProCount = reqProductCount.getText().isEmpty() ? 0 : Integer.parseInt(reqProductCount.getText());
                    reqGolCount = reqGoldCount.getText().isEmpty() ? 0 : Integer.parseInt(reqGoldCount.getText());
                    offerProCount = offerProductCount.getText().isEmpty() ? 0 : Integer.parseInt(offerProductCount.getText());
                } catch (NumberFormatException e) {
                    getController().showResponse(new Response("Request counts must be integers"));
                    return true;
                }
                if (reqGolCount * reqProCount != 0) {
                    getController().showResponse(new Response("You can choose only gold or product; not both"));
                    return true;
                }
                Response resp;
                if (toggled) {
                    if (reqGolCount == 0) {
                        resp = TradeService.getInstance().tradeProductRequest(new TradeProductDto(
                            friend.getName(),
                            "request",
                            offerProduct.getText(),
                            offerProCount,
                            reqProduct.getText(),
                            reqProCount
                        ));
                    } else {
                        resp = TradeService.getInstance().tradePriceRequest(new TradePriceDto(
                            friend.getName(),
                            "request",
                            offerProduct.getText(),
                            offerProCount,
                            reqGolCount
                        ));
                    }
                } else {
                    if (reqGolCount == 0) {
                        resp = TradeService.getInstance().tradeProductOffer(new TradeProductDto(
                            friend.getName(),
                            "request",
                            offerProduct.getText(),
                            offerProCount,
                            reqProduct.getText(),
                            reqProCount
                        ));
                    } else {
                        resp = TradeService.getInstance().tradePriceOffer(new TradePriceDto(
                            friend.getName(),
                            "request",
                            offerProduct.getText(),
                            offerProCount,
                            reqGolCount
                        ));
                    }
                }
                getController().showResponse(resp);
                if (resp.shouldBeBack()) {
                    offerProductCount.setText("0");
                    reqGoldCount.setText("0");
                    reqProductCount.setText("0");
                    offerProduct.clear();
                    reqGold.clear();
                    reqProduct.clear();
                    endTask(array, exitButton);
                    createInventory(skin, menuGroup, stage);
                }
                return true;
            }
        });


        Table content = new Table();
        content.setFillParent(true);
        content.add(request).width(window.getWidth()).height(window.getHeight() - 100).padBottom(20).padTop(50).row();


        Table content2 = new Table();
        content2.setFillParent(true);
        content2.add(fridge).width(0.9f * window2.getWidth() - 500).height(window.getHeight() - 100).padRight(70).padBottom(20).padTop(50);
        content2.add(info).width(450).height(window2.getHeight() - 100).padBottom(20).padTop(50).row();


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
                super.act(delta);
            }
        };
        group.addActor(window);
        group.addActor(window2);
        group.addActor(exitButton);
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
