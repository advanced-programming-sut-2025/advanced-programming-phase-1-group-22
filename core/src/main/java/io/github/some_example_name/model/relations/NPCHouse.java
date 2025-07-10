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
    private Integer width = 4;
    private Integer height = 6;

    public NPCHouse(NPC owner) {
        this.owner = owner;
        this.sprite = new Sprite(owner.getType().getTextureHouse());
        this.sprite.setSize(width * App.tileWidth,height * App.tileHeight);
    }
}
