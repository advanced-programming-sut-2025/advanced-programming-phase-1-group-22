package model;

import lombok.Getter;
import lombok.Setter;
import model.enums.Season;
import model.relations.Friendship;
import model.relations.Mission;
import model.relations.NPC;
import model.relations.Player;
import utils.App;

@Getter
@Setter
public class TimeAndDate {
    private Game currentGame = App.getInstance().getCurrentGame();
    private Integer hour = 9;
    private Integer minute = 0;
    private Season season = Season.SPRING;
    private Integer day = 1;
    private Integer year = 0;

    public TimeAndDate() {
    }

    public TimeAndDate(int day, int hour) {
        this.hour = hour;
        this.day = day;
    }
    public TimeAndDate(int day, int hour, Season season, int year) {
        this.hour = hour;
        this.day = day;
        this.season = season;
        this.year = year;
    }

//    public void moveTimeForward(TimeAndDate timeAndDate)) {
//    }

    public void moveTimeForward() {
        for (Friendship friendship : currentGame.getFriendships()) {
            if (friendship.getLastSeen().getDay().equals(day)) {
                friendship.setXp(friendship.getXp() + -10);
                if (friendship.getXp() <= 0) {
                    friendship.setXp(90);
                    friendship.setFriendShipLevel(friendship.getFriendShipLevel() - 1);
                }
            }
        }
        giveRewardToLevelThreeFriends();
        boolean nextDay = false;
        minute += 15;
        if (minute >= 60) {
            hour++;
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
            year++;
            season = Season.SPRING;
        }
        if (nextDay) {
            App.getInstance().getCurrentGame().startDay();
            //TODO: reset Energy sets and randomizing the day
        }
    }

    private void giveRewardToLevelThreeFriends() {
        for (Friendship friendship : currentGame.getFriendships()) {
            if ((friendship.getFirstPlayer() instanceof NPC && friendship.getSecondPlayer() instanceof Player)) {
                if (friendship.getFriendShipLevel() == 3) {
                    NPC npc = (NPC) friendship.getFirstPlayer();
                    Mission mission = npc.getType().getMissions().get(npc.getType().getMissions().size() % 3);
                    int size = mission.getReward().entrySet().size();
                    Salable[] array = (Salable[]) mission.getReward().keySet().toArray();
                    Salable gift = array[size % 4];
                    ((Player) friendship.getSecondPlayer()).getInventory().addProductToBackPack(gift, 1);
                }
            }
            if ((friendship.getSecondPlayer() instanceof NPC && friendship.getFirstPlayer() instanceof Player)) {
                if (friendship.getFriendShipLevel() == 3) {
                    NPC npc = (NPC) friendship.getSecondPlayer();
                    Mission mission = npc.getType().getMissions().get(npc.getType().getMissions().size() % 3);
                    int size = mission.getReward().entrySet().size();
                    Salable[] array = (Salable[]) mission.getReward().keySet().toArray();
                    Salable gift = array[size % 4];
                    ((Player) friendship.getFirstPlayer()).getInventory().addProductToBackPack(gift, 1);
                }
            }
        }
    }

    public int compareDailyTime(TimeAndDate timeAndDate) {
        if (timeAndDate.getHour() > hour) return 1;
        if (timeAndDate.getHour() < hour) return -1;
        return timeAndDate.getMinute().compareTo(minute);
    }

    public int compareTime(TimeAndDate timeAndDate) {
        int ms = (((timeAndDate.getYear() * 4 + timeAndDate.getSeason().ordinal()) * 28 + timeAndDate.getDay()) * 24 +
                timeAndDate.getHour());
        int now = (((year * 4 + season.ordinal()) * 28 + day) * 24 + hour);

        if (ms == now) return 0;
        return ms < now ? -1 : 1;
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
        String res = "Year: " + year + "\nDay: " + (season.ordinal() * 28 + day);
        res += "Time: " + hour + ":" + minute + "'";
        return res;
    }

    public TimeAndDate getNextMorning() {
        int day = this.day + 1;
        int year = this.year;
        Season season = this.season;
        if (day >= 29) {
            day = 1;
            season = Season.values()[season.ordinal() + 1];
        }
        if (season.ordinal() == 4) {
            year++;
            season = Season.SPRING;
        }
        return new TimeAndDate(day, 9, season, year);
    }

    private TimeAndDate getNextHour() {
        int hour = this.hour + 1;
        int day = this.day;
        int year = this.year;
        Season season = this.season;
        if (hour >= 21) {
            day++;
            hour = 9;
        }
        if (day >= 29) {
            day = 1;
            season = Season.values()[season.ordinal() + 1];
        }
        if (season.ordinal() == 4) {
            year++;
            season = Season.SPRING;
        }
        return new TimeAndDate(day, hour, season, year);
    }

    public TimeAndDate getNextXHour(int x) {
        TimeAndDate timeAndDate = this;
        for (int i = 0; i < x; i++) {
            timeAndDate = timeAndDate.getNextHour();
        }
        return timeAndDate;
    }

    private TimeAndDate getNextDay() {
        int day = this.day + 1;
        int year = this.year;
        Season season = this.season;
        if (day >= 29) {
            day = 1;
            season = Season.values()[season.ordinal() + 1];
        }
        if (season.ordinal() == 4) {
            year++;
            season = Season.SPRING;
        }
        return new TimeAndDate(day, this.hour, season, year);
    }
    public TimeAndDate getNextXDay(int x) {
        TimeAndDate timeAndDate = this;
        for (int i = 0; i < x; i++) {
            timeAndDate = timeAndDate.getNextDay();
        }
        return timeAndDate;
    }
}
