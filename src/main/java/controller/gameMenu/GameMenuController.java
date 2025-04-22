package controller.gameMenu;

import controller.MenuController;
import model.*;
import model.enums.Weather;
import model.exception.InvalidInputException;
import model.records.Response;
import model.structure.farmInitialElements.GreenHouse;
import service.GameService;
import utils.App;

import javax.swing.plaf.SpinnerUI;
import java.util.Scanner;

public class GameMenuController extends MenuController {
    private final GameService gameService = GameService.getInstance();

    public Response exitGame(String[] params) {
        return gameService.exitGame();
    }

    public Response terminateGame(String[] params) {
        return gameService.terminateGame();
    }

    public Response nextTurn(String[] params) {
        return gameService.nextTurn();
    }

    public Response time(String[] params) {
        return gameService.time();
    }

    public Response date(String[] params) {
        return gameService.date();
    }

    public Response dateTime(String[] params) {
        return gameService.dateTime();
    }

    public Response dayOfTheWeek(String[] params) {
        return gameService.dayOfTheWeek();
    }

    public Response season(String[] params) {
        return gameService.season();
    }

    public Response weather(String[] params) {
        return gameService.weather();
    }

    public Response weatherForecast(String[] params) {
        return gameService.weatherForecast();
    }

    public Response greenhouseBuild(String[] params) {
        return gameService.greenhouseBuild();
    }

    public Response helpReadingMap(String[] params) {
        return gameService.helpReadingMap();
    }

    public Response walk(String[] params) {
        return gameService.walk(params[0], params[1]); //TODO Scanner not passed
    }

    public Response C_Thor(String[] params) {
        return gameService.C_Thor(params[0], params[1]);
    }

    public Response C_WeatherSet(String[] params) {
        return gameService.C_WeatherSet(params[0]);
    }

    public Response printMap(String[] params) {
        return gameService.printMap(params[0], params[1], params[2]);
    }

    public Response C_AdvnaceTime(String[] params) {
        return gameService.C_AdvanceTime(params[0]);
    }

    public Response C_AdvanceDate(String[] params) {
        return gameService.C_AdvanceDate(params[0]);
    }

    public Response showPlayerEnergy(String... params) {
        return gameService.showPlayerEnergy();
    }

    public Response setPlayerEnergy(String[] params) {
        int energy = Integer.parseInt(params[0]);
        return gameService.setPlayerEnergy(energy);
    }

    public Response setPlayerUnlimitedEnergy(String[] params) {
        return gameService.setPlayerUnlimitedEnergy();
    }

    public Response showPlayerInventory(String... params) {
        return gameService.showPlayerInventory();
    }

    public Response removeFromPlayerInventory(String[] params) {
        String itemName = params[0];
        boolean haveItemNumber = params.length == 2;
        if (haveItemNumber) {
            int itemNumber = Integer.parseInt(params[1]);
            return gameService.removeFromPlayerInventory(itemName, true, itemNumber);
        }
        return gameService.removeFromPlayerInventory(itemName,false);
    }

    public Response toolEquip(String[] params) {
        return gameService.toolEquip(params[0]);
    }

    public Response showCurrentTool(String[] params) {
        return gameService.showCurrentTool();
    }

    public Response showAvailableTools(String[] params) {
        return gameService.showAvailableTools();
    }

    public Response upgradeTool(String[] params) {
        return gameService.upgradeTool(params[0]);
    }

    public Response useTool(String[] params) {
        return gameService.useTool(params[0]);
    }

    public Response pickFromFloor(String[] params) {
        return gameService.pickFromFloor();
    }

    public Response craftInfo(String[] params) {
        return null;
    }

    public Response plantSeed(String[] params) {
        return null;
    }

    public Response showplant(String[] params) {
        return null;
    }

    public Response fertilize(String[] params) {
        return null;
    }

    public Response howMuchWater(String[] params) {
        return null;
    }

    public Response placeItem(String[] params) {
        return null;
    }

    public Response C_AddItem(String[] params) {
        return null;
    }

    public Response pet(String[] params) {
        return null;
    }

    public Response feedHay(String[] params) {
        return null;
    }

    public Response collectProduce(String[] params) {
        return null;
    }

    public Response sellAnimal(String[] params) {
        return null;
    }

    public Response shepherdAnimals(String[] params) {
        return null;
    }

    public Response animals(String[] params) {
        return null;
    }

    public Response produces(String[] params) {
        return null;
    }

    public Response C_SetFriendship(String[] params) {
        return null;
    }

    public Response artisanUse(String[] params) {
        return null;
    }

    public Response artisanGet(String[] params) {
        return null;
    }

    public Response C_AddDollars(String[] params) {
        return null;
    }

    public Response sell(String[] params) {
        return null;
    }

    public Response friendship(String[] params) {
        return null;
    }

    public Response talk(String[] params) {
        return null;
    }

    public Response talkHistory(String[] params) {
        return null;
    }

    public Response gift(String[] params) {
        return null;
    }

    public Response giftList(String[] params) {
        return null;
    }

    public Response giftRate(String[] params) {
        return null;
    }

    public Response giftHistory(String[] params) {
        return null;
    }

    public Response hug(String[] params) {
        return null;
    }

    public Response flower(String[] params) {
        return null;
    }

    public Response askMarriage(String[] params) {
        return null;
    }

    public Response respond(String[] params) {
        return null;
    }

    public Response startTrade(String[] params) {
        return null;
    }

    public Response meetNPC(String[] params) {
        return null;
    }

    public Response giftNPC(String[] params) {
        return null;
    }

    public Response questsFinish(String[] params) {
        return null;
    }

    public Response eat(String[] params) {
        return null;
    }

    public Response questsList(String[] params) {
        return null;
    }

    public Response friendshipNPCList(String[] params) {
        return null;
    }

    public Response craftingShowRecipes(String[] strings) {
        return null;
    }

    public Response cookingShowRecipes(String[] strings) {
        return null;
    }

    public Response cookingRefrigeratorPick(String[] strings) {
        return null;
    }

    public Response craftingCraft(String[] strings) {
        return null;
    }

    public Response cookingRefrigeratorPut(String[] strings) {
        return null;
    }

    public Response cookingPrepare(String[] strings) {
        return null;
    }

    public Response build(String[] strings) {
        return null;
    }

    public Response buyAnimal(String[] strings) {
        return null;
    }

    public Response showAllProducts(String[] strings) {
        return null;
    }

    public Response purchase(String[] strings) {
        return null;
    }

    public Response purchase1(String[] strings) {
        return null;
    }

    public Response trade(String[] strings) {
        return null;
    }

    public Response tradeList(String[] strings) {
        return null;
    }

    public Response tradeResponse(String[] strings) {
        return null;
    }

    public Response tradeHistory(String[] strings) {
        return null;
    }
}
