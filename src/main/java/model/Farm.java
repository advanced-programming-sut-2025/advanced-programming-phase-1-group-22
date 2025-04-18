package model;

import lombok.Getter;
import lombok.Setter;
import model.structure.Structure;
import model.structure.farmInitialElements.HardCodeFarmElements;
import utils.App;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
    private Integer farmXStart = xCenter - farmType.getLength() / 2;
    private Integer farmYStart = yCenter - farmType.getWidth() / 2;
    private Integer farmXEnd = xCenter + farmType.getLength() / 2;
    private Integer farmYEnd = yCenter + farmType.getWidth() / 2;
    private Integer farmIndex = 0;

    public void fillFarmType(int i) {
        farmIndex = i;
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
        for (int i = farmXStart; i < farmXEnd; i++) {
            tiles.addAll(Arrays.asList(app.getCurrentGame().tiles[i]).subList(farmYStart, farmYEnd));
        }
    }

    public void getClone() {
        for (HardCodeFarmElements structure : farmType.getStructures()) {
            HardCodeFarmElements cloneEl = structure.cloneEl();

            structures.add(cloneEl);
        }
    }

    public void transmission(HardCodeFarmElements farmElements) {
        for (Pair pair : farmElements.getTilePairList()) {
            tiles.add(app.getCurrentGame().tiles[pair.getX() + farmXStart][pair.getY() + farmYStart]);
        }
    }

    public void setFence() {
        for (int i = farmXStart; i < farmXEnd; i++) {
            app.getCurrentGame().getTiles()[i][farmYStart].setTileType(TileType.FENCE);
            app.getCurrentGame().getTiles()[i][farmYEnd].setTileType(TileType.FENCE);
        }
        for (int i = farmYStart; i < farmYEnd; i++) {
            app.getCurrentGame().getTiles()[farmXStart][i].setTileType(TileType.FENCE);
            app.getCurrentGame().getTiles()[farmXEnd][i].setTileType(TileType.FENCE);
        }
    }

    public void setDoor() {
        Random random = new Random();
        int xRand = random.nextInt(farmYStart, farmYEnd);
        int yRand = random.nextInt(farmXStart, farmXEnd);
        int xConst = 0;
        int yConst = 0;
        switch (farmIndex) {
            case 0 -> {
                xConst = farmXEnd;
                yConst = farmYEnd;
            }
            case 1 -> {
                xConst = farmXStart;
                yConst = farmYEnd;
            }
            case 2 -> {
                xConst = farmXStart;
                yConst = farmYStart;
            }
            case 3 -> {
                xConst = farmXEnd;
                yConst = farmYStart;
            }
        }
        app.getCurrentGame().getTiles()[xRand][yConst].setTileType(TileType.DOOR);
        app.getCurrentGame().getTiles()[xConst][yRand].setTileType(TileType.DOOR);
    }
}
