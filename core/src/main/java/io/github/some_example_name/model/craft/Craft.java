package io.github.some_example_name.model.craft;

import com.badlogic.gdx.graphics.Texture;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.Tile;
import io.github.some_example_name.model.TimeAndDate;
import io.github.some_example_name.model.products.TreesAndFruitsAndSeeds.MadeProduct;
import io.github.some_example_name.model.products.TreesAndFruitsAndSeeds.MadeProductType;
import io.github.some_example_name.model.structure.Structure;

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
        return craftType.getTilesAffected(getTiles().get(0));
    }

    @Override
    public Integer getContainingEnergy() {return 0;}

    @Override
    public Texture getTexture() {
        return craftType.getTexture();
    }
}
