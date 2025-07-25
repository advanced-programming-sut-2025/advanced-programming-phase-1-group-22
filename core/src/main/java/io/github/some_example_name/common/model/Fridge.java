package io.github.some_example_name.common.model;

import lombok.Getter;
import io.github.some_example_name.common.model.structure.Structure;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Fridge extends Structure {
    private Map<Salable, Integer> products = new HashMap<>();


    public String showInventory(){
        StringBuilder message = new StringBuilder();
        for (Map.Entry<Salable, Integer> salableIntegerEntry : products.entrySet()) {
            message.append(salableIntegerEntry.getKey().getName()).append(" : ")
                    .append(salableIntegerEntry.getValue()).append("\n");
        }
        if (message.toString().equalsIgnoreCase("")) return "No items in refrigerator";
        return message.toString();
    }

    public void deleteProduct(Salable product, int itemNumber) {
        if (products.get(product) == itemNumber){
            products.remove(product);
        }
        else if (products.get(product) < itemNumber){
            int oldItemNumber = products.getOrDefault(product,0);
            products.put(product,oldItemNumber - itemNumber);
        }
    }

    public void addProduct(Salable product,int itemNumber) {
        int oldValue = products.getOrDefault(product,0);
        products.put(product,itemNumber + oldValue);
    }

    public Salable getProduct(String name) {
        for (Map.Entry<Salable, Integer> salableIntegerEntry : products.entrySet()) {
            if (salableIntegerEntry.getKey().getName().equals(name)){
                return salableIntegerEntry.getKey();
            }
        }
        return null;
    }

    public Integer countProduct(Salable salable) {
        if (!products.containsKey(salable)) return 0;
        return products.get(salable);
    }

    public boolean checkProductAvailability(String name, int count) {
        Salable product = findProduct(name);
        if (product == null) return false;
        return products.get(product) >= count;
    }

    public boolean checkProductInFridge(Salable product) {
        return products.containsKey(product);
    }

    public Salable findProduct(String product) {
        for (Salable salable : products.keySet()) {
            if (salable.getName().equals(product)) {
                return salable;
            }
        }
        return null;

    }
}
