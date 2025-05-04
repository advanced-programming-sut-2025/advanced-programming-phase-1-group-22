package model.structure.stores;

import lombok.Getter;
import model.Salable;
import model.enums.Season;
import model.exception.InvalidInputException;
import model.gameSundry.Sundry;
import model.gameSundry.SundryType;
import model.products.TreesAndFruitsAndSeeds.MadeProduct;
import model.products.TreesAndFruitsAndSeeds.MadeProductType;
import model.receipe.CraftingRecipe;
import model.records.Response;
import model.relations.Player;
import model.source.Seed;
import model.source.SeedType;
import model.tools.BackPack;
import model.tools.BackPackType;
import model.tools.Tool;
import utils.App;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum PierreShop implements Shop {
    // Miscellaneous Items
    DEHYDRATOR_RECIPE(() -> CraftingRecipe.DEHYDRATOR_RECIPE, CraftingRecipe.DEHYDRATOR_RECIPE.getName(),
            "dehydrator recipe.",
            CraftingRecipe.DEHYDRATOR_RECIPE.getPrice(), 1),
    GRASS_STARTER_RECIPE(() -> CraftingRecipe.GRASS_STARTER_RECIPE, CraftingRecipe.GRASS_STARTER_RECIPE.getName(),
            "grass starter recipe.",
            CraftingRecipe.GRASS_STARTER_RECIPE.getPrice(), 1),
    RICE(() -> SundryType.RICE, SundryType.RICE.getName(),
            "A basic grain used in many recipes.",
            SundryType.RICE.getPrice() * 2, -1),
    WHEAT_FLOUR(() -> SundryType.WHEAT_FLOUR, SundryType.WHEAT_FLOUR.getName(),
            "Ground wheat for baking.",
            SundryType.WHEAT_FLOUR.getPrice() * 2, -1),
    BOUQUET(() -> SundryType.BOUQUET, SundryType.BOUQUET.getName(),
            "A lovely arrangement of flowers.",
            SundryType.BOUQUET.getPrice() * 2, 2),
    WEDDING_RING(() -> SundryType.WEDDING_RING, SundryType.WEDDING_RING.getName(),
            "A symbol of eternal love.",
            SundryType.WEDDING_RING.getPrice() * 2, 2),
    SUGAR(() -> SundryType.SUGAR, SundryType.SUGAR.getName(),
            "Sweet crystalline substance.",
            SundryType.SUGAR.getPrice() * 2, 1),
    OIL(() -> MadeProductType.OIL, MadeProductType.OIL.getName(),
            "For cooking and crafting.",
            MadeProductType.OIL.getSellPrice() * 2, 1),
    VINEGAR(() -> SundryType.VINEGAR, SundryType.VINEGAR.getName(),
            "A sour liquid used in cooking.",
            SundryType.VINEGAR.getPrice() * 2, -1),
    DELUXE_RETAINING_SOIL(() -> SundryType.DELUXE_RETAINING_SOIL, SundryType.DELUXE_RETAINING_SOIL.getName(),
            "Keeps soil watered overnight.",
            SundryType.DELUXE_RETAINING_SOIL.getPrice() * 2, -1),
    GRASS_STARTER(() -> SundryType.GRASS_STARTER, SundryType.GRASS_STARTER.getName(),
            "Plant to grow grass outside.",
            SundryType.GRASS_STARTER.getPrice() * 2, -1),
    SPEED_GRO(() -> SundryType.SPEED_GROW, SundryType.SPEED_GROW.getName(),
            "Speeds up plant growth by 10%.",
            SundryType.SPEED_GROW.getPrice() * 2, -1),
    BASIC_RETAINING_SOIL(() -> SundryType.BASIC_RETAINING_SOIL, SundryType.BASIC_RETAINING_SOIL.getName(),
            "Basic water-retaining soil.",
            SundryType.BASIC_RETAINING_SOIL.getPrice() * 2, -1),
    QUALITY_RETAINING_SOIL(() -> SundryType.QUALITY_RETAINING_SOIL, SundryType.QUALITY_RETAINING_SOIL.getName(),
            "High-quality water-retaining soil.",
            SundryType.QUALITY_RETAINING_SOIL.getPrice() * 2, -1),
    LARGE_PACK(() -> BackPackType.BIG_BACKPACK, BackPackType.BIG_BACKPACK.getName(),
            "Increases inventory capacity.",
            2000, 1),
    DELUXE_PACK(() -> BackPackType.BIG_BACKPACK, BackPackType.BIG_BACKPACK.getName(),
            "Maximizes inventory capacity.",
            10000, 1),

    // Fruit Trees
    APPLE_SAPLING(() -> SeedType.APPLE_SAPLING, "Apple Sapling",
            "Takes 28 days to mature. Produces apples in fall.",
            4000, -1),
    APRICOT_SAPLING(() -> SeedType.APRICOT_SAPLING, "Apricot Sapling",
            "Takes 28 days to mature. Produces apricots in spring.",
            2000, -1),
    CHERRY_SAPLING(() -> SeedType.CHERRY_SAPLING, "Cherry Sapling",
            "Takes 28 days to mature. Produces cherries in spring.",
            3400, -1),
    ORANGE_SAPLING(() -> SeedType.ORANGE_SAPLING, "Orange Sapling",
            "Takes 28 days to mature. Produces oranges in summer.",
            4000, -1),
    PEACH_SAPLING(() -> SeedType.PEACH_SAPLING, "Peach Sapling",
            "Takes 28 days to mature. Produces peaches in summer.",
            6000, -1),
    POMEGRANATE_SAPLING(() -> SeedType.POMEGRANATE_SAPLING, "Pomegranate Sapling",
            "Takes 28 days to mature. Produces pomegranates in fall.",
            6000, -1),

    // Spring Seeds
    PARSNIP_SEEDS(() -> SeedType.PARSNIP_SEEDS, "Parsnip Seeds",
            "Plant in spring. Takes 4 days to mature.",
            20, 30, 5, Season.SPRING),
    BEAN_STARTER(() -> SeedType.BEAN_STARTER, "Bean Starter",
            "Plant in spring. Takes 10 days, keeps producing. Grows on trellis.",
            60, 90, 5, Season.SPRING),
    CAULIFLOWER_SEEDS(() -> SeedType.CAULIFLOWER_SEEDS, "Cauliflower Seeds",
            "Plant in spring. Takes 12 days for large cauliflower.",
            80, 120, 5, Season.SPRING),
    POTATO_SEEDS(() -> SeedType.POTATO_SEEDS, "Potato Seeds",
            "Plant in spring. Takes 6 days, chance for multiple potatoes.",
            50, 75, 5, Season.SPRING),
    TULIP_BULB(() -> SeedType.TULIP_BULB, "Tulip Bulb",
            "Plant in spring. Takes 6 days for colorful flowers.",
            20, 30, 5, Season.SPRING),
    KALE_SEEDS(() -> SeedType.KALE_SEEDS, "Kale Seeds",
            "Plant in spring. Takes 6 days. Harvest with scythe.",
            70, 105, 5, Season.SPRING),
    JAZZ_SEEDS(() -> SeedType.JAZZ_SEEDS, "Jazz Seeds",
            "Plant in spring. Takes 7 days for blue puffball flowers.",
            30, 45, 5, Season.SPRING),
    GARLIC_SEEDS(() -> SeedType.GARLIC_SEEDS, "Garlic Seeds",
            "Plant in spring. Takes 4 days to mature.",
            40, 60, 5, Season.SPRING),
    RICE_SHOOT(() -> SeedType.RICE_SHOOT, "Rice Shoot",
            "Plant in spring. Takes 8 days. Grows faster near water.",
            40, 60, 5, Season.SPRING),

    // Summer Seeds
    MELON_SEEDS(() -> SeedType.MELON_SEEDS, "Melon Seeds",
            "Plant in summer. Takes 12 days to mature.",
            80, 120, 5, Season.SUMMER),
    TOMATO_SEEDS(() -> SeedType.TOMATO_SEEDS, "Tomato Seeds",
            "Plant in summer. Takes 11 days, keeps producing.",
            50, 75, 5, Season.SUMMER),
    BLUEBERRY_SEEDS(() -> SeedType.BLUEBERRY_SEEDS, "Blueberry Seeds",
            "Plant in summer. Takes 13 days, keeps producing.",
            80, 120, 5, Season.SUMMER),
    PEPPER_SEEDS(() -> SeedType.PEPPER_SEEDS, "Pepper Seeds",
            "Plant in summer. Takes 5 days, keeps producing.",
            40, 60, 5, Season.SUMMER),
    WHEAT_SEEDS(() -> SeedType.WHEAT_SEEDS, "Wheat Seeds",
            "Plant in summer/fall. Takes 4 days. Harvest with scythe.",
            10, 15, 5, Season.SUMMER),
    RADISH_SEEDS(() -> SeedType.RADISH_SEEDS, "Radish Seeds",
            "Plant in summer. Takes 6 days to mature.",
            40, 60, 5, Season.SUMMER),
    POPPY_SEEDS(() -> SeedType.POPPY_SEEDS, "Poppy Seeds",
            "Plant in summer. Takes 7 days for red flowers.",
            100, 150, 5, Season.SUMMER),
    SPANGLE_SEEDS(() -> SeedType.SPANGLE_SEEDS, "Spangle Seeds",
            "Plant in summer. Takes 8 days for tropical flowers.",
            50, 75, 5, Season.SUMMER),
    HOPS_STARTER(() -> SeedType.HOPS_STARTER, "Hops Starter",
            "Plant in summer. Takes 11 days, keeps producing. Trellis.",
            60, 90, 5, Season.SUMMER),
    CORN_SEEDS(() -> SeedType.CORN_SEEDS, "Corn Seeds",
            "Plant in summer/fall. Takes 14 days, keeps producing.",
            150, 225, 5, Season.SUMMER),
    SUNFLOWER_SEEDS(() -> SeedType.SUNFLOWER_SEEDS, "Sunflower Seeds",
            "Plant in summer/fall. Takes 8 days. Yields more seeds.",
            200, 300, 5, Season.SUMMER),
    RED_CABBAGE_SEEDS(() -> SeedType.RED_CABBAGE_SEEDS, "Red Cabbage Seeds",
            "Plant in summer. Takes 9 days to mature.",
            100, 150, 5, Season.SUMMER),

    // Fall Seeds
    EGGPLANT_SEEDS(() -> SeedType.EGGPLANT_SEEDS, "Eggplant Seeds",
            "Plant in fall. Takes 5 days, keeps producing.",
            20, 30, 5, Season.FALL),
    PUMPKIN_SEEDS(() -> SeedType.PUMPKIN_SEEDS, "Pumpkin Seeds",
            "Plant in fall. Takes 13 days to mature.",
            100, 150, 5, Season.FALL),
    BOK_CHOY_SEEDS(() -> SeedType.BOK_CHOY_SEEDS, "Bok Choy Seeds",
            "Plant in fall. Takes 4 days to mature.",
            50, 75, 5, Season.FALL),
    YAM_SEEDS(() -> SeedType.YAM_SEEDS, "Yam Seeds",
            "Plant in fall. Takes 10 days to mature.",
            60, 90, 5, Season.FALL),
    CRANBERRY_SEEDS(() -> SeedType.CRANBERRY_SEEDS, "Cranberry Seeds",
            "Plant in fall. Takes 7 days, keeps producing.",
            240, 360, 5, Season.FALL),
    FAIRY_SEEDS(() -> SeedType.FAIRY_SEEDS, "Fairy Seeds",
            "Plant in fall. Takes 12 days for mysterious flowers.",
            200, 300, 5, Season.FALL),
    AMARANTH_SEEDS(() -> SeedType.AMARANTH_SEEDS, "Amaranth Seeds",
            "Plant in fall. Takes 7 days. Harvest with scythe.",
            70, 105, 5, Season.FALL),
    GRAPE_STARTER(() -> SeedType.GRAPE_STARTER, "Grape Starter",
            "Plant in fall. Takes 10 days, keeps producing. Trellis.",
            60, 90, 5, Season.FALL),
    ARTICHOKE_SEEDS(() -> SeedType.ARTICHOKE_SEEDS, "Artichoke Seeds",
            "Plant in fall. Takes 8 days to mature.",
            30, 45, 5, Season.FALL);

    // Getters

    private final String name;
    private final String description;
    private final int inSeasonPrice;
    private final int outOfSeasonPrice;
    private int dailySold;
    private final int dailyLimit;
    private final Season season;

    @FunctionalInterface
    private interface IngredientsSupplier {
        Salable get();
    }

    private transient volatile Salable salable;
    private final PierreShop.IngredientsSupplier ingredientsSupplier;

    public Salable getSalable() {
        if (salable == null) {
            salable = ingredientsSupplier.get();
        }
        return salable;
    }

    // Constructor for non-seasonal items
    PierreShop(IngredientsSupplier salable, String name, String description, int price, int dailyLimit) {
        this(salable, name, description, price, price, dailyLimit, null);
    }

    // Constructor for seasonal items
    PierreShop(IngredientsSupplier salable, String name, String description,
               int inSeasonPrice, int outOfSeasonPrice, int dailyLimit, Season season) {
        this.ingredientsSupplier = salable;
        this.name = name;
        this.description = description;
        this.inSeasonPrice = inSeasonPrice;
        this.outOfSeasonPrice = outOfSeasonPrice;
        this.dailyLimit = dailyLimit;
        this.season = season;
        this.dailySold = 0;
    }

    public int getPrice() {
        if (season == null) {
            return inSeasonPrice;
        }
        return isInSeason() ? inSeasonPrice : outOfSeasonPrice;
    }

    public boolean isInSeason() {
        return season == getCurrentSeason();
    }

    public static String showAllProducts() {
        StringBuilder res = new StringBuilder();
        for (PierreShop value : PierreShop.values()) {
            res.append(value.toString()).append(" ").append(value.getPrice()).append("$\n");
        }
        return res.toString();
    }

    public static String showAvailableProducts() {
        StringBuilder res = new StringBuilder();
        for (PierreShop value : PierreShop.values()) {
            if (value.dailyLimit != value.dailySold) {
                res.append(value.toString()).append(" ").append(value.getPrice()).append("$\n");
            }
        }
        return res.toString();
    }

    private Season getCurrentSeason() {
        return App.getInstance().getCurrentGame().getTimeAndDate().getSeason();
    }

    public static Response purchase(String name, Integer count) {
        PierreShop salable = null;
        for (PierreShop value : PierreShop.values()) {
            if (value.getName().equals(name)) {
                salable = value;
            }
        }
        if (salable == null) return new Response("Item not found");
        if (salable.dailyLimit != -1 && salable.dailyLimit < salable.dailySold + count) {
            return new Response("Not enough in stock");
        }
        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
        if (!player.getInventory().isInventoryHaveCapacity(salable.getSalable())) {
            return new Response("Not enough space in your backpack.");
        }
        if (player.getAccount().getGolds() < salable.getPrice()) {
            return new Response("Not enough golds");
        }
        player.getAccount().removeGolds(salable.getPrice());
        salable.dailySold += count;
        Salable item = null;
        if (salable.getSalable() instanceof CraftingRecipe) {
            player.getCraftingRecipes().put((CraftingRecipe) salable.salable, true);
            return new Response("Bought successfully", true);
        }
        if (salable.getSalable() instanceof SundryType) item = new Sundry((SundryType) salable.salable);
        if (salable.getSalable() instanceof MadeProductType) item = new MadeProduct((MadeProductType) salable.salable);
        if (salable.getSalable() instanceof SeedType) item = new Seed((SeedType) salable.salable);
        if (salable.getSalable() instanceof Tool) item = salable.salable;
        if (item == null) throw new InvalidInputException("Item not found in pierreShop");
        player.getInventory().addProductToBackPack(item, count);
        return new Response("Bought successfully", true);
    }

    public void resetDailySold() {
        dailySold = 0;
    }
}