package command;

public class GameCommands extends CommandClass {
    public static final GameCommands exitGame = new GameCommands("^\\s*exit\\s+game\\s*$");
    public static final GameCommands terminateGame = new GameCommands("^\\s*terminate\\s+game\\s*$");
    public static final GameCommands nextTurn = new GameCommands("^\\s*next\\s+turn\\s*$");
    public static final GameCommands time = new GameCommands("^\\s*time\\s*$");
    public static final GameCommands date = new GameCommands("^\\s*date\\s*$");
    public static final GameCommands dateTime = new GameCommands("^\\s*dateTime\\s*$");
    public static final GameCommands dayOfTheWeek = new GameCommands("^\\s*day\\s+of\\s+the\\s+week\\s*$");
    public static final GameCommands season = new GameCommands("^\\s*season\\s*$");
    public static final GameCommands C_AdvanceTime = new GameCommands("^\\s*cheat\\s+advance\\s+time\\s+(?<X>\\d+)h\\s*$");
    public static final GameCommands C_AdvanceDate = new GameCommands("^\\s*cheat\\s+advance\\s+date\\s+(?<X>\\d+)d\\s*$");
    public static final GameCommands C_Thor = new GameCommands("^\\s*cheat\\s+thor\\s+(?<X>\\d+)\\s+(?<Y>\\d+)\\s*$");
    public static final GameCommands weather = new GameCommands("^\\s*weather\\s*$");
    public static final GameCommands weatherForecast = new GameCommands("^\\s*weather\\s+forecast\\s*$");
    public static final GameCommands C_WeatherSet = new GameCommands("^\\s*cheat\\s+weather\\s+set\\s+(?<Type>\\S+)\\s*$");
    public static final GameCommands greenhouseBuild = new GameCommands("^\\s*greenhouse\\s+build\\s*$");
    public static final GameCommands walk = new GameCommands("^\\s*walk\\s+-l\\s+(?<X>\\d+)\\s*,\\s*(?<Y>\\d+)\\s*$");
    public static final GameCommands whereAmI = new GameCommands("^\\s*@\\s*$");
    public static final GameCommands printMap = new GameCommands("^\\s*print\\s+map\\s+-l\\s+(?<X>\\d+)\\s*,\\s*(?<Y>\\d+)\\s+" +
            "-s\\s+(?<size>\\d+)\\s*$");
    public static final GameCommands helpReadingMap = new GameCommands("^\\s*help\\s+reading\\s+map\\s*$");
    public static final GameCommands placeItem = new GameCommands("^\\s*place\\s+item\\s+-n\\s+(?<itemName>.+)\\s+-d\\s+(?<direction>\\d+)\\s*$");
    public static final GameCommands C_AddItem = new GameCommands("^\\s*cheat\\s+add\\s+item\\s+-n\\s+(?<name>.+)\\s+-c\\s+" +
            "(?<count>\\d+)\\s*$");

