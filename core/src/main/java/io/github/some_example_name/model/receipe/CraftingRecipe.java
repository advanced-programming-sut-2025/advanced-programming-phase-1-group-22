package io.github.some_example_name.model.receipe;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;
import io.github.some_example_name.model.craft.CraftType;
import io.github.some_example_name.utils.App;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Getter
public enum CraftingRecipe implements Recipe {
    CHERRY_BOMB_RECIPE("Cherry Bomb recipe", "A recipe to make Cherry Bomb", 50, () -> CraftType.CHERRY_BOMB, GameAsset.CHERRY_BOMB),
    BOMB_RECIPE("Bomb recipe", "A recipe to make Bomb", 50, () -> CraftType.BOMB, GameAsset.BOMB),
    MEGA_BOMB_RECIPE("Mega Bomb recipe", "A recipe to make Mega Bomb", 50, () -> CraftType.MEGA_BOMB, GameAsset.MEGA_BOMB),
    SPRINKLER_RECIPE("Sprinklers recipe", "A recipe to make Sprinkler", 0, () -> CraftType.SPRINKLER, GameAsset.SPRINKLER),
    QUALITY_SPRINKLER_RECIPE("Quality Sprinklers recipe", "A recipe to make Quality Sprinkler", 0, () -> CraftType.QUALITY_SPRINKLER, GameAsset.QUALITY_SPRINKLER),
    IRIDIUM_SPRINKLER_RECIPE("Iridium Sprinklers recipe", "A recipe to make Iridium Sprinkler", 0, () -> CraftType.IRIDIUM_SPRINKLER, GameAsset.IRIDIUM_SPRINKLER),
    CHARCOAL_KILN_RECIPE("Charcoal Klin recipe", "A recipe to make Charcoal Klin", 0, () -> CraftType.CHARCOAL_KLIN, GameAsset.CHARCOAL_KILN),
    FURNACE_RECIPE("Furnace recipe", "A recipe to make Furnace", 0, () -> CraftType.FURNACE, GameAsset.FURNACE),
    SCARECROW_RECIPE("Scare crow recipe", "A recipe to make Scarecrow", 0, () -> CraftType.SCARE_CROW, GameAsset.SCARECROW),
    DELUXE_SCARECROW_RECIPE("Deluxe Scarecrow recipe", "A recipe to make Deluxe Scarecrow", 0, () -> CraftType.DELUXE_SCARECROW, GameAsset.DELUXE_SCARECROW),
    BEE_HOUSE_RECIPE("Bee House recipe", "A recipe to make Bee House", 0, () -> CraftType.BEE_HOUSE, GameAsset.BEE_HOUSE),
    CHEESE_PRESS_RECIPE("Cheese Press recipe", "A recipe to make Cheese Press", 0, () -> CraftType.CHEESE_PRESS, GameAsset.CHEESE_PRESS),
    KEG_RECIPE("Keg recipe", "A recipe to make Keg", 0, () -> CraftType.KEG, GameAsset.KEG),
    LOOM_RECIPE("Loom recipe", "A recipe to make Loom", 0, () -> CraftType.LOOM, GameAsset.LOOM),
    MAYONNAISE_MACHINE_RECIPE("Mayonnaise Machine recipe", "A recipe to make Mayonnaise Machine", 0, () -> CraftType.MAYONNAISE_MACHINE, GameAsset.MAYONNAISE_MACHINE),
    OIL_MAKER_RECIPE("Oil Maker recipe", "A recipe to make Oil Maker", 0, () -> CraftType.OIL_MAKER, GameAsset.OIL_MAKER),
    PRESERVES_JAR_RECIPE("Preserves Jar recipe", "A recipe to make Preserves Jar", 0, () -> CraftType.PRESERVES_JAR, GameAsset.PRESERVES_JAR),
    DEHYDRATOR_RECIPE("Dehydrator recipe", "A recipe to make Dehydrator", 10000, () -> CraftType.DEHYDRATOR, GameAsset.DEHYDRATOR),
    GRASS_STARTER_RECIPE("Grass Starter recipe", "A recipe to make Grass Starter", 1000, () -> CraftType.GRASS_STARTER, GameAsset.GRASS_STARTER),
    FISH_SMOKER_RECIPE("Fish Smoker recipe", "A recipe to make Fish Smoker", 0, () -> CraftType.FISH_SMOKER, GameAsset.FISH_SMOKER),
    MYSTIC_TREE_SEED_RECIPE("Mystic Tree Seed recipe", "A recipe to make Mystic Tree Seed", 100, () -> CraftType.MYSTIC_TREE_SEEDS, GameAsset.MYSTIC_TREE_SEED),;

    private String name;
    private Integer id;
    private String description;
    private Integer price;
    private Texture texture;

    @FunctionalInterface
    private interface CraftTypeSupplier {
        CraftType get();
    }

    private transient final AtomicReference<CraftType> craftType = new AtomicReference<>();
    private final CraftTypeSupplier craftTypeSupplier;

    public CraftType getCraft() {
        return craftType.updateAndGet(cache ->
                cache != null ? cache : craftTypeSupplier.get()
        );
    }


    CraftingRecipe(String name, String description, Integer price, CraftTypeSupplier craftType, Texture texture) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.craftTypeSupplier = craftType;
        this.texture = texture;
    }

    public void addInfo(Table info, Skin skin) {
        for (Map.Entry<Salable, Integer> ingredient : getCraft().getIngredients().entrySet()) {
            Image image = new Image(ingredient.getKey().getTexture());
            image.setWidth(50);
            image.setHeight(50);
            info.add(image).width(50).padRight(10);
            info.add(new Label(ingredient.getValue().toString(), skin)).width(10).padRight(30);
            info.add(new Label(ingredient.getKey().getName(), skin)).expandX().fillX().row();
        }
        info.add(new Label(getCraft().getDescription(), skin)).colspan(3).expandX().fillX().row();

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
        String res = this.getCraft().getName() + ":\n" + this.getCraft().getDescription() + "\nIngredients:\n";
        res += this.getCraft().getProductsString();
        res += "Price: " + this.getCraft().getSellPrice() + "$\n";
        res += this.getCraft().isCraftingPossible(App.getInstance().getCurrentGame().getCurrentPlayer()) + "\n\n";
        return res;
    }

    @Override
    public Integer getContainingEnergy() {return 0;}
}
