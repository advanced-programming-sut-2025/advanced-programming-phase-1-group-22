package service;

import model.*;
import model.abilitiy.Ability;
import model.animal.Animal;
import model.animal.AnimalType;
import model.craft.CraftType;
import model.enums.Season;
import model.enums.Weather;
import model.exception.InvalidInputException;
import model.products.*;
import model.products.TreesAndFruitsAndSeeds.FruitType;
import model.products.TreesAndFruitsAndSeeds.Tree;
import model.products.TreesAndFruitsAndSeeds.TreeType;
import model.records.Response;
import model.relations.Player;
import model.shelter.FarmBuilding;
import model.source.*;
import model.structure.Structure;
import model.structure.farmInitialElements.GreenHouse;
import model.structure.farmInitialElements.Lake;
import model.structure.stores.*;
import model.tools.*;
import utils.App;
import variables.Session;
import view.Menu;
import view.ViewRender;

import java.util.*;

public class GameService {
	private static GameService instance;
	App app = App.getInstance();


	private GameService() {
	}

	public static GameService getInstance() {
		if (instance==null) {
			instance = new GameService();
		}
		return instance;
	}

	public Response exitGame() {
		if (app.getCurrentGame().getCurrentPlayer().getUser()!=Session.getCurrentUser()) {
			return new Response("You are not allowed to exit; the player who has started the app.getCurrentGame() can" +
					" end it");
		}
		//TODO save app.getCurrentGame() not mine to do
		Session.setCurrentMenu(Menu.MAIN);
		return new Response("Exited from game.", true);
	}

	public Response undoTermination() {
		app.getCurrentGame().setPlayersInFavorTermination(0);
		return new Response("Termination unsuccessful");
	}

	public Response terminateGame() {
		app.getCurrentGame().addTermination();
		if (app.getCurrentGame().getPlayersInFavorTermination()==app.getCurrentGame().getPlayers().size()) {
			App.getInstance().getGames().remove(app.getCurrentGame());
			Session.setCurrentMenu(Menu.MAIN);
			for (Player player : app.getCurrentGame().getPlayers()) {
				player.getUser().setIsPlaying(false);
			}

			//TODO remove app.getCurrentGame() from databases
			return new Response("The game is terminated", true);
		} else {
			return new Response("Do you agree on Termination? Either Enter \"no\" or \"terminate game\"");
		}
	}

	public Response nextTurn() {
		Player player = app.getCurrentGame().getCurrentPlayer();
		player.setEnergyPerTurn(player.getMaxEnergyPerTurn());
		app.getCurrentGame().nextPlayer();
		TimeAndDate time = new TimeAndDate(0, 8);
		if (app.getCurrentGame().getTimeAndDate().compareTime(time) <= 0) {
			player.setEnergy(player.getMaxEnergy()); //TODO implementing the faint
		}
		return new Response("It's next player's turn", true);
	}

	public Response time() {
		String result = app.getCurrentGame().getTimeAndDate().getHour() + ":";
		result += app.getCurrentGame().getTimeAndDate().getHour();
		return new Response(result, true);
	}

	public Response date() {
		Integer days = app.getCurrentGame().getTimeAndDate().getSeason().ordinal() * 28;
		days += app.getCurrentGame().getTimeAndDate().getDay();
		return new Response(days.toString(), true);
	}

	public Response dateTime() {
		return new Response(app.getCurrentGame().getTimeAndDate().toString(), true);
	}

	public Response dayOfTheWeek() {
		return new Response(app.getCurrentGame().getTimeAndDate().getDayOfTheWeek(), true);
	}

	public Response C_AdvanceTime(String x) {
		int hours = Integer.parseInt(x);
		for (int i = 0; i < hours * 4; i++) {
			nextTurn();
		}
		return new Response(x + "hours passed", true);
	}

	public Response C_AdvanceDate(String x) {
		int days = Integer.parseInt(x);
		for (int i = 0; i < days * 4 * 13; i++) {
			nextTurn();
		}
		return new Response(x + "days passed", true);
	}

	public Response season() {
		return new Response(app.getCurrentGame().getTimeAndDate().getSeason().toString(), true);
	}

	public Response C_WeatherSet(String type) {
		Weather weather;
		try {
			weather = Weather.valueOf(type.toUpperCase());
		} catch (IllegalArgumentException e) {
			return new Response("Weather not found!");
		}
		app.getCurrentGame().getVillage().setWeather(weather);
		return new Response("Weather set to " + type + " successfully.", true);
	}

	public Response C_Thor(String x, String y) {
		int x1 = Integer.parseInt(x);
		int y1 = Integer.parseInt(y);
		if (x1 < 0 || y1 < 0 || x1 >= app.getCurrentGame().getLength() || y1 >= app.getCurrentGame().getWidth()) {
			return new Response("Position out of bound");
		}
		app.getCurrentGame().getVillage().getWeather().thunderBolt(x1, y1);
		return new Response("The tile is burnt to earth", true);
	}

	public Response greenhouseBuild() {
		Farm farm = null;
		for (int i = 0; i < app.getCurrentGame().getVillage().getFarms().size(); i++) {
			farm = app.getCurrentGame().getVillage().getFarms().get(i);
			if (farm.getPlayers()==app.getCurrentGame().getCurrentPlayer()) {
				break;
			}
		}
		if (farm==null) return null;
		GreenHouse greenHouse = farm.getGreenHouse();
		if (greenHouse.isBuilt()) {
			return new Response("Greenhouse already built.");
		}
		switch (greenHouse.areIngredientsAvailable(app.getCurrentGame().getCurrentPlayer())) {
			case 1 -> {
				return new Response("You need 1000 golds to build it");
			}
			case 2 -> {
				return new Response("You need 500 golds to build it");
			}
		}
		return new Response("The greenhouse is built", true);
		//TODO greenhouse initialization
	}

