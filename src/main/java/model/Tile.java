package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import utils.App;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Tile implements Serializable {
    private Integer id;
    private Integer x;
    private Integer y;
    private Boolean isFilled = false;
    private Boolean isPassable = true;
    private TileType tileType = TileType.FLAT;
    @JsonIgnore
    private App app = App.getInstance();

    public Tile() {
    }

    public Boolean isPassable() {
        if (!isPassable) return false;
        boolean flag = switch (tileType) {
            case MUD, FENCE -> false;
            case FLAT -> true;
            case PATH -> true;
            case SNOW -> true;
            case GRASS -> true;
            case FLOWER -> true;
            case PLOWED -> true;
            case DOOR -> checkDoor();
        };

        return flag;
    }

    private boolean checkDoor() {
        Game currentGame = app.getCurrentGame();
        for (Farm farm : currentGame.getVillage().getFarms()) {
            if (farm.isPairInFarm(new Pair(x, y))) {
                return currentGame.getPlayers().contains(currentGame.getCurrentPlayer());
            }

        }
        return false;
    }

    public Tile(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }
}
