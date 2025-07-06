package io.github.some_example_name.model.relations;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.model.structure.Structure;
import io.github.some_example_name.utils.App;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NPCHouse extends Structure {
    private NPC owner;
    private Sprite sprite;

    public NPCHouse(NPC owner) {
        this.owner = owner;
        this.sprite = new Sprite(owner.getType().getTextureHouse());
        this.sprite.setSize(4 * App.tileWidth,6 * App.tileHeight);
    }
}
