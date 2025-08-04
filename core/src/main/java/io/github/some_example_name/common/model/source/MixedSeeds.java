package io.github.some_example_name.common.model.source;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.structure.Structure;

@Getter
@Setter
@NoArgsConstructor
public class MixedSeeds extends Structure implements Salable {
    private MixedSeedsType mixedSeedsType;
    private transient Sprite sprite;
    private transient Texture texture;

    public MixedSeeds(MixedSeedsType mixedSeedsType) {
        this.mixedSeedsType = mixedSeedsType;
        this.texture = GameAsset.MIXED_SEEDS;
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
    public Integer getContainingEnergy() {
        return 0;
    }

    @Override
    public MixedSeeds copy() {
        return new MixedSeeds(mixedSeedsType);
    }

    public Sprite getSprite() {
        if (sprite == null) {
            this.sprite = new Sprite(GameAsset.MIXED_SEEDS);
            this.sprite.setSize(App.tileWidth / 2f, App.tileHeight / 2f);
        }
        return sprite;
    }
}
