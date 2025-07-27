package io.github.some_example_name.common.model.tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.common.model.StructureUpdateState;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;
import io.github.some_example_name.common.model.TimeAndDate;
import io.github.some_example_name.common.model.products.HarvestAbleProduct;
import io.github.some_example_name.common.model.products.TreesAndFruitsAndSeeds.Fruit;
import io.github.some_example_name.common.model.products.TreesAndFruitsAndSeeds.Tree;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.Tile;
import io.github.some_example_name.common.model.TileType;
import io.github.some_example_name.common.model.abilitiy.Ability;
import io.github.some_example_name.common.model.source.Crop;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.common.utils.App;

import java.util.List;

@Getter
public class Scythe implements Tool {
    private final Integer energyCost = 2;
    private final Texture texture = GameAsset.SCYTHE;
    private final Sprite sprite = new Sprite(GameAsset.SCYTHE);

    public Scythe() {
        this.sprite.setSize(App.tileWidth, App.tileHeight);
    }

    @Override
    public void addToolEfficiency(double efficiency) {

    }

    @Override
    public String getName() {
        return "scythe";
    }

    @Override
    public Integer getContainingEnergy() {
        return 0;
    }

    @Override
    public int getSellPrice() {
        return 0;
    }

    @Override
    public Tool getToolByLevel(int level) {
        return null;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getEnergy(Player player) {
        int minus = 0;
        if (player.getAbilityLevel(Ability.FARMING) == 4) {
            minus += 1;
        }
        if (player.getBuffAbility() != null && player.getBuffAbility().equals(Ability.FARMING)) {
            minus += 1;
        }
        return (int) (App.getInstance().getCurrentGame().getWeatherCoefficient() * energyCost - minus);
    }

    @Override
    public String useTool(Player player, Tile tile) {
        player.changeEnergy(-this.getEnergy(player));
        if (tile.getTileType().equals(TileType.GRASS) && !tile.getIsFilled()) {
            tile.setTileType(TileType.FLAT);
            GameClient.getInstance().updateTileState(tile);
            player.upgradeAbility(Ability.FARMING);
            return "you remove the grass";
        }
        List<Structure> structures = App.getInstance().getCurrentGame().getVillage().findStructuresByTile(tile);
        for (Structure structure : structures) {
            if (structure instanceof HarvestAbleProduct) {
                if (((HarvestAbleProduct) structure).getBurn()) {
                    return "this harvest is burned with thunder! use axe to get its coal";
                }
                if (!((HarvestAbleProduct) structure).canHarvest()) {
                    return "you have to wait until complete harvest";
                }
                if (structure instanceof Tree) {
                    if (((Tree) structure).getTreeType().getIsForaging()) {
                        return "foraging tree does not have fruit";
                    }
                    if (((Tree) structure).getIsBroken()) {
                        return "this tree is broken, use axe to get its wood";
                    }
                    Fruit fruit = new Fruit(((Tree) structure).getTreeType().getFruit());
                    if (player.getInventory().isInventoryHaveCapacity(fruit)) {
                        player.getInventory().addProductToBackPack(fruit, 1);
                        TimeAndDate timeAndDate = new TimeAndDate();
                        timeAndDate.setDay(App.getInstance().getCurrentGame().getTimeAndDate().getTotalDays());
                        ((HarvestAbleProduct) structure).setLastHarvest(timeAndDate);
                        player.upgradeAbility(Ability.FARMING);
                        return "you harvest a " + fruit.getName();
                    }
                    return "your inventory is full so you can not harvest";
                }
                Crop crop = new Crop(((Crop) structure).getCropType());
                if (crop.getIsOneTime()) {
                    if (player.getInventory().isInventoryHaveCapacity(crop)) {
                        int numberOfHarvest = ((Crop) structure).getIsGiant() ? 10 : 1;
                        player.getInventory().addProductToBackPack(crop, numberOfHarvest);
                        App.getInstance().getCurrentGame().getVillage().removeStructure(structure);
                        GameClient.getInstance().updateStructureState(structure, StructureUpdateState.DELETE, true, null);
                        tile.setTileType(TileType.FLAT);
                        GameClient.getInstance().updateTileState(tile);
                        player.upgradeAbility(Ability.FARMING);
                        if (numberOfHarvest == 10) {
                            return "you harvest a giant crop so you got 10 " + crop.getName();
                        }
                        return "you harvest a " + crop.getName();
                    }
                    return "your inventory is full so you can not harvest";
                } else {
                    if (player.getInventory().isInventoryHaveCapacity(crop)) {
                        int numberOfHarvest = ((Crop) structure).getIsGiant() ? 10 : 1;
                        player.getInventory().addProductToBackPack(crop, numberOfHarvest);
                        TimeAndDate timeAndDate = new TimeAndDate();
                        timeAndDate.setDay(App.getInstance().getCurrentGame().getTimeAndDate().getTotalDays());
                        ((HarvestAbleProduct) structure).setLastHarvest(timeAndDate);
                        player.upgradeAbility(Ability.FARMING);
                        if (numberOfHarvest == 10) {
                            return "you harvest a giant crop so you got 10 " + crop.getName();
                        }
                        return "you harvest a " + crop.getName();
                    }
                    return "your inventory is full so you can not harvest";
                }
            }
        }
        return "you use this tool in a wrong way";
    }
}
