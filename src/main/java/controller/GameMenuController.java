package controller;

import model.*;
import model.abilitiy.Ability;
import model.exception.InvalidInputException;
import model.products.Product;
import model.receipe.Recipe;
import model.records.Response;
import model.shelter.ShippingBin;
import model.structure.Structure;
import model.structure.stores.BlackSmithUpgrade;
import model.structure.stores.Store;
import model.structure.stores.StoreType;
import model.tools.Tool;
import utils.App;

import javax.security.auth.callback.TextOutputCallback;
import java.lang.module.ResolutionException;
import java.util.List;
import java.util.Map;

public class GameMenuController extends MenuController {
	public Response showPlayerEnergy(){
		return new Response("player energy : " + getCurrentPlayer().getEnergy(),true);
	}

	public void setPlayerEnergy(int energy){
		getCurrentPlayer().setEnergy(energy);
	}

	public void setPlayerUnlimitedEnergy(){
		getCurrentPlayer().setEnergyIsInfinite(true);
	}

	public Response showPlayerInventory(){
		return new Response(getCurrentPlayer().getInventory().showInventory(),true);
	}

	public Response removeFromPlayerInventory(String itemName,boolean haveItemNumber, int... itemNumbers){
		Player currentPlayer = getCurrentPlayer();
		Salable currentProduct = getProductFromInventory(currentPlayer,itemName.trim());
		if (currentProduct == null){
			return new Response("the inventory does not contain this item");
		}
		if (!haveItemNumber){
			currentPlayer.getInventory().deleteProductFromBackPack(currentProduct,currentPlayer,
					currentPlayer.getInventory().getProducts().get(currentProduct));
			return new Response("you delete " + itemName + " completely",true);
		}
		int itemNumber = Math.min(itemNumbers[0],currentPlayer.getInventory().getProducts().get(currentProduct));
		currentPlayer.getInventory().deleteProductFromBackPack(currentProduct,currentPlayer,itemNumber);
		return new Response(itemName + " of " + itemName + "removed",true);
	}

	public Response toolEquip(String name){
		Player currentPlayer = getCurrentPlayer();
		Tool currentTool = getToolFromPlayerInventory(name.trim(),currentPlayer);
		if (currentTool == null){
			return new Response("there is not a tool with this name in inventory");
		}
		currentPlayer.setCurrentCarrying(currentTool);
		return new Response("you carrying " + name + " now",true);
	}

	public Response showCurrentTool(){
		Player currentPlayer = getCurrentPlayer();
		if (currentPlayer.getCurrentCarrying() == null || !(currentPlayer.getCurrentCarrying() instanceof Tool)){
			return new Response("you do not carry any Tool");
		}
		return new Response("tool name : " + currentPlayer.getCurrentCarrying().getName(),true);
	}

	public Response showAvailableTools(){
		Player currentPlayer = getCurrentPlayer();
		return new Response(makeTokenToShowAvailableTools(currentPlayer),true);
	}

	public Response upgradeTool(String toolName){
		Player currentPlayer = getCurrentPlayer();
		Tool currentTool = getToolFromPlayerInventory(toolName.trim(),currentPlayer);
		if (currentTool == null){
			return new Response("there is not a tool with this name in inventory");
		}

		Tool upgradeTool = currentTool.getToolByLevel(currentTool.getLevel() + 1);

		if (upgradeTool == null){
			return new Response("upgrade is not available");
		}

		if (!isPlayerInBlackSmith(currentPlayer)){
			return new Response("you have to be in blackSmith store to upgrade tools");
		}

		BlackSmithUpgrade blackSmithUpgrade = BlackSmithUpgrade.getUpgradeByTool(upgradeTool);

		if (blackSmithUpgrade == null){
			return new Response("this tool do not have any upgrade in  blackSmith");
		}

		if (!playerHaveEnoughResourceToUpgrade(currentPlayer,blackSmithUpgrade)){
			return new Response("you do not have enough resource to upgrade tool");
		}
		upgradeTool(currentPlayer,blackSmithUpgrade,currentTool,upgradeTool);
		return new Response("the tool upgrade to " + upgradeTool.getName(),true);
	}

	public Response useTool(String direction){
		Player currentPlayer = getCurrentPlayer();
		Direction currentDirection = Direction.getByName(direction);
		Tool currentTool = getCurrentTool(currentPlayer);
		if (currentTool == null){
			return new Response("you do not carrying any tool");
		}
		else if (currentTool.getEnergy(currentPlayer) > currentPlayer.getEnergy()){
			return new Response("you do not have enough energy to use this tool");
		}
		Tile currentTile = getTileByXAndY(currentPlayer.getTiles().getFirst().getX() + currentDirection.getXTransmit(),
				currentPlayer.getTiles().getFirst().getY() + currentDirection.getYTransmit());
		if (currentTile == null){
			return new Response("out of bond");
		}
		return new Response(currentTool.useTool(currentPlayer,currentTile),true);
	}

	public Response pickFromFloor(){
		Player currentPlayer = getCurrentPlayer();
		Tile currentTile = currentPlayer.getTiles().getFirst();
		List<Structure> structures = App.getInstance().getCurrentGame().getVillage().findStructuresByTile(currentTile);
		Structure currentStructure = null;

		for (Structure structure : structures) {
			if (structure.getIsPickable()){
				currentStructure = structure;
			}
		}
		if (currentStructure == null){
			return new Response("there is nothing to pick on the floor");
		}
		return new Response(tryToPickUp(currentPlayer,currentStructure,currentTile),true);
	}

	private Player getCurrentPlayer(){
		return App.getInstance().getCurrentGame().getCurrentPlayer();
	}

	private Salable getProductFromInventory(Player currentPlayer, String itemName){
		Salable currentProduct = null;
		for (Map.Entry<Salable, Integer> salableIntegerEntry : currentPlayer.getInventory().getProducts().entrySet()) {
			if (salableIntegerEntry.getKey().getName().equals(itemName)){
				currentProduct = salableIntegerEntry.getKey();
			}
		}

		return currentProduct;
	}

	private Tool getToolFromPlayerInventory(String name, Player player){
		Tool currentTool = null;
		for (Map.Entry<Salable, Integer> salableIntegerEntry : player.getInventory().getProducts().entrySet()) {
			if (salableIntegerEntry.getKey().getName().equals(name) &&
					salableIntegerEntry.getKey() instanceof Tool){
				currentTool = (Tool) salableIntegerEntry.getKey();
			}
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

	private String tryToPickUp(Player player,Structure structure,Tile tile){
		if (player.getInventory().isInventoryHaveCapacity((Salable) structure)){
			player.getInventory().addProductToBackPack((Salable) structure,1);
			tile.setIsFilled(false);
			App.getInstance().getCurrentGame().getVillage().removeStructure(structure);
			player.upgradeAbility(Ability.FORAGING);
			return "you picked up a " + ((Salable) structure).getName();
		}
		return "your backpack is full";
	}
}