	public Response weatherForecast() {
		return new Response(app.getCurrentGame().getVillage().getTomorrowWeather().toString(), true);
	}

	public Response weather() {
		return new Response(app.getCurrentGame().getVillage().getWeather().toString(), true);
	}

	public Response printMap(String x, String y, String size) {
		int x1 = Integer.parseInt(x);
		int y1 = Integer.parseInt(y);
		if (x1 < 0 || y1 < 0 || x1 >= app.getCurrentGame().getLength() || y1 >= app.getCurrentGame().getWidth()) {
			return new Response("Position out of bound");
		}
		app.getCurrentGame().getVillage().printMap(x1, y1, Integer.parseInt(size));
		return Response.empty();
	}

	public Response walk(String x, String y) {
		int x1 = Integer.parseInt(x);
		int y1 = Integer.parseInt(y);
		if (x1 < 0 || y1 < 0 || x1 >= app.getCurrentGame().getLength() || y1 >= app.getCurrentGame().getWidth()) {
			return new Response("Position out of bound");
		}
		for (Farm farm : app.getCurrentGame().getVillage().getFarms()) {
			if (farm.isPairInFarm(new Pair(x1, y1))) {
				if (farm.getPlayers().contains(app.getCurrentGame().getCurrentPlayer())) break;
				return new Response("You are not allowed to enter this farm");
			}
		}
		WalkingStrategy walkingStrategy = new WalkingStrategy();
		Player player = app.getCurrentGame().getCurrentPlayer();
		int energy = walkingStrategy.calculateEnergy(
				new Pair(player.getTiles().getFirst().getX(), player.getTiles().getFirst().getX()), new Pair(x1, y1)
		);
		if (energy==-1) return new Response("No path available");
		String confirmation;
		while (true) {
			System.out.println("Energy needed: " + energy + "\nY/n");
			confirmation = ViewRender.getResponse().message();
			if (confirmation.equals("Y")) break;
			if (confirmation.equals("n")) return new Response("You didn't moved");
		}
		if (player.getEnergy() < energy) {
			player.faint();
			return new Response("Not enough energy; you fainted");
		}
		player.removeEnergy(energy);
		player.getTiles().clear();
		player.getTiles().add(app.getCurrentGame().tiles[x1][y1]);
		if (player.getEnergyPerTurn() <= 0) nextTurn();
		return new Response("Moved to the tile.", true);
	}

	public Response helpReadingMap() {
		//TODO
		return Response.empty();
	}

	public Response showPlayerEnergy() {
		return new Response("player energy : " + getCurrentPlayer().getEnergy(), true);
	}

	public Response setPlayerEnergy(int energy) {
		getCurrentPlayer().setEnergy(energy);
		return Response.empty();
	}

	public Response setPlayerUnlimitedEnergy() {
		getCurrentPlayer().setEnergyIsInfinite(true);
		return Response.empty();
	}

	public Response showPlayerInventory() {
		return new Response(getCurrentPlayer().getInventory().showInventory(), true);
	}

	public Response removeFromPlayerInventory(String itemName, boolean haveItemNumber, int... itemNumbers) {
		Player currentPlayer = getCurrentPlayer();
		Salable currentProduct = getProductFromInventory(currentPlayer, itemName.trim());
		if (currentProduct==null) {
			return new Response("the inventory does not contain this item");
		}
		if (!haveItemNumber) {
			currentPlayer.getInventory().deleteProductFromBackPack(currentProduct, currentPlayer,
					currentPlayer.getInventory().getProducts().get(currentProduct));
			return new Response("you delete " + itemName + " completely", true);
		}
		int itemNumber = Math.min(itemNumbers[0], currentPlayer.getInventory().getProducts().get(currentProduct));
		currentPlayer.getInventory().deleteProductFromBackPack(currentProduct, currentPlayer, itemNumber);
		return new Response(itemName + " of " + itemName + "removed", true);
	}

	public Response toolEquip(String name) {
		Player currentPlayer = getCurrentPlayer();
		Tool currentTool = getToolFromPlayerInventory(name.trim(), currentPlayer);
		if (currentTool==null) {
			return new Response("there is not a tool with this name in inventory");
		}
		currentPlayer.setCurrentCarrying(currentTool);
		return new Response("you carrying " + name + " now", true);
	}

	public Response showCurrentTool() {
		Player currentPlayer = getCurrentPlayer();
		if (currentPlayer.getCurrentCarrying()==null || !(currentPlayer.getCurrentCarrying() instanceof Tool)) {
			return new Response("you do not carry any Tool");
		}
		return new Response("tool name : " + currentPlayer.getCurrentCarrying().getName(), true);
	}

	public Response showAvailableTools() {
		Player currentPlayer = getCurrentPlayer();
		return new Response(makeTokenToShowAvailableTools(currentPlayer), true);
	}

