package io.github.some_example_name.command;

public class TradeCommands extends CommandClass {
    public static final TradeCommands START_TRADE = new TradeCommands("start\\s+trade\\s*");
    public static final TradeCommands TRADE = new TradeCommands("trade\\s+-u\\s+(\\w+)\\s+-t\\s+(request|offer)\\s+-i\\s+(\\w+)\\s+-a\\s+(\\d+)(?:\\s+-p\\s+(\\d+))?(?:\\s+-ti\\s+(.+)\\s+-ta\\s+(\\d+))?");
    public static final TradeCommands TRADE_LIST = new TradeCommands("trade\\s+list");
    public static final TradeCommands TRADE_RESPONSE = new TradeCommands("trade\\s+response\\s+(?<respond>\\b(?:accept|reject)\\b)\\s+-i\\s+(?<id>\\d+)");
    public static final TradeCommands TRADE_HISTORY = new TradeCommands("trade\\s+history");

    TradeCommands(String regex) {
        super(regex);
    }
}
