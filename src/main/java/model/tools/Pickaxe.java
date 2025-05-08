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
    COPPER("copper pickaxe",1, 4),
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
        return this.level;
    }

    @Override
    public Tool getToolByLevel(int level) {
        for (Pickaxe value : Pickaxe.values()) {
            if (value.getLevel() == level){
                return value;
            }
        }
        return null;
    }

    @Override
    public int getEnergy(Player player) {
        if (player.getAbilityLevel(Ability.MINING) == 4){
            return (int) (App.getInstance().getCurrentGame().getWeatherCoefficient() * energyCost - 1);
        }
        return (int) (App.getInstance().getCurrentGame().getWeatherCoefficient() * energyCost);
    }

    @Override
    public String useTool(Player player, Tile tile) {
        if (tile.getTileType().equals(TileType.PLOWED) && !tile.getIsFilled()){
            tile.setTileType(TileType.FLAT);
            player.upgradeAbility(Ability.MINING);
            player.upgradeAbility(Ability.FORAGING);
            player.changeEnergy(-this.getEnergy(player));
            return  "you set tile type from plowed to flat";
        }
        List<Structure> structures = App.getInstance().getCurrentGame().getVillage().findStructuresByTile(tile);
        for (Structure structure : structures) {
            if (structure != null) {
                if (structure instanceof Stone){
                    if (((Stone)structure).getStoneType().equals(StoneType.SMALL_STONE)){
                        Mineral mineral = new Mineral(MineralType.STONE);
                        return breakStone(mineral,player,structure);
                    }
                }
                else if (structure instanceof Mineral){
                    if (((Mineral)structure).getForagingMineralType().equals(MineralType.COPPER_ORE)){
                        Mineral mineral = new Mineral(MineralType.COPPER_ORE);
                        return breakStone(mineral,player,structure);
                    }
                    else if (((Mineral)structure).getForagingMineralType().equals(MineralType.IRON_ORE)){
                        if (this.level >= 1){
                            Mineral mineral = new Mineral(MineralType.IRON_ORE);
                            return breakStone(mineral,player,structure);
                        }
                        player.changeEnergy(-Math.max(0,this.getEnergy(player) - 1));
                        return "your tool have to be at least level 1 to break iron ore";
                    }
                    else if (((Mineral)structure).getForagingMineralType().equals(MineralType.IRIDIUM_ORE)){
                        if (this.level >= 3){
                            Mineral mineral = new Mineral(MineralType.IRIDIUM_ORE);
                            return breakStone(mineral,player,structure);
                        }
                        player.changeEnergy(-Math.max(0,this.getEnergy(player) - 1));
                        return "your tool have to be at least level 3 to break iridium ore";
                    }
                    else if (((Mineral)structure).getForagingMineralType().equals(MineralType.GOLD_ORE)){
                        if (this.level >= 2){
                            Mineral mineral = new Mineral(MineralType.GOLD_ORE);
                            return breakStone(mineral,player,structure);
                        }
                        player.changeEnergy(-Math.max(0,this.getEnergy(player) - 1));
                        return "your tool have to be at least level 2 to break gold ore";
                    }
                    else {
                        if (this.level >= 2){
                            Mineral mineral = new Mineral(((Mineral)structure).getForagingMineralType());
                            return breakStone(mineral,player,structure);
                        }
                        player.changeEnergy(-Math.max(0,this.getEnergy(player) - 1));
                        return "your tool have to be at least level 2 to break jewelry ore";
                    }
                }
                else if (structure.getIsPickable()){
                    App.getInstance().getCurrentGame().getVillage().removeStructure(structure);
                    return "you remove a item";
                }
            }
        }
        player.changeEnergy(-Math.max(0,this.getEnergy(player) - 1));
        return "you use this tool in a wrong way";
    }

    private String breakStone(Mineral mineral,Player player,Structure structure){
        if (player.getInventory().isInventoryHaveCapacity(mineral)){
            player.getInventory().addProductToBackPack(mineral,1);
            player.upgradeAbility(Ability.MINING);
            player.upgradeAbility(Ability.FORAGING);
            player.changeEnergy(-this.getEnergy(player));
            App.getInstance().getCurrentGame().getVillage().removeStructure(structure);
            return  "you break a stone and get " + mineral.getName();
        }
        player.changeEnergy(-Math.max(0,this.getEnergy(player) - 1));
        return  "your inventory is full so you can not break " + mineral.getName();
    }

    @Override
    public Integer getContainingEnergy() {return 0;}
}