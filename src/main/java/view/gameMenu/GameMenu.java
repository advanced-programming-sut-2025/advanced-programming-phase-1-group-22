package view.gameMenu;

import command.GameMenuCommand;
import controller.GameMenuController;
import view.Menu;

import java.util.Scanner;
import java.util.regex.Matcher;

public class GameMenu extends Menu {
	private final GameMenuController gameMenuController = new GameMenuController();

	Matcher matcher;

	public void check(Scanner scanner) {
		String input = scanner.nextLine();
		if ((matcher = GameMenuCommand.SHOW_ENERGY.getMatcher(input.trim()))!=null) {
			System.out.println(gameMenuController.showPlayerEnergy());
		} else if ((matcher = GameMenuCommand.ENERGY_SET.getMatcher(input.trim()))!=null) {
			gameMenuController.setPlayerEnergy(Integer.parseInt(matcher.group(1)));
		} else if ((matcher = GameMenuCommand.SET_ENERGY_UNLIMITED.getMatcher(input.trim()))!=null) {
			gameMenuController.setPlayerUnlimitedEnergy();
		} else if ((matcher = GameMenuCommand.SHOW_INVENTORY.getMatcher(input.trim()))!=null) {

		}
	}
}
