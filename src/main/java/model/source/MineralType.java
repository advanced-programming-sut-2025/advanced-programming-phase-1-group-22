package model.source;

import lombok.Getter;
import lombok.Setter;
import model.products.Product;
@Getter
public enum MineralType implements Product{
    QUARTZ("quartz","A clear crystal commonly found in caves and mines.", 25),
    EARTH_CRYSTAL("earth crystal","A resinous substance found near the surface.", 50),
    FROZEN_TEAR("frozen tear","A crystal fabled to be the frozen tears of a yeti.", 75),
    FIRE_QUARTZ("fire quartz","A glowing red crystal commonly found near hot lava.", 100),
    EMERALD("emerald","A precious stone with a brilliant green color.", 250),
    AQUAMARINE("aquamarine","A shimmery blue-green gem.", 180),
    RUBY("ruby","A precious stone that is sought after for its rich color and beautiful luster.", 250),
    AMETHYST("amethyst","A purple variant of quartz.", 100),
    TOPAZ("topaz","Fairly common but still prized for its beauty.", 80),
    JADE("jade","A pale green ornamental stone.", 200),
    DIAMOND("diamond","A rare and valuable gem.", 750),
    PRISMATIC_SHARED("prismatic shared","A very rare and powerful substance with unknown origins.", 2_000),
    COPPER("copper","A common ore that can be smelted into bars.", 5),
    IRON("iron","A fairly common ore that can be smelted into bars.", 10),
    GOLD("gold","A precious ore that can be smelted into bars.", 25),
    IRIDIUM("iridium","An exotic ore with many curious properties. Can be smelted into bars.", 100),
    COAL("coal","A combustible rock that is useful for crafting and smelting.", 15),
    COPPER_ORE("copper ore","A common ore that can be smelted into bars.", 75 / 2),
    IRON_ORE("iron ore","A fairly common ore that can be smelted into bars.", 150 / 2),
    GOLD_ORE("gold ore","A precious ore that can be smelted into bars.", 400 / 2),
    IRIDIUM_ORE("iridium ore","",0),
    WOOD("wood","",0),
    STONE("stone","",0),
    FIBER("fiber","",0),
    HARD_WOOD("hard wood","",0);

    private final String name;
    private final String description;
    private final Integer price;

    MineralType(String name,String description, Integer price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    @Override
    public int getSellPrice() {
        return price;
    }

    @Override
    public String getName() {
        return this.name.toLowerCase();
    }
}
