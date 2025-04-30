package model.structure.stores;

import model.Salable;
import model.gameSundry.SundryType;
import model.products.TreesAndFruitsAndSeeds.MadeProductType;
import model.records.Response;
import model.source.SeedType;
import model.tools.BackPackType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum PierreShop implements Shop {
    // Miscellaneous Items
    RICE(SundryType.RICE, SundryType.RICE.getName(),
            "A basic grain used in many recipes.",
            SundryType.RICE.getPrice() * 2, -1),
    WHEAT_FLOUR(SundryType.WHEAT_FLOUR, SundryType.WHEAT_FLOUR.getName(),
            "Ground wheat for baking.",
            SundryType.WHEAT_FLOUR.getPrice() * 2, -1),
    BOUQUET(SundryType.BOUQUET, SundryType.BOUQUET.getName(),
            "A lovely arrangement of flowers.",
            SundryType.BOUQUET.getPrice() * 2, 2),
    WEDDING_RING(SundryType.WEDDING_RING, SundryType.WEDDING_RING.getName(),
            "A symbol of eternal love.",
            SundryType.WEDDING_RING.getPrice() * 2, 2),
    SUGAR(SundryType.SUGAR, SundryType.SUGAR.getName(),
            "Sweet crystalline substance.",
            SundryType.SUGAR.getPrice() * 2, 1),
    OIL(MadeProductType.OIL, MadeProductType.OIL.getName(),
            "For cooking and crafting.",
            MadeProductType.OIL.getSellPrice() * 2, 1),
    VINEGAR(SundryType.VINEGAR, SundryType.VINEGAR.getName(),
            "A sour liquid used in cooking.",
            SundryType.VINEGAR.getPrice() * 2, -1),
    DELUXE_RETAINING_SOIL(SundryType.DELUXE_RETAINING_SOIL, SundryType.DELUXE_RETAINING_SOIL.getName(),
            "Keeps soil watered overnight.",
            SundryType.DELUXE_RETAINING_SOIL.getPrice() * 2, -1),
    GRASS_STARTER(SundryType.GRASS_STARTER, SundryType.GRASS_STARTER.getName(),
            "Plant to grow grass outside.",
            SundryType.GRASS_STARTER.getPrice() * 2, -1),
    SPEED_GRO(SundryType.SPEED_GROW, SundryType.SPEED_GROW.getName(),
            "Speeds up plant growth by 10%.",
            SundryType.SPEED_GROW.getPrice() * 2, -1),
    BASIC_RETAINING_SOIL(SundryType.BASIC_RETAINING_SOIL, SundryType.BASIC_RETAINING_SOIL.getName(),
            "Basic water-retaining soil.",
            SundryType.BASIC_RETAINING_SOIL.getPrice() * 2, -1),
    QUALITY_RETAINING_SOIL(SundryType.QUALITY_RETAINING_SOIL, SundryType.QUALITY_RETAINING_SOIL.getName(),
            "High-quality water-retaining soil.",
            SundryType.QUALITY_RETAINING_SOIL.getPrice() * 2, -1),
    LARGE_PACK(BackPackType.BIG_BACKPACK, BackPackType.BIG_BACKPACK.getName(),
            "Increases inventory capacity.",
            2000, 1),
    DELUXE_PACK(BackPackType.BIG_BACKPACK, BackPackType.BIG_BACKPACK.getName(),
            "Maximizes inventory capacity.",
            10000, 1),

    // Fruit Trees
    APPLE_SAPLING(SeedType.APPLE_SAPLING, "Apple Sapling",
            "Takes 28 days to mature. Produces apples in fall.",
            4000, -1),
    APRICOT_SAPLING(SeedType.APRICOT_SAPLING, "Apricot Sapling",
            "Takes 28 days to mature. Produces apricots in spring.",
            2000, -1),
    CHERRY_SAPLING(SeedType.CHERRY_SAPLING, "Cherry Sapling",
            "Takes 28 days to mature. Produces cherries in spring.",
            3400, -1),
    ORANGE_SAPLING(SeedType.ORANGE_SAPLING, "Orange Sapling",
            "Takes 28 days to mature. Produces oranges in summer.",
            4000, -1),
    PEACH_SAPLING(SeedType.PEACH_SAPLING, "Peach Sapling",
            "Takes 28 days to mature. Produces peaches in summer.",
            6000, -1),
    POMEGRANATE_SAPLING(SeedType.POMEGRANATE_SAPLING, "Pomegranate Sapling",
            "Takes 28 days to mature. Produces pomegranates in fall.",
            6000, -1),

    // Spring Seeds
    PARSNIP_SEEDS(SeedType.PARSNIP_SEEDS, "Parsnip Seeds",
            "Plant in spring. Takes 4 days to mature.",
            20, 30, 5, Season.SPRING),
    BEAN_STARTER(SeedType.BEAN_STARTER, "Bean Starter",
            "Plant in spring. Takes 10 days, keeps producing. Grows on trellis.",
            60, 90, 5, Season.SPRING),
    CAULIFLOWER_SEEDS(SeedType.CAULIFLOWER_SEEDS, "Cauliflower Seeds",
            "Plant in spring. Takes 12 days for large cauliflower.",
            80, 120, 5, Season.SPRING),
    POTATO_SEEDS(SeedType.POTATO_SEEDS, "Potato Seeds",
            "Plant in spring. Takes 6 days, chance for multiple potatoes.",
            50, 75, 5, Season.SPRING),
    TULIP_BULB(SeedType.TULIP_BULB, "Tulip Bulb",
            "Plant in spring. Takes 6 days for colorful flowers.",
            20, 30, 5, Season.SPRING),
    KALE_SEEDS(SeedType.KALE_SEEDS, "Kale Seeds",
            "Plant in spring. Takes 6 days. Harvest with scythe.",
            70, 105, 5, Season.SPRING),
    JAZZ_SEEDS(SeedType.JAZZ_SEEDS, "Jazz Seeds",
            "Plant in spring. Takes 7 days for blue puffball flowers.",
            30, 45, 5, Season.SPRING),
    GARLIC_SEEDS(SeedType.GARLIC_SEEDS, "Garlic Seeds",
            "Plant in spring. Takes 4 days to mature.",
            40, 60, 5, Season.SPRING),
    RICE_SHOOT(SeedType.RICE_SHOOT, "Rice Shoot",
            "Plant in spring. Takes 8 days. Grows faster near water.",
            40, 60, 5, Season.SPRING),

    // Summer Seeds
    MELON_SEEDS(SeedType.MELON_SEEDS, "Melon Seeds",
            "Plant in summer. Takes 12 days to mature.",
            80, 120, 5, Season.SUMMER),
    TOMATO_SEEDS(SeedType.TOMATO_SEEDS, "Tomato Seeds",
            "Plant in summer. Takes 11 days, keeps producing.",
            50, 75, 5, Season.SUMMER),
    BLUEBERRY_SEEDS(SeedType.BLUEBERRY_SEEDS, "Blueberry Seeds",
            "Plant in summer. Takes 13 days, keeps producing.",
            80, 120, 5, Season.SUMMER),
    PEPPER_SEEDS(SeedType.PEPPER_SEEDS, "Pepper Seeds",
            "Plant in summer. Takes 5 days, keeps producing.",
            40, 60, 5, Season.SUMMER),
    WHEAT_SEEDS(SeedType.WHEAT_SEEDS, "Wheat Seeds",
            "Plant in summer/fall. Takes 4 days. Harvest with scythe.",
            10, 15, 5, Season.SUMMER),
    RADISH_SEEDS(SeedType.RADISH_SEEDS, "Radish Seeds",
            "Plant in summer. Takes 6 days to mature.",
            40, 60, 5, Season.SUMMER),
    POPPY_SEEDS(SeedType.POPPY_SEEDS, "Poppy Seeds",
            "Plant in summer. Takes 7 days for red flowers.",
            100, 150, 5, Season.SUMMER),
    SPANGLE_SEEDS(SeedType.SPANGLE_SEEDS, "Spangle Seeds",
            "Plant in summer. Takes 8 days for tropical flowers.",
            50, 75, 5, Season.SUMMER),
    HOPS_STARTER(SeedType.HOPS_STARTER, "Hops Starter",
            "Plant in summer. Takes 11 days, keeps producing. Trellis.",
            60, 90, 5, Season.SUMMER),
    CORN_SEEDS(SeedType.CORN_SEEDS, "Corn Seeds",
            "Plant in summer/fall. Takes 14 days, keeps producing.",
            150, 225, 5, Season.SUMMER),
    SUNFLOWER_SEEDS(SeedType.SUNFLOWER_SEEDS, "Sunflower Seeds",
            "Plant in summer/fall. Takes 8 days. Yields more seeds.",
            200, 300, 5, Season.SUMMER),
    RED_CABBAGE_SEEDS(SeedType.RED_CABBAGE_SEEDS, "Red Cabbage Seeds",
            "Plant in summer. Takes 9 days to mature.",
            100, 150, 5, Season.SUMMER),

    // Fall Seeds
    EGGPLANT_SEEDS(SeedType.EGGPLANT_SEEDS, "Eggplant Seeds",
            "Plant in fall. Takes 5 days, keeps producing.",
            20, 30, 5, Season.FALL),
    PUMPKIN_SEEDS(SeedType.PUMPKIN_SEEDS, "Pumpkin Seeds",
            "Plant in fall. Takes 13 days to mature.",
            100, 150, 5, Season.FALL),
    BOK_CHOY_SEEDS(SeedType.BOK_CHOY_SEEDS, "Bok Choy Seeds",
            "Plant in fall. Takes 4 days to mature.",
            50, 75, 5, Season.FALL),
    YAM_SEEDS(SeedType.YAM_SEEDS, "Yam Seeds",
            "Plant in fall. Takes 10 days to mature.",
            60, 90, 5, Season.FALL),
    CRANBERRY_SEEDS(SeedType.CRANBERRY_SEEDS, "Cranberry Seeds",
            "Plant in fall. Takes 7 days, keeps producing.",
            240, 360, 5, Season.FALL),
    FAIRY_SEEDS(SeedType.FAIRY_SEEDS, "Fairy Seeds",
            "Plant in fall. Takes 12 days for mysterious flowers.",
            200, 300, 5, Season.FALL),
    AMARANTH_SEEDS(SeedType.AMARANTH_SEEDS, "Amaranth Seeds",
            "Plant in fall. Takes 7 days. Harvest with scythe.",
            70, 105, 5, Season.FALL),
    GRAPE_STARTER(SeedType.GRAPE_STARTER, "Grape Starter",
            "Plant in fall. Takes 10 days, keeps producing. Trellis.",
            60, 90, 5, Season.FALL),
    ARTICHOKE_SEEDS(SeedType.ARTICHOKE_SEEDS, "Artichoke Seeds",
            "Plant in fall. Takes 8 days to mature.",
            30, 45, 5, Season.FALL);

    private final Salable salable;
    private final String name;
    private final String description;
    private final int inSeasonPrice;
    private final int outOfSeasonPrice;
    private int dailySold;
    private final int dailyLimit;
    private final Season season;

    public enum Season {
        SPRING, SUMMER, FALL, WINTER
    }

    // Constructor for non-seasonal items
    PierreShop(Salable salable, String name, String description, int price, int dailyLimit) {
        this(salable, name, description, price, price, dailyLimit, null);
    }

    // Constructor for seasonal items
    PierreShop(Salable salable, String name, String description,
               int inSeasonPrice, int outOfSeasonPrice, int dailyLimit, Season season) {
        this.salable = salable;
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
    // This should be implemented based on your game's calendar system
    private Season getCurrentSeason() {
        // Placeholder - replace with actual game season tracking
        // Example: return GameState.getCurrentSeason();
        return Season.SPRING; // Default for example
    }

    public static List<PierreShop> getProductsForSeason(Season season) {
        return Arrays.stream(values())
                .filter(item -> item.season == season)
                .collect(Collectors.toList());
    }

    public static List<PierreShop> getAvailableProducts() {
        return Arrays.stream(values())
                .filter(item -> item.dailyLimit == -1 || item.dailySold < item.dailyLimit)
                .collect(Collectors.toList());
    }



    public static Response purchase(String name, int quantity) {
        return Response.empty();//TODO
    }

    public void resetDailyStock() {
        this.dailySold = 0;
    }

    public static void resetAllDailyStock() {
        for (PierreShop item : values()) {
            item.resetDailyStock();
        }
    }

    // Getters
    public Salable getSalable() {
        return salable;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getInSeasonPrice() {
        return inSeasonPrice;
    }

    public int getOutOfSeasonPrice() {
        return outOfSeasonPrice;
    }

    public int getDailySold() {
        return dailySold;
    }

    public int getDailyLimit() {
        return dailyLimit;
    }

    public Season getSeason() {
        return season;
    }
}