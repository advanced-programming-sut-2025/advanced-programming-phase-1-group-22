package model.relations;

import lombok.Getter;
import lombok.ToString;
import model.TimeAndDate;
import model.enums.Season;
import model.enums.Weather;
@Getter
@ToString
public enum Dialog {
    D1("Hello"),D2("Are you ok?"),D3("Goodbye");
    private Weather weather;
    private Season currentSeason;
    private TimeAndDate timeAndDate;
    private Integer friendShipLevel;
    private final String dialog;

    Dialog(String dialog) {

        this.dialog = dialog;
    }
}
