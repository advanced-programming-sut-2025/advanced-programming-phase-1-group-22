package io.github.some_example_name.common.model.source;

import com.badlogic.gdx.graphics.Texture;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;
import io.github.some_example_name.common.model.enums.Season;
import io.github.some_example_name.common.model.products.Harvestable;

import java.util.List;

@Getter
public enum CropType implements Harvestable {
    BLUE_JAZZ("Blue Jazz", (SeedType.JAZZ_SEEDS), List.of(1, 2, 2, 2), true, 0, 50,
        true, 45, List.of(Season.SPRING), false, false, List.of(GameAsset.BLUE_JAZZ_STAGE_1, GameAsset.BLUE_JAZZ_STAGE_2, GameAsset.BLUE_JAZZ_STAGE_3, GameAsset.BLUE_JAZZ_STAGE_4, GameAsset.BLUE_JAZZ_STAGE_5), null),
    CARROT("Carrot", (SeedType.CARROT_SEEDS), List.of(1, 1, 1), true, 0, 35,
        true, 75, List.of(Season.SPRING), false, false, List.of(GameAsset.CARROT_STAGE_1, GameAsset.CARROT_STAGE_2, GameAsset.CARROT_STAGE_3, GameAsset.CARROT_STAGE_4), null),
    CAULIFLOWER("Cauliflower", (SeedType.CAULIFLOWER_SEEDS), List.of(1, 2, 4, 4, 1), true, 0,
        175, true, 75, List.of(Season.SPRING), true, false, List.of(GameAsset.CAULIFLOWER_STAGE_1, GameAsset.CAULIFLOWER_STAGE_2, GameAsset.CAULIFLOWER_STAGE_3, GameAsset.CAULIFLOWER_STAGE_4, GameAsset.CAULIFLOWER_STAGE_5, GameAsset.CAULIFLOWER_STAGE_6), GameAsset.GIANT_CAULIFLOWER),
    COFFEE_BEAN("Coffee Bean", (SeedType.COFFEE_BEAN), List.of(1, 2, 2, 3, 2), false, 2, 15,
        false, 0, List.of(Season.SPRING, Season.SUMMER), false, false, List.of(GameAsset.COFFEE_STAGE_2, GameAsset.COFFEE_STAGE_3, GameAsset.COFFEE_STAGE_4, GameAsset.COFFEE_STAGE_5, GameAsset.COFFEE_STAGE_6, GameAsset.COFFEE_STAGE_7), null),
    GREEN_BEAN("Green Bean", (SeedType.BEAN_STARTER), List.of(1, 1, 1, 3, 4), false, 3, 40,
        true, 25, List.of(Season.SPRING), false, false, List.of(GameAsset.GREEN_BEAN_STAGE_2, GameAsset.GREEN_BEAN_STAGE_3, GameAsset.GREEN_BEAN_STAGE_4, GameAsset.GREEN_BEAN_STAGE_5, GameAsset.GREEN_BEAN_STAGE_6, GameAsset.GREEN_BEAN_STAGE_7, GameAsset.GREEN_BEAN_STAGE_8), null),
    KALE("Kale", (SeedType.KALE_SEEDS), List.of(1, 2, 2, 1), true, 0, 110, true, 50,
        List.of(Season.SPRING), false, false, List.of(GameAsset.KALE_STAGE_1, GameAsset.KALE_STAGE_2, GameAsset.KALE_STAGE_3, GameAsset.KALE_STAGE_4, GameAsset.KALE_STAGE_5), null),
    PARSNIP("Parsnip", (SeedType.PARSNIP_SEEDS), List.of(1, 1, 1, 1), true, 0, 35, true, 25,
        List.of(Season.SPRING), false, false, List.of(GameAsset.PARSNIP_STAGE_1, GameAsset.PARSNIP_STAGE_2, GameAsset.PARSNIP_STAGE_3, GameAsset.PARSNIP_STAGE_4, GameAsset.PARSNIP_STAGE_5), null),
    POTATO("Potato", (SeedType.POTATO_SEEDS), List.of(1, 1, 1, 2, 1), true, 0, 80, true, 25,
        List.of(Season.SPRING), false, false, List.of(GameAsset.POTATO_STAGE_1, GameAsset.POTATO_STAGE_2, GameAsset.POTATO_STAGE_3, GameAsset.POTATO_STAGE_4, GameAsset.POTATO_STAGE_5, GameAsset.POTATO_STAGE_6), null),
    RHUBARB("Rhubarb", (SeedType.RHUBARB_SEEDS), List.of(2, 2, 2, 3, 4), true, 0, 220, false, 0,
        List.of(Season.SPRING), false, false, List.of(GameAsset.RHUBARB_STAGE_1, GameAsset.RHUBARB_STAGE_2, GameAsset.RHUBARB_STAGE_3, GameAsset.RHUBARB_STAGE_4, GameAsset.RHUBARB_STAGE_5, GameAsset.RHUBARB_STAGE_6), null),
    STRAWBERRY("Strawberry", (SeedType.STRAWBERRY_SEEDS), List.of(1, 1, 2, 2, 2), false, 4, 120, true, 50,
        List.of(Season.SPRING), false, false, List.of(GameAsset.STRAWBERRY_STAGE_1, GameAsset.STRAWBERRY_STAGE_2, GameAsset.STRAWBERRY_STAGE_3, GameAsset.STRAWBERRY_STAGE_4, GameAsset.STRAWBERRY_STAGE_4, GameAsset.STRAWBERRY_STAGE_5, GameAsset.STRAWBERRY_STAGE_6, GameAsset.STRAWBERRY_STAGE_7), null),
    TULIP("Tulip", (SeedType.TULIP_BULB), List.of(1, 1, 2, 2), true, 0, 30, true, 45,
        List.of(Season.SPRING), false, false, List.of(GameAsset.TULIP_STAGE_1, GameAsset.TULIP_STAGE_2, GameAsset.TULIP_STAGE_3, GameAsset.TULIP_STAGE_4, GameAsset.TULIP_STAGE_6), null),
    UNMILLED_RICE("Unmilled Rice", (SeedType.RICE_SHOOT), List.of(1, 2, 2, 3), true, 0, 30, true, 3,
        List.of(Season.SPRING), false, false, List.of(GameAsset.UNMILLED_RICE_STAGE_1, GameAsset.UNMILLED_RICE_STAGE_2, GameAsset.UNMILLED_RICE_STAGE_3, GameAsset.UNMILLED_RICE_STAGE_4, GameAsset.UNMILLED_RICE_STAGE_5), null),
    BLUEBERRY("Blueberry", (SeedType.BLUEBERRY_SEEDS), List.of(1, 3, 3, 4, 2), false, 4, 50, true, 25,
        List.of(Season.SUMMER), false, false, List.of(GameAsset.BLUEBERRY_STAGE_1, GameAsset.BLUEBERRY_STAGE_2, GameAsset.BLUEBERRY_STAGE_3, GameAsset.BLUEBERRY_STAGE_4, GameAsset.BLUEBERRY_STAGE_4, GameAsset.BLUEBERRY_STAGE_5, GameAsset.BLUEBERRY_STAGE_6, GameAsset.BLUEBERRY_STAGE_7), null),
    CORN("Corn", (SeedType.CORN_SEEDS), List.of(2, 3, 3, 3, 3), false, 4, 50, true, 25,
        List.of(Season.SUMMER, Season.FALL), false, false, List.of(GameAsset.CORN_STAGE_1, GameAsset.CORN_STAGE_2, GameAsset.CORN_STAGE_3, GameAsset.CORN_STAGE_4, GameAsset.CORN_STAGE_5, GameAsset.CORN_STAGE_6, GameAsset.CORN_STAGE_7), null),
    HOPS("Hops", (SeedType.HOPS_STARTER), List.of(1, 1, 2, 3, 4), false, 1, 25, true, 45,
        List.of(Season.SUMMER), false, false, List.of(GameAsset.HOPS_STAGE_1, GameAsset.HOPS_STAGE_3, GameAsset.HOPS_STAGE_4, GameAsset.HOPS_STAGE_5, GameAsset.HOPS_STAGE_6, GameAsset.HOPS_STAGE_7, GameAsset.HOPS_STAGE_8), null),
    HOT_PEPPER("Hot Pepper", (SeedType.PEPPER_SEEDS), List.of(1, 1, 1, 1, 1), false, 3, 40,
        true, 13, List.of(Season.SUMMER), false, false, List.of(GameAsset.HOT_PEPPER_STAGE_1, GameAsset.HOT_PEPPER_STAGE_2, GameAsset.HOT_PEPPER_STAGE_3, GameAsset.HOT_PEPPER_STAGE_4, GameAsset.HOT_PEPPER_STAGE_5, GameAsset.HOT_PEPPER_STAGE_6), null),
    MELON("Melon", (SeedType.MELON_SEEDS), List.of(1, 2, 3, 3, 3), true, 0, 250, true, 113,
        List.of(Season.SUMMER), true, false, List.of(GameAsset.MELON_STAGE_1, GameAsset.MELON_STAGE_2, GameAsset.MELON_STAGE_3, GameAsset.MELON_STAGE_4, GameAsset.MELON_STAGE_5, GameAsset.MELON_STAGE_6), GameAsset.GIANT_MELON),
    POPPY("Poppy", (SeedType.POPPY_SEEDS), List.of(1, 2, 2, 2), true, 0, 140, true, 45,
        List.of(Season.SUMMER), false, false, List.of(GameAsset.POPPY_STAGE_1, GameAsset.POPPY_STAGE_2, GameAsset.POPPY_STAGE_3, GameAsset.POPPY_STAGE_4, GameAsset.POPPY_STAGE_6), null),
    RADISH("Radish", (SeedType.RADISH_SEEDS), List.of(2, 1, 2, 1), true, 0, 90, true, 45,
        List.of(Season.SUMMER), false, false, List.of(GameAsset.RADISH_STAGE_1, GameAsset.RADISH_STAGE_2, GameAsset.RADISH_STAGE_3, GameAsset.RADISH_STAGE_4, GameAsset.RADISH_STAGE_5), null),
    RED_CABBAGE("Red Cabbage", (SeedType.RED_CABBAGE_SEEDS), List.of(2, 1, 2, 2, 2), true, 0, 260,
        true, 75, List.of(Season.SUMMER), false, false, List.of(GameAsset.RED_CABBAGE_STAGE_1, GameAsset.RED_CABBAGE_STAGE_2, GameAsset.RED_CABBAGE_STAGE_3, GameAsset.RED_CABBAGE_STAGE_4, GameAsset.RED_CABBAGE_STAGE_5, GameAsset.RED_CABBAGE_STAGE_6), null),
    STARFRUIT("Starfruit", (SeedType.STARFRUIT_SEEDS), List.of(2, 3, 2, 3, 3), true, 0, 750, true, 125,
        List.of(Season.SUMMER), false, false, List.of(GameAsset.STARFRUIT_STAGE_1, GameAsset.STARFRUIT_STAGE_2, GameAsset.STARFRUIT_STAGE_3, GameAsset.STARFRUIT_STAGE_4, GameAsset.STARFRUIT_STAGE_5, GameAsset.STARFRUIT_STAGE_6), null),
    SUMMER_SPANGLE("Summer Spangle", (SeedType.SPANGLE_SEEDS), List.of(1, 2, 3, 1), true, 0, 90, true, 45,
        List.of(Season.SUMMER), false, false, List.of(GameAsset.SUMMER_SPANGLE_STAGE_1, GameAsset.SUMMER_SPANGLE_STAGE_2, GameAsset.SUMMER_SPANGLE_STAGE_3, GameAsset.SUMMER_SPANGLE_STAGE_4, GameAsset.SUMMER_SPANGLE_STAGE_5), null),
    SUMMER_SQUASH("Summer Squash", (SeedType.SUMMER_SQUASH_SEEDS), List.of(1, 1, 1, 2, 1), false, 3, 45, true, 63,
        List.of(Season.SUMMER), false, false, List.of(GameAsset.SUMMER_SQUASH_STAGE_1, GameAsset.SUMMER_SQUASH_STAGE_2, GameAsset.SUMMER_SQUASH_STAGE_3, GameAsset.SUMMER_SQUASH_STAGE_4, GameAsset.SUMMER_SQUASH_STAGE_5, GameAsset.SUMMER_SQUASH_STAGE_6, GameAsset.SUMMER_SQUASH_STAGE_7), null),
    SUNFLOWER("Sunflower", (SeedType.SUNFLOWER_SEEDS), List.of(1, 2, 3, 2), true, 0, 80, true, 45,
        List.of(Season.SUMMER, Season.FALL), false, false, List.of(GameAsset.SUNFLOWER_STAGE_1, GameAsset.SUNFLOWER_STAGE_2, GameAsset.SUNFLOWER_STAGE_3, GameAsset.SUNFLOWER_STAGE_4, GameAsset.SUNFLOWER_STAGE_5), null),
    TOMATO("Tomato", (SeedType.TOMATO_SEEDS), List.of(2, 2, 2, 2, 3), false, 4, 60, true, 20,
        List.of(Season.SUMMER), false, false, List.of(GameAsset.TOMATO_STAGE_1, GameAsset.TOMATO_STAGE_2, GameAsset.TOMATO_STAGE_3, GameAsset.TOMATO_STAGE_4, GameAsset.TOMATO_STAGE_5, GameAsset.TOMATO_STAGE_6, GameAsset.TOMATO_STAGE_7), null),
    WHEAT("Wheat", (SeedType.WHEAT_SEEDS), List.of(1, 1, 1, 1), true, 0, 25, false, 0,
        List.of(Season.SUMMER, Season.FALL), false, false, List.of(GameAsset.WHEAT_STAGE_1, GameAsset.WHEAT_STAGE_2, GameAsset.WHEAT_STAGE_3, GameAsset.WHEAT_STAGE_4, GameAsset.WHEAT_STAGE_5), null),
    AMARANTH("Amaranth", (SeedType.AMARANTH_SEEDS), List.of(1, 2, 2, 2), true, 0, 150, true, 50,
        List.of(Season.FALL), false, false, List.of(GameAsset.AMARANTH_STAGE_1, GameAsset.AMARANTH_STAGE_2, GameAsset.AMARANTH_STAGE_3, GameAsset.AMARANTH_STAGE_4, GameAsset.AMARANTH_STAGE_5), null),
    ARTICHOKE("Artichoke", (SeedType.ARTICHOKE_SEEDS), List.of(2, 2, 1, 2, 1), true, 0, 160, true, 30,
        List.of(Season.FALL), false, false, List.of(GameAsset.ARTICHOKE_STAGE_1, GameAsset.ARTICHOKE_STAGE_2, GameAsset.ARTICHOKE_STAGE_3, GameAsset.ARTICHOKE_STAGE_4, GameAsset.ARTICHOKE_STAGE_5, GameAsset.ARTICHOKE_STAGE_6), null),
    BEET("Beet", (SeedType.BEET_SEEDS), List.of(1, 1, 2, 2), true, 0, 100, true, 30,
        List.of(Season.FALL), false, false, List.of(GameAsset.BEET_STAGE_1, GameAsset.BEET_STAGE_2, GameAsset.BEET_STAGE_3, GameAsset.BEET_STAGE_4, GameAsset.BEET_STAGE_5), null),
    BOK_CHOY("Bok Choy", (SeedType.BOK_CHOY_SEEDS), List.of(1, 1, 1, 1), true, 0, 80,
        true, 25, List.of(Season.FALL), false, false, List.of(GameAsset.BOK_CHOY_STAGE_1, GameAsset.BOK_CHOY_STAGE_2, GameAsset.BOK_CHOY_STAGE_3, GameAsset.BOK_CHOY_STAGE_4, GameAsset.BOK_CHOY_STAGE_5), null),
    BROCCOLI("Broccoli", (SeedType.BROCCOLI_SEEDS), List.of(2, 2, 2, 2), false, 4, 70, true, 63,
        List.of(Season.FALL), false, false, List.of(GameAsset.BROCCOLI_STAGE_1, GameAsset.BROCCOLI_STAGE_2, GameAsset.BROCCOLI_STAGE_3, GameAsset.BROCCOLI_STAGE_4, GameAsset.BROCCOLI_STAGE_5), null),
    CRANBERRIES("Cranberries", (SeedType.CRANBERRY_SEEDS), List.of(1, 2, 1, 1, 2), false, 5, 75,
        true, 38, List.of(Season.FALL), false, false, List.of(GameAsset.CRANBERRY_STAGE_1, GameAsset.CRANBERRY_STAGE_2, GameAsset.CRANBERRY_STAGE_3, GameAsset.CRANBERRY_STAGE_4, GameAsset.CRANBERRY_STAGE_5, GameAsset.CRANBERRY_STAGE_6, GameAsset.CRANBERRY_STAGE_7), null),
    EGGPLANT("Eggplant", (SeedType.EGGPLANT_SEEDS), List.of(1, 1, 1, 1), false, 5, 60,
        true, 20, List.of(Season.FALL), false, false, List.of(GameAsset.EGGPLANT_STAGE_1, GameAsset.EGGPLANT_STAGE_2, GameAsset.EGGPLANT_STAGE_3, GameAsset.EGGPLANT_STAGE_4, GameAsset.EGGPLANT_STAGE_5, GameAsset.EGGPLANT_STAGE_6, GameAsset.EGGPLANT_STAGE_7), null),
    FAIRY_ROSE("Fairy Rose", (SeedType.FAIRY_SEEDS), List.of(1, 4, 4, 3), true, 0, 290, true, 45,
        List.of(Season.FALL), false, false, List.of(GameAsset.FAIRY_ROSE_STAGE_1, GameAsset.FAIRY_ROSE_STAGE_2, GameAsset.FAIRY_ROSE_STAGE_3, GameAsset.FAIRY_ROSE_STAGE_4, GameAsset.FAIRY_ROSE_STAGE_5), null),
    GRAPE("Grape", (SeedType.GRAPE_STARTER), List.of(1, 1, 2, 3, 3), false, 3, 80, true, 38,
        List.of(Season.FALL), false, false, List.of(GameAsset.GRAPE_STAGE_1, GameAsset.GRAPE_STAGE_2, GameAsset.GRAPE_STAGE_3, GameAsset.GRAPE_STAGE_4, GameAsset.GRAPE_STAGE_5, GameAsset.GRAPE_STAGE_6, GameAsset.GRAPE_STAGE_7), null),
    PUMPKIN("Pumpkin", (SeedType.PUMPKIN_SEEDS), List.of(1, 2, 3, 4, 3), true, 0, 320, false, 0,
        List.of(Season.FALL), true, false, List.of(GameAsset.PUMPKIN_STAGE_1, GameAsset.PUMPKIN_STAGE_2, GameAsset.PUMPKIN_STAGE_3, GameAsset.PUMPKIN_STAGE_4, GameAsset.PUMPKIN_STAGE_5, GameAsset.PUMPKIN_STAGE_6), GameAsset.GIANT_PUMPKIN),
    YAM("Yam", (SeedType.YAM_SEEDS), List.of(1, 3, 3, 3), true, 0, 160, true, 45,
        List.of(Season.FALL), false, false, List.of(GameAsset.YAM_STAGE_1, GameAsset.YAM_STAGE_2, GameAsset.YAM_STAGE_3, GameAsset.YAM_STAGE_4, GameAsset.YAM_STAGE_5), null),
    SWEET_GEM_BERRY("Sweet Gem Berry", (SeedType.RARE_SEED), List.of(2, 4, 6, 6, 6), true, 0, 3000, false, 0,
        List.of(Season.FALL), false, false, List.of(GameAsset.SWEET_GEM_BERRY_STAGE_1, GameAsset.SWEET_GEM_BERRY_STAGE_2, GameAsset.SWEET_GEM_BERRY_STAGE_3, GameAsset.SWEET_GEM_BERRY_STAGE_4, GameAsset.SWEET_GEM_BERRY_STAGE_5, GameAsset.SWEET_GEM_BERRY_STAGE_6), null),
    POWDERMELON("Powdermelon", (SeedType.POWDER_MELON_SEEDS), List.of(1, 2, 1, 2, 1), true, 0,
        60, true, 63, List.of(Season.WINTER), true, false, List.of(GameAsset.POWDERMELON_STAGE_1, GameAsset.POWDERMELON_STAGE_2, GameAsset.POWDERMELON_STAGE_3, GameAsset.POWDERMELON_STAGE_4, GameAsset.POWDERMELON_STAGE_5, GameAsset.POWDERMELON_STAGE_6), GameAsset.GIANT_POWDERMELON),
    ANCIENT_FRUIT("Ancient Fruit", (SeedType.ANCIENT_SEEDS), List.of(2, 7, 7, 7, 5), false, 7, 550, false, 0,
        List.of(Season.SPRING, Season.SUMMER, Season.FALL), false, false, List.of(GameAsset.ANCIENT_FRUIT_STAGE_1, GameAsset.ANCIENT_FRUIT_STAGE_2, GameAsset.ANCIENT_FRUIT_STAGE_3, GameAsset.ANCIENT_FRUIT_STAGE_4, GameAsset.ANCIENT_FRUIT_STAGE_5, GameAsset.ANCIENT_FRUIT_STAGE_6, GameAsset.ANCIENT_FRUIT_STAGE_7), null),
    COMMON_MUSHROOM("Common Mushroom", null, null, true, 0, 40, true, 38,
        List.of(Season.SPECIAL), false, true, List.of(GameAsset.COMMON_MUSHROOM), null),
    DAFFODIL("Daffodil", null, null, true, 0, 30, false, 0,
        List.of(Season.SPRING), false, true, List.of(GameAsset.DAFFODIL), null),
    DANDELION("Dandelion", null, null, true, 0, 40,
        true, 25, List.of(Season.SPRING), false, true, List.of(GameAsset.DANDELION), null),
    LEEK("Leek", null, null, true, 0, 60, true, 40,
        List.of(Season.SPRING), false, true, List.of(GameAsset.LEEK), null),
    MOREL("Morel", null, null, true, 0, 150, true, 20,
        List.of(Season.SPRING), false, true, List.of(GameAsset.MOREL), null),
    SALMONBERRY("Salmonberry", null, null, true, 0, 5, true, 25,
        List.of(Season.SPRING), false, true, List.of(GameAsset.SALMONBERRY), null),
    SPRING_ONION("Spring Onion", null, null, true, 0, 8, true, 13,
        List.of(Season.SPRING), false, true, List.of(GameAsset.SPRING_ONION), null),
    WILD_HORSERADISH("Wild Horseradish", null, null, true, 0, 50,
        true, 13, List.of(Season.SPRING), false, true, List.of(GameAsset.WILD_HORSERADISH), null),
    FIDDLEHEAD_FERN("Fiddlehead Fern", null, null, true, 0, 90, true, 25,
        List.of(Season.SUMMER), false, true, List.of(GameAsset.FIDDLEHEAD_FERN), null),
    RED_MUSHROOM("Red Mushroom", null, null, true, 0, 75, true, -50,
        List.of(Season.SUMMER), false, true, List.of(GameAsset.RED_MUSHROOM), null),
    SPICE_BERRY("Spice Berry", null, null, true, 0, 80, true, 25,
        List.of(Season.SUMMER), false, true, List.of(GameAsset.SPICE_BERRY), null),
    SWEET_PEA("Sweet Pea", null, null, true, 0, 50, false, 0,
        List.of(Season.SUMMER), false, true, List.of(GameAsset.SWEET_PEA), null),
    BLACKBERRY("Blackberry", null, null, true, 0, 25, true, 25,
        List.of(Season.FALL), false, true, List.of(GameAsset.BLACKBERRY), null),
    CHANTERELLE("Chanterelle", null, null, true, 0, 160, true, 75,
        List.of(Season.FALL), false, true, List.of(GameAsset.CHANTERELLE), null),
    HAZELNUT("Hazelnut", null, null, true, 0, 40, true, 38,
        List.of(Season.FALL), false, true, List.of(GameAsset.HAZELNUT), null),
    PURPLE_MUSHROOM("Purple Mushroom", null, null, true, 0, 90,
        true, 30, List.of(Season.FALL), false, true, List.of(GameAsset.PURPLE_MUSHROOM), null),
    WILD_PLUM("Wild Plum", null, null, true, 0, 80, true, 25,
        List.of(Season.FALL), false, true, List.of(GameAsset.WILD_PLUM), null),
    CROCUS("Crocus", null, null, true, 0, 60, false, 0,
        List.of(Season.WINTER), false, true, List.of(GameAsset.CROCUS), null),
    CRYSTAL_FRUIT("Crystal Fruit", null, null, true, 0, 150, true, 63,
        List.of(Season.WINTER), false, true, List.of(GameAsset.CRYSTAL_FRUIT), null),
    HOLLY("Holly", null, null, true, 0, 80, true, -37,
        List.of(Season.WINTER), false, true, List.of(GameAsset.HOLLY), null),
    SNOW_YAM("Snow Yam", null, null, true, 0, 100, true, 30,
        List.of(Season.WINTER), false, true, List.of(GameAsset.SNOW_YAM), null),
    WINTER_ROOT("Winter Root", null, null, true, 0, 70, true, 25,
        List.of(Season.WINTER), false, true, List.of(GameAsset.WINTER_ROOT), null);

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
    @JsonIgnore
    private final transient List<Texture> textures;
    private final transient Texture giantTexture;

    CropType(String name, Source source, List<Integer> harvestStages, boolean oneTime, int regrowthTime,
             int baseSellPrice, boolean isEdible, int energy, List<Season> seasons, boolean canBecomeGiant,
             boolean isForaging, List<Texture> textures, Texture giantTexture) {
        this.name = name;
        this.source = source;
        if (harvestStages != null) {
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
        this.textures = textures;
        this.giantTexture = giantTexture;
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
        if (this.source != null) {
            token.append("Source: ").append(this.getSource().getName());
        }
        token.append("\n");
        token.append("Stages: ");
        if (this.harvestStages != null) {
            token.append(harvestStages);
        }
        token.append("\n");
        token.append("Total Harvest Time: ");
        if (this.harvestStages != null) {
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


    @Override
    public Integer getContainingEnergy() {
        return getEnergy();
    }

    @JsonIgnore
    @Override
    public Texture getTexture() {
        return textures.get(0);
    }
}
