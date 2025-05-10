package model.cook;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.Buff;
import model.Fridge;
import model.Salable;
import model.abilitiy.Ability;
import model.animal.Fish;
import model.animal.FishType;
import model.gameSundry.SundryType;
import model.products.AnimalProductType;
import model.products.Product;
import model.products.TreesAndFruitsAndSeeds.FruitType;
import model.products.TreesAndFruitsAndSeeds.MadeProductType;
import model.records.Response;
import model.relations.Player;
import model.source.CropType;
import model.source.MineralType;
import model.structure.stores.StoreType;
import model.tools.BackPack;
import model.tools.FishingPole;

import javax.swing.*;
import javax.xml.transform.sax.SAXResult;
import java.util.Collections;
import java.util.Map;

@Getter
@ToString
public enum FoodType implements Product {
    FRIED_EGG("fried egg", null, 50, 35) {
        @Override
        public Boolean isValidIngredient(Fridge fridge, Player player) {
            return checkEgg(fridge, player);
        }

        @Override
        public String getProductsString() {
            return "Any Egg\n";
        }

        @Override
        public void removeIngredients(Fridge fridge, Player player) {
            AnimalProductType[] eggs = {AnimalProductType.DINOSAUR_EGG, AnimalProductType.HEN_EGG,
                    AnimalProductType.DUCK_EGG, AnimalProductType.HEN_BIG_EGG};
            for (AnimalProductType egg : eggs) {
                if (fridge.checkProductInFridge(egg)) fridge.deleteProduct(egg, 1);
                else player.getInventory().deleteProductFromBackPack(egg, player, 1);
            }
        }
    },
    BACKED_FISH("backed fish", ()->Map.of(FishType.SARDINE, 1, FishType.SALMON, 1, CropType.WHEAT, 1), 75, 100),
    TROUT_SOUP("trout soup", ()->Map.of(), 50, 250/2),
    SALAD("salad", ()->Map.of(CropType.DANDELION, 1, CropType.LEEK, 1), 113, 110),
    OMELET("omelet", null, 100, 115, StoreType.STARDROPSALON) {
        @Override
        public Boolean isValidIngredient(Fridge fridge, Player player) {
            if (!checkEgg(fridge, player)) return false;
            return checkMilk(fridge, player);
        }

        @Override
        public String getProductsString() {
            return "Any Egg, Any Milk\n";
        }

        @Override
        public void removeIngredients(Fridge fridge, Player player) {
            AnimalProductType[] eggs = {AnimalProductType.DINOSAUR_EGG, AnimalProductType.HEN_EGG,
                    AnimalProductType.DUCK_EGG, AnimalProductType.HEN_BIG_EGG};
            AnimalProductType[] milks = {AnimalProductType.MILK, AnimalProductType.BIG_MILK,
                    AnimalProductType.BIG_GOAT_MILK, AnimalProductType.GOAT_MILK};
            for (AnimalProductType egg : eggs) {
                if (fridge.checkProductInFridge(egg)) fridge.deleteProduct(egg, 1);
                else player.getInventory().deleteProductFromBackPack(egg, player, 1);
            }
            for (AnimalProductType milk : milks) {
                if (fridge.checkProductInFridge(milk)) fridge.deleteProduct(milk, 1);
                else player.getInventory().deleteProductFromBackPack(milk, player, 1);
            }
        }
    },
    PUMPKIN_PIE("pumpkin pie", null, 225, 385, StoreType.STARDROPSALON) {
        @Override
        public Boolean isValidIngredient(Fridge fridge, Player player) {

            if (!checkProduct(fridge, player, CropType.PUMPKIN, 1)) return false;
            if (!checkProduct(fridge, player, SundryType.WHEAT_FLOUR, 1)) return false;
            if (!checkProduct(fridge, player, SundryType.SUGAR, 1)) return false;
            if (!checkMilk(fridge, player)) return false;

            return true;
        }

        @Override
        public String getProductsString() {
            return "Any Milk, Pumpkin, Wheat, Sugar\n";
        }

        @Override
        public void removeIngredients(Fridge fridge, Player player) {
            AnimalProductType[] milks = {AnimalProductType.MILK, AnimalProductType.BIG_MILK,
                    AnimalProductType.BIG_GOAT_MILK, AnimalProductType.GOAT_MILK};
            for (AnimalProductType milk : milks) {
                if (fridge.checkProductInFridge(milk)) fridge.deleteProduct(milk, 1);
                else player.getInventory().deleteProductFromBackPack(milk, player, 1);
            }
            removeProduct(fridge, player, CropType.PUMPKIN, 1);
            removeProduct(fridge, player, SundryType.WHEAT_FLOUR, 1);
            removeProduct(fridge, player, SundryType.SUGAR, 1);
        }
    },
    SPAGHETTI("spaghetti", ()->Map.of(CropType.TOMATO, 1, SundryType.WHEAT_FLOUR, 1), 75, 120, StoreType.STARDROPSALON),
    PIZZA("pizza", ()->Map.of(CropType.TOMATO, 1, MadeProductType.CHEESE, 1, SundryType.WHEAT_FLOUR, 1), 150, 300, StoreType.STARDROPSALON),
    TORTILLA("tortilla", ()->Map.of(CropType.CORN, 1), 50, 50, StoreType.STARDROPSALON),
    MAKI_ROLL("maki roll", null, 100, 220, StoreType.STARDROPSALON) {
        @Override
        public Boolean isValidIngredient(Fridge fridge, Player player) {
            if (!checkProduct(fridge, player, SundryType.RICE, 1)) return false;
            if (!checkProduct(fridge, player, MineralType.FIBER, 1)) return false;

            for (Salable salable : fridge.getProducts().keySet()) {
                if (salable instanceof Fish) return true;
            }
            for (Salable salable : player.getInventory().getProducts().keySet()) {
                if (salable instanceof Fish) return true;
            }
            return false;
        }

        @Override
        public String getProductsString() {
            return "Any Fish\n";
        }

        @Override
        public void removeIngredients(Fridge fridge, Player player) {
            removeProduct(fridge, player, SundryType.RICE, 1);
            removeProduct(fridge, player, MineralType.FIBER, 1);
            for (Salable salable : fridge.getProducts().keySet()) {
                if (salable instanceof Fish) {
                    fridge.deleteProduct(salable, 1);
                    return;
                }
            }
            for (Salable salable : player.getInventory().getProducts().keySet()) {
                if (salable instanceof Fish) {
                    player.getInventory().deleteProductFromBackPack(salable, player, 1);
                    return;
                }
            }
        }
    },
    TRIPLE_SHOT_ESPRESSO("triple shot espresso", ()->Map.of(MadeProductType.COFFE, 3), 200, 450, new Buff(5, 100), StoreType.STARDROPSALON),
    COOKIE("cookie", null, 90, 140, StoreType.STARDROPSALON) {
        @Override
        public Boolean isValidIngredient(Fridge fridge, Player player) {
            if (!checkMilk(fridge, player)) return false;
            if (!checkProduct(fridge, player, SundryType.WHEAT_FLOUR, 1)) return false;
            if (!checkProduct(fridge, player, SundryType.SUGAR, 1)) return false;
            return true;
        }

        @Override
        public String getProductsString() {
            return "Any Egg, Wheat\n";
        }

        @Override
        public void removeIngredients(Fridge fridge, Player player) {
            AnimalProductType[] milks = {AnimalProductType.MILK, AnimalProductType.BIG_MILK,
                    AnimalProductType.BIG_GOAT_MILK, AnimalProductType.GOAT_MILK};
            for (AnimalProductType milk : milks) {
                if (fridge.checkProductInFridge(milk)) fridge.deleteProduct(milk, 1);
            }
            removeProduct(fridge, player, SundryType.WHEAT_FLOUR, 1);
            removeProduct(fridge, player, SundryType.SUGAR, 1);
        }
    },
    HASH_BROWNS("hash browns", ()->Map.of(CropType.POTATO, 1, MadeProductType.OIL, 1), 90, 120, new Buff(5, Ability.FARMING), StoreType.STARDROPSALON),
    PANCAKES("pancakes", null, 90, 80, new Buff(11, Ability.FORAGING), StoreType.STARDROPSALON) {
        @Override
        public Boolean isValidIngredient(Fridge fridge, Player player) {
            if (!checkProduct(fridge, player, SundryType.WHEAT_FLOUR, 1)) return false;

            return checkEgg(fridge, player);
        }

        @Override
        public String getProductsString() {
            return "Any Pancakes\n";
        }

        @Override
        public void removeIngredients(Fridge fridge, Player player) {
            AnimalProductType[] eggs = {AnimalProductType.DINOSAUR_EGG, AnimalProductType.HEN_EGG,
                    AnimalProductType.DUCK_EGG, AnimalProductType.HEN_BIG_EGG};
            for (AnimalProductType egg : eggs) {
                if (fridge.checkProductInFridge(egg)) fridge.deleteProduct(egg, 1);
            }
            removeProduct(fridge, player, SundryType.WHEAT_FLOUR, 1);
        }
    },
    FRUIT_SALAD("fruit salad", ()->Map.of(FruitType.APRICOT, 1, CropType.BLUEBERRY, 1, CropType.MELON, 1), 263, 450, StoreType.STARDROPSALON),
    RED_PLATE("red plate", ()->Map.of(CropType.RED_CABBAGE, 1, CropType.RADISH, 1), 240, 400, new Buff(3, 50), StoreType.STARDROPSALON),
    BREAD("bread", ()->Map.of(SundryType.WHEAT_FLOUR, 1), 50, 60, StoreType.STARDROPSALON),
    SALMON_DINNER("salmon dinner", ()->Map.of(FishType.SALMON, 1, CropType.KALE, 1, CropType.AMARANTH, 1), 125, 300, StoreType.STARDROPSALON),
    VEGETABLE_MEDLEY("vegetable medley", ()->Map.of(CropType.TOMATO, 1, CropType.BEET, 1), 165, 120, StoreType.STARDROPSALON),
    FORMER_LUNCH("farmer's lunch", ()->Map.of(CropType.PARSNIP, 1, FoodType.OMELET, 1), 200, 150, new Buff(5, Ability.FARMING), Ability.FARMING, 1),
    SURVIVAL_BURGER("survival burger", ()->Map.of(CropType.EGGPLANT, 1, CropType.CARROT, 1, FoodType.BREAD, 1), 125, 180, new Buff(5, Ability.FORAGING), Ability.FORAGING, 3),
    DISH_O_THE_SEA("dish o the sea", ()->Map.of(FoodType.HASH_BROWNS, 1, FishType.SARDINE, 2), 150, 220, new Buff(5, Ability.FISHING), Ability.FISHING, 2),
    SEA_FORM_PUDDING("sea from pudding", ()->Map.of(FishType.FLOUNDER, 1, FishType.MIDNIGHT_CARP, 1), 175, 300, new Buff(10, Ability.FISHING), Ability.FISHING, 3),
    MINERS_TREAT("miners treat", null, 125, 200, new Buff(5, Ability.MINING), Ability.MINING, 1) {
        @Override
        public Boolean isValidIngredient(Fridge fridge, Player player) {
            if (!checkMilk(fridge, player)) return false;
            if (!checkProduct(fridge, player, CropType.CARROT, 2)) return false;
            if (!checkProduct(fridge, player, SundryType.SUGAR, 1)) return false;
            return true;
        }

        @Override
        public String getProductsString() {
            return "Any Milk, Carrot\n";
        }

        @Override
        public void removeIngredients(Fridge fridge, Player player) {
            AnimalProductType[] milks = {AnimalProductType.MILK, AnimalProductType.BIG_MILK,
                    AnimalProductType.BIG_GOAT_MILK, AnimalProductType.GOAT_MILK};
            for (AnimalProductType milk : milks) {
                if (fridge.checkProductInFridge(milk)) fridge.deleteProduct(milk, 1);
            }
            removeProduct(fridge, player, CropType.CARROT, 2);
            removeProduct(fridge, player, SundryType.SUGAR, 1);

        }
    };
    private final String name;
    private int energy;
    private int sellPrice;
    private Buff buff;
    private Object source;
    private Integer[] level;
    private final IngredientsSupplier ingredientsSupplier;
    private transient volatile Map<Salable, Integer> resolvedIngredients;

