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
public class Mineral extends Structure implements Salable {
    private MineralType foragingMineralType;
    private Texture texture;
    private Sprite sprite;

    public Mineral(MineralType foragingMineralType) {
        this.foragingMineralType = foragingMineralType;
        this.texture = foragingMineralType.getTexture();
        this.sprite = new Sprite(foragingMineralType.getTexture());
        this.sprite.setSize(App.tileWidth/2f,App.tileHeight/2f);
    }

    @Override
    public String getName() {
        return this.foragingMineralType.getName();
    }

    @Override
    public int getSellPrice() {
        return foragingMineralType.getSellPrice();
    }

    @Override
    public Integer getContainingEnergy() {return 0;}

    @Override
    public Mineral copy() {
        return new Mineral(foragingMineralType);
    }
}
