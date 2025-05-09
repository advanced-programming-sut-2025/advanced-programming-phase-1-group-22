package model.products.TreesAndFruitsAndSeeds;

import lombok.Getter;
import lombok.ToString;
import model.enums.Season;
import model.products.Harvestable;
import model.products.Product;

import java.io.Serializable;

@Getter
@ToString
public enum FruitType implements Harvestable , Serializable {
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
	private TreeType treeType;
	private String name;
	private Integer baseSellPrice;
	private Boolean isEdible;
	private Integer fruitEnergy;
	private Season season;

	FruitType() {
	}

	FruitType(String name, Integer baseSellPrice, Boolean isEdible, Integer fruitEnergy, Season season, TreeType treeType) {
		this.name = name;
		this.baseSellPrice = baseSellPrice;
		this.isEdible = isEdible;
		this.fruitEnergy = fruitEnergy;
		this.season = season;
		this.treeType = treeType;
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