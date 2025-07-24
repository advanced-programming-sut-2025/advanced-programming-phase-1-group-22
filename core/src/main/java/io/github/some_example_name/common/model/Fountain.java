package io.github.some_example_name.common.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.some_example_name.common.model.dto.SpriteComponent;
import io.github.some_example_name.common.model.dto.SpriteHolder;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;

public class Fountain extends Structure {
    private final TextureRegion main;
    private final TextureRegion[][] regions1;
    private final TextureRegion[][] regions2;
    @Getter
    private final SpriteComponent spriteComponent;

    public Fountain() {
        main = new TextureRegion(GameAsset.PELICAN_TOWN, 256, 512, 78, 78);
        regions1 = new TextureRegion(GameAsset.PELICAN_TOWN, 208, 512, 48, 48).split(16, 16);
        regions2 = new TextureRegion(GameAsset.PELICAN_TOWN, 272, 592, 96, 48).split(16, 48);
        Sprite mainSprite = new Sprite(main);
        setSize(mainSprite);
        AnimatedSprite leftSprite = new AnimatedSprite(
            new Animation<>(0.2f, regions1[0][0], regions1[0][1], regions1[0][2], regions1[1][0])
        );
        setSize(leftSprite);
        leftSprite.setScale(2.2f / 2.4f);
        AnimatedSprite rightSprite = new AnimatedSprite(
            new Animation<>(0.2f, regions1[1][1], regions1[2][0], regions1[2][1])
        );
        setSize(rightSprite);
        AnimatedSprite middleSprite = new AnimatedSprite(
            new Animation<>(0.2f, regions2[0][0], regions2[0][1], regions2[0][2],
                regions2[0][3], regions2[0][4], regions2[0][5])
        );
        setSize(middleSprite);
        Sprite topSprite = new Sprite(regions1[1][2]);
        setSize(topSprite);
        spriteComponent = new SpriteComponent(
            new SpriteHolder(mainSprite),
            new SpriteHolder(leftSprite, new Tuple<>(15 / 13f, 62 / 13f)),
            new SpriteHolder(rightSprite, new Tuple<>(47 / 13f, 62 / 13f)),
            new SpriteHolder(middleSprite, new Tuple<>(31 / 13f, 30 / 13f)),
            new SpriteHolder(topSprite, new Tuple<>(31 / 13f, 78 / 13f))
        );
    }

    private void setSize(Sprite sprite) {
        sprite.setSize(App.tileWidth * sprite.getWidth() / 13,
            App.tileHeight * sprite.getHeight() / 13);
        if (sprite instanceof AnimatedSprite animatedSprite) {
            animatedSprite.setLooping(true);
        }
    }
}
