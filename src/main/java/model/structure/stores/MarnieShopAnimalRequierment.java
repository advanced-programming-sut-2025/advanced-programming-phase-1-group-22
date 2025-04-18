package model.structure.stores;

import lombok.Getter;
import model.Salable;
import model.products.Hay;
import model.tools.MilkPail;
import model.tools.Shear;
@Getter
public enum MarnieShopAnimalRequierment {
    HAY(new Hay(),50,-1),MILK_pAIL(MilkPail.getInstance(),1000,1)
    ,SHEARS(Shear.SHEAR,1000,1);

    private final Salable product;
    private final Integer price;
    private final Integer dailyLimit;

    MarnieShopAnimalRequierment(Salable product, Integer price, Integer dailyLimit) {
        this.product = product;
        this.price = price;
        this.dailyLimit = dailyLimit;
    }
}
