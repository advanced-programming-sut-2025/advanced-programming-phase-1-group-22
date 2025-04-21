package controller;

import model.*;
import model.exception.InvalidInputException;
import model.products.Product;
import model.receipe.Recipe;
import model.shelter.ShippingBin;
import model.structure.Structure;
import model.structure.stores.BlackSmithUpgrade;
import model.structure.stores.Store;
import model.structure.stores.StoreType;
import model.tools.Tool;
import utils.App;

import javax.security.auth.callback.TextOutputCallback;
import java.lang.module.ResolutionException;
import java.util.Map;

public class GameMenuController extends MenuController {
	public Result showPlayerEnergy(){
		return new Result(true,"player energy : " + getCurrentPlayer().getEnergy());
	}

	public void setPlayerEnergy(int energy){
		getCurrentPlayer().setEnergy(energy);
	}

	public void setPlayerUnlimitedEnergy(){
		getCurrentPlayer().setEnergyIsInfinite(true);
	}

	public Result showPlayerInventory(){
		return new Result(true,getCurrentPlayer().getInventory().showInventory());
	}

	public void removeFromPlayerInventory(String itemName,boolean haveItemNumber, int... itemNumbers){
		Player currentPlayer = getCurrentPlayer();
		Salable currentProduct = getProductFromInventory(currentPlayer,itemName.trim());
		if (!haveItemNumber){
			currentPlayer.getInventory().deleteProductFromBackPack(currentProduct,currentPlayer,
					currentPlayer.getInventory().getProducts().get(currentProduct));
		}
		else {
			int itemNumber = Math.min(itemNumbers[0],currentPlayer.getInventory().getProducts().get(currentProduct));
			currentPlayer.getInventory().deleteProductFromBackPack(currentProduct,currentPlayer,itemNumber);
		}
	}

	public void toolEquip(String name){
		Player currentPlayer = getCurrentPlayer();
		Tool currentTool = getToolFromPlayerInventory(name.trim(),currentPlayer);
		currentPlayer.setCurrentCarrying(currentTool);
	}

	public Result showCurrentTool(){
		Player currentPlayer = getCurrentPlayer();
		if (currentPlayer.getCurrentCarrying() == null || !(currentPlayer.getCurrentCarrying() instanceof Tool)){
			return new Result(false,"you do not carry any Tool");
		}
		return new Result(true,"tool name : " + currentPlayer.getCurrentCarrying().getName());
	}

	public Result showAvailableTools(){
		Player currentPlayer = getCurrentPlayer();
		return new Result(true,makeTokenToShowAvailableTools(currentPlayer));
	}

	public Result upgradeTool(String toolName){
		Player currentPlayer = getCurrentPlayer();
		Tool currentTool = getToolFromPlayerInventory(toolName.trim(),currentPlayer);
		Tool upgradeTool = currentTool.getToolByLevel(currentTool.getLevel() + 1);

		if (!isPlayerInBlackSmith(currentPlayer)){
			return new Result(false,"you have to be in blackSmith store to upgrade tools");
		}

		BlackSmithUpgrade blackSmithUpgrade = BlackSmithUpgrade.getUpgradeByTool(upgradeTool);

		if (blackSmithUpgrade == null){
			return new Result(false,"this tool do not have any upgrade");
		}

		if (!playerHaveEnoughResourceToUpgrade(currentPlayer,blackSmithUpgrade)){
			return new Result(false,"you do not have enough resource to upgrade tool");
		}
		upgradeTool(currentPlayer,blackSmithUpgrade,currentTool,upgradeTool);
		return new Result(true,"the tool upgrade to " + upgradeTool.getName());
	}

	public Result useTool(String direction){
		Player currentPlayer = getCurrentPlayer();
		Direction currentDirection = Direction.getByName(direction);
		Tool currentTool = getCurrentTool(currentPlayer);
		if (currentTool == null){
			return new Result(false,"you do not carrying any tool");
		}
		else if (currentTool.getEnergy(currentPlayer) > currentPlayer.getEnergy()){
			return new Result(false,"you do not have enough energy to use this tool");
		}
		Tile currentTile = getTileByXAndY(currentPlayer.getTiles().getFirst().getX() + currentDirection.getXTransmit(),
				currentPlayer.getTiles().getFirst().getY() + currentDirection.getYTransmit());
		if (currentTile == null){
			return new Result(false,"out of bond");
		}
		return new Result(true,currentTool.useTool(currentPlayer,currentTile));
	}

