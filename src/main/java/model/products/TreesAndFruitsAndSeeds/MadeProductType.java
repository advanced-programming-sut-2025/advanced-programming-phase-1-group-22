package model.products.TreesAndFruitsAndSeeds;

import lombok.Getter;
import lombok.ToString;
import model.Salable;
import model.TimeAndDate;
import model.animal.Fish;
import model.exception.InvalidInputException;
import model.products.AnimalProductType;
import model.products.HarvestAbleProduct;
import model.products.Product;
import model.source.Crop;
import model.source.CropType;
import model.source.MineralType;
import model.source.SeedType;

import java.util.Map;

@Getter
@ToString
public enum MadeProductType implements Product {
    HONEY("It's a sweet syrup produced by bees.", 75, new TimeAndDate(4, 0), Map.of(), 350, true),
    CHEESE("It's your basic cheese.", 100, new TimeAndDate(0, 3), Map.of(AnimalProductType.MILK, 1, AnimalProductType.BIG_MILK, 1), null, true) {
        @Override
        public Integer calcPrice(Product product) {
            if (product.equals(AnimalProductType.MILK)) {
                return 230;
            }
            return 345;
        }
    },
    GOAT_CHEESE("Soft cheese made from goat's milk.", 100, new TimeAndDate(0, 3), Map.of(AnimalProductType.GOAT_MILK, 1, AnimalProductType.BIG_GOAT_MILK, 1), null, true) {
        @Override
        public Integer calcPrice(Product product) {
            if (product.equals(AnimalProductType.GOAT_MILK)) {
                return 400;
            }
            return 600;
        }
    },
    BEER("Drink in moderation.", 50, new TimeAndDate(1, 0), Map.of(CropType.WHEAT, 1), 200, true),
    VINEGAR("An aged fermented liquid used in many cooking recipes.", 13, new TimeAndDate(0, 10), Map.of(CropType.UNMILLED_RICE, 1), 100, true),
    COFFE("It smells delicious. This is sure to give you a boost.", 75, new TimeAndDate(0, 2), Map.of(CropType.COFFEE_BEAN, 5), 150, true),
    JUICE("A sweet, nutritious beverage.", null, new TimeAndDate(4, 0), null, null, true) {
        @Override
        public Integer calcPrice(Product product) {
            return (int) (2.25 * (double) product.getSellPrice());
        }

        @Override
        public Integer calcEnergy(Product product) {
            return 2 * ((HarvestAbleProduct) product).getContainingEnergy();
        }

        @Override
        public boolean isIngredientsValid(Product ingredient, Integer amount, boolean coal) {
            if (ingredient instanceof Crop) {
                if (amount > 0) return true;
                throw new InvalidInputException("Not enough ingredients.");
            }
            throw new InvalidInputException("Ingredient is not a vegtable");
        }
    },
    MEAD("A fermented beverage made from honey. Drink in moderation.", 100, new TimeAndDate(0, 10), Map.of(MadeProductType.HONEY, 1), 300, true),
    PALE_ALE("Drink in moderation.", 50, new TimeAndDate(3, 0), Map.of(CropType.HOPS, 1), 300, true),
    WINE("Drink in moderation.", null, new TimeAndDate(7, 0), null, null, true) {
        @Override
        public Integer calcEnergy(Product product) {
            return (int) (1.75 * (double) ((HarvestAbleProduct) product).getContainingEnergy());
        }

        @Override
        public Integer calcPrice(Product product) {
            return 3 * product.getSellPrice();
        }

        @Override
        public boolean isIngredientsValid(Product ingredient, Integer amount, boolean coal) {
            if (ingredient instanceof Fruit) {
                if (amount > 0) return true;
                throw new InvalidInputException("Not enough ingredients.");
            }
            throw new InvalidInputException("Ingredient is not a fruit");
        }
    },
    //    DRIED_MUSHROOMS("A package of gourmet mushrooms.", 50, null, null, null, true) {
//        @Override
//        public Integer calcPrice(Product product) {
//            return (int) (7.5 * (double) product.getSellPrice()) + 25;
//        }
//
//        @Override
//        public boolean isIngredientsValid(Product ingredient, Integer amount, boolean coal) {
//            if (ingredient instanceof HarvestAbleProduct && ((HarvestAbleProduct)ingredient).isMushroom()) {
//                if (amount > 4) return true;
//                throw new InvalidInputException("Not enough ingredients.");
//            }
//            throw new InvalidInputException("Ingredient is not a mushroom");
//        }
//
//        @Override
//        public TimeAndDate calcProccesingTime(Product product) {
//            return calcNextMorning();
//        }
//    },
    DRIED_FRUIT("Chewy pieces of dried fruit.", 75, null, null, null, true) {
        @Override
        public Integer calcPrice(Product product) {
            return (int) (7.5 * (double) product.getSellPrice()) + 25;
        }

        @Override
        public boolean isIngredientsValid(Product ingredient, Integer amount, boolean coal) {
            if (ingredient.equals(CropType.GRAPE)) {
                throw new InvalidInputException("Grapes can't be dried");
            }
            if (ingredient instanceof Fruit) {
                if (amount > 4) return true;
                throw new InvalidInputException("Not enough ingredients.");
            }
            throw new InvalidInputException("Ingredient is not a food");
        }

        @Override
        public TimeAndDate calcProccesingTime(Product product) {
            return calcNextMorning();
        }
    },
    RAISINS("It's said to be the Junimos' favorite food.", 125, null, Map.of(CropType.GRAPE, 5), 600, true) {
        @Override
        public TimeAndDate calcProccesingTime(Product product) {
            return calcNextMorning();
        }
    },
    COAL("Turns 10 pieces of wood into one piece of coal.", 0, new TimeAndDate(0, 1), Map.of(MineralType.WOOD, 10), 50, false), //RODO Adding wood to minerals
    CLOTH("A bolt of fine wool cloth.", 0, new TimeAndDate(0, 4), Map.of(AnimalProductType.SHEEP_WOOL, 1, AnimalProductType.RABBIT_WOOL, 1), 470, false),
    MAYONNAISE("It looks spreadable.", 50, new TimeAndDate(0, 3), Map.of(AnimalProductType.HEN_EGG, 1, AnimalProductType.HEN_BIG_EGG, 1), null, true) {
        @Override
        public Integer calcPrice(Product product) {
            if (product.equals(AnimalProductType.HEN_EGG)) return 190;
            return 237;
        }
    },
    DUCK_MAYONNAISE("It's a rich, yellow mayonnaise.", 75, new TimeAndDate(0, 3), Map.of(AnimalProductType.DUCK_EGG, 1), 37, true),
    DINOSAUR_MAYONNAISE("It's thick and creamy, with a vivid green hue. It smells like grass and leather.", 125, new TimeAndDate(0, 3), Map.of(AnimalProductType.DINOSAUR_EGG, 1), 800, true),
    TRUFFLE_OIL("A gourmet cooking ingredient.", 38, new TimeAndDate(0, 6), Map.of(AnimalProductType.TRUFFLE, 1), 1065, true),
    OIL("All purpose cooking oil.", 13, null, Map.of(CropType.CORN, 1, SeedType.SUNFLOWER_SEEDS, 1, CropType.SUNFLOWER, 1), 100, true) {
        public TimeAndDate calcProccesingTime(Product product) {
            if (product.equals(CropType.CORN)) return new TimeAndDate(0, 6);
            else if (product.equals(CropType.SUNFLOWER)) return new TimeAndDate(2, 0);
            return new TimeAndDate(0, 1);
        }
    },
    PICKLES("A jar of your home-made pickles.", null, new TimeAndDate(0, 6), null, null, true) {
        @Override
        public Integer calcEnergy(Product product) {
            return (int) (1.75 * (double) ((HarvestAbleProduct) product).getContainingEnergy());
        }

        @Override
        public Integer calcPrice(Product product) {
            return 2 * product.getSellPrice() + 50;
        }

        @Override
        public boolean isIngredientsValid(Product ingredient, Integer amount, boolean coal) {
            if (ingredient instanceof Crop) {
                if (amount > 0) return true;
                throw new InvalidInputException("Not enough ingredients.");
            }
            throw new InvalidInputException("Ingredient is not a vegetables");
        }
    },
    JELLY("Gooey.", null, new TimeAndDate(3, 0), null, null, true) {
        @Override
        public Integer calcEnergy(Product product) {
            return 2 * ((HarvestAbleProduct) product).getContainingEnergy();
        }

        @Override
        public Integer calcPrice(Product product) {
            return 2 * product.getSellPrice() + 50;
        }

        @Override
        public boolean isIngredientsValid(Product ingredient, Integer amount, boolean coal) {
            if (ingredient instanceof Fruit) {
                if (amount > 0) return true;
                throw new InvalidInputException("Not enough ingredients.");
            }
            throw new InvalidInputException("Ingredient is not a fruit");
        }
    },
    SMOKED_FISH("A whole fish, smoked to perfection.", null, new TimeAndDate(0, 1), null, null, true) {
        @Override
        public Integer calcEnergy(Product product) {
            return (int) (1.5 * (double) ((Fish) product).getContainingEnergy());
        }

        @Override
        public Integer calcPrice(Product product) {
            return 2 * product.getSellPrice();
        }

        @Override
        public boolean isIngredientsValid(Product ingredient, Integer amount, boolean coal) {
            if (!coal) throw new InvalidInputException("No coal is given");
            if (ingredient instanceof Fish) {
                if (amount > 0) return true;
                throw new InvalidInputException("Not enough ingredients.");
            }
            throw new InvalidInputException("Ingredient is not a fish");
        }
    },
    IRON_BAR("Turns ore and coal into metal bars.", 0, new TimeAndDate(0, 4), Map.of(MineralType.IRON_ORE, 5), 1500, false, true),
    COPPER_BAR("Turns ore and coal into metal bars.", 0, new TimeAndDate(0, 4), Map.of(MineralType.COPPER_ORE, 5), 750, false, true),
    GOLD_BAR("Turns ore and coal into metal bars.", 0, new TimeAndDate(0, 4), Map.of(MineralType.GOLD_ORE, 5), 4000, false, true),
    IRIDIUM_BAR("Turns ore and coal into metal bars.", 0, new TimeAndDate(0, 4), Map.of(MineralType.IRIDIUM_ORE, 5), 10000, false, true); //TODO SellPrice not found


