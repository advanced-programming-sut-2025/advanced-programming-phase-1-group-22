package model.tools;

import lombok.Getter;
import lombok.Setter;
import model.Player;
import model.Result;
import model.Salable;
import model.exception.InvalidInputException;
import model.products.Product;
import model.shelter.ShippingBin;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
@Getter
@Setter
public class BackPack {
    private BackPackType backPackType;
    private Map<Salable, Integer> products = new HashMap<>();

    public BackPack(BackPackType backPackType) {
        this.backPackType = backPackType;
    }

    public String showInventory(){
        StringBuilder message = new StringBuilder();
        for (Map.Entry<Salable, Integer> salableIntegerEntry : products.entrySet()) {
            message.append(salableIntegerEntry.getKey().getName()).append(" : ").append(salableIntegerEntry.getValue());
        }
        return message.toString();
    }

    public void deleteProductFromBackPack(Salable product,Player player,int itemNumber) {
        TrashCan trashCan = getPlayerTrashCan(player);
        if (trashCan != null){
            trashCan.givePlayerProductPrice(player,product,itemNumber);
        }
        if (products.get(product) == itemNumber){
            products.remove(product);
        }
        else if (products.get(product) < itemNumber){
            int oldItemNumber = products.getOrDefault(product,0);
            products.put(product,oldItemNumber - itemNumber);
        }
    }

    public void addProductToBackPack(Salable product,int itemNumber) {
        int oldValue = products.getOrDefault(product,0);
        products.put(product,itemNumber + oldValue);
    }

    public Salable getProductFromBackPack(String name) throws InvalidInputException{
        for (Map.Entry<Salable, Integer> salableIntegerEntry : products.entrySet()) {
            if (salableIntegerEntry.getKey().getName().equals(name)){
                return salableIntegerEntry.getKey();
            }
        }
        throw new InvalidInputException("there is no item name " + name);
    }

    public Boolean isInventoryHaveCapacity(Salable product){
		return backPackType.getCapacity() < products.size() ||
				products.containsKey(product);
	}

    private TrashCan getPlayerTrashCan(Player player){
        for (Map.Entry<Salable, Integer> salableIntegerEntry : player.getInventory().getProducts().entrySet()) {
            if (salableIntegerEntry.getKey() instanceof TrashCan){
                return (TrashCan) salableIntegerEntry.getKey();
            }
        }
        return null;
    public boolean checkProductAvailabilityInBackPack(Product product, int count) {
        if (!products.containsKey(product)) return false;
        return products.get(product) >= count;
    }
}
