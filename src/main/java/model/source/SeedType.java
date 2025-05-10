package model.source;

import lombok.Getter;
import model.enums.Season;
import model.exception.InvalidInputException;

@Getter
public enum SeedType implements Source {
    JAZZ_SEEDS("Jazz Seeds", Season.SPRING, true,37/2),
    CARROT_SEEDS("Carrot Seeds", Season.SPRING, true,5/2),
    CAULIFLOWER_SEEDS("Cauliflower Seeds", Season.SPRING, true,100/2),
    COFFEE_BEAN("Coffee Bean", Season.SPRING, true,200/2),
    GARLIC_SEEDS("Garlic Seeds", Season.SPRING, true,0),
    BEAN_STARTER("Bean Starter", Season.SPRING, true,75/2),
    KALE_SEEDS("Kale Seeds", Season.SPRING, true,87/2),
    PARSNIP_SEEDS("Parsnip Seeds", Season.SPRING, true,25/2),
    POTATO_SEEDS("Potato Seeds", Season.SPRING, true,62/2),
    RHUBARB_SEEDS("Rhubarb Seeds", Season.SPRING, true,100/2),
    STRAWBERRY_SEEDS("Strawberry Seeds", Season.SPRING, true,100/2),
    TULIP_BULB("Tulip Bulb", Season.SPRING, true,25/2),
    RICE_SHOOT("Rice Shoot", Season.SPRING, true,0),
    BLUEBERRY_SEEDS("Blueberry Seeds", Season.SUMMER, true,0),
    CORN_SEEDS("Corn Seeds", Season.SUMMER, true,0),
    HOPS_STARTER("Hops Starter", Season.SUMMER, true,50/2),
    PEPPER_SEEDS("Pepper Seeds", Season.SUMMER, true,52/2),
    MELON_SEEDS("Melon Seeds", Season.SUMMER, true,100/2),
    POPPY_SEEDS("Poppy Seeds", Season.SUMMER, true,125/2),
    RADISH_SEEDS("Radish Seeds", Season.SUMMER, true,50/2),
    RED_CABBAGE_SEEDS("Red Cabbage Seeds", Season.SUMMER, true,0),
    STARFRUIT_SEEDS("Starfruit Seeds", Season.SUMMER, true,400/2),
    SPANGLE_SEEDS("Spangle Seeds", Season.SUMMER, true,62/2),
    SUMMER_SQUASH_SEEDS("Summer Squash Seeds", Season.SUMMER, true,10/2),
    SUNFLOWER_SEEDS("Sunflower Seeds", Season.SUMMER, true,125/2),
    TOMATO_SEEDS("Tomato Seeds", Season.SUMMER, true,62/2),
    WHEAT_SEEDS("Wheat Seeds", Season.SUMMER, true,12/2),
    AMARANTH_SEEDS("Amaranth Seeds", Season.FALL, true,87/2),
    ARTICHOKE_SEEDS("Artichoke Seeds", Season.FALL, true,0),
    BEET_SEEDS("Beet Seeds", Season.FALL, true,20/2),
    BOK_CHOY_SEEDS("Bok Choy Seeds", Season.FALL, true,62/2),
    BROCCOLI_SEEDS("Broccoli Seeds", Season.FALL, true,15/2),
    CRANBERRY_SEEDS("Cranberry Seeds", Season.FALL, true,300/2),
    EGGPLANT_SEEDS("Eggplant Seeds", Season.FALL, true,25/2),
    FAIRY_SEEDS("Fairy Seeds", Season.FALL, true,250/2),
    GRAPE_STARTER("Grape Starter", Season.FALL, true,75/2),
    PUMPKIN_SEEDS("Pumpkin Seeds", Season.FALL, true,125/2),
    YAM_SEEDS("Yam Seeds", Season.FALL, true,75/2),
    RARE_SEED("Rare Seed", Season.FALL, true,1000/2),
    POWDER_MELON_SEEDS("Powdermelon Seeds", Season.WINTER, true,20/2),
    ANCIENT_SEEDS("Ancient Seeds", Season.SPECIAL, true,0),
    MIXED_SEEDS("Mixed Seeds", Season.SPECIAL, true,0),
    APRICOT_SAPLING("Apricot Sapling", Season.SPECIAL, false,0),
    CHERRY_SAPLING("Cherry Sapling", Season.SPECIAL, false,0),
    BANANA_SAPLING("Banana Sapling", Season.SPECIAL, false,0),
    MANGO_SAPLING("Mango Sapling", Season.SPECIAL, false,0),
    ORANGE_SAPLING("Orange Sapling", Season.SPECIAL, false,0),
    PEACH_SAPLING("Peach Sapling", Season.SPECIAL, false,0),
    APPLE_SAPLING("Apple Sapling", Season.SPECIAL, false,0),
    POMEGRANATE_SAPLING("Pomegranate Sapling", Season.SPECIAL, false,0),
    ACORNS("Acorns", Season.SPECIAL, false,0),
    MAPLE_SEEDS("Maple Seeds", Season.SPECIAL, false,0),
    PINE_CONES("Pine cones", Season.SPECIAL, false,0),
    MAHOGANY_SEEDS("Mahogany Seeds", Season.SPECIAL, false,0),
    MUSHROOM_TREE_SEEDS("Mushroom Tree Seeds", Season.SPECIAL, false,0),
    MYSTIC_TREE_SEEDS("Mystic Tree Seeds", Season.SPECIAL, false,0);

    private final String name;
    private final Season season;
    private final boolean isForaging;
    private final Integer price;

    SeedType(String name, Season season, boolean isForaging,int price) {
        this.name = name;
        this.season = season;
        this.isForaging = isForaging;
        this.price = price;
    }

    @Override
    public String getName(){
        return this.name.toLowerCase();
    }

    @Override
    public int getSellPrice() {
        return this.price;
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