package view.gameMenu;

import command.CommandClass;
import controller.gameMenu.GameMenuController;
import model.records.Response;
import utils.App;
import view.CommandProcessor;
import view.Menu;
import view.mainMenu.MapSelectionMenu;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Matcher;

import static command.GameCommands.*;

public abstract class GameMenu implements CommandProcessor {

    private final GameMenuController controller = new GameMenuController();
    
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
        commandsFunctionMap.put(PICK_PRODUCT,controller::pickFromFloor);
        commandsFunctionMap.put(craftInfo, controller::craftInfo);
        commandsFunctionMap.put(plantSeed, controller::plantSeed);
        commandsFunctionMap.put(showplant, controller::showplant);
        commandsFunctionMap.put(fertilize, controller::fertilize);
        commandsFunctionMap.put(howMuchWater, controller::howMuchWater);
        commandsFunctionMap.put(placeItem, controller::placeItem);
        commandsFunctionMap.put(C_AddItem, controller::C_AddItem);
        commandsFunctionMap.put(pet, controller::pet);
        commandsFunctionMap.put(feedHay, controller::feedHay);
        commandsFunctionMap.put(collectProduce, controller::collectProduce);
        commandsFunctionMap.put(sellAnimal, controller::sellAnimal);
        commandsFunctionMap.put(shepherdAnimals, controller::shepherdAnimals);
        commandsFunctionMap.put(animals, controller::animals);
        commandsFunctionMap.put(produces, controller::produces);
        commandsFunctionMap.put(C_SetFriendship, controller::C_SetFriendship);
        commandsFunctionMap.put(artisanUse, controller::artisanUse);
        commandsFunctionMap.put(artisanGet, controller::artisanGet);
        commandsFunctionMap.put(C_AddDollars, controller::C_AddDollars);
        commandsFunctionMap.put(sell, controller::sell);
        commandsFunctionMap.put(sellAll, controller::sellAll);
        commandsFunctionMap.put(friendship, controller::friendship);
        commandsFunctionMap.put(talk, controller::talk);
        commandsFunctionMap.put(talkHistory, controller::talkHistory);
        commandsFunctionMap.put(gift, controller::gift);
        commandsFunctionMap.put(giftList, controller::giftList);
        commandsFunctionMap.put(giftRate, controller::giftRate);
        commandsFunctionMap.put(giftHistory, controller::giftHistory);
        commandsFunctionMap.put(hug, controller::hug);
        commandsFunctionMap.put(flower, controller::flower);
        commandsFunctionMap.put(askMarriage, controller::askMarriage);
        commandsFunctionMap.put(respond, controller::respond);
        commandsFunctionMap.put(startTrade, controller::startTrade);
        commandsFunctionMap.put(meetNPC, controller::meetNPC);
        commandsFunctionMap.put(giftNPC, controller::giftNPC);
        commandsFunctionMap.put(questsFinish, controller::questsFinish);
        commandsFunctionMap.put(eat, controller::eat);
        commandsFunctionMap.put(questsList, controller::questsList);
        commandsFunctionMap.put(friendshipNPCList, controller::friendshipNPCList);
        return commandsFunctionMap;
    }
}
