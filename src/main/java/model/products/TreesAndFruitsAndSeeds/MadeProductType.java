package model.products.TreesAndFruitsAndSeeds;

import lombok.Getter;
import lombok.ToString;
import model.Salable;
import model.TimeAndDate;
import model.animal.Fish;
import model.craft.CraftType;
import model.products.AnimalProductType;
import model.products.HarvestAbleProduct;
import model.products.Product;
import model.records.Response;
import model.source.Crop;
import model.source.CropType;
import model.source.MineralType;
import model.source.SeedType;
import utils.App;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@ToString
public enum MadeProductType implements Product, Serializable {

    HONEY("honey", () -> CraftType.BEE_HOUSE, "It's a sweet syrup produced by bees.", 75, new TimeAndDate(4, 0), () -> Map.of(), 350, true),
    CHEESE("cheese", () -> CraftType.CHEESE_PRESS, "It's your basic cheese.", 100, new TimeAndDate(0, 3), () -> Map.of(AnimalProductType.MILK, 1, AnimalProductType.BIG_MILK, 1), 230, true) {
        @Override
        public Integer calcPrice(Salable product) {
            if (product.equals(AnimalProductType.MILK)) {
                return 230;
            }
            return 345;
        }
    },
    GOAT_CHEESE("goat cheese", () -> CraftType.CHEESE_PRESS, "Soft cheese made from goat's milk.", 100, new TimeAndDate(0, 3), () -> Map.of(AnimalProductType.GOAT_MILK, 1, AnimalProductType.BIG_GOAT_MILK, 1), 400, true) {
        @Override
        public Integer calcPrice(Salable product) {
            if (product.equals(AnimalProductType.GOAT_MILK)) {
                return 400;
            }
            return 600;
        }
    },
    BEER("beer", () -> CraftType.KEG, "Drink in moderation.", 50, new TimeAndDate(1, 0), () -> Map.of(CropType.WHEAT, 1), 200, true),
    VINEGAR("vinegar", () -> CraftType.KEG, "An aged fermented liquid used in many cooking recipes.", 13, new TimeAndDate(0, 10), () -> Map.of(CropType.UNMILLED_RICE, 1), 100, true),
    COFFE("coffee", () -> CraftType.KEG, "It smells delicious. This is sure to give you a boost.", 75, new TimeAndDate(0, 2), () -> Map.of(CropType.COFFEE_BEAN, 5), 150, true),
    JUICE("juice", () -> CraftType.KEG, "A sweet, nutritious beverage.", 0, new TimeAndDate(4, 0), null, 0, true) {
        @Override
        public Integer calcPrice(Salable product) {
            return (int) (2.25 * (double) product.getSellPrice());
        }

        @Override
        public Integer calcEnergy(Salable product) {
            return 2 * ((HarvestAbleProduct) product).getContainingEnergy();
        }

        @Override
        public Response isIngredientsValid(Salable ingredient, Integer amount, boolean coal) {
            if (ingredient instanceof Crop) {
                if (amount > 0) return new Response("", true);
                return new Response("Not enough ingredients.");
            }
            return new Response("Ingredient is not a vegetable");
        }
    },
    MEAD("mead", () -> CraftType.KEG, "A fermented beverage made from honey. Drink in moderation.", 100, new TimeAndDate(0, 10), () -> Map.of(MadeProductType.HONEY, 1), 300, true),
    PALE_ALE("pale ale", () -> CraftType.KEG, "Drink in moderation.", 50, new TimeAndDate(3, 0), () -> Map.of(CropType.HOPS, 1), 300, true),
    WINE("wine", () -> CraftType.KEG, "Drink in moderation.", 0, new TimeAndDate(7, 0), null, 0, true) {
        @Override
        public Integer calcEnergy(Salable product) {
            return (int) (1.75 * (double) ((HarvestAbleProduct) product).getContainingEnergy());
        }

        @Override
        public Integer calcPrice(Salable product) {
            return 3 * product.getSellPrice();
        }

        @Override
        public Response isIngredientsValid(Salable ingredient, Integer amount, boolean coal) {
            if (ingredient instanceof Fruit) {
                if (amount > 0) return new Response("", true);
                return new Response("Not enough ingredients.");
            }
            return new Response("Ingredient is not a fruit");
        }
    },
    //    DRIED_MUSHROOMS("dried mushrooms", CraftType.DEHYDRATOR, "A package of gourmet mushrooms.", 50, null, null, null, true) {
//        @Override
//        public Integer calcPrice(Salable product) {
//            return (int) (7.5 * (double) product.getSellPrice()) + 25;
//        }
//
//        @Override
//        public Response isIngredientsValid(Salable ingredient, Integer amount, boolean coal) {
//            if (ingredient instanceof HarvestAbleProduct && ((HarvestAbleProduct)ingredient).isMushroom()) {
//                if (amount > 4) return true;
//                return new Response("Not enough ingredients.");
//            }
//            return new Response("Ingredient is not a mushroom");
//        }
//
//        @Override
//        public TimeAndDate calcProccesingTime(Salable product) {
//            return calcNextMorning();
//        }
//
//    @Override
//    public Integer countIngredient() {
//        return 5);
//    }
//    },
    DRIED_FRUIT("dried fruit",()-> CraftType.DEHYDRATOR, "Chewy pieces of dried fruit.", 75, null, null, 0, true) {
        @Override
        public Integer calcPrice(Salable product) {
            return (int) (7.5 * (double) product.getSellPrice()) + 25;
        }

        @Override
        public Response isIngredientsValid(Salable ingredient, Integer amount, boolean coal) {
            if (ingredient.equals(CropType.GRAPE)) {
                return new Response("Grapes can't be dried");
            }
            if (ingredient instanceof Fruit) {
                if (amount > 4) return new Response("", true);
                return new Response("Not enough ingredients.");
            }
            return new Response("Ingredient is not a food");
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
    RAISINS("raisins",()-> CraftType.DEHYDRATOR, "It's said to be the Junimos' favorite food.", 125, null, () -> Map.of(CropType.GRAPE, 5), 600, true) {
        @Override
        public TimeAndDate calcProccesingTime(Salable product) {
            return calcNextMorning();
        }
    },
    COAL("coal",()-> CraftType.CHARCOAL_KLIN, "Turns 10 pieces of wood into one piece of coal.", 0, new TimeAndDate(0, 1), () -> Map.of(MineralType.WOOD, 10), 50, false), //RODO Adding wood to minerals
    CLOTH("cloth", ()->CraftType.LOOM, "A bolt of fine wool cloth.", 0, new TimeAndDate(0, 4), () -> Map.of(AnimalProductType.SHEEP_WOOL, 1, AnimalProductType.RABBIT_WOOL, 1), 470, false),
    MAYONNAISE("mayonnaise", ()->CraftType.MAYONNAISE_MACHINE, "It looks spreadable.", 50, new TimeAndDate(0, 3), () -> Map.of(AnimalProductType.HEN_EGG, 1, AnimalProductType.HEN_BIG_EGG, 1), 190, true) {
        @Override
        public Integer calcPrice(Salable product) {
            if (product.equals(AnimalProductType.HEN_EGG)) return 190;
            return 237;
        }
    },
    DUCK_MAYONNAISE("duck mayonnaise",()-> CraftType.MAYONNAISE_MACHINE, "It's a rich, yellow mayonnaise.", 75, new TimeAndDate(0, 3), () -> Map.of(AnimalProductType.DUCK_EGG, 1), 37, true),
    DINOSAUR_MAYONNAISE("dinosaur mayonnaise", ()->CraftType.MAYONNAISE_MACHINE, "It's thick and creamy, with a vivid green hue. It smells like grass and leather.", 125, new TimeAndDate(0, 3), () -> Map.of(AnimalProductType.DINOSAUR_EGG, 1), 800, true),
    TRUFFLE_OIL("truffle oil",()-> CraftType.OIL_MAKER, "A gourmet cooking ingredient.", 38, new TimeAndDate(0, 6), () -> Map.of(AnimalProductType.TRUFFLE, 1), 1065, true),
    OIL("oil", ()->CraftType.OIL_MAKER, "All purpose cooking oil.", 13, null, () -> Map.of(CropType.CORN, 1, SeedType.SUNFLOWER_SEEDS, 1, CropType.SUNFLOWER, 1), 100, true) {
        public TimeAndDate calcProccesingTime(Salable product) {
            if (product.equals(CropType.CORN)) return new TimeAndDate(0, 6);
            else if (product.equals(CropType.SUNFLOWER)) return new TimeAndDate(2, 0);
            return new TimeAndDate(0, 1);
        }
    },
    PICKLES("pickles",()-> CraftType.PRESERVES_JAR, "A jar of your home-made pickles.", 0, new TimeAndDate(0, 6), null, 0, true) {
        @Override
        public Integer calcEnergy(Salable product) {
            return (int) (1.75 * (double) ((HarvestAbleProduct) product).getContainingEnergy());
        }

        @Override
        public Integer calcPrice(Salable product) {
            return 2 * product.getSellPrice() + 50;
        }

        @Override
        public Response isIngredientsValid(Salable ingredient, Integer amount, boolean coal) {
            if (ingredient instanceof Crop) {
                if (amount > 0) return new Response("", true);
                return new Response("Not enough ingredients.");
            }
            return new Response("Ingredient is not a vegetables");
        }
    },
    JELLY("jelly",()-> CraftType.PRESERVES_JAR, "Gooey.", 0, new TimeAndDate(3, 0), null, 50, true) {
        @Override
        public Integer calcEnergy(Salable product) {
            return 2 * ((HarvestAbleProduct) product).getContainingEnergy();
        }

        @Override
        public Integer calcPrice(Salable product) {
            return 2 * product.getSellPrice() + 50;
        }

        @Override
        public Response isIngredientsValid(Salable ingredient, Integer amount, boolean coal) {
            if (ingredient instanceof Fruit) {
                if (amount > 0) return new Response("", true);
                return new Response("Not enough ingredients.");
            }
            return new Response("Ingredient is not a fruit");
        }
    },
    SMOKED_FISH("smoked fish", ()->CraftType.FISH_SMOKER, "A whole fish, smoked to perfection.", 0, new TimeAndDate(0, 1), null, 50, true) {
        @Override
        public Integer calcEnergy(Salable product) {
            return (int) (1.5 * (double) ((Fish) product).getContainingEnergy());
        }

        @Override
        public Integer calcPrice(Salable product) {
            return 2 * product.getSellPrice();
        }

        @Override
        public Response isIngredientsValid(Salable ingredient, Integer amount, boolean coal) {
            if (!coal) return new Response("No coal is given");
            if (ingredient instanceof Fish) {
                if (amount > 0) return new Response("", true);
                return new Response("Not enough ingredients.");
            }
            return new Response("Ingredient is not a fish");
        }
    },
    IRON_BAR("iron bar", ()->CraftType.FURNACE, "Turns ore and coal into metal bars.", 0, new TimeAndDate(0, 4), () -> Map.of(MineralType.IRON_ORE, 5), 1500, false, true),
    COPPER_BAR("copper bar", ()->CraftType.FURNACE, "Turns ore and coal into metal bars.", 0, new TimeAndDate(0, 4), () -> Map.of(MineralType.COPPER_ORE, 5), 750, false, true),
    GOLD_BAR("gold bar",()-> CraftType.FURNACE, "Turns ore and coal into metal bars.", 0, new TimeAndDate(0, 4), () -> Map.of(MineralType.GOLD_ORE, 5), 4000, false, true),
    IRIDIUM_BAR("iridium bar",()-> CraftType.FURNACE, "Turns ore and coal into metal bars.", 0, new TimeAndDate(0, 4), () -> Map.of(MineralType.IRIDIUM_ORE, 5), 10000, false, true); //TODO SellPrice not found
    private String name;
    private String description;
    private int energy;
    private TimeAndDate processingTime;
    private Integer sellPrice;
    private boolean isEdible;
    private boolean isCoalNeeded;

    MadeProductType() {
    }

    @FunctionalInterface
    private interface IngredientsSupplier {
        Map<Salable, Integer> get();
    }

    @FunctionalInterface
    private interface CraftTypeSupplier {
        CraftType get();
    }

    private IngredientsSupplier ingredientsSupplier;
    private CraftTypeSupplier craftTypeSupplier;
    private transient final AtomicReference<Map<Salable, Integer>> ingredientsCache = new AtomicReference<>();
    private transient final AtomicReference<CraftType> craftType = new AtomicReference<>();

    MadeProductType(String name, CraftTypeSupplier craftType, String description, Integer energy, TimeAndDate processingTime, IngredientsSupplier ingredientsSupplier, Integer sellPrice, boolean isEdible) {
        this.name = name;
        this.craftTypeSupplier = craftType;
        this.description = description;
        this.energy = energy;
        this.sellPrice = sellPrice;
        this.processingTime = processingTime;
        this.ingredientsSupplier = ingredientsSupplier;
        this.isEdible = isEdible;
        this.isCoalNeeded = false;
    }


    MadeProductType(String name, CraftTypeSupplier craftType, String description, Integer energy,
                    TimeAndDate processingTime, IngredientsSupplier ingredientsSupplier,
                    Integer sellPrice, boolean isEdible, boolean isCoalNeeded) {
        this.name = name;
        this.craftTypeSupplier = craftType;
        this.description = description;
        this.energy = energy;
        this.processingTime = processingTime;
        this.ingredientsSupplier = ingredientsSupplier;  // Store the supplier
        this.sellPrice = sellPrice;
        this.isEdible = isEdible;
        this.isCoalNeeded = isCoalNeeded;
    }


    public static MadeProductType findByCraft(CraftType craft) {
        for (MadeProductType value : MadeProductType.values()) {
            if (craft.equals(value.getCraft())) return value;
        }
        return null;
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
            now = now.getNextXHour(processing.getHour());
            return now.getNextXDay(processing.getDay());
        }
    }

    private static TimeAndDate calcNextMorning() {
        return null;
    }


    public Response isIngredientsValid(Salable product, Integer amount, boolean coal) {
        if (isCoalNeeded && !coal) {
            return new Response("No coal given!");
        }

        for (Salable ingredient : this.getIngredients().keySet()) {
            if (product.equals(ingredient)) {
                if (amount >= this.getIngredients().get(ingredient)) return new Response("", true);
                return new Response("Amount is not enough");
            }
            return new Response("Ingredient not found.");
        }
        return new Response("", true);
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