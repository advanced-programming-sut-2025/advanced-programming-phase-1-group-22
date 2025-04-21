package model.tools;

import lombok.Getter;
import lombok.Setter;
import model.products.Product;

import java.util.HashMap;
import java.util.Map;
@Getter
@Setter
public class BackPack {
    private BackPackType backPackType = BackPackType.NORMAL_BAKCPACK;
    private Map<Product, Integer> products = new HashMap<>();

    public void addProductToBackPack(Product product) {

    }

    public void getProductFromBackPack(Product product) {

    }

    public void deleteProductFromBackPack(Product product, int count) {
        if (!products.containsKey(product)) return;
        products.put(product, products.get(product) - count);
    }

    public boolean checkProductAvailabilityInBackPack(Product product, int count) {
        if (!products.containsKey(product)) return false;
        return products.get(product) >= count;
    }
}
