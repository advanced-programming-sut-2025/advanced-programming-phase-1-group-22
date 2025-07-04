package io.github.some_example_name.model.products.TreesAndFruitsAndSeeds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.github.some_example_name.model.Game;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;
import lombok.ToString;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.TimeAndDate;
import io.github.some_example_name.model.animal.Fish;
import io.github.some_example_name.model.craft.CraftType;
import io.github.some_example_name.model.exception.InvalidInputException;
import io.github.some_example_name.model.products.AnimalProductType;
import io.github.some_example_name.model.products.HarvestAbleProduct;
import io.github.some_example_name.model.products.Product;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.model.source.Crop;
import io.github.some_example_name.model.source.CropType;
import io.github.some_example_name.model.source.MineralType;
import io.github.some_example_name.model.source.SeedType;
import io.github.some_example_name.utils.App;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@ToString
public enum MadeProductType implements Product {

    HONEY("honey", () -> CraftType.BEE_HOUSE, "It's a sweet syrup produced by bees.", 75, new TimeAndDate(4, 0), () -> Map.of(), 350, true, GameAsset.HONEY),
    CHEESE("cheese", () -> CraftType.CHEESE_PRESS, "It's your basic cheese.", 100, new TimeAndDate(0, 3), () -> Map.of(AnimalProductType.MILK, 1, AnimalProductType.BIG_MILK, 1), 230, true, GameAsset.CHEESE) {
        @Override
        public Integer calcPrice(Salable product) {
            if (product.equals(AnimalProductType.MILK)) {
                return 230;
            }
            return 345;
        }

        @Override
        protected void addPriceInfo(Table info, Skin skin) {
            info.add(new Label("230/345g", skin)).width(50).expandX().row();
        }
    },
    GOAT_CHEESE("goat cheese", () -> CraftType.CHEESE_PRESS, "Soft cheese made from goat's milk.", 100, new TimeAndDate(0, 3), () -> Map.of(AnimalProductType.GOAT_MILK, 1, AnimalProductType.BIG_GOAT_MILK, 1), 400, true, GameAsset.GOAT_CHEESE) {
        @Override
        public Integer calcPrice(Salable product) {
            if (product.equals(AnimalProductType.GOAT_MILK)) {
                return 400;
            }
            return 600;
        }

        @Override
        protected void addPriceInfo(Table info, Skin skin) {
            info.add(new Label("400/600g", skin)).width(150).expandX().row();
        }
    },
    PALE_ALE("pale ale", () -> CraftType.KEG, "Drink in moderation.", 50, new TimeAndDate(3, 0), () -> Map.of(CropType.HOPS, 1), 300, true, GameAsset.PALE_ALE),
    MEAD("mead", () -> CraftType.KEG, "A fermented beverage made from honey. Drink in moderation.", 100, new TimeAndDate(0, 10), () -> Map.of(MadeProductType.HONEY, 1), 300, true, GameAsset.MEAD),
    BEER("beer", () -> CraftType.KEG, "Drink in moderation.", 50, new TimeAndDate(1, 0), () -> Map.of(CropType.WHEAT, 1), 200, true, GameAsset.BEER) {},
    VINEGAR("vinegar", () -> CraftType.KEG, "An aged fermented liquid used in many cooking recipes.", 13, new TimeAndDate(0, 10), () -> Map.of(CropType.UNMILLED_RICE, 1), 100, true, GameAsset.VINEGAR) {},
    COFFE("coffee", () -> CraftType.KEG, "It smells delicious. This is sure to give you a boost.", 75, new TimeAndDate(0, 2), () -> Map.of(CropType.COFFEE_BEAN, 5), 150, true, GameAsset.COFFEE),
    JUICE("juice", () -> CraftType.KEG, "A sweet, nutritious beverage.", 0, new TimeAndDate(4, 0), null, 0, true, GameAsset.JUICE) {
        @Override
        public Integer calcPrice(Salable product) {
            return (int) (2.25 * (double) product.getSellPrice());
        }

        @Override
        protected void addPriceInfo(Table info, Skin skin) {
            info.add(new Label("2.25 * ingredient price", skin)).width(150).expandX().row();
        }

        @Override
        public Integer calcEnergy(Salable product) {
            return 2 * ((HarvestAbleProduct) product).getContainingEnergy();
        }

        @Override
        protected void addEdibleInfo(Table info, Skin skin) {
            info.add(new Label("Twice the base", skin)).width(150).padRight(5);
            info.add(new Image(GameAsset.ENERGY)).height(50).width(50).padRight(60);
        }

        @Override
        public Response isIngredientsValid(Salable ingredient, Integer amount, boolean coal) {
            if (ingredient instanceof Crop) {
                if (amount > 0) return new Response("", true);
                return new Response("Not enough ingredients.");
            }
            return new Response("Ingredient is not a vegetable");
        }

        @Override
        protected void addIngredientInfo(Table info, Skin skin) {
            info.add(new Image(GameAsset.WHEAT)).width(50).padRight(5);
            info.add(new Label("1", skin)).width(20).padRight(5);
            info.add(new Label("Any vegetable", skin)).colspan(7).expandX().row();
        }

        @Override
        public Integer countIngredient() {
            return 1;
        }
    },
    WINE("wine", () -> CraftType.KEG, "Drink in moderation.", 0, new TimeAndDate(7, 0), null, 0, true, GameAsset.WINE) {
        @Override
        public Integer calcEnergy(Salable product) {
            return (int) (1.75 * (double) ((HarvestAbleProduct) product).getContainingEnergy());
        }

        @Override
        protected void addEdibleInfo(Table info, Skin skin) {
            info.add(new Label("1.75 * the base", skin)).width(150).padRight(5);
            info.add(new Image(GameAsset.ENERGY)).height(50).width(50).padRight(60);
        }

        @Override
        public Integer calcPrice(Salable product) {
            return 3 * product.getSellPrice();
        }

        @Override
        protected void addPriceInfo(Table info, Skin skin) {
            info.add(new Label("3 * base price", skin)).width(150).expandX().row();
        }

        @Override
        public Response isIngredientsValid(Salable ingredient, Integer amount, boolean coal) {
            if (ingredient instanceof Fruit) {
                if (amount > 0) return new Response("", true);
                return new Response("Not enough ingredients.");
            }
            return new Response("Ingredient is not a fruit");
        }

        @Override
        protected void addIngredientInfo(Table info, Skin skin) {
            info.add(new Image(GameAsset.APPLE)).width(50).padRight(5);
            info.add(new Label("1", skin)).width(20).padRight(5);
            info.add(new Label("Any Fruit", skin)).colspan(7).expandX().row();
        }

        @Override
        public Integer countIngredient() {
            return 1;
        }
    },
    DRIED_MUSHROOMS("dried mushrooms", () -> CraftType.DEHYDRATOR, "A package of gourmet mushrooms.", 50, null, () -> Map.of(FruitType.COMMON_MUSHROOM, 5, CropType.RED_MUSHROOM, 5, CropType.PURPLE_MUSHROOM, 5), 25, true, GameAsset.DRIED_MUSHROOMS) {
        @Override
        public Integer calcPrice(Salable product) {
            return (int) (7.5 * (double) product.getSellPrice()) + 25;
        }

        @Override
        protected void addPriceInfo(Table info, Skin skin) {
            info.add(new Label("7.5 * base price + 25g", skin)).width(150).expandX().row();
        }

        @Override
        public TimeAndDate calcProccesingTime(Salable product) {
            return calcNextMorning();
        }

        @Override
        protected void addIngredientInfo(Table info, Skin skin) {
            info.add(new Image(GameAsset.MUSHROOM_CAP)).width(50).padRight(5);
            info.add(new Label("5", skin)).width(20).padRight(5);
            info.add(new Label("Any Mushroom", skin)).colspan(7).expandX().row();
        }
    },
    DRIED_FRUIT("dried fruit",()-> CraftType.DEHYDRATOR, "Chewy pieces of dried fruit.", 75, null, null, 0, true, GameAsset.DRIED_FRUIT) {
        @Override
        public Integer calcPrice(Salable product) {
            return (int) (7.5 * (double) product.getSellPrice()) + 25;
        }

        @Override
        protected void addPriceInfo(Table info, Skin skin) {
            info.add(new Label("7.5 * base price + 25", skin)).width(150).expandX().row();
        }

        @Override
        public Response isIngredientsValid(Salable ingredient, Integer amount, boolean coal) {
            if (ingredient.equals(CropType.GRAPE)) {
                return new Response("Grapes can't be dried");
            }
            if (ingredient instanceof Fruit) {
                if (amount > 4) return new Response("", true);
                throw new InvalidInputException("Amount is not enough"); // UNCHECKED
//                return new Response("Not enough ingredients.");
            }
            return new Response("Ingredient is not a food");
        }

        @Override
        protected void addIngredientInfo(Table info, Skin skin) {
            info.add(new Image(GameAsset.APPLE)).width(50).padRight(5);
            info.add(new Label("5", skin)).width(20).padRight(5);
            info.add(new Label("Any Fruit", skin)).colspan(7).expandX().row();
        }


        @Override
        public TimeAndDate calcProccesingTime(Salable product) {
            return calcNextMorning();
        }

        @Override
        public Integer countIngredient() {
            return 5;
        }
    },
    RAISINS("raisins",()-> CraftType.DEHYDRATOR, "It's said to be the Junimos' favorite food.", 125, null, () -> Map.of(CropType.GRAPE, 5), 600, true, GameAsset.RAISINS) {
        @Override
        public TimeAndDate calcProccesingTime(Salable product) {
            return calcNextMorning();
        }
    },
    COAL("coal",()-> CraftType.CHARCOAL_KLIN, "Turns 10 pieces of wood into one piece of coal.", 0, new TimeAndDate(0, 1), () -> Map.of(MineralType.WOOD, 10), 50, false, GameAsset.COAL), //RODO Adding wood to minerals
    CLOTH("cloth", ()->CraftType.LOOM, "A bolt of fine wool cloth.", 0, new TimeAndDate(0, 4), () -> Map.of(AnimalProductType.SHEEP_WOOL, 1, AnimalProductType.RABBIT_WOOL, 1), 470, false, GameAsset.CLOTH),
    MAYONNAISE("mayonnaise", ()->CraftType.MAYONNAISE_MACHINE, "It looks spreadable.", 50, new TimeAndDate(0, 3), () -> Map.of(AnimalProductType.HEN_EGG, 1, AnimalProductType.HEN_BIG_EGG, 1), 190, true, GameAsset.MAYONNAISE) {
        @Override
        public Integer calcPrice(Salable product) {
            if (product.equals(AnimalProductType.HEN_EGG)) return 190;
            return 237;
        }

        @Override
        protected void addPriceInfo(Table info, Skin skin) {
            info.add(new Label("190/237g", skin)).width(150).expandX().row();
        }
    },
    DUCK_MAYONNAISE("duck mayonnaise",()-> CraftType.MAYONNAISE_MACHINE, "It's a rich, yellow mayonnaise.", 75, new TimeAndDate(0, 3), () -> Map.of(AnimalProductType.DUCK_EGG, 1), 37, true, GameAsset.DUCK_MAYONNAISE),
    DINOSAUR_MAYONNAISE("dinosaur mayonnaise", ()->CraftType.MAYONNAISE_MACHINE, "It's thick and creamy, with a vivid green hue. It smells like grass and leather.", 125, new TimeAndDate(0, 3), () -> Map.of(AnimalProductType.DINOSAUR_EGG, 1), 800, true, GameAsset.DINOSAUR_MAYONNAISE),
    TRUFFLE_OIL("truffle oil",()-> CraftType.OIL_MAKER, "A gourmet cooking ingredient.", 38, new TimeAndDate(0, 6), () -> Map.of(AnimalProductType.TRUFFLE, 1), 1065, true,  GameAsset.TRUFFLE_OIL) {},
    OIL("oil", ()->CraftType.OIL_MAKER, "All purpose cooking oil.", 13, null, () -> Map.of(CropType.CORN, 1, SeedType.SUNFLOWER_SEEDS, 1, CropType.SUNFLOWER, 1), 100, true, GameAsset.OIL) {
        @Override
        public TimeAndDate calcProccesingTime(Salable product) {
            if (product.equals(CropType.CORN)) return new TimeAndDate(0, 6);
            else if (product.equals(CropType.SUNFLOWER)) return new TimeAndDate(2, 0);
            return new TimeAndDate(0, 1);
        }

        @Override
        protected void addETAInfo(Table info, Skin skin) {
            info.add(new Label("ETA: 6h/2d/1h", skin)).colspan(2).width(140).padRight(30);
        }

    },
    PICKLES("pickles",()-> CraftType.PRESERVES_JAR, "A jar of your home-made pickles.", 0, new TimeAndDate(0, 6), null, 0, true, GameAsset.PICKLES) {
        @Override
        public Integer calcEnergy(Salable product) {
            return (int) (1.75 * (double) ((HarvestAbleProduct) product).getContainingEnergy());
        }

        @Override
        protected void addEdibleInfo(Table info, Skin skin) {
            info.add(new Label("1.75 * base", skin)).width(150).padRight(5);
            info.add(new Image(GameAsset.ENERGY)).height(50).width(50).padRight(60);
        }

        @Override
        protected void addPriceInfo(Table info, Skin skin) {
            info.add(new Label("2 * base price + 50g", skin)).width(150).expandX().row();
        }

        @Override
        public Integer calcPrice(Salable product) {
            return 2 * product.getSellPrice() + 50;
        }

        @Override
        public Response isIngredientsValid(Salable ingredient, Integer amount, boolean coal) {
            if (ingredient instanceof Crop) {
                if (amount > 0) return new Response("", true);
//                return new Response("Not enough ingredients.");
                throw new InvalidInputException("Amount is not enough"); // UNCHECKED
            }
            return new Response("Ingredient is not a vegetables");
        }

        @Override
        protected void addIngredientInfo(Table info, Skin skin) {
            info.add(new Image(GameAsset.WHEAT)).width(50).padRight(5);
            info.add(new Label("1", skin)).width(20).padRight(5);
            info.add(new Label("Any Vegetable", skin)).colspan(7).expandX().row();
        }

        @Override
        public Integer countIngredient() {
            return 1;
        }
    },
    JELLY("jelly",()-> CraftType.PRESERVES_JAR, "Gooey.", 0, new TimeAndDate(3, 0), null, 50, true, GameAsset.JELLY) {
        @Override
        public Integer calcEnergy(Salable product) {
            return 2 * ((HarvestAbleProduct) product).getContainingEnergy();
        }

        @Override
        protected void addEdibleInfo(Table info, Skin skin) {
            info.add(new Label("Twice the base", skin)).width(150).padRight(5);
            info.add(new Image(GameAsset.ENERGY)).height(50).width(50).padRight(60);
        }

        @Override
        public Integer calcPrice(Salable product) {
            return 2 * product.getSellPrice() + 50;
        }

        @Override
        protected void addPriceInfo(Table info, Skin skin) {
            info.add(new Label("Twice the price + 50g", skin)).width(150).expandX().row();
        }

        @Override
        public Response isIngredientsValid(Salable ingredient, Integer amount, boolean coal) {
            if (ingredient instanceof Fruit) {
                if (amount > 0) return new Response("", true);
                return new Response("Not enough ingredients.");
            }
            return new Response("Ingredient is not a fruit");
        }

        @Override
        protected void addIngredientInfo(Table info, Skin skin) {
            info.add(new Image(GameAsset.APPLE)).width(50).padRight(5);
            info.add(new Label("1", skin)).width(20).padRight(5);
            info.add(new Label("Any Fruit", skin)).colspan(7).expandX().row();
        }

        @Override
        public Integer countIngredient() {
            return 1;
        }
    },
    SMOKED_FISH("smoked fish", ()->CraftType.FISH_SMOKER, "A whole fish, smoked to perfection.", 0, new TimeAndDate(0, 1), null, 50, true, GameAsset.SMOKED_FISH) {
        @Override
        public Integer calcEnergy(Salable product) {
            return (int) (1.5 * (double) ((Fish) product).getContainingEnergy());
        }

        {
            isCoalNeeded = true;
        }

        @Override
        protected void addEdibleInfo(Table info, Skin skin) {
            info.add(new Label("1.5 * the base", skin)).width(150).padRight(5);
            info.add(new Image(GameAsset.ENERGY)).height(50).width(50).padRight(60);
        }

        @Override
        public Integer calcPrice(Salable product) {
            return 2 * product.getSellPrice();
        }

        @Override
        protected void addPriceInfo(Table info, Skin skin) {
            info.add(new Label("Twice the base price", skin)).width(50).expandX().row();
        }

        @Override
        public Response isIngredientsValid(Salable ingredient, Integer amount, boolean coal) {
            if (!coal) {
                throw new InvalidInputException("No coal given!"); // UNCHECKED
//                return new Response("No coal is given");
            }
            if (ingredient instanceof Fish) {
                if (amount > 0) return new Response("", true);
                return new Response("Not enough ingredients.");
            }
            return new Response("Ingredient is not a fish");
        }

        @Override
        protected void addIngredientInfo(Table info, Skin skin) {
            info.add(new Image(GameAsset.FISH)).width(50).padRight(5);
            info.add(new Label("1", skin)).width(40).padRight(5);
            info.add(new Label("Any Fish", skin)).colspan(7).expandX().row();
        }

        @Override
        public Integer countIngredient() {
            return 1;
        }
    },
    IRON_BAR("iron bar", ()->CraftType.FURNACE, "Turns ore and coal into metal bars.", 0, new TimeAndDate(0, 4), () -> Map.of(MineralType.IRON_ORE, 5), 1500, false, true, GameAsset.IRON_ORE) ,
    COPPER_BAR("copper bar", ()->CraftType.FURNACE, "Turns ore and coal into metal bars.", 0, new TimeAndDate(0, 4), () -> Map.of(MineralType.COPPER_ORE, 5), 750, false, true, GameAsset.COPPER_ORE),
    GOLD_BAR("gold bar",()-> CraftType.FURNACE, "Turns ore and coal into metal bars.", 0, new TimeAndDate(0, 4), () -> Map.of(MineralType.GOLD_ORE, 5), 4000, false, true, GameAsset.GOLD_ORE),
    IRIDIUM_BAR("iridium bar",()-> CraftType.FURNACE, "Turns ore and coal into metal bars.", 0, new TimeAndDate(0, 4), () -> Map.of(MineralType.IRIDIUM_ORE, 5), 10000, false, true, GameAsset.IRIDIUM_ORE); //TODO SellPrice not found
    private final String name;
    private String description;
    private int energy;
    private TimeAndDate processingTime;
    private final Integer sellPrice;
    private boolean isEdible;
    protected boolean isCoalNeeded;
    private Texture texture;

