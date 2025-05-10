package model.source;

import lombok.Getter;
import model.enums.Season;
import model.exception.InvalidInputException;

@Getter
public enum SeedType implements Source {
    JAZZ_SEEDS("Jazz Seeds", Season.SPRING, true),
    CARROT_SEEDS("Carrot Seeds", Season.SPRING, true),
    CAULIFLOWER_SEEDS("Cauliflower Seeds", Season.SPRING, true),
    COFFEE_BEAN("Coffee Bean", Season.SPRING, true),
    GARLIC_SEEDS("Garlic Seeds", Season.SPRING, true),
    BEAN_STARTER("Bean Starter", Season.SPRING, true),
    KALE_SEEDS("Kale Seeds", Season.SPRING, true),
    PARSNIP_SEEDS("Parsnip Seeds", Season.SPRING, true),
    POTATO_SEEDS("Potato Seeds", Season.SPRING, true),
    RHUBARB_SEEDS("Rhubarb Seeds", Season.SPRING, true),
    STRAWBERRY_SEEDS("Strawberry Seeds", Season.SPRING, true),
    TULIP_BULB("Tulip Bulb", Season.SPRING, true),
    RICE_SHOOT("Rice Shoot", Season.SPRING, true),
    BLUEBERRY_SEEDS("Blueberry Seeds", Season.SUMMER, true),
    CORN_SEEDS("Corn Seeds", Season.SUMMER, true),
    HOPS_STARTER("Hops Starter", Season.SUMMER, true),
    PEPPER_SEEDS("Pepper Seeds", Season.SUMMER, true),
    MELON_SEEDS("Melon Seeds", Season.SUMMER, true),
    POPPY_SEEDS("Poppy Seeds", Season.SUMMER, true),
    RADISH_SEEDS("Radish Seeds", Season.SUMMER, true),
    RED_CABBAGE_SEEDS("Red Cabbage Seeds", Season.SUMMER, true),
    STARFRUIT_SEEDS("Starfruit Seeds", Season.SUMMER, true),
    SPANGLE_SEEDS("Spangle Seeds", Season.SUMMER, true),
    SUMMER_SQUASH_SEEDS("Summer Squash Seeds", Season.SUMMER, true),
    SUNFLOWER_SEEDS("Sunflower Seeds", Season.SUMMER, true),
    TOMATO_SEEDS("Tomato Seeds", Season.SUMMER, true),
    WHEAT_SEEDS("Wheat Seeds", Season.SUMMER, true),
    AMARANTH_SEEDS("Amaranth Seeds", Season.FALL, true),
    ARTICHOKE_SEEDS("Artichoke Seeds", Season.FALL, true),
    BEET_SEEDS("Beet Seeds", Season.FALL, true),
    BOK_CHOY_SEEDS("Bok Choy Seeds", Season.FALL, true),
    BROCCOLI_SEEDS("Broccoli Seeds", Season.FALL, true),
    CRANBERRY_SEEDS("Cranberry Seeds", Season.FALL, true),
    EGGPLANT_SEEDS("Eggplant Seeds", Season.FALL, true),
    FAIRY_SEEDS("Fairy Seeds", Season.FALL, true),
    GRAPE_STARTER("Grape Starter", Season.FALL, true),
    PUMPKIN_SEEDS("Pumpkin Seeds", Season.FALL, true),
    YAM_SEEDS("Yam Seeds", Season.FALL, true),
    RARE_SEED("Rare Seed", Season.FALL, true),
    POWDER_MELON_SEEDS("Powdermelon Seeds", Season.WINTER, true),
    ANCIENT_SEEDS("Ancient Seeds", Season.SPECIAL, true),
    MIXED_SEEDS("Mixed Seeds", Season.SPECIAL, true),
    APRICOT_SAPLING("Apricot Sapling", Season.SPECIAL, false),
    CHERRY_SAPLING("Cherry Sapling", Season.SPECIAL, false),
    BANANA_SAPLING("Banana Sapling", Season.SPECIAL, false),
    MANGO_SAPLING("Mango Sapling", Season.SPECIAL, false),
    ORANGE_SAPLING("Orange Sapling", Season.SPECIAL, false),
    PEACH_SAPLING("Peach Sapling", Season.SPECIAL, false),
    APPLE_SAPLING("Apple Sapling", Season.SPECIAL, false),
    POMEGRANATE_SAPLING("Pomegranate Sapling", Season.SPECIAL, false),
    ACORNS("Acorns", Season.SPECIAL, false),
    MAPLE_SEEDS("Maple Seeds", Season.SPECIAL, false),
    PINE_CONES("Pine cones", Season.SPECIAL, false),
    MAHOGANY_SEEDS("Mahogany Seeds", Season.SPECIAL, false),
    MUSHROOM_TREE_SEEDS("Mushroom Tree Seeds", Season.SPECIAL, false),
    MYSTIC_TREE_SEEDS("Mystic Tree Seeds", Season.SPECIAL, false);

    private final String name;
    private final Season season;
    private final boolean isForaging;

    SeedType(String name, Season season, boolean isForaging) {
        this.name = name;
        this.season = season;
        this.isForaging = isForaging;
    }

    @Override
    public String getName(){
        return this.name.toLowerCase();
    }

    @Override
    public int getSellPrice() {
        return 0;
    }

    public static SeedType getFromName(String name){
        for (SeedType value : SeedType.values()) {
            if (value.name.equals(name)){
                return value;
            }
        }
        throw new InvalidInputException("there is no seed with this name");
    }

    @Override
    public Integer getContainingEnergy() {return 0;}
}