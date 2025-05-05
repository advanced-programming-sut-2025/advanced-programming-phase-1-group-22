package controller.gameMenu;

import controller.MenuController;
import model.*;
import model.enums.Weather;
import model.exception.InvalidInputException;
import model.receipe.CraftingRecipe;
import model.records.Response;
import model.relations.Player;
import model.structure.farmInitialElements.GreenHouse;
import service.GameService;
import utils.App;

import javax.swing.plaf.SpinnerUI;
import java.util.List;
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
        return gameService.walk(params[0], params[1]);
    }

    public Response whereAmI(String[] params) {
        return new Response(App.getInstance().getCurrentGame().getCurrentPlayer().getTiles().getFirst().toString(),
                true);
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
        if (params[2] != null) {
            int itemNumber = Integer.parseInt(params[1]);
            return gameService.removeFromPlayerInventory(itemName, true, itemNumber);
        }
        return gameService.removeFromPlayerInventory(itemName, false);
    }

    public Response toolEquip(String[] params) {
        return gameService.toolEquip(params[0].trim());
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

    public Response fishing(String[] parms) {
        String fishingPoleName = parms[0];
        return gameService.fishing(fishingPoleName);
    }

    public Response build(String[] parms) {
        String buildingName = parms[0].trim();
        int x = Integer.parseInt(parms[1]);
        int y = Integer.parseInt(parms[2]);
        return gameService.build(buildingName, x, y);
    }

    public Response buyAnimal(String[] parms) {
        String animal = parms[0].trim();
        String name = parms[1].trim();
        return gameService.buyAnimal(animal, name);
    }

    public Response pet(String[] params) {
        String name = params[0].trim();
        return gameService.pet(name);
    }

    public Response setFriendship(String[] parms) {
        String name = parms[0].trim();
        int count = Integer.parseInt(parms[1]);
        return gameService.setFriendShip(name, count);
    }

    public Response showAnimals(String[] parms) {
        return gameService.showAnimals();
    }

    public Response shepherdAnimals(String[] params) {
        String name = params[0].trim();
        int x = Integer.parseInt(params[1]);
        int y = Integer.parseInt(params[2]);
        return gameService.shepherdAnimals(name, x, y);
    }

    public Response feedHay(String[] parms) {
        String name = parms[0].trim();
        return gameService.feedHay(name);
    }

    public Response sellAnimal(String[] params) {
        String name = params[0].trim();
        return gameService.sellAnimal(name);
    }

    public Response produces(String[] params) {
        return gameService.produces();
    }

    public Response collectProduce(String[] params) {
        String name = params[0].trim();
        return gameService.collectProduce(name);
    }

    public Response craftInfo(String[] params) {
        String name = params[0].trim();
        return gameService.craftInfo(name);
    }

    public Response plantSeed(String[] params) {
        String name = params[0].trim();
        String direction = params[1];
        return gameService.plantSeed(name, direction);
    }

    public Response showPlant(String[] params) {
        int x = Integer.parseInt(params[0]);
        int y = Integer.parseInt(params[1]);
        return gameService.showPlant(x, y);
    }

    public Response fertilize(String[] params) {
        String fertilize = params[0].trim();
        String direction = params[1];
        return gameService.fertilize(fertilize, direction);
    }

    public Response howMuchWater(String[] params) {
        return gameService.howMuchWater();
    }

    public Response placeItem(String[] params) {
        return gameService.placeItem(params[0], params[1]);
    }

    public Response C_AddItem(String[] params) {
        return gameService.C_AddItem(params[0].trim(), params[1]);
    }

    public Response C_SetFriendship(String[] params) {
        return null;
    }

    public Response artisanUse(String[] params) {
        return gameService.artisanUse(params[0], params[1], params[2]);
    }

    public Response artisanGet(String[] params) {
        return gameService.artisanGet(params[0]);
    }

    public Response C_AddDollars(String[] params) {
        return gameService.C_AddDollars(params[0]);
    }

    public Response sell(String[] params) {
        return gameService.sell(params[0], params[1]);
    }

    public Response sellAll(String[] params) {
        return gameService.sellAll(params[0]);
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
        return gameService.eat(params[0]);
    }

    public Response questsList(String[] params) {
        return null;
    }

    public Response friendshipNPCList(String[] params) {
        return null;
    }

    public Response craftingShowRecipes(String[] strings) {
        return gameService.craftingShowRecipes();
    }

    public Response cookingShowRecipes(String[] strings) {
        return gameService.cookingShowRecipes();
    }

    public Response cookingRefrigeratorPick(String[] strings) {
        return gameService.cookingRefrigeratorPick(strings[0]);
    }

    public Response craftingCraft(String[] strings) {
        return gameService.craftingCraft(strings[0]);
    }

    public Response cookingRefrigeratorPut(String[] strings) {
        return gameService.cookingRefrigeratorPut(strings[0]);
    }

    public Response cookingPrepare(String[] strings) {
        return gameService.cookingPrepare(strings[0]);
    }

    public Response showAllProducts(String[] strings) {
        return gameService.showAllProducts();
    }

    public Response showAllAvailableProducts(String[] strings) {
        return gameService.showAllAvailableProducts();
    }

    public Response purchase(String[] strings) {
        return gameService.purchase(strings[0], strings[1]);
    }

    public Response purchase1(String[] strings) {
        return gameService.purchase(strings[0], "1");
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