	public Response upgradeTool(String toolName) {
		Player currentPlayer = getCurrentPlayer();
		Tool currentTool = getToolFromPlayerInventory(toolName.trim(), currentPlayer);
		if (currentTool==null) {
			return new Response("there is not a tool with this name in inventory");
		}

		Tool upgradeTool = currentTool.getToolByLevel(currentTool.getLevel() + 1);

		if (upgradeTool==null) {
			return new Response("upgrade is not available");
		}

		if (!isPlayerInStore()) {
			return new Response("you have to be in blackSmith store to upgrade tools");
		}

		BlackSmithUpgrade blackSmithUpgrade = BlackSmithUpgrade.getUpgradeByTool(upgradeTool);

		if (blackSmithUpgrade==null) {
			return new Response("this tool do not have any upgrade in  blackSmith");
		}

		if (!playerHaveEnoughResourceToUpgrade(currentPlayer, blackSmithUpgrade)) {
			return new Response("you do not have enough resource to upgrade tool");
		}
		upgradeTool(currentPlayer, blackSmithUpgrade, currentTool, upgradeTool);
		return new Response("the tool upgrade to " + upgradeTool.getName(), true);
	}

	public Response useTool(String direction) {
		Player currentPlayer = getCurrentPlayer();
		Direction currentDirection = Direction.getByName(direction);
		Tool currentTool = getCurrentTool(currentPlayer);
		if (currentTool==null) {
			return new Response("you do not carrying any tool");
		} else if (currentTool.getEnergy(currentPlayer) > currentPlayer.getEnergy()) {
			return new Response("you do not have enough energy to use this tool");
		}
		Tile currentTile = getTileByXAndY(currentPlayer.getTiles().getFirst().getX() + currentDirection.getXTransmit(),
				currentPlayer.getTiles().getFirst().getY() + currentDirection.getYTransmit());
		if (currentTile==null) {
			return new Response("out of bond");
		}
		return new Response(currentTool.useTool(currentPlayer, currentTile), true);
	}

	public Response pickFromFloor() {
		Player currentPlayer = getCurrentPlayer();
		Tile currentTile = currentPlayer.getTiles().getFirst();
		List<Structure> structures = App.getInstance().getCurrentGame().getVillage().findStructuresByTile(currentTile);
		Structure currentStructure = null;

		for (Structure structure : structures) {
			if (structure.getIsPickable()) {
				currentStructure = structure;
			}
		}
		if (currentStructure==null) {
			return new Response("there is nothing to pick on the floor");
		}
		return new Response(tryToPickUp(currentPlayer, currentStructure, currentTile), true);
	}

	public Response fishing(String name) {
		Player player = getCurrentPlayer();
		Tool currentTool;
		try {
			currentTool = (Tool) player.getInventory().getProductFromBackPack(name.trim());
		} catch (InvalidInputException e) {
			return new Response("you do not carrying fishing pole");
		}
		player.setCurrentCarrying(currentTool);
		if (currentTool.getEnergy(player) > player.getEnergy()) {
			return new Response("you do not have enough energy to use this tool");
		}
		Tile currentTile = isThereAnyLakeAround(player);
		if (currentTile==null) {
			return new Response("you have to be around the lake to fishing");
		}
		return new Response(currentTool.useTool(player, currentTile), true);
	}

	public Response build(String name, int x, int y) {
		Player currentPlayer = getCurrentPlayer();
		CarpenterShopFarmBuildings carpenterShopFarmBuildings = CarpenterShopFarmBuildings.getFromName(name);
		if (carpenterShopFarmBuildings==null) {
			return new Response("there is no farm building with this name");
		}
		if (!isPlayerInStore()) {
			return new Response("you have to be in Carpenter store to build");
		}
		Farm currentFarm = getPlayerMainFarm(currentPlayer);
		if (!thisIsInFarm(currentFarm, x, y)) {
			return new Response("you have to chose a place in your farm");
		} else if (!playerHaveEnoughResourceToBuild(currentPlayer, carpenterShopFarmBuildings)) {
			return new Response("you do not have enough resource to build");
		}
		FarmBuilding farmBuilding = new FarmBuilding(carpenterShopFarmBuildings.getFarmBuildingType());

		return new Response(buildStructureInAPlace(currentPlayer, carpenterShopFarmBuildings,
				currentFarm, farmBuilding, farmBuilding.getFarmBuildingType().getHeight(),
				farmBuilding.getFarmBuildingType().getWidth(), x, y), true);
	}

	public Response buyAnimal(String animalType, String name) {
		Player currentPlayer = getCurrentPlayer();
		MarnieShopAnimal marnieShopAnimal = MarnieShopAnimal.getFromName(animalType);
		if (marnieShopAnimal==null) {
			return new Response("there is no such animal");
		} else if (!isPlayerInStore()) {
			return new Response("you have to be in Marnie Shop Animal to buy animals");
		}
		Farm currentFarm = getPlayerMainFarm(currentPlayer);
		if (!isAnimalNameUnique(currentFarm,name)){
			return new Response("there is a animal with this name");
		}
		else if (!playerHaveEnoughResourceToBuyAnimal(marnieShopAnimal,currentPlayer)){
			return new Response("you do not have enough resource to buy animal");
		}
		Animal animal = new Animal(marnieShopAnimal.getAnimalType());
		animal.setOwner(currentPlayer);
		if (animal.getAnimalType().getIsBarnAnimal()){
			return new Response(addNewBarnAnimal(currentFarm,animal,currentPlayer,marnieShopAnimal));
		}
		return new Response(addNewCoopAnimal(currentFarm,animal,currentPlayer,marnieShopAnimal),true);
	}

	public Response pet(String name){
		Player currentPlayer = getCurrentPlayer();
		Animal currentAnimal = getAnimalAroundPlayer(currentPlayer,name);
		if (currentAnimal == null){
			return new Response("you have to be around animal to pet it");
		}
		else if (!currentPlayer.getAnimals().contains(currentAnimal)){
			return new Response("you only can pet your animals");
		}
		if (!currentAnimal.getPet()){
			currentAnimal.setPet(true);
			int oldFriendShip = currentAnimal.getRelationShipQuality();
			currentAnimal.setRelationShipQuality(oldFriendShip + 15);
			return new Response("you pet " + name,true);
		}
		return new Response("you already pet this animal");
	}

