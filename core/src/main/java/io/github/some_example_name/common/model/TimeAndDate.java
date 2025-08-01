package io.github.some_example_name.common.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.common.model.enums.Weather;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.utils.GameAsset;
import lombok.Getter;
import lombok.Setter;
import io.github.some_example_name.common.model.enums.Season;
import io.github.some_example_name.common.utils.App;

@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class TimeAndDate {
    @JsonIgnore
    private transient Sprite mainClock = new Sprite(GameAsset.CLOCK_MAIN);
    @JsonIgnore
    private transient Sprite arrow = new Sprite(GameAsset.CLOCK_ARROW);
    @JsonIgnore
    private transient Sprite seasonSprite = new Sprite(GameAsset.ClOCK_MANNERS[1]);
    @JsonIgnore
    private transient Sprite weather = new Sprite(GameAsset.ClOCK_MANNERS[6]);
//    @JsonBackReference
    private transient Game currentGame = App.getInstance().getCurrentGame();
    private float delta = 0f;
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

    public TimeAndDate(int day, int hour, int minute, Season season, int year) {
        this.hour = hour;
        this.day = day;
        this.season = season;
        this.year = year;
        this.minute = minute;
    }

    public void updateTime(float delta) {
        this.delta += delta;
        if (this.delta > 4) {
            moveTimeForward();
            this.delta -= 4;
        }
    }

    public void moveTimeForward() {
        boolean nextDay = false;
        minute += 1;
        App.getInstance().getCurrentGame().getVillage().updateNpcs(this);
        if (minute >= 60) {
            hour++;
            minute = 0;
        }
        if (hour >= 22) {
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
        }
        Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
        if (player.getBuff() != null) {
            if (!player.getBuff().nextHour()) {
                player.getBuff().defectBuff(player);
                player.setBuff(null);
            }
        }
    }

    public int getTotalDays(){
        int days = year * 4;
        days += season.ordinal();
        days *= 28;
        days += day;
        return days;
    }

    public String getTime() {
        return getHour() + ":" + getMinute();
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
        res += "\nTime: " + hour + ":" + minute + "'";
        return res;
    }
    @JsonBackReference
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
        if (hour >= 24) {
            day++;
            hour = 0;
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
        return new TimeAndDate(day, this.hour, this.minute, season, year);
    }

    public TimeAndDate getNextXDay(int x) {
        TimeAndDate timeAndDate = this;
        for (int i = 0; i < x; i++) {
            timeAndDate = timeAndDate.getNextDay();
        }
        return timeAndDate;
    }


    public Sprite updateBatch(SpriteBatch batch) {
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        float width = 366;
        float height = 300;
        mainClock.draw(batch);
        mainClock.setSize(width, height);
        mainClock.setPosition(camera.position.x + camera.viewportWidth/2f - mainClock.getWidth(),
            camera.position.y + camera.viewportHeight/2f - mainClock.getHeight());
        arrow.draw(batch);
        arrow.setSize(width * 0.1f, height * 0.28f);
        arrow.setRotation(180 - 180f / 60 / 13 * ((hour - 9)*60 + minute) );
        arrow.setPosition(mainClock.getX() + width * 0.3082f - arrow.getWidth()/2 * (float) Math.cos(arrow.getRotation()/180 * Math.PI),
            mainClock.getY() + height / 2 + arrow.getHeight()/2 - arrow.getWidth()/2 * (float) Math.sin(arrow.getRotation()/180 * Math.PI));
        GameAsset.MAIN_FONT.getData().setScale(2.43f);
        GameAsset.MAIN_FONT.setColor(Color.RED);
        GameAsset.MAIN_FONT.draw(batch, getDayOfTheWeek() + " " + getDay(),
            mainClock.getX() + width*0.43f, mainClock.getY() + height * 0.90f);
        GameAsset.MAIN_FONT.draw(batch, getHour()%12 + ":" + getMinute() + " " + (getHour() > 11 ? "p.m." : "a.m."),
            mainClock.getX() + width*0.50f, mainClock.getY() + height * 0.50f);
        weather.draw(batch);
        weather.setSize(width * 0.180f, height * 0.200f);
        weather.setPosition(mainClock.getX() + 0.405f * width, mainClock.getY() + 0.55f * height);
        seasonSprite.draw(batch);
        seasonSprite.setSize(weather.getWidth(), weather.getHeight());
        seasonSprite.setPosition(weather.getX() + 0.33f * width, weather.getY());
        int golds = App.getInstance().getCurrentGame().getCurrentPlayer().getAccount().getGolds();
        for (int i = 0; golds > 0; golds /= 10, i++) {
            GameAsset.MAIN_FONT.draw(batch, "" + golds % 10,
                mainClock.getX() + width * (0.826f - 0.082f*i), mainClock.getY() + height * 0.17f);
        }
        return mainClock;
    }

    public void setWeatherSprite(Weather weather) {
        if (App.getInstance().getCurrentGame().getCurrentPlayer().getIsWedding()) {
            this.weather = new Sprite(GameAsset.ClOCK_MANNERS[8]);
            return;
        }
        switch (weather) {
            case RAINY -> this.weather = new Sprite(GameAsset.ClOCK_MANNERS[6]);
            case SNOWY -> this.weather = new Sprite(GameAsset.ClOCK_MANNERS[9]);
            case STORMY -> this.weather = new Sprite(GameAsset.ClOCK_MANNERS[11]);
            case SUNNY -> this.weather = new Sprite(GameAsset.ClOCK_MANNERS[7]);
        }
    }
    public void setSeasonSprite() {
        if (App.getInstance().getCurrentGame().getCurrentPlayer().getIsWedding()) {
            this.seasonSprite = new Sprite(GameAsset.ClOCK_MANNERS[3]);
            return;
        }
        switch (season) {
            case SPRING -> this.seasonSprite = new Sprite(GameAsset.ClOCK_MANNERS[0]);
            case SUMMER -> this.seasonSprite = new Sprite(GameAsset.ClOCK_MANNERS[1]);
            case FALL -> this.seasonSprite = new Sprite(GameAsset.ClOCK_MANNERS[2]);
            case WINTER -> this.seasonSprite = new Sprite(GameAsset.ClOCK_MANNERS[4]);
        }
    }

    public String getDayHour() {
        return day + "d " + hour + "h";
    }

    public long difference(TimeAndDate ETA) {
        long sum = 0;
        sum += (year - ETA.year);
        sum *= 4;
        sum += (season.ordinal() - ETA.season.ordinal());
        sum *= 28;
        sum += (day - ETA.day);
        sum *= 24;
        sum += (hour - ETA.hour);
        sum *= 60;
        sum += (minute - ETA.minute);
        return sum;
    }

    public TimeAndDate copy() {
        return new TimeAndDate(day, hour, minute, season, year);
    }

    public void resetDay() {
        delta = 0;
        minute = 0;
        hour = 9;
    }
}

