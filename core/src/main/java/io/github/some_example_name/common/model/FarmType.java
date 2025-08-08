package io.github.some_example_name.common.model;

import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.common.model.shelter.ShippingBin;
import io.github.some_example_name.common.model.structure.farmInitialElements.*;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum FarmType {
    GRASS_FARM("Grass Farm", 50, 40, GameAsset.GRASS_FARM),
    BLUE_FARM("Blue Farm", 60, 45, GameAsset.BLUE_FARM),
    FLOWER_FARM("Flower Farm", 50, 40, GameAsset.FLOWER_FARM),
    ROCKY_FARM("Rocky Farm", 60, 45, GameAsset.ROCKY_FARM),
    DESERT_FARM("Desert Farm", 40, 30, GameAsset.DESERT_FARM);
    private List<HardCodeFarmElements> structures = new ArrayList<>();
    private final Integer length;
    private final String name;
    private final Integer width;
    private final Texture texture;

    FarmType(String name, int length, int width, Texture texture) {
        this.name = name;
        this.length = length;
        this.width = width;
        this.texture = texture;
    }

    public static FarmType getFromName(String name) {
        for (FarmType value : FarmType.values()) {
            if (value.name.equals(name)) return value;
        }
        return null;
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
        GreenHouse greenHouse = new GreenHouse();
        Lake lake = new Lake();
        setTilePairs(cottage, new Pair(36, 32));
        setTilePairs(shippingBin, new Pair(44, 34));
        setTilePairs(greenHouse, new Pair(10, 22));
        for (int i = 10; i < 18; i++) {
            for (int j = 10; j < 20; j++) {
                lake.getTilePairList().add(new Pair(i, j));
                lake.setWidth(8);
                lake.setHeight(10);
            }
        }
        structures.addAll(List.of(cottage, shippingBin, greenHouse, lake));
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
        GreenHouse greenHouse = new GreenHouse();
        Lake lake = new Lake();
        setTilePairs(cottage, new Pair(6, 27));
        setTilePairs(shippingBin, new Pair(14, 29));
        setTilePairs(greenHouse, new Pair(45, 3));
        for (int i = 4; i < 14; i++) {
            for (int j = 4; j < 24; j++) {
                lake.getTilePairList().add(new Pair(i, j));
                lake.setWidth(10);
                lake.setHeight(20);
            }
        }
        structures.addAll(List.of(cottage, shippingBin, greenHouse, lake));
    }

    public void FlowerFarmInit() {
        Cottage cottage = new Cottage();
        ShippingBin shippingBin = new ShippingBin();
        GreenHouse greenHouse = new GreenHouse();
        Lake lake = new Lake();
        setTilePairs(cottage, new Pair(2, 4));
        setTilePairs(shippingBin, new Pair(10, 6));
        setTilePairs(greenHouse, new Pair(20, 2));
        for (int i = 26; i < 37; i++) {
            for (int j = 14; j < 20; j++) {
                lake.getTilePairList().add(new Pair(i, j));
                lake.setWidth(11);
                lake.setHeight(6);
            }
        }
        structures.addAll(List.of(cottage, shippingBin, greenHouse, lake));
    }

    public void RockyFarmInit() {
        Cottage cottage = new Cottage();
        ShippingBin shippingBin = new ShippingBin();
        GreenHouse greenHouse = new GreenHouse();
        Lake lake = new Lake();
        setTilePairs(cottage, new Pair(30, 20));
        setTilePairs(shippingBin, new Pair(38, 22));
        setTilePairs(greenHouse, new Pair(45, 20));
        for (int i = 3; i < 7; i++) {
            for (int j = 37; j < 42; j++) {
                lake.getTilePairList().add(new Pair(i, j));
                lake.setWidth(4);
                lake.setHeight(7);
            }
        }
        structures.addAll(List.of(cottage, shippingBin, greenHouse, lake));
    }

    public void DesertFarmInit() {
        Cottage cottage = new Cottage();
        ShippingBin shippingBin = new ShippingBin();
        GreenHouse greenHouse = new GreenHouse();
        Lake lake = new Lake();
        setTilePairs(cottage, new Pair(22, 10));
        setTilePairs(shippingBin, new Pair(30, 12));
        setTilePairs(greenHouse, new Pair(16, 18));
        lake.getTilePairList().addAll(List.of(new Pair(16, 15), new Pair(16, 14)));
        lake.setWidth(1);
        lake.setHeight(2);
        lake.setInDesert(true);
        structures.addAll(List.of(cottage, shippingBin, greenHouse, lake));
    }

    @Override
    public String toString() {
        return name;
    }
}
