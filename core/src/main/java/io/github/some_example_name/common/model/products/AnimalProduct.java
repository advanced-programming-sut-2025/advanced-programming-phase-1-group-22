package io.github.some_example_name.common.model.products;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.common.utils.App;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.structure.Structure;

@Getter
@Setter
@NoArgsConstructor
public class AnimalProduct extends Structure implements Salable {
    private AnimalProductType animalProductType;
    private ProductQuality productQuality = ProductQuality.NORMAL;
    private transient Texture texture;
    private transient Sprite sprite;

    public AnimalProduct(AnimalProductType animalProductType) {
        this.animalProductType = animalProductType;
    }

    @Override
    public String getName() {
        return this.animalProductType.getName();
    }

    @Override
    public int getSellPrice() {
        return (int) (animalProductType.getSellPrice() * productQuality.getPriceCoefficient());
    }

    @Override
    public Integer getContainingEnergy() {return animalProductType.getEnergy();}

    @Override
    public AnimalProduct copy() {
        return new AnimalProduct(this.animalProductType);
    }

    public Texture getTexture() {
        if (texture == null) init();
        return texture;
    }

    public Sprite getSprite() {
        if (sprite == null) init();
        return sprite;
    }

    private void init() {
        this.texture = animalProductType.getTexture();
        this.sprite = new Sprite(animalProductType.getTexture());
        this.sprite.setSize(App.tileWidth,App.tileHeight);
    }
}
