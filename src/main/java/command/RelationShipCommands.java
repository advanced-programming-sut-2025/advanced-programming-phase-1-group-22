package command;

public class RelationShipCommands extends CommandClass {
    public static final RelationShipCommands SHOW_FRIENDSHIPS = new RelationShipCommands("friendShips");
    public static final RelationShipCommands TALK_TO_ANOTHER_PLAYER = new RelationShipCommands("talk\\s+-u\\s+(.*)\\s+-m\\s+(.*)");
    public static final RelationShipCommands TALK_HISTORY = new RelationShipCommands("talk\\s+history\\s+-u\\s+(.*)");
    public static final RelationShipCommands GIVE_GIFT = new RelationShipCommands("gift -u (.*) -i (.*) -m (.*)");
    public static final RelationShipCommands GIVE_LIST = new RelationShipCommands("gift list");
    public static final RelationShipCommands RATE_GIFT = new RelationShipCommands("gift rate -i (.*) -r (.*)");
    public static final RelationShipCommands GIFT_HISTORY = new RelationShipCommands("gift history -u (.*)");
    public static final RelationShipCommands HUG = new RelationShipCommands("hug -u (.*)");
    public static final RelationShipCommands GIVE_FLOWER = new RelationShipCommands("flower -u (.*)");
    public static final RelationShipCommands MARRY = new RelationShipCommands("ask marriage -u (.*) -r (.*)");
    public static final RelationShipCommands RESPOND = new RelationShipCommands("respond (accept|reject) -u (.*)$");

    RelationShipCommands(String regex) {
        super(regex);
    }
}
