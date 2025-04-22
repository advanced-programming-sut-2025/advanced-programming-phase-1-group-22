package service;

import model.*;
import model.enums.Weather;
import model.exception.InvalidInputException;
import model.records.Response;
import model.structure.farmInitialElements.GreenHouse;
import utils.App;
import utils.InitialGame;
import variables.Session;
import view.Menu;
import view.ViewRender;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameService {
    private static GameService instance;
    App app = App.getInstance();


    private GameService() {
    }

    public static GameService getInstance() {
        if (instance == null) {
            instance = new GameService();
        }
        return instance;
    }
    public Response exitGame() {
        if (app.getCurrentGame().getCurrentPlayer().getUser() != Session.getCurrentUser()) {
            return new Response("You are not allowed to exit; the player who has started the app.getCurrentGame() can" +
                    " end it");
        }
        //TODO save app.getCurrentGame() not mine to do
        Session.setCurrentMenu(Menu.MAIN);
        return new Response("Exited from game.", true);
    }

    public Response undoTermination() {
        app.getCurrentGame().setPlayersInFavorTermination(0);
        return new Response("Termination unsuccessful");
    }

    public Response terminateGame() {
        app.getCurrentGame().addTermination();
        if (app.getCurrentGame().getPlayersInFavorTermination() == app.getCurrentGame().getPlayers().size()) {
            App.getInstance().getGames().remove(app.getCurrentGame());
            Session.setCurrentMenu(Menu.MAIN);
            for (Player player : app.getCurrentGame().getPlayers()) {
                player.getUser().setIsPlaying(false);
            }

            //TODO remove app.getCurrentGame() from databases
            return new Response("The game is terminated", true);
        } else {
            return new Response("Do you agree on Termination? Either Enter \"no\" or \"terminate game\"");
        }
    }

    public Response nextTurn() {
        Player player = app.getCurrentGame().getCurrentPlayer();
        player.setEnergyPerTurn(player.getMaxEnergyPerTurn());
        app.getCurrentGame().nextPlayer();
        TimeAndDate time = new TimeAndDate(0, 8);
        if (app.getCurrentGame().getTimeAndDate().compareTime(time) <= 0) {
            player.setEnergy(player.getMaxEnergy()); //TODO implementing the faint
        }
        return new Response("It's next player's turn", true);
    }

    public Response time() {
        String result = app.getCurrentGame().getTimeAndDate().getHour() + ":";
        result += app.getCurrentGame().getTimeAndDate().getHour();
        return new Response(result, true);
    }

    public Response date() {
        Integer days = app.getCurrentGame().getTimeAndDate().getSeason().ordinal()*28;
        days += app.getCurrentGame().getTimeAndDate().getDay();
        return new Response(days.toString(), true);
    }

    public Response dateTime() {
        return new Response(app.getCurrentGame().getTimeAndDate().toString(), true);
    }

    public Response dayOfTheWeek() {
        return new Response(app.getCurrentGame().getTimeAndDate().getDayOfTheWeek(), true);
    }

    public Response C_AdvanceTime(String x) {
        int hours = Integer.parseInt(x);
        for (int i = 0; i < hours * 4; i++) {
            nextTurn();
        }
        return new Response(x + "hours passed", true);
    }

    public Response C_AdvanceDate(String x) {
        int days = Integer.parseInt(x);
        for (int i = 0; i < days * 4 * 13; i++) {
            nextTurn();
        }
        return new Response(x + "days passed", true);
    }

    public Response season() {
        return new Response(app.getCurrentGame().getTimeAndDate().getSeason().toString(), true);
    }

    public Response C_WeatherSet(String type) {
        Weather weather;
        try {
            weather = Weather.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new Response("Weather not found!");
        }
        app.getCurrentGame().getVillage().setWeather(weather);
        return new Response("Weather set to " + type + " successfully.", true);
    }

    public Response C_Thor(String x, String y) {
        int x1 = Integer.parseInt(x);
        int y1 = Integer.parseInt(y);
        if (x1 < 0 || y1 < 0 || x1 >= app.getCurrentGame().getLength() || y1 >= app.getCurrentGame().getWidth()) {
            return new Response("Position out of bound");
        }
        app.getCurrentGame().getVillage().getWeather().thunderBolt(x1, y1);
        return new Response("The tile is burnt to earth", true);
    }

    public Response greenhouseBuild() {
        Farm farm = null;
        for (int i = 0; i < app.getCurrentGame().getVillage().getFarms().size(); i++) {
            farm = app.getCurrentGame().getVillage().getFarms().get(i);
            if (farm.getPlayers() == app.getCurrentGame().getCurrentPlayer()) {
                break;
            }
        }
        if (farm == null) return null;
        GreenHouse greenHouse = farm.getGreenHouse();
        if (greenHouse.isBuilt()) {
            return new Response("Greenhouse already built.");
        }
        switch (greenHouse.areIngredientsAvailable(app.getCurrentGame().getCurrentPlayer())) {
            case 1 -> {
                return new Response("You need 1000 golds to build it");
            }
            case 2 -> {
                return new Response("You need 500 golds to build it");
            }
        }
        return new Response("The greenhouse is built", true);
        //TODO greenhouse initialization
    }

    public Response weatherForecast() {
        return new Response(app.getCurrentGame().getVillage().getTomorrowWeather().toString(), true);
    }

    public Response weather() {
        return new Response(app.getCurrentGame().getVillage().getWeather().toString(), true);
    }

    public Response printMap(String x, String y, String size) {
        int x1 = Integer.parseInt(x);
        int y1 = Integer.parseInt(y);
        if (x1 < 0 || y1 < 0 || x1 >= app.getCurrentGame().getLength() || y1 >= app.getCurrentGame().getWidth()) {
            return new Response("Position out of bound");
        }
        app.getCurrentGame().getVillage().printMap(x1, y1, Integer.parseInt(size));
        return Response.empty();
    }

    public Response walk(String x, String y) {
        int x1 = Integer.parseInt(x);
        int y1 = Integer.parseInt(y);
        if (x1 < 0 || y1 < 0 || x1 >= app.getCurrentGame().getLength() || y1 >= app.getCurrentGame().getWidth()) {
            return new Response("Position out of bound");
        }
        for (Farm farm : app.getCurrentGame().getVillage().getFarms()) {
            if (farm.isPairInFarm(new Pair(x1, y1))) {
                if (farm.getPlayers().contains(app.getCurrentGame().getCurrentPlayer())) break;
                return new Response("You are not allowed to enter this farm");
            }
        }
        WalkingStrategy walkingStrategy = new WalkingStrategy();
        Player player = app.getCurrentGame().getCurrentPlayer();
        int energy = walkingStrategy.calculateEnergy(
                new Pair(player.getTiles().getFirst().getX(),player.getTiles().getFirst().getX()) , new Pair(x1, y1)
        );
        if (energy == -1) return new Response("No path available");
        String confirmation;
        while (true) {
            System.out.println("Energy needed: " + energy + "\nY/n");
            confirmation = ViewRender.getResponse().message();
            if (confirmation.equals("Y")) break;
            if (confirmation.equals("n")) return new Response("You didn't moved");
        }
        if (player.getEnergy() < energy) {
            player.faint();
            return new Response("Not enough energy; you fainted");
        }
        player.removeEnergy(energy);
        player.getTiles().clear();
        player.getTiles().add(app.getCurrentGame().tiles[x1][y1]);
        if (player.getEnergyPerTurn() <= 0) nextTurn();
        return new Response("Moved to the tile.", true);
    }

    public Response helpReadingMap() {
        //TODO
        return Response.empty();
    }

    public Response energyShow() {
        return null;
    }

    public Response C_EnergyLimited() {
        return null;
    }

    public Response inventoryShow() {
        return null;
    }

    public Response toolsShowCurrent() {
        return null;
    }

    public Response toolsShowAvailable() {
        return null;
    }

    public Response C_EnergySet(String value) {
        return null;
    }

    public Response inventoryTrash(String name, String amount) {
        return null;
    }

    public Response toolsEquip(String toolName) {
        return null;
    }

    public Response toolsUpgrade(String toolName) {
        return null;
    }

    public Response toolsUse(String direction) {
        return null;
    }

    public Response craftInfo(String craftName) {
        return null;
    }

    public Response plantSeed(String seed, String direction) {
        return null;
    }

    public Response showPlant(String x, String y) {
        return null;
    }

    public Response fertilize(String fertilizer) {
        return null;
    }

    public Response howmuchWater() {
        return null;
    }

    public Response placeItem(String itemName, String direction) {
        return null;
    }

    public Response C_AddItem(String name, String count) {
        return null;
    }

    public Response pet(String name) {
        return null;
    }

    public Response feedHay(String name) {
        return null;
    }

    public Response collectProduce(String name) {
        return null;
    }

    public Response sellAnimal(String name) {
        return null;
    }

    public Response shepherAnimal(String name, String x, String y) {
        return null;
    }

    public Response animals() {
        return null;
    }

    public Response produces() {
        return null;
    }

    public Response C_SetFriendship(String name, String count) {
        return null;
    }

    public Response artisanUse(String name, String item1, String item2) {
        return null;
    }

    public Response artisanGet(String name) {
        return null;
    }

    public Response C_AddDollars(String count) {
        return null;
    }

    public Response sell(String name, String count) {
        return null;
    }

    public Response friendship() {
        return null;
    }

    public Response talk(String username, String message) {
        return null;
    }

    public Response talkHistory(String username) {
        return null;
    }

    public Response gift(String username, String item, String amount) {
        return null;
    }

    public Response giftList() {
        return null;
    }

    public Response giftRate(String giftNumber, String rate) {
        return null;
    }

    public Response giftHistory(String username) {
        return null;
    }

    public Response hug(String username) {
        return null;
    }

    public Response flower(String username) {
        return null;
    }

    public Response askMarriage(String username, String ring) {
        return null;
    }

    public Response respond(String respond, String username) {
        return null;
    }

    public Response startTrade() {
        return null;
    }

    public Response meetNPC(String npcName) {
        return null;
    }

    public Response giftNPC(String npcName) {
        return null;
    }

    public Response questsFinish(String index) {
        return null;
    }

    public Response questsList() {
        return null;
    }

    public Response friendshipNPCList() {
        return null;
    }

    public Response eat(String foodName) {

        return null;
    }
}
