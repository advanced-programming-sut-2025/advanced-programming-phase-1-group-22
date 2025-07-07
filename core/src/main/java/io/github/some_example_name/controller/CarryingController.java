package io.github.some_example_name.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.github.some_example_name.MainGradle;
import io.github.some_example_name.model.Direction;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.Tile;
import io.github.some_example_name.model.gameSundry.Sundry;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.source.MixedSeeds;
import io.github.some_example_name.model.source.Seed;
import io.github.some_example_name.model.tools.Tool;
import io.github.some_example_name.service.GameService;
import io.github.some_example_name.utils.App;
import io.github.some_example_name.view.GameView;

public class CarryingController {
    private final GameService gameService = new GameService();
    private final WorldController worldController = new WorldController();

    public void update(){
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        Salable carrying = currentPlayer.getCurrentCarrying();
        if (carrying != null && !(carrying instanceof Tool)){
            Sprite sprite = null;
            if (carrying.getSprite() == null) {
                if (carrying.getSprites() != null){
                    sprite = carrying.getSprites().get(0).getSprite();
                }
            } else {
                sprite = carrying.getSprite();
            }
            if (sprite != null){
                sprite.draw(MainGradle.getInstance().getBatch());
                sprite.setPosition(currentPlayer.getTiles().get(0).getX() * App.tileWidth,
                    currentPlayer.getTiles().get(0).getY() * App.tileHeight);
            }
            if (GameView.captureInput) {
                handleInput(carrying);
            }
        }
    }

    private void handleInput(Salable carrying) {
        Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        Vector3 mouse = new Vector3(GameView.screenX,GameView.screenY,0);
        MainGradle.getInstance().getCamera().unproject(mouse);
        Vector2 mouseWorld = new Vector2(mouse.x,mouse.y);
        int mouseTileX = (int) Math.floor(mouseWorld.x / App.tileWidth);
        int mouseTileY = (int) Math.floor(mouseWorld.y / App.tileHeight);
        int dx = mouseTileX - currentPlayer.getTiles().get(0).getX();
        int dy = mouseTileY - currentPlayer.getTiles().get(0).getY();

        if (Gdx.input.isKeyJustPressed(Input.Keys.C) || Gdx.input.justTouched()){
            if (carrying instanceof Seed || carrying instanceof MixedSeeds){
                handlePlanting(carrying,currentPlayer, dx,dy);
            } else if (carrying instanceof Sundry && ((Sundry)carrying).getSundryType().getName().contains("Soil")) {
                handleFertilize(carrying, currentPlayer, dx, dy);
            } else if (carrying != null)  {
                if (dy <= 1 && dx <= 1) placeItem(carrying, dx, dy);
            }
        }
    }

    private void placeItem(Salable item, int xTransmit, int yTransmit){
        WorldController worldController = new WorldController();
        GameService gameService = new GameService();
        Direction direction = Direction.getByXAndY(xTransmit, yTransmit);
        if (direction!=null) worldController.showResponse(gameService.placeItem(item, direction));
    }

    private void handleFertilize(Salable carrying,Player player,int dx, int dy){
        if ((Math.abs(dx) <= 1 && Math.abs(dy) <= 1) && !(dx == 0 && dy == 0)){
            fertilize(player,carrying,dx,dy);
        }
    }

    private void fertilize(Player player,Salable carrying,int dx, int dy){
        Tile tile = getTileByXAndY(player.getTiles().get(0).getX() + dx,player.getTiles().get(0).getY() + dy);
        if (tile!=null) worldController.showResponse(gameService.fertilize(carrying.getName(),tile));
    }

    private void handlePlanting(Salable carrying,Player currentPlayer, int dx, int dy){
        if ((Math.abs(dx) <= 1 && Math.abs(dy) <= 1) && !(dx == 0 && dy == 0)){
            plant(currentPlayer,carrying,dx,dy);
        }
    }

    private void plant(Player player,Salable carrying,int dx, int dy){
        Tile tile = getTileByXAndY(player.getTiles().get(0).getX() + dx,player.getTiles().get(0).getY() + dy);
        if (tile!=null) worldController.showResponse(gameService.plantSeed(carrying.getName(),tile));
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
