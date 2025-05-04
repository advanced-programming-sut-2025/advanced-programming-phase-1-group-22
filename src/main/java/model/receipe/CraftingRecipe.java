package model.receipe;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.Salable;
import model.craft.CraftType;
import utils.App;

import java.util.Map;

@Getter
public enum CraftingRecipe implements Recipe {
    CHERRY_BOMB_RECIPE("Cherry Bomb recipe", "A recipe to make Cherry Bomb", 50, () -> CraftType.CHERRY_BOMB),
    BOMB_RECIPE("Bomb recipe", "A recipe to make Bomb", 50, () -> CraftType.BOMB),
    MEGA_BOMB_RECIPE("Mega Bomb recipe", "A recipe to make Mega Bomb", 50, () -> CraftType.MEGA_BOMB),
    SPRINKLER_RECIPE("Sprinkler recipe", "A recipe to make Sprinkler", 0, () -> CraftType.SPRINKLER),
    QUALITY_SPRINKLER_RECIPE("Quality Sprinkler recipe", "A recipe to make Quality Sprinkler", 0, () -> CraftType.QUALITY_SPRINKLER),
    IRIDIUM_SPRINKLER_RECIPE("Iridium Sprinkler recipe", "A recipe to make Iridium Sprinkler", 0, () -> CraftType.IRIDIUM_SPRINKLER),
    CHARCOAL_KILN_RECIPE("Charcoal Kiln recipe", "A recipe to make Charcoal Kiln", 0, () -> CraftType.CHARCOAL_KLIN),
    FURNACE_RECIPE("Furnace recipe", "A recipe to make Furnace", 0, () -> CraftType.FURNACE),
    SCARECROW_RECIPE("Scarecrow recipe", "A recipe to make Scarecrow", 0, () -> CraftType.SCARE_CROW),
    DELUXE_SCARECROW_RECIPE("Deluxe Scarecrow recipe", "A recipe to make Deluxe Scarecrow", 0, () -> CraftType.DELUXE_SCARECROW),
    BEE_HOUSE_RECIPE("Bee House recipe", "A recipe to make Bee House", 0, () -> CraftType.BEE_HOUSE),
    CHEESE_PRESS_RECIPE("Cheese Press recipe", "A recipe to make Cheese Press", 0, () -> CraftType.CHEESE_PRESS),
    KEG_RECIPE("Keg recipe", "A recipe to make Keg", 0, () -> CraftType.KEG),
    LOOM_RECIPE("Loom recipe", "A recipe to make Loom", 0, () -> CraftType.LOOM),
    MAYONNAISE_MACHINE_RECIPE("Mayonnaise Machine recipe", "A recipe to make Mayonnaise Machine", 0, () -> CraftType.MAYONNAISE_MACHINE),
    OIL_MAKER_RECIPE("Oil Maker recipe", "A recipe to make Oil Maker", 0, () -> CraftType.OIL_MAKER),
    PRESERVES_JAR_RECIPE("Preserves Jar recipe", "A recipe to make Preserves Jar", 0, () -> CraftType.PRESERVES_JAR),
    DEHYDRATOR_RECIPE("Dehydrator recipe", "A recipe to make Dehydrator", 10000, () -> CraftType.DEHYDRATOR),
    GRASS_STARTER_RECIPE("Grass Starter recipe", "A recipe to make Grass Starter", 1000, () -> CraftType.GRASS_STARTER),
    FISH_SMOKER_RECIPE("Fish Smoker recipe", "A recipe to make Fish Smoker", 0, () -> CraftType.FISH_SMOKER),
    MYSTIC_TREE_SEED_RECIPE("Mystic Tree Seed recipe", "A recipe to make Mystic Tree Seed", 100, () -> CraftType.MYSTIC_TREE_SEEDS);

    private String name;
    private Integer id;
    private String description;
    private Integer price;

    @FunctionalInterface
    private interface IngredientsSupplier {
        CraftType get();
    }

    private transient volatile CraftType craftType;
    private final CraftingRecipe.IngredientsSupplier ingredientsSupplier;

    public CraftType getIngredients() {
        if (craftType == null) {
            craftType = ingredientsSupplier.get();
        }
        return craftType;
    }


    private CraftingRecipe(String name, String description, Integer price, IngredientsSupplier craftType) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.ingredientsSupplier = craftType;
    }

    @Override
    public String getName() {
        return this.name.toLowerCase();
    }

    @Override
    public int getSellPrice() {
        return price;
    }

    @Override
    public String toString() {
        String res = this.getIngredients().getName() + ":\n\n" + this.getIngredients().getDescription() + "\n\nIngredients:\n";
        res += this.getIngredients().getProductsString();
        res += "\nPrice: " + this.getIngredients().getSellPrice() + "$\n";
        res += this.getIngredients().isCraftingPossible(App.getInstance().getCurrentGame().getCurrentPlayer()) + "\n";
        return res;
    }
}
