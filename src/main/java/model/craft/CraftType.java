package model.craft;

import lombok.Getter;
import lombok.ToString;
import model.Pair;
import model.Salable;
import model.Tile;
import model.abilitiy.Ability;
import model.products.Product;
import model.products.TreesAndFruitsAndSeeds.MadeProductType;
import model.records.Response;
import model.relations.Player;
import model.source.MineralType;
import model.source.SeedType;
import model.structure.stores.StoreType;
import model.tools.BackPack;
import utils.App;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@ToString
public enum CraftType implements Product {
    CHERRY_BOMB("cherry_bomb", "هرچیز در شعاع ۳ تایلی را نابود میکند", () -> Map.of(MineralType.COPPER_ORE, 4, MineralType.COAL, 1), 50, Map.of(Ability.MINING, 1)) {
        public List<Tile> getTilesAffected(Tile origin) {
            Pair current = new Pair(origin.getX(), origin.getY());
            Tile[][] tiles = App.getInstance().getCurrentGame().tiles;
            return getTilesInRadius(current, 3, tiles);
        }
    },
    BOMB("bomb", "هرچیز در شعاع  ۵ تایلی را نابود میکند", () -> Map.of(MineralType.IRON_ORE, 4, MineralType.COAL, 1), 50, Map.of(Ability.MINING, 2)) {
        public List<Tile> getTilesAffected(Tile origin) {
            Pair current = new Pair(origin.getX(), origin.getY());
            Tile[][] tiles = App.getInstance().getCurrentGame().tiles;
            return getTilesInRadius(current, 5, tiles);
        }
    },
    MEGA_BOMB("mega_bomb", "هرچیز در شعاع ۷ تایلی را نابود میکند", () -> Map.of(MineralType.GOLD_ORE, 4, MineralType.COAL, 1), 50, Map.of(Ability.MINING, 3)) {
        public List<Tile> getTilesAffected(Tile origin) {
            Pair current = new Pair(origin.getX(), origin.getY());
            Tile[][] tiles = App.getInstance().getCurrentGame().tiles;
            return getTilesInRadius(current, 7, tiles);
        }
    },
    SPRINKLER("sprinklers", "به ۴ تایل مجاور آب میدهد", () -> Map.of(MadeProductType.IRON_BAR, 1, MadeProductType.COPPER_BAR, 1), 0, Map.of(Ability.FARMING, 1)) {
        public List<Tile> getTilesAffected(Tile origin) {
            Pair current = new Pair(origin.getX(), origin.getY());
            Tile[][] tiles = App.getInstance().getCurrentGame().tiles;
            return getTilesInRadius(current, 4, tiles);
        }
    },
    QUALITY_SPRINKLER("quality_sprinklers", "به ۸ تایل مجاور آب میدهد", () -> Map.of(MadeProductType.IRON_BAR, 1, MadeProductType.GOLD_BAR, 1), 0, Map.of(Ability.FARMING, 2)) {
        public List<Tile> getTilesAffected(Tile origin) {
            Pair current = new Pair(origin.getX(), origin.getY());
            Tile[][] tiles = App.getInstance().getCurrentGame().tiles;
            return getTilesInRadius(current, 8, tiles);
        }
    },
    IRIDIUM_SPRINKLER("iridium_sprinklers", "به ۲۴ تایل مجاور آب میدهد", () -> Map.of(MadeProductType.IRIDIUM_BAR, 1, MadeProductType.GOLD_BAR, 1), 0, Map.of(Ability.FARMING, 3)) {
        public List<Tile> getTilesAffected(Tile origin) {
            Pair current = new Pair(origin.getX(), origin.getY());
            Tile[][] tiles = App.getInstance().getCurrentGame().tiles;
            return getTilesInRadius(current, 24, tiles);
        }
    },
    CHARCOAL_KLIN("charcoal_klin", "۱۰ چوب را تبدیل به ۱ ذغال میکند", () -> Map.of(MineralType.WOOD, 20, MadeProductType.COPPER_BAR, 2), 0, Map.of(Ability.FORAGING, 1)),
    FURNACE("furnace", "کانی ها و ذغال را تبدیل به شمش میکند", () -> Map.of(MineralType.COPPER_ORE, 20, MineralType.STONE, 25), 0, Map.of()),
    SCARE_CROW("scare_crow", "از حمله کلاغ ها تا شعاع ۸ تایلی جلوگیری میکند", () -> Map.of(MineralType.COAL, 1, MineralType.WOOD, 50, MineralType.FIBER, 20), 0, Map.of()) {
        public List<Tile> getTilesAffected(Tile origin) {
            Pair current = new Pair(origin.getX(), origin.getY());
            Tile[][] tiles = App.getInstance().getCurrentGame().tiles;
            return getTilesInRadius(current, 8, tiles);
        }
    },
    DELUXE_SCARECROW("deluxe_scarecrow", "از حمله کلاغ ها تا شعاع 12 تایلی جلوگیری میکند", () -> Map.of(MineralType.WOOD, 50, MineralType.COAL, 1, MineralType.FIBER, 20, MineralType.IRIDIUM_ORE, 1), 0, Map.of(Ability.FARMING, 2)) {
        public List<Tile> getTilesAffected(Tile origin) {
            Pair current = new Pair(origin.getX(), origin.getY());
            Tile[][] tiles = App.getInstance().getCurrentGame().tiles;
            return getTilesInRadius(current, 12, tiles);
        }
    },
    BEE_HOUSE("bee_house", "اگر در مزرعه گذاشته شود عسل تولید میکند", () -> Map.of(MineralType.WOOD, 40, MineralType.COAL, 8, MadeProductType.IRON_BAR, 1), 0, Map.of(Ability.FARMING, 1)),
    CHEESE_PRESS("cheese_press", "شیر را به پنیر تبدیل میکند", () -> Map.of(MineralType.WOOD, 45, MineralType.STONE, 45, MadeProductType.COPPER_BAR, 1), 0, Map.of(Ability.FARMING, 2)),
    KEG("keg", "میوه و سبزیجات را به نوشیدنی تبدیل میکند", () -> Map.of(MineralType.WOOD, 30, MadeProductType.COPPER_BAR, 1, MadeProductType.IRON_BAR, 1), 0, Map.of(Ability.FARMING, 3)),
    LOOM("loom", "پشم را به پارچه تبدیل میکند", () -> Map.of(MineralType.WOOD, 60, MineralType.FIBER, 30), 0, Map.of(Ability.FARMING, 3)),
    MAYONNAISE_MACHINE("mayonnaise_machine", "تخم مرغ را به سس مایونز تبدیل میکند", () -> Map.of(MineralType.WOOD, 15, MineralType.STONE, 15, MadeProductType.COPPER_BAR, 1), 0, Map.of()),
    OIL_MAKER("oil_maker", "truffle را به روغن تبدیل میکند", () -> Map.of(MineralType.WOOD, 100, MadeProductType.IRON_BAR, 1, MadeProductType.GOLD_BAR, 1), 0, Map.of(Ability.FARMING, 3)),
    PRESERVES_JAR("preserves_jar", "سبزیجات را به ترشی و میوه ها را به مربا تبدیل میکند", () -> Map.of(MineralType.WOOD, 50, MineralType.STONE, 40, MineralType.COAL, 8), 0, Map.of(Ability.FARMING, 2)),
    DEHYDRATOR("dehydrator", "میوه یا قارچ را خشک میکند", () -> Map.of(MineralType.WOOD, 30, MineralType.STONE, 20, MineralType.FIBER, 30), 0, StoreType.PIERRE_SHOP),
    GRASS_STARTER("grass_starter", "باعث رشد علف میشود", () -> Map.of(MineralType.WOOD, 1, MineralType.FIBER, 1), 0, StoreType.PIERRE_SHOP),
    FISH_SMOKER("fish_smoker", "هر ماهی را با یک ذغال با حفظ کیفیتش تبدیل به ماهی دودی میکند", () -> Map.of(MineralType.WOOD, 50, MineralType.COAL, 10, MadeProductType.IRON_BAR, 3), 0, StoreType.FISH_SHOP),
    MYSTIC_TREE_SEEDS("mystic_tree_seed", "میتواند کاشته شود تا mystic tree رشد کند", () -> Map.of(SeedType.PINE_CONES, 5, SeedType.MAHOGANY_SEEDS, 5, SeedType.ACORNS, 5, SeedType.MAPLE_SEEDS, 5), 100, Map.of(Ability.MINING, 4));

