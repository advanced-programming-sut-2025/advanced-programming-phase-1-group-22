package io.github.some_example_name.common.model.enums;

import com.badlogic.gdx.utils.Timer;
import io.github.some_example_name.common.model.structure.Lightening;
import lombok.Getter;
import io.github.some_example_name.common.model.Game;
import io.github.some_example_name.common.model.TileType;
import io.github.some_example_name.common.model.products.TreesAndFruitsAndSeeds.Tree;
import io.github.some_example_name.common.model.source.Crop;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.common.model.structure.Trunk;
import io.github.some_example_name.common.utils.App;

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
        Lightening lightening = new Lightening();
        lightening.setTile(game.tiles[x][y]);
        game.getVillage().addStructure(lightening);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                game.getVillage().removeStructureFromList(lightening);
                game.tiles[x][y].setTileType(TileType.THUNDERED);
                ArrayList<Structure> structures = game.getVillage().findStructuresByTile(game.tiles[x][y]);
                for (Structure structure : structures) {
                    if (structure instanceof Tree && !((Tree)structure).getInGreenHouse()) {
                        ((Tree)structure).burn();
                        breakTree(x, y);
                    }
                    if (structure instanceof Trunk) {
                        ((Trunk)structure).burn();
                    }
                    if (structure instanceof Crop && !((Crop)structure).getInGreenHouse()) {
                        ((Crop)structure).burn();
                    }
                }
            }
        }, 0.7f);
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
