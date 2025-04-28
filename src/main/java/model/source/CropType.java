package model.source;

import lombok.Getter;
import model.enums.Season;
import model.products.Harvestable;

import java.util.List;

@Getter
public enum CropType implements Harvestable {
	BLUE_JAZZ("Blue Jazz", (SeedType.JAZZ_SEEDS),
			List.of(1, 2, 2, 2), true, 0, 50, true, 45, List.of(Season.SPRING), false, false),
	CARROT("Carrot", (SeedType.CARROT_SEEDS),
			List.of(1, 1, 1), true, 0, 35, true, 75, List.of(Season.SPRING), false, false),
	CAULIFLOWER("Cauliflower", (SeedType.CAULIFLOWER_SEEDS),
			List.of(1, 2, 4, 4, 1), true, 0, 175, true, 75, List.of(Season.SPRING), true, false),
	COFFEE_BEAN("Coffee Bean", (SeedType.COFFEE_BEAN),
			List.of(1, 2, 2, 3, 2), false, 2, 15, false, 0, List.of(Season.SPRING, Season.SUMMER), false, false),
	GARLIC("Garlic", (SeedType.GARLIC_SEEDS),
			List.of(1, 1, 1, 1), true, 0, 60, true, 20, List.of(Season.SPRING), false, false),
	GREEN_BEAN("Green Bean", (SeedType.BEAN_STARTER),
			List.of(1, 1, 1, 3, 4), false, 3, 40, true, 25, List.of(Season.SPRING), false, false),
	KALE("Kale", (SeedType.KALE_SEEDS),
			List.of(1, 2, 2, 1), true, 0, 110, true, 50, List.of(Season.SPRING), false, false),
	PARSNIP("Parsnip", (SeedType.PARSNIP_SEEDS),
			List.of(1, 1, 1, 1), true, 0, 35, true, 25, List.of(Season.SPRING), false, false),
	POTATO("Potato", (SeedType.POTATO_SEEDS),
			List.of(1, 1, 1, 2, 1), true, 0, 80, true, 25, List.of(Season.SPRING), false, false),
	RHUBARB("Rhubarb", (SeedType.RHUBARB_SEEDS),
			List.of(2, 2, 2, 3, 4), true, 0, 220, false, 0, List.of(Season.SPRING), false, false),
	STRAWBERRY("Strawberry", (SeedType.STRAWBERRY_SEEDS),
			List.of(1, 1, 2, 2, 2), false, 4, 120, true, 50, List.of(Season.SPRING), false, false),
	TULIP("Tulip", (SeedType.TULIP_BULB),
			List.of(1, 1, 2, 2), true, 0, 30, true, 45, List.of(Season.SPRING), false, false),
	UNMILLED_RICE("Unmilled Rice", (SeedType.RICE_SHOOT),
			List.of(1, 2, 2, 3), true, 0, 30, true, 3, List.of(Season.SPRING), false, false),
	BLUEBERRY("Blueberry", (SeedType.BLUEBERRY_SEEDS),
			List.of(1, 3, 3, 4, 2), false, 4, 50, true, 25, List.of(Season.SUMMER), false, false),
	CORN("Corn", (SeedType.CORN_SEEDS),
			List.of(2, 3, 3, 3, 3), false, 4, 50, true, 25, List.of(Season.SUMMER, Season.FALL), false, false),
	HOPS("Hops", (SeedType.HOPS_STARTER),
			List.of(1, 1, 2, 3, 4), false, 1, 25, true, 45, List.of(Season.SUMMER), false, false),
	HOT_PEPPER("Hot Pepper", (SeedType.PEPPER_SEEDS),
			List.of(1, 1, 1, 1, 1), false, 3, 40, true, 13, List.of(Season.SUMMER), false, false),
	MELON("Melon", (SeedType.MELON_SEEDS),
			List.of(1, 2, 3, 3, 3), true, 0, 250, true, 113, List.of(Season.SUMMER), true, false),
	POPPY("Poppy", (SeedType.POPPY_SEEDS),
			List.of(1, 2, 2, 2), true, 0, 140, true, 45, List.of(Season.SUMMER), false, false),
	RADISH("Radish", (SeedType.RADISH_SEEDS),
			List.of(2, 1, 2, 1), true, 0, 90, true, 45, List.of(Season.SUMMER), false, false),
	RED_CABBAGE("Red Cabbage", (SeedType.RED_CABBAGE_SEEDS),
			List.of(2, 1, 2, 2, 2), true, 0, 260, true, 75, List.of(Season.SUMMER), false, false),
	STARFRUIT("Starfruit", (SeedType.STARFRUIT_SEEDS),
			List.of(2, 3, 2, 3, 3), true, 0, 750, true, 125, List.of(Season.SUMMER), false, false),
	SUMMER_SPANGLE("Summer Spangle", (SeedType.SPANGLE_SEEDS),
			List.of(1, 2, 3, 1), true, 0, 90, true, 45, List.of(Season.SUMMER), false, false),
	SUMMER_SQUASH("Summer Squash", (SeedType.SUMMER_SQUASH_SEEDS),
			List.of(1, 1, 1, 2, 1), false, 3, 45, true, 63, List.of(Season.SUMMER), false, false),
	SUNFLOWER("Sunflower", (SeedType.SUNFLOWER_SEEDS),
			List.of(1, 2, 3, 2), true, 0, 80, true, 45, List.of(Season.SUMMER, Season.FALL), false, false),
	TOMATO("Tomato", (SeedType.TOMATO_SEEDS),
			List.of(2, 2, 2, 2, 3), false, 4, 60, true, 20, List.of(Season.SUMMER), false, false),
	WHEAT("Wheat", (SeedType.WHEAT_SEEDS),
			List.of(1, 1, 1, 1), true, 0, 25, false, 0, List.of(Season.SUMMER, Season.FALL), false, false),
	AMARANTH("Amaranth", (SeedType.AMARANTH_SEEDS),
			List.of(1, 2, 2, 2), true, 0, 150, true, 50, List.of(Season.FALL), false, false),
	ARTICHOKE("Artichoke", (SeedType.ARTICHOKE_SEEDS),
			List.of(2, 2, 1, 2, 1), true, 0, 160, true, 30, List.of(Season.FALL), false, false),
	BEET("Beet", (SeedType.BEET_SEEDS),
			List.of(1, 1, 2, 2), true, 0, 100, true, 30, List.of(Season.FALL), false, false),
	BOK_CHOY("Bok Choy", (SeedType.BOK_CHOY_SEEDS),
			List.of(1, 1, 1, 1), true, 0, 80, true, 25, List.of(Season.FALL), false, false),
	BROCCOLI("Broccoli", (SeedType.BROCCOLI_SEEDS),
			List.of(2, 2, 2, 2), false, 4, 70, true, 63, List.of(Season.FALL), false, false),
	CRANBERRIES("Cranberries", (SeedType.CRANBERRY_SEEDS),
			List.of(1, 2, 1, 1, 2), false, 5, 75, true, 38, List.of(Season.FALL), false, false),
	EGGPLANT("Eggplant", (SeedType.EGGPLANT_SEEDS),
			List.of(1, 1, 1, 1), false, 5, 60, true, 20, List.of(Season.FALL), false, false),
	FAIRY_ROSE("Fairy Rose", (SeedType.FAIRY_SEEDS),
			List.of(1, 4, 4, 3), true, 0, 290, true, 45, List.of(Season.FALL), false, false),
	GRAPE("Grape", (SeedType.GRAPE_STARTER),
			List.of(1, 1, 2, 3, 3), false, 3, 80, true, 38, List.of(Season.FALL), false, false),
	PUMPKIN("Pumpkin", (SeedType.PUMPKIN_SEEDS),
			List.of(1, 2, 3, 4, 3), true, 0, 320, false, 0, List.of(Season.FALL), true, false),
	YAM("Yam", (SeedType.YAM_SEEDS),
			List.of(1, 3, 3, 3), true, 0, 160, true, 45, List.of(Season.FALL), false, false),
	SWEET_GEM_BERRY("Sweet Gem Berry", (SeedType.RARE_SEED),
			List.of(2, 4, 6, 6, 6), true, 0, 3000, false, 0, List.of(Season.FALL), false, false),
	POWDERMELON("Powdermelon", (SeedType.POWDER_MELON_SEEDS),
			List.of(1, 2, 1, 2, 1), true, 0, 60, true, 63, List.of(Season.WINTER), true, false),
	ANCIENT_FRUIT("Ancient Fruit", (SeedType.ANCIENT_SEEDS),
			List.of(2, 7, 7, 7, 5), false, 7, 550, false, 0, List.of(Season.SPRING, Season.SUMMER, Season.FALL), false, false),
	COMMON_MUSHROOM("Common Mushroom", null, null, true, 0, 40, true, 38, List.of(Season.SPECIAL), false, true),
	DAFFODIL("Daffodil", null, null, true, 0, 30, false, 0, List.of(Season.SPRING), false, true),
	DANDELION("Dandelion", null, null, true, 0, 40, true, 25, List.of(Season.SPRING), false, true),
	LEEK("Leek", null, null, true, 0, 60, true, 40, List.of(Season.SPRING), false, true),
	MOREL("Morel", null, null, true, 0, 150, true, 20, List.of(Season.SPRING), false, true),
	SALMONBERRY("Salmonberry", null, null, true, 0, 5, true, 25, List.of(Season.SPRING), false, true),
	SPRING_ONION("Spring Onion", null, null, true, 0, 8, true, 13, List.of(Season.SPRING), false, true),
	WILD_HORSERADISH("Wild Horseradish", null, null, true, 0, 50, true, 13, List.of(Season.SPRING), false, true),
	FIDDLEHEAD_FERN("Fiddlehead Fern", null, null, true, 0, 90, true, 25, List.of(Season.SUMMER), false, true),
	RED_MUSHROOM("Red Mushroom", null, null, true, 0, 75, true, -50, List.of(Season.SUMMER), false, true),
	SPICE_BERRY("Spice Berry", null, null, true, 0, 80, true, 25, List.of(Season.SUMMER), false, true),
	SWEET_PEA("Sweet Pea", null, null, true, 0, 50, false, 0, List.of(Season.SUMMER), false, true),
	BLACKBERRY("Blackberry", null, null, true, 0, 25, true, 25, List.of(Season.FALL), false, true),
	CHANTERELLE("Chanterelle", null, null, true, 0, 160, true, 75, List.of(Season.FALL), false, true),
	HAZELNUT("Hazelnut", null, null, true, 0, 40, true, 38, List.of(Season.FALL), false, true),
	PURPLE_MUSHROOM("Purple Mushroom", null, null, true, 0, 90, true, 30, List.of(Season.FALL), false, true),
	WILD_PLUM("Wild Plum", null, null, true, 0, 80, true, 25, List.of(Season.FALL), false, true),
	CROCUS("Crocus", null, null, true, 0, 60, false, 0, List.of(Season.WINTER), false, true),
	CRYSTAL_FRUIT("Crystal Fruit", null, null, true, 0, 150, true, 63, List.of(Season.WINTER), false, true),
	HOLLY("Holly", null, null, true, 0, 80, true, -37, List.of(Season.WINTER), false, true),
	SNOW_YAM("Snow Yam", null, null, true, 0, 100, true, 30, List.of(Season.WINTER), false, true),
	WINTER_ROOT("Winter Root", null, null, true, 0, 70, true, 25, List.of(Season.WINTER), false, true);