	public Response setFriendShip(String name, int count){
		Player currentPlayer = getCurrentPlayer();
		Animal currrentAnimal = getAnimalWithName(name,currentPlayer);
		if (currrentAnimal == null){
			return new Response("you do not have such animal");
		}
		currrentAnimal.setRelationShipQuality(count);
		return new Response("friendShip upgrade to "+ count,true);
	}

	public Response showAnimals(){
		Player currentPlayer = getCurrentPlayer();
		return new Response(makeStringTokenShowAnimals(currentPlayer),true);
	}

	public Response shepherdAnimals(String name,int x, int y){
		Player currentPlayer = getCurrentPlayer();
		Animal currentAnimal = getAnimalWithName(name,currentPlayer);
		if (currentAnimal == null){
			return new Response("you do not have animal with such name");
		}
		Tile currentTile = getTileByXAndY(x,y);
		if (currentTile == null){
			return new Response("out of bound");
		}
		List<Farm> farms = getPlayerFarms(currentPlayer);
		if (!isPlayerAccessTile(currentTile,farms)){
			return new Response("your animal can not be in that tile!");
		}
		else if (!tileIsAvailableForAnimal(currentTile,farms)){
			return new Response("you can not put your animal in this tile");
		}
		currentAnimal.setTiles(List.of(currentTile));
		if (isAnimalInBarnOrCage(currentAnimal,farms)){
			currentAnimal.setIsAnimalStayOutAllNight(false);
		}
		currentAnimal.setIsAnimalStayOutAllNight(true);
		if (!currentAnimal.getIsFeed()){
			currentAnimal.setIsFeed(true);
			int oldFriendShip = currentAnimal.getRelationShipQuality();
			currentAnimal.setRelationShipQuality(oldFriendShip + 8);
		}
		return new Response("animal shepherd successfully",true);
	}

	public Response feedHay(String name){
		Player currentPlayer = getCurrentPlayer();
		Animal currentAnimal = getAnimalWithName(name,currentPlayer);
		if (currentAnimal == null){
			return new Response("you do not have animal with such name");
		}
		Hay hay = getPlayerHay(currentPlayer);
		if (hay == null){
			return new Response("you do not have any hay");
		}
		if (currentAnimal.getIsFeed()){
			return new Response("animal already eaten");
		}
		currentAnimal.setIsFeed(true);
		int oldFriendShip = currentAnimal.getRelationShipQuality();
		currentAnimal.setRelationShipQuality(oldFriendShip + 8);
		return new Response(currentAnimal.getName() + " eat hay!",true);
	}

	public Response sellAnimal(String name){
		Player currentPlayer = getCurrentPlayer();
		Animal currentAnimal = getAnimalWithName(name,currentPlayer);
		if (currentAnimal == null){
			return new Response("you do not have animal with such name");
		}
		int price = (int) (currentAnimal.getSellPrice() * calculateAnimalCoefficientPrice(currentAnimal));
		int oldGold = currentPlayer.getAccount().getGolds();
		currentPlayer.getAccount().setGolds(oldGold + price);
		currentPlayer.getAnimals().remove(currentAnimal);
		removeAnimalFromVillage(currentAnimal,getPlayerFarms(currentPlayer));
		return new Response("you sell this animal "+ price,true);
	}

	public Response produces(){
		Player currentPlayer = getCurrentPlayer();
		return new Response(makeTokenShowAnimalProduce(currentPlayer),true);
	}

	public Response collectProduce(String name){
		Player currentPlayer = getCurrentPlayer();
		Animal currentAnimal = getAnimalWithName(name,currentPlayer);
		if (currentAnimal == null){
			return new Response("you do not have animal with such name");
		}
		else if (!isPlayerNearAnimal(currentPlayer,currentAnimal)){
			return new Response("you need to be next to animal to collect produce");
		}
		return new Response(collectProduce(currentAnimal,currentPlayer),true);
	}

	public Response craftInfo(String name){
		Harvestable harvestableType = getHarvestableType(name);
		if (harvestableType == null){
			return new Response("there is no harvestable with this name");
		}
		return new Response(harvestableType.craftInfo(),true);
	}

	public Response plantSeed(String name, String direction){
		Player currentPlayer = getCurrentPlayer();
		MixedSeeds mixedSeeds = (MixedSeeds) getProductFromInventory(currentPlayer,name);
		Seed seed;
		if (mixedSeeds != null){
			if (!mixedSeeds.getMixedSeedsType().getSeason().equals(App.getInstance().getCurrentGame().getTimeAndDate().getSeason())){
				return new Response("you should use this seed in " + mixedSeeds.getMixedSeedsType().getSeason());
			}
			seed = generateSeedOfMixedSeed(mixedSeeds.getMixedSeedsType());
		}
		else {
			seed = (Seed) getProductFromInventory(currentPlayer,name);
		}
		if (seed == null){
			return new Response("you do not have this seed in your inventory");
		}
		Direction currentDirection = Direction.getByName(direction);
		Tile currentTile = getTileByXAndY(currentPlayer.getTiles().getFirst().getX() + currentDirection.getXTransmit(),
				currentPlayer.getTiles().getFirst().getY() + currentDirection.getYTransmit());
		if (currentTile == null){
			return new Response("out of bound");
		}
		else if (currentTile.getIsFilled()){
			return new Response("this tile is not available for farming");
		}
		else if (!currentTile.getTileType().equals(TileType.PLOWED)){
			return new Response("you should plow the tile first");
		}
		else if (!seed.getSeedType().getSeason().equals(App.getInstance().getCurrentGame().getTimeAndDate().getSeason()) &&
		!seed.getSeedType().getSeason().equals(Season.SPECIAL)){
			return new Response("you should use this seed in " + seed.getSeedType().getSeason());
		}
		HarvestAbleProduct harvestableProduct = getHarvestableFromSeed(seed.getSeedType());
		if (harvestableProduct == null){
			return new Response("this seed is not valid");
		}
		harvestableProduct.setTiles(List.of(currentTile));
		harvestableProduct.setStartPlanting(App.getInstance().getCurrentGame().getTimeAndDate());
		Farm currentFarm = getPlayerInWitchFarm(currentPlayer);
		if (currentFarm == null){
			return new Response("you should plant in a farm");
		}
		currentFarm.getStructures().add(harvestableProduct);
		return new Response("you plant successfully",true);
	}

