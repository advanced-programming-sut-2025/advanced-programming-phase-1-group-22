package io.github.some_example_name.common.model;

import lombok.Getter;

@Getter
public enum StructureUpdateState {
    ADD("add structure"),
    UPDATE("update structure"),
    DELETE("delete structure");

    private final String name;

    StructureUpdateState(String name) {
        this.name = name;
    }
}
