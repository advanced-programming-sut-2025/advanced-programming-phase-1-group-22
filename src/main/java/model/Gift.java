package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Gift {
    private Salable gift;
    private Actor gifter;
    private Actor gifting;
    private Integer rate;
    public void giveGiftToNPC(){

    }
    public void giveGiftToPlayer(){

    }
}
