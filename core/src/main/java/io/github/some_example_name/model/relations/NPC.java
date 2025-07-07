package io.github.some_example_name.model.relations;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;
import lombok.NoArgsConstructor;
import io.github.some_example_name.model.Actor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class NPC extends Actor {
    private NPCType type;
    private Sprite sprite;
    private boolean haveDialog = false;
    private Sprite spriteDialogBox;
    private boolean giftedToday = false;

    public NPC(NPCType type) {
        this.type = type;
        this.sprite = new Sprite(type.getTextureCharacter());
        this.sprite.setSize((float) App.tileWidth, (float) (App.tileHeight * 1.5));
        this.spriteDialogBox = new Sprite(GameAsset.DIALOG_BOX);
        this.spriteDialogBox.setSize(App.tileWidth / 2f, App.tileHeight / 2f);
    }
}
