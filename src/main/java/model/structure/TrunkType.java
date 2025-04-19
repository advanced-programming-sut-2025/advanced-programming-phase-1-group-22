package model.structure;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum TrunkType {
    SMALL_TRUNK(1, 1), NORMAL_TRUNK(2, 1), LARGE_TRUNK(2, 2);
    private Integer length;
    private Integer width;

    TrunkType(Integer length, Integer width) {
        this.length = length;
        this.width = width;
    }
}
