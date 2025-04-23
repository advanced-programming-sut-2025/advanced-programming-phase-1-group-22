package model.relations;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.Salable;

@Getter
@Setter
@ToString
public class Trade {
    private Integer id;
    private Player customer;
    private Player trader;
    private Salable salable;
    private Integer quantity;
    private Salable requieredSalable;
    private Integer quantityRequired;
    private Boolean isAccepted;
    private Integer price;
    public void createTrade(){

    }
    public void rejectTrade(){

    }
    public void acceptTrade(){

    }
}