    @FunctionalInterface
    private interface IngredientsSupplier {
        Map<Salable, Integer> get();
    }

    public Map<Salable, Integer> getIngredients() {
        Map<Salable, Integer> result = resolvedIngredients;
        if (result == null) {
            synchronized (this) {
                result = resolvedIngredients;
                if (result == null) {
                    resolvedIngredients = result = ingredientsSupplier != null ?
                            ingredientsSupplier.get() : Collections.emptyMap();
                }
            }
        }
        return result;
    }

    FoodType(String name, IngredientsSupplier ingredientsSupplier, Integer energy, Integer sellPrice, Buff buff, Object source, Integer... level) {
        this.name = name;
        this.ingredientsSupplier = ingredientsSupplier;
        this.energy = energy;
        this.sellPrice = sellPrice;
        this.buff = buff;
        this.source = source;
        if (level.length == 1) {
            this.level = new Integer[]{level[0]};
        }
    }

    FoodType(String name, IngredientsSupplier ingredientsSupplier, Integer energy, Integer sellPrice, Object source, Integer... level) {
        this.name = name;
        this.ingredientsSupplier = ingredientsSupplier;
        this.energy = energy;
        this.sellPrice = sellPrice;
        this.source = source;
        if (level.length == 1) {
            this.level = new Integer[]{level[0]};
        }
    }

