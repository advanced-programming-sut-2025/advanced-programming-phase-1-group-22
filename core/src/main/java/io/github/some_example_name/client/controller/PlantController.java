package io.github.some_example_name.client.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.client.view.GameView;
import io.github.some_example_name.client.view.mainMenu.CropsInfo;
import io.github.some_example_name.client.view.mainMenu.GreenHouseRecipe;
import io.github.some_example_name.common.model.Farm;
import io.github.some_example_name.common.model.Tile;
import io.github.some_example_name.common.model.products.HarvestAbleProduct;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.common.model.structure.farmInitialElements.GreenHouse;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;

import java.util.Objects;

public class PlantController {
    public void update() {
        handleInputs();
    }

    private void handleInputs() {
        Vector3 mouse = new Vector3(GameView.screenX, GameView.screenY, 0);
        MainGradle.getInstance().getCamera().unproject(mouse);
        Vector2 mouseWorld = new Vector2(mouse.x, mouse.y);
        int mouseTileX = (int) Math.floor(mouseWorld.x / App.tileWidth);
        int mouseTileY = (int) Math.floor(mouseWorld.y / App.tileHeight);
        Tile tile = getTileByXAndY(mouseTileX, mouseTileY);
        if (tile == null) return;
        HarvestAbleProduct harvestAbleProduct = getHarvest(tile);
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            if (harvestAbleProduct == null) {
                if (isThereGreenHouse(tile)) {
                    GreenHouseRecipe greenHouseReciepe = new GreenHouseRecipe();
                    greenHouseReciepe.createMenu(GameView.stage, GameAsset.SKIN, WorldController.getInstance());
                }
            } else {
                CropsInfo cropsInfo = new CropsInfo(harvestAbleProduct);
                cropsInfo.createMenu(GameView.stage, GameAsset.SKIN, WorldController.getInstance());
            }
        }
    }

    private Tile getTileByXAndY(int x, int y) {
        for (Tile[] tile : App.getInstance().getCurrentGame().tiles) {
            for (Tile tile1 : tile) {
                if (tile1.getX() == x && tile1.getY() == y) {
                    return tile1;
                }
            }
        }
        return null;
    }

    private boolean isThereGreenHouse(Tile tile) {
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            farm.applyPendingChanges();
            for (Structure structure : farm.getStructuresSnapshot()) {
                if (structure instanceof GreenHouse) {
                    if (structure.getTiles().contains(tile)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private HarvestAbleProduct getHarvest(Tile tile) {
        HarvestAbleProduct harvestAbleProduct = null;
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            farm.applyPendingChanges();
            for (Structure structure : farm.getStructuresSnapshot()) {
                if (structure instanceof HarvestAbleProduct) {
                    if (Objects.equals(structure.getTiles().get(0).getX(), tile.getX()) &&
                        Objects.equals(structure.getTiles().get(0).getY(), tile.getY())) {
                        harvestAbleProduct = (HarvestAbleProduct) structure;
                    }
                }
            }
        }
        return harvestAbleProduct;
    }
}
