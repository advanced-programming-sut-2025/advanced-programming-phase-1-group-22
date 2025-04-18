package model;

import lombok.Getter;
import lombok.ToString;
import model.structure.Structure;
import model.structure.farmInitialElements.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public enum FarmType {
    GRASS_FARM(50, 40), BLUE_FARM(60, 45), FLOWER_FARM(50, 40),
    ROCKY_FARM(60, 45),
    DESERT_FARM(40, 30);
    private List<Structure> structures = new ArrayList<>();
    private final Integer length;
    private final Integer width;

    FarmType(int length, int width) {
        this.length = length;
        this.width = width;
        switch (this) {
            case GRASS_FARM -> GrassFarmInit();
            case BLUE_FARM -> BlueFarmInit();
            case FLOWER_FARM -> FlowerFarmInit();
            case ROCKY_FARM -> RockyFarmInit();
            case DESERT_FARM -> DesertFarmInit();
        }

    }

    public void GrassFarmInit() {
        Cottage cottage = new Cottage();
        Quarry quarry = new Quarry();
        GreenHouse greenHouse = new GreenHouse();
        Lake lake = new Lake();
        setTilePairs(cottage, new Pair(36, 32));
        setTilePairs(greenHouse, new Pair(10, 22));
        setTilePairs(quarry, new Pair(28, 10));
        for (int i = 10; i < 18; i++) {
            for (int j = 10; j < 20; j++) {
                lake.getTilePairList().add(new Pair(i, j));
            }
        }
        structures.addAll(List.of(cottage, quarry, greenHouse, lake));
    }

    public void setTilePairs(HardCodeFarmElements hardCodeFarmElements, Pair pair) {
        for (int i = pair.getX(); i <= pair.getX() + hardCodeFarmElements.getLength(); i++) {
            for (int j = pair.getY(); j <= pair.getY() + hardCodeFarmElements.getWidth(); j++) {
                hardCodeFarmElements.getTilePairList().add(new Pair(i, j));
            }
        }
    }

    public void BlueFarmInit() {
        Cottage cottage = new Cottage();
        Quarry quarry = new Quarry();
        GreenHouse greenHouse = new GreenHouse();
        Lake lake = new Lake();
        setTilePairs(cottage, new Pair(6, 20));
        setTilePairs(greenHouse, new Pair(45, 3));
        setTilePairs(quarry, new Pair(45, 30));
        for (int i = 4; i < 14; i++) {
            for (int j = 4; j < 24; j++) {
                lake.getTilePairList().add(new Pair(i, j));
            }
        }
        structures.addAll(List.of(cottage, quarry, greenHouse, lake));
    }

    public void FlowerFarmInit() {
        Cottage cottage = new Cottage();
        Quarry quarry = new Quarry();
        GreenHouse greenHouse = new GreenHouse();
        Lake lake = new Lake();
        setTilePairs(cottage, new Pair(2, 4));
        setTilePairs(greenHouse, new Pair(20, 2));
        setTilePairs(quarry, new Pair(2, 14));
        for (int i = 26; i < 37; i++) {
            for (int j = 14; j < 20; j++) {
                lake.getTilePairList().add(new Pair(i, j));
            }
        }
        structures.addAll(List.of(cottage, quarry, greenHouse, lake));
    }

    public void RockyFarmInit() {
        Cottage cottage = new Cottage();
        Quarry quarry = new Quarry();
        GreenHouse greenHouse = new GreenHouse();
        Lake lake = new Lake();
        setTilePairs(cottage, new Pair(30, 20));
        setTilePairs(greenHouse, new Pair(45, 20));
        setTilePairs(quarry, new Pair(4, 3));
        for (int i = 3; i < 7; i++) {
            for (int j = 37; j < 42; j++) {
                lake.getTilePairList().add(new Pair(i, j));
            }
        }
        structures.addAll(List.of(cottage, quarry, greenHouse, lake));
    }

    public void DesertFarmInit() {
        Cottage cottage = new Cottage();
        Quarry quarry = new Quarry();
        GreenHouse greenHouse = new GreenHouse();
        Lake lake = new Lake();
        setTilePairs(cottage, new Pair(22, 10));
        setTilePairs(greenHouse, new Pair(16, 18));
        setTilePairs(quarry, new Pair(2, 16));
        lake.getTilePairList().addAll(List.of(new Pair(16, 15), new Pair(16, 14)));
        structures.addAll(List.of(cottage, quarry, greenHouse, lake));
    }


}
