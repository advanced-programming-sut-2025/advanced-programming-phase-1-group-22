package io.github.some_example_name.common.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import io.github.some_example_name.common.utils.App;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tile {
    private Integer id;
    private Integer x;
    private Integer y;
    private Boolean isFilled = false;
    @JsonProperty("passable")
    private Boolean isPassable = true;
    private TileType tileType = TileType.FLAT;

    public Boolean isPassable() {
        if (!isPassable) return false;
        boolean flag = switch (tileType) {
            case FENCE -> false;
            case FLAT, PATH, GRASS, FLOWER, SNOW, PLOWED, THUNDERED, FERTILIZED -> true;
            case DOOR -> checkDoor();
        };

        return flag;
    }

    private boolean checkDoor() {
        Game currentGame = App.getInstance().getCurrentGame();
        for (Farm farm : currentGame.getVillage().getFarms()) {
            if (farm.isPairInFarm(new Pair(x, y))) {
                return farm.getPlayers().contains(currentGame.getCurrentPlayer());
            }

        }
        return false;
    }

    public Tile(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public TextureRegion getTextureRegion() {
        Game currentGame = App.getInstance().getCurrentGame();
        Pair pair = new Pair();
        switch (tileType) {
            case PLOWED: {
                boolean f0 = TileType.PLOWED != currentGame.getTiles()[this.getX()][this.getY() + 1].getTileType();
                boolean f1 = TileType.PLOWED != currentGame.getTiles()[this.getX() + 1][this.getY()].getTileType();
                boolean f2 = TileType.PLOWED != currentGame.getTiles()[this.getX()][this.getY() - 1].getTileType();
                boolean f3 = TileType.PLOWED != currentGame.getTiles()[this.getX() - 1][this.getY()].getTileType();
                TileType.mapBooleansToPair(f0, f1, f2, f3, pair);
            }
            case GRASS: {
                boolean f0 = TileType.GRASS != currentGame.getTiles()[this.getX()][this.getY() + 1].getTileType();
                boolean f1 = TileType.GRASS != currentGame.getTiles()[this.getX() + 1][this.getY()].getTileType();
                boolean f2 = TileType.GRASS != currentGame.getTiles()[this.getX()][this.getY() - 1].getTileType();
                boolean f3 = TileType.GRASS != currentGame.getTiles()[this.getX() - 1][this.getY()].getTileType();
                TileType.mapBooleansToPair(f0, f1, f2, f3, pair);
                return tileType.getTexture()[pair.getX()][pair.getY()];
            }
            default:
                return tileType.getTexture()[0][0];
        }
    }

    public void updateTileState(Tile tile) {
        x = tile.x;
        y = tile.y;
        isFilled = tile.isFilled;
        isPassable = tile.isPassable;
        tileType = tile.tileType;
    }
}
