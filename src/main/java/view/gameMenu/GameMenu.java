package view.gameMenu;

import command.GameMenuCommand;
import controller.GameMenuController;
import controller.gameMenu.GameMenuController;
import utils.App;
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
import java.util.regex.Matcher;

public abstract class GameMenu extends Menu {
    private final String exitGame = "^\\s*exit\\s+game\\s*$";
    private final String terminateGame = "^\\s*terminate\\s+game\\s*$";
    private final String nextTurn = "^\\s*next\\s+Turn\\s*$";
    private final String time = "^\\s*time\\s*$";
    private final String date = "^\\s*date\\s*$";
    private final String dateTime = "^\\s*dateTime\\s*$";
    private final String dayOfTheWeek = "^\\s*day\\s+of\\s+the\\s+week\\s*$";
    private final String season = "^\\s*season\\s*$";
    private final String C_AdvanceTime = "^\\s*cheat\\s+advance\\s+time\\s+(?<X>\\d+)h\\s*$";
    private final String C_AdvanceDate = "^\\s*cheat\\s+advance\\s+date\\s+(?<X>\\d+)d\\s*$";
    private final String C_Thor = "^\\s*cheat\\s+thor\\s+(?<X>\\d+)\\s+(?<Y>\\d+)\\s*$";
    private final String weather = "^\\s*weather\\s*$";
    private final String weatherForecast = "^\\s*weather\\s+forecast\\s*$";
    private final String C_WeatherSet = "^\\s*cheat\\s+weather\\s+set\\s+(?<Type>\\S+)\\s*$";
    private final String greenhouseBuild = "^\\s*greenhouse\\s+build\\s*$";
    private final String walk = "^\\s*walk\\s+-l\\s+(?<X>\\d+)\\s*,\\s*(?<Y>\\d+)\\s*$";
    private final String printMap = "^\\s*print\\s+map\\s+-l\\s+(?<X>\\d+)\\s*,\\s*(?<Y>\\d+)\\s+" +
            "-s\\s+(?<size>\\d+)\\s*$";
    private final String helpReadingMap = "^\\s*help\\s+reading\\s+map\\s*$";
    private final String energyShow = "^\\s*energy\\s+show\\s*$";
    private final String C_EnergySet = "^\\s*energy\\s+set\\s+-v\\s+(?<value>\\d+)\\s*$";
    private final String C_EnergyUnlimited = "^\\s*energy\\s+unlimited\\s*$";
    private final String inventoryShow = "^\\s*inventory\\s+show\\s*$";
    private final String inventoryTrash = "^\\s*inventory\\s+trash\\s+-i\\s+(?<name>\\S+)\\s+-n\\s+" +
            "(?<amount>\\d+)\\s*$";
    private final String toolsEquip = "^\\s*tools\\s+equip\\s+(?<toolName>.+)\\s*$";
    private final String toolsShowCurrent = "^\\s*tools\\s+show\\s+current\\s*$";
    private final String toolsShowCAvailable = "^\\s*tools\\s+show\\s+available\\s*$";
    private final String toolsUpgrade = "^\\s*tools\\s+upgrade\\s+(?<toolName>.+)\\s*$";
    private final String toolsUse = "^\\s*tools\\s+use\\s+-d\\s+(?<direction>\\d+)\\s*$";
    private final String craftInfo = "^\\s*craft\\s+info\\s+-n\\s+(?<craftName>.+)\\s*$";
    private final String plantSeed = "^\\s*plant\\s+seed\\s+-s\\s+(?<seed>.+)\\s+-d\\s+(?<direction>\\d+)\\s*$";
    private final String showplant = "^\\s*showplant\\s+-l\\s+(?<X>\\d+)\\s*,\\s*(?<Y>\\d+)\\s*$";
    private final String fertilize = "^\\s*fertilize\\s+-f\\s+(?<fertilizer>.+)\\s+-d\\s+(?<direction>\\d+)\\s*$";
    private final String howMuchWater = "^\\s*howmuch\\s+water\\s*$";
    private final String placeItem = "^\\s*place\\s+item\\s+-n\\s+(?<itemName>.+)\\s+-d\\s+(?<direction>\\d+)\\s*$";
    private final String C_AddItem = "^\\s*cheat\\s+add\\s+item\\s+-n\\s+(?<name>\\S+)\\s+-c\\s+" +
            "(?<count>\\d+)\\s*$";
    private final String pet = "^\\s*pet\\s+-n\\s+(?<name>\\S+)\\s*$";
    private final String feedHay = "^\\s*feed\\s+hay\\s+-n\\s+(?<name>\\S+)\\s*$";
    private final String collectProduce = "^\\s*collect\\s+produce\\s+-n\\s+(?<name>\\S+)\\s*$";
    private final String sellAnimal = "^\\s*sell\\s+animal\\s+-n\\s+(?<name>\\S+)\\s*$";
    private final String shepherdAnimals = "^\\s*shepherd\\s+animals\\s+-n\\s+(?<name>\\S+)\\s+-l\\s+(?<X>\\d+)\\s*" +
            ",\\s*(?<Y>\\d+)\\s*$";
    private final String animals = "^\\s*animals\\s*$";
    private final String produces = "^\\s*produces\\s*$";
    private final String C_SetFriendship = "^\\s*cheat\\s+set\\s+friendship\\s+-n\\s+(?<name>\\S+)\\s+-c\\s+" +
            "(?<count>\\d+)\\s*$";
    private final String artisanUse = "^\\s*artisan\\s+use\\s+(?<name>\\S+)\\s+(?<item1>\\S+)\\s*(?<item2>\\S*)\\s*$"; //TODO Might be incorrect
    private final String artisanGet = "^\\s*artisan\\s+get\\s+(?<name>.+)\\s*$";
    private final String C_AddDollars = "^\\s*cheat\\s+add\\s+(?<count>\\d+)\\s+dollars\\s*$";
    private final String sell = "^\\s*sell\\s+(?<name>.+)\\s+-n\\s+(?<count>\\d+)\\s*$";
    private final String friendship = "^\\s*friendship\\s*$";
    private final String talk = "^\\s*talk\\s+-u\\s+(?<username>.+)\\s+-m\\s+(?<message>.+)\\s*$";
    private final String talkHistory = "^\\s*talk\\s+history\\s+-u\\s+(?<username>.+)\\s*$";
    private final String gift = "^\\s*gift\\s+-u\\s+(?<username>.+)\\s+-i\\s+(?<item>.+)\\s+-a\\s+(?<amount>\\d+)\\s*$";
    private final String giftList = "^\\s*gift\\s+list\\s*$";
    private final String giftRate = "^\\s*gift\\s+rate\\s+-i\\s+(?<giftNumber>.+)\\s+-r\\s+(?<rate>\\d+)\\s*$";
    private final String giftHistory = "^\\s*gift\\s+history\\s+-u\\s+(?<username>.+)\\s*$";
    private final String hug = "^\\s*hug\\s+-u\\s+(?<username>.+)\\s*$";
    private final String flower = "^\\s*flower\\s+-u\\s+(?<username>.+)\\s*$";
    private final String askMarriage = "^\\s*ask\\s+marriage\\s+-u\\s+(?<username>.+)\\s+-r\\s+(?<ring>.+)\\s*$";
    private final String respond = "^\\s*respond\\s+(?<respond>\\b" + "accept|reject)\\s+-u\\s+(?<username>.+)\\s*$";
    private final String startTrade = "^\\s*start\\s+trade\\s*$";
    private final String meetNPC = "^\\s*meet\\s+NPC\\s+(?<npcName>.+)\\s*$";
    private final String giftNPC = "^\\s*gift\\s+NPC\\s+(?<npcName>.+)\\s+-i\\s+(?<item>.+)\\s*$";
    private final String friendshipNPCList = "^\\s*friendship\\s+NPC\\s+list\\s*$";
    private final String questsList = "^\\s*quests\\s+list\\s*$";
    private final String questsFinish = "^\\s*quests\\s+finish\\s+-i\\s+(?<index>\\d+)\\s*$";
    private final String eat = "^\\s*eat\\s+(?<foodName>)\\s*$";


