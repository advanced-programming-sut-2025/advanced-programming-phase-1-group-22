package io.github.some_example_name.common.model.relations;

import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.utils.App;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Trade {
    private Integer id;
    private Player customer;
    private Player trader;
    private Map<Salable, Integer> proposed;
    private Map<Salable, Integer> answered;


    public Trade(Player player, Player player1, Map<Salable, Integer> proposed, Map<Salable, Integer> answered) {
        trader = player;
        customer = player1;
        this.proposed = proposed;
        this.answered = answered;
    }
}
