package model.craft;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.Salable;
import model.Tile;
import model.TimeAndDate;
import model.products.TreesAndFruitsAndSeeds.MadeProduct;
import model.products.TreesAndFruitsAndSeeds.MadeProductType;
import model.structure.Structure;

import java.util.List;

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
        setIsPickable(true);
    }

    public List<Tile> getRange() {
        return craftType.getTilesAffected(getTiles().get(0));
    }

    @Override
    public String getName() {
        return this.craftType.getName();
    }

    @Override
    public int getSellPrice() {
        return craftType.getSellPrice();
    }

    public List<Tile> getAffectedTiles() {
        return craftType.getTilesAffected(getTiles().getFirst());
    }

    @Override
    public Integer getContainingEnergy() {return 0;}
}
