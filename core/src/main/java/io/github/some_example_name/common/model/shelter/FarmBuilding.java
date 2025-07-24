package io.github.some_example_name.common.model.shelter;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.common.utils.App;
import lombok.Getter;
import lombok.Setter;
import io.github.some_example_name.common.model.animal.Animal;
import io.github.some_example_name.common.model.structure.Structure;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FarmBuilding extends Structure {
	private FarmBuildingType farmBuildingType;
	private List<Animal> animals = new ArrayList<>();
    private Sprite sprite;
    private Sprite spriteInterior;

	public FarmBuilding(FarmBuildingType farmBuildingType) {
		this.farmBuildingType = farmBuildingType;
        if (farmBuildingType.getSprite() != null){
            this.sprite = farmBuildingType.getSprite();
            this.sprite.setSize(App.tileWidth * farmBuildingType.getWidth(),App.tileHeight * farmBuildingType.getHeight());
        }
        if (farmBuildingType.getSpriteInterior() != null){
            this.spriteInterior = farmBuildingType.getSpriteInterior();
            this.spriteInterior.setSize(App.tileWidth * farmBuildingType.getWidth(),App.tileHeight * farmBuildingType.getHeight());
        }
	}

	public Boolean canAddNewAnimal(){
		return farmBuildingType.getCapacity() >= animals.size() + 1;
	}
}
