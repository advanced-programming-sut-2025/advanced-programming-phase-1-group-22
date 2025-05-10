package model;

import lombok.Getter;

@Getter
public enum TileType {
    GRASS("🌿"), FLOWER("🌸"), SNOW("❄️"), FLAT("▫️"),
    PATH("🧱"), FENCE("🚧"), DOOR("🚪"),PLOWED("🟫"), THUNDERED("🌩️");
    private final String name;

    TileType(String name) {
        this.name = name;
    }


    public String StringToCharacter() {
        return name;
    }
}
