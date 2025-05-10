package model.enums;

import lombok.Getter;
import model.Game;
import model.TileType;
import model.products.TreesAndFruitsAndSeeds.Tree;
import model.source.Crop;
import model.source.Seed;
import model.structure.Structure;
import model.structure.Trunk;
import utils.App;

import java.util.ArrayList;
import java.util.List;

@Getter

public enum Weather {
    SUNNY(List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER),1.5),
    RAINY(List.of(Season.SPRING, Season.SUMMER, Season.FALL),1.2),
    STORMY(List.of(Season.SPRING, Season.SUMMER, Season.FALL),0.5),
    SNOWY(List.of(Season.WINTER),1.0);
    private final List<Season> seasons;
    private final Double fishingCoefficient;

    Weather(List<Season> seasons,Double fishingCoefficient) {
        this.fishingCoefficient = fishingCoefficient;
        this.seasons = seasons;
    }

    public void thunderBolt(int x, int y) {
        Game game = App.getInstance().getCurrentGame();
        game.tiles[x][y].setTileType(TileType.THUNDERED);
        ArrayList<Structure> structures = game.getVillage().findStructuresByTile(game.tiles[x][y]);
        for (Structure structure : structures) {
            if (structure instanceof Tree && !((Tree)structure).getInGreenHouse()) {
                ((Tree)structure).burn();
            }
            if (structure instanceof Trunk) {
                ((Trunk)structure).burn();
            }
            if (structure instanceof Crop && !((Crop)structure).getInGreenHouse()) {
                ((Crop)structure).burn();
            }
        }
    }

    public void breakTree(int x, int y){
        Game game = App.getInstance().getCurrentGame();
        ArrayList<Structure> structures = game.getVillage().findStructuresByTile(game.tiles[x][y]);
        for (Structure structure : structures) {
            if (structure instanceof Tree && !((Tree)structure).getInGreenHouse()) {
                ((Tree)structure).breakTree();
            }
        }
    }

    @Override
    public String toString() {
        return  name().toLowerCase();
    }
}