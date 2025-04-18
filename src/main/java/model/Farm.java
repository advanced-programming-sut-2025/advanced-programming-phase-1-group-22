package model;

import lombok.Getter;
import lombok.Setter;
import model.structure.Structure;
import utils.App;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class Farm {

    private List<Tile> tiles;
    private List<Player> players;
    private List<Structure> structures;
    private Integer xCenter;
    private Integer yCenter;
    private FarmType farmType;
    private App app = App.getInstance();

    public void fillFarmType(int i) {
        switch (i) {
            case 0 -> {
                xCenter = 30;
                yCenter = 23;
            }
            case 1 -> {
                xCenter = 129;
                yCenter = 23;
            }
            case 2 -> {
                xCenter = 30;
                yCenter = 96;
            }
            case 3 -> {
                xCenter = 129;
                yCenter = 96;
            }
        }
    }

    public void setFarmTiles() {
        for (int i = xCenter - farmType.getLength() / 2; i < xCenter + farmType.getLength() / 2; i++) {
            tiles.addAll(Arrays.asList(app.getCurrentGame().tiles[i]).subList(yCenter - farmType.getWidth() / 2, yCenter + farmType.getWidth() / 2));
        }
    }
}
