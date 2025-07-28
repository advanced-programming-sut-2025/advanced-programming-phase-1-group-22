package io.github.some_example_name.common.model.relations;

import com.badlogic.gdx.graphics.Texture;
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
import java.util.List;
import java.util.Map;

@Getter
@ToString
public enum NPCType {
    SEBASTIAN("sebastian", GameAsset.SEBASTIAN_, GameAsset.CABIN1, GameAsset.SEBASTIAN,
        () -> List.of(AnimalProductType.SHEEP_WOOL, FoodType.PUMPKIN_PIE, FoodType.PIZZA),
        () -> List.of(
            new Mission(1, () -> Map.of(MineralType.IRON, 50), () -> Map.of(MineralType.DIAMOND, 2), 0),
            new Mission(2, () -> Map.of(FoodType.PUMPKIN_PIE, 1), () -> Map.of(MineralType.GOLD, 5000), 0),
            new Mission(3, () -> Map.of(MineralType.STONE, 150), () -> Map.of(MineralType.QUARTZ, 50), Season.FALL)
        ),
        1),

    ABIGIL("ebigel", GameAsset.ABIGEL_, GameAsset.CABIN2, GameAsset.ABIGAIL,
        () -> List.of(MineralType.IRON_ORE, MineralType.STONE, MadeProductType.COFFE),
        () -> List.of(
            new Mission(1, () -> Map.of(MadeProductType.GOLD_BAR, 1), () -> Map.of(MineralType.GOLD, 1), 0),
            new Mission(2, () -> Map.of(CropType.PUMPKIN, 1), () -> Map.of(MineralType.GOLD, 500), 1),
            new Mission(3, () -> Map.of(CropType.WHEAT, 50), () -> Map.of(WateringCanType.IRIDIUM, 1), Season.WINTER)
        ),
        2),

    HARVEY("harvey", GameAsset.HARVEY_, GameAsset.CABIN3, GameAsset.HARVEY,
        () -> List.of(MadeProductType.PICKLES, MadeProductType.WINE, MadeProductType.COFFE),
        () -> List.of(
            new Mission(1, () -> Map.of(MineralType.GOLD, 12), () -> Map.of(MineralType.GOLD, 750), 0),
            new Mission(2, () -> Map.of(FishType.SALMON, 1), () -> Map.of(MineralType.GOLD, 1), 1),
            new Mission(3, () -> Map.of(MadeProductType.WINE, 1), () -> Map.of(FoodType.SALAD, 5), Season.WINTER)
        ),
        3),

    LIA("lia", GameAsset.LIA_, GameAsset.CABIN4, GameAsset.LIA_ICON,
        () -> List.of(MadeProductType.WINE, CropType.GRAPE, FoodType.SALAD),
        () -> List.of(
            new Mission(1, () -> Map.of(MineralType.HARD_WOOD, 10), () -> Map.of(MineralType.GOLD, 500), 0),
            new Mission(2, () -> Map.of(FishType.SALMON, 1), () -> Map.of(CookingRecipe.SALMON_DINNER_RECIPE, 1), 1),
            new Mission(3, () -> Map.of(MineralType.WOOD, 200), () -> Map.of(CraftType.DELUXE_SCARECROW, 3), Season.SUMMER)
        ),
        4),

    RABIN("rabin", GameAsset.ROBIN_, GameAsset.CABIN5, GameAsset.ROBIN,
        () -> List.of(FoodType.SPAGHETTI, MineralType.WOOD, MadeProductType.IRON_BAR),
        () -> List.of(
            new Mission(1, () -> Map.of(MineralType.WOOD, 80), () -> Map.of(MineralType.GOLD, 1000), 0),
            new Mission(2, () -> Map.of(MadeProductType.IRON_BAR, 10), () -> Map.of(CraftType.BEE_HOUSE, 3), 1),
            new Mission(3, () -> Map.of(MineralType.WOOD, 1000), () -> Map.of(MineralType.GOLD, 25_000), Season.WINTER)
        ),
        5);

    private final String name;
    private final transient Texture textureCharacter;
    private final transient Texture textureHouse;
    private final transient Texture textureIcon;
    private final List<Mission> missions = new ArrayList<>();
    private final int missionSeasonDis;

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
            MissionsSupplier missionsSupplier, int missionSeasonDis) {
        this.name = name;
        this.textureCharacter = textureCharacter;
        this.textureHouse = textureHouse;
        this.textureIcon = textureIcon;
        this.ingredientsSupplier = ingredientsSupplier;
        this.missionsSupplier = missionsSupplier;
        this.missionSeasonDis = missionSeasonDis;
    }
}
