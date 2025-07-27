package io.github.some_example_name.common.model.source;

import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;
import io.github.some_example_name.common.model.products.Product;

@Getter
public enum MineralType implements Product {
    QUARTZ("quartz", "A clear crystal commonly found in caves and mines.", 25, GameAsset.QUARTZ),
    EARTH_CRYSTAL("earth crystal", "A resinous substance found near the surface.", 50,GameAsset.EARTH_CRYSTAL),
    FROZEN_TEAR("frozen tear", "A crystal fabled to be the frozen tears of a yeti.", 75,GameAsset.FROZEN_TEAR),
    FIRE_QUARTZ("fire quartz", "A glowing red crystal commonly found near hot lava.", 100,GameAsset.FIRE_QUARTZ),
    EMERALD("emerald", "A precious stone with a brilliant green color.", 250,GameAsset.EMERALD),
    AQUAMARINE("aquamarine", "A shimmery blue-green gem.", 180,GameAsset.AQUAMARINE),
    RUBY("ruby", "A precious stone that is sought after for its rich color and beautiful luster.", 250,GameAsset.RUBY),
    AMETHYST("amethyst", "A purple variant of quartz.", 100,GameAsset.AMETHYST),
    TOPAZ("topaz", "Fairly common but still prized for its beauty.", 80,GameAsset.TOPAZ),
    JADE("jade", "A pale green ornamental stone.", 200,GameAsset.JADE),
    DIAMOND("diamond", "A rare and valuable gem.", 750,GameAsset.DIAMOND),
    PRISMATIC_SHARED("prismatic shared", "A very rare and powerful substance with unknown origins.", 2_000,GameAsset.PRISMATIC_SHARD),
    COPPER("copper", "A common ore that can be smelted into bars.", 5,GameAsset.COPPER_BAR),
    IRON("iron", "A fairly common ore that can be smelted into bars.", 10,GameAsset.IRON_BAR),
    GOLD("gold", "A precious ore that can be smelted into bars.", 25,GameAsset.GOLD_BAR),
    IRIDIUM("iridium", "An exotic ore with many curious properties. Can be smelted into bars.", 100,GameAsset.IRIDIUM_BAR),
    COAL("coal", "A combustible rock that is useful for crafting and smelting.", 15,GameAsset.COAL),
    COPPER_ORE("copper ore", "A common ore that can be smelted into bars.", 75 / 2,GameAsset.COPPER_ORE),
    IRON_ORE("iron ore", "A fairly common ore that can be smelted into bars.", 150 / 2,GameAsset.IRON_ORE),
    GOLD_ORE("gold ore", "A precious ore that can be smelted into bars.", 400 / 2,GameAsset.GOLD_ORE),
    IRIDIUM_ORE("iridium ore", "", 0,GameAsset.IRIDIUM_ORE),
    FIBER("fiber", "", 0,GameAsset.FIBER),
    WOOD("wood", "", 10 / 2,GameAsset.WOOD),
    STONE("stone", "", 20 / 2,GameAsset.STONE),
    HARD_WOOD("hard wood", "", 0,GameAsset.HARDWOOD);

    private final String name;
    private final String description;
    private final Integer price;
    private final transient Texture texture;

    MineralType(String name, String description, Integer price,Texture texture) {
        this.name = name;
        this.description = description;
        this.price = price;
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
}
