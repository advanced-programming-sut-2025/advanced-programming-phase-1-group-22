package io.github.some_example_name.common.model.products.TreesAndFruitsAndSeeds;

import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;
import lombok.ToString;
import io.github.some_example_name.common.model.enums.Season;
import io.github.some_example_name.common.model.products.Harvestable;
import io.github.some_example_name.common.model.source.SeedType;
import io.github.some_example_name.common.model.source.Source;

import java.util.List;

@Getter
@ToString
public enum TreeType implements Harvestable {
    ACORNS(null, Season.SPECIAL, "Acorns", SeedType.ACORNS, true, null, 0,List.of(GameAsset.ORANGE_STAGE_4),null,GameAsset.ORANGETREELIGHTNING),
    MAPLE_SEEDS(null, Season.SPECIAL, "Maple Seeds", SeedType.MAPLE_SEEDS, true, null, 0,List.of(GameAsset.ORANGE_STAGE_4),null,GameAsset.ORANGETREELIGHTNING),
    PINE_CONES(null, Season.SPECIAL, "Pine cones", SeedType.PINE_CONES, true, null, 0,List.of(GameAsset.ORANGE_STAGE_4),null,GameAsset.ORANGETREELIGHTNING),
    MAHOGANY_SEEDS(null, Season.SPECIAL, "Mahogany Seeds", SeedType.MAHOGANY_SEEDS, true, null, 0,List.of(GameAsset.ORANGE_STAGE_4),null,GameAsset.ORANGETREELIGHTNING),
    MUSHROOM_TREE_SEEDS(null, Season.SPECIAL, "Mushroom Tree Seeds", SeedType.MUSHROOM_TREE_SEEDS, true, null, 0,List.of(GameAsset.ORANGE_STAGE_4),null,GameAsset.ORANGETREELIGHTNING),
    APRICOT_TREE((FruitType.APRICOT), Season.SPRING, "Apricot Tree", SeedType.APRICOT_SAPLING, false, List.of(7, 7, 7, 7), 1,List.of(GameAsset.APRICOT_STAGE_1,GameAsset.APRICOT_STAGE_2,GameAsset.APRICOT_STAGE_3,GameAsset.APRICOT_STAGE_4,GameAsset.APRICOT_STAGE_5),GameAsset.APRICOT_STAGE_5_FRUIT,GameAsset.APRICOTTREELIGHTNING),
    CHERRY_TREE((FruitType.CHERRY), Season.SPRING, "Cherry Tree", SeedType.CHERRY_SAPLING, false, List.of(7, 7, 7, 7), 1,List.of(GameAsset.CHERRY_STAGE_1,GameAsset.CHERRY_STAGE_2,GameAsset.CHERRY_STAGE_3,GameAsset.CHERRY_STAGE_4,GameAsset.CHERRY_STAGE_5),GameAsset.CHERRY_STAGE_5_FRUIT,GameAsset.CHERRYTREELIGHTNING),
    BANANA_TREE((FruitType.BANANA), Season.SUMMER, "Banana Tree", SeedType.BANANA_SAPLING, false, List.of(7, 7, 7, 7), 1,List.of(GameAsset.BANANA_STAGE_1,GameAsset.BANANA_STAGE_2,GameAsset.BANANA_STAGE_3,GameAsset.BANANA_STAGE_4,GameAsset.BANANA_STAGE_5),GameAsset.BANANA_STAGE_5_FRUIT,GameAsset.BANANATREELIGHTNING),
    MANGO_TREE((FruitType.MANGO), Season.SUMMER, "Mango Tree", SeedType.MANGO_SAPLING, false, List.of(7, 7, 7, 7), 1,List.of(GameAsset.MANGO_STAGE_1,GameAsset.MANGO_STAGE_2,GameAsset.MANGO_STAGE_3,GameAsset.MANGO_STAGE_4,GameAsset.MANGO_STAGE_5),GameAsset.MANGO_STAGE_5_FRUIT,GameAsset.MANGOTREELIGHTNING),
    ORANGE_TREE((FruitType.ORANGE), Season.SUMMER, "Orange Tree", SeedType.ORANGE_SAPLING, false, List.of(7, 7, 7, 7), 1,List.of(GameAsset.ORANGE_STAGE_1,GameAsset.ORANGE_STAGE_2,GameAsset.ORANGE_STAGE_3,GameAsset.ORANGE_STAGE_4,GameAsset.ORANGE_STAGE_5),GameAsset.ORANGE_STAGE_5_FRUIT,GameAsset.ORANGETREELIGHTNING),
    PEACH_TREE((FruitType.PEACH), Season.SUMMER, "Peach Tree", SeedType.PEACH_SAPLING, false, List.of(7, 7, 7, 7), 1,List.of(GameAsset.PEACH_STAGE_1,GameAsset.PEACH_STAGE_2,GameAsset.PEACH_STAGE_3,GameAsset.PEACH_STAGE_4,GameAsset.PEACH_STAGE_5),GameAsset.PEACH_STAGE_5_FRUIT,GameAsset.PEACHTREELIGHTNING),
    APPLE_TREE((FruitType.APPLE), Season.FALL, "Apple Tree", SeedType.APPLE_SAPLING, false, List.of(7, 7, 7, 7), 1,List.of(GameAsset.APPLE_STAGE_1,GameAsset.APPLE_STAGE_2,GameAsset.APPLE_STAGE_3,GameAsset.APPLE_STAGE_4,GameAsset.APPLE_STAGE_5),GameAsset.APPLE_STAGE_5_FRUIT,GameAsset.APPLETREELIGHTNING),
    POMEGRANATE_TREE((FruitType.POMEGRANATE), Season.FALL, "Pomegranate Tree", SeedType.POMEGRANATE_SAPLING, false, List.of(7, 7, 7, 7), 1,List.of(GameAsset.POMEGRANATE_STAGE_1,GameAsset.POMEGRANATE_STAGE_2,GameAsset.POMEGRANATE_STAGE_3,GameAsset.POMEGRANATE_STAGE_4,GameAsset.POMEGRANATE_STAGE_5),GameAsset.POMEGRANATE_STAGE_5_FRUIT,GameAsset.POMEGRANATETREELIGHTNING),
    OAK_TREE((FruitType.OAK_RESIN), Season.SPECIAL, "Oak Tree", SeedType.ACORNS, false, List.of(7, 7, 7, 7), 7,List.of(GameAsset.OAK_STAGE_1,GameAsset.OAK_STAGE_2,GameAsset.OAK_STAGE_3,GameAsset.OAK_STAGE_4,GameAsset.OAK_STAGE_5),GameAsset.OAK_STAGE_4,GameAsset.OAK_STUMP_SPRING),
    MAPLE_TREE((FruitType.MAPLE_SYRUP), Season.SPECIAL, "Maple Tree", SeedType.MAPLE_SEEDS, false, List.of(7, 7, 7, 7), 9,List.of(GameAsset.MAPLE_STAGE_1,GameAsset.MAPLE_STAGE_2,GameAsset.MAPLE_STAGE_3,GameAsset.MAPLE_STAGE_4,GameAsset.MAPLE_STAGE_5),GameAsset.MAPLE_STAGE_4,GameAsset.MAPLE_STUMP_FALL),
    PINE_TREE((FruitType.PINE_TAR), Season.SPECIAL, "Pine Tree", SeedType.PINE_CONES, false, List.of(7, 7, 7, 7), 5,List.of(GameAsset.PINE_STAGE_1,GameAsset.PINE_STAGE_2,GameAsset.PINE_STAGE_3,GameAsset.PINE_STAGE_4,GameAsset.PINE_STAGE_5),GameAsset.PINE_STAGE_4,GameAsset.PINE_STUMP_FALL),
    MAHOGANY_TREE((FruitType.SAP), Season.SPECIAL, "Mahogany Tree", SeedType.MAHOGANY_SEEDS, false, List.of(7, 7, 7, 7), 1,List.of(GameAsset.MAHOGANY_STAGE_1,GameAsset.MAHOGANY_STAGE_2,GameAsset.MAHOGANY_STAGE_3,GameAsset.MAHOGANY_STAGE_4,GameAsset.MAHOGANY_STAGE_5),GameAsset.MAHOGANY_STAGE_4,GameAsset.MAHOGANY_STUMP_FALL),
    MUSHROOM_TREE((FruitType.COMMON_MUSHROOM), Season.SPECIAL, "Mushroom Tree", SeedType.MUSHROOM_TREE_SEEDS, false, List.of(7, 7, 7, 7), 1,List.of(GameAsset.MUSHROOMTREE_STAGE_1,GameAsset.MUSHROOMTREE_STAGE_2,GameAsset.MUSHROOMTREE_STAGE_3,GameAsset.MUSHROOMTREE_STAGE_4,GameAsset.MUSHROOMTREE_STAGE_5),GameAsset.MUSHROOMTREE_STAGE_4,GameAsset.MAHOGANY_STUMP_FALL),
    MYSTIC_TREE((FruitType.MYSTIC_SYRUP), Season.SPECIAL, "Mystic Tree", SeedType.MYSTIC_TREE_SEEDS, false, List.of(7, 7, 7, 7), 7,List.of(GameAsset.MYSTIC_TREE_STAGE_1,GameAsset.MYSTIC_TREE_STAGE_2,GameAsset.MYSTIC_TREE_STAGE_3,GameAsset.MYSTIC_TREE_STAGE_4,GameAsset.MYSTIC_TREE_STAGE_5),GameAsset.MYSTIC_TREE_STAGE_4,GameAsset.MYSTIC_TREE_STUMP);

