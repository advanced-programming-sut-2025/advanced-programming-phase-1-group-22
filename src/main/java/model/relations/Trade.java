package model.relations;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.Salable;

@Getter
@Setter
@ToString
@Builder
public class Trade {
    private Integer id;
    private Player customer;
    private Player trader;
    private String salable;
    private Integer quantity;
    private String requiredItem;
    private Integer quantityRequired;
    private Boolean isAccepted;
    private Integer price;
    private Boolean IShouldAnswer;
    private Boolean isAnswered;
    private static Integer lastId = 1;

    public Trade(Player customer, Player trader, String salable, int quantity, int price,boolean iShouldAnswer) {
        this.id = lastId++;
        this.customer = customer;
        this.trader = trader;
        this.salable = salable;
        this.quantity = quantity;
        this.price = price;
        this.IShouldAnswer = iShouldAnswer;
    }

    public Trade(Player customer, Player trader, String salable, Integer quantity, int quantityRequired, String requiredItem,boolean IShouldAnswer) {
        this.id = lastId++;
        this.customer = customer;
        this.trader = trader;
        this.salable = salable;
        this.quantity = quantity;
        this.quantityRequired = quantityRequired;
        this.requiredItem = requiredItem;
        this.IShouldAnswer = IShouldAnswer;
    }

    public Trade() {
    }

    public void createTrade() {

    }

    public void rejectTrade() {

    }

    public void acceptTrade() {

    }
}
