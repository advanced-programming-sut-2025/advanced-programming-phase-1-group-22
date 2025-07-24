package io.github.some_example_name.common.model.structure;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.common.model.AnimatedSprite;
import io.github.some_example_name.common.model.Tile;
import io.github.some_example_name.common.model.Tuple;
import io.github.some_example_name.common.model.dto.SpriteHolder;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Lightening extends Structure {

    private ArrayList<SpriteHolder> sprites = new ArrayList<>();

    public Lightening() {
        TextureRegion[] a = {new TextureRegion(GameAsset.LIGHTENING, 101, 115, 120, 626)
            , new TextureRegion(GameAsset.LIGHTENING, 217, 115, 120, 626)
            , new TextureRegion(GameAsset.LIGHTENING, 353, 115, 140, 626)
            , new TextureRegion(GameAsset.LIGHTENING, 531, 115, 150, 626)
            , new TextureRegion(GameAsset.LIGHTENING, 915, 115, 120, 626)
            , new TextureRegion(GameAsset.LIGHTENING, 1060, 115, 120, 626)
        };
        AnimatedSprite sprite = new AnimatedSprite(new Animation<>(0.1f, a));
        AnimatedSprite sprite2 = new AnimatedSprite(new Animation<>(0.1f, new TextureRegion(GameAsset.YELLOW_SQUARE)));
        sprite2.setAlphaAnimation(new Animation<>(0.06f, 0.1f, 0.2f, 0.3f, 0.3f, 0.4f, 0.4f, 0.4f, 0.3f, 0.3f, 0.2f, 0.1f));
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        sprite2.setSize(camera.viewportWidth*3, camera.viewportHeight*3);
        sprites.add(new SpriteHolder(sprite, new Tuple<>(0f,0f)));
        sprites.add(new SpriteHolder(sprite2, new  Tuple<>(0f,0f)));
    }

    public void setTile(Tile tile) {
        tiles.clear();
        tiles.add(tile);
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        sprites.get(sprites.size() - 1).setOffset(new Tuple<>(
            -tile.getX() + (camera.position.x - 3*camera.viewportWidth/2) / App.tileWidth,
            -tile.getY() + (camera.position.y - 3*camera.viewportHeight/2) / App.tileHeight
        ));
    }
}
