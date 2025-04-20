package controller.gameMenu;

import controller.MenuController;
import model.Game;
import model.Menus;
import model.Player;
import model.exception.InvalidInputException;
import utils.App;
import view.mainMenu.MainMenu;

public class GameMenuController extends MenuController {
    public void exitGame() {
        if (App.getInstance().getCurrentGame().getCurrentPlayer().getUser() != App.getInstance().getCurrentUser()) {
            throw new InvalidInputException("You are not allowed to exit; the player who has started the game can" +
                    " end it");
        }
        //TODO save game not mine to do
        App.getInstance().setCurrentMenu(Menus.MainMenu);
    }

    public void undoTermination() {
        Game game = App.getInstance().getCurrentGame();
        game.setPlayersInFavorTermination(0);
        System.out.println("Termination unsuccessful!");
    }

    public void terminateGame() {
        Game game = App.getInstance().getCurrentGame();
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
        App.getInstance().getCurrentGame().nextPlayer();
        //TODO Reset energy, forward the clock
    }

    public void time() {
    }

    public void date() {
    }

    public void dateTime() {
    }

    public void dayOfTheWeek() {
    }

    public void C_AdvanceTime(String x) {
    }

    public void C_AdvanceDate(String x) {
    }

    public void season() {

    }

    public void printMap(String x, String y, String size) {
    }

    public void walk(String x, String y) {
    }

    public void C_WeatherSet(String type) {
    }

    public void C_Thor(String x, String y) {
    }

    public void helpReadingMap() {
    }

    public void greenhouseBuild() {
    }

    public void weatherForecast() {
    }

    public void weather() {
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
