package model.craft;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.Salable;
import model.TimeAndDate;
import model.products.TreesAndFruitsAndSeeds.MadeProduct;
import model.products.TreesAndFruitsAndSeeds.MadeProductType;
import model.structure.Structure;

@Getter
@Setter
@ToString
public class Craft extends Structure implements Salable {
    private Integer id;
    private CraftType craftType;
    private MadeProduct madeProduct;
    private TimeAndDate ETA;

    public Craft(CraftType craftType, MadeProduct madeProduct, TimeAndDate ETA) {
        this.craftType = craftType;
        this.madeProduct = madeProduct;
        this.ETA = ETA;
    }

    @Override
    public String getName() {
        return this.craftType.getName();
    }

    @Override
    public int getSellPrice() {
        return craftType.getSellPrice();
    }
}
