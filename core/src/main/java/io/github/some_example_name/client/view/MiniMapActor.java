package io.github.some_example_name.client.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.some_example_name.common.model.Farm;
import io.github.some_example_name.common.model.Tile;
import io.github.some_example_name.common.model.TileType;
import io.github.some_example_name.common.model.animal.Animal;
import io.github.some_example_name.common.model.dto.SpriteComponent;
import io.github.some_example_name.common.model.dto.SpriteHolder;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.common.model.structure.farmInitialElements.Lake;
import io.github.some_example_name.common.utils.App;

import java.util.ArrayList;
import java.util.List;

public class MiniMapActor extends Actor {
    private final float scale;
    private float currentDelta = 0f;

    public MiniMapActor(float scale) {
        this.scale = scale;
        setSize(App.getInstance().getCurrentGame().tiles.length * App.tileWidth * scale,
            App.getInstance().getCurrentGame().tiles[0].length * App.tileHeight * scale);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.currentDelta = delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();

        batch.begin();
        drawTiles(batch);
        drawStructures(batch, currentDelta);
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

    private void drawStructures(Batch batch, float delta) {
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            farm.applyPendingChanges();
            List<Structure> structures = farm.getStructuresSnapshot();
            for (Structure structure : structures.toArray(new Structure[0])) {
                if (structure.getSprite() != null) {
                    if (structure instanceof Lake) {
                        for (Tile tile : structure.getTiles()) {
                            Sprite sprite = new Sprite(structure.getSprite());
                            sprite.setSize(App.tileWidth * scale, App.tileHeight * scale);
                            sprite.setPosition(getX() + tile.getX() * App.tileWidth * scale,
                                getY() + tile.getY() * App.tileHeight * scale);
                            sprite.draw(batch);
                        }
                    } else if (structure instanceof Animal animal) {
                        if (animal.getIsAnimalStayOutAllNight()) {
                            Sprite sprite = new Sprite(structure.getSprite());
                            drawRawSprite(sprite, structure, batch);
                        }
                    } else {
                        Sprite sprite = new Sprite(structure.getSprite());
                        drawRawSprite(sprite, structure, batch);
                    }
                }
                if (structure.getSprites() != null) {
                    for (SpriteHolder sprite : structure.getSprites()) {
                        drawSpriteHolder(sprite, structure, batch);
                    }
                }
                if (structure.getSpriteComponent() != null) {
                    drawSpriteComponent(delta, structure.getSpriteComponent(), structure, batch);
                }
            }
        }
        App.getInstance().getCurrentGame().getVillage().applyPendingChanges();
        for (Structure structure : App.getInstance().getCurrentGame().getVillage().getStructuresSnapshot()) {
            if (structure.getSprite() != null) {
                Sprite sprite = new Sprite(structure.getSprite());
                drawRawSprite(sprite, structure, batch);
            }
            if (structure.getSprites() != null) {
                for (SpriteHolder sprite : structure.getSprites()) {
                    drawSpriteHolder(sprite, structure, batch);
                }
            }
            if (structure.getSpriteComponent() != null) {
                drawSpriteComponent(delta, structure.getSpriteComponent(), structure, batch);
            }
        }
    }

    private void drawRawSprite(Sprite sprite, Structure structure, Batch batch) {
        sprite.setSize(structure.getSprite().getWidth() * scale, structure.getSprite().getHeight() * scale);
        sprite.setPosition(getX() + structure.getTiles().get(0).getX() * App.tileWidth * scale,
            getY() + structure.getTiles().get(0).getY() * App.tileHeight * scale);
        int width = structure.getWidth() == null ? 1 : structure.getWidth();
        int height = structure.getHeight() == null ? 1 : structure.getHeight();
        sprite.setPosition(getX() + (width / 2f + structure.getTiles().get(0).getX()) * App.tileWidth * scale - sprite.getWidth() / 2,
            getY() + (height / 2f + structure.getTiles().get(0).getY()) * App.tileHeight * scale - sprite.getHeight() / 2);
        sprite.draw(batch);
    }

    private void drawSpriteHolder(SpriteHolder sprite, Structure structure, Batch batch) {
        if (structure.getTiles().isEmpty()) return;
        Sprite copy = new Sprite(sprite.getSprite());
        copy.setSize(copy.getWidth() * scale, copy.getHeight() * scale);
        copy.setPosition(
            getX() + (sprite.getOffset().getX() + structure.getTiles().get(0).getX()) * App.tileWidth * scale,
            getY() + (sprite.getOffset().getY() + structure.getTiles().get(0).getY()) * App.tileHeight * scale);
        copy.draw(batch);
    }

    private void drawSpriteComponent(float delta, SpriteComponent sprite, Structure structure, Batch batch) {
        ArrayList<SpriteHolder> spriteHolders = structure.getSpriteComponent().getSprites(delta);
        for (SpriteHolder spriteHolder : spriteHolders) {
            Sprite copy = new Sprite(spriteHolder.getSprite());
            copy.setSize(copy.getWidth() * scale, copy.getHeight() * scale);
            copy.setPosition(
                getX() + (sprite.getOffset().getX() + spriteHolder.getOffset().getX() + structure.getTiles().get(0).getX()) * App.tileWidth * scale,
                getY() + (sprite.getOffset().getY() + spriteHolder.getOffset().getY() + structure.getTiles().get(0).getY()) * App.tileHeight * scale
            );
            copy.draw(batch);
        }
    }
}
