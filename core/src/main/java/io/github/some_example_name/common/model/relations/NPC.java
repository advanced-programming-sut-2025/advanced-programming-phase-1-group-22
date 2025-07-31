package io.github.some_example_name.common.model.relations;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;
import lombok.NoArgsConstructor;
import io.github.some_example_name.common.model.Actor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class NPC extends Actor {
    private NPCType type;
    private transient Sprite sprite;
    private boolean haveDialog = false;
    private transient Sprite spriteDialogBox;
    private boolean giftedToday = false;

    public NPC(NPCType type) {
        this.type = type;
    }

    private void init() {
        this.sprite = new Sprite(type.getTextureCharacter());
        this.sprite.setSize((float) App.tileWidth, (float) (App.tileHeight * 1.5));
        this.spriteDialogBox = new Sprite(GameAsset.DIALOG_BOX);
        this.spriteDialogBox.setSize(App.tileWidth / 2f, App.tileHeight / 2f);
    }

    public Sprite getSprite() {
        if (sprite == null) init();
        return sprite;
    }

    public Sprite getSpriteDialogBox() {
        if (spriteDialogBox == null) init();
        return spriteDialogBox;
    }

    @Override
    public String getName() {
        return type.getName();
    }

    @Override
    public String getNickname() {
        return getName();
    }


}
