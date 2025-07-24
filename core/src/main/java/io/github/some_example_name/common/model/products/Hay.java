package io.github.some_example_name.common.model.products;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;
import io.github.some_example_name.common.model.Salable;
@Getter
public class Hay implements Salable {
    private final Integer price = 50 / 2;
    private final Sprite sprite;
    private final Texture texture;

    public Hay(){
        this.sprite = new Sprite(GameAsset.WHEAT_STAGE_5);
        this.sprite.setSize(App.tileWidth,App.tileHeight);
        this.texture = GameAsset.WHEAT_STAGE_5;
    }

    @Override
    public String getName() {
        return "hay";
    }

    @Override
    public int getSellPrice() {
        return this.price;
    }

    @Override
    public Integer getContainingEnergy() {return 0;}

    @Override
    public Hay copy() {
        return new Hay();
    }
}
