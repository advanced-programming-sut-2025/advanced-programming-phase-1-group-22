package io.github.some_example_name.model.structure.farmInitialElements;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;
import lombok.Setter;
import io.github.some_example_name.model.shelter.FarmBuilding;
import io.github.some_example_name.model.shelter.FarmBuildingType;
import io.github.some_example_name.model.structure.Structure;

@Getter
@Setter
public class Cottage extends HardCodeFarmElements {
	private final FarmBuildingType farmBuildingType = FarmBuildingType.Cottage;
    private Sprite sprite;

	public Cottage(HardCodeFarmElements hardCodeFarmElements) {
		super(hardCodeFarmElements);
        this.sprite = new Sprite(GameAsset.HOUSE);
        this.sprite.setSize(App.tileWidth * 6,App.tileHeight * 6);
	}

	public Cottage() {
		super.setLength(6);
		super.setWidth(6);
        this.sprite = new Sprite(GameAsset.HOUSE);
        this.sprite.setSize(App.tileWidth * 6,App.tileHeight * 6);
	}

	@Override
	public HardCodeFarmElements cloneEl() {
		return new Cottage(this);
	}
}
