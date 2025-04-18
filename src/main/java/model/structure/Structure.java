package model.structure;

import lombok.Getter;
import lombok.Setter;
import model.Tile;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public abstract class Structure {
    List<Tile> tiles = new ArrayList<Tile>();

}
