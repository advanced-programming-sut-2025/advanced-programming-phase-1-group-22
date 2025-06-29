package io.github.some_example_name.model.products.TreesAndFruitsAndSeeds;

import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;
import lombok.ToString;
import io.github.some_example_name.model.enums.Season;
import io.github.some_example_name.model.products.Harvestable;
import io.github.some_example_name.model.products.Product;

@Getter
@ToString
public enum FruitType implements Harvestable {
    APRICOT("Apricot", 59, true, 38, Season.SPRING, TreeType.APRICOT_TREE, GameAsset.APRICOT),
    CHERRY("Cherry", 80, true, 38, Season.SPRING, TreeType.CHERRY_TREE,GameAsset.CHERRY),
    BANANA("Banana", 150, true, 75, Season.SUMMER, TreeType.BANANA_TREE,GameAsset.BANANA),
    MANGO("Mango", 130, true, 100, Season.SUMMER, TreeType.MANGO_TREE,GameAsset.MANGO),
    PEACH("Peach", 140, true, 38, Season.SUMMER, TreeType.PEACH_TREE,GameAsset.PEACH),
    ORANGE("Orange", 100, true, 38, Season.SUMMER, TreeType.ORANGE_TREE,GameAsset.ORANGE),
    APPLE("Apple", 100, true, 38, Season.FALL, TreeType.APPLE_TREE,GameAsset.APPLE),
    POMEGRANATE("Pomegranate", 140, true, 38, Season.FALL, TreeType.POMEGRANATE_TREE,GameAsset.POMEGRANATE),
    OAK_RESIN("Oak Resin ", 150, false, 0, Season.SPECIAL, TreeType.OAK_TREE,GameAsset.OAK_RESIN),
    MAPLE_SYRUP("Maple Syrup", 200, false, 0, Season.SPECIAL, TreeType.MAPLE_TREE,GameAsset.MAPLE_SYRUP),
    PINE_TAR("Pine Tar", 100, false, 0, Season.SPECIAL, TreeType.PINE_TREE,GameAsset.PINE_TAR),
    SAP("Sap", 2, true, -2, Season.SPECIAL, TreeType.MAHOGANY_TREE,GameAsset.SAP),
    COMMON_MUSHROOM("Common Mushroom", 40, true, 38, Season.SPECIAL, TreeType.MUSHROOM_TREE,GameAsset.COMMON_MUSHROOM),
    MYSTIC_SYRUP("Mystic Syrup", 1000, true, 500, Season.SPECIAL, TreeType.MYSTIC_TREE,GameAsset.MYSTIC_SYRUP);

    private final TreeType treeType;
    private final String name;
    private final Integer baseSellPrice;
    private final Boolean isEdible;
    private final Integer fruitEnergy;
    private final Season season;
    private final Texture texture;

    FruitType(String name, Integer baseSellPrice, Boolean isEdible, Integer fruitEnergy, Season season, TreeType treeType,Texture texture) {
        this.name = name;
        this.baseSellPrice = baseSellPrice;
        this.isEdible = isEdible;
        this.fruitEnergy = fruitEnergy;
        this.season = season;
        this.treeType = treeType;
        this.texture = texture;
    }
    @Override
    public int getEnergy() {
        return isEdible ? fruitEnergy : 0;
    }

	@Override
	public int getSellPrice() {
		return baseSellPrice;
	}

	@Override
	public String craftInfo() {
		String token = "";
		token += "Name: " + this.name + "\n";
		token += "Source: " + this.treeType.getSource().getName() + "\n";
		token += "Stages: " + "7-7-7-7\n";
		token += "Total Harvest Time: " + "28\n";
		token += "One Time: " + "False\n";
		token += "Regrowth Time: " + this.treeType.getHarvestCycle() + "\n";
		token += "Base Sell Price: " + this.baseSellPrice + "\n";
		token += "IsEdible: " + this.isEdible + "\n";
		token += "Base Energy: " + this.fruitEnergy + "\n";
		token += "Base Health: \n";
		token += "Season: " + this.season.toString().toLowerCase() + "\n";
		token += "Can Become Giant: \n";
		return token;
	}

	@Override
	public Integer getContainingEnergy() {return getEnergy();}
}
