package io.github.some_example_name.model.relations;

import lombok.Getter;
import lombok.ToString;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.animal.FishType;
import io.github.some_example_name.model.cook.FoodType;
import io.github.some_example_name.model.craft.CraftType;
import io.github.some_example_name.model.enums.Season;
import io.github.some_example_name.model.products.AnimalProductType;
import io.github.some_example_name.model.products.TreesAndFruitsAndSeeds.MadeProductType;
import io.github.some_example_name.model.receipe.CookingRecipe;
import io.github.some_example_name.model.source.CropType;
import io.github.some_example_name.model.source.MineralType;
import io.github.some_example_name.model.structure.NPCHouse;
import io.github.some_example_name.model.tools.WateringCanType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Getter
@ToString
public enum NPCType {
    SEBASTIAN("sebastian", "",
            () -> List.of(AnimalProductType.SHEEP_WOOL, FoodType.PUMPKIN_PIE, FoodType.PIZZA),
            () -> List.of(
                    new Mission(() -> Map.of(MineralType.IRON, 50), () -> Map.of(MineralType.DIAMOND, 2), 0),
                    new Mission(() -> Map.of(FoodType.PUMPKIN_PIE, 1), () -> Map.of(MineralType.GOLD, 5000), 0),
                    new Mission(() -> Map.of(MineralType.STONE, 150), () -> Map.of(MineralType.QUARTZ, 50), Season.FALL)
            ),
            1),

    EBIGIL("ebigel", "",
            () -> List.of(MineralType.IRON_ORE, MineralType.STONE, MadeProductType.COFFE),
            () -> List.of(
                    new Mission(() -> Map.of(MadeProductType.GOLD_BAR, 1), () -> Map.of(MineralType.GOLD, 1), 0),
                    new Mission(() -> Map.of(CropType.PUMPKIN, 1), () -> Map.of(MineralType.GOLD, 500), 1),
                    new Mission(() -> Map.of(CropType.WHEAT, 50), () -> Map.of(WateringCanType.IRIDIUM, 1), Season.WINTER)
            ),
            2),

    HARVEY("harvey", "",
            () -> List.of(MadeProductType.PICKLES, MadeProductType.WINE, MadeProductType.COFFE),
            () -> List.of(
                    new Mission(() -> Map.of(MineralType.GOLD, 12), () -> Map.of(MineralType.GOLD, 750), 0),
                    new Mission(() -> Map.of(FishType.SALMON, 1), () -> Map.of(MineralType.GOLD, 1), 1),
                    new Mission(() -> Map.of(MadeProductType.WINE, 1), () -> Map.of(FoodType.SALAD, 5), Season.WINTER)
            ),
            3),

    LIA("lia", "",
            () -> List.of(MadeProductType.WINE, CropType.GRAPE, FoodType.SALAD),
            () -> List.of(
                    new Mission(() -> Map.of(MineralType.HARD_WOOD, 10), () -> Map.of(MineralType.GOLD, 500), 0),
                    new Mission(() -> Map.of(FishType.SALMON, 1), () -> Map.of(CookingRecipe.SALMON_DINNER_RECIPE, 1), 1),
                    new Mission(() -> Map.of(MineralType.WOOD, 200), () -> Map.of(CraftType.DELUXE_SCARECROW, 3), Season.SUMMER)
            ),
            4),

    RABIN("rabin", "",
            () -> List.of(FoodType.SPAGHETTI, MineralType.WOOD, MadeProductType.IRON_BAR),
            () -> List.of(
                    new Mission(() -> Map.of(MineralType.WOOD, 80), () -> Map.of(MineralType.GOLD, 1000), 0),
                    new Mission(() -> Map.of(MadeProductType.IRON_BAR, 10), () -> Map.of(CraftType.BEE_HOUSE, 3), 1),
                    new Mission(() -> Map.of(MineralType.WOOD, 1000), () -> Map.of(MineralType.GOLD, 25_000), Season.WINTER)
            ),
            5);

    private Integer id;
    private final String name;
    private final String job;
    private String personality;
    private NPCHouse NPCHouse;
    private List<Mission> missions = new ArrayList<>();
    private int missionSeasonDis;

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

    NPCType(String name, String job, IngredientsSupplier ingredientsSupplier,
            MissionsSupplier missionsSupplier, int missionSeasonDis) {
        this.name = name;
        this.job = job;
        this.ingredientsSupplier = ingredientsSupplier;
        this.missionsSupplier = missionsSupplier;
        this.missionSeasonDis = missionSeasonDis;
    }
}
