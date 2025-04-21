package model;

import lombok.Getter;

@Getter
public enum TileType {
    GRASS('1'), FLOWER('2'), SNOW('3'), MUD('4'), FLAT('.'), PATH('#'), FENCE('-'), DOOR('+');
    private final Character name;

    TileType(Character name) {
        this.name = name;
    }


    public Character StringToCharacter() {
        return name;
    }
}
