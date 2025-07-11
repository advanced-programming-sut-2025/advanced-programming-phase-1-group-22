package io.github.some_example_name.model.structure.farmInitialElements;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;
import lombok.Setter;
import io.github.some_example_name.model.shelter.FarmBuildingType;

@Getter
@Setter
public class Lake extends HardCodeFarmElements {
    private final FarmBuildingType farmBuildingType = FarmBuildingType.Lake;
    private final Sprite sprite;
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