    public void addInfo(Table info, Skin skin) {
        info.add(new Image(getTexture())).width(50).padRight(5);
        info.add(new Label(getName(), skin)).width(200).padRight(30);
        addEdibleInfo(info, skin);
        addETAInfo(info, skin);
        addPriceInfo(info, skin);
        info.add(new Label(getDescription(), skin)).colspan(9).expandX().row();
        addIngredientInfo(info, skin);
        if (isCoalNeeded) {
            info.add(new Image(GameAsset.COAL)).width(50).padRight(5);
            info.add(new Label("1", skin)).width(40).padRight(5);
            info.add(new Label("Coal Required", skin)).colspan(7).expandX().row();
        }
    }

    protected void addIngredientInfo(Table info, Skin skin) {
        for (Map.Entry<Salable, Integer> salable : ingredientsSupplier.get().entrySet()) {
            addIngredientInfo(info, skin, salable);
        }
    }

    protected void addPriceInfo(Table info, Skin skin) {
        info.add(new Label(getSellPrice() + "g", skin)).width(50).expandX().row();
    }

    protected void addETAInfo(Table info, Skin skin) {
        info.add(new Label("ETA: " + ((getProcessingTime() != null) ? getProcessingTime().getDayHour() : "Next day"), skin))
            .colspan(2).width(140).padRight(30);
    }

