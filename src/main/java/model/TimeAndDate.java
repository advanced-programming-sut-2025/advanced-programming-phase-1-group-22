package model;

import lombok.Getter;
import lombok.Setter;
import model.enums.Season;
import utils.App;

@Getter
@Setter
public class TimeAndDate {

    private Integer hour = 9;
    private Integer minute = 0;
    private Season season = Season.SPRING;
    private Integer day = 1;
    private Integer year = 0;

    public TimeAndDate() {}

    public TimeAndDate(int day, int hour) {
        this.hour = hour;
        this.day = day;
    }

//    public void moveTimeForward(TimeAndDate timeAndDate)) {
//    }

    public void moveTimeForward() {
        boolean nextDay = false;
        minute += 15;
        if (minute >= 60) {
            hour ++;
            minute = 0;
        }
        if (hour >= 21) {
            day++;
            hour = 9;
            nextDay = true;
        }
        if (day >= 29) {
            day = 1;
            season = Season.values()[season.ordinal() + 1];
        }
        if (season.ordinal() == 4) {
            year ++;
            season = Season.SPRING;
        }
        if (nextDay) {
            App.getInstance().getCurrentGame().getVillage().startDay();
            //TODO: reset Energy sets and randomizing the day
        }
    }

    public int compareTime(TimeAndDate timeAndDate) {
        if (timeAndDate.getHour() > hour) return 1;
        if (timeAndDate.getHour() < hour) return -1;
        return timeAndDate.getMinute().compareTo(minute);
    }

    public String getDayOfTheWeek() {
        return switch (day % 7) {
            case 0 -> "Saturday";
            case 1 -> "Sunday";
            case 2 -> "Monday";
            case 3 -> "Tuesday";
            case 4 -> "Wednesday";
            case 5 -> "Thursday";
            case 6 -> "Friday";
            default -> "No day";
        };
    }

    @Override
    public String toString() {
        String res = "Year: " + year + "\nDay: " + (season.ordinal()*28 + day);
        res += "Time: " + hour + ":" + minute + "'";
        return res;
    }
}
