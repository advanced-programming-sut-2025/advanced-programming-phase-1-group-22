package view.gameMenu;

import command.CommandClass;
import controller.gameMenu.RelationShipController;
import model.records.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static command.RelationShipCommands.*;

public class RelationShipMenu extends GameMenu {
    private static RelationShipMenu instance;
    public static RelationShipMenu getInstance() {
        if(instance == null) {
            instance = new RelationShipMenu();
        }
        return instance;
    }

    private RelationShipMenu() {
        commandsFunctionMap.putAll(super.getFunctionsMap());
        commandsFunctionMap.put(SHOW_FRIENDSHIPS,controller::showFriendShips);
        commandsFunctionMap.put(TALK_TO_ANOTHER_PLAYER,controller::talkToAnotherPlayer);
        commandsFunctionMap.put(TALK_HISTORY,controller::showTalkHistory);
        commandsFunctionMap.put(GIVE_GIFT,controller::giveGift);
        commandsFunctionMap.put(GIVE_LIST,controller::showGottedGifts);
        commandsFunctionMap.put(RATE_GIFT,controller::rateGift);
        commandsFunctionMap.put(GIFT_HISTORY,controller::showGiftHistory);
        commandsFunctionMap.put(HUG,controller::hug);
        commandsFunctionMap.put(GIVE_FLOWER,controller::giveFlower);
        commandsFunctionMap.put(MARRY,controller::marry);
        commandsFunctionMap.put(RESPOND,controller::respond);
    }


    private final RelationShipController controller = new RelationShipController();

    private final Map<CommandClass, Function<String[], Response>> commandsFunctionMap = new HashMap<>();

}
