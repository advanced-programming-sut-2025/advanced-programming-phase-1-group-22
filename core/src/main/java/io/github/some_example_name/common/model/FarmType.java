package io.github.some_example_name.common.model;

import io.github.some_example_name.common.model.shelter.ShippingBin;
import io.github.some_example_name.common.model.structure.farmInitialElements.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum FarmType {
    GRASS_FARM("Grass Farm", 50, 40),
    BLUE_FARM("Blue Farm", 60, 45),
    FLOWER_FARM("Flower Farm", 50, 40),
    ROCKY_FARM("Rocky Farm", 60, 45),
    DESERT_FARM("Desert Farm", 40, 30);
    private List<HardCodeFarmElements> structures = new ArrayList<>();
    private final Integer length;
    private final String name;
    private final Integer width;

    FarmType(String name, int length, int width) {
        this.name = name;
        this.length = length;
        this.width = width;
    }

    public void initial() {
        if (structures.isEmpty()) {
            switch (this) {
                case GRASS_FARM -> GrassFarmInit();
                case BLUE_FARM -> BlueFarmInit();
                case FLOWER_FARM -> FlowerFarmInit();
                case ROCKY_FARM -> RockyFarmInit();
                case DESERT_FARM -> DesertFarmInit();
            }
        }
    }

    public void GrassFarmInit() {
        Cottage cottage = new Cottage();
        ShippingBin shippingBin = new ShippingBin();
        Quarry quarry = new Quarry();
        GreenHouse greenHouse = new GreenHouse();
        Lake lake = new Lake();
        setTilePairs(cottage, new Pair(36, 32));
        setTilePairs(shippingBin, new Pair(44, 34));
        setTilePairs(greenHouse, new Pair(10, 22));
        setTilePairs(quarry, new Pair(28, 10));
        for (int i = 10; i < 18; i++) {
            for (int j = 10; j < 20; j++) {
                lake.getTilePairList().add(new Pair(i, j));
            }
        }
        structures.addAll(List.of(cottage, shippingBin, quarry, greenHouse, lake));
    }

    public void setTilePairs(HardCodeFarmElements hardCodeFarmElements, Pair pair) {
        for (int i = pair.getX(); i <= pair.getX() + hardCodeFarmElements.getWidth(); i++) {
            for (int j = pair.getY(); j <= pair.getY() + hardCodeFarmElements.getHeight(); j++) {
                hardCodeFarmElements.getTilePairList().add(new Pair(i, j));
            }
        }
    }

    public void BlueFarmInit() {
        Cottage cottage = new Cottage();
        ShippingBin shippingBin = new ShippingBin();
        Quarry quarry = new Quarry();
        GreenHouse greenHouse = new GreenHouse();
        Lake lake = new Lake();
        setTilePairs(cottage, new Pair(6, 27));
        setTilePairs(shippingBin, new Pair(14, 29));
        setTilePairs(greenHouse, new Pair(45, 3));
        setTilePairs(quarry, new Pair(45, 30));
        for (int i = 4; i < 14; i++) {
            for (int j = 4; j < 24; j++) {
                lake.getTilePairList().add(new Pair(i, j));
            }
        }
        structures.addAll(List.of(cottage, shippingBin, quarry, greenHouse, lake));
    }

    public void FlowerFarmInit() {
        Cottage cottage = new Cottage();
        ShippingBin shippingBin = new ShippingBin();
        Quarry quarry = new Quarry();
        GreenHouse greenHouse = new GreenHouse();
        Lake lake = new Lake();
        setTilePairs(cottage, new Pair(2, 4));
        setTilePairs(shippingBin, new Pair(10, 6));
        setTilePairs(greenHouse, new Pair(20, 2));
        setTilePairs(quarry, new Pair(2, 14));
        for (int i = 26; i < 37; i++) {
            for (int j = 14; j < 20; j++) {
                lake.getTilePairList().add(new Pair(i, j));
            }
        }
        structures.addAll(List.of(cottage, shippingBin, quarry, greenHouse, lake));
    }

    public void RockyFarmInit() {
        Cottage cottage = new Cottage();
        ShippingBin shippingBin = new ShippingBin();
        Quarry quarry = new Quarry();
        GreenHouse greenHouse = new GreenHouse();
        Lake lake = new Lake();
        setTilePairs(cottage, new Pair(30, 20));
        setTilePairs(shippingBin, new Pair(38, 22));
        setTilePairs(greenHouse, new Pair(45, 20));
        setTilePairs(quarry, new Pair(4, 3));
        for (int i = 3; i < 7; i++) {
            for (int j = 37; j < 42; j++) {
                lake.getTilePairList().add(new Pair(i, j));
            }
        }
        structures.addAll(List.of(cottage, shippingBin, quarry, greenHouse, lake));
    }

    public void DesertFarmInit() {
        Cottage cottage = new Cottage();
        ShippingBin shippingBin = new ShippingBin();
        Quarry quarry = new Quarry();
        GreenHouse greenHouse = new GreenHouse();
        Lake lake = new Lake();
        setTilePairs(cottage, new Pair(22, 10));
        setTilePairs(shippingBin, new Pair(30, 12));
        setTilePairs(greenHouse, new Pair(16, 18));
        setTilePairs(quarry, new Pair(2, 16));
        lake.getTilePairList().addAll(List.of(new Pair(16, 15), new Pair(16, 14)));
        structures.addAll(List.of(cottage, shippingBin, quarry, greenHouse, lake));
    }

    @Override
    public String toString() {
        return name;
    }
}