	private final String name;
	private final Source source;
	private final List<Integer> harvestStages;
	private final boolean oneTime;
	private final int regrowthTime;
	private final int baseSellPrice;
	private final boolean isEdible;
	private final int energy;
	private final List<Season> seasons;
	private final boolean canBecomeGiant;
	private final boolean isForaging;

	CropType(String name, Source source, List<Integer> harvestStages, boolean oneTime, int regrowthTime,
			 int baseSellPrice, boolean isEdible, int energy, List<Season> seasons, boolean canBecomeGiant,
			 boolean isForaging) {
		this.name = name;
		this.source = source;
		if (harvestStages!=null) {
			this.harvestStages = List.copyOf(harvestStages);
		} else {
			this.harvestStages = null;
		}
		this.oneTime = oneTime;
		this.regrowthTime = regrowthTime;
		this.baseSellPrice = baseSellPrice;
		this.isEdible = isEdible;
		this.energy = energy;
		this.seasons = List.copyOf(seasons);
		this.canBecomeGiant = canBecomeGiant;
		this.isForaging = isForaging;
	}

	@Override
	public int getSellPrice() {
		return baseSellPrice;
	}

	@Override
	public String getName() {
		return this.name.toLowerCase();
	}

	@Override
	public String craftInfo() {
		StringBuilder token = new StringBuilder();
		token.append("Name: ").append(this.name).append("\n");
		token.append("Source: ");
		if (this.source != null){
			token.append("Source: ").append(this.getSource().getName());
		}
		token.append("\n");
		token.append("Stages: ");
		if (this.harvestStages != null){
			for (int i = 0; i < harvestStages.size(); i++) {
				token.append(harvestStages);
				if (i != harvestStages.size() - 1){
					token.append("-");
				}
			}
		}
		token.append("\n");
		token.append("Total Harvest Time: ");
		if (this.harvestStages != null){
			int total = 0;
			for (Integer harvestStage : harvestStages) {
				total += harvestStage;
			}
			token.append(total);
		}
		token.append("\n");
		token.append("One Time: ").append(this.oneTime).append("\n");
		token.append("Regrowth Time: ").append(this.regrowthTime).append("\n");
		token.append("Base Sell Price: ").append(this.baseSellPrice).append("\n");
		token.append("IsEdible: ").append(this.isEdible).append("\n");
		token.append("Base Energy: ").append(this.energy).append("\n");
		token.append("Base Health: \n");
		token.append("Season: ");
		for (Season season : this.seasons) {
			token.append(season).append(" ");
		}
		token.append("\n");
		token.append("Can Become Giant: ").append(this.canBecomeGiant).append("\n");
		return token.toString();
	}
}