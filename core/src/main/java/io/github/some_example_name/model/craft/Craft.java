package io.github.some_example_name.model.craft;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.model.dto.SpriteHolder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.Tile;
import io.github.some_example_name.model.TimeAndDate;
import io.github.some_example_name.model.products.TreesAndFruitsAndSeeds.MadeProduct;
import io.github.some_example_name.model.products.TreesAndFruitsAndSeeds.MadeProductType;
import io.github.some_example_name.model.structure.Structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@ToString
public class Craft extends Structure implements Salable {
    private Integer id;
    private CraftType craftType;
    private MadeProduct madeProduct;
    private TimeAndDate ETA;
    private Sprite sprite;
    private HashMap<Salable, Integer> ingredients = new HashMap<>();

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

    @Override
    public Sprite getSprite() {
        if (sprite == null) {
            sprite = new Sprite(craftType.getTexture());
        }
        return sprite;
    }

    @Override
    public ArrayList<SpriteHolder> getSprites() {
        if (getETA() == null) return super.getSprites();
        ArrayList<SpriteHolder> sprites = new ArrayList<>();
        return sprites;
    }
}