    private String description;
    private Integer energy;
    private TimeAndDate processingTime;
    private Map<Salable, Integer> ingredients;
    private Integer sellPrice;
    private boolean isEdible;
    private boolean isCoalNeeded;

    MadeProductType(String description, Integer energy, TimeAndDate processingTime, Map<Salable, Integer> ingredients, Integer sellPrice, boolean isEdible) {
        this.description = description;
        this.energy = energy;
        this.sellPrice = sellPrice;
        this.processingTime = processingTime;
        this.ingredients = ingredients;
        this.isEdible = isEdible;
        this.isCoalNeeded = false;
    }

    MadeProductType(String description, Integer energy, TimeAndDate processingTime, Map<Salable, Integer> ingredients, Integer sellPrice, boolean isEdible, boolean isCoalNeeded) {
        this.description = description;
        this.energy = energy;
        this.sellPrice = sellPrice;
        this.processingTime = processingTime;
        this.ingredients = ingredients;
        this.isEdible = isEdible;
        this.isCoalNeeded = isCoalNeeded;
    }


    MadeProductType() {
    }

    public Integer calcPrice(Product product) {
        return sellPrice;
    }


    public Integer calcEnergy(Product product) {
        return energy;
    }


    public TimeAndDate calcProccesingTime(Product product) {
        return processingTime;
    }

    private static TimeAndDate calcNextMorning() {
        //TODO
        return null;
    }


    public boolean isIngredientsValid(Product product, Integer amount, boolean coal) {
        if (isCoalNeeded && !coal) {
            throw new InvalidInputException("No coal given!");
        }

        for (Salable ingredient : ingredients.keySet()) {
            if (product.equals(ingredient)) {
                if (amount >= ingredients.get(ingredient)) return true;
                throw new InvalidInputException("Amount is not enough");
            }
            throw new InvalidInputException("Ingredient not found.");
        }
        return true;
    }

    @Override
    public int getSellPrice() {
        return this.sellPrice;
    }
}