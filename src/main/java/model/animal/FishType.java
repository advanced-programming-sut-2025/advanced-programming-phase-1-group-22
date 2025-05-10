package model.animal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.enums.Season;
import model.products.Product;

@Getter
@ToString
public enum FishType implements Product {
	SALMON("Salmon", 75, Season.FALL, false),
	SARDINE("Sardine", 40, Season.FALL, false),
	SHAD("Shad", 60, Season.FALL, false),
	BLUE_DISCUS("Blue Discus", 120, Season.FALL, false), MIDNIGHT_CARP("Midnight Carp", 150, Season.WINTER, false), SQUID("Squid", 80, Season.WINTER, false),
	TUNA("Tuna", 100, Season.WINTER, false),
	PERCH("Perch", 55, Season.WINTER, false),
	FLOUNDER("Flounder", 100, Season.SPRING, false),
	LIONFISH("Lionfish", 100, Season.SPRING, false),
	HERRING("Herring", 30, Season.SPRING, false),
	GHOSTFISH("Ghostfish", 45, Season.SPRING, false),
	TILAPIA("Tilapia", 75, Season.SUMMER, false),
	DORADO("Dorado", 100, Season.SUMMER, false),
	SUNFISH("Sunfish", 30, Season.SUMMER, false),
	RAINBOW_TROUT("Rainbow Trout", 65, Season.SUMMER, false),
	Legend("Legend", 5000, Season.SPRING, true),
	GLACIERFISH("Glacierfish", 1000, Season.WINTER, true),
	ANGLER("Angler", 900, Season.FALL, true),
	CRIMSONFISH("Crimsonfish", 1500, Season.SUMMER, true);

	private final String name;
	private final Integer price;
	private final Season season;
	private final Boolean isLegendary;

	FishType(String name, int price, Season season, Boolean isLegendary) {
		this.name = name;
		this.price = price;
		this.season = season;
		this.isLegendary = isLegendary;
	}

	@Override
	public int getSellPrice() {
		return this.price;
	}

	@Override
	public int getEnergy() {
		return 0;
	}

	@Override
	public String getName() {
		return this.name.toLowerCase();
	}


	@Override
	public Integer getContainingEnergy() {return getEnergy();}
}
