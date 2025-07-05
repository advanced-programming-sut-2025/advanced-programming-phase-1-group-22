package io.github.some_example_name.model.relations;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import io.github.some_example_name.model.Actor;
import io.github.some_example_name.model.Salable;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public class Gift {
    private Integer giftId;
    private Salable gift;
    private Integer amount;
    private Actor giver;
    private Actor given;
    private Integer rate;

    public Gift(Actor giver, Actor given, Integer amount, Salable gift, int giftId) {
        this.gift = gift;
        this.amount = amount;
        this.giver = giver;
        this.given = given;
        this.giftId = giftId;
    }

    public void giveGiftToNPC() {

    }

    public void giveGiftToPlayer() {

    }
}
