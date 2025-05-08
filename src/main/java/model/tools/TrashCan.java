package model.tools;

import lombok.Getter;
import model.relations.Player;
import model.Salable;
import model.Tile;
import model.products.Product;

@Getter
public enum TrashCan implements Tool {
    NORMAL("normal trashcan",0,0),
    COPPER("copper trashcan",1,15),
    IRON("iron trashcan",2,30),
    GOLD("gold trashcan",3,45),
    IRIDIUM("iridium trashcan",4,60);

    private final String name;
    private final Integer level;
    private final Integer prunedValue;

    TrashCan(String name,int level,int prunedValue) {
        this.name = name;
        this.level = level;
        this.prunedValue = prunedValue;
    }

    public void addSalableToTrashCan(Product product){

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
    public Tool getToolByLevel(int level) {
        for (TrashCan value : TrashCan.values()) {
            if (value.getLevel() == level){
                return value;
            }
        }
        return null;
    }

    @Override
    public int getEnergy(Player player) {
        return 0;
    }

    @Override
    public int getLevel(){
        return this.level;
    }

    @Override
    public String useTool(Player player, Tile tile) {
        return "";
    }

    public void givePlayerProductPrice(Player player, Salable salable, int itemNumber){
        int price = salable.getSellPrice() * itemNumber * (this.prunedValue / 100);
        int oldGold = player.getAccount().getGolds();
        player.getAccount().setGolds(oldGold + price);
    }
}