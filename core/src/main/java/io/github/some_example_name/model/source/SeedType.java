package io.github.some_example_name.model.source;

import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;
import io.github.some_example_name.model.enums.Season;
import io.github.some_example_name.model.exception.InvalidInputException;

@Getter
public enum SeedType implements Source {
    JAZZ_SEEDS("Jazz Seeds", Season.SPRING, true,37/2, GameAsset.JAZZ_SEEDS),
    CARROT_SEEDS("Carrot Seeds", Season.SPRING, true,5/2,GameAsset.CARROT_SEEDS),
    CAULIFLOWER_SEEDS("Cauliflower Seeds", Season.SPRING, true,100/2,GameAsset.CAULIFLOWER_SEEDS),
    COFFEE_BEAN("Coffee Bean", Season.SPRING, true,200/2,GameAsset.COFFEE_BEAN),
    GARLIC_SEEDS("Garlic Seeds", Season.SPRING, true,0,GameAsset.GARLIC_SEEDS),
    BEAN_STARTER("Bean Starter", Season.SPRING, true,75/2,GameAsset.BEAN_STARTER),
    KALE_SEEDS("Kale Seeds", Season.SPRING, true,87/2,GameAsset.KALE_SEEDS),
    PARSNIP_SEEDS("Parsnip Seeds", Season.SPRING, true,25/2,GameAsset.PARSNIP_SEEDS),
    POTATO_SEEDS("Potato Seeds", Season.SPRING, true,62/2,GameAsset.POTATO_SEEDS),
    RHUBARB_SEEDS("Rhubarb Seeds", Season.SPRING, true,100/2,GameAsset.RHUBARB_SEEDS),
    STRAWBERRY_SEEDS("Strawberry Seeds", Season.SPRING, true,100/2,GameAsset.STRAWBERRY_SEEDS),
    TULIP_BULB("Tulip Bulb", Season.SPRING, true,25/2,GameAsset.TULIP_BULB),
    RICE_SHOOT("Rice Shoot", Season.SPRING, true,0,GameAsset.RICE_SHOOT),
    BLUEBERRY_SEEDS("Blueberry Seeds", Season.SUMMER, true,0,GameAsset.BLUEBERRY_SEEDS),
    CORN_SEEDS("Corn Seeds", Season.SUMMER, true,0, GameAsset.CORN_SEEDS),
    HOPS_STARTER("Hops Starter", Season.SUMMER, true,50/2,GameAsset.HOPS_STARTER),
    PEPPER_SEEDS("Pepper Seeds", Season.SUMMER, true,52/2,GameAsset.PEPPER_SEEDS),
    MELON_SEEDS("Melon Seeds", Season.SUMMER, true,100/2,GameAsset.MELON_SEEDS),
    POPPY_SEEDS("Poppy Seeds", Season.SUMMER, true,125/2,GameAsset.POPPY_SEEDS),
    RADISH_SEEDS("Radish Seeds", Season.SUMMER, true,50/2,GameAsset.RADISH_SEEDS),
    RED_CABBAGE_SEEDS("Red Cabbage Seeds", Season.SUMMER, true,0,GameAsset.RED_CABBAGE_SEEDS),
    STARFRUIT_SEEDS("Starfruit Seeds", Season.SUMMER, true,400/2,GameAsset.STARFRUIT_SEEDS),
    SPANGLE_SEEDS("Spangle Seeds", Season.SUMMER, true,62/2,GameAsset.SPRING_SEEDS),
    SUMMER_SQUASH_SEEDS("Summer Squash Seeds", Season.SUMMER, true,10/2,GameAsset.SUMMER_SQUASH_SEEDS),
    SUNFLOWER_SEEDS("Sunflower Seeds", Season.SUMMER, true,125/2,GameAsset.SUNFLOWER_SEEDS),
    TOMATO_SEEDS("Tomato Seeds", Season.SUMMER, true,62/2,GameAsset.TOMATO_SEEDS),
    WHEAT_SEEDS("Wheat Seeds", Season.SUMMER, true,12/2,GameAsset.WHEAT_SEEDS),
    AMARANTH_SEEDS("Amaranth Seeds", Season.FALL, true,87/2,GameAsset.AMARANTH_SEEDS),
    ARTICHOKE_SEEDS("Artichoke Seeds", Season.FALL, true,0,GameAsset.ARTICHOKE_SEEDS),
    BEET_SEEDS("Beet Seeds", Season.FALL, true,20/2,GameAsset.BEET_SEEDS),
    BOK_CHOY_SEEDS("Bok Choy Seeds", Season.FALL, true,62/2,GameAsset.BOK_CHOY_SEEDS),
    BROCCOLI_SEEDS("Broccoli Seeds", Season.FALL, true,15/2,GameAsset.BROCCOLI_SEEDS),
    CRANBERRY_SEEDS("Cranberry Seeds", Season.FALL, true,300/2,GameAsset.CRANBERRY_SEEDS),
    EGGPLANT_SEEDS("Eggplant Seeds", Season.FALL, true,25/2,GameAsset.EGGPLANT_SEEDS),
    FAIRY_SEEDS("Fairy Seeds", Season.FALL, true,250/2,GameAsset.FAIRY_SEEDS),
    GRAPE_STARTER("Grape Starter", Season.FALL, true,75/2,GameAsset.GRAPE_STARTER),
    PUMPKIN_SEEDS("Pumpkin Seeds", Season.FALL, true,125/2,GameAsset.PUMPKIN_SEEDS),
    YAM_SEEDS("Yam Seeds", Season.FALL, true,75/2,GameAsset.YAM_SEEDS),
    RARE_SEED("Rare Seed", Season.FALL, true,1000/2,GameAsset.RARE_SEED),
    POWDER_MELON_SEEDS("Powdermelon Seeds", Season.WINTER, true,20/2,GameAsset.POWDERMELON_SEEDS),
    ANCIENT_SEEDS("Ancient Seeds", Season.SPECIAL, true,0,GameAsset.ANCIENT_SEEDS),
    MIXED_SEEDS("Mixed Seeds", Season.SPECIAL, true,0,GameAsset.MIXED_SEEDS),
    APRICOT_SAPLING("Apricot Sapling", Season.SPECIAL, false,0,GameAsset.APRICOT_SAPLING),
    CHERRY_SAPLING("Cherry Sapling", Season.SPECIAL, false,0,GameAsset.CHERRY_SAPLING),
    BANANA_SAPLING("Banana Sapling", Season.SPECIAL, false,0,GameAsset.BANANA_SAPLING),
    MANGO_SAPLING("Mango Sapling", Season.SPECIAL, false,0,GameAsset.MANGO_SAPLING),
    ORANGE_SAPLING("Orange Sapling", Season.SPECIAL, false,0,GameAsset.ORANGE_SAPLING),
    PEACH_SAPLING("Peach Sapling", Season.SPECIAL, false,0,GameAsset.PEACH_SAPLING),
    APPLE_SAPLING("Apple Sapling", Season.SPECIAL, false,0,GameAsset.APPLE_SAPLING),
    POMEGRANATE_SAPLING("Pomegranate Sapling", Season.SPECIAL, false,0,GameAsset.POMEGRANATE_SAPLING),
    ACORNS("Acorns", Season.SPECIAL, false,0,GameAsset.ACORN),
    MAPLE_SEEDS("Maple Seeds", Season.SPECIAL, false,0,GameAsset.MAPLE_SEED),
    PINE_CONES("Pine cones", Season.SPECIAL, false,0,GameAsset.PINE_CONE),
    MAHOGANY_SEEDS("Mahogany Seeds", Season.SPECIAL, false,0,GameAsset.MAHOGANY_SEED),
    MUSHROOM_TREE_SEEDS("Mushroom Tree Seeds", Season.SPECIAL, false,0,GameAsset.MUSHROOM_TREE_SEED),
    MYSTIC_TREE_SEEDS("Mystic Tree Seeds", Season.SPECIAL, false,0,GameAsset.MYSTIC_TREE_SEED);

    private final String name;
    private final Season season;
    private final boolean isForaging;
    private final Integer price;
    private final Texture texture;

    SeedType(String name, Season season, boolean isForaging,int price,Texture texture) {
        this.name = name;
        this.season = season;
        this.isForaging = isForaging;
        this.price = price;
        this.texture = texture;
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
