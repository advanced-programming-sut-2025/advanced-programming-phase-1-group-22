package io.github.some_example_name.common.model.relations;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
    private Boolean IShouldAnswer = false;
    private Boolean isAnswered = false;
    private static Integer lastId = 1;
    private Boolean isSuccessfulled =false;

    public Trade(Player customer, Player trader, String salable, int quantity, int price, boolean iShouldAnswer) {
        this.id = lastId++;
        this.customer = customer;
        this.trader = trader;
        this.salable = salable;
        this.quantity = quantity;
        this.price = price;
        this.IShouldAnswer = iShouldAnswer;
    }

    public Trade(Player customer, Player trader, String salable, Integer quantity, int quantityRequired, String requiredItem, boolean IShouldAnswer) {
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

    @Override
    public String toString() {
        return "Trade{" +
                "id=" + id +
                ", customer=" + customer.getUser().getUsername() +
                ", trader=" + trader.getUser().getUsername() +
                ", salable='" + salable + '\'' +
                ", quantity=" + quantity +
                ", requiredItem='" + requiredItem + '\'' +
                ", quantityRequired=" + quantityRequired +
                ", isAccepted=" + isAccepted +
                ", price=" + price +
                ", IShouldAnswer=" + IShouldAnswer +
                ", isAnswered=" + isAnswered +
                ", isSuccessfulled=" + isSuccessfulled +
                '}';
    }
}
