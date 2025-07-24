package io.github.some_example_name.common.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FlagWraper {
    boolean flag;

    public FlagWraper(boolean b) {
        this.flag = b;
    }
}
