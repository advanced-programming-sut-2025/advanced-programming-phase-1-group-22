package model.relations;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Account implements Serializable {
    private Integer id;
    private Integer golds = 0;

    public Account() {
    }

    public void removeGolds(int count) {
        golds -= count;
    }
}
