package model.products.TreesAndFruitsAndSeeds;

import lombok.Getter;
import lombok.Setter;
import model.Salable;
import model.products.Product;
@Getter
@Setter
public class MadeProduct implements Salable {
    private MadeProductType madeProducts;
    private Integer price;
}
