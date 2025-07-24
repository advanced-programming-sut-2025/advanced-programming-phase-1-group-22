package io.github.some_example_name.common.model.products.TreesAndFruitsAndSeeds;

import com.badlogic.gdx.graphics.Texture;
import lombok.Getter;
import lombok.Setter;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.products.Product;

@Getter
@Setter
public class MadeProduct implements Salable {
    private MadeProductType madeProductType;
    private Salable product;
    private Integer price;

    public MadeProduct(MadeProductType madeProductType, Salable product) {
        this.madeProductType = madeProductType;
        this.product = product;
        price = madeProductType.calcPrice(product);
    }

    public MadeProduct(MadeProductType value) {
        this.madeProductType = value;
        //TODO adding default product
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


    @Override
    public Integer getContainingEnergy() {return getEnergy();}

    @Override
    public Texture getTexture() {
        return madeProductType.getTexture();
    }

    @Override
    public MadeProduct copy() {
        return new MadeProduct(madeProductType, product);
    }
}
