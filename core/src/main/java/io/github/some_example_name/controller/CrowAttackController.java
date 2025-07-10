package io.github.some_example_name.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.model.Farm;
import io.github.some_example_name.model.products.HarvestAbleProduct;
import io.github.some_example_name.model.products.TreesAndFruitsAndSeeds.Tree;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.structure.Structure;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.view.CrowAttack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CrowAttackController {
    private CrowAttack crowAttack;

    public void update() {
        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
        Farm farm = getPlayerMainFarm(player);
        if (crowAttack != null) {
            crowAttack.draw(MainGradle.getInstance().getBatch());
            crowAttack.update(Gdx.graphics.getDeltaTime());
            if (crowAttack.isFinished()) {
                if (crowAttack.getHarvestAbleProduct() instanceof Tree tree) tree.setAttackByCrow(true);
                else
                    App.getInstance().getCurrentGame().getVillage().removeStructure(crowAttack.getHarvestAbleProduct());
                crowAttack = null;
            }
        } else {
            if (!farm.getCrowAttackToday()) {
                crowAttack(farm);
            }
        }
    }

    private void crowAttack(Farm farm) {
        List<HarvestAbleProduct> harvestAbleProducts = new ArrayList<>();
        for (Structure structure : farm.getStructures()) {
            if (structure instanceof HarvestAbleProduct harvestAbleProduct) {
                harvestAbleProducts.add(harvestAbleProduct);
            }
        }
        Random random2 = new Random();
        HarvestAbleProduct harvestAbleProduct;
        if (!harvestAbleProducts.isEmpty()) {
            harvestAbleProduct = harvestAbleProducts.get(Math.abs(random2.nextInt()) % harvestAbleProducts.size());
        } else return;

        Random random = new Random();
        if (random.nextInt() % 4 == 0) {
            if (!harvestAbleProduct.getAroundScareCrow() && !harvestAbleProduct.getInGreenHouse()) {
                this.crowAttack = new CrowAttack(new Vector2(harvestAbleProduct.getTiles().get(0).getX() * App.tileWidth,
                    harvestAbleProduct.getTiles().get(0).getY() * App.tileHeight), 4, 10, harvestAbleProduct);
                farm.setCrowAttackToday(true);
            }
        }
    }

    private Farm getPlayerMainFarm(Player player) {
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            if (farm.getPlayers().get(0).equals(player)) {
                return farm;
            }
        }
        return null;
    }
}
