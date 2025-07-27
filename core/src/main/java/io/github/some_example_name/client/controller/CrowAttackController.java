package io.github.some_example_name.client.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.common.model.Farm;
import io.github.some_example_name.common.model.StructureUpdateState;
import io.github.some_example_name.common.model.products.HarvestAbleProduct;
import io.github.some_example_name.common.model.products.TreesAndFruitsAndSeeds.Tree;
import io.github.some_example_name.common.model.source.Crop;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.client.view.CrowAttack;

import java.util.*;

public class CrowAttackController {
    private final Map<Farm, CrowAttack> crowAttacks = new HashMap<>();

    public CrowAttackController(){
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            crowAttacks.put(farm,null);
        }
    }

    public void update() {
//        for (Map.Entry<Farm, CrowAttack> farmCrowAttackEntry : crowAttacks.entrySet()) {
//            Farm farm = farmCrowAttackEntry.getKey();
//            CrowAttack crowAttack = farmCrowAttackEntry.getValue();
//            if (crowAttack != null) {
//                crowAttack.draw(MainGradle.getInstance().getBatch());
//                crowAttack.update(Gdx.graphics.getDeltaTime());
//                if (crowAttack.isFinished()) {
//                    if (crowAttack.getHarvestAbleProduct() instanceof Tree tree) {
//                        tree.setAttackByCrow(true);
//                        GameClient.getInstance().updateStructureState(tree, StructureUpdateState.UPDATE, true, tree.getTiles().get(0));
//                    } else {
//                        Crop crop = (Crop) crowAttack.getHarvestAbleProduct();
//                        App.getInstance().getCurrentGame().getVillage().removeStructure(crop);
//                        GameClient.getInstance().updateStructureState(crop, StructureUpdateState.UPDATE, true, crop.getTiles().get(0));
//                    }
//                    crowAttacks.put(farm, null);
//                }
//            } else {
//                if (!farm.getCrowAttackToday()) {
//                    crowAttack(farm);
//                }
//            }
//        }
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
                if (harvestAbleProduct.getTiles().isEmpty()) return;
                CrowAttack crowAttack = new CrowAttack(new Vector2(harvestAbleProduct.getTiles().get(0).getX() * App.tileWidth,
                    harvestAbleProduct.getTiles().get(0).getY() * App.tileHeight), 4, 10, harvestAbleProduct);
                farm.setCrowAttackToday(true);
                crowAttacks.put(farm, crowAttack);
                GameClient.getInstance().updateFarmCrowAttack(farm, true);
            }
        }
    }
}
