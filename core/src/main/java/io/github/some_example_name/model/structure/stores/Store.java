package io.github.some_example_name.model.structure.stores;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.model.Farm;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.view.Menu;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import io.github.some_example_name.model.Pair;
import io.github.some_example_name.model.Tile;
import io.github.some_example_name.model.source.Source;
import io.github.some_example_name.model.structure.Structure;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Store extends Structure{
   private StoreType storeType;
   private Sprite sprite;
   private Integer width;
   private Integer height;
   private Sprite insideSprite;

   public Store(StoreType storeType, int width, int height) {
      this.storeType = storeType;
      this.sprite = new Sprite(storeType.getTexture());
      this.insideSprite = new Sprite(storeType.getTextureInterior());
      this.insideSprite.setSize(width * App.tileWidth,height * App.tileHeight);
      this.sprite.setSize(width * App.tileWidth,height * App.tileHeight);
      this.width = width;
      this.height = height;
   }

    public Sprite getSprite() {
        if (App.getInstance().getCurrentGame().getCurrentPlayer().getCurrentMenu() == Menu.STORE_MENU) {
            return insideSprite;
        } else {
            return sprite;
        }
    }
}
