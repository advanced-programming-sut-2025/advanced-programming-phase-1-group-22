package model.enums;

import lombok.Getter;
import model.Game;
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
    SUNNY(List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER)),
    RAINY(List.of(Season.SPRING, Season.SUMMER, Season.FALL)),
    STORMY(List.of(Season.SPRING, Season.SUMMER, Season.FALL)),
    SNOWY(List.of(Season.WINTER));
    private final List<Season> seasons;

    Weather(List<Season> seasons) {

        this.seasons = seasons;
    }

    public void weatherEffect() {

    }

    public void thunderBolt(int x, int y) {
        Game game = App.getInstance().getCurrentGame();
        ArrayList<Structure> structures = game.getVillage().findStructuresByTile(game.tiles[x][y]);
        for (Structure structure : structures) {
            if (structure instanceof Tree) {
                ((Tree)structure).burn();
            }
            if (structure instanceof Trunk) {
                ((Trunk)structure).burn();
            }
            if (structure instanceof Seed) {
                ((Seed)structure).burn();
            }
            if (structure instanceof Crop) {
                ((Crop)structure).burn();
            }
        }
    }
}