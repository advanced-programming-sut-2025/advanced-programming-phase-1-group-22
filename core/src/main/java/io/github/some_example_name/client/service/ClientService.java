package io.github.some_example_name.client.service;

import io.github.some_example_name.common.model.Farm;
import io.github.some_example_name.common.model.Tile;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.common.utils.App;

import java.util.List;
import java.util.Objects;

public class ClientService {

    public void handleUpdatePosition(String username, int positionX, int positionY) {
        Player player = getPlayerByUsername(username);
        if (player == null) return;
        player.getTiles().clear();
        player.getTiles().add(App.getInstance().getCurrentGame().tiles[positionX][positionY]);
    }

    public void updateTileState(Tile tile) {
        for (Tile[] tiles : App.getInstance().getCurrentGame().tiles) {
            for (Tile tile1 : tiles) {
                if (Objects.equals(tile1.getX(), tile.getX()) && Objects.equals(tile1.getY(), tile.getY())) {
                    tile1.updateTileState(tile);
                    break;
                }
            }
        }
    }

    public void handleAddStructure(Structure structure, String username, Boolean inFarm) {
        if (inFarm) {
            Player player = getPlayerByUsername(username);
            Farm farm = getPlayerInWitchFarm(player);
            if (farm == null) return;
            farm.getStructures().add(structure);
        } else {
            App.getInstance().getCurrentGame().getVillage().getStructures().add(structure);
        }
    }

    public void handleDeleteStructure(List<Tile> tiles) {
        Structure structure = getStructureByTile(tiles);
        if (structure == null) return;
        App.getInstance().getCurrentGame().getVillage().removeStructure(structure);
    }

    private Structure getStructureByTile(List<Tile> tiles) {
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            for (Structure structure : farm.getStructures()) {
                if (structure.getTiles().size() == tiles.size()) {
                    boolean found = true;
                    for (int i = 0; i < structure.getTiles().size(); i++) {
                        if (!isTilesEqual(structure.getTiles().get(i), tiles.get(i))) {
                            found = false;
                            break;
                        }
                    }
                    if (found) return structure;
                }
            }
        }
        for (Structure structure : App.getInstance().getCurrentGame().getVillage().getStructures()) {
            if (structure.getTiles().size() == tiles.size()) {
                boolean found = true;
                for (int i = 0; i < structure.getTiles().size(); i++) {
                    if (!isTilesEqual(structure.getTiles().get(i), tiles.get(i))) {
                        found = false;
                        break;
                    }
                }
                if (found) return structure;
            }
        }
        return null;
    }

    private boolean isTilesEqual(Tile tile1, Tile tile2) {
        return Objects.equals(tile1.getX(), tile2.getX()) && Objects.equals(tile1.getY(), tile2.getY());
    }

    private Player getPlayerByUsername(String username) {
        for (Player player : App.getInstance().getCurrentGame().getPlayers()) {
            if (player.getUser().getUsername().equals(username)) {
                return player;
            }
        }
        return null;
    }

    private Farm getPlayerInWitchFarm(Player player) {
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            if (farm.getTiles().contains(player.getTiles().get(0))) {
                return farm;
            }
        }
        return null;
    }

}
