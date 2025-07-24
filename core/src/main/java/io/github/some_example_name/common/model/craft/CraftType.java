package io.github.some_example_name.common.model.craft;

import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;
import lombok.ToString;
import io.github.some_example_name.common.model.Pair;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.Tile;
import io.github.some_example_name.common.model.abilitiy.Ability;
import io.github.some_example_name.common.model.products.Product;
import io.github.some_example_name.common.model.products.TreesAndFruitsAndSeeds.MadeProductType;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.source.MineralType;
import io.github.some_example_name.common.model.source.SeedType;
import io.github.some_example_name.common.model.structure.stores.StoreType;
import io.github.some_example_name.common.model.tools.BackPack;
import io.github.some_example_name.common.utils.App;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@ToString
public enum CraftType implements Product {
    CHERRY_BOMB("cherry_bomb", "Destroys anything in 3 tiles far.", () -> Map.of(MineralType.COPPER_ORE, 4, MineralType.COAL, 1), 50, Map.of(Ability.MINING, 1), GameAsset.CHERRY_BOMB, GameAsset.CHERRY_BOMB, GameAsset.CHERRY_BOMB) {
        public List<Tile> getTilesAffected(Tile origin) {
            Pair current = new Pair(origin.getX(), origin.getY());
            Tile[][] tiles = App.getInstance().getCurrentGame().tiles;
            return getTilesInRadius(current, 3, tiles);
        }
    },
    BOMB("bomb", "Destroys anything in 5 tiles far.", () -> Map.of(MineralType.IRON_ORE, 4, MineralType.COAL, 1), 50, Map.of(Ability.MINING, 2), GameAsset.BOMB, GameAsset.BOMB, GameAsset.BOMB) {
        public List<Tile> getTilesAffected(Tile origin) {
            Pair current = new Pair(origin.getX(), origin.getY());
            Tile[][] tiles = App.getInstance().getCurrentGame().tiles;
            return getTilesInRadius(current, 5, tiles);
        }
    },
    MEGA_BOMB("mega_bomb", "Destroys anything in 7 tiles far.", () -> Map.of(MineralType.GOLD_ORE, 4, MineralType.COAL, 1), 50, Map.of(Ability.MINING, 3), GameAsset.MEGA_BOMB, GameAsset.MEGA_BOMB, GameAsset.MEGA_BOMB) {
        public List<Tile> getTilesAffected(Tile origin) {
            Pair current = new Pair(origin.getX(), origin.getY());
            Tile[][] tiles = App.getInstance().getCurrentGame().tiles;
            return getTilesInRadius(current, 7, tiles);
        }
    },
    SPRINKLER("sprinklers", "Waters anything in 4 tiles far.", () -> Map.of(MadeProductType.IRON_BAR, 1, MadeProductType.COPPER_BAR, 1), 0, Map.of(Ability.FARMING, 1), GameAsset.SPRINKLER, GameAsset.SPRINKLER, GameAsset.SPRINKLER) {
        public List<Tile> getTilesAffected(Tile origin) {
            Pair current = new Pair(origin.getX(), origin.getY());
            Tile[][] tiles = App.getInstance().getCurrentGame().tiles;
            return getTilesInRadius(current, 4, tiles);
        }
    },
    QUALITY_SPRINKLER("quality_sprinklers", "Waters anything in 8 tiles far.", () -> Map.of(MadeProductType.IRON_BAR, 1, MadeProductType.GOLD_BAR, 1), 0, Map.of(Ability.FARMING, 2), GameAsset.QUALITY_SPRINKLER, GameAsset.QUALITY_SPRINKLER, GameAsset.QUALITY_SPRINKLER) {
        public List<Tile> getTilesAffected(Tile origin) {
            Pair current = new Pair(origin.getX(), origin.getY());
            Tile[][] tiles = App.getInstance().getCurrentGame().tiles;
            return getTilesInRadius(current, 8, tiles);
        }
    },
    IRIDIUM_SPRINKLER("iridium_sprinklers", "Waters anything in 24 tiles far.", () -> Map.of(MadeProductType.IRIDIUM_BAR, 1, MadeProductType.GOLD_BAR, 1), 0, Map.of(Ability.FARMING, 3), GameAsset.IRIDIUM_SPRINKLER, GameAsset.IRIDIUM_SPRINKLER, GameAsset.IRIDIUM_SPRINKLER) {
        public List<Tile> getTilesAffected(Tile origin) {
            Pair current = new Pair(origin.getX(), origin.getY());
            Tile[][] tiles = App.getInstance().getCurrentGame().tiles;
            return getTilesInRadius(current, 24, tiles);
        }
    },
    CHARCOAL_KLIN("charcoal_klin", "Converts 10 pieces of wood to coal.", () -> Map.of(MineralType.WOOD, 20, MadeProductType.COPPER_BAR, 2), 0, Map.of(Ability.FORAGING, 1), GameAsset.CHARCOAL_KILN_OFF, GameAsset.CHARCOAL_KILN, GameAsset.CHARCOAL_KILN),
    FURNACE("furnace", "Converts ore to bar.", () -> Map.of(MineralType.COPPER_ORE, 20, MineralType.STONE, 25), 0, Map.of(), GameAsset.FURNACE, GameAsset.FURNACE_ON, GameAsset.FURNACE),
    SCARE_CROW("scare_crow", "Scares crows in 8 tiles far.", () -> Map.of(MineralType.COAL, 1, MineralType.WOOD, 50, MineralType.FIBER, 20), 0, Map.of(), GameAsset.SCARECROW, GameAsset.SCARECROW, GameAsset.SCARECROW) {
        public List<Tile> getTilesAffected(Tile origin) {
            Pair current = new Pair(origin.getX(), origin.getY());
            Tile[][] tiles = App.getInstance().getCurrentGame().tiles;
            return getTilesInRadius(current, 8, tiles);
        }
    },
    DELUXE_SCARECROW("deluxe_scarecrow", "Scares crows in 12 tiles far.", () -> Map.of(MineralType.WOOD, 50, MineralType.COAL, 1, MineralType.FIBER, 20, MineralType.IRIDIUM_ORE, 1), 0, Map.of(Ability.FARMING, 2), GameAsset.DELUXE_SCARECROW, GameAsset.DELUXE_SCARECROW, GameAsset.DELUXE_SCARECROW) {
        public List<Tile> getTilesAffected(Tile origin) {
            Pair current = new Pair(origin.getX(), origin.getY());
            Tile[][] tiles = App.getInstance().getCurrentGame().tiles;
            return getTilesInRadius(current, 12, tiles);
        }
    },
    BEE_HOUSE("bee_house", "Produces honey if put on the farm.", () -> Map.of(MineralType.WOOD, 40, MineralType.COAL, 8, MadeProductType.IRON_BAR, 1), 0, Map.of(Ability.FARMING, 1), GameAsset.BEE_HOUSE, GameAsset.BEE_HOUSE_FULL, GameAsset.BEE_HOUSE_FULL),
    CHEESE_PRESS("cheese_press", "Presses milk to cheese.", () -> Map.of(MineralType.WOOD, 45, MineralType.STONE, 45, MadeProductType.COPPER_BAR, 1), 0, Map.of(Ability.FARMING, 2), GameAsset.CHEESE_PRESS, GameAsset.CHEESE_PRESS, GameAsset.CHEESE_PRESS) ,
    KEG("keg", "Converts fruits and vegetables to drinks.", () -> Map.of(MineralType.WOOD, 30, MadeProductType.COPPER_BAR, 1, MadeProductType.IRON_BAR, 1), 0, Map.of(Ability.FARMING, 3), GameAsset.KEG, GameAsset.KEG, GameAsset.KEG),
    LOOM("loom", "Converts wool to cloth.", () -> Map.of(MineralType.WOOD, 60, MineralType.FIBER, 30), 0, Map.of(Ability.FARMING, 3), GameAsset.LOOM, GameAsset.LOOM, GameAsset.LOOM_DONE),
    MAYONNAISE_MACHINE("mayonnaise_machine", "Converts eggs to mayonnaise sauce.", () -> Map.of(MineralType.WOOD, 15, MineralType.STONE, 15, MadeProductType.COPPER_BAR, 1), 0, Map.of(), GameAsset.MAYONNAISE_MACHINE, GameAsset.MAYONNAISE_MACHINE, GameAsset.MAYONNAISE_MACHINE),
    OIL_MAKER("oil_maker", "Converts truffle to oil.", () -> Map.of(MineralType.WOOD, 100, MadeProductType.IRON_BAR, 1, MadeProductType.GOLD_BAR, 1), 0, Map.of(Ability.FARMING, 3), GameAsset.OIL_MAKER, GameAsset.OIL_MAKER, GameAsset.OIL_MAKER),
    PRESERVES_JAR("preserves_jar", "Converts vegetables and fruits to jam and pickles.", () -> Map.of(MineralType.WOOD, 50, MineralType.STONE, 40, MineralType.COAL, 8), 0, Map.of(Ability.FARMING, 2), GameAsset.PRESERVES_JAR, GameAsset.PRESERVES_JAR, GameAsset.PRESERVES_JAR),
    DEHYDRATOR("dehydrator", "Dehydrates fruits or mushrooms.", () -> Map.of(MineralType.WOOD, 30, MineralType.STONE, 20, MineralType.FIBER, 30), 0, StoreType.PIERRE_SHOP, GameAsset.DEHYDRATOR, GameAsset.DEHYDRATOR, GameAsset.DEHYDRATOR),
    GRASS_STARTER("grass_starter", "Derives grass to grow.", () -> Map.of(MineralType.WOOD, 1, MineralType.FIBER, 1), 0, StoreType.PIERRE_SHOP, GameAsset.GRASS_STARTER, GameAsset.GRASS_STARTER, GameAsset.GRASS_STARTER),
    FISH_SMOKER("fish_smoker", "Converts a fish to a smoked fish retaining the quality.", () -> Map.of(MineralType.WOOD, 50, MineralType.COAL, 10, MadeProductType.IRON_BAR, 3), 0, StoreType.FISH_SHOP, GameAsset.FISH_SMOKER, GameAsset.FISH_SMOKER_ON, GameAsset.FISH_SMOKER),
    MYSTIC_TREE_SEEDS("mystic_tree_seed", "A seed which when in planted a mystic tree grows.", () -> Map.of(SeedType.PINE_CONES, 5, SeedType.MAHOGANY_SEEDS, 5, SeedType.ACORNS, 5, SeedType.MAPLE_SEEDS, 5), 100, Map.of(Ability.MINING, 4), GameAsset.MYSTIC_TREE_SEED, GameAsset.MYSTIC_TREE_SEED, GameAsset.MYSTIC_TREE_SEED);

