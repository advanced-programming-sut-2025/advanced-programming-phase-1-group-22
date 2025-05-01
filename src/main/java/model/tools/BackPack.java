package model.tools;

import lombok.Getter;
import lombok.Setter;
import model.products.ProductQuality;
import model.relations.Player;
import model.Salable;
import model.exception.InvalidInputException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class BackPack {
    private BackPackType backPackType;
    private Map<Salable, Integer> products = new HashMap<>();

    public BackPack(BackPackType backPackType) {
        this.backPackType = backPackType;
    }

    public String showInventory() {
        StringBuilder message = new StringBuilder();
        for (Map.Entry<Salable, Integer> salableIntegerEntry : products.entrySet()) {
            message.append(salableIntegerEntry.getKey().getName()).append(" : ").append(salableIntegerEntry.getValue());
        }
        return message.toString();
    }

    public void deleteProductFromBackPack(Salable product, Player player, int itemNumber) {
        TrashCan trashCan = getPlayerTrashCan(player);
        if (trashCan != null) {
            trashCan.givePlayerProductPrice(player, product, itemNumber);
        }
        Salable equivalentProduct = findEquivalentProduct(product);
        if (equivalentProduct != null) {
            if (products.get(equivalentProduct) == itemNumber) {
                products.remove(equivalentProduct);
            } else if (products.get(product) < itemNumber) {
                int oldItemNumber = products.getOrDefault(equivalentProduct, 0);
                products.put(equivalentProduct, oldItemNumber - itemNumber);
            }
        }
    }

    public void addProductToBackPack(Salable product, int itemNumber) {
        Salable existingProduct = findEquivalentProduct(product);
        if (existingProduct != null) {
            int currentQuantity = products.getOrDefault(existingProduct, 0);
            products.put(existingProduct, currentQuantity + itemNumber);
        } else {
            products.put(product, itemNumber);
        }
    }

    private Salable findEquivalentProduct(Salable newProduct) {
        for (Salable existingProduct : products.keySet()) {
            if (areProductsEquivalent(existingProduct, newProduct)) {
                return existingProduct;
            }
        }
        return null;
    }

    private boolean areProductsEquivalent(Salable product1, Salable product2) {
        if (!product1.getClass().equals(product2.getClass())) {
            return false;
        }

        try {
            for (Field field : product1.getClass().getDeclaredFields()) {
                if (field.getType().equals(ProductQuality.class)) {
                    field.setAccessible(true);
                    ProductQuality quality1 = (ProductQuality) field.get(product1);
                    ProductQuality quality2 = (ProductQuality) field.get(product2);
                    field.setAccessible(false);
                    return quality1 == quality2;
                }
            }
            return true;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to compare products", e);
        }
    }

    public Salable getProductFromBackPack(String name) {
        for (Map.Entry<Salable, Integer> salableIntegerEntry : products.entrySet()) {
            if (salableIntegerEntry.getKey().getName().equals(name)) {
                return salableIntegerEntry.getKey();
            }
        }
        return null;
    }

    public Integer countProductFromBackPack(String salable) {
        Salable product = findProductInBackPackByNAme(salable);
        if (product == null) return 0;
        return products.get(product);
    }

    public Boolean isInventoryHaveCapacity(Salable product) {
        return backPackType.getIsInfinite() || backPackType.getCapacity() < products.size() ||
                products.containsKey(product);
    }

    private TrashCan getPlayerTrashCan(Player player) {
        for (Map.Entry<Salable, Integer> salableIntegerEntry : player.getInventory().getProducts().entrySet()) {
            if (salableIntegerEntry.getKey() instanceof TrashCan) {
                return (TrashCan) salableIntegerEntry.getKey();
            }
        }
        return null;
    }

    public boolean checkProductAvailabilityInBackPack(String name, int count) {
        Salable product = findProductInBackPackByNAme(name);
        if (product == null) return false;
        return products.get(product) >= count;
    }

    public Salable findProductInBackPackByNAme(String product) {
        for (Salable salable : products.keySet()) {
            if (salable.getName().equals(product)) {
                return salable;
            }
        }
        return null;

    }
}