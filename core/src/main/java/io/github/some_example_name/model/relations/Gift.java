package io.github.some_example_name.model.relations;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import io.github.some_example_name.model.Actor;
import io.github.some_example_name.model.Salable;

@Getter
@Setter
@ToString
public class Gift {
    private Integer giftId = 0;
    private Salable gift;
    private Actor gifter;
    private Actor gifting;
    private Integer rate;

    public Gift(Actor gifter, Actor gifting, Salable gift) {
        this.gift = gift;
        this.gifter = gifter;
        this.gifting = gifting;
        this.giftId = getGiftId() + 1;
    }

    public void giveGiftToNPC() {

    }

    public void giveGiftToPlayer() {

    }
}
