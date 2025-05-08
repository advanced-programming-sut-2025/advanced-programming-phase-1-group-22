package save;

import com.google.gson.Gson;
import model.*;
import model.relations.Friendship;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import lombok.Getter;

//
@Getter
public final class GameState implements Serializable {
    //    private static final long serialVersionUID = 1L;
//
//    // Core Game Data
//    private final VillageData village;
//    private final List<PlayerData> players;
//    private final UUID currentPlayerId;
//    private final List<Friendship> friendships;
//    private final TimeAndDate time;
//    private final double weather;
//    private final int terminationVotes;
//
//    // World State
//    private final TileData[][] tiles;
//    private final List<StructureData> structures;
//
//    /* Nested Data Classes (All Serializable) */
//
//    @Getter
//    public static class VillageData implements Serializable {
//        private final String name;
//        private final int population;
//
//        public VillageData(Village village) {
//            this.name = village.getName();
//            this.population = village.getPopulation();
//        }
//    }
//
//    @Getter
//    public static class PlayerData implements Serializable {
//        private final UUID id;
//        private final String name;
//        private final InventoryData inventory;
//
//        public PlayerData(Player player) {
//            this.id = player.getId();
//            this.name = player.getName();
//            this.inventory = new InventoryData(player.getInventory());
//        }
//    }
//
//    @Getter
//    public static class StructureData implements Serializable {
//        private final String type; // "House", "Farm" etc.
//        private final List<int[]> tilePositions; // [x,y] pairs
//        private final boolean pickable;
//        private final Map<String, Object> properties;
//
//        public StructureData(Structure structure) {
//            this.type = structure.getClass().getSimpleName();
//            this.tilePositions = structure.getTiles().stream()
//                    .map(t -> new int[]{t.getX(), t.getY()})
//                    .toList();
//            this.pickable = structure.isPickable();
//            this.properties = new HashMap<>();
//
//            // Type-specific properties
//            if (structure instanceof House h) {
//                properties.put("owner", h.getOwner().getId());
//                properties.put("capacity", h.getResidentCapacity());
//            }
//            else if (structure instanceof Farm f) {
//                properties.put("crop", f.getCurrentCrop().name());
//                properties.put("growth", f.getGrowthProgress());
//            }
//            // Add other structure types...
//        }
//    }
//
//    @Getter
//    public static class TileData implements Serializable {
//        private final String terrain;
//        private final String item; // Nullable
//
//        public TileData(Tile tile) {
//            this.terrain = tile.getTerrain().name();
//            this.item = tile.getItem() != null ?
//                    tile.getItem().getId().toString() : null;
//        }
//    }
//
//    /* Constructor and Methods */
//
    public GameState(Game game) {
//        this.village = new VillageData(game.getVillage());
//        this.players = game.getPlayers().stream()
//                .map(PlayerData::new)
//                .toList();
//        this.currentPlayerId = game.getCurrentPlayer().getId();
//        this.npcs = game.getNpcs().stream()
//                .map(NPCData::new)
//                .toList();
//        this.friendships = game.getFriendships().stream()
//                .map(FriendshipData::new)
//                .toList();
//        this.time = new TimeData(game.getTimeAndDate());
//        this.weather = game.getWeatherCoefficient();
//        this.terminationVotes = game.getPlayersInFavorTermination();
//
//        // Convert world
//        this.tiles = convertTiles(game.tiles);
//        this.structures = game.getAllStructures().stream()
//                .map(StructureData::new)
//                .toList();
//    }
//
//    private TileData[][] convertTiles(Tile[][] source) {
//        TileData[][] converted = new TileData[source.length][];
//        for (int x = 0; x < source.length; x++) {
//            converted[x] = new TileData[source[x].length];
//            for (int y = 0; y < source[x].length; y++) {
//                converted[x][y] = new TileData(source[x][y]);
//            }
//        }
//        return converted;
    }

    //
//    /* Serialization Helpers */
//
    public String toJson() {
        return new Gson().toJson(this);
    }

    public static GameState fromJson(String json) {
        return new Gson().fromJson(json, GameState.class);
    }
}