package controller.gameMenu;

import controller.MenuController;
import model.*;
import model.enums.Weather;
import model.exception.InvalidInputException;
import model.structure.farmInitialElements.GreenHouse;
import utils.App;
import view.mainMenu.MainMenu;

import java.sql.Time;

public class GameMenuController extends MenuController {
    Game game = App.getInstance().getCurrentGame();
    public void exitGame() {
        if (game.getCurrentPlayer().getUser() != App.getInstance().getCurrentUser()) {
            throw new InvalidInputException("You are not allowed to exit; the player who has started the game can" +
                    " end it");
        }
        //TODO save game not mine to do
        App.getInstance().setCurrentMenu(Menus.MainMenu);
    }

    public void undoTermination() {
        game.setPlayersInFavorTermination(0);
        System.out.println("Termination unsuccessful!");
    }

    public void terminateGame() {
        game.addTermination();
        if (game.getPlayersInFavorTermination() == game.getPlayers().size()) {
            App.getInstance().getGames().remove(game);
            System.out.println("The game is terminated");
            App.getInstance().setCurrentMenu(Menus.MainMenu);
            for (Player player : game.getPlayers()) {
                player.getUser().setPlaying(false);
            }

            //TODO remove game from databases
        } else {
            System.out.println("Do you agree on Termination? Either Enter \"no\" or \"terminate game\"");
        }
    }

    public void nextTurn() {
        Player player = game.getCurrentPlayer();
        player.setEnergyPerTurn(player.getMaxEnergyPerTurn());
        game.nextPlayer();
        TimeAndDate time = new TimeAndDate(0, 8);
        if (game.getTimeAndDate().compareTime(time) <= 0) {
            player.setEnergy(player.getMaxEnergy()); //TODO implementing the faint
        }
    }

    public void time() {
        System.out.println(game.getTimeAndDate().getHour() + ":"
                + game.getTimeAndDate().getHour());
    }

    public void date() {
        System.out.println(game.getTimeAndDate().getSeason().ordinal()*28 +
                + game.getTimeAndDate().getDay());
    }

    public void dateTime() {
        System.out.println((game.getTimeAndDate()));
    }

    public void dayOfTheWeek() {
        System.out.println(game.getTimeAndDate().getDayOfTheWeek());
    }

    public void C_AdvanceTime(String x) {
        int hours = Integer.parseInt(x);
        for (int i = 0; i < hours * 4; i++) {
            nextTurn();
        }
    }

    public void C_AdvanceDate(String x) {
        int days = Integer.parseInt(x);
        for (int i = 0; i < days * 4 * 13; i++) {
            nextTurn();
        }
    }

    public void season() {
        System.out.println(game.getTimeAndDate().getSeason());
    }

    public void C_WeatherSet(String type) {
        Weather weather = game.getVillage().getWeather();
        try {
            weather = Weather.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Weather not found!");
        }
        game.getVillage().setWeather(weather);
        System.out.println("Weather set to " + type + " successfully.");
    }

    public void C_Thor(String x, String y) {
        int x1 = Integer.parseInt(x);
        int y1 = Integer.parseInt(y);
        if (x1 < 0 || y1 < 0 || x1 >= game.getLength() || y1 >= game.getWidth()) {
            throw new InvalidInputException("Position out of bound");
        }
        game.getVillage().getWeather().thunderBolt(x1, y1);
    }

    public void greenhouseBuild() {
        Farm farm = null;
        for (int i = 0; i < game.getVillage().getFarms().size(); i++) {
            farm = game.getVillage().getFarms().get(i);
            if (farm.getPlayers() == game.getCurrentPlayer()) {
                break;
            }
        }
        if (farm == null) return;
        GreenHouse greenHouse = farm.getGreenHouse();
        if (greenHouse.isBuilt()) {
            throw new InvalidInputException("Greenhouse already built.");
        }
        switch (greenHouse.areIngredientsAvailable(game.getCurrentPlayer())) {
            case 1-> throw new InvalidInputException("You need 1000 golds to build it");
            case 2-> throw new InvalidInputException("You need 500 golds to build it");
        }
        System.out.println("The greenhouse is built");
        //TODO greenhouse initialization
    }

    public void weatherForecast() {
        System.out.println(game.getVillage().getTomorrowWeather());
    }

    public void weather() {
        System.out.println(game.getVillage().getWeather());
    }

    public void printMap(String x, String y, String size) {
    }

    public void walk(String x, String y) {
    }

    public void helpReadingMap() {
    }

    public void energyShow() {
    }

    public void C_EnergyLimited() {
    }

    public void inventoryShow() {
    }

    public void toolsShowCurrent() {
    }

    public void toolsShowAvailable() {
    }

    public void C_EnergySet(String value) {
    }

    public void inventoryTrash(String name, String amount) {
    }

    public void toolsEquip(String toolName) {
    }

    public void toolsUpgrade(String toolName) {
    }

    public void toolsUse(String direction) {
    }

    public void craftInfo(String craftName) {
    }

    public void plantSeed(String seed, String direction) {
    }

    public void showPlant(String x, String y) {
    }

    public void fertilize(String fertilizer) {
    }

    public void howmuchWater() {
    }

    public void placeItem(String itemName, String direction) {
    }

    public void C_AddItem(String name, String count) {
    }

    public void pet(String name) {
    }

    public void feedHay(String name) {
    }

    public void collectProduce(String name) {
    }

    public void sellAnimal(String name) {
    }

    public void shepherAnimal(String name, String x, String y) {
    }

    public void animals() {
    }

    public void produces() {
    }

    public void C_SetFriendship(String name, String count) {
    }

    public void artisanUse(String name, String item1, String item2) {
    }

    public void artisanGet(String name) {
    }

    public void C_AddDollars(String count) {
    }

    public void sell(String name, String count) {
    }

    public void friendship() {
    }

    public void talk(String username, String message) {
    }

    public void talkHistory(String username) {
    }

    public void gift(String username, String item, String amount) {
    }

    public void giftList() {
    }

    public void giftRate(String giftNumber, String rate) {
    }

    public void giftHistory(String username) {
    }

    public void hug(String username) {
    }

    public void flower(String username) {
    }

    public void askMarriage(String username, String ring) {
    }

    public void respond(String respond, String username) {
    }

    public void startTrade() {
    }

    public void meetNPC(String npcName) {
    }

    public void giftNPC(String npcName) {
    }

    public void questsFinish(String index) {
    }

    public void questsList() {
    }

    public void friendshipNPCList() {
    }

    public void eat(String foodName) {

    }
}
