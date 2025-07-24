package io.github.some_example_name.client.controller.gameMenu;

import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.server.service.GameService;
import io.github.some_example_name.server.service.RelationService;
import io.github.some_example_name.common.utils.App;

public class CheatCodeMenuController {
    private final GameService gameService = GameService.getInstance();
    private final RelationService relationService = RelationService.getInstance();

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

    public Response walk(String[] params) {
        int x = Integer.parseInt(params[0]);
        int y = Integer.parseInt(params[1]);
        return gameService.walk(x, y);
    }

    public Response whereAmI(String[] params) {
        return new Response(App.getInstance().getCurrentGame().getCurrentPlayer().getTiles().get(0).toString(),
            true);
    }

    public Response C_Thor(String[] params) {
        return gameService.C_Thor(params[0], params[1]);
    }

    public Response C_WeatherSet(String[] params) {
        return gameService.C_WeatherSet(params[0]);
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

    public Response setFriendship(String[] parms) {
        String name = parms[0].trim();
        int count = Integer.parseInt(parms[1]);
        return gameService.setFriendShip(name, count);
    }

    public Response showAnimals(String[] parms) {
        return gameService.showAnimals();
    }

    public Response craftInfo(String[] params) {
        String name = params[0].trim();
        return gameService.craftInfo(name);
    }

    public Response showPlant(String[] params) {
        int x = Integer.parseInt(params[0]);
        int y = Integer.parseInt(params[1]);
        return gameService.showPlant(x, y);
    }

    public Response C_AddItem(String[] params) {
        return gameService.C_AddItem(params[0].trim(), params[1]);
    }

    public Response C_AddDollars(String[] params) {
        return gameService.C_AddDollars(params[0]);
    }

    public Response eat(String[] params) {
        return gameService.eat(params[0]);
    }

    public Response buffShow(String[] params) {
        return gameService.buffShow();
    }

    public Response craftingShowRecipes(String[] strings) {
        return gameService.craftingShowRecipes();
    }

    public Response cookingShowRecipes(String[] strings) {
        return gameService.cookingShowRecipes();
    }

    public Response showAllProducts(String[] strings) {
        return gameService.showAllProducts();
    }

    public Response purchase(String[] strings) {
        return gameService.purchase(strings[0], strings[1]);
    }

    public Response changeFriendship(String... params) {
        int friendshipLevel = Integer.parseInt(params[0]);
        return relationService.friendShip_CH(friendshipLevel);
    }

    public Response nextTurn(String... strings) {
        return gameService.nextTurn();
    }

    public Response isCrowAttack(String... strings) {
        return gameService.isCrowAttackToday();
    }

    public Response setCrowAttack(String... strings) {
        return gameService.setCrowAttack();
    }
}
