package io.github.some_example_name.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.Tile;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.tools.FishingPole;
import io.github.some_example_name.model.tools.Tool;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.view.GameView;

import java.util.ArrayList;
import java.util.List;

public class ToolController {
    private float animationTime = 0f;
    private boolean isAnimating = false;

    public void update() {
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        Salable carrying = currentPlayer.getCurrentCarrying();
        Tool currentTool;
        if (carrying instanceof Tool) {
            currentTool = (Tool) carrying;
        } else {
            return;
        }

        float baseX = currentPlayer.getTiles().get(0).getX() * App.tileWidth;
        float baseY = currentPlayer.getTiles().get(0).getY() * App.tileHeight;
        float offsetY = 0f;

        if (isAnimating) {
            animationTime += Gdx.graphics.getDeltaTime();

            if (animationTime < 0.1f) {
                offsetY = -40 * (animationTime / 0.1f);
            } else if (animationTime < 0.2f) {
                offsetY = -40 + 40 * ((animationTime - 0.1f) / 0.1f);
            } else {
                isAnimating = false;
            }
        }

        currentTool.getSprite().draw(MainGradle.getInstance().getBatch());
        currentTool.getSprite().setPosition(baseX, baseY + offsetY);

        if (GameView.captureInput) {
            handleInput();
        }
    }

    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.C) || Gdx.input.justTouched()) {
            handleToolUse(GameView.screenX, GameView.screenY);
        }
    }

    public void handleToolRotation(int x, int y) {
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        Salable carrying = currentPlayer.getCurrentCarrying();
        Tool tool;
        if (carrying instanceof Tool) {
            tool = (Tool) carrying;
        } else {
            return;
        }
        Sprite sprite = tool.getSprite();
        sprite.setRotation(calculateRotation(x, y, sprite.getX(), sprite.getY()));
    }

    public void handleToolUse(int x, int y) {
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        Salable carrying = currentPlayer.getCurrentCarrying();
        Tool tool;
        if (carrying instanceof Tool tool1 && !(tool1 instanceof FishingPole)) {
            tool = tool1;
        } else {
            return;
        }
        Vector3 mouse = new Vector3(x, y, 0);
        MainGradle.getInstance().getCamera().unproject(mouse);
        Vector2 mouseWorld = new Vector2(mouse.x, mouse.y);
        int mouseTileX = (int) Math.floor(mouseWorld.x / App.tileWidth);
        int mouseTileY = (int) Math.floor(mouseWorld.y / App.tileHeight);
        int dx = mouseTileX - currentPlayer.getTiles().get(0).getX();
        int dy = mouseTileY - currentPlayer.getTiles().get(0).getY();
        if ((Math.abs(dx) <= 1 && Math.abs(dy) <= 1) && !(dx == 0 && dy == 0)) {
            useTool(currentPlayer, tool, dx, dy);
            animationTime = 0f;
            isAnimating = true;
        }
    }

    private void useTool(Player player, Tool tool, int xTransmit, int yTransmit) {
        WorldController worldController = new WorldController();
        Tile tile = getTileByXAndY(player.getTiles().get(0).getX() + xTransmit, player.getTiles().get(0).getY() + yTransmit);
        if (tile != null) worldController.showResponse(new Response(tool.useTool(player, tile)));
    }

    private float calculateRotation(int x, int y, float objectX, float objectY) {
        Vector3 worldCords = new Vector3(x, y, 0);
        MainGradle.getInstance().getViewport().unproject(worldCords);
        float angle = (float) Math.atan2(worldCords.y - objectY, worldCords.x - objectX);
        return angle * MathUtils.radiansToDegrees;
    }

    private Tile getTileByXAndY(int x, int y) {
        for (Tile[] tile : App.getInstance().getCurrentGame().tiles) {
            for (Tile tile1 : tile) {
                if (tile1.getX() == x && tile1.getY() == y) {
                    return tile1;
                }
            }
        }
        return null;
    }
}
