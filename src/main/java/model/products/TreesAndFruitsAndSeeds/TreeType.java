package model.products.TreesAndFruitsAndSeeds;

import lombok.Getter;
import lombok.ToString;
import model.enums.Season;
import model.products.Harvestable;
import model.source.SeedType;
import model.source.Source;

import java.util.List;

@Getter
@ToString
public enum TreeType implements Harvestable {
      ACORNS(null, Season.SPECIAL, "Acorns", SeedType.ACORNS, true, null, 0),
     MAPLE_SEEDS(null, Season.SPECIAL, "Maple Seeds", SeedType.MAPLE_SEEDS, true, null, 0),
      PINE_CONES(null, Season.SPECIAL, "Pine cones", SeedType.PINE_CONES, true, null, 0),
     MAHOGANY_SEEDS(null, Season.SPECIAL, "Mahogany Seeds", SeedType.MAHOGANY_SEEDS, true, null, 0),
     MUSHROOM_TREE_SEEDS(null, Season.SPECIAL, "Mushroom Tree Seeds", SeedType.MUSHROOM_TREE_SEEDS, true, null, 0),
    APRICOT_TREE((FruitType.APRICOT), Season.SPRING, "Apricot Tree", SeedType.APRICOT_SAPLING, false, List.of(7, 7, 7, 7), 1),
    CHERRY_TREE((FruitType.CHERRY), Season.SPRING, "Cherry Tree", SeedType.CHERRY_SAPLING, false, List.of(7, 7, 7, 7), 1),
    BANANA_TREE((FruitType.BANANA), Season.SUMMER, "Banana Tree", SeedType.BANANA_SAPLING, false, List.of(7, 7, 7, 7), 1),
    MANGO_TREE((FruitType.MANGO), Season.SUMMER, "Mango Tree", SeedType.MANGO_SAPLING, false, List.of(7, 7, 7, 7), 1),
    ORANGE_TREE((FruitType.ORANGE), Season.SUMMER, "Orange Tree", SeedType.ORANGE_SAPLING, false, List.of(7, 7, 7, 7), 1),
    PEACH_TREE((FruitType.PEACH), Season.SUMMER, "Peach Tree", SeedType.PEACH_SAPLING, false, List.of(7, 7, 7, 7), 1),
    APPLE_TREE((FruitType.APPLE), Season.FALL, "Apple Tree", SeedType.APPLE_SAPLING, false, List.of(7, 7, 7, 7), 1),
    POMEGRANATE_TREE((FruitType.POMEGRANATE), Season.FALL, "Pomegranate Tree", SeedType.POMEGRANATE_SAPLING, false, List.of(7, 7, 7, 7), 1),
    OAK_TREE((FruitType.OAK_RESIN), Season.SPECIAL, "Oak Tree", SeedType.ACORNS, false, List.of(7, 7, 7, 7), 7),
    MAPLE_TREE((FruitType.MAPLE_SYRUP), Season.SPECIAL, "Maple Tree", SeedType.MAPLE_SEEDS, false, List.of(7, 7, 7, 7), 9),
    PINE_TREE((FruitType.PINE_TAR), Season.SPECIAL, "Pine Tree", SeedType.PINE_CONES, false, List.of(7, 7, 7, 7), 5),
    MAHOGANY_TREE((FruitType.SAP), Season.SPECIAL, "Mahogany Tree", SeedType.MAHOGANY_SEEDS, false, List.of(7, 7, 7, 7), 1),
    MUSHROOM_TREE((FruitType.COMMON_MUSHROOM), Season.SPECIAL, "Mushroom Tree", SeedType.MUSHROOM_TREE_SEEDS, false, List.of(7, 7, 7, 7), 1),
    MYSTIC_TREE((FruitType.MYSTIC_SYRUP), Season.SPECIAL, "Mystic Tree", SeedType.MYSTIC_TREE_SEEDS, false, List.of(7, 7, 7, 7), 7);


    private final Boolean IsForaging;
    private final List<Integer> harvestStages;

    private final FruitType fruit;
    private final Season season;
    private final String name;
    private final Source source;
    private final Integer harvestCycle;

    TreeType(FruitType fruit, Season season, String name, Source source, boolean IsForaging, List<Integer> harvestStages, int harvestCycle) {
        this.fruit = fruit;
        this.season = season;
        this.name = name;
        this.source = source;
        this.IsForaging = IsForaging;
        this.harvestStages = harvestStages;
        this.harvestCycle = harvestCycle;
    }

    @Override
    public String craftInfo() {
        String token = "";
        token += "Name: " + this.name + "\n";
        token += "Source: " + this.source.getName() + "\n";
        if (this.harvestStages == null){
            token += "Stages: \n";
            token += "Total Harvest Time: \n";
        }
        else {
            token += "Stages: " + "[7 ,7 ,7 ,7]\n";
            token += "Total Harvest Time: " + "28\n";
        }
        token += "One Time: " + "False\n";
        token += "Regrowth Time: " + this.harvestCycle + "\n";
        if (this.fruit != null){
            token += "Base Sell Price: " + this.fruit.getBaseSellPrice() + "\n";
            token += "IsEdible: " + this.fruit.getIsEdible() + "\n";
            token += "Base Energy: " + this.fruit.getEnergy() + "\n";
        }else {
            token += "Base Sell Price: \n";
            token += "IsEdible: \n";
            token += "Base Energy: \n";
        }
        token += "Base Health: \n";
        token += "Season: " + this.season.toString().toLowerCase() + "\n";
        token += "Can Become Giant: false\n";
        return token;
    }

    @Override
    public int getSellPrice() {
        return 0;
    }

    @Override
    public Integer getContainingEnergy() {
        return Harvestable.super.getContainingEnergy();
    }

    @Override
    public int getEnergy() {
        return 0;
    }
}