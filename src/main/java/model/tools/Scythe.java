package model.tools;

import lombok.Getter;
import model.products.HarvestAbleProduct;
import model.products.TreesAndFruitsAndSeeds.Fruit;
import model.products.TreesAndFruitsAndSeeds.Tree;
import model.relations.Player;
import model.Tile;
import model.TileType;
import model.abilitiy.Ability;
import model.source.Crop;
import model.structure.Structure;
import utils.App;

import java.util.List;

@Getter
public class Scythe implements Tool {
    private final Integer energyCost = 2;

    @Override
    public void addToolEfficiency(double efficiency) {

    }

    @Override
    public String getName() {
        return "scythe";
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
        if(player.getAbilityLevel(Ability.FARMING) == 4){
            return (int) (App.getInstance().getCurrentGame().getWeatherCoefficient() * energyCost - 1);
        }
        return (int) (App.getInstance().getCurrentGame().getWeatherCoefficient() * energyCost);
    }

    @Override
    public String useTool(Player player, Tile tile) {
        player.changeEnergy(-this.getEnergy(player));
        if (tile.getTileType().equals(TileType.GRASS) && !tile.getIsFilled()){
            tile.setTileType(TileType.FLAT);
            player.upgradeAbility(Ability.FARMING);
            return "you remove the grass";
        }
        List<Structure> structures = App.getInstance().getCurrentGame().getVillage().findStructuresByTile(tile);
        for (Structure structure : structures) {
            if (structure instanceof HarvestAbleProduct){
                if (((HarvestAbleProduct)structure).getBurn()){
                    return "this harvest is burned with thunder! use axe to get its coal";
                }
                if (!((HarvestAbleProduct)structure).canHarvest()){
                    return "you have to wait until complete harvest";
                }
                if (structure instanceof Tree){
                    if (((Tree)structure).getTreeType().getIsForaging()){
                        return "foraging tree does not have fruit";
                    }
                    Fruit fruit = new Fruit(((Tree)structure).getTreeType().getFruit());
                    if (player.getInventory().isInventoryHaveCapacity(fruit)){
                        player.getInventory().addProductToBackPack(fruit,1);
                        ((HarvestAbleProduct)structure).setLastHarvest(App.getInstance().getCurrentGame().getTimeAndDate());
                        player.upgradeAbility(Ability.FARMING);
                        return "you harvest a " + fruit.getName();
                    }
                    return "your inventory is full so you can not harvest";
                }
                Crop crop = new Crop(((Crop)structure).getCropType());
                if (crop.getIsOneTime()){
                    if (player.getInventory().isInventoryHaveCapacity(crop)){
                        player.getInventory().addProductToBackPack(crop,1);
                        App.getInstance().getCurrentGame().getVillage().removeStructure(structure);
                        player.upgradeAbility(Ability.FARMING);
                        return "you harvest a " + crop.getName();
                    }
                    return "your inventory is full so you can not harvest";
                }
                else {
                    if (player.getInventory().isInventoryHaveCapacity(crop)){
                        player.getInventory().addProductToBackPack(crop,1);
                        crop.setLastHarvest(App.getInstance().getCurrentGame().getTimeAndDate());
                        player.upgradeAbility(Ability.FARMING);
                        return "you harvest a " + crop.getName();
                    }
                    return "your inventory is full so you can not harvest";
                }
            }
        }

        return "you use this tool in a wrong way";
    }
}