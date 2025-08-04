package io.github.some_example_name.client.view.mainMenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.client.controller.WorldController;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.products.HarvestAbleProduct;
import io.github.some_example_name.common.model.products.TreesAndFruitsAndSeeds.Tree;
import io.github.some_example_name.common.model.source.Crop;

import java.util.ArrayList;

public class CropsInfo extends PopUp {
    private final HarvestAbleProduct harvestAbleProduct;

    public CropsInfo(HarvestAbleProduct harvestAbleProduct) {
        this.harvestAbleProduct = harvestAbleProduct;
    }

    @Override
    public void createMenu(Stage stage, Skin skin, WorldController playerController) {
        super.createMenu(stage, skin, playerController);
        Window window = new Window("", skin);
        window.setSize(900, 700);
        window.setPosition(
            (MainGradle.getInstance().getCamera().viewportWidth - window.getWidth()) / 2f,
            (MainGradle.getInstance().getCamera().viewportHeight - window.getHeight()) / 2f
        );
        window.setMovable(false);

        Table info = new Table();
        fillTable(info, harvestAbleProduct);

        ArrayList<Actor> array = new ArrayList<>();
        array.add(window);
        ImageButton exitButton = provideExitButton(array);

        Table content = new Table();
        content.setFillParent(true);
        content.add(info).width(600).height(600).padTop(20).expand().top();
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

    private void fillTable(Table table, HarvestAbleProduct harvestAbleProduct) {
        if (harvestAbleProduct instanceof Crop crop) {
            table.add(new Label("Name: " + crop.getCropType().getName(), skin)).row();
            if (crop.getCropType().getSource() != null) {
                table.add(new Label("Source: " + crop.getCropType().getSource().getName(), skin)).row();
            }
            if (crop.getCropType().getHarvestStages() != null) {
                table.add(new Label("Stage: " + crop.getCropType().getHarvestStages(), skin)).row();
            }
            if (crop.getCropType().getHarvestStages() != null) {
                int total = 0;
                for (Integer harvestStage : crop.getCropType().getHarvestStages()) {
                    total += harvestStage;
                }
                table.add(new Label("Total Harvest Time: " + total, skin)).row();
            }
            table.add(new Label("One Time: " + crop.getCropType().isOneTime(), skin)).row();
            table.add(new Label("Regrowth Time: " + crop.getCropType().getRegrowthTime(), skin)).row();
            table.add(new Label("Base Sell Price: " + crop.getCropType().getBaseSellPrice(), skin)).row();
            table.add(new Label("IsEdible: " + crop.getCropType().isEdible(), skin)).row();
            table.add(new Label("Base Energy: " + crop.getCropType().getEnergy(), skin)).row();
            table.add(new Label("Season: " + crop.getCropType().getSeasons(), skin)).row();
            table.add(new Label("Can Become Giant: " + crop.getCropType().isCanBecomeGiant(), skin)).row();
        } else if (harvestAbleProduct instanceof Tree tree) {
            table.add(new Label("name: " + tree.getTreeType().getName(), skin)).row();
            table.add(new Label("Source: " + tree.getTreeType().getSource().getName(), skin)).row();
            if (tree.getTreeType().getHarvestStages() != null) {
                table.add(new Label("Stage: [7 ,7 ,7 ,7]", skin)).row();
                table.add(new Label("Total Harvest Time: 28", skin)).row();
            }
            table.add(new Label("One Time: false", skin)).row();
            table.add(new Label("Regrowth Time: " + tree.getTreeType().getHarvestCycle(), skin)).row();
            if (tree.getTreeType().getFruit() != null) {
                table.add(new Label("Base Sell Price: " + tree.getTreeType().getFruit().getBaseSellPrice(), skin)).row();
                table.add(new Label("IsEdible: " + tree.getTreeType().getFruit().getIsEdible(), skin)).row();
                table.add(new Label("Base Energy: " + tree.getTreeType().getFruit().getEnergy(), skin)).row();
            }
            table.add(new Label("Season: " + tree.getTreeType().getSeason().toString().toLowerCase(), skin)).row();
            table.add(new Label("Can Become Giant: false", skin)).row();
        }
    }

    @Override
    protected void handleDragRelease(InputEvent event, float x, float y, int pointer, Image itemImage, Salable item, Image dragImage, Boolean flag) {
    }
}
