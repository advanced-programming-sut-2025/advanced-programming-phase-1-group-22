package view.gameMenu;

import command.CommandClass;
import controller.AccountMenuController;
import controller.MainMenuController;
import controller.gameMenu.GameMenuController;
import model.records.Response;
import view.CommandProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static command.AccountCommands.ENTER_MENU;
import static command.AccountCommands.SHOW_CURRENT_MENU;
import static command.GameCommands.*;

public abstract class GameMenu implements CommandProcessor {

    private final GameMenuController controller = new GameMenuController();
    private final MainMenuController mainMenuController = new MainMenuController();

    @Override
    public Map<CommandClass, Function<String[], Response>> getFunctionsMap() {
        final Map<CommandClass, Function<String[], Response>> commandsFunctionMap = new HashMap<>();
        commandsFunctionMap.put(exitGame, controller::exitGame);
        commandsFunctionMap.put(terminateGame, controller::terminateGame);
        commandsFunctionMap.put(nextTurn, controller::nextTurn);
        commandsFunctionMap.put(time, controller::time);
        commandsFunctionMap.put(date, controller::date);
        commandsFunctionMap.put(dateTime, controller::dateTime);
        commandsFunctionMap.put(dayOfTheWeek, controller::dayOfTheWeek);
        commandsFunctionMap.put(season, controller::season);
        commandsFunctionMap.put(weather, controller::weather);
        commandsFunctionMap.put(weatherForecast, controller::weatherForecast);
        commandsFunctionMap.put(greenhouseBuild, controller::greenhouseBuild);
        commandsFunctionMap.put(helpReadingMap, controller::helpReadingMap);
        commandsFunctionMap.put(walk, controller::walk);
        commandsFunctionMap.put(whereAmI, controller::whereAmI);
        commandsFunctionMap.put(C_Thor, controller::C_Thor);
        commandsFunctionMap.put(C_WeatherSet, controller::C_WeatherSet);
        commandsFunctionMap.put(printMap, controller::printMap);
        commandsFunctionMap.put(C_AdvanceTime, controller::C_AdvnaceTime);
        commandsFunctionMap.put(C_AdvanceDate, controller::C_AdvanceDate);
        commandsFunctionMap.put(SHOW_ENERGY, controller::showPlayerEnergy);
        commandsFunctionMap.put(SET_ENERGY_UNLIMITED, controller::setPlayerUnlimitedEnergy);
        commandsFunctionMap.put(SHOW_INVENTORY, controller::showPlayerInventory);
        commandsFunctionMap.put(SHOW_CURRENT_TOOL, controller::showCurrentTool);
        commandsFunctionMap.put(SHOW_AVAILABLE_TOOLS, controller::showAvailableTools);
        commandsFunctionMap.put(ENERGY_SET, controller::setPlayerEnergy);
        commandsFunctionMap.put(REMOVE_FROM_INVENTORY, controller::removeFromPlayerInventory);
        commandsFunctionMap.put(TOOL_EQUIP, controller::toolEquip);
        commandsFunctionMap.put(UPGRADE_TOOL, controller::upgradeTool);
        commandsFunctionMap.put(USE_TOOL, controller::useTool);
        commandsFunctionMap.put(PICK_PRODUCT, controller::pickFromFloor);
        commandsFunctionMap.put(FISHING, controller::fishing);
        commandsFunctionMap.put(CRAFT_INFO, controller::craftInfo);
        commandsFunctionMap.put(PLANT_SEED, controller::plantSeed);
        commandsFunctionMap.put(SHOW_PLANT, controller::showPlant);
        commandsFunctionMap.put(FERTILIZE, controller::fertilize);
        commandsFunctionMap.put(HOW_MUCH_WATER, controller::howMuchWater);
        commandsFunctionMap.put(placeItem, controller::placeItem);
        commandsFunctionMap.put(C_AddItem, controller::C_AddItem);
        commandsFunctionMap.put(PET, controller::pet);
        commandsFunctionMap.put(CHEAT_SET_FRIENDSHIP_WITH_ANIMAL, controller::setFriendship);
        commandsFunctionMap.put(FEED_HAY, controller::feedHay);
        commandsFunctionMap.put(COLLECT_PRODUCE, controller::collectProduce);
        commandsFunctionMap.put(SELL_ANIMAL, controller::sellAnimal);
        commandsFunctionMap.put(SHEPHERD_ANIMALS, controller::shepherdAnimals);
        commandsFunctionMap.put(SHOW_ANIMALS, controller::showAnimals);
        commandsFunctionMap.put(PRODUCES, controller::produces);
        commandsFunctionMap.put(artisanUse, controller::artisanUse);
        commandsFunctionMap.put(artisanGet, controller::artisanGet);
        commandsFunctionMap.put(C_AddDollars, controller::C_AddDollars);
        commandsFunctionMap.put(sell, controller::sell);
        commandsFunctionMap.put(sellAll, controller::sellAll);
        commandsFunctionMap.put(eat, controller::eat);
        commandsFunctionMap.put(SHOW_CURRENT_MENU, mainMenuController::showCurrentMenu);
        commandsFunctionMap.put(ENTER_MENU, mainMenuController::switchMenu);
        return commandsFunctionMap;
    }
}
