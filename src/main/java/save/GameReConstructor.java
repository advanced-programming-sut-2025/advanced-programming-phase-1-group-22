package save;

import model.Game;
import model.relations.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameReConstructor {
    private final GameState state;
    private final Map<UUID, Player> playerCache = new HashMap<>();

    public GameReConstructor(GameState state) {
        this.state = state;
    }

    public Game reconstruct() {
        return null;
    }
}
