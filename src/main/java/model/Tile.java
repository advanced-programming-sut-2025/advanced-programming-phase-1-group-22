package model;

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
    private TileType tileType = TileType.FLAT;
    private App app = App.getInstance();

    public Boolean isPassible() {
        boolean flag = switch (tileType) {
            case MUD, FENCE -> false;
            case FLAT -> true;
            case PATH -> true;
            case SNOW -> true;
            case GRASS -> true;
            case FLOWER -> true;
            case DOOR -> checkDoor();
        };

        return flag;
    }

    private boolean checkDoor() {
        Game currentGame = app.getCurrentGame();
        for (Farm farm : currentGame.getVillage().getFarms()) {
            if (Math.abs(farm.getXCenter() - x) <= (farm.getFarmType().getLength() / 2) &&
                    Math.abs(farm.getYCenter() - y) <= (farm.getFarmType().getWidth() / 2)) {
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
