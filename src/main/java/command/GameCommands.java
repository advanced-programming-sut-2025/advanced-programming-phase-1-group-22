package command;

public class GameCommands extends CommandClass {
    public static final GameCommands exitGame = new GameCommands("^\\s*exit\\s+game\\s*$");
    public static final GameCommands terminateGame = new GameCommands("^\\s*terminate\\s+game\\s*$");
    public static final GameCommands nextTurn = new GameCommands("^\\s*next\\s+Turn\\s*$");
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
    public static final GameCommands printMap = new GameCommands("^\\s*print\\s+map\\s+-l\\s+(?<X>\\d+)\\s*,\\s*(?<Y>\\d+)\\s+" +
            "-s\\s+(?<size>\\d+)\\s*$");
    public static final GameCommands helpReadingMap = new GameCommands("^\\s*help\\s+reading\\s+map\\s*$");
    public static final GameCommands energyShow = new GameCommands("^\\s*energy\\s+show\\s*$");
    public static final GameCommands C_EnergySet = new GameCommands("^\\s*energy\\s+set\\s+-v\\s+(?<value>\\d+)\\s*$");
    public static final GameCommands C_EnergyUnlimited = new GameCommands("^\\s*energy\\s+unlimited\\s*$");
    public static final GameCommands inventoryShow = new GameCommands("^\\s*inventory\\s+show\\s*$");
    public static final GameCommands inventoryTrash = new GameCommands("^\\s*inventory\\s+trash\\s+-i\\s+(?<name>\\S+)\\s+-n\\s+" +
            "(?<amount>\\d+)\\s*$");
    public static final GameCommands toolsEquip = new GameCommands("^\\s*tools\\s+equip\\s+(?<toolName>.+)\\s*$");
    public static final GameCommands toolsShowCurrent = new GameCommands("^\\s*tools\\s+show\\s+current\\s*$");
    public static final GameCommands toolsShowCAvailable = new GameCommands("^\\s*tools\\s+show\\s+available\\s*$");
    public static final GameCommands toolsUpgrade = new GameCommands("^\\s*tools\\s+upgrade\\s+(?<toolName>.+)\\s*$");
    public static final GameCommands toolsUse = new GameCommands("^\\s*tools\\s+use\\s+-d\\s+(?<direction>\\d+)\\s*$");
    public static final GameCommands craftInfo = new GameCommands("^\\s*craft\\s+info\\s+-n\\s+(?<craftName>.+)\\s*$");
    public static final GameCommands plantSeed = new GameCommands("^\\s*plant\\s+seed\\s+-s\\s+(?<seed>.+)\\s+-d\\s+(?<direction>\\d+)\\s*$");
    public static final GameCommands showplant = new GameCommands("^\\s*showplant\\s+-l\\s+(?<X>\\d+)\\s*,\\s*(?<Y>\\d+)\\s*$");
    public static final GameCommands fertilize = new GameCommands("^\\s*fertilize\\s+-f\\s+(?<fertilizer>.+)\\s+-d\\s+(?<direction>\\d+)\\s*$");
    public static final GameCommands howMuchWater = new GameCommands("^\\s*howmuch\\s+water\\s*$");
    public static final GameCommands placeItem = new GameCommands("^\\s*place\\s+item\\s+-n\\s+(?<itemName>.+)\\s+-d\\s+(?<direction>\\d+)\\s*$");
    public static final GameCommands C_AddItem = new GameCommands("^\\s*cheat\\s+add\\s+item\\s+-n\\s+(?<name>\\S+)\\s+-c\\s+" +
            "(?<count>\\d+)\\s*$");
    public static final GameCommands pet = new GameCommands("^\\s*pet\\s+-n\\s+(?<name>\\S+)\\s*$");
    public static final GameCommands feedHay = new GameCommands("^\\s*feed\\s+hay\\s+-n\\s+(?<name>\\S+)\\s*$");
    public static final GameCommands collectProduce = new GameCommands("^\\s*collect\\s+produce\\s+-n\\s+(?<name>\\S+)\\s*$");
    public static final GameCommands sellAnimal = new GameCommands("^\\s*sell\\s+animal\\s+-n\\s+(?<name>\\S+)\\s*$");
    public static final GameCommands shepherdAnimals = new GameCommands("^\\s*shepherd\\s+animals\\s+-n\\s+(?<name>\\S+)\\s+-l\\s+(?<X>\\d+)\\s*" +
            ",\\s*(?<Y>\\d+)\\s*$");
    public static final GameCommands animals = new GameCommands("^\\s*animals\\s*$");
    public static final GameCommands produces = new GameCommands("^\\s*produces\\s*$");
    public static final GameCommands C_SetFriendship = new GameCommands("^\\s*cheat\\s+set\\s+friendship\\s+-n\\s+(?<name>\\S+)\\s+-c\\s+" +
            "(?<count>\\d+)\\s*$");
    public static final GameCommands artisanUse = new GameCommands("^\\s*artisan\\s+use\\s+(?<name>\\S+)\\s+(?<item1>\\S+)\\s*(?<item2>\\S*)\\s*$"); //TODO Might be incorrect
    public static final GameCommands artisanGet = new GameCommands("^\\s*artisan\\s+get\\s+(?<name>.+)\\s*$");
    public static final GameCommands C_AddDollars = new GameCommands("^\\s*cheat\\s+add\\s+(?<count>\\d+)\\s+dollars\\s*$");
    public static final GameCommands sell = new GameCommands("^\\s*sell\\s+(?<name>.+)\\s+-n\\s+(?<count>\\d+)\\s*$");
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

