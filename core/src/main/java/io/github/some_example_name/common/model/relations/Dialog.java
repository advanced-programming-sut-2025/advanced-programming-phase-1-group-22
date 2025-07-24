package io.github.some_example_name.common.model.relations;

import lombok.Getter;
import lombok.ToString;
import io.github.some_example_name.common.model.Game;
import io.github.some_example_name.common.model.TimeAndDate;
import io.github.some_example_name.common.utils.App;

@Getter
@ToString
public enum Dialog {
    D1("Hello"),
    D2("Are you ok?"),
    D3("How is it going?"),
    D4("long time no see!"),
    D5("Goodbye"),
    D6("nice to meet you!");

    private final String dialog;

    Dialog(String dialog) {
        this.dialog = dialog;
    }

    public static Dialog getDialog(TimeAndDate timeAndDate, int friendShipLevel) {
        Game currentGame = App.getInstance().getCurrentGame();
        return Dialog.values()[currentGame.getTimeAndDate().getSeason().ordinal() % 2 +
            currentGame.getVillage().getWeather().ordinal() % 2 + timeAndDate.getHour() / 12 + friendShipLevel / 4];
    }
}
