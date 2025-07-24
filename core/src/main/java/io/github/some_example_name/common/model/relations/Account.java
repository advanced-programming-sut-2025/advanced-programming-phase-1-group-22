package io.github.some_example_name.common.model.relations;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Account {
    private Integer id;
    private Integer golds = 0;
    private Integer earned = 0;

    public void removeGolds(int count) {
        golds -= count;
        if (count < 0) earned -= count;
    }

    public void setGolds(int count) {
        if (count > golds) earned += (count - golds);
        golds = count;
    }
}
