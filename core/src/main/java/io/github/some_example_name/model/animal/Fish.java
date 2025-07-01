package io.github.some_example_name.model.animal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.utils.App;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.enums.Season;
import io.github.some_example_name.model.products.Product;
import io.github.some_example_name.model.products.ProductQuality;
import io.github.some_example_name.model.structure.Structure;

import java.nio.channels.spi.SelectorProvider;

@Getter
@Setter
@ToString
public class Fish extends Structure implements Salable {
	private FishType fishType;
    private Texture texture;
    private Sprite sprite;
	private Integer containingEnergy = 20; //TODO Energy not found
	private ProductQuality productQuality;

	public int getSellPrice() {
		return (int) (this.fishType.getSellPrice() * productQuality.getPriceCoefficient());
	}

	public Fish(FishType fishType) {
		this.fishType = fishType;
        this.texture = fishType.getTexture();
        this.sprite = new Sprite(fishType.getTexture());
        this.sprite.setSize(App.tileWidth,App.tileHeight);
	}
	public Fish(FishType fishType, ProductQuality productQuality) {
		this.fishType = fishType;
		this.productQuality = productQuality;
        this.texture = fishType.getTexture();
        this.sprite = new Sprite(fishType.getTexture());
        this.sprite.setSize(App.tileWidth,App.tileHeight);
	}

	@Override
	public String getName() {
		return this.fishType.getName();
	}
}
