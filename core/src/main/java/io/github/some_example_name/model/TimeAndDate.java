package io.github.some_example_name.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.model.enums.Weather;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;
import lombok.Setter;
import io.github.some_example_name.model.enums.Season;
import io.github.some_example_name.utils.App;

@Getter
@Setter
public class TimeAndDate {
    private Sprite mainClock = new Sprite(GameAsset.CLOCK_MAIN);
    private Sprite arrow = new Sprite(GameAsset.CLOCK_ARROW);
    private Sprite seasonSprite = new Sprite(GameAsset.ClOCK_MANNERS[1]);
    private Sprite weather = new Sprite(GameAsset.ClOCK_MANNERS[6]);
    @JsonBackReference
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

    public TimeAndDate(int day, int hour, int minute, Season season, int year) {
        this.hour = hour;
        this.day = day;
        this.season = season;
        this.year = year;
        this.minute = minute;
    }

//    public void moveTimeForward(TimeAndDate timeAndDate)) {
//    }

    public void moveTimeForward() {
        boolean nextDay = false;
        int numberOfPlayers = App.getInstance().getCurrentGame().getPlayers().size();
        minute += 60/numberOfPlayers;
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
            setWeatherSprite(App.getInstance().getCurrentGame().getVillage().getTomorrowWeather());
            setSeasonSprite(season);
            App.getInstance().getCurrentGame().startDay();
        }
    }

    public int getTotalDays(){
        int days = App.getInstance().getCurrentGame().getTimeAndDate().getSeason().ordinal() * 28;
        days += App.getInstance().getCurrentGame().getTimeAndDate().getDay();
        return days;
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


    public void updateBatch(SpriteBatch batch) {
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        float width = 976;
        float height = 800;
        mainClock.draw(batch);
        mainClock.setSize(width, height);
        mainClock.setPosition(camera.position.x + Gdx.graphics.getWidth(),
            camera.position.y + Gdx.graphics.getHeight() - mainClock.getHeight()/2);
        arrow.draw(batch);
        arrow.setSize(width * 0.1f, height * 0.28f);
        arrow.setRotation(180 - 180f / 60 / 13 * ((hour - 9)*60 + minute) );
        arrow.setPosition(mainClock.getX() + width * 0.3082f - arrow.getWidth()/2 * (float) Math.cos(arrow.getRotation()/180 * Math.PI),
            mainClock.getY() + height / 2 + arrow.getHeight()/2 - arrow.getWidth()/2 * (float) Math.sin(arrow.getRotation()/180 * Math.PI));
        GameAsset.MAIN_FONT.getData().setScale(6.5f);
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
    }

    private void setWeatherSprite(Weather weather) {
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
    private void setSeasonSprite(Season season) {
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
}
