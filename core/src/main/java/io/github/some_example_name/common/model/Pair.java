package io.github.some_example_name.common.model;

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

    public boolean isOrigin() {
        return x == 0 && y == 0;
    }
}
