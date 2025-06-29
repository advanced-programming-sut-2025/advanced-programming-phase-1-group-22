package io.github.some_example_name.model.relations;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Account {
    private Integer id;
    private Integer golds = 0;

    public void removeGolds(int count) {
        golds -= count;
    }
}
