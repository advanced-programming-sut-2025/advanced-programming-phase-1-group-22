package model.enums;

import lombok.Getter;
import model.Tile;

import java.util.List;

@Getter

public enum Weather {
    SUNNY(List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER)),
    RAINY(List.of(Season.SPRING, Season.SUMMER, Season.FALL)),
    STORMY(List.of(Season.SPRING, Season.SUMMER, Season.FALL)),
    SNOWY(List.of(Season.WINTER));
    private final List<Season> seasons;

    Weather(List<Season> seasons) {
        this.seasons = seasons;
    }

    public void weatherEffect() {

    }

    public void thunderBolt(Tile... tiles) {

    }
}