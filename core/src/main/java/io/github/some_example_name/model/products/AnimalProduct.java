package io.github.some_example_name.model.products;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.utils.App;
import lombok.Getter;
import lombok.Setter;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.structure.Structure;

@Getter
@Setter
public class AnimalProduct extends Structure implements Salable {
    private AnimalProductType animalProductType;
    private ProductQuality productQuality = ProductQuality.NORMAL;
    private Texture texture;
    private Sprite sprite;

    public AnimalProduct(AnimalProductType animalProductType) {
        this.animalProductType = animalProductType;
        this.texture = animalProductType.getTexture();
        this.sprite = new Sprite(animalProductType.getTexture());
        this.sprite.setSize(App.tileWidth,App.tileHeight);
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
}
