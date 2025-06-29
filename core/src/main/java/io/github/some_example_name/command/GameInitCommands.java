package io.github.some_example_name.command;

public class GameInitCommands extends CommandClass {
    public static final GameInitCommands newGame = new GameInitCommands("^\\s*game\\s+new\\s+-u(\\s+(.*))?\\s*$");
    public static final GameInitCommands loadGame = new GameInitCommands("^\\s*load\\s+game\\s*$");
    public static final GameInitCommands gameMap = new GameInitCommands("^\\s*game\\s+map\\s+(\\d+)\\s*$");

    GameInitCommands(String regex) {
        super(regex);
    }
}
