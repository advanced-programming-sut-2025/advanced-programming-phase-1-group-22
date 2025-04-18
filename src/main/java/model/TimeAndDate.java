package model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.enums.Season;

@Getter
@Setter
@ToString
public class TimeAndDate {

    private Integer hour = 9;
    private Integer minute = 0;
    private Season season = Season.SPRING;
    private Integer day = 1;

    public TimeAndDate(int day, int hour) {
        this.hour = hour;
        this.day = day;
    }

    public void moveTimeForward(TimeAndDate timeAndDate) {

    }
}
