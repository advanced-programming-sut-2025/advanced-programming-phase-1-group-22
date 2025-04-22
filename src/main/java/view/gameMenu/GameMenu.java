package view.gameMenu;

import command.GameMenuCommand;
import controller.GameMenuController;
import view.Menu;

import java.util.Scanner;
import java.util.regex.Matcher;

public class GameMenu {
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
			System.out.println(gameMenuController.showPlayerInventory());
		} else if ((matcher = GameMenuCommand.REMOVE_FROM_INVENTORY.getMatcher(input.trim()))!=null) {
			if (matcher.groupCount()==2) {
				System.out.println(gameMenuController.removeFromPlayerInventory(matcher.group(1),
						true, Integer.parseInt(matcher.group(2))));
			} else {
				System.out.println(gameMenuController.removeFromPlayerInventory(matcher.group(1), false));
			}
		} else if ((matcher = GameMenuCommand.TOOL_EQUIP.getMatcher(input.trim()))!=null) {
			System.out.println(gameMenuController.toolEquip(matcher.group(1)));
		} else if ((matcher = GameMenuCommand.SHOW_CURRENT_TOOL.getMatcher(input.trim()))!=null) {
			System.out.println(gameMenuController.showCurrentTool());
		} else if ((matcher = GameMenuCommand.SHOW_AVAILABLE_TOOLS.getMatcher(input.trim()))!=null) {
			System.out.println(gameMenuController.showAvailableTools());
		} else if ((matcher = GameMenuCommand.UPGRADE_TOOL.getMatcher(input.trim()))!=null) {
			System.out.println(gameMenuController.upgradeTool(matcher.group(1)));
		} else if ((matcher = GameMenuCommand.USE_TOOL.getMatcher(input.trim()))!=null) {
			System.out.println(gameMenuController.useTool(matcher.group(1)));
		} else if ((matcher = GameMenuCommand.PICK_PRODUCT.getMatcher(input.trim()))!=null) {
			System.out.println(gameMenuController.pickFromFloor());
		}
	}
}
