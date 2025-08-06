package io.github.some_example_name.common.model.gameSundry;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.github.some_example_name.common.model.Salable;
@Getter
@Setter
@NoArgsConstructor
public class Sundry implements Salable {
    private SundryType sundryType;
    private Texture texture;
    private Sprite sprite;

    public Sundry(SundryType sundryType) {
        this.sundryType = sundryType;
        init();
    }

    private void init() {
        this.texture = sundryType.getTexture();
        this.sprite = new Sprite(sundryType.getTexture());
    }

    public Texture getTexture() {
        if (texture == null) init();
        return texture;
    }

    public Sprite getSprite() {
        if (sprite == null) init();
        return sprite;
    }

    @Override
    public String getName() {
        return sundryType.getName();
    }

    @Override
    public int getSellPrice() {
        return sundryType.getPrice();
    }

    @Override
    public Integer getContainingEnergy() {return sundryType.getContainingEnergy();}

    @Override
    public Sundry copy() {
        return new Sundry(sundryType);
    }
}