	public Response showPlant(int x, int y){
		Player currentPlayer = getCurrentPlayer();
		Tile currentTile = getTileByXAndY(x,y);
		if (currentTile == null){
			return new Response("out of bound");
		}
		if (!isTileInPlayerFarms(getPlayerFarms(currentPlayer),currentTile)){
			return new Response("you can not access other farms");
		}
		HarvestAbleProduct harvestAbleProduct = findHarvestable(currentTile);
		if (harvestAbleProduct == null){
			return new Response("there is no harvestable in this tile");
		}
		return new Response(makeTokenShowHarvestable(harvestAbleProduct),true);
	}

	public Response fertilize(String fertilize,String direction){
		//TODO fertilizing
		Player currentPlayer = getCurrentPlayer();
		Direction currentDirection = Direction.getByName(direction);
		Tile currentTile = getTileByXAndY(currentPlayer.getTiles().getFirst().getX() + currentDirection.getXTransmit(),
				currentPlayer.getTiles().getFirst().getY() + currentDirection.getYTransmit());
		if (currentTile == null){
			return new Response("out of bound");
		}
		HarvestAbleProduct harvestAbleProduct = findHarvestable(currentTile);
		if (harvestAbleProduct == null){
			return new Response("there is no harvestable in this tile");
		}
		harvestAbleProduct.setFertilized(true);
		return new Response("you successfully fertilize " + harvestAbleProduct.getName(),true);
	}

	public Response howMuchWater(){
		Player currentPlayer = getCurrentPlayer();
		Tool currentTool = getCurrentTool(currentPlayer);
		if (currentTool == null || !(currentTool instanceof WateringCan)){
			return new Response("you do not carrying watering can");
		}
		return new Response("remain water: " + ((WateringCan)currentTool).getRemain(),true);
	}

	private Player getCurrentPlayer() {
		return App.getInstance().getCurrentGame().getCurrentPlayer();
	}

	private Salable getProductFromInventory(Player currentPlayer, String itemName) {
		Salable currentProduct = null;
		for (Map.Entry<Salable, Integer> salableIntegerEntry : currentPlayer.getInventory().getProducts().entrySet()) {
			if (salableIntegerEntry.getKey().getName().equals(itemName)) {
				currentProduct = salableIntegerEntry.getKey();
			}
		}

		return currentProduct;
	}

	private Tool getToolFromPlayerInventory(String name, Player player) {
		Tool currentTool = null;
		for (Map.Entry<Salable, Integer> salableIntegerEntry : player.getInventory().getProducts().entrySet()) {
			if (salableIntegerEntry.getKey().getName().equals(name) &&
					salableIntegerEntry.getKey() instanceof Tool) {
				currentTool = (Tool) salableIntegerEntry.getKey();
			}
		}

		return currentTool;
	}

	private String makeTokenToShowAvailableTools(Player player) {
		if (player.getInventory().getProducts().isEmpty()) {
			return "the inventory is empty";
		}
		StringBuilder token = new StringBuilder();
		token.append("tools: " + "\n");
		for (Map.Entry<Salable, Integer> salableIntegerEntry : player.getInventory().getProducts().entrySet()) {
			if (salableIntegerEntry.getKey() instanceof Tool) {
				token.append(salableIntegerEntry.getKey().getName()).append("\n");
			}
		}
		return token.toString();
	}

	private Boolean isPlayerInStore() {
		return false;
	}

	private Boolean playerHaveEnoughResourceToUpgrade(Player player, BlackSmithUpgrade blackSmithUpgrade) {
		if (blackSmithUpgrade.getCost() > player.getAccount().getGolds()) {
			return false;
		}
		for (Map.Entry<Product, Integer> productIntegerEntry : blackSmithUpgrade.getIngredient().entrySet()) {
			if (player.getInventory().getProducts().containsKey(productIntegerEntry.getKey())) {
				if (player.getInventory().getProducts().get(productIntegerEntry.getKey()) < productIntegerEntry.getValue()) {
					return false;
				}
			}
			return false;
		}
		return true;
	}

	private void upgradeTool(Player player, BlackSmithUpgrade blackSmithUpgrade, Tool oldtool, Tool upgradeTool) {
		int oldGold = player.getAccount().getGolds();
		player.getAccount().setGolds(oldGold - blackSmithUpgrade.getCost());

		for (Map.Entry<Product, Integer> productIntegerEntry : blackSmithUpgrade.getIngredient().entrySet()) {
			if (player.getInventory().getProducts().containsKey(productIntegerEntry.getKey())) {
				player.getInventory().deleteProductFromBackPack(productIntegerEntry.getKey(), player, productIntegerEntry.getValue());
			}
		}
		if (oldtool instanceof WateringCan){
			((WateringCan)oldtool).setWateringCanType((WateringCanType) upgradeTool);
		}
		else {
			player.getInventory().getProducts().remove(oldtool);
			player.getInventory().getProducts().put(upgradeTool, 1);
		}
	}