    public static final GameCommands artisanUse = new GameCommands("^\\s*artisan\\s+use\\s+(?<name>\\S+)\\s+(?<item1>\\S+)\\s*(?<item2>\\S*)\\s*$"); //TODO Might be incorrect
    public static final GameCommands artisanGet = new GameCommands("^\\s*artisan\\s+get\\s+(?<name>.+)\\s*$");
    public static final GameCommands C_AddDollars = new GameCommands("^\\s*cheat\\s+add\\s+(?<count>\\d+)\\s+dollars\\s*$");
    public static final GameCommands sell = new GameCommands("^\\s*sell\\s+(?<name>.+)\\s+-n\\s+(?<count>\\d+)\\s*$");
    public static final GameCommands sellAll = new GameCommands("^\\s*sell\\s+(?<name>.+)\\s*$");
    public static final GameCommands friendship = new GameCommands("^\\s*friendship\\s*$");
    public static final GameCommands talk = new GameCommands("^\\s*talk\\s+-u\\s+(?<username>.+)\\s+-m\\s+(?<message>.+)\\s*$");
    public static final GameCommands talkHistory = new GameCommands("^\\s*talk\\s+history\\s+-u\\s+(?<username>.+)\\s*$");
    public static final GameCommands gift = new GameCommands("^\\s*gift\\s+-u\\s+(?<username>.+)\\s+-i\\s+(?<item>.+)\\s+-a\\s+(?<amount>\\d+)\\s*$");
    public static final GameCommands giftList = new GameCommands("^\\s*gift\\s+list\\s*$");
    public static final GameCommands giftRate = new GameCommands("^\\s*gift\\s+rate\\s+-i\\s+(?<giftNumber>.+)\\s+-r\\s+(?<rate>\\d+)\\s*$");
    public static final GameCommands giftHistory = new GameCommands("^\\s*gift\\s+history\\s+-u\\s+(?<username>.+)\\s*$");
    public static final GameCommands hug = new GameCommands("^\\s*hug\\s+-u\\s+(?<username>.+)\\s*$");
    public static final GameCommands flower = new GameCommands("^\\s*flower\\s+-u\\s+(?<username>.+)\\s*$");
    public static final GameCommands askMarriage = new GameCommands("^\\s*ask\\s+marriage\\s+-u\\s+(?<username>.+)\\s+-r\\s+(?<ring>.+)\\s*$");
    public static final GameCommands respond = new GameCommands("^\\s*respond\\s+(?<respond>\\b" + "accept|reject)\\s+-u\\s+(?<username>.+)\\s*$");
    public static final GameCommands startTrade = new GameCommands("^\\s*start\\s+trade\\s*$");
    public static final GameCommands meetNPC = new GameCommands("^\\s*meet\\s+NPC\\s+(?<npcName>.+)\\s*$");
    public static final GameCommands giftNPC = new GameCommands("^\\s*gift\\s+NPC\\s+(?<npcName>.+)\\s+-i\\s+(?<item>.+)\\s*$");
    public static final GameCommands friendshipNPCList = new GameCommands("^\\s*friendship\\s+NPC\\s+list\\s*$");
    public static final GameCommands questsList = new GameCommands("^\\s*quests\\s+list\\s*$");
    public static final GameCommands questsFinish = new GameCommands("^\\s*quests\\s+finish\\s+-i\\s+(?<index>\\d+)\\s*$");
    public static final GameCommands eat = new GameCommands("^\\s*eat\\s+(?<foodName>)\\s*$");

    public static final GameCommands craftingShowRecipes = new GameCommands("crafting show recipes");
    public static final GameCommands craftingCraft = new GameCommands("^\\s*crafting\\s+craft\\s+(?<itemName>.+)\\s*$");
    public static final GameCommands cookingRefrigeratorPut = new GameCommands("^\\s*cooking\\s+refrigerator\\s+put\\s+(?<itemName>.+)\\s*$");
    public static final GameCommands cookingRefrigeratorPick = new GameCommands("^\\s*cooking\\s+refrigerator\\s+pick\\s+(?<itemName>.+)\\s*$");
    public static final GameCommands cookingPrepare = new GameCommands("^\\s*cooking\\s+prepare\\s+(?<recipeName>.+)\\s*$");
    public static final GameCommands cookingShowRecipes = new GameCommands("^\\s*cooking\\s+show\\s+recipes\\s*$");

    public static final GameCommands showAllProducts = new GameCommands("^\\s*show\\s+all\\s+products\\s*$");
    public static final GameCommands showAllAvailableProducts = new GameCommands("^\\s*show\\s+all\\s+available\\s+products\\s*$");
    public static final GameCommands purchase = new GameCommands("^\\s*purchase\\s+(?<name>.+)\\s+-n\\s+(?<count>\\d+)\\s*$");
    public static final GameCommands purchase1 = new GameCommands("^\\s*purchase\\s+(?<name>.+)\\s*$");

    public static final GameCommands SHOW_ENERGY = new GameCommands("show\\s+energy");
    public static final GameCommands ENERGY_SET = new GameCommands("set\\s+energy\\s+-v\\s+(\\d+)");
    public static final GameCommands SET_ENERGY_UNLIMITED = new GameCommands("energy\\s+unlimited");

