package model;

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

    public TimeAndDate() {}

    public TimeAndDate(int day, int hour) {
        this.hour = hour;
        this.day = day;
        moveTimeForward();
    }

//    public void moveTimeForward(TimeAndDate timeAndDate)) {
//    }

    public void moveTimeForward() {
        minute += 15;
        if (minute >= 60) {
            hour ++;
            minute = 0;
        }
        if (hour >= 21) {
            day++;
            hour = 9;
            //TODO: reset Energy sets and randomizing the day
        }
        if (day >= 29) {
            day = 1;
            season = Season.values()[(season.ordinal() + 1) % 4];
        }
    }

    public int compareTime(TimeAndDate timeAndDate) {
        if (timeAndDate.getHour() > hour) return 1;
        if (timeAndDate.getHour() < hour) return -1;
        if (timeAndDate.getMinute() > minute) return 1;
        if (timeAndDate.getMinute() < minute) return -1;
        return 0;
    }
}
