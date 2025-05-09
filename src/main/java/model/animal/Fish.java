package model.animal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.Salable;
import model.enums.Season;
import model.products.Product;
import model.products.ProductQuality;
import model.structure.Structure;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Fish extends Structure implements Salable, Serializable {
	private FishType fishType;
	private Integer containingEnergy = 20; //TODO Energy not found
	private ProductQuality productQuality;

	public Fish() {
	}

	public int getSellPrice() {
		return (int) (this.fishType.getSellPrice() * productQuality.getPriceCoefficient());
	}

	public Fish(FishType fishType) {
		this.fishType = fishType;
	}
	public Fish(FishType fishType, ProductQuality productQuality) {
		this.fishType = fishType;
		this.productQuality = productQuality;
	}

	@Override
	public String getName() {
		return this.fishType.getName();
	}
}