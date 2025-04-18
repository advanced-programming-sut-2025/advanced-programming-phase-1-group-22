package model.tools;

import lombok.Getter;
import lombok.Setter;
import model.products.Product;

import java.util.Map;
@Getter
@Setter
public class BackPack {
    private BackPackType backPackType;
    private Map<Product, Integer> products;

    public void addProductToBackPack(Product product) {

    }

    public void getProductFromBackPack(Product product) {

    }

    public void deleteProductFromBackPack(Product product) {

    }

    public boolean checkProductAvailabilityInBackPack(Product product) {


        return false;
    }
}