    public static final GameCommands craftingShowRecipes = new GameCommands("^\\s*crafting\\s+show\\s+recipes\\s*$");
    public static final GameCommands craftingCraft = new GameCommands("^\\s*crafting\\s+craft\\s+(?<itemName>.+)\\s*$");
    public static final GameCommands cookingRefrigeratorPut = new GameCommands("^\\s*cooking\\s+refrigerator\\s+put\\s+(?<itemName>.+)\\s*$");
    public static final GameCommands cookingRefrigeratorPick = new GameCommands("^\\s*cooking\\s+refrigerator\\s+pick\\s+(?<itemName>.+)\\s*$");
    public static final GameCommands cookingPrepare = new GameCommands("^\\s*cooking\\s+prepare\\s+(?<recipeName>.+)\\s*$");
    public static final GameCommands cookingShowRecipes = new GameCommands("^\\s*cooking\\s+show\\s+recipes\\s*$");

    public static final GameCommands build = new GameCommands("^\\s*build\\s+-a\\s+(?<buildingName>.+)\\s+-l\\s+(?<X>\\d+)\\s*" +
            ",\\s*(?<Y>\\d+)\\s*$");
    public static final GameCommands buyAnimal = new GameCommands("^\\s*buy\\s+animal\\s+-a\\s+(?<animal>.+)\\s+-n\\s+(?<name>.+)\\s*$");
    public static final GameCommands showAllProducts = new GameCommands("^\\s*show\\s+all\\s+products\\s*$");
    public static final GameCommands purchase = new GameCommands("^\\s*purchase\\s+(?<name>.+)\\s+-n\\s+(?<count>\\d+)\\s*$");
    public static final GameCommands purchase1 = new GameCommands("^\\s*purchase\\s+(?<name>.+)\\s*$");

    public static final GameCommands trade = new GameCommands("^\\s*trade\\s+-u\\s+(?<username>.+)\\s+-t\\s+(?<type>\\boffer|request)\\s+-i\\s+" +
            "(?<item>.+)\\s+-a\\s+(?<amount>\\d+)(\\s+-p\\s+(?<price>\\d+))?(\\s+-ti\\s+(?<targetItem>.+)\\s+-ta\\" +
            "s+(?<targetAmount>\\d+))?\\s*$"); //TODO the possibility to offer target item first and price next
    public static final GameCommands tradeList = new GameCommands("^\\s*trade\\s+list\\s*$");
    public static final GameCommands tradeResponse = new GameCommands("^\\s*trade\\s+respond\\s+(?<respond>\\baccept|reject)\\s+-i\\s+(?<id>\\d+)\\s*$");
    public static final GameCommands tradeHistory = new GameCommands("^\\s*trade\\s+history\\s*$");

    GameCommands(String regex) {
        super(regex);
    }
}
