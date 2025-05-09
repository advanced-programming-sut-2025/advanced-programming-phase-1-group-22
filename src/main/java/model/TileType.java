package model;

import lombok.Getter;

import java.io.Serializable;

@Getter
public enum TileType implements Serializable {
    GRASS('G'), FLOWER('F'), SNOW('S'), MUD('M'), FLAT('.'),
    PATH('+'), FENCE('C'), DOOR('D'),PLOWED('P');
    private Character name;

    TileType() {
    }

    TileType(Character name) {
        this.name = name;
    }


    public Character StringToCharacter() {
        return name;
    }
}
