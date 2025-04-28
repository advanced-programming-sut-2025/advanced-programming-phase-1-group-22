package model.cook;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.Buff;
import model.Fridge;
import model.Salable;
import model.abilitiy.Ability;
import model.animal.FishType;
import model.products.AnimalProductType;
import model.products.Product;
import model.products.TreesAndFruitsAndSeeds.FruitType;
import model.products.TreesAndFruitsAndSeeds.MadeProductType;
import model.records.Response;
import model.relations.Player;
import model.source.CropType;
import model.structure.stores.StoreType;
import model.tools.BackPack;
import model.tools.FishingPole;

import javax.xml.transform.sax.SAXResult;
import java.util.Map;

@Getter
@ToString
public enum FoodType implements Product {
    FRIED_EGG("fried egg",null, 50, 35) {
        @Override
        public Boolean isValidIngredient(Fridge fridge) {
            return fridge.checkProductInFridge(AnimalProductType.DINOSAUR_EGG) ||
                    fridge.checkProductInFridge(AnimalProductType.HEN_EGG) ||
                    fridge.checkProductInFridge(AnimalProductType.DUCK_EGG) ||
                    fridge.checkProductInFridge(AnimalProductType.HEN_BIG_EGG);
        }
        @Override
        public String getProductsString() {
            return "Any Egg\n";
        }
        @Override
        public void removeIngredients(Fridge fridge) {
            AnimalProductType[] eggs = {AnimalProductType.DINOSAUR_EGG, AnimalProductType.HEN_EGG,
                                        AnimalProductType.DUCK_EGG, AnimalProductType.HEN_BIG_EGG};
            for (AnimalProductType egg : eggs) {
                if (fridge.checkProductInFridge(egg)) fridge.deleteProduct(egg, 1);
            }
        }
    },
    BACKED_FISH("backed fish",Map.of(FishType.SARDINE, 1, FishType.SALMON, 1, CropType.WHEAT, 1), 75, 100),
    SALAD("salad",Map.of(CropType.DANDELION, 1), 113, 110),
    OMELET("omelet",null, 100, 115, StoreType.STARDROPSALON) {
        @Override
        public Boolean isValidIngredient(Fridge fridge) {
            return (fridge.checkProductInFridge(AnimalProductType.DINOSAUR_EGG) ||
                    fridge.checkProductInFridge(AnimalProductType.HEN_EGG) ||
                    fridge.checkProductInFridge(AnimalProductType.DUCK_EGG) ||
                    fridge.checkProductInFridge(AnimalProductType.HEN_BIG_EGG)) &&
                    (fridge.checkProductInFridge(AnimalProductType.MILK) ||
                            fridge.checkProductInFridge(AnimalProductType.BIG_GOAT_MILK) ||
                            fridge.checkProductInFridge(AnimalProductType.GOAT_MILK) ||
                            fridge.checkProductInFridge(AnimalProductType.BIG_MILK));
        }
        @Override
        public String getProductsString() {
            return "Any Egg, Any Milk\n";
        }

        @Override
        public void removeIngredients(Fridge fridge) {
            AnimalProductType[] eggs = {AnimalProductType.DINOSAUR_EGG, AnimalProductType.HEN_EGG,
                    AnimalProductType.DUCK_EGG, AnimalProductType.HEN_BIG_EGG};
            AnimalProductType[] milks = {AnimalProductType.MILK, AnimalProductType.BIG_MILK,
                    AnimalProductType.BIG_GOAT_MILK, AnimalProductType.GOAT_MILK};
            for (AnimalProductType egg : eggs) {
                if (fridge.checkProductInFridge(egg)) fridge.deleteProduct(egg, 1);
            }
            for (AnimalProductType milk : milks) {
                if (fridge.checkProductInFridge(milk)) fridge.deleteProduct(milk, 1);
            }
        }
    },
    PUMPKIN_PIE("pumpkin pie",null, 225, 385, StoreType.STARDROPSALON) {
        @Override
        public Boolean isValidIngredient(Fridge fridge) {
            return (fridge.checkProductInFridge(AnimalProductType.MILK) ||
                    fridge.checkProductInFridge(AnimalProductType.BIG_GOAT_MILK) ||
                    fridge.checkProductInFridge(AnimalProductType.GOAT_MILK) ||
                    fridge.checkProductInFridge(AnimalProductType.BIG_MILK)) &&
                    fridge.checkProductInFridge(CropType.PUMPKIN) && fridge.checkProductInFridge(CropType.WHEAT);
        }
        @Override
        public String getProductsString() {
            return "Any Milk, Pumpkin, Wheat\n";
        }
        @Override
        public void removeIngredients(Fridge fridge) {
            AnimalProductType[] milks = {AnimalProductType.MILK, AnimalProductType.BIG_MILK,
                    AnimalProductType.BIG_GOAT_MILK, AnimalProductType.GOAT_MILK};
            for (AnimalProductType milk : milks) {
                if (fridge.checkProductInFridge(milk)) fridge.deleteProduct(milk, 1);
            }
            fridge.deleteProduct(CropType.PUMPKIN, 1);
            fridge.deleteProduct(CropType.WHEAT, 1);
        }
    },
    SPAGHETTI("spaghetti",Map.of(CropType.TOMATO, 1, CropType.WHEAT, 1), 75, 120, StoreType.STARDROPSALON),
    PIZZA("pizza",Map.of(CropType.TOMATO, 1, MadeProductType.CHEESE, 1, CropType.WHEAT, 1), 150, 300, StoreType.STARDROPSALON),
    TORTILLA("tortilla",Map.of(CropType.CORN, 1), 50, 50, StoreType.STARDROPSALON),
    MAKI_ROLL("maki roll",null, 100, 220, StoreType.STARDROPSALON) {
        @Override
        public Boolean isValidIngredient(Fridge fridge) {
            for (Salable salable : fridge.getProducts().keySet()) {
                if (salable instanceof FishType) return true;
            }
            return false;
        }
        @Override
        public String getProductsString() {
            return "Any Fish\n";
        }
        @Override
        public void removeIngredients(Fridge fridge) {
            for (Salable salable : fridge.getProducts().keySet()) {
                if (salable instanceof FishType) fridge.deleteProduct(salable, 1);
            }
        }
    },
    TRIPLE_SHOT_ESPRESSO("triple shot espresso",Map.of(MadeProductType.COFFE, 3), 200, 450, new Buff(5, 100), StoreType.STARDROPSALON),
    COOKIE("cookie",null, 90, 140, StoreType.STARDROPSALON) {
        @Override
        public Boolean isValidIngredient(Fridge fridge) {
            return (fridge.checkProductInFridge(AnimalProductType.DINOSAUR_EGG) ||
                    fridge.checkProductInFridge(AnimalProductType.HEN_EGG) ||
                    fridge.checkProductInFridge(AnimalProductType.DUCK_EGG) ||
                    fridge.checkProductInFridge(AnimalProductType.HEN_BIG_EGG)) &&
                    fridge.checkProductInFridge(CropType.WHEAT);
        }
        @Override
        public String getProductsString() {
            return "Any Egg, Wheat\n";
        }
        @Override
        public void removeIngredients(Fridge fridge) {
            AnimalProductType[] milks = {AnimalProductType.MILK, AnimalProductType.BIG_MILK,
                    AnimalProductType.BIG_GOAT_MILK, AnimalProductType.GOAT_MILK};
            for (AnimalProductType milk : milks) {
                if (fridge.checkProductInFridge(milk)) fridge.deleteProduct(milk, 1);
            }
            fridge.deleteProduct(CropType.WHEAT,1);
        }
    },
    HASH_BROWNS("hash browns",Map.of(CropType.POTATO, 1, MadeProductType.OIL, 1), 90, 120, new Buff(5, Ability.FARMING), StoreType.STARDROPSALON),
    PANCAKES("pancakes",null, 90, 80, new Buff(11, Ability.FORAGING), StoreType.STARDROPSALON) {
        @Override
        public Boolean isValidIngredient(Fridge fridge) {
            return (fridge.checkProductInFridge(AnimalProductType.DINOSAUR_EGG) ||
                    fridge.checkProductInFridge(AnimalProductType.HEN_EGG) ||
                    fridge.checkProductInFridge(AnimalProductType.DUCK_EGG) ||
                    fridge.checkProductInFridge(AnimalProductType.HEN_BIG_EGG)) &&
                    fridge.checkProductInFridge(CropType.WHEAT);
        }
        @Override
        public String getProductsString() {
            return "Any Pancakes\n";
        }
        @Override
        public void removeIngredients(Fridge fridge) {
            AnimalProductType[] eggs = {AnimalProductType.DINOSAUR_EGG, AnimalProductType.HEN_EGG,
                    AnimalProductType.DUCK_EGG, AnimalProductType.HEN_BIG_EGG};
            for (AnimalProductType egg : eggs) {
                if (fridge.checkProductInFridge(egg)) fridge.deleteProduct(egg, 1);
            }
            fridge.deleteProduct(CropType.WHEAT, 1);
        }
    },
    FRUIT_SALAD("fruit salad",Map.of(FruitType.APRICOT, 1, CropType.BLUEBERRY, 1, CropType.MELON, 1), 263, 450, StoreType.STARDROPSALON),
    RED_PLATE("red plate",Map.of(CropType.RED_CABBAGE, 1, CropType.RADISH, 1), 240, 400, new Buff(3, 50), StoreType.STARDROPSALON),
    BREAD("bread",Map.of(CropType.WHEAT, 1), 50, 60, StoreType.STARDROPSALON),
    SALMON_DINNER("salmon dinner",Map.of(FishType.SALMON, 1, CropType.KALE, 1, CropType.AMARANTH, 1), 125, 300, StoreType.STARDROPSALON),
    VEGETABLE_MEDLEY("vegetable medley",Map.of(CropType.TOMATO, 1, CropType.BEET, 1), 165, 120, StoreType.STARDROPSALON),
    FORMER_LUNCH("former lunch",Map.of(CropType.PARSNIP, 1, FoodType.OMELET, 1), 200, 150, new Buff(5, Ability.FARMING), Ability.FARMING, 1),
    SURVIVAL_BURGER("survival burger",Map.of(CropType.EGGPLANT, 1, CropType.CARROT, 1, FoodType.BREAD, 1), 125, 180, new Buff(5, Ability.FORAGING), Ability.FORAGING, 3),
    DISH_O_THE_SEA("dish o the sea",Map.of(FoodType.HASH_BROWNS, 1, FishType.SARDINE, 1), 150, 220, new Buff(5, Ability.FISHING), Ability.FISHING, 2),
    SEA_FORM_PUDDING("sea from pudding",Map.of(FishType.FLOUNDER, 1, FishType.MIDNIGHT_CARP, 1), 175, 300, new Buff(10, Ability.FISHING), Ability.FISHING, 3),
    MINERS_TREAT("miners treat",null, 125, 200, new Buff(5, Ability.MINING), Ability.MINING, 1) {
        @Override
        public Boolean isValidIngredient(Fridge fridge) {
            return (fridge.checkProductInFridge(AnimalProductType.MILK) ||
                    fridge.checkProductInFridge(AnimalProductType.BIG_GOAT_MILK) ||
                    fridge.checkProductInFridge(AnimalProductType.GOAT_MILK) ||
                    fridge.checkProductInFridge(AnimalProductType.BIG_MILK)) &&
                    fridge.checkProductInFridge(CropType.CARROT);
        }
        @Override
        public String getProductsString() {
            return "Any Milk, Carrot\n";
        }
        @Override
        public void removeIngredients(Fridge fridge) {
            AnimalProductType[] milks = {AnimalProductType.MILK, AnimalProductType.BIG_MILK,
                    AnimalProductType.BIG_GOAT_MILK, AnimalProductType.GOAT_MILK};
            for (AnimalProductType milk : milks) {
                if (fridge.checkProductInFridge(milk)) fridge.deleteProduct(milk, 1);
            }
            fridge.deleteProduct(CropType.CARROT, 1);
        }
    };
    private final String name;
    private Map<Product, Integer> ingredients;
    private int energy;
    private int sellPrice;
    private Buff buff;
    private Object source;
    private Integer[] level;