    @FunctionalInterface
    private interface IngredientsSupplier {
        Map<Salable, Integer> get();
    }

    private transient volatile Map<Salable, Integer> resolvedIngredients;
    private final CraftType.IngredientsSupplier ingredientsSupplier;

    public Map<Salable, Integer> getIngredients() {
        if (resolvedIngredients == null) {
            resolvedIngredients = ingredientsSupplier.get();
        }
        return resolvedIngredients;
    }

    CraftType(String name, String description, IngredientsSupplier ingredientsSupplier, Integer sellPrice, Map<Ability, Integer> abilities) {
        this.name = name;
        this.description = description;
        this.ingredientsSupplier = ingredientsSupplier;
        this.sellPrice = sellPrice;
        this.abilities = abilities;
    }

    CraftType(String name, String description, IngredientsSupplier ingredientsSupplier, Integer sellPrice, StoreType storeType) {
        this.name = name;
        this.description = description;
        this.ingredientsSupplier = ingredientsSupplier;
        this.sellPrice = sellPrice;
        this.storeType = storeType;
    }

    private final String name;
    private final String description;
    private final Integer sellPrice;
    private Map<Ability, Integer> abilities;
    private StoreType storeType;

    public int getSellPrice() {
        return this.sellPrice;
    }

    @Override
    public String getName() {
        return this.name.toLowerCase();
    }

