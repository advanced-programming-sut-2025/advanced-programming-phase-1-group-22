package io.github.some_example_name.model.structure.farmInitialElements;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;
import lombok.Setter;
import io.github.some_example_name.model.shelter.FarmBuildingType;

@Getter
@Setter
public class Lake extends HardCodeFarmElements {
    private final FarmBuildingType farmBuildingType = FarmBuildingType.Lake;
    private final Sprite sprite;

    public Lake(Lake lake) {
        super(lake);
        this.sprite = new Sprite(GameAsset.WATER);
    }

    public Lake() {
        super.setWidth(-1);
        super.setLength(-1);
        this.sprite = new Sprite(GameAsset.WATER);
    }

    @Override
    public HardCodeFarmElements cloneEl() {
        return new Lake(this);
    }
}
