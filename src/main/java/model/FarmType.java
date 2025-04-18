package model;

import lombok.Getter;
import lombok.ToString;
import model.structure.GreenHouse;
import model.structure.Structure;

import java.util.List;

@Getter
@ToString
public enum FarmType {
    GRASS_FARM(), BLUE_FARM(), FLOWER_FARM(), ROCKY_FARM(), DESERT_FARM();
    private List<Tile> tiles;
    private List<Structure> structures;
    private GreenHouse greenHouse;

}
