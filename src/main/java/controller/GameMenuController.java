package controller;

import model.exception.InvalidInputException;
import utils.App;

public class GameMenuController extends MenuController {
	public int showPlayerEnergy(){
		try {
			return App.getInstance().getCurrentGame().getCurrentPlayer().getEnergy();
		} catch (Exception e) {
			throw new InvalidInputException("there is no player");
		}
	}

	public void setPlayerEnergy(int energy){
		try {
			App.getInstance().getCurrentGame().getCurrentPlayer().setEnergy(energy);
		} catch (Exception e) {
			throw new InvalidInputException("there is no player");
		}
	}

	public void setPlayerUnlimitedEnergy(){
		try {
			App.getInstance().getCurrentGame().getCurrentPlayer().setEnergyIsInfinite(true);
		} catch (Exception e) {
			throw new InvalidInputException("there is no player");
		}
	}

	public String showPlayerInventory(){
		try {
			return App.getInstance().getCurrentGame().getCurrentPlayer().getInventory().showInventory();
		} catch (Exception e) {
			throw new InvalidInputException("the inventory is empty");
		}
	}

	public void removeFromPlayerInventory(String itemName){

	}
}