    @FunctionalInterface
    private interface IngredientsSupplier {
        Map<Salable, Integer> get();
    }

    private transient volatile Map<Salable, Integer> resolvedIngredients;
    private final IngredientsSupplier ingredientsSupplier;
    private Texture texture;
    private Texture texture1;
    private Texture texture2;

    public Map<Salable, Integer> getIngredients() {
        if (resolvedIngredients == null) {
            resolvedIngredients = ingredientsSupplier.get();
        }
        return resolvedIngredients;
    }

    CraftType(String name, String description, IngredientsSupplier ingredientsSupplier, Integer sellPrice,
              Map<Ability, Integer> abilities, Texture texture, Texture texture1, Texture texture2) {
        this.name = name;
        this.description = description;
        this.ingredientsSupplier = ingredientsSupplier;
        this.sellPrice = sellPrice;
        this.abilities = abilities;
        this.texture = texture;
        this.texture1 = texture1;
        this.texture2 = texture2;
    }

    CraftType(String name, String description, IngredientsSupplier ingredientsSupplier, Integer sellPrice,
              StoreType storeType, Texture texture, Texture texture1, Texture texture2) {
        this.name = name;
        this.description = description;
        this.ingredientsSupplier = ingredientsSupplier;
        this.sellPrice = sellPrice;
        this.storeType = storeType;
        this.texture = texture;
        this.texture1 = texture1;
        this.texture2 = texture2;
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
