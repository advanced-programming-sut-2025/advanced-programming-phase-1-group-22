package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pair {
    private Integer x;
    private Integer y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
