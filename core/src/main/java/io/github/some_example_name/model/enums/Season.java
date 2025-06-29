package io.github.some_example_name.model.enums;

import lombok.Getter;
import lombok.ToString;
import io.github.some_example_name.model.source.SeedType;


import java.util.List;
@Getter
public enum Season {
    SPRING(),SUMMER(),FALL(),WINTER(),SPECIAL();

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
