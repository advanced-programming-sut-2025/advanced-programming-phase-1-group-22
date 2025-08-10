package io.github.some_example_name.common.model.shelter;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.common.utils.App;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.github.some_example_name.common.model.animal.Animal;
import io.github.some_example_name.common.model.structure.Structure;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FarmBuilding extends Structure {
	private FarmBuildingType farmBuildingType;
	private List<Animal> animals = new ArrayList<>();
    private transient Sprite sprite;
    private transient Sprite spriteInterior;

	public FarmBuilding(FarmBuildingType farmBuildingType) {
		this.farmBuildingType = farmBuildingType;
	}

    private void init() {
        if (farmBuildingType.getSprite() != null){
            this.sprite = farmBuildingType.getSprite();
            this.sprite.setSize(App.tileWidth * farmBuildingType.getWidth(),App.tileHeight * farmBuildingType.getHeight());
        }
        if (farmBuildingType.getSpriteInterior() != null){
            this.spriteInterior = farmBuildingType.getSpriteInterior();
            this.spriteInterior.setSize(App.tileWidth * farmBuildingType.getWidth(),App.tileHeight * farmBuildingType.getHeight());
        }
    }

    public Sprite getSprite() {
        if (sprite == null) init();
        return sprite;
    }

    public Sprite getSpriteInterior() {
        if (spriteInterior == null) init();
        return spriteInterior;
    }

    @Override
    public Integer getWidth() {
        return farmBuildingType.getWidth();
    }

    @Override
    public Integer getHeight() {
        return farmBuildingType.getHeight();
    }

    public Boolean canAddNewAnimal(){
		return farmBuildingType.getCapacity() >= animals.size() + 1;
	}
}
