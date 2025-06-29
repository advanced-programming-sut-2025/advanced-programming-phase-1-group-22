package io.github.some_example_name.model.animal;

import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import io.github.some_example_name.model.enums.Season;
import io.github.some_example_name.model.products.Product;

@Getter
@ToString
public enum FishType implements Product {
    SALMON("Salmon", 75, Season.FALL, false, GameAsset.SALMON),
    SARDINE("Sardine", 40, Season.FALL, false,GameAsset.SARDINE),
    SHAD("Shad", 60, Season.FALL, false,GameAsset.SHAD),
    BLUE_DISCUS("Blue Discus", 120, Season.FALL, false,GameAsset.BLUE_DISCUS),
    MIDNIGHT_CARP("Midnight Carp", 150, Season.WINTER, false,GameAsset.MIDNIGHT_CARP),
    SQUID("Squid", 80, Season.WINTER, false,GameAsset.SQUID),
    TUNA("Tuna", 100, Season.WINTER, false,GameAsset.TUNA),
    PERCH("Perch", 55, Season.WINTER, false,GameAsset.PERCH),
    FLOUNDER("Flounder", 100, Season.SPRING, false,GameAsset.FLOUNDER),
    LIONFISH("Lionfish", 100, Season.SPRING, false,GameAsset.LIONFISH),
    HERRING("Herring", 30, Season.SPRING, false,GameAsset.HERRING),
    GHOSTFISH("Ghostfish", 45, Season.SPRING, false,GameAsset.GHOSTFISH),
    TILAPIA("Tilapia", 75, Season.SUMMER, false,GameAsset.TILAPIA),
    DORADO("Dorado", 100, Season.SUMMER, false,GameAsset.DORADO),
    SUNFISH("Sunfish", 30, Season.SUMMER, false,GameAsset.SUNFISH),
    RAINBOW_TROUT("Rainbow Trout", 65, Season.SUMMER, false,GameAsset.RAINBOW_TROUT),
    Legend("Legend", 5000, Season.SPRING, true,GameAsset.LEGEND),
    GLACIERFISH("Glacierfish", 1000, Season.WINTER, true,GameAsset.GLACIERFISH),
    ANGLER("Angler", 900, Season.FALL, true,GameAsset.ANGLER),
    CRIMSONFISH("Crimsonfish", 1500, Season.SUMMER, true,GameAsset.CRIMSONFISH);

    private final String name;
    private final Integer price;
    private final Season season;
    private final Boolean isLegendary;
    private final Texture texture;

    FishType(String name, int price, Season season, Boolean isLegendary,Texture texture) {
        this.name = name;
        this.price = price;
        this.season = season;
        this.isLegendary = isLegendary;
        this.texture = texture;
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
