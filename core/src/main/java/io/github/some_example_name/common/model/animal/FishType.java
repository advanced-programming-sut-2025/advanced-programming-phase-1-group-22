package io.github.some_example_name.common.model.animal;

import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;
import lombok.ToString;
import io.github.some_example_name.common.model.enums.Season;
import io.github.some_example_name.common.model.products.Product;

@Getter
@ToString
public enum FishType implements Product {
    SALMON("Salmon", 75, Season.FALL, false, GameAsset.SALMON, MiniGameBehavior.MIXED),
    SARDINE("Sardine", 40, Season.FALL, false, GameAsset.SARDINE, MiniGameBehavior.DART),
    SHAD("Shad", 60, Season.FALL, false, GameAsset.SHAD, MiniGameBehavior.SMOOTH),
    BLUE_DISCUS("Blue Discus", 120, Season.FALL, false, GameAsset.BLUE_DISCUS, MiniGameBehavior.DART),
    MIDNIGHT_CARP("Midnight Carp", 150, Season.WINTER, false, GameAsset.MIDNIGHT_CARP, MiniGameBehavior.MIXED),
    SQUID("Squid", 80, Season.WINTER, false, GameAsset.SQUID, MiniGameBehavior.SINKER),
    TUNA("Tuna", 100, Season.WINTER, false, GameAsset.TUNA, MiniGameBehavior.SMOOTH),
    PERCH("Perch", 55, Season.WINTER, false, GameAsset.PERCH, MiniGameBehavior.DART),
    FLOUNDER("Flounder", 100, Season.SPRING, false, GameAsset.FLOUNDER, MiniGameBehavior.SINKER),
    LIONFISH("Lionfish", 100, Season.SPRING, false, GameAsset.LIONFISH, MiniGameBehavior.SMOOTH),
    HERRING("Herring", 30, Season.SPRING, false, GameAsset.HERRING, MiniGameBehavior.DART),
    GHOSTFISH("Ghostfish", 45, Season.SPRING, false, GameAsset.GHOSTFISH, MiniGameBehavior.MIXED),
    TILAPIA("Tilapia", 75, Season.SUMMER, false, GameAsset.TILAPIA, MiniGameBehavior.MIXED),
    DORADO("Dorado", 100, Season.SUMMER, false, GameAsset.DORADO, MiniGameBehavior.MIXED),
    SUNFISH("Sunfish", 30, Season.SUMMER, false, GameAsset.SUNFISH, MiniGameBehavior.MIXED),
    RAINBOW_TROUT("Rainbow Trout", 65, Season.SUMMER, false, GameAsset.RAINBOW_TROUT, MiniGameBehavior.MIXED),
    Legend("Legend", 5000, Season.SPRING, true, GameAsset.LEGEND, MiniGameBehavior.MIXED),
    GLACIERFISH("Glacierfish", 1000, Season.WINTER, true, GameAsset.GLACIERFISH, MiniGameBehavior.MIXED),
    ANGLER("Angler", 900, Season.FALL, true, GameAsset.ANGLER, MiniGameBehavior.SMOOTH),
    CRIMSONFISH("Crimsonfish", 1500, Season.SUMMER, true, GameAsset.CRIMSONFISH, MiniGameBehavior.MIXED);

    private final String name;
    private final Integer price;
    private final Season season;
    private final Boolean isLegendary;
    private final Texture texture;
    private final MiniGameBehavior behavior;

    FishType(String name, int price, Season season, Boolean isLegendary, Texture texture, MiniGameBehavior behavior) {
        this.name = name;
        this.price = price;
        this.season = season;
        this.isLegendary = isLegendary;
        this.texture = texture;
        this.behavior = behavior;
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
    public Integer getContainingEnergy() {
        return getEnergy();
    }
}
