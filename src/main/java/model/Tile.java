package model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import utils.App;

@Getter
@Setter
@ToString
public class Tile {
    private Integer id;
    private Integer x;
    private Integer y;
    private Boolean isFilled = false;
    private Boolean isPassable = true;
    private TileType tileType = TileType.FLAT;

    public Boolean isPassable() {
        if (!isPassable) return false;
        boolean flag = switch (tileType) {
            case MUD, FENCE -> false;
            case FLAT, PATH, GRASS, FLOWER, SNOW, PLOWED, THUNDERED -> true;
			case DOOR -> checkDoor();
        };

        return flag;
    }

    private boolean checkDoor() {
        Game currentGame = App.getInstance().getCurrentGame();
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
