package model;

import lombok.Getter;
import lombok.Setter;
import model.structure.GreenHouse;
import model.structure.Structure;

import java.util.List;
@Getter
@Setter
public class Farm {
    private List<Player> players;
    private List<Structure> structures;
    private GreenHouse greenHouse;

    public boolean createGreenHounse(Tile... tile) {


        return false;
    }
}
