package io.github.some_example_name.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.model.Farm;
import io.github.some_example_name.model.shelter.ShippingBin;
import io.github.some_example_name.model.structure.Structure;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.GameAsset;
import io.github.some_example_name.view.GameView;
import io.github.some_example_name.view.mainMenu.ShippingBinMenu;

public class ShippingBinController {
    private final WorldController worldController = new WorldController();

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
            for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
                for (Structure structure : farm.getStructures()) {
                    if (structure instanceof ShippingBin) {
                        if (collision((ShippingBin) structure, worldX, worldY)) {
                            createShippingBinMenu((ShippingBin) structure);
                        }
                    }
                }
            }
        }
    }

    private boolean collision(ShippingBin shippingBin, float worldX, float worldY) {
        Sprite sprite = shippingBin.getSprite();
        sprite.setPosition(shippingBin.getTiles().get(0).getX() * App.tileWidth, shippingBin.getTiles().get(0).getY() * App.tileHeight);
        return worldX >= sprite.getX() && worldX <= sprite.getX() + sprite.getWidth() && worldY >= sprite.getY() && worldY <= sprite.getY() + sprite.getHeight();
    }

    private void createShippingBinMenu(ShippingBin shippingBin) {
        ShippingBinMenu shippingBinMenu = new ShippingBinMenu(shippingBin);
        shippingBinMenu.createMenu(GameView.stage, GameAsset.SKIN, worldController);
    }
}
