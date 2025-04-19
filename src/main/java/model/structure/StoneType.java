package model.structure;

import lombok.Getter;

@Getter
public enum StoneType {
    SMALL_STONE(1, 1), NORMAL_STONE(2, 1), LARGE_STONE(2, 2);
    private Integer length;
    private Integer width;

    StoneType(int length, int width) {
        this.length = length;
        this.width = width;
    }
}