    protected void addEdibleInfo(Table info, Skin skin) {
        if (getEnergy() != 0) {
            info.add(new Label(Integer.valueOf(getEnergy()).toString(), skin)).width(150).padRight(5);
            info.add(new Image(GameAsset.ENERGY)).height(50).width(50).padRight(60);
        } else {
            info.add(new Label("Not", skin)).width(50).padRight(5);
            info.add(new Label("Edible", skin)).width(50).padRight(60);
        }
    }

    protected void addIngredientInfo(Table info, Skin skin, Map.Entry<Salable, Integer> salable) {
        info.add(new Image(salable.getKey().getTexture())).width(50).padRight(5);
        info.add(new Label(salable.getValue().toString(), skin)).width(40).padRight(5);
        info.add(new Label(salable.getKey().getName(), skin)).colspan(7).expandX().row();
    }


    @FunctionalInterface
    private interface IngredientsSupplier {
        Map<Salable, Integer> get();
    }

    @FunctionalInterface
    private interface CraftTypeSupplier {
        CraftType get();
    }

    protected final IngredientsSupplier ingredientsSupplier;
    private final CraftTypeSupplier craftTypeSupplier;
    private transient final AtomicReference<Map<Salable, Integer>> ingredientsCache = new AtomicReference<>();
    private transient final AtomicReference<CraftType> craftType = new AtomicReference<>();