    FoodType(String name, IngredientsSupplier ingredientsSupplier, Integer energy, Integer sellPrice) {
        this.name = name;
        this.ingredientsSupplier = ingredientsSupplier;
        this.energy = energy;
        this.sellPrice = sellPrice;
    }

    public Boolean isValidIngredient(Fridge fridge, Player player) {
        for (Salable product : this.getIngredients().keySet()) {
            if (!fridge.checkProductAvailability(product.getName(), this.getIngredients().get(product))) {
                BackPack inventory = player.getInventory();
                if (!inventory.checkProductAvailabilityInBackPack(product.getName(), this.getIngredients().get(product))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int getSellPrice() {
        return this.sellPrice;
    }

    @Override
    public String getName() {
        return this.name.toLowerCase();
    }

    public String getProductsString() {
        if (this.getIngredients() != null) {
            StringBuilder res = new StringBuilder();
            for (Salable salable : this.getIngredients().keySet()) {
                res.append(salable.getName());
                if (this.getIngredients().get(salable) != 1) res.append(" *").append(this.getIngredients().get(salable));
                res.append(", ");
            }
            return res + "\n";
        }
        return "";
    }

    public void removeIngredients(Fridge fridge, Player player) {
        for (Salable product : this.getIngredients().keySet()) {
            Salable product1 = fridge.findProduct(product.getName());
            if (product1 != null) {
                fridge.deleteProduct(product1, this.getIngredients().get(product));
            } else {
                BackPack inventory = player.getInventory();
                product1 = inventory.findProductInBackPackByNAme(product.getName());
                inventory.deleteProductFromBackPack(product1, player, this.getIngredients().get(product));
            }
        }
    }

    private static boolean checkMilk(Fridge fridge, Player player) {
        if (!(fridge.checkProductInFridge(AnimalProductType.MILK) ||
                fridge.checkProductInFridge(AnimalProductType.BIG_GOAT_MILK) ||
                fridge.checkProductInFridge(AnimalProductType.GOAT_MILK) ||
                fridge.checkProductInFridge(AnimalProductType.BIG_MILK))) {
            if (!(player.getInventory().checkProductAvailabilityInBackPack(AnimalProductType.MILK.getName(), 1) ||
                    player.getInventory().checkProductAvailabilityInBackPack(AnimalProductType.BIG_GOAT_MILK.getName(), 1) ||
                    player.getInventory().checkProductAvailabilityInBackPack(AnimalProductType.GOAT_MILK.getName(), 1) ||
                    player.getInventory().checkProductAvailabilityInBackPack(AnimalProductType.BIG_MILK.getName(), 1))) {
                return false;
            }
        }
        return true;
    }
    private static boolean checkEgg(Fridge fridge, Player player) {
        if (!(fridge.checkProductInFridge(AnimalProductType.DINOSAUR_EGG) ||
                fridge.checkProductInFridge(AnimalProductType.HEN_EGG) ||
                fridge.checkProductInFridge(AnimalProductType.DUCK_EGG) ||
                fridge.checkProductInFridge(AnimalProductType.HEN_BIG_EGG))) {
            if (!(player.getInventory().checkProductAvailabilityInBackPack(AnimalProductType.DINOSAUR_EGG.getName(), 1) ||
                    player.getInventory().checkProductAvailabilityInBackPack(AnimalProductType.HEN_EGG.getName(), 1) ||
                    player.getInventory().checkProductAvailabilityInBackPack(AnimalProductType.DUCK_EGG.getName(), 1) ||
                    player.getInventory().checkProductAvailabilityInBackPack(AnimalProductType.HEN_BIG_EGG.getName(), 1))) {
                return false;
            }
        }
        return true;
    }
    private static boolean checkProduct(Fridge fridge, Player player, Salable salable, int count) {
        return fridge.checkProductInFridge(salable) ||
                player.getInventory().checkProductAvailabilityInBackPack(salable.getName(), count);
    }
    private static void removeProduct(Fridge fridge, Player player, Salable salable, int count) {
        if (fridge.checkProductInFridge(salable)) fridge.deleteProduct(salable, 1);
        else {
            salable = player.getInventory().findProductInBackPackByNAme(salable.getName());
            player.getInventory().deleteProductFromBackPack(salable, player, 1);
        }
    }

    @Override
    public Integer getContainingEnergy() {
        return energy;
    }
}