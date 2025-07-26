package io.github.some_example_name.common.model.craft;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.Tile;
import io.github.some_example_name.common.model.TimeAndDate;
import io.github.some_example_name.common.model.Tuple;
import io.github.some_example_name.common.model.dto.SpriteHolder;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import io.github.some_example_name.common.model.products.TreesAndFruitsAndSeeds.MadeProduct;
import io.github.some_example_name.common.model.structure.Structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class Craft extends Structure implements Salable {
    private Integer id;
    private CraftType craftType;
    @Setter
    private MadeProduct madeProduct;
    private TimeAndDate ETA;
    private TimeAndDate startTime;
    private transient SpriteHolder mainSprite;
    private transient SpriteHolder processContainer;
    private transient SpriteHolder processed;
    private transient HashMap<Salable, Integer> ingredients = new HashMap<>();

    public Craft(CraftType craftType, MadeProduct madeProduct, TimeAndDate ETA) {
        this.craftType = craftType;
        this.madeProduct = madeProduct;
        this.ETA = ETA;
        if (ETA == null) {
            startTime = null;
        } else {
            this.startTime = App.getInstance().getCurrentGame().getTimeAndDate().copy();
        }
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
    public ArrayList<SpriteHolder> getSprites() {
        ArrayList<SpriteHolder> sprites = new ArrayList<>();
        if (mainSprite == null || processed != null && getETA() == null) {
            processed = null;
            mainSprite = new SpriteHolder(new Sprite(craftType.getTexture()), null);
            mainSprite.setOffset(new Tuple<>(
                0.5f - mainSprite.getSprite().getWidth() / 2f / App.tileWidth,
                0f
            ));
        }
        sprites.add(mainSprite);

        if (getETA() == null) return sprites;
        if (getETA().compareTime(App.getInstance().getCurrentGame().getTimeAndDate()) < 0) {
            if (processContainer == null) {
                mainSprite.setSprite(new Sprite(craftType.getTexture1()));
                processContainer = new SpriteHolder(new Sprite(GameAsset.BUTTON), new Tuple<>(0.1f, 1.0f));
                processContainer.getSprite().setSize(App.tileWidth * 0.8f, App.tileHeight * 0.2f);
            }
            if (processed == null) {
                processed = new SpriteHolder(
                    new Sprite(GameAsset.GREEN_SQUARE),
                    new Tuple<>(
                        processContainer.getOffset().getX() + processContainer.getSprite().getWidth() * 0.1f / App.tileWidth,
                        processContainer.getOffset().getY() + processContainer.getSprite().getHeight() * 0.1f / App.tileHeight
                    )
                );
            }
            processed.getSprite().setSize(
                processContainer.getSprite().getWidth() * 0.8f / startTime.difference(getETA())
                    * startTime.difference(App.getInstance().getCurrentGame().getTimeAndDate()),
                processContainer.getSprite().getHeight() * 0.8f
            );
            sprites.add(processContainer);
            sprites.add(processed);
        } else {
            if (processContainer != null) {
                mainSprite.setSprite(new Sprite(craftType.getTexture2()));
                processContainer = null;
                processed = new SpriteHolder(new Sprite(GameAsset.NOTIFICATION),
                    new Tuple<>(0.25f, 1.1f));
                processed.getSprite().setSize(App.tileWidth * 0.5f, App.tileHeight * 0.5f);
            }
            sprites.add(processed);
        }
        return sprites;
    }

    public void setETA(TimeAndDate ETA) {
        this.ETA = ETA;
        this.startTime = App.getInstance().getCurrentGame().getTimeAndDate().copy();
    }

    @Override
    public Craft copy() {
        return new Craft(craftType, madeProduct, ETA);
    }
}
