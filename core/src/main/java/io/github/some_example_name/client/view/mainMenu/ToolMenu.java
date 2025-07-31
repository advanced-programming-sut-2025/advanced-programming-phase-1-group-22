package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.client.controller.WorldController;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.tools.Tool;
import io.github.some_example_name.common.model.tools.WateringCan;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;

import java.util.ArrayList;
import java.util.Map;

public class ToolMenu extends PopUp {

    @Override
    public void createMenu(Stage stage, Skin skin, WorldController playerController) {
        super.createMenu(stage, skin, playerController);
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        Window window = new Window("", skin);
        window.setSize(700, 500);
        window.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);

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

        java.util.List<Tool> tools = new ArrayList<>();
        synchronized (currentPlayer.getInventory().getProducts()){
            for (Map.Entry<Salable, Integer> salableIntegerEntry : currentPlayer.getInventory().getProducts().entrySet()) {
                if (salableIntegerEntry.getKey() instanceof Tool) {
                    tools.add((Tool) salableIntegerEntry.getKey());
                }
            }
        }

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 6; col++) {
                Image slot = new Image(slotTexture);

                int index = row * 6 + col;
                if (index < tools.size()) {
                    Salable item = tools.get(index);
                    Image itemImage = new Image(item.getTexture());
                    ArrayList<ScrollPane> list = new ArrayList<>();
                    list.add(scrollPane);
                    addDrag(itemImage, stage, currentPlayer, item, list, true);
                    itemImage.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            if (getTapCount() == 2) {
                                currentPlayer.setCurrentCarrying(item);
                                GameClient.getInstance().updatePlayerCarryingObject(currentPlayer);
                                refreshInventory(stage, inventory, currentPlayer, slotTexture, currentPlayer, trashCan, scrollPane);
                            }
                        }
                    });
                    if (item == currentPlayer.getCurrentCarrying()) itemImage.setColor(1, 1, 1, 1f);
                    else itemImage.setColor(1, 1, 1, 0.5f);
                    itemImage.setSize(90, 90);
                    Stack stack = new Stack();
                    if (item instanceof WateringCan wateringCan) {
                        ProgressBar progressBar = new ProgressBar(0, wateringCan.getWateringCanType().getCapacity(), 1, false, GameAsset.SKIN);
                        progressBar.setValue(wateringCan.getRemain());
                        progressBar.setAnimateDuration(0.2f);
                        progressBar.setSize(80, 5);
                        progressBar.setTouchable(Touchable.disabled);
                        progressBar.setColor(Color.BLUE);

                        Table group = new Table();
                        group.add(itemImage).size(90, 90).row();
                        group.add(progressBar).width(80).height(8).padTop(4);

                        stack.add(slot);
                        stack.add(group);
                    } else {
                        stack.add(slot);
                        stack.add(itemImage);
                    }
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

    private void refreshInventory(Stage stage, Table inventory, Player player, Texture slotTexture, Player currentPlayer, ImageButton trashCan, ScrollPane scrollPane) {
        inventory.clear();
        java.util.List<Tool> tools = new ArrayList<>();
        synchronized (currentPlayer.getInventory().getProducts()){
            for (Map.Entry<Salable, Integer> salableIntegerEntry : currentPlayer.getInventory().getProducts().entrySet()) {
                if (salableIntegerEntry.getKey() instanceof Tool) {
                    tools.add((Tool) salableIntegerEntry.getKey());
                }
            }
        }

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 6; col++) {
                Image slot = new Image(slotTexture);
                int index = row * 6 + col;

                if (index < tools.size()) {
                    Salable item = tools.get(index);
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
                                GameClient.getInstance().updatePlayerCarryingObject(player);
                                refreshInventory(stage, inventory, currentPlayer, slotTexture, currentPlayer, trashCan, scrollPane);
                            }
                        }
                    });
                    if (item == currentPlayer.getCurrentCarrying()) itemImage.setColor(1, 1, 1, 1f);
                    else itemImage.setColor(1, 1, 1, 0.5f);
                    Stack stack = new Stack();
                    if (item instanceof WateringCan wateringCan) {
                        ProgressBar progressBar = new ProgressBar(0, wateringCan.getWateringCanType().getCapacity(), 1, false, GameAsset.SKIN);
                        progressBar.setValue(wateringCan.getRemain());
                        progressBar.setAnimateDuration(0.2f);
                        progressBar.setSize(80, 5);
                        progressBar.setTouchable(Touchable.disabled);
                        progressBar.setColor(Color.BLUE);

                        Table group = new Table();
                        group.add(itemImage).size(90, 90).row();
                        group.add(progressBar).width(80).height(8).padTop(4);

                        stack.add(slot);
                        stack.add(group);
                    } else {
                        stack.add(slot);
                        stack.add(itemImage);
                    }
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
    }
}
