package model;

import lombok.Getter;
import model.exception.InvalidInputException;
import model.relations.Player;
import model.structure.Structure;
import model.tools.BackPackType;
import model.tools.TrashCan;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Fridge extends Structure {
    private Map<Salable, Integer> products = new HashMap<>();


    public String showInventory(){
        StringBuilder message = new StringBuilder();
        for (Map.Entry<Salable, Integer> salableIntegerEntry : products.entrySet()) {
            message.append(salableIntegerEntry.getKey().getName()).append(" : ").append(salableIntegerEntry.getValue());
        }
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

    public boolean checkProductAvailability(Salable product, int count) {
        if (!products.containsKey(product)) return false;
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
