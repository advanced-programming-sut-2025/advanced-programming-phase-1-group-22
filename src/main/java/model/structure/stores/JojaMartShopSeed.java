package model.structure.stores;

import lombok.Getter;
import model.enums.Season;
import model.source.SeedType;

import java.util.List;
@Getter
public enum JojaMartShopSeed {
	PARSNIP_SEEDS(SeedType.PARSNIP_SEEDS,List.of(Season.SPRING),25,5),
	BEAN_STARTER(SeedType.BEAN_STARTER,List.of(Season.SPRING),75,5),
	CAULIFLOWER_SEEDS(SeedType.CAULIFLOWER_SEEDS,List.of(Season.SPRING),100,5),
	POTATO_SEEDS(SeedType.POTATO_SEEDS,List.of(Season.SPRING),62,5),
	STRAWBERRY_SEEDS(SeedType.STRAWBERRY_SEEDS,List.of(Season.SPRING),100,5),
	TULIP_BULB(SeedType.TULIP_BULB,List.of(Season.SPRING),25,5),
	KALE_SEEDS(SeedType.KALE_SEEDS,List.of(Season.SPRING),87,5),
	COFFEE_BEANS(SeedType.COFFEE_BEAN,List.of(Season.SPRING,Season.SUMMER),200,1),
	CARROT_SEEDS(SeedType.CARROT_SEEDS,List.of(Season.SPRING),5,10),
	RHUBARB_SEEDS(SeedType.RHUBARB_SEEDS,List.of(Season.SPRING),100,5),
	JAZZ_SEEDS(SeedType.JAZZ_SEEDS,List.of(Season.SPRING),37,5),
	TOMATO_SEEDS(SeedType.TOMATO_SEEDS, List.of(Season.SUMMER), 62, 5),
	PEPPER_SEEDS(SeedType.PEPPER_SEEDS, List.of(Season.SUMMER), 50, 5),
	WHEAT_SEEDS(SeedType.WHEAT_SEEDS, List.of(Season.SUMMER,Season.FALL), 12, 10),
	SUMMER_SQUASH_SEEDS(SeedType.SUMMER_SQUASH_SEEDS, List.of(Season.SUMMER), 10, 10),
	RADISH_SEEDS(SeedType.RADISH_SEEDS, List.of(Season.SUMMER), 50, 5),
	MELON_SEEDS(SeedType.MELON_SEEDS, List.of(Season.SUMMER), 100, 5),
	HOPS_STARTER(SeedType.HOPS_STARTER, List.of(Season.SUMMER), 75, 5),
	POPPY_SEEDS(SeedType.POPPY_SEEDS, List.of(Season.SUMMER), 125, 5),
	SPANGLE_SEEDS(SeedType.SPANGLE_SEEDS, List.of(Season.SUMMER), 62, 5),
	STARFRUIT_SEEDS(SeedType.STARFRUIT_SEEDS, List.of(Season.SUMMER), 400, 5),
	SUNFLOWER_SEEDS(SeedType.SUNFLOWER_SEEDS, List.of(Season.SUMMER,Season.FALL), 125, 5),
	CORN_SEEDS(SeedType.CORN_SEEDS, List.of(Season.FALL), 187, 5),
	EGGPLANT_SEEDS(SeedType.EGGPLANT_SEEDS, List.of(Season.FALL), 25, 5),
	PUMPKIN_SEEDS(SeedType.PUMPKIN_SEEDS, List.of(Season.FALL), 125, 5),
	BROCCOLI_SEEDS(SeedType.BROCCOLI_SEEDS, List.of(Season.FALL), 15, 5),
	AMARANTH_SEEDS(SeedType.AMARANTH_SEEDS, List.of(Season.FALL), 87, 5),
	GRAPE_STARTER(SeedType.GRAPE_STARTER, List.of(Season.FALL), 75, 5),
	BEET_SEEDS(SeedType.BEET_SEEDS, List.of(Season.FALL), 20, 5),
	YAM_SEEDS(SeedType.YAM_SEEDS, List.of(Season.FALL), 75, 5),
	BOK_CHOY_SEEDS(SeedType.BOK_CHOY_SEEDS, List.of(Season.FALL), 62, 5),
	CRANBERRY_SEEDS(SeedType.CRANBERRY_SEEDS, List.of(Season.FALL), 300, 5),
	FAIRY_SEEDS(SeedType.FAIRY_SEEDS, List.of(Season.FALL), 250, 5),
	RARE_SEED(SeedType.RARE_SEED, List.of(Season.FALL), 1000, 1),
	POWDERMELON_SEEDS(SeedType.POWDERMELON_SEEDS, List.of(Season.WINTER), 20, 10);

	private final SeedType seedType;
	private final List<Season> seasons;
	private final Integer price;
	private final Integer dailyLimit;

	JojaMartShopSeed(SeedType seedType, List<Season> seasons, Integer price, Integer dailyLimit) {
		this.seedType = seedType;
		this.seasons = seasons;
		this.price = price;
		this.dailyLimit = dailyLimit;
	}
}
