package model.products.TreesAndFruitsAndSeeds;

import lombok.Getter;
import lombok.Setter;
import model.Salable;
import model.products.Product;

@Getter
@Setter
public class MadeProduct implements Salable {
    private MadeProductType madeProductType;
    private Salable product;
    private Integer price;

    public MadeProduct(MadeProductType madeProductType, Salable product) {
        this.madeProductType = madeProductType;
        this.product = product;
        price = madeProductType.calcPrice((Product) product);
    }

    public int getPrice() {
        return madeProductType.calcPrice((Product) product);
    }

    public int getEnergy() {
        return madeProductType.calcEnergy((Product) product);
    }

    @Override
    public String getName() {
        return this.madeProductType.getName().toLowerCase();
    }

    @Override
    public int getSellPrice() {
        return madeProductType.getSellPrice();
    }
}