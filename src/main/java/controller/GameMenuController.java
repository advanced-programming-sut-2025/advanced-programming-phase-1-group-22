package controller;

import model.Farm;
import model.Player;
import model.Result;
import model.Salable;
import model.exception.InvalidInputException;
import model.products.Product;
import model.receipe.Recipe;
import model.shelter.ShippingBin;
import model.structure.Structure;
import utils.App;

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
}