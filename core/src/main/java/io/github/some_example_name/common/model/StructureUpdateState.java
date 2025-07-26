package io.github.some_example_name.common.model;

import lombok.Getter;

@Getter
public enum StructureUpdateState {
    ADD("=add_structure"),
    UPDATE("=update_structure"),
    DELETE("=delete_structure");

    private final String name;

    StructureUpdateState(String name) {
        this.name = name;
    }
}