	private Player getCurrentPlayer(){
		Player currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
		if (currentPlayer == null){
			throw new InvalidInputException("there is no player");
		}
		return currentPlayer;
	}

	private Salable getProductFromInventory(Player currentPlayer, String itemName){
		Salable currentProduct = null;
		for (Map.Entry<Salable, Integer> salableIntegerEntry : currentPlayer.getInventory().getProducts().entrySet()) {
			if (salableIntegerEntry.getKey().getName().equals(itemName)){
				currentProduct = salableIntegerEntry.getKey();
			}
		}
		if (currentProduct == null){
			throw new InvalidInputException("the inventory does not contain this item");
		}
		return currentProduct;
	}

	private Tool getToolFromPlayerInventory(String name, Player player){
		Tool currentTool = null;
		for (Map.Entry<Salable, Integer> salableIntegerEntry : player.getInventory().getProducts().entrySet()) {
			if (salableIntegerEntry.getKey().getName().equals(name) && salableIntegerEntry.getKey() instanceof Tool){
				currentTool = (Tool) salableIntegerEntry.getKey();
			}
		}
		if (currentTool == null){
			throw new InvalidInputException("there is not a tool with this name in inventory");
		}
		return currentTool;
	}

	private String makeTokenToShowAvailableTools(Player player){
		if (player.getInventory().getProducts().isEmpty()){
			return "the inventory is empty";
		}
		StringBuilder token = new StringBuilder();
		token.append("tools: " + "\n");
		for (Map.Entry<Salable, Integer> salableIntegerEntry : player.getInventory().getProducts().entrySet()) {
			if (salableIntegerEntry.getKey() instanceof Tool){
				token.append(salableIntegerEntry.getKey().getName()).append("\n");
			}
		}
		return token.toString();
	}

	private Boolean isPlayerInBlackSmith(Player player){
		for (Structure structure : App.getInstance().getCurrentGame().getVillage().getStructures()) {
			if (structure instanceof Store &&
					((Store)structure).getStoreType().equals(StoreType.BLACK_SMITH)){
				for (Tile tile : structure.getTiles()) {
					if (player.getTiles().getFirst().getX() == tile.getX() &&
							player.getTiles().getFirst().getY() == tile.getY()){
						return true;
					}
				}
			}
		}
		return false;
	}

	private Boolean playerHaveEnoughResourceToUpgrade(Player player,BlackSmithUpgrade blackSmithUpgrade){
		if (blackSmithUpgrade.getCost() > player.getAccount().getGolds()){
			return false;
		}
		for (Map.Entry<Product, Integer> productIntegerEntry : blackSmithUpgrade.getIngredient().entrySet()) {
			if (player.getInventory().getProducts().containsKey(productIntegerEntry.getKey())){
				if (player.getInventory().getProducts().get(productIntegerEntry.getKey()) < productIntegerEntry.getValue()){
					return false;
				}
			}
			return false;
		}
		return true;
	}

	private void upgradeTool(Player player,BlackSmithUpgrade blackSmithUpgrade,Tool oldtool,Tool upgradeTool){
		int oldGold = player.getAccount().getGolds();
		player.getAccount().setGolds(oldGold - blackSmithUpgrade.getCost());

		for (Map.Entry<Product, Integer> productIntegerEntry : blackSmithUpgrade.getIngredient().entrySet()) {
			if (player.getInventory().getProducts().containsKey(productIntegerEntry.getKey())){
				player.getInventory().deleteProductFromBackPack(productIntegerEntry.getKey(),player,productIntegerEntry.getValue());
			}
		}

		player.getInventory().getProducts().remove(oldtool);
		player.getInventory().getProducts().put(upgradeTool,1);
	}

	private Tool getCurrentTool(Player player){
		if (player.getCurrentCarrying() == null ||
				!(player.getCurrentCarrying() instanceof Tool)){
			return null;
		}
		return (Tool) player.getCurrentCarrying();
	}

	private Tile getTileByXAndY(int x, int y){
		for (Tile[] tile : App.getInstance().getCurrentGame().tiles) {
			for (Tile tile1 : tile) {
				if (tile1.getX() == x && tile1.getY() == y){
					return tile1;
				}
			}
		}
		return null;
	}
}