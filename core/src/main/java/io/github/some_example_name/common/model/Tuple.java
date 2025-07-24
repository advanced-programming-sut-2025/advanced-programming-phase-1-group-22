package io.github.some_example_name.common.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Tuple<T> {
    private T x;
    private T y;

    public Tuple(T x, T y) {
        this.x = x;
        this.y = y;
    }
}
