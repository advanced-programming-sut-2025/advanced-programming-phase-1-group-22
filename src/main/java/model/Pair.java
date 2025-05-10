package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Pair {
    private Integer x;
    private Integer y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
