package io.github.some_example_name.common.model.enums;

import lombok.Getter;

@Getter
public enum Season {
    SPRING(),SUMMER(),FALL(),WINTER(),SPECIAL();

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
