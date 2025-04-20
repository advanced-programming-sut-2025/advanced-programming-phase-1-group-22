package controller.mainMenu;

import controller.MenuController;

public class MainMenuController extends MenuController {
    private String newGameEmpty = "\\S+";
    private String newGamePlayers1 = "^(?<player1>\\S+)$";
    private String newGamePlayers2 = "^(?<player1>\\S+)\\s+(?<player2>\\S+)$";
    private String newGamePlayers3 = "^(?<player1>\\S+)\\s+(?<player2>\\S+)\\s+(?<player3>\\S+)$";
    private String newGamePlayers4 = "^(?<player1>\\S+)\\s+(?<player2>\\S+)\\s+(?<player3>\\S+)\\s+(?<player4>\\S+)";

    public void newGame(String players) {
    }

    public void loadGame() {
    }
}
