package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    public Tile(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }


}
