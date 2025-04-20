package model.tools;

import lombok.Getter;
import lombok.Setter;
import model.Player;
import model.Result;
import model.Salable;
import model.exception.InvalidInputException;
import model.products.Product;
import model.shelter.ShippingBin;

import java.util.InputMismatchException;
import java.util.Map;
@Getter
@Setter
public class BackPack {
    private BackPackType backPackType;
    private Map<Salable, Integer> products;

    public String showInventory(){
        StringBuilder message = new StringBuilder();
        for (Map.Entry<Salable, Integer> salableIntegerEntry : products.entrySet()) {
            message.append(salableIntegerEntry.getKey().getName()).append(" : ").append(salableIntegerEntry.getValue());
        }
        return message.toString();
    }

    public void deleteProductFromBackPack(Salable product,Player player,int itemNumber) {
        if (player.getShippingBin() != null){
            int oldValue = player.getShippingBin().getSalable().getOrDefault(product,0);
            player.getShippingBin().getSalable().put(product,itemNumber + oldValue);
        }
        if (products.get(product) == itemNumber){
            products.remove(product);
        }
        else if (products.get(product) < itemNumber){
            int oldItemNumber = products.getOrDefault(product,0);
            products.put(product,oldItemNumber - itemNumber);
        }
    }

    public Result addProductToBackPack(Salable product,int itemNumber) {
        if (backPackType.getCapacity() >= products.size() &&
                !products.containsKey(product)){
            return new Result(false,"the backpack is full, you can not add new item");
        }
        int oldValue = products.getOrDefault(product,0);
        products.put(product,itemNumber + oldValue);
        return new Result(true,itemNumber + " of " + product.getName() + "successfully added");
    }

    public Salable getProductFromBackPack(String name) {
        for (Map.Entry<Salable, Integer> salableIntegerEntry : products.entrySet()) {
            if (salableIntegerEntry.getKey().getName().equals(name)){
                return salableIntegerEntry.getKey();
            }
        }
        throw new InvalidInputException("there is no item name " + name);
    }
}