package model;

import lombok.Getter;

@Getter
public enum TileType {
    GRASS('G'), FLOWER('F'), SNOW('S'), MUD('M'), FLAT('.'), PATH('+'), FENCE('C'), DOOR('D');
    private final Character name;

    TileType(Character name) {
        this.name = name;
    }


    public Character StringToCharacter() {
        return name;
    }
}
