package io.github.some_example_name.client.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.some_example_name.common.model.Farm;
import io.github.some_example_name.common.model.Tile;
import io.github.some_example_name.common.model.TileType;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.common.model.structure.farmInitialElements.Lake;
import io.github.some_example_name.common.utils.App;

public class MiniMapActor extends Actor {
    private final float scale;

    public MiniMapActor(float scale) {
        this.scale = scale;
        setSize(App.getInstance().getCurrentGame().tiles.length * App.tileWidth * scale,
            App.getInstance().getCurrentGame().tiles[0].length * App.tileHeight * scale);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();

        batch.begin();
        drawTiles(batch);
        drawStructures(batch);
    }

    private void drawTiles(Batch batch) {
        Tile[][] tiles = App.getInstance().getCurrentGame().tiles;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                TextureRegion tex = tiles[i][j].getTextureRegion();
                batch.draw(TileType.FLAT.getTexture()[0][0],
                    getX() + i * App.tileWidth * scale,
                    getY() + j * App.tileHeight * scale,
                    App.tileWidth * scale,
                    App.tileHeight * scale);
                if (tex != null) {
                    batch.draw(tex,
                        getX() + i * App.tileWidth * scale,
                        getY() + j * App.tileHeight * scale,
                        App.tileWidth * scale,
                        App.tileHeight * scale);
                }
            }
        }
    }

    private void drawStructures(Batch batch) {
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            farm.applyPendingChanges();
            for (Structure structure : farm.getStructuresSnapshot()) {
                if (structure.getSprite() != null) {
                    if (structure instanceof Lake) {
                        for (Tile tile : structure.getTiles()) {
                            Sprite sprite = new Sprite(structure.getSprite());
                            sprite.setSize(App.tileWidth * scale, App.tileHeight * scale);
                            sprite.setPosition(getX() + tile.getX() * App.tileWidth * scale,
                                getY() + tile.getY() * App.tileHeight * scale);
                            sprite.draw(batch);
                        }
                    } else {
                        Sprite sprite = new Sprite(structure.getSprite());
                        sprite.setSize(structure.getSprite().getWidth() * scale, structure.getSprite().getHeight() * scale);
                        sprite.setPosition(getX() + structure.getTiles().get(0).getX() * App.tileWidth * scale,
                            getY() + structure.getTiles().get(0).getY() * App.tileHeight * scale);
                        sprite.draw(batch);
                    }
                }
            }
        }
        App.getInstance().getCurrentGame().getVillage().applyPendingChanges();
        App.getInstance().getCurrentGame().getVillage().forEachStructure(structure -> {
            if (structure.getSprite() != null) {
                Sprite sprite = new Sprite(structure.getSprite());
                sprite.setSize(structure.getSprite().getWidth() * scale, structure.getSprite().getHeight() * scale);
                sprite.setPosition(getX() + structure.getTiles().get(0).getX() * App.tileWidth * scale,
                    getY() + structure.getTiles().get(0).getY() * App.tileHeight * scale);
                sprite.draw(batch);
            }
        });
    }
}
