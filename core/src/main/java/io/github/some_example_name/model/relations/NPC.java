package io.github.some_example_name.model.relations;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.utils.App;
import lombok.Getter;
import lombok.NoArgsConstructor;
import io.github.some_example_name.model.Actor;
@NoArgsConstructor
@Getter
public class NPC extends Actor {
    private NPCType type;
    private Sprite sprite;

    public NPC(NPCType type) {
        this.type = type;
        this.sprite = new Sprite(type.getTextureCharacter());
        this.sprite.setSize((float)App.tileWidth,(float)(App.tileHeight * 1.5));
    }
}