	private Tool getCurrentTool(Player player) {
		if (player.getCurrentCarrying()==null ||
				!(player.getCurrentCarrying() instanceof Tool)) {
			return null;
		}
		return (Tool) player.getCurrentCarrying();
	}

	private Tile getTileByXAndY(int x, int y) {
		for (Tile[] tile : App.getInstance().getCurrentGame().tiles) {
			for (Tile tile1 : tile) {
				if (tile1.getX()==x && tile1.getY()==y) {
					return tile1;
				}
			}
		}
		return null;
	}

	private String tryToPickUp(Player player, Structure structure, Tile tile) {
		if (player.getInventory().isInventoryHaveCapacity((Salable) structure)) {
			player.getInventory().addProductToBackPack((Salable) structure, 1);
			tile.setIsFilled(false);
			App.getInstance().getCurrentGame().getVillage().removeStructure(structure);
			player.upgradeAbility(Ability.FORAGING);
			return "you picked up a " + ((Salable) structure).getName();
		}
		return "your backpack is full";
	}

	private Tile isThereAnyLakeAround(Player player) {
		for (Direction value : Direction.values()) {
			Tile currentTile = getTileByXAndY(player.getTiles().getFirst().getX() + value.getXTransmit(),
					player.getTiles().getFirst().getY() + value.getYTransmit());
			if (currentTile!=null) {
				List<Structure> structures = App.getInstance().getCurrentGame().getVillage().findStructuresByTile(currentTile);
				for (Structure structure : structures) {
					if (structure instanceof Lake) {
						return currentTile;
					}
				}
			}
		}
		return null;
	}