    MadeProductType(String name, CraftTypeSupplier craftType, String description, Integer energy, TimeAndDate processingTime, IngredientsSupplier ingredientsSupplier, Integer sellPrice, boolean isEdible, Texture texture) {
        this.name = name;
        this.craftTypeSupplier = craftType;
        this.description = description;
        this.energy = energy;
        this.sellPrice = sellPrice;
        this.processingTime = processingTime;
        this.ingredientsSupplier = ingredientsSupplier;
        this.isEdible = isEdible;
        this.isCoalNeeded = false;
        this.texture = texture;
    }


    MadeProductType(String name, CraftTypeSupplier craftType, String description, Integer energy,
                    TimeAndDate processingTime, IngredientsSupplier ingredientsSupplier,
                    Integer sellPrice, boolean isEdible, boolean isCoalNeeded, Texture texture) {
        this.name = name;
        this.craftTypeSupplier = craftType;
        this.description = description;
        this.energy = energy;
        this.processingTime = processingTime;
        this.ingredientsSupplier = ingredientsSupplier;  // Store the supplier
        this.sellPrice = sellPrice;
        this.isEdible = isEdible;
        this.isCoalNeeded = isCoalNeeded;
        this.texture = texture;
    }


    public static ArrayList<MadeProductType> findByCraft(CraftType craft) {
        ArrayList<MadeProductType> list = new ArrayList<>();
        for (MadeProductType value : MadeProductType.values()) {
            if (craft.equals(value.getCraft())) list.add(value);
        }
        return list;
    }

