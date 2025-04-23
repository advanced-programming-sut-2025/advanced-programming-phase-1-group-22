package model.relations;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.Actor;
import model.Salable;
import model.enums.Season;

import java.util.Map;

@Getter
@Setter
@ToString
public class Mission {
    private Integer id;
    private Actor doer;
    private NPCType requester;
    private Map<Salable, Integer> request;
    private Map<Salable, Integer> reward;
    private Integer requiredLevel;
    private Season requiredSeason;

    public Mission(NPCType requester, Map<Salable, Integer> request, Map<Salable, Integer> reward, Integer requiredLevel) {
        this.requester = requester;
        this.request = request;
        this.reward = reward;
        this.requiredLevel = requiredLevel;
    }

    public Mission(NPCType requester, Map<Salable, Integer> request, Map<Salable, Integer> reward, Season requiredSeason) {
        this.requester = requester;
        this.request = request;
        this.reward = reward;
        this.requiredSeason = requiredSeason;
    }

    public void giveMission() {

    }

    public void getMission() {

    }
}
