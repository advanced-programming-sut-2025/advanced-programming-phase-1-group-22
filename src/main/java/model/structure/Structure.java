package model.structure;

import lombok.Getter;
import lombok.Setter;
import model.Tile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public abstract class Structure implements Serializable {
    List<Tile> tiles = new ArrayList<>();
    Boolean isPickable;
}