    public static final GameCommands SHOW_INVENTORY = new GameCommands("inventory\\s+show");
    public static final GameCommands REMOVE_FROM_INVENTORY = new GameCommands("inventory\\s+trash\\s+-i\\s+([a-zA-Z ]+)(\\s+-n\\s*(\\d+))?");
    public static final GameCommands PICK_PRODUCT = new GameCommands("pick\\s+up");

    public static final GameCommands TOOL_EQUIP = new GameCommands("tools\\s+equip\\s+(.+)");
    public static final GameCommands SHOW_CURRENT_TOOL = new GameCommands("tools\\s+show\\s+current");
    public static final GameCommands SHOW_AVAILABLE_TOOLS = new GameCommands("tools\\s+show\\s+available");
    public static final GameCommands UPGRADE_TOOL = new GameCommands("tools\\s+upgrade\\s+([a-zA-Z ]+)");
    public static final GameCommands USE_TOOL = new GameCommands("tools\\s+use\\s+-d\\s+(north|south|west|east|northwest|northeast|southeast|southwest)");

    public static final GameCommands FISHING = new GameCommands("fishing\\s+-p\\s+(.+)");

    public static final GameCommands BUILD_FARM_BUILDING = new GameCommands("build\\s+-a\\s+(.+)\\s+-l\\s+<\\s*(\\d+)\\s*,\\s*(\\d+)\\s*>");
    public static final GameCommands BUY_ANIMAL = new GameCommands("^\\s*buy\\s+animal\\s+-a\\s+(?<animal>.+)\\s+-n\\s+(?<name>.+)\\s*$");
    public static final GameCommands PET = new GameCommands("^\\s*pet\\s+-n\\s+(?<name>.+)\\s*$");
    public static final GameCommands CHEAT_SET_FRIENDSHIP_WITH_ANIMAL = new GameCommands("^\\s*cheat\\s+set\\s+friendship\\s+-n\\s+(?<name>.+)\\s+-c\\s+" +
            "(?<count>\\d+)\\s*$");
    public static final GameCommands SHOW_ANIMALS = new GameCommands("^\\s*animals\\s*$");
    public static final GameCommands FEED_HAY = new GameCommands("^\\s*feed\\s+hay\\s+-n\\s+(?<name>.+)\\s*$");
    public static final GameCommands COLLECT_PRODUCE = new GameCommands("^\\s*collect\\s+produce\\s+-n\\s+(?<name>.+)\\s*$");
    public static final GameCommands SELL_ANIMAL = new GameCommands("^\\s*sell\\s+animal\\s+-n\\s+(?<name>.+)\\s*$");
    public static final GameCommands SHEPHERD_ANIMALS = new GameCommands("^\\s*shepherd\\s+animals\\s+-n\\s+(?<name>.+)\\s+-l\\s+<\\s*(\\d+)\\s*,\\s*(\\d+)\\s*>\\s*$");
    public static final GameCommands PRODUCES = new GameCommands("^\\s*produces\\s*$");

    public static final GameCommands CRAFT_INFO = new GameCommands("^\\s*craft\\s+info\\s+-n\\s+(?<craftName>.+)\\s*$");

    public static final GameCommands PLANT_SEED = new GameCommands("^\\s*plant\\s+-s\\s+(?<seed>.+)\\s+-d\\s+(north|south|west|east|northwest|northeast|southeast|southwest)\\s*$");
    public static final GameCommands SHOW_PLANT = new GameCommands("^\\s*showplant\\s+-l\\s+(?<X>\\d+)\\s*,\\s*(?<Y>\\d+)\\s*$");
    public static final GameCommands FERTILIZE = new GameCommands("^\\s*fertilize\\s+-f\\s+(?<fertilizer>.+)\\s+-d\\s+(north|south|west|east|northwest|northeast|southeast|southwest)\\s*$");
    public static final GameCommands HOW_MUCH_WATER = new GameCommands("^\\s*howmuch\\s+water\\s*$");

    GameCommands(String regex) {
        super(regex);
    }
}
