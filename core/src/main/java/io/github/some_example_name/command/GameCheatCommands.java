package io.github.some_example_name.command;

public class GameCheatCommands extends CommandClass {
    public static final GameCheatCommands time = new GameCheatCommands("^\\s*time\\s*$");
    public static final GameCheatCommands date = new GameCheatCommands("^\\s*date\\s*$");
    public static final GameCheatCommands dateTime = new GameCheatCommands("^\\s*dateTime\\s*$");
    public static final GameCheatCommands dayOfTheWeek = new GameCheatCommands("^\\s*day\\s+of\\s+the\\s+week\\s*$");
    public static final GameCheatCommands season = new GameCheatCommands("^\\s*season\\s*$");
    public static final GameCheatCommands C_AdvanceTime = new GameCheatCommands("^\\s*cheat\\s+advance\\s+time\\s+(?<X>\\d+)h\\s*$");
    public static final GameCheatCommands C_AdvanceDate = new GameCheatCommands("^\\s*cheat\\s+advance\\s+date\\s+(?<X>\\d+)d\\s*$");
    public static final GameCheatCommands C_Thor = new GameCheatCommands("^\\s*cheat\\s+thor\\s+(?<X>\\d+)\\s+(?<Y>\\d+)\\s*$");
    public static final GameCheatCommands weather = new GameCheatCommands("^\\s*weather\\s*$");
    public static final GameCheatCommands weatherForecast = new GameCheatCommands("^\\s*weather\\s+forecast\\s*$");
    public static final GameCheatCommands C_WeatherSet = new GameCheatCommands("^\\s*cheat\\s+weather\\s+set\\s+(?<Type>\\S+)\\s*$");
    public static final GameCheatCommands walk = new GameCheatCommands("^\\s*walk\\s+-l\\s+(?<X>\\d+)\\s*,\\s*(?<Y>\\d+)\\s*$");
    public static final GameCheatCommands whereAmI = new GameCheatCommands("^\\s*@\\s*$");
    public static final GameCheatCommands C_AddItem = new GameCheatCommands("^\\s*cheat\\s+add\\s+item\\s+-n\\s+(?<name>.+)\\s+-c\\s+" +
        "(?<count>\\S+)\\s*$");
    public static final GameCheatCommands C_AddDollars = new GameCheatCommands("^\\s*cheat\\s+add\\s+(?<count>\\d+)\\s+dollars\\s*$");
    public static final GameCheatCommands friendship = new GameCheatCommands("^\\s*friendship\\s*$");
    public static final GameCheatCommands eat = new GameCheatCommands("^\\s*eat\\s+(?<foodName>.+)\\s*$");
    public static final GameCheatCommands buffShow = new GameCheatCommands("^\\s*buff\\s+show\\s*$");
    public static final GameCheatCommands craftingShowRecipes = new GameCheatCommands("crafting show recipes");
    public static final GameCheatCommands cookingShowRecipes = new GameCheatCommands("^\\s*cooking\\s+show\\s+recipes\\s*$");
    public static final GameCheatCommands SHOW_ENERGY = new GameCheatCommands("show\\s+energy");
    public static final GameCheatCommands ENERGY_SET = new GameCheatCommands("set\\s+energy\\s+-v\\s+(\\d+)");
    public static final GameCheatCommands SET_ENERGY_UNLIMITED = new GameCheatCommands("energy\\s+unlimited");
    public static final GameCheatCommands CHEAT_SET_FRIENDSHIP_WITH_ANIMAL = new GameCheatCommands("^\\s*cheat\\s+set\\s+friendship\\s+-n\\s+(?<name>.+)\\s+-c\\s+" +
        "(?<count>\\d+)\\s*$");
    public static final GameCheatCommands SHOW_ANIMALS = new GameCheatCommands("^\\s*animals\\s*$");
    public static final GameCheatCommands CRAFT_INFO = new GameCheatCommands("^\\s*craft\\s+info\\s+-n\\s+(?<craftName>.+)\\s*$");
    public static final GameCheatCommands SHOW_PLANT = new GameCheatCommands("^\\s*showplant\\s+-l\\s+(?<X>\\d+)\\s*,\\s*(?<Y>\\d+)\\s*$");
    public static final GameCheatCommands CHANGE_FRIENDSHIP = new GameCheatCommands("change\\s+friendship\\s+(.*)");
    public static final GameCheatCommands NEXT_TURN = new GameCheatCommands("next\\s+turn");

    GameCheatCommands(String regex) {
        super(regex);
    }
}
