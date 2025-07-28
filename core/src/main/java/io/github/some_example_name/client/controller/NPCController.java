package io.github.some_example_name.client.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import io.github.some_example_name.client.MainGradle;
import io.github.some_example_name.common.model.relations.NPC;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;
import io.github.some_example_name.client.view.GameView;
import io.github.some_example_name.client.view.mainMenu.DialogMenu;
import io.github.some_example_name.client.view.mainMenu.NPCMenu;

public class NPCController {
    private final WorldController worldController = new WorldController();

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
                    if (collisionWithDialog(npc.getSpriteDialogBox(), worldX, worldY)) {
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
        OrthographicCamera camera = MainGradle.getInstance().getCamera();
        int tileMinX = (int) ((camera.position.x - (camera.viewportWidth / 2)) / App.tileWidth);
        int tileMaxX = (int) ((camera.position.x + (camera.viewportWidth / 2)) / App.tileWidth);
        int tileMinY = (int) ((camera.position.y - (camera.viewportHeight / 2)) / App.tileHeight);
        int tileMaxY = (int) ((camera.position.y + (camera.viewportHeight / 2)) / App.tileHeight);
        App.getInstance().getCurrentGame().getVillage().applyPendingChanges();
        App.getInstance().getCurrentGame().getVillage().forEachStructure(structure -> {
            if (structure instanceof NPC npc) {
                int tileX = npc.getTiles().get(0).getX();
                int tileY = npc.getTiles().get(0).getY();
                npc.setHaveDialog(tileX >= tileMinX && tileX <= tileMaxX &&
                    tileY >= tileMinY && tileY <= tileMaxY);
            }
        });
    }

    private boolean collision(Structure structure, float worldX, float worldY) {
        Sprite sprite = structure.getSprite();
        sprite.setPosition(structure.getTiles().get(0).getX() * App.tileWidth, structure.getTiles().get(0).getY() * App.tileHeight);
        return worldX >= sprite.getX() && worldX <= sprite.getX() + sprite.getWidth() && worldY >= sprite.getY() && worldY <= sprite.getY() + sprite.getHeight();
    }

    private boolean collisionWithDialog(Sprite sprite, float worldX, float worldY) {
        return worldX >= sprite.getX() && worldX <= sprite.getX() + sprite.getWidth() && worldY >= sprite.getY() && worldY <= sprite.getY() + sprite.getHeight();
    }
}
