package io.github.some_example_name.client.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.common.model.relations.NPC;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;
import io.github.some_example_name.client.view.GameView;
import io.github.some_example_name.client.view.mainMenu.DialogMenu;
import io.github.some_example_name.client.view.mainMenu.NPCMenu;

import java.util.Random;

public class NPCController {
    private final WorldController worldController = WorldController.getInstance();

    public void update() {
        if (GameView.captureInput) {
            handleInputs();
        }
        handleDialog();
    }

    private void handleInputs() {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector3 worldCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            MainGradle.getInstance().getCamera().unproject(worldCoords);
            float worldX = worldCoords.x;
            float worldY = worldCoords.y;
            App.getInstance().getCurrentGame().getVillage().applyPendingChanges();
            App.getInstance().getCurrentGame().getVillage().forEachStructure(structure -> {
                if (structure instanceof NPC npc) {
                    if (npc.getSprites().size() > 1 &&
                        collisionWithDialog(npc.getSprites().get(1).getSprite(), worldX, worldY)) {
                        DialogMenu dialogMenu = new DialogMenu(npc);
                        dialogMenu.createMenu(GameView.stage, GameAsset.SKIN, worldController);
                    }
                }
            });
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)){
            Vector3 worldCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            MainGradle.getInstance().getCamera().unproject(worldCoords);
            float worldX = worldCoords.x;
            float worldY = worldCoords.y;
            App.getInstance().getCurrentGame().getVillage().applyPendingChanges();
            App.getInstance().getCurrentGame().getVillage().forEachStructure(structure -> {
                if (structure instanceof NPC npc){
                    if (collision(npc,worldX,worldY)){
                        NPCMenu npcMenu = new NPCMenu(npc);
                        npcMenu.createMenu(GameView.stage,GameAsset.SKIN,worldController);
                    }
                }
            });
        }
    }

    private void handleDialog() {
        App.getInstance().getCurrentGame().getVillage().applyPendingChanges();
        for (Player player : App.getInstance().getCurrentGame().getPlayers()) {
            if (player.getDead()) continue;
            if (player.equals(App.getInstance().getCurrentGame().getCurrentPlayer())) break;
            return;
        }
        Random random = new Random();
        App.getInstance().getCurrentGame().getVillage().forEachStructure(structure -> {
            if (structure instanceof NPC npc && !npc.isHaveDialog() && random.nextInt(20) == 1) {
                GameClient.getInstance().addDialog(npc);
            }
        });
    }

    private boolean collision(Structure structure, float worldX, float worldY) {
        Sprite sprite = structure.getSprites().get(0).getSprite();
        sprite.setPosition(structure.getTiles().get(0).getX() * App.tileWidth, structure.getTiles().get(0).getY() * App.tileHeight);
        return worldX >= sprite.getX() && worldX <= sprite.getX() + sprite.getWidth() && worldY >= sprite.getY() && worldY <= sprite.getY() + sprite.getHeight();
    }

    private boolean collisionWithDialog(Sprite sprite, float worldX, float worldY) {
        return worldX >= sprite.getX() && worldX <= sprite.getX() + sprite.getWidth() && worldY >= sprite.getY() && worldY <= sprite.getY() + sprite.getHeight();
    }
}
