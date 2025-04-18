package model.products.TreesAndFruitsAndSeeds;

import lombok.Getter;
import lombok.ToString;
import model.enums.Season;
import model.products.Product;

@Getter
@ToString
public enum FruitType implements Product {
    APRICOT("Apricot", 59, true, 38, Season.SPRING, TreeType.APRICOT_TREE),
    CHERRY("Cherry", 80, true, 38, Season.SPRING, TreeType.CHERRY_TREE),
    BANANA("Banana", 150, true, 75, Season.SUMMER, TreeType.BANANA_TREE),
    MANGO("Mango", 130, true, 100, Season.SUMMER, TreeType.MANGO_TREE),
    PEACH("Peach", 140, true, 38, Season.SUMMER, TreeType.PEACH_TREE),
    ORANGE("Orange", 100, true, 38, Season.SUMMER, TreeType.ORANGE_TREE),
    APPLE("Apple", 100, true, 38, Season.FALL, TreeType.APPLE_TREE),
    POMEGRANATE("Pomegranate", 140, true, 38, Season.FALL, TreeType.POMEGRANATE_TREE),
    OAK_RESIN("Oak Resin ", 150, false, 0, Season.SPECIAL, TreeType.OAK_TREE),
    MAPLE_SYRUP("Maple Syrup", 200, false, 0, Season.SPECIAL, TreeType.MAPLE_TREE),
    PINE_TAR("Pine Tar", 100, false, 0, Season.SPECIAL, TreeType.PINE_TREE),
    SAP("Sap", 2, true, -2, Season.SPECIAL, TreeType.MAHOGANY_TREE),
    COMMON_MUSHROOM("Common Mushroom", 40, true, 38, Season.SPECIAL, TreeType.MUSHROOM_TREE),
    MYSTIC_SYRUP("Mystic Syrup", 1000, true, 500, Season.SPECIAL, TreeType.MYSTIC_TREE),
    ;
    private final TreeType treeType;
    private final String Name;
    private final Integer baseSellPrice;
    private final Boolean isEdible;
    private final Integer fruitEnergy;
    private final Season season;

    FruitType(String name, Integer baseSellPrice, Boolean isEdible, Integer fruitEnergy, Season season, TreeType treeType) {
        Name = name;
        this.baseSellPrice = baseSellPrice;
        this.isEdible = isEdible;
        this.fruitEnergy = fruitEnergy;
        this.season = season;
        this.treeType = treeType;
    }


    @Override
    public int getSellPrice() {
        return baseSellPrice;
    }
}