	private Farm getPlayerMainFarm(Player player) {
		for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
			if (farm.getPlayers().getFirst().equals(player)) {
				return farm;
			}
		}
		return null;
	}

	private List<Farm> getPlayerFarms(Player player){
		List<Farm> farms = new ArrayList<>();
		for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
			if (farm.getPlayers().contains(player)){
				farms.add(farm);
			}
		}
		return farms;
	}

	private boolean thisIsInFarm(Farm farm, int x, int y) {
		for (Tile tile : farm.getTiles()) {
			if (tile.getX()==x && tile.getY()==y) {
				return true;
			}
		}
		return false;
	}

	private boolean playerHaveEnoughResourceToBuild(Player player, CarpenterShopFarmBuildings carpenterShopFarmBuildings) {
		if (carpenterShopFarmBuildings.getPrice() > player.getAccount().getGolds()) {
			return false;
		}
		for (Map.Entry<Product, Integer> productIntegerEntry : carpenterShopFarmBuildings.getCost().entrySet()) {
			if (player.getInventory().getProducts().containsKey(productIntegerEntry.getKey())) {
				if (player.getInventory().getProducts().get(productIntegerEntry.getKey()) < productIntegerEntry.getValue()) {
					return false;
				}
			}
			return false;
		}
		return true;
	}

	private String buildStructureInAPlace(Player player, CarpenterShopFarmBuildings carpenterShopFarmBuildings, Farm farm, Structure structure, int length, int width, int x, int y) {
		Tile[][] tiles1 = app.getCurrentGame().tiles;
		List<Tile> tiles2 = new ArrayList<>();
		boolean flag = true;
		for (int j = x; j < x + length; j++) {
			for (int k = y; k < y + width; k++) {
				if (tiles1[j][k].getIsFilled()) {
					flag = false;
				} else {
					tiles1[j][k].setIsFilled(true);
					tiles2.add(tiles1[j][k]);
				}
			}
		}
		if (flag) {
			structure.getTiles().addAll(tiles2);
			farm.getStructures().add(structure);
			payForBuild(carpenterShopFarmBuildings, player);
			return "this building successfully add to your farm";
		}
		tiles2.clear();
		return "this structure can not be here";
	}

	private void payForBuild(CarpenterShopFarmBuildings carpenterShopFarmBuildings, Player player) {
		int oldGold = player.getAccount().getGolds();
		player.getAccount().setGolds(oldGold - carpenterShopFarmBuildings.getPrice());
		for (Map.Entry<Product, Integer> productIntegerEntry : carpenterShopFarmBuildings.getCost().entrySet()) {
			player.getInventory().deleteProductFromBackPack(productIntegerEntry.getKey(), player, productIntegerEntry.getValue());
		}
	}

	private boolean isAnimalNameUnique(Farm farm, String name){
		for (Structure structure : farm.getStructures()) {
			if (structure instanceof Animal){
				if (((Animal)structure).getName().equals(name)){
					return false;
				}
			}
		}
		return true;
	}

	private String addNewBarnAnimal(Farm farm,Animal animal,Player player,
									MarnieShopAnimal marnieShopAnimal) {
		for (Structure structure : farm.getStructures()) {
			if (structure instanceof FarmBuilding) {
				if (((FarmBuilding) structure).getFarmBuildingType().getIsBarn()) {
					if (((FarmBuilding) structure).canAddNewAnimal()) {
						((FarmBuilding)structure).getAnimals().add(animal);
						int oldGold = player.getAccount().getGolds();
						player.getAccount().setGolds(oldGold - marnieShopAnimal.getPrice());
						animal.setTiles(List.of(structure.getTiles().getFirst()));
						player.getAnimals().add(animal);
						farm.getStructures().add(animal);
						return "a/an " + animal.getAnimalType().getName() + " added successfully";
					}

				}
			}
		}
		return "your barn is full";
	}

	private String addNewCoopAnimal(Farm farm,Animal animal,Player player,
									MarnieShopAnimal marnieShopAnimal){
		for (Structure structure : farm.getStructures()) {
			if (structure instanceof FarmBuilding) {
				if (((FarmBuilding) structure).getFarmBuildingType().getIsCoop()) {
					if (((FarmBuilding) structure).canAddNewAnimal()) {
						((FarmBuilding)structure).getAnimals().add(animal);
						int oldGold = player.getAccount().getGolds();
						player.getAccount().setGolds(oldGold - marnieShopAnimal.getPrice());
						animal.setTiles(List.of(structure.getTiles().getFirst()));
						player.getAnimals().add(animal);
						farm.getStructures().add(animal);
						return "a/an " + animal.getAnimalType().getName() + " added successfully";
					}
				}
			}
		}
		return "your coop is full";
	}

	private boolean playerHaveEnoughResourceToBuyAnimal(MarnieShopAnimal marnieShopAnimal,Player player){
		return marnieShopAnimal.getPrice() <= player.getAccount().getGolds();
	}

	private Animal getAnimalAroundPlayer(Player player,String name){
		for (Animal animal : player.getAnimals()) {
			if (animal.getName().equals(name)){
				for (Direction value : Direction.values()) {
					if (player.getTiles().getFirst().getX() + value.getXTransmit() == animal.getTiles().getFirst().getX() &&
							player.getTiles().getFirst().getY() + value.getYTransmit() == animal.getTiles().getFirst().getY()){
						return animal;
					}
				}
			}
		}
		return null;
	}

	private Animal getAnimalWithName(String name, Player player){
		for (Animal animal : player.getAnimals()) {
			if (animal.getName().equals(name)){
				return animal;
			}
		}
		return null;
	}

	private String makeStringTokenShowAnimals(Player player){
		StringBuilder token = new StringBuilder();
		token.append("animals:\n");
		for (Animal animal : player.getAnimals()) {
			token.append("name: ").append(animal.getName()).append("\n");
			token.append("is feed today: ").append(animal.getIsFeed()).append("\n");
			token.append("is pet today: ").append(animal.getPet()).append("\n");
		}
		return token.toString();
	}

	private boolean isPlayerAccessTile(Tile tile,List<Farm> farms){
		for (Farm farm : farms) {
			if (farm.getTiles().contains(tile)){
				return true;
			}
		}
		return false;
	}

	private boolean isAnimalInBarnOrCage(Animal animal,List<Farm> farms){
		for (Farm farm : farms) {
			for (Structure structure : farm.getStructures()) {
				if (structure instanceof FarmBuilding && structure.getTiles().contains(animal.getTiles().getFirst())){
					return true;
				}
			}
		}
		return false;
	}

	private boolean tileIsAvailableForAnimal(Tile tile,List<Farm> farms){
		for (Farm farm : farms) {
			for (Structure structure : farm.getStructures()) {
				if (structure.getTiles().contains(tile)){
					return structure instanceof FarmBuilding &&
							(((FarmBuilding) structure).getFarmBuildingType().getIsCoop() ||
							((FarmBuilding) structure).getFarmBuildingType().getIsBarn());
				}
			}
		}
		return true;
	}

	private Hay getPlayerHay(Player player){
		Hay hay = null;
		for (Map.Entry<Salable, Integer> salableIntegerEntry : player.getInventory().getProducts().entrySet()) {
			if (salableIntegerEntry.getKey() instanceof Hay){
				hay = (Hay) salableIntegerEntry.getKey();
				player.getInventory().deleteProductFromBackPack(hay,player,1);
				break;
			}
		}
		return hay;
	}

	private double calculateAnimalCoefficientPrice(Animal animal){
		int friendShip = animal.getRelationShipQuality();
		return ((double) friendShip / 1000) + 0.3;
	}

	private void removeAnimalFromVillage(Animal animal,List<Farm> farms){
		for (Farm farm : farms) {
			farm.getStructures().remove(animal);
		}
	}

	private String makeTokenShowAnimalProduce(Player player){
		StringBuilder token = new StringBuilder();
		token.append("remain products: \n");
		for (Animal animal : player.getAnimals()) {
			if (animal.getTodayProduct() != null){
				token.append(animal.getName()).append(": \n");
				token.append("    ").append(animal.getTodayProduct().getName()).append(" quality : ").append(animal.getTodayProduct().getProductQuality()).append("\n");
			}
		}
		return token.toString();
	}

	private boolean isPlayerNearAnimal(Player player, Animal animal){
		for (Direction value : Direction.values()) {
			if (player.getTiles().getFirst().getX() + value.getXTransmit() == animal.getTiles().getFirst().getX() &&
					player.getTiles().getFirst().getY() + value.getYTransmit() == animal.getTiles().getFirst().getY()){
				return true;
			}
		}
		return false;
	}

	private String collectProduce(Animal animal,Player player){
		if (animal.getAnimalType().equals(AnimalType.COW) ||
		animal.getAnimalType().equals(AnimalType.GOAT)){
			if (player.getCurrentCarrying() != null && player.getCurrentCarrying() instanceof MilkPail){
				return ((Tool)player.getCurrentCarrying()).useTool(player,animal.getTiles().getFirst());
			}
			return "you do not carrying needed tool";
		}
		if (animal.getAnimalType().equals(AnimalType.SHEEP)) {
			if (player.getCurrentCarrying() != null && player.getCurrentCarrying() instanceof Shear){
				return ((Tool)player.getCurrentCarrying()).useTool(player,animal.getTiles().getFirst());
			}
			return "you do not carrying needed tool";
		}
		if (animal.getAnimalType().equals(AnimalType.PIG)){
			if (!isAnimalInBarnOrCage(animal,getPlayerFarms(player))){
				if (animal.getTodayProduct() != null){
					AnimalProduct animalProduct = animal.getTodayProduct();
					if (player.getInventory().isInventoryHaveCapacity(animalProduct)){
						player.getInventory().addProductToBackPack(animalProduct,1);
						animal.setTodayProduct(null);
						return "you collect produce of " + animal.getName() + ": " + animalProduct.getName() +
								" with quality: " + animalProduct.getProductQuality();
					}
					return "your inventory is full so you can not produce";
				}
				return "this animal do not have produce today";
			}
			return "the pig must be out of barn";
		}
		if (animal.getTodayProduct() != null){
			AnimalProduct animalProduct = animal.getTodayProduct();
			if (player.getInventory().isInventoryHaveCapacity(animalProduct)){
				player.getInventory().addProductToBackPack(animalProduct,1);
				animal.setTodayProduct(null);
				return "you collect produce of " + animal.getName() + ": " + animalProduct.getName() +
						" with quality: " + animalProduct.getProductQuality();
			}
			return "your inventory is full so you can not produce";
		}
		return "this animal do not have produce today";
	}

	private Harvestable getHarvestableType(String name){
		for (FruitType value : FruitType.values()) {
			if (value.getName().equals(name)){
				return value;
			}
		}
		for (CropType value : CropType.values()) {
			if (value.getName().equals(name)){
				return value;
			}
		}
		return null;
	}

	private HarvestAbleProduct getHarvestableFromSeed(SeedType seedType){
		for (TreeType value : TreeType.values()) {
			if (value.getSource() != null && value.getSource().equals(seedType)){
				return new Tree(value);
			}
		}
		for (CropType value : CropType.values()) {
			if (value.getSource() != null && value.getSource().equals(seedType)){
				return new Crop(value);
			}
		}
		return null;
	}

	private Farm getPlayerInWitchFarm(Player player){
		for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
			if (farm.getTiles().contains(player.getTiles().getFirst())){
				return farm;
			}
		}
		return null;
	}

	private HarvestAbleProduct findHarvestable(Tile tile){
		List<Structure> structures = App.getInstance().getCurrentGame().getVillage().findStructuresByTile(tile);
		for (Structure structure : structures) {
			if (structure instanceof HarvestAbleProduct){
				return (HarvestAbleProduct) structure;
			}
		}
		return null;
	}

	private boolean isTileInPlayerFarms(List<Farm> farms,Tile tile){
		for (Farm farm : farms) {
			if (farm.getTiles().contains(tile)){
				return true;
			}
		}
		return false;
	}

	private String makeTokenShowHarvestable(HarvestAbleProduct harvestAbleProduct){
		String token = "";
		token += "Name:" + harvestAbleProduct.getName() + "\n";
		token += "Remain days until can harvest: " + harvestAbleProduct.remainDaysUntilCanHarvest() + "\n";
		token += "Regrowth level: " + harvestAbleProduct.calculateRegrowthLevel() + "\n";
		token += "isWatered today: " + harvestAbleProduct.getIsWaterToday() + "\n";
		token += "isFertilized: " + harvestAbleProduct.getIsFertilized() + "\n";
		return token;
	}

	private Seed generateSeedOfMixedSeed(MixedSeedsType mixedSeedsType){
		Random random = new Random();
		int randNumber = random.nextInt();
		return new Seed(mixedSeedsType.getSeedTypeList().get(randNumber % mixedSeedsType.getSeedTypeList().size()));
	}

	public Response placeItem(String itemName, String direction) {
		return null;
	}

	public Response C_AddItem(String name, String count) {
		return null;
	}

	public Response shepherAnimal(String name, String x, String y) {
		return null;
	}

	public Response animals() {
		return null;
	}

	public Response C_SetFriendship(String name, String count) {
		return null;
	}

	public Response artisanUse(String name, String item1, String item2) {
		return null;
	}

	public Response artisanGet(String name) {
		return null;
	}

	public Response C_AddDollars(String count) {
		return null;
	}

	public Response sell(String name, String count) {
		return null;
	}

	public Response friendship() {
		return null;
	}

	public Response talk(String username, String message) {
		return null;
	}

	public Response talkHistory(String username) {
		return null;
	}

	public Response gift(String username, String item, String amount) {
		return null;
	}

	public Response giftList() {
		return null;
	}

	public Response giftRate(String giftNumber, String rate) {
		return null;
	}

	public Response giftHistory(String username) {
		return null;
	}

	public Response hug(String username) {
		return null;
	}

	public Response flower(String username) {
		return null;
	}

	public Response askMarriage(String username, String ring) {
		return null;
	}

	public Response respond(String respond, String username) {
		return null;
	}

	public Response startTrade() {
		return null;
	}

	public Response meetNPC(String npcName) {
		return null;
	}

	public Response giftNPC(String npcName) {
		return null;
	}

	public Response questsFinish(String index) {
		return null;
	}

	public Response questsList() {
		return null;
	}

	public Response friendshipNPCList() {
		return null;
	}

	public Response eat(String foodName) {

		return null;
	}
}
