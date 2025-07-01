package io.github.some_example_name.model.tools;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.github.some_example_name.model.products.ProductQuality;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.saveGame.JsonPreparable;
import io.github.some_example_name.saveGame.ObjectMapWrapper;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class BackPack implements JsonPreparable {
    private BackPackType backPackType;
    private Map<Salable, Integer> products = new HashMap<>();

    public BackPack(BackPackType backPackType) {
        this.backPackType = backPackType;
    }

    public String showInventory() {
        StringBuilder message = new StringBuilder();
        for (Map.Entry<Salable, Integer> salableIntegerEntry : products.entrySet()) {
            message.append(salableIntegerEntry.getKey().getName()).append(" : ").append(salableIntegerEntry.getValue()).append("\n");
        }
        return message.toString();
    }

    public void deleteProductFromBackPack(Salable product, Player player, int itemNumber) {
        if (product == null) {
            return;
        }
        TrashCan trashCan = getPlayerTrashCan(player);
        if (trashCan != null) {
            trashCan.givePlayerProductPrice(player, product, itemNumber);
        }
        Salable equivalentProduct = findEquivalentProduct(product);
        if (equivalentProduct != null) {
            if (products.get(equivalentProduct) == itemNumber) {
                products.remove(equivalentProduct);
            } else if (products.get(product) > itemNumber) {
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

        if (product1.getClass().isEnum()) {
            return product1 == product2;
        }

        try {
            boolean hasProductQualityField = false;
            boolean productQualityMatches = true;

            for (Field field : product1.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value1 = field.get(product1);
                Object value2 = field.get(product2);
                field.setAccessible(false);

                if (field.getType().equals(ProductQuality.class)) {
                    hasProductQualityField = true;
                    productQualityMatches = (value1 == value2);
                } else if (!Objects.equals(value1, value2)) {
                    return false;
                }
                if (product1 instanceof Tool && field.getName().equals("level")) {
                    field.setAccessible(true);
                    int level1 = (int) field.get(product1);
                    int level2 = (int) field.get(product2);
                    field.setAccessible(false);
                    return level2 == level1;
                }
            }

            return !hasProductQualityField || productQualityMatches;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to compare products", e);
        }
    }

    public Salable getProductFromBackPack(String name) {
        for (Map.Entry<Salable, Integer> salableIntegerEntry : products.entrySet()) {
            if (salableIntegerEntry.getKey().getName().equalsIgnoreCase(name)) {
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
        Salable product1;
        if (!(product instanceof Tool)) {
            product1 = findProductInBackPackByNAme(product.getName());
            if (product1 == null) {
                return backPackType.getIsInfinite() || (backPackType.getCapacity() > products.size());
            }
            product = product1;
        }
        return backPackType.getIsInfinite() || (backPackType.getCapacity() > products.size()) ||
                productsAreEquivalent(product);
    }

    private boolean productsAreEquivalent(Salable product) {
        for (Map.Entry<Salable, Integer> salableIntegerEntry : products.entrySet()) {
            if (areProductsEquivalent(product, salableIntegerEntry.getKey())) {
                return true;
            }
        }
        return false;
    }

    private TrashCan getPlayerTrashCan(Player player) {
        return player.getCurrentTrashCan();
    }

    public boolean checkProductAvailabilityInBackPack(String name, int count) {
        Salable product = findProductInBackPackByNAme(name);
        if (product == null) return false;
        return products.get(product) >= count;
    }

    public Salable findProductInBackPackByNAme(String product) {
        return getProductFromBackPack(product);
//        for (Salable salable : products.keySet()) {
//            if (salable.getName().equals(product)) {
//                return salable;
//            }
//        }
//        return null;

    }
    @JsonProperty("productsWrapper")
    private ObjectMapWrapper productsWrapper;

    @Override
    public void prepareForSave(ObjectMapper mapper) {
        this.productsWrapper = new ObjectMapWrapper((Map<Object, Integer>)(Map<?, ?>) products, mapper);
    }

    @Override
    public void unpackAfterLoad(ObjectMapper mapper) {
        this.products = (Map<Salable, Integer>) (Map<?, ?>) productsWrapper.toMap(mapper);
    }
}
