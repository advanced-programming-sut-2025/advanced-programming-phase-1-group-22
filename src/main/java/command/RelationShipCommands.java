package command;

public class RelationShipCommands extends CommandClass {
    public static final RelationShipCommands SHOW_FRIENDSHIPS = new RelationShipCommands("friendships");
    public static final RelationShipCommands TALK_TO_ANOTHER_PLAYER = new RelationShipCommands("talk\\s+-u\\s+(.*)\\s+-m\\s+(.*)");
    public static final RelationShipCommands TALK_HISTORY = new RelationShipCommands("talk\\s+history\\s+-u\\s+(.*)");
    public static final RelationShipCommands GIVE_GIFT = new RelationShipCommands("gift -u (.*) -i (.*) -m (.*)");
    public static final RelationShipCommands GIVE_LIST = new RelationShipCommands("gift\\s+list");
    public static final RelationShipCommands RATE_GIFT = new RelationShipCommands("gift rate -i (.*) -r (.*)");
    public static final RelationShipCommands GIFT_HISTORY = new RelationShipCommands("gift history -u (.*)");
    public static final RelationShipCommands HUG = new RelationShipCommands("hug -u (.*)");
    public static final RelationShipCommands GIVE_FLOWER = new RelationShipCommands("flower -u (.*)");
    public static final RelationShipCommands MARRY = new RelationShipCommands("ask marriage -u (.*) -r (.*)");
    public static final RelationShipCommands RESPOND = new RelationShipCommands("respond (accept|reject) -u (.*)$");
    public static final RelationShipCommands MEET_NPC = new RelationShipCommands("meet\\s+npc\\s+(.*)");
    public static final RelationShipCommands NPC_GIFT = new RelationShipCommands("gift\\s+NPC\\s+(.*)\\s+-i\\s+(.*)");
    public static final RelationShipCommands NPC_FRIENDSHIP = new RelationShipCommands("friendship\\s+NPC\\s+list");
    public static final RelationShipCommands QUESTS_LIST = new RelationShipCommands("quests\\s+list");
    public static final RelationShipCommands DO_MISSION = new RelationShipCommands("quests\\s+finish\\s+-i\\s+(\\d+)");

    RelationShipCommands(String regex) {
        super(regex);
    }
}
