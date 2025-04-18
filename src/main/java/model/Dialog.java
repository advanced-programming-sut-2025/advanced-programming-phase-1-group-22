package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.enums.Season;
import model.enums.Weather;
@Getter
@ToString
public enum Dialog {
    D1("Salam");
    private Weather weather;
    private Season currentSeason;
    private TimeAndDate timeAndDate;
    private Integer friendShipLevel;
    private final String dialog;

    Dialog(String dialog) {
        this.dialog = dialog;
    }
}
