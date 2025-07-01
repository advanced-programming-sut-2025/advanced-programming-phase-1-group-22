package io.github.some_example_name.model.source;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.utils.App;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.structure.Structure;

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
    public void burn() {
        //TODO
    }

    @Override
    public Integer getContainingEnergy() {return seedType.getContainingEnergy();}
}
