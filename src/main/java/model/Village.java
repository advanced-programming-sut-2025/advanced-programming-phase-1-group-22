package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.structure.Structure;

import java.util.List;
@Getter
@Setter
@ToString
public class Village {
    private List<Tile> tiles;
    private List<Farm> farms;
    private List<Structure> structures;
}
