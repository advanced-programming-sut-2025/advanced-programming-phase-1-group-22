package model.cook;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.Buff;
import model.abilitiy.Ability;
import model.animal.FishType;
import model.products.AnimalProductType;
import model.products.Product;
import model.products.TreesAndFruitsAndSeeds.FruitType;
import model.products.TreesAndFruitsAndSeeds.MadeProductType;
import model.source.CropType;
import model.structure.stores.StoreType;
import model.tools.FishingPole;

import java.util.Map;

@Getter
@ToString
public enum FoodType implements Product {
    FRIED_EGG(null, 50, 35) {
        @Override
        public Boolean isValidIngredient(Product... products) {
            return products[0].equals(AnimalProductType.DINOSAUR_EGG) ||
                    products[0].equals(AnimalProductType.HEN_EGG) ||
                    products[0].equals(AnimalProductType.DUCK_EGG) ||
                    products[0].equals(AnimalProductType.HEN_BIG_EGG);
        }
    },
    BACKED_FISH(Map.of(FishType.SARDINE, 1, FishType.SALMON, 1, CropType.WHEAT, 1), 75, 100),
    SALAD(Map.of(CropType.DANDELION, 1), 113, 110),
    OMELET(null, 100, 115, StoreType.STARDROPSALON) {
        @Override
        public Boolean isValidIngredient(Product... products) {
            return (products[0].equals(AnimalProductType.DINOSAUR_EGG) ||
                    products[0].equals(AnimalProductType.HEN_EGG) ||
                    products[0].equals(AnimalProductType.DUCK_EGG) ||
                    products[0].equals(AnimalProductType.HEN_BIG_EGG)) &&
                    (products[1].equals(AnimalProductType.MILK) ||
                            products[1].equals(AnimalProductType.BIG_GOAT_MILK) ||
                            products[1].equals(AnimalProductType.GOAT_MILK) ||
                            products[1].equals(AnimalProductType.BIG_MILK));
        }
    },
    PUMPKIN_PIE(null, 225, 385, StoreType.STARDROPSALON) {
        @Override
        public Boolean isValidIngredient(Product... products) {
            return (products[2].equals(AnimalProductType.MILK) ||
                    products[2].equals(AnimalProductType.BIG_GOAT_MILK) ||
                    products[2].equals(AnimalProductType.GOAT_MILK) ||
                    products[2].equals(AnimalProductType.BIG_MILK)) &&
                    products[0].equals(CropType.PUMPKIN) && products[1].equals(CropType.WHEAT);
        }
    },
    SPAGHETTI(Map.of(CropType.TOMATO, 1, CropType.WHEAT, 1), 75, 120, StoreType.STARDROPSALON),
    PIZZA(Map.of(CropType.TOMATO, 1, MadeProductType.CHEESE, 1, CropType.WHEAT, 1), 150, 300, StoreType.STARDROPSALON),
    TORTILLA(Map.of(CropType.CORN, 1), 50, 50, StoreType.STARDROPSALON),
    MAKI_ROLL(null, 100, 220, StoreType.STARDROPSALON) {
        @Override
        public Boolean isValidIngredient(Product... products) {
            return (products[0] instanceof FishType);
        }
    },
    TRIPLE_SHOT_ESPRESSO(Map.of(MadeProductType.COFFE, 3), 200, 450, new Buff(5, 100), StoreType.STARDROPSALON),
    COOKIE(null, 90, 140, StoreType.STARDROPSALON) {
        @Override
        public Boolean isValidIngredient(Product... products) {
            return (products[0].equals(AnimalProductType.DINOSAUR_EGG) ||
                    products[0].equals(AnimalProductType.HEN_EGG) ||
                    products[0].equals(AnimalProductType.DUCK_EGG) ||
                    products[0].equals(AnimalProductType.HEN_BIG_EGG)) &&
                    products[1].equals(CropType.WHEAT);
        }
    },
    HASH_BROWNS(Map.of(CropType.POTATO, 1, MadeProductType.OIL, 1), 90, 120, new Buff(5, Ability.FARMING), StoreType.STARDROPSALON),
    PANCAKES(null, 90, 80, new Buff(11, Ability.FORAGING), StoreType.STARDROPSALON) {
        @Override
        public Boolean isValidIngredient(Product... products) {
            return (products[0].equals(AnimalProductType.DINOSAUR_EGG) ||
                    products[0].equals(AnimalProductType.HEN_EGG) ||
                    products[0].equals(AnimalProductType.DUCK_EGG) ||
                    products[0].equals(AnimalProductType.HEN_BIG_EGG)) &&
                    products[1].equals(CropType.WHEAT);
        }
    },
    FRUIT_SALAD(Map.of(FruitType.APRICOT, 1, CropType.BLUEBERRY, 1, CropType.MELON, 1), 263, 450, StoreType.STARDROPSALON),
    RED_PLATE(Map.of(CropType.RED_CABBAGE, 1, CropType.RADISH, 1), 240, 400, new Buff(3, 50), StoreType.STARDROPSALON),
    BREAD(Map.of(CropType.WHEAT, 1), 50, 60, StoreType.STARDROPSALON),
    SALMON_DINNER(Map.of(FishType.SALMON, 1, CropType.KALE, 1, CropType.AMARANTH, 1), 125, 300, StoreType.STARDROPSALON),
    VEGETABLE_MEDLEY(Map.of(CropType.TOMATO, 1, CropType.BEET, 1), 165, 120, StoreType.STARDROPSALON),
    FORMER_LUNCH(Map.of(CropType.PARSNIP, 1, FoodType.OMELET, 1), 200, 150, new Buff(5, Ability.FARMING), Ability.FARMING, 1),
    SURVIVAL_BURGER(Map.of(CropType.EGGPLANT, 1, CropType.CARROT, 1, FoodType.BREAD, 1), 125, 180, new Buff(5, Ability.FORAGING), Ability.FORAGING, 3),
    DISH_O_THE_SEA(Map.of(FoodType.HASH_BROWNS, 1, FishType.SARDINE, 1), 150, 220, new Buff(5, Ability.FISHING), Ability.FISHING, 2),
    SEA_FORM_PUDDING(Map.of(FishType.FLOUNDER, 1, FishType.MIDNIGHT_CARP, 1), 175, 300, new Buff(10, Ability.FISHING), Ability.FISHING, 3),
    MINERS_TREAT(null, 125, 200, new Buff(5, Ability.MINING), Ability.MINING, 1) {
        @Override
        public Boolean isValidIngredient(Product... products) {
            return (products[1].equals(AnimalProductType.MILK) ||
                    products[1].equals(AnimalProductType.BIG_GOAT_MILK) ||
                    products[1].equals(AnimalProductType.GOAT_MILK) ||
                    products[1].equals(AnimalProductType.BIG_MILK)) &&
                    products[0].equals(CropType.CARROT);
        }
    };
    private Map<Product, Integer> ingredients;
    private Integer energy;
    private Integer sellPrice;
    private Buff buff;
    private Object source;
    private Integer[] level;

    FoodType(Map<Product, Integer> products, Integer energy, Integer sellPrice, Buff buff, Object source, Integer... level) {
        this.ingredients = products;
        this.energy = energy;
        this.sellPrice = sellPrice;
        this.buff = buff;
        this.source = source;
        if (level.length == 1) {
            this.level = new Integer[]{level[0]};
        }
    }

    FoodType(Map<Product, Integer> products, Integer energy, Integer sellPrice, Object source, Integer... level) {
        this.ingredients = products;
        this.energy = energy;
        this.sellPrice = sellPrice;
        this.source = source;
        if (level.length == 1) {
            this.level = new Integer[]{level[0]};
        }
    }

    FoodType(Map<Product, Integer> products, Integer energy, Integer sellPrice) {
        this.ingredients = products;
        this.energy = energy;
        this.sellPrice = sellPrice;
    }

    public Boolean isValidIngredient(Product... products) {
        return false;
    }

    @Override
    public int getSellPrice() {
        return this.sellPrice;
    }
}
