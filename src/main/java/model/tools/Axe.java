package model.tools;

import lombok.Getter;
import model.relations.Player;
import model.Salable;
import model.Tile;
import model.abilitiy.Ability;
import model.products.TreesAndFruitsAndSeeds.Tree;
import model.source.*;
import model.structure.Structure;
import model.structure.Trunk;
import model.structure.TrunkType;
import utils.App;

import java.util.List;

@Getter
public enum Axe implements Tool{
    NORMAL("normal axe",0, 5),
    CUPPER("cupper axe",1, 4),
    IRON("iron axe",2, 3),
    GOLD("gold axe",3, 2),
    IRIDIUM("iridium axe",4, 1);

    private final String name;
    private final int level;
    private final int energyCost;

    Axe(String name,int level1, int energyUse1) {
        this.name = name;
        this.level = level1;
        this.energyCost = energyUse1;
    }

    @Override
    public void addToolEfficiency(double efficiency) {

    }

    @Override
    public Tool getToolByLevel(int level) {
        for (Axe value : Axe.values()) {
            if (value.level == level){
                return value;
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return this.name.toLowerCase();
    }

    @Override
    public int getSellPrice() {
        return 0;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getEnergy(Player player) {
        if (player.getAbilityLevel(Ability.FORAGING) == 4){
            return energyCost - 1;
        }
        return energyCost;
    }

    @Override
    public String useTool(Player player, Tile tile) {
        boolean success = false;
        List<Structure> structures = App.getInstance().getCurrentGame().getVillage().findStructuresByTile(tile);
        for (Structure structure : structures) {
            if (structure != null){
                if (structure instanceof Trunk){
                    if (((Trunk)structure).getTrunkType().equals(TrunkType.SMALL_TRUNK)){
                        afterUseToolForMineralOrTree(new Mineral(MineralType.WOOD),player,tile,structure);
                        success = true;
                        break;
                    }
                }
                if (structure instanceof Tree){
                    if (((Tree)structure).getTreeType().getIsForaging()){
                        afterUseToolForMineralOrTree(new Seed(SeedType.getFromName(((Tree)structure).getTreeType().getName())),
                                player,tile,structure);
                        afterUseToolForMineralOrTree(new Mineral(MineralType.WOOD),player,tile,structure);
                        success = true;
                        break;
                    }
                }
            }
        }

        if (success){
            player.upgradeAbility(Ability.FORAGING);
            player.changeEnergy(-this.getEnergy(player));
            return "you successfully use this tool";
        }
        player.changeEnergy(-Math.max(0,this.getEnergy(player) - 1));
        return "you use this tool in a wrong way";
    }

    private void afterUseToolForMineralOrTree(Structure mineralOrSeed, Player player, Tile tile, Structure structure){
        if (player.getInventory().isInventoryHaveCapacity((Salable) mineralOrSeed)){
            player.getInventory().addProductToBackPack((Salable) mineralOrSeed,1);
            tile.setIsFilled(false);
        }
        else {
            mineralOrSeed.setTiles(List.of(tile));
            mineralOrSeed.setIsPickable(true);
            App.getInstance().getCurrentGame().getVillage().addStructureToPlayerFarmByPlayerTile(player, mineralOrSeed);
        }
        App.getInstance().getCurrentGame().getVillage().removeStructure(structure);
    }
}