    private final Boolean IsForaging;
    private final List<Integer> harvestStages;

    private final FruitType fruit;
    private final Season season;
    private final String name;
    private final Source source;
    private final Integer harvestCycle;
    private final transient List<Texture> textures;
    private final transient Texture fruitedTexture;
    private final transient Texture burnTexture;

    TreeType(FruitType fruit, Season season, String name, Source source, boolean IsForaging, List<Integer> harvestStages, int harvestCycle, List<Texture> textures,Texture fruitedTexture, Texture burnTexture) {
        this.fruit = fruit;
        this.season = season;
        this.name = name;
        this.source = source;
        this.IsForaging = IsForaging;
        this.harvestStages = harvestStages;
        this.harvestCycle = harvestCycle;
        this.textures = textures;
        this.fruitedTexture = fruitedTexture;
        this.burnTexture = burnTexture;
    }

    @Override
    public String craftInfo() {
        String token = "";
        token += "Name: " + this.name + "\n";
        token += "Source: " + this.source.getName() + "\n";
        if (this.harvestStages == null){
            token += "Stages: \n";
            token += "Total Harvest Time: \n";
        }
        else {
            token += "Stages: " + "[7 ,7 ,7 ,7]\n";
            token += "Total Harvest Time: " + "28\n";
        }
        token += "One Time: " + "False\n";
        token += "Regrowth Time: " + this.harvestCycle + "\n";
        if (this.fruit != null){
            token += "Base Sell Price: " + this.fruit.getBaseSellPrice() + "\n";
            token += "IsEdible: " + this.fruit.getIsEdible() + "\n";
            token += "Base Energy: " + this.fruit.getEnergy() + "\n";
        }else {
            token += "Base Sell Price: \n";
            token += "IsEdible: \n";
            token += "Base Energy: \n";
        }
        token += "Base Health: \n";
        token += "Season: " + this.season.toString().toLowerCase() + "\n";
        token += "Can Become Giant: false\n";
        return token;
    }

    @Override
    public int getSellPrice() {
        return 0;
    }

    @Override
    public Integer getContainingEnergy() {
        return Harvestable.super.getContainingEnergy();
    }

    @Override
    public int getEnergy() {
        return 0;
    }
}
