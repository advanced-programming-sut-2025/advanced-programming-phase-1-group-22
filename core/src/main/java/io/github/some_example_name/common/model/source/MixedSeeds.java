package io.github.some_example_name.common.model.source;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;
import lombok.Setter;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.structure.Structure;

@Getter
@Setter
public class MixedSeeds extends Structure implements Salable {
    private MixedSeedsType mixedSeedsType;
    private Sprite sprite;

    public MixedSeeds(MixedSeedsType mixedSeedsType) {
        this.mixedSeedsType = mixedSeedsType;
        this.sprite = new Sprite(GameAsset.MIXED_SEEDS);
        this.sprite.setSize(App.tileWidth/2f,App.tileHeight/2f);
    }

    @Override
    public String getName() {
        return this.mixedSeedsType.getName();
    }

    @Override
    public int getSellPrice() {
        return mixedSeedsType.getSellPrice();
    }

    @Override
    public Integer getContainingEnergy() {return 0;}

    @Override
    public MixedSeeds copy() {
        return new MixedSeeds(mixedSeedsType);
    }
}