    public List<Tile> getTilesAffected(Tile origin) {
        return null;
    }

    public String getProductsString() {
        String res = "";
        for (Salable salable : this.getIngredients().keySet()) {
            res += salable.getName();
            if (this.getIngredients().get(salable) != 1) res += " x" + this.getIngredients().get(salable);
            res += ", ";
        }
        return res.substring(0, res.length()-2) + "\n";
    }

    public Response isCraftingPossible(Player player) {
        BackPack inventory = player.getInventory();
        for (Salable salable : this.getIngredients().keySet()) {
            if (!inventory.checkProductAvailabilityInBackPack(salable.getName(), this.getIngredients().get(salable))) {
                return new Response("You lack " + (this.getIngredients().get(salable)
                        - inventory.countProductFromBackPack(salable.getName()))
                        + " of " + salable.getName() + " in order to craft this item.");
            }
        }
        return new Response("You can craft this item.", true);
    }

    public void removeIngredients(Player player) {
        BackPack inventory = player.getInventory();
        for (Map.Entry<Salable, Integer> entry : this.getIngredients().entrySet()) {
            Salable salable = inventory.findProductInBackPackByNAme(entry.getKey().getName());
            inventory.deleteProductFromBackPack(salable, player, entry.getValue());
        }
    }

    @Override
    public int getEnergy() {
        return 0;
    }

    private List<Tile> getTilesByDiffPairs(Pair origin, ArrayList<Pair> pairs) {
        Tile[][] tiles = App.getInstance().getCurrentGame().tiles;
        List<Tile> affectedTiles = new ArrayList<>();
        for (Pair pair : pairs) {
            affectedTiles.add(getTileByDiff(origin, pair.getX(), pair.getY(), tiles));
        }
        return affectedTiles;
    }

    private static List<Tile> getTilesInRadius(Pair origin, int r, Tile[][] tiles) {
        List<Tile> affectedTiles = new ArrayList<>();
        for (int i = -r; i <= r; i++) {
            for (int j = -r + Math.abs(i); j <= r - Math.abs(i); j++) {
                affectedTiles.add(getTileByDiff(origin, i, j, tiles));
            }
        }
        return affectedTiles;
    }

    private static Tile getTileByDiff(Pair origin, int diffX, int diffY, Tile[][] tiles) {
        return tiles[origin.getX() + diffX][origin.getY() + diffY];
    }

    @Override
    public Integer getContainingEnergy() {return 0;}
}