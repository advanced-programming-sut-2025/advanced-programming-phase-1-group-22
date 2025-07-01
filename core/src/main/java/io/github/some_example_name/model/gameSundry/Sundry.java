package io.github.some_example_name.model.gameSundry;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.github.some_example_name.model.Salable;
@Getter
@Setter
@NoArgsConstructor
public class Sundry implements Salable {
    private SundryType sundryType;
    private Texture texture;
    private Sprite sprite;

    public Sundry(SundryType sundryType) {
        this.sundryType = sundryType;
        this.texture = sundryType.getTexture();
        this.sprite = new Sprite(sundryType.getTexture());
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
}