    public Map<Salable, Integer> getIngredients() {
        return ingredientsCache.updateAndGet(cache ->
                cache != null ? cache : ingredientsSupplier.get()
        );
    }

    public CraftType getCraft() {
        return craftType.updateAndGet(cache ->
                cache != null ? cache : craftTypeSupplier.get()
        );
    }

    public Integer calcPrice(Salable product) {
        return sellPrice;
    }


    public Integer calcEnergy(Salable product) {
        return energy;
    }


    public TimeAndDate calcProccesingTime(Salable product) {
        return processingTime;
    }

    public TimeAndDate calcETA(Salable product) {
        TimeAndDate now = App.getInstance().getCurrentGame().getTimeAndDate();
        TimeAndDate processing = calcProccesingTime(product);
        if (processing == null) {
            return now.getNextMorning();
        } else {
            now = now.getNextXDay(processing.getDay());
            return now.getNextXHour(processing.getHour());
        }
    }

    private static TimeAndDate calcNextMorning() {
        return null;
    }


    public Response isIngredientsValid(Salable product, Integer amount, boolean coal) {
        if (isCoalNeeded && !coal) {
//            return new Response("No coal given!");
            throw new InvalidInputException("No coal given!"); // UNCHECKED
        }

        for (Salable ingredient : this.getIngredients().keySet()) {
            if (product.getName().equalsIgnoreCase(ingredient.getName())) {
                if (amount >= this.getIngredients().get(ingredient)) return new Response("", true);
                throw new InvalidInputException("Amount is not enough"); // UNCHECKED
//                return new Response("Amount is not enough");
            }
//            return new Response("", true);
        }
        return new Response("Ingredient not found.");
    }

    public Integer countIngredient() {
        return this.getIngredients().values().iterator().next();
    }

    @Override
    public int getSellPrice() {
        return this.sellPrice;
    }

    @Override
    public String getName() {
        return this.name.toLowerCase();
    }

    @Override
    public Integer getContainingEnergy() {return getEnergy();}
}
