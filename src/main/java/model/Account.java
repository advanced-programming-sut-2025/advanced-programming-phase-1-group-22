package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
@Getter
@Setter
@ToString
public class Account  {
    private Integer id;
    private Integer golds;

    public void removeGolds(int count) {
        golds -= count;
    }
}