    FoodType(String name,Map<Product, Integer> products, Integer energy, Integer sellPrice, Buff buff, Object source, Integer... level) {
        this.name = name;
        this.ingredients = products;
        this.energy = energy;
        this.sellPrice = sellPrice;
        this.buff = buff;
        this.source = source;
        if (level.length == 1) {
            this.level = new Integer[]{level[0]};
        }
    }

    FoodType(String name,Map<Product, Integer> products, Integer energy, Integer sellPrice, Object source, Integer... level) {
        this.name = name;
        this.ingredients = products;
        this.energy = energy;
        this.sellPrice = sellPrice;
        this.source = source;
        if (level.length == 1) {
            this.level = new Integer[]{level[0]};
        }
    }

    FoodType(String name,Map<Product, Integer> products, Integer energy, Integer sellPrice) {
        this.name = name;
        this.ingredients = products;
        this.energy = energy;
        this.sellPrice = sellPrice;
    }

    public Boolean isValidIngredient(Fridge fridge) {
        for (Product product : ingredients.keySet()) {
            if (!fridge.checkProductAvailability(product, ingredients.get(product))) return false;
        }
        return true;
    }

    @Override
    public int getSellPrice() {
        return this.sellPrice;
    }

    @Override
    public String getName(){
        return this.name.toLowerCase();
    }

    public String getProductsString() {
        if (ingredients != null) {
            StringBuilder res = new StringBuilder();
            for (Product salable : ingredients.keySet()) {
                res.append(salable.getName());
                if (ingredients.get(salable) != 1) res.append(" *").append(ingredients.get(salable));
                res.append(", ");
            }
            return res + "\n";
        } return "";
    }

    public void removeIngredients(Fridge fridge) {
        for (Product product : ingredients.keySet()) {
            fridge.deleteProduct(product, ingredients.get(product));
        }
    }
}