    private final GameMenuController controller = new GameMenuController();

    public boolean check(String input, Scanner scanner) {
        Matcher matcher = null;

        if (App.getInstance().getCurrentGame().getPlayersInFavorTermination() != 0) {
            if (isMatched(input, terminateGame) != null) {
                controller.terminateGame();
            } else if (isMatched(input, "^\\s*no\\s*$") != null) {
                controller.undoTermination();
            } else {
                System.out.println("Do you agree on Termination? Either Enter \"no\" or \"terminate game\"");
            }
        } else if (isMatched(input, exitGame) != null) {
            controller.exitGame();
        } else if (isMatched(input, terminateGame) != null) {
            controller.terminateGame();
        } else if (isMatched(input, nextTurn) != null) {
            controller.nextTurn();
        } else if (isMatched(input, time) != null) {
            controller.time();
        } else if (isMatched(input, date) != null) {
            controller.date();
        } else if (isMatched(input, dateTime) != null) {
            controller.dateTime();
        } else if (isMatched(input, dayOfTheWeek) != null) {
            controller.dayOfTheWeek();
        } else if (isMatched(input, season) != null) {
            controller.season();
        } else if (isMatched(input, weather) != null) {
            controller.weather();
        } else if (isMatched(input, weatherForecast) != null) {
            controller.weatherForecast();
        } else if (isMatched(input, greenhouseBuild) != null) {
            controller.greenhouseBuild();
        } else if (isMatched(input, helpReadingMap) != null) {
            controller.helpReadingMap();
        } else if ((matcher = isMatched(input, C_Thor)) != null) {
            controller.C_Thor(matcher.group("X"), matcher.group("Y"));
        } else if ((matcher = isMatched(input, C_WeatherSet)) != null) {
            controller.C_WeatherSet(matcher.group("Type"));
        } else if ((matcher = isMatched(input, walk)) != null) {
            controller.walk(scanner, matcher.group("X"), matcher.group("Y"));
        } else if ((matcher = isMatched(input, printMap)) != null) {
            controller.printMap(matcher.group("X"), matcher.group("Y"), matcher.group("size"));
        } else if ((matcher = isMatched(input, C_AdvanceTime)) != null) {
            controller.C_AdvanceTime(matcher.group("X"));
        } else if ((matcher = isMatched(input, C_AdvanceDate)) != null) {
            controller.C_AdvanceDate(matcher.group("X"));
        } else if (isMatched(input, energyShow) != null) {
            controller.energyShow();
        } else if (isMatched(input, C_EnergyUnlimited) != null) {
            controller.C_EnergyLimited();
        } else if (isMatched(input, inventoryShow) != null) {
            controller.inventoryShow();
        } else if (isMatched(input, toolsShowCurrent) != null) {
            controller.toolsShowCurrent();
        } else if (isMatched(input, toolsShowCAvailable) != null) {
            controller.toolsShowAvailable();
        } else if ((matcher = isMatched(input, C_EnergySet)) != null) {
            controller.C_EnergySet(matcher.group("value"));
        } else if ((matcher = isMatched(input, inventoryTrash)) != null) {
            controller.inventoryTrash(matcher.group("name"), matcher.group("amount"));
        } else if ((matcher = isMatched(input, toolsEquip)) != null) {
            controller.toolsEquip(matcher.group("toolName"));
        } else if ((matcher = isMatched(input, toolsUpgrade)) != null) {
            controller.toolsUpgrade(matcher.group("toolName"));
        } else if ((matcher = isMatched(input, toolsUse)) != null) {
            controller.toolsUse(matcher.group("direction"));
        } else if ((matcher = isMatched(input, craftInfo)) != null) {
            controller.craftInfo(matcher.group("craftName"));
        } else if ((matcher = isMatched(input, plantSeed)) != null) {
            controller.plantSeed(matcher.group("seed"), matcher.group("direction"));
        } else if ((matcher = isMatched(input, showplant)) != null) {
            controller.showPlant(matcher.group("X"), matcher.group("Y"));
        } else if ((matcher = isMatched(input, fertilize)) != null) {
            controller.fertilize(matcher.group("fertilizer"));
        } else if (isMatched(input, howMuchWater) != null) {
            controller.howmuchWater();
        } else if ((matcher = isMatched(input, placeItem)) != null) {
            controller.placeItem(matcher.group("itemName"), matcher.group("direction"));
        } else if ((matcher = isMatched(input, C_AddItem)) != null) {
            controller.C_AddItem(matcher.group("name"), matcher.group("count"));
        } else if ((matcher = isMatched(input, pet)) != null) {
            controller.pet(matcher.group("name"));
        } else if ((matcher = isMatched(input, feedHay)) != null) {
            controller.feedHay(matcher.group("name"));
        } else if ((matcher = isMatched(input, collectProduce)) != null) {
            controller.collectProduce(matcher.group("name"));
        } else if ((matcher = isMatched(input, sellAnimal)) != null) {
            controller.sellAnimal(matcher.group("name"));
        } else if ((matcher = isMatched(input, shepherdAnimals)) != null) {
            controller.shepherAnimal(matcher.group("name"), matcher.group("X"), matcher.group("Y"));
        } else if (isMatched(input, animals) != null) {
            controller.animals();
        } else if (isMatched(input, produces) != null) {
            controller.produces();
        } else if ((matcher = isMatched(input, C_SetFriendship)) != null) {
            controller.C_SetFriendship(matcher.group("name"), matcher.group("count"));
        } else if ((matcher = isMatched(input, artisanUse)) != null) {
            controller.artisanUse(matcher.group("name"), matcher.group("item1"), matcher.group("item2"));
        } else if ((matcher = isMatched(input, artisanGet)) != null) {
            controller.artisanGet(matcher.group("name"));
        } else if ((matcher = isMatched(input, C_AddDollars)) != null) {
            controller.C_AddDollars(matcher.group("count"));
        } else if ((matcher = isMatched(input, sell)) != null) {
            controller.sell(matcher.group("name"), matcher.group("count"));
        } else if (isMatched(input, friendship) != null) {
            controller.friendship();
        } else if ((matcher = isMatched(input, talk)) != null) {
            controller.talk(matcher.group("username"), matcher.group("message"));
        } else if ((matcher = isMatched(input, talkHistory)) != null) {
            controller.talkHistory(matcher.group("username"));
        } else if ((matcher = isMatched(input, gift)) != null) {
            controller.gift(matcher.group("username"), matcher.group("item"), matcher.group("amount"));
        } else if (isMatched(input, giftList) != null) {
            controller.giftList();
        } else if ((matcher = isMatched(input, giftRate)) != null) {
            controller.giftRate(matcher.group("giftNumber"), matcher.group("rate"));
        } else if ((matcher = isMatched(input, giftHistory)) != null) {
            controller.giftHistory(matcher.group("username"));
        } else if ((matcher = isMatched(input, hug)) != null) {
            controller.hug(matcher.group("username"));
        } else if ((matcher = isMatched(input, flower)) != null) {
            controller.flower(matcher.group("username"));
        } else if ((matcher = isMatched(input, askMarriage)) != null) {
            controller.askMarriage(matcher.group("username"), matcher.group("ring"));
        } else if ((matcher = isMatched(input, respond)) != null) {
            controller.respond(matcher.group("respond"), matcher.group("username"));
        } else if (isMatched(input, startTrade) != null) {
            controller.startTrade();
        } else if ((matcher = isMatched(input, meetNPC)) != null) {
            controller.meetNPC(matcher.group("npcName"));
        } else if ((matcher = isMatched(input, giftNPC)) != null) {
            controller.giftNPC(matcher.group("npcName"));
        } else if ((matcher = isMatched(input, questsFinish)) != null) {
            controller.questsFinish(matcher.group("index"));
        } else if ((matcher = isMatched(input, eat)) != null) {
            controller.eat(matcher.group("foodName"));
        } else if (isMatched(input, questsList) != null) {
            controller.questsList();
        } else if (isMatched(input, friendshipNPCList) != null) {
            controller.friendshipNPCList();
        } else {
            return false;
        }
        return true;
    }

}
