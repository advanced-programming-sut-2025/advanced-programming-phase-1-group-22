package io.github.some_example_name.client.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.common.model.*;
import io.github.some_example_name.common.model.products.HarvestAbleProduct;
import io.github.some_example_name.common.model.relations.Mission;
import io.github.some_example_name.common.model.relations.NPCType;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.common.model.structure.stores.Shop;
import io.github.some_example_name.common.utils.App;

import java.util.*;

public class ClientService {

    public void handleUpdatePosition(String username, int positionX, int positionY, Direction direction) {
        Player player = getPlayerByUsername(username);
        if (player == null) return;
        player.getTiles().clear();
        player.getTiles().add(App.getInstance().getCurrentGame().tiles[positionX][positionY]);
        player.setDirection(direction);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (player.getIsLazy() == 0) {
                    ((AnimatedSprite) player.getSprites().get(0).getSprite()).setLooping(false);
                }
            }
        }, 0.2f);
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

    public void handleDeleteStructure(List<Tile> tiles) {
        Structure structure = getStructureByTile(tiles);
        if (structure == null) return;
        App.getInstance().getCurrentGame().getVillage().removeStructure(structure);
    }

    public void handleCurrentCarrying(Salable salable, String username) {
        Player player = getPlayerByUsername(username);
        if (player == null) return;
        player.setCurrentCarrying(salable);
    }

    public void handleCrowAttack(String farmType, boolean isCrowAttack, HarvestAbleProduct harvestAbleProduct) {
        FarmType farmType1 = FarmType.getFromName(farmType);
        if (farmType1 == null) return;
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            if (farm.getFarmType().equals(farmType1)) {
                farm.setCrowAttackToday(isCrowAttack);
                if (isCrowAttack) farm.setAttackedProduct(harvestAbleProduct);
            }
        }
    }

    public void handleStore(Shop shop, int amount) {
        shop.increaseDailySold(amount);
    }

    public void handleNpcMission(int missionID, String npcName, String username) {
        Player player = getPlayerByUsername(username);
        if (player == null) return;
        for (NPCType value : NPCType.values()) {
            for (Mission mission1 : value.getMissions()) {
                mission1.setRequester(value);
            }
        }
        for (NPCType value : NPCType.values()) {
            for (Mission valueMission : value.getMissions()) {
                if (valueMission.getRequester().getName().equals(npcName)
                    && valueMission.getId() == missionID) {
                    valueMission.setDoer(player);
                    break;
                }
            }
        }
    }

    private Structure getStructureByTile(List<Tile> tiles) {
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            farm.applyPendingChanges();
            List<Structure> structuresCopy = farm.getStructuresSnapshot();
            for (Structure structure : structuresCopy) {
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
        App.getInstance().getCurrentGame().getVillage().applyPendingChanges();
        List<Structure> structuresCopy = App.getInstance().getCurrentGame().getVillage().getStructuresSnapshot();
        for (Structure structure : structuresCopy) {
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

    public Player getPlayerByUsername(String username) {
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

    public void die() {
        Gdx.app.postRunnable(() -> {
            //todo What more should be done?
            MainGradle.getInstance().getScreen().dispose();
            MainGradle.getInstance().initialMenu();
        });
    }
}
