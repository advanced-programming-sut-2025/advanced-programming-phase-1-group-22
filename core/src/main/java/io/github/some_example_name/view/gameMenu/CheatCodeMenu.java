package io.github.some_example_name.view.gameMenu;

import io.github.some_example_name.command.CommandClass;
import io.github.some_example_name.controller.gameMenu.CheatCodeMenuController;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.view.CommandProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static io.github.some_example_name.command.GameCheatCommands.*;

public class CheatCodeMenu implements CommandProcessor {
    private static CheatCodeMenu instance;
    private final Map<CommandClass, Function<String[], Response>> commandsFunctionMap = new HashMap<>();

    private CheatCodeMenu() {
        CheatCodeMenuController controller = new CheatCodeMenuController();
        commandsFunctionMap.put(craftingShowRecipes, controller::craftingShowRecipes);
        commandsFunctionMap.put(cookingShowRecipes, controller::cookingShowRecipes);
        commandsFunctionMap.put(time, controller::time);
        commandsFunctionMap.put(date, controller::date);
        commandsFunctionMap.put(dateTime, controller::dateTime);
        commandsFunctionMap.put(dayOfTheWeek, controller::dayOfTheWeek);
        commandsFunctionMap.put(season, controller::season);
        commandsFunctionMap.put(weather, controller::weather);
        commandsFunctionMap.put(weatherForecast, controller::weatherForecast);
        commandsFunctionMap.put(walk, controller::walk);
        commandsFunctionMap.put(whereAmI, controller::whereAmI);
        commandsFunctionMap.put(C_Thor, controller::C_Thor);
        commandsFunctionMap.put(C_WeatherSet, controller::C_WeatherSet);
        commandsFunctionMap.put(C_AdvanceTime, controller::C_AdvnaceTime);
        commandsFunctionMap.put(C_AdvanceDate, controller::C_AdvanceDate);
        commandsFunctionMap.put(SHOW_ENERGY, controller::showPlayerEnergy);
        commandsFunctionMap.put(SET_ENERGY_UNLIMITED, controller::setPlayerUnlimitedEnergy);
        commandsFunctionMap.put(ENERGY_SET, controller::setPlayerEnergy);
        commandsFunctionMap.put(CRAFT_INFO, controller::craftInfo);
        commandsFunctionMap.put(SHOW_PLANT, controller::showPlant);
        commandsFunctionMap.put(C_AddItem, controller::C_AddItem);
        commandsFunctionMap.put(CHEAT_SET_FRIENDSHIP_WITH_ANIMAL, controller::setFriendship);
        commandsFunctionMap.put(SHOW_ANIMALS, controller::showAnimals);
        commandsFunctionMap.put(C_AddDollars, controller::C_AddDollars);
        commandsFunctionMap.put(eat, controller::eat);
        commandsFunctionMap.put(buffShow, controller::buffShow);
        commandsFunctionMap.put(CHANGE_FRIENDSHIP, controller::changeFriendship);
        commandsFunctionMap.put(NEXT_TURN, controller::nextTurn);
        commandsFunctionMap.put(IS_CROW_ATTACK, controller::isCrowAttack);
        commandsFunctionMap.put(SET_CROW_ATTACK, controller::setCrowAttack);
    }

    public static CheatCodeMenu getInstance() {
        if (instance == null) {
            instance = new CheatCodeMenu();
        }
        return instance;
    }

    @Override
    public Map<CommandClass, Function<String[], Response>> getFunctionsMap() {
        return commandsFunctionMap;
    }

}
