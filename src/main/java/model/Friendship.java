package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class Friendship {
    private Integer id;
    private Actor firstPlayer;
    private Actor secondPlayer;
    private List<Gift> gifts;
    private Integer friendShipLevel;
    private Integer xp;
    private Map<String, Actor> dialogs;

    public void talkToNPC() {

    }

    public void talkToPlayer() {

    }
}
