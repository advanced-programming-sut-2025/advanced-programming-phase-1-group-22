package io.github.some_example_name.common.model.structure.farmInitialElements;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;
import lombok.Setter;
import io.github.some_example_name.common.model.shelter.FarmBuildingType;

@Getter
@Setter
public class Lake extends HardCodeFarmElements {
    private final FarmBuildingType farmBuildingType = FarmBuildingType.Lake;
    private final Sprite sprite;
    private boolean fishAdded = false;
    private boolean isInGreenHouse = false;
//    private TextureRegion[][] regions = GameAsset.SEA_FLOORING;

    public Lake(Lake lake) {
        super(lake);
        this.sprite = new Sprite(GameAsset.WATER);
    }

    public Lake() {
        super.setHeight(-1);
        super.setWidth(-1);
        this.sprite = new Sprite(GameAsset.WATER);
    }

    @Override
    public HardCodeFarmElements copyEl() {
        return new Lake(this);
    }
}
