package model.tools;

import lombok.Getter;
import model.relations.Player;
import model.Tile;
import model.TileType;
import model.abilitiy.Ability;
import model.source.Mineral;
import model.source.MineralType;
import model.structure.Stone;
import model.structure.StoneType;
import model.structure.Structure;
import utils.App;

import java.util.List;

@Getter
public enum Pickaxe implements Tool {
    NORMAL("normal pickaxe",0, 5),
    CUPPER("cupper pickaxe",1, 4),
    IRON("iron pickaxe",2, 3),
    GOLD("gold pickaxe",3, 2),
    IRIDIUM("iridium pickaxe",4, 1);

    private final String name;
    private final int level;
    private final int energyCost;

    Pickaxe(String name,int level1, int energyUse1) {
        this.name = name;
        this.level = level1;
        this.energyCost = energyUse1;
    }

    @Override
    public void addToolEfficiency(double efficiency) {

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
    public int getLevel(){
        return 0;
    }

    @Override
    public Tool getToolByLevel(int level) {
        for (Pickaxe value : Pickaxe.values()) {
            if (value.level == level){
                return value;
            }
        }
        return null;
    }

    @Override
    public int getEnergy(Player player) {
        if (player.getAbilityLevel(Ability.MINING) == 4){
            return energyCost - 1;
        }
        return energyCost;
    }

    @Override
    public String useTool(Player player, Tile tile) {
        boolean success = false;
        if (tile.getTileType().equals(TileType.PLOWED) && !tile.getIsFilled()){
            tile.setTileType(TileType.FLAT);
            success = true;
        }
        List<Structure> structures = App.getInstance().getCurrentGame().getVillage().findStructuresByTile(tile);
        for (Structure structure : structures) {
            if (structure != null) {
                if (structure instanceof Stone){
                    if (((Stone)structure).getStoneType().equals(StoneType.SMALL_STONE)){
                        afterUseTool(new Mineral(MineralType.STONE),player,tile,structure);
                        success = true;
                        break;
                    }
                }
                else if (structure instanceof Mineral && !structure.getIsPickable()){
                    if (((Mineral)structure).getForagingMineralType().equals(MineralType.COPPER_ORE)){
                        afterUseTool(new Mineral(MineralType.COPPER_ORE),player,tile,structure);
                        success = true;
                        break;
                    }
                    else if (((Mineral)structure).getForagingMineralType().equals(MineralType.IRON_ORE)){
                        if (this.level >= 1){
                            afterUseTool(new Mineral(MineralType.IRIDIUM_ORE),player,tile,structure);
                            success = true;
                            break;
                        }
                    }
                    else if (((Mineral)structure).getForagingMineralType().equals(MineralType.IRIDIUM_ORE)){
                        if (this.level >= 3){
                            afterUseTool(new Mineral(MineralType.IRIDIUM_ORE),player,tile,structure);
                            success = true;
                            break;
                        }
                    }
                    else if (((Mineral)structure).getForagingMineralType().equals(MineralType.GOLD_ORE)){
                        if (this.level >= 2){
                            afterUseTool(new Mineral(MineralType.GOLD_ORE),player,tile,structure);
                            success = true;
                            break;
                        }
                    }
                }
                else if (structure.getIsPickable()){
                    App.getInstance().getCurrentGame().getVillage().removeStructure(structure);
                }
            }
        }

        if (success){
            player.upgradeAbility(Ability.MINING);
            player.upgradeAbility(Ability.FORAGING);
            player.changeEnergy(-this.getEnergy(player));
            return "you successfully use this tool";
        }
        else {
            player.changeEnergy(-Math.max(0,this.getEnergy(player) - 1));
            return "you use this tool in a wrong way";
        }
    }

    private void afterUseTool(Mineral mineral, Player player,Tile tile,Structure structure){
        if (player.getInventory().isInventoryHaveCapacity(mineral)){
            player.getInventory().addProductToBackPack(mineral,1);
            tile.setIsFilled(false);
        }
        else {
            mineral.setTiles(List.of(tile));
            mineral.setIsPickable(true);
            App.getInstance().getCurrentGame().getVillage().addStructureToPlayerFarmByPlayerTile(player,mineral);
        }
        App.getInstance().getCurrentGame().getVillage().removeStructure(structure);
	}
}