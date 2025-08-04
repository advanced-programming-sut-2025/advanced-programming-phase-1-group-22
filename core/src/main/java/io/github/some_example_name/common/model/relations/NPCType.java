package io.github.some_example_name.common.model.relations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.some_example_name.common.model.AnimatedSprite;
import io.github.some_example_name.common.model.Direction;
import io.github.some_example_name.common.model.animal.Fish;
import io.github.some_example_name.common.model.cook.Food;
import io.github.some_example_name.common.model.craft.Craft;
import io.github.some_example_name.common.model.products.TreesAndFruitsAndSeeds.MadeProduct;
import io.github.some_example_name.common.model.source.Crop;
import io.github.some_example_name.common.model.source.Mineral;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;
import lombok.ToString;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.animal.FishType;
import io.github.some_example_name.common.model.cook.FoodType;
import io.github.some_example_name.common.model.craft.CraftType;
import io.github.some_example_name.common.model.enums.Season;
import io.github.some_example_name.common.model.products.AnimalProductType;
import io.github.some_example_name.common.model.products.TreesAndFruitsAndSeeds.MadeProductType;
import io.github.some_example_name.common.model.receipe.CookingRecipe;
import io.github.some_example_name.common.model.source.CropType;
import io.github.some_example_name.common.model.source.MineralType;
import io.github.some_example_name.common.model.tools.WateringCanType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@ToString
public enum NPCType {
    SEBASTIAN("sebastian", GameAsset.SEBASTIAN_FULL_NPC, GameAsset.CABIN1, GameAsset.SEBASTIAN,
        () -> List.of(AnimalProductType.SHEEP_WOOL, FoodType.PUMPKIN_PIE, FoodType.PIZZA),
        () -> List.of(
            new Mission(1, () -> Map.of(new Mineral(MineralType.IRON), 50), () -> Map.of(new Mineral(MineralType.DIAMOND), 2), 0),
            new Mission(2, () -> Map.of(new Food(FoodType.PUMPKIN_PIE), 1), () -> Map.of(new Mineral(MineralType.GOLD), 5000), 0),
            new Mission(3, () -> Map.of(new Mineral(MineralType.STONE), 150), () -> Map.of(new Mineral(MineralType.QUARTZ), 50), Season.FALL)
        ), 256, 128, 65, 65, 1,
        "I'm a young kind man with lovely and flirty attributes and I'm so loved here. And I'm very concerned of my dishes."),

    ABIGIL("ebigel", GameAsset.ABIGAIL_FULL_NPC, GameAsset.CABIN2, GameAsset.ABIGAIL,
        () -> List.of(MineralType.IRON_ORE, MineralType.STONE, MadeProductType.COFFE),
        () -> List.of(
            new Mission(1, () -> Map.of(new MadeProduct(MadeProductType.GOLD_BAR), 1), () -> Map.of(new Mineral(MineralType.GOLD), 1), 0),
            new Mission(2, () -> Map.of(new Crop(CropType.PUMPKIN), 1), () -> Map.of(new Mineral(MineralType.GOLD), 500), 1),
            new Mission(3, () -> Map.of(new Crop(CropType.WHEAT), 50), () -> Map.of(WateringCanType.IRIDIUM, 1), Season.WINTER)
        ), 256, 128, 65, 65, 2,
        "I'm lovely gorgeous girl with purple attributes and I'm in love."),

    HARVEY("harvey", GameAsset.HARVEY_FULL_NPC, GameAsset.CABIN3, GameAsset.HARVEY,
        () -> List.of(MadeProductType.PICKLES, MadeProductType.WINE, MadeProductType.COFFE),
        () -> List.of(
            new Mission(1, () -> Map.of(new Mineral(MineralType.GOLD), 12), () -> Map.of(new Mineral(MineralType.GOLD), 750), 0),
            new Mission(2, () -> Map.of(new Fish(FishType.SALMON), 1), () -> Map.of(new Mineral(MineralType.GOLD), 1), 1),
            new Mission(3, () -> Map.of(new MadeProduct(MadeProductType.WINE), 1), () -> Map.of(new Food(FoodType.SALAD), 5), Season.WINTER)
        ), 256, 128, 65, 65, 3,
        "I'm an old man trying to make a living through reading books; I'm so wise everyone comes to me."),

    LIA("lia", GameAsset.LEAH_FULL_NPC, GameAsset.CABIN4, GameAsset.LIA_ICON,
        () -> List.of(MadeProductType.WINE, CropType.GRAPE, FoodType.SALAD),
        () -> List.of(
            new Mission(1, () -> Map.of(new Mineral(MineralType.HARD_WOOD), 10), () -> Map.of(new Mineral(MineralType.GOLD), 500), 0),
            new Mission(2, () -> Map.of(new Fish(FishType.SALMON), 1), () -> Map.of(CookingRecipe.SALMON_DINNER_RECIPE, 1), 1),
            new Mission(3, () -> Map.of(new Mineral(MineralType.WOOD), 200), () -> Map.of(new Craft(CraftType.DELUXE_SCARECROW, null, null), 3), Season.SUMMER)
        ), 256, 96, 65, 65, 4,
        "I'm flirty mean teenage girl who tries to have anything she wants."),

    RABIN("rabin", GameAsset.ROBIN_FULL_NPC, GameAsset.CABIN5, GameAsset.ROBIN,
        () -> List.of(FoodType.SPAGHETTI, MineralType.WOOD, MadeProductType.IRON_BAR),
        () -> List.of(
            new Mission(1, () -> Map.of(new Mineral(MineralType.WOOD), 80), () -> Map.of(new Mineral(MineralType.GOLD), 1000), 0),
            new Mission(2, () -> Map.of(new MadeProduct(MadeProductType.IRON_BAR), 10), () -> Map.of(new Craft(CraftType.BEE_HOUSE, null, null), 3), 1),
            new Mission(3, () -> Map.of(new Mineral(MineralType.WOOD), 1000), () -> Map.of(new Mineral(MineralType.GOLD), 25_000), Season.WINTER)
        ), 256, 128, 65, 65, 5,
        "I'm hardworking man in love of wood and iron. Somehow like a carpenter.");

    private final String name;
    private final transient Texture textureCharacter;
    private final transient Texture textureHouse;
    private final transient TextureRegion textureIcon;
    private final transient TextureRegion[][] miniTextures;
    private final String personality;
    private final List<Mission> missions = new ArrayList<>();
    private final int missionSeasonDis;
    private final HashMap<Direction, Integer> directions;


    public AnimatedSprite getLazy(Direction direction) {
        Animation<TextureRegion> animation = new Animation<>(0.1f,
            miniTextures[directions.get(direction)][0]);
        AnimatedSprite x = new AnimatedSprite(animation);
        x.setLooping(false);
        return x;
    }

    public AnimatedSprite getWalking(Direction direction) {
        Animation<TextureRegion> animation = new Animation<>(0.1f,
            miniTextures[directions.get(direction)][1], miniTextures[directions.get(direction)][2],
            miniTextures[directions.get(direction)][3], miniTextures[directions.get(direction)][0]);
        return new AnimatedSprite(animation);
    }


    @FunctionalInterface
    private interface IngredientsSupplier {
        List<Salable> get();
    }

    @FunctionalInterface
    private interface MissionsSupplier {
        List<Mission> get();
    }

    private transient volatile List<Salable> resolvedIngredients;
    private final IngredientsSupplier ingredientsSupplier;
    private final MissionsSupplier missionsSupplier;

    public List<Salable> getFavorites() {
        if (resolvedIngredients == null) {
            resolvedIngredients = ingredientsSupplier.get();
        }
        return resolvedIngredients;
    }

    public List<Mission> getMissions() {
        if (missions.isEmpty()) {
            missions.addAll(missionsSupplier.get());
        }
        return missions;
    }

    NPCType(String name, Texture textureCharacter, Texture textureHouse, Texture textureIcon, IngredientsSupplier ingredientsSupplier,
            MissionsSupplier missionsSupplier, int avatarX, int avatarY, int avatarWidth, int avatarHeight, int missionSeasonDis, String personality) {
        this.name = name;
        this.textureCharacter = textureCharacter;
        this.textureHouse = textureHouse;
        this.textureIcon = new TextureRegion(textureCharacter, avatarX, avatarY, avatarWidth, avatarHeight);
        this.miniTextures = new TextureRegion(textureCharacter, 192, 0, 64, textureCharacter.getHeight()).split(16, 32);
        this.ingredientsSupplier = ingredientsSupplier;
        this.missionsSupplier = missionsSupplier;
        this.missionSeasonDis = missionSeasonDis;
        this.personality = personality;
        directions = new HashMap<>();
        directions.put(Direction.NORTH, 2);
        directions.put(Direction.CENTRE, 0);
        directions.put(Direction.NORTHEAST, 1);
        directions.put(Direction.SOUTHEAST, 1);
        directions.put(Direction.EAST, 1);
        directions.put(Direction.SOUTH, 0);
        directions.put(Direction.NORTHWEST, 3);
        directions.put(Direction.WEST, 3);
        directions.put(Direction.SOUTHWEST, 3);
    }
}
