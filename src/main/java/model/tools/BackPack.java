package model.tools;

import lombok.Getter;
import lombok.Setter;
import model.Salable;
import model.exception.InvalidInputException;
import model.products.Product;

import java.util.Map;
@Getter
@Setter
public class BackPack {
    private BackPackType backPackType;
    private Integer totalTakenSpace;
    private Map<Salable, Integer> products;

    public String showInventory(){
        StringBuilder message = new StringBuilder();
        for (Map.Entry<Salable, Integer> salableIntegerEntry : products.entrySet()) {
            message.append(salableIntegerEntry.getKey().getName()).append(" : ").append(salableIntegerEntry.getValue());
        }
        return message.toString();
    }

    public void deleteProductFromBackPack(String itemName, int itemNumber) {
        Salable currentProduct = null;
        for (Map.Entry<Salable, Integer> salableIntegerEntry : products.entrySet()) {
            if (salableIntegerEntry.getKey().getName().equals(itemName)){
                currentProduct = salableIntegerEntry.getKey();
            }
        }
        if (currentProduct == null){
            throw new InvalidInputException("the inventory does not contain this item");
        }

    }

    public void addProductToBackPack(Salable product) {

    }

    public void getProductFromBackPack(Salable product) {

    }

    public boolean checkProductAvailabilityInBackPack(Salable product) {


        return false;
    }
}