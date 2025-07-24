package io.github.some_example_name.client.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.common.model.Farm;
import io.github.some_example_name.common.model.Tile;
import io.github.some_example_name.common.model.abilitiy.Ability;
import io.github.some_example_name.common.model.animal.Fish;
import io.github.some_example_name.common.model.products.ProductQuality;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.common.model.structure.farmInitialElements.Lake;
import io.github.some_example_name.common.model.tools.FishingPole;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.client.view.MiniGame;

public class FishingController {
    private final WorldController worldController = new WorldController();
    private MiniGame miniGame;

    public void update() {
        if (miniGame != null) {
            miniGame.draw(MainGradle.getInstance().getBatch(), Gdx.graphics.getDeltaTime());
            if (miniGame.isGameOver()) {
                if (miniGame.isGameWin()) {
                    App.getInstance().getCurrentGame().getCurrentPlayer().upgradeAbility(Ability.FISHING);
                    if (!miniGame.isShowWinFish()) {
                        getFish(miniGame);
                        if (miniGame.isFishingPerfect())
                            worldController.showResponse(new Response("This Fishing was perfect!", true));
                        else worldController.showResponse(new Response("You win and get a fish", true));
                        miniGame = null;
                    }
                } else {
                    worldController.showResponse(new Response("You lose the fish"));
                    miniGame = null;
                }
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) miniGame = null;
        } else if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector3 worldCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            MainGradle.getInstance().getCamera().unproject(worldCoords);
            float worldX = worldCoords.x;
            float worldY = worldCoords.y;
            for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
                for (Structure structure : farm.getStructures()) {
                    if (structure instanceof Lake lake) {
                        if (collision(lake, worldX, worldY)) {
                            fishing();
                        }
                    }
                }
            }
        }
    }

    private void getFish(MiniGame miniGame) {
        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
        Fish fish = miniGame.getFish();
        if (miniGame.isFishingPerfect()) {
            if (!fish.getProductQuality().equals(ProductQuality.NORMAL)) {
                int level = fish.getProductQuality().getLevel();
                fish.setProductQuality(ProductQuality.getByLevel(Math.min(4, level + 1)));
            }
            player.getAbilities().compute(Ability.FISHING, (k, fishingXp) -> (int) (fishingXp * 2.4));
        }
        int count = miniGame.getFishingPole().generateNumberOfFish(player);
        player.getInventory().addProductToBackPack(fish, count);
    }

    private void fishing() {
        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
        if (player.getCurrentCarrying() instanceof FishingPole fishingPole) {
            player.changeEnergy(-fishingPole.getEnergy(player));
            Fish fish = fishingPole.fishing(player);
            if (fishingPole.canFish(player, fish)) {
                miniGame = new MiniGame(fish, fishingPole, MainGradle.getInstance().getCamera());
            } else {
                worldController.showResponse(new Response("You have not enough energy Or your inventory is full"));
            }
        } else {
            worldController.showResponse(new Response("You have to carry a fishing pole"));
        }
    }

    private boolean collision(Structure structure, float worldX, float worldY) {
        Tile tile = getTileByXAndY((int) (worldX / App.tileWidth), (int) (worldY / App.tileHeight));
        if (tile == null) return false;
        return structure.getTiles().contains(tile);
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
}
