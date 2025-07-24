package io.github.some_example_name.common.model.source;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.common.utils.App;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.structure.Structure;

@Getter
@Setter
@NoArgsConstructor
public class Seed extends Structure implements Salable {
    private SeedType seedType;
    private Texture texture;
    private Sprite sprite;

    public Seed(SeedType seedType) {
        this.seedType = seedType;
        this.texture = seedType.getTexture();
        this.sprite = new Sprite(seedType.getTexture());
        this.sprite.setSize(App.tileWidth/2f,App.tileHeight/2f);
    }

    @Override
    public String getName() {
        return this.seedType.getName();
    }

    @Override
    public int getSellPrice() {
        return seedType.getSellPrice();
    }

    @Override
    public Integer getContainingEnergy() {return seedType.getContainingEnergy();}

    @Override
    public Seed copy() {
        return new Seed(seedType);
    }
}
