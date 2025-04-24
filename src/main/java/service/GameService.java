package service;

import model.*;
import model.abilitiy.Ability;
import model.animal.AnimalType;
import model.animal.FishType;
import model.cook.Food;
import model.cook.FoodType;
import model.craft.Craft;
import model.craft.CraftType;
import model.enums.Weather;
import model.products.AnimalProductType;
import model.products.Hay;
import model.products.Product;
import model.products.TreesAndFruitsAndSeeds.FruitType;
import model.products.TreesAndFruitsAndSeeds.MadeProduct;
import model.products.TreesAndFruitsAndSeeds.MadeProductType;
import model.receipe.CookingRecipe;
import model.receipe.CraftingRecipe;
import model.records.Response;
import model.relations.Player;
import model.source.CropType;
import model.source.MineralType;
import model.source.MixedSeedsType;
import model.source.SeedType;
import model.structure.Structure;
import model.structure.farmInitialElements.GreenHouse;
import model.structure.stores.BlackSmithUpgrade;
import model.structure.stores.Store;
import model.structure.stores.StoreType;
import model.tools.BackPack;
import model.tools.Tool;
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
        if (instance == null) {
            instance = new GameService();
        }
        return instance;
    }
    public Response exitGame() {
        if (app.getCurrentGame().getCurrentPlayer().getUser() != Session.getCurrentUser()) {
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
        if (app.getCurrentGame().getPlayersInFavorTermination() == app.getCurrentGame().getPlayers().size()) {
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
        if (app.getCurrentGame().getTimeAndDate().compareDailyTime(time) <= 0) {
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
        Integer days = app.getCurrentGame().getTimeAndDate().getSeason().ordinal()*28;
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
            if (farm.getPlayers() == app.getCurrentGame().getCurrentPlayer()) {
                break;
            }
        }
        if (farm == null) return null;
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
                new Pair(player.getTiles().getFirst().getX(),player.getTiles().getFirst().getX()) , new Pair(x1, y1)
        );
        if (energy == -1) return new Response("No path available");
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

    public Response showPlayerEnergy(){
        return new Response("player energy : " + getCurrentPlayer().getEnergy(),true);
    }

    public Response setPlayerEnergy(int energy){
        getCurrentPlayer().setEnergy(energy);
        return Response.empty();
    }

    public Response setPlayerUnlimitedEnergy(){
        getCurrentPlayer().setEnergyIsInfinite(true);
        return Response.empty();
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

    public Response craftInfo(String craftName) {
        return null;
    }

    public Response plantSeed(String seed, String direction) {
        return null;
    }

    public Response showPlant(String x, String y) {
        return null;
    }

    public Response fertilize(String fertilizer) {
        return null;
    }

    public Response howmuchWater() {
        return null;
    }

    public Response placeItem(String itemName, String direction) {
        Player player = app.getCurrentGame().getCurrentPlayer();
        Salable product = player.getInventory().findProductInBackPackByNAme(itemName);
        if (product == null) return new Response(itemName + " not found in your backpack.");
        Direction dir = Direction.getByName(direction);
        if (dir == null) return new Response(direction + " not recognized as a direction.");
        Tile tile = app.getCurrentGame().tiles[player.getTiles().getFirst().getX() + dir.getXTransmit()]
                                              [player.getTiles().getFirst().getY() + dir.getYTransmit()];
        if (tile.getIsFilled()) return new Response("The tile you're trying to put the item on, is filled");
        if (!(product instanceof Structure)) return  new Response(itemName + " Cannot be put on ground"); //TODO Some objects are not structure but must be put on ground
        player.getInventory().deleteProductFromBackPack(product, player, 1);
        ((Structure)product).getTiles().add(tile);
        tile.setIsFilled(true); //TODO not always is filled;
        return new Response(itemName + " is put on the ground.", true);
    }

    public Response C_AddItem(String name, String count) { //TODO conflict of product and productType
        BackPack inventory = app.getCurrentGame().getCurrentPlayer().getInventory();
        Salable salable = null;
        for (FishType value : FishType.values()) {
            if (name.equals(value.getName())) salable = value;
        }
        if (salable == null) {
            for (FoodType value : FoodType.values()) {
                if (name.equals(value.getName())) salable = value;
            }
        }

        if (salable == null) {
            for (CraftType value : CraftType.values()) {
                if (name.equals(value.getName())) salable = value;
            }
        }

        if (salable == null) {
            for (FruitType value : FruitType.values()) {
                if (name.equals(value.getName())) salable = value;
            }
        }

        if (salable == null) {
            for (MadeProductType value : MadeProductType.values()) {
                if (name.equals(value.getName())) salable = value;
            }
        }

        if (salable == null) {
            for (AnimalProductType value : AnimalProductType.values()) {
                if (name.equals(value.getName())) salable = value;
            }
        }

        if (salable == null) {
            for (CropType value : CropType.values()) {
                if (name.equals(value.getName())) salable = value;
            }
        }

        if (salable == null) {
            for (MineralType value : MineralType.values()) {
                if (name.equals(value.getName())) salable = value;
            }
        }

        if (salable == null) {
            for (MixedSeedsType value : MixedSeedsType.values()) {
                if (name.equals(value.getName())) salable = value;
            }
        }

        if (salable == null) {
            for (SeedType value : SeedType.values()) {
                if (name.equals(value.getName())) salable = value;
            }
        }

        if (salable == null) {
            if (name.equals("hay")) salable = new Hay();
        }
        if (salable == null) {
            if (name.equals("flower")) salable = new Flower();
        }

        if (salable == null) return new Response(name + " cannot be added to the backpack");
        if (inventory.isInventoryHaveCapacity(salable)) return new Response("Backpack hasn't enough space");
        inventory.addProductToBackPack(salable, Integer.parseInt(count));
        return new Response(name + " *" + count + " added to backpack.", true);
    }

    public Response pet(String name) {
        return null;
    }

    public Response feedHay(String name) {
        return null;
    }

    public Response collectProduce(String name) {
        return null;
    }

    public Response sellAnimal(String name) {
        return null;
    }

    public Response shepherAnimal(String name, String x, String y) {
        return null;
    }

    public Response animals() {
        return null;
    }

    public Response produces() {
        return null;
    }

    public Response C_SetFriendship(String name, String count) {
        return null;
    }

    public Response artisanUse(String name, String item1, String item2) {
        Player player = app.getCurrentGame().getCurrentPlayer();
        Salable craft = player.getInventory().findProductInBackPackByNAme(name);
        if (!(craft instanceof CraftType)) {
            return new Response("No artisan called " + name + " found in your backpack.");
        }
        Salable product1 = null, product2 = null;
        for (Salable value : player.getInventory().getProducts().keySet()) {
            if (item1.equals(value.getName())) {
                product1 = value;
                break;
            }
        }
        if (product1 == null) return new Response(item1 + " not found in your backpack.");
        if (item2 != null && !item2.isEmpty()) {
            for (Salable value : player.getInventory().getProducts().keySet()) {
                if (item2.equals(value.getName())) {
                    product2 = value;
                    break;
                }
            }
            if (product2 == null) return new Response(item2 + " not found in your backpack.");
        }
        MadeProductType madeProductType = MadeProductType.findByCraft((CraftType) craft);
        if (madeProductType == null) return new Response(name + "is not a valid artisan.");
        Response isArtisanValid = madeProductType.isIngredientsValid((Product)product1, player.getInventory().countProductFromBackPack(product1),
                product2 != null);
        if (!isArtisanValid.shouldBeBack()) return isArtisanValid;
        player.getInventory().deleteProductFromBackPack(product1, player, madeProductType.countIngredient());
        if (product2 != null) player.getInventory().deleteProductFromBackPack(MadeProductType.COAL, player, 1);
        player.addCraft(new Craft(madeProductType.getCraftType(), new MadeProduct(madeProductType, product1), madeProductType.calcETA(product1)));
        return new Response("The item will be ready in due time.");
    }

    public Response artisanGet(String name) {
        Player player = app.getCurrentGame().getCurrentPlayer();
        Salable craftType = player.getInventory().findProductInBackPackByNAme(name);
        if (!(craftType instanceof CraftType)) {
            return new Response("No artisan called " + name + " found in your backpack.");
        }
        Craft craft = player.findCraft(craftType);
        if (craft == null) return new Response("You have not started an artisan of type " + name);
        if (!player.getInventory().isInventoryHaveCapacity(craft)) {
            return new Response("Backpack is full.");
        }
        if (craft.getETA().compareTime(app.getCurrentGame().getTimeAndDate()) > 0) {
            return new Response("Still not ready!");
        }
        player.getInventory().addProductToBackPack(craft, 1);
        player.getCrafts().remove(craft);
        return new Response("The artisan collected", true);
    }

    public Response C_AddDollars(String count) {
        app.getCurrentGame().getCurrentPlayer().getAccount().removeGolds(-Integer.valueOf(count));
        return new Response(count + "$ added to your account.", true);
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
        Player player = app.getCurrentGame().getCurrentPlayer();
        Salable food = player.getInventory().findProductInBackPackByNAme(foodName);
        if (food == null) return new Response(foodName + " not found in your backpack.");
        if (((Product)food).getEnergy() == 0) return new Response(foodName + " is not edible");
        player.getInventory().deleteProductFromBackPack(food, player, 1);
        player.changeEnergy(((Product)food).getEnergy());
        if (food instanceof FoodType) player.setBuff((Buff)((FoodType)food).getBuff().clone());
        return new Response(foodName + " is eaten now");
    }

    public Response craftingShowRecipes() {
        Player player = app.getCurrentGame().getCurrentPlayer();
        List<CraftingRecipe> craftingRecipeList = player.getCraftingRecipes();
        if (craftingRecipeList.isEmpty()) return new Response("No recipe found you loser.");
        StringBuilder response = new StringBuilder("Crafting recipes you've learnt so far: \n");
        for (CraftingRecipe craftingRecipe : craftingRecipeList) {
            response.append(craftingRecipe.toString()).append("\n");
        }
        return new Response(response.append("\n").toString(), true);
    }

    public Response cookingShowRecipes() {
        Player player = app.getCurrentGame().getCurrentPlayer();
        List<CookingRecipe> cookingRecipeList = player.getCookingRecipes();
        if (cookingRecipeList.isEmpty()) return new Response("No recipe found you loser.");
        StringBuilder response = new StringBuilder("Cooking recipes you've learnt so far: \n");
        for (CookingRecipe cookingRecipe : cookingRecipeList) {
            response.append(cookingRecipe.toString()).append("\n");
        }
        return new Response(response.append("\n").toString(), true);
    }

    public Response craftingCraft(String name) {
        Player player = app.getCurrentGame().getCurrentPlayer();
        CraftingRecipe recipe = player.findCraftingRecipe(name);
        if (recipe == null) return new Response("You've not learnt to craft " + name);
        Response isPossible = recipe.getCraftType().isCraftingPossible(player);
        if (!isPossible.shouldBeBack()) return isPossible;
        if (!player.getInventory().isInventoryHaveCapacity(recipe.getCraftType())) {
            return new Response("You don't have enough space in your backpack");
        }
        player.removeEnergy(2);
        recipe.getCraftType().removeIngredients(player);
        player.getInventory().addProductToBackPack(recipe.getCraftType(), 1);
        return new Response(recipe.getCraftType().getName() + " crafted successfully.");
    }

    public Response cookingRefrigeratorPick(String name) {
        Player player = app.getCurrentGame().getCurrentPlayer();
        Fridge fridge = app.getCurrentGame().findFarm().getFridge();
        Salable product = fridge.findProduct(name);
        if (product == null) {
            return new Response(name + " not found in the fridge");
        }
        if (!player.getInventory().isInventoryHaveCapacity(product)) {
            return new Response("Not enough space in backpack.");
        }
        player.getInventory().addProductToBackPack(product, fridge.countProduct(product));
        fridge.deleteProduct(product, fridge.countProduct(product));
        return new Response("Picked up.");
    }


    public Response cookingRefrigeratorPut(String name) {
        Player player = app.getCurrentGame().getCurrentPlayer();
        Fridge fridge = app.getCurrentGame().findFarm().getFridge();
        Salable product = player.getInventory().getProductFromBackPack(name);
        if (product == null) {
            return new Response(name + " not found in the backpack");
        }
        if (((Product)product).getEnergy() == 0) { //TODO check for being edible
            return new Response("Can't put the inedible items in the refrigerator.");
        }
        fridge.addProduct(product, player.getInventory().countProductFromBackPack(product));
        player.getInventory().deleteProductFromBackPack(product, player,
                player.getInventory().countProductFromBackPack(product));
        return new Response("Put down.");
    }

    public Response cookingPrepare(String name) {
        Player player = app.getCurrentGame().getCurrentPlayer();
        Fridge fridge = app.getCurrentGame().findFarm().getFridge();
        CookingRecipe recipe = player.findCookingRecipe(name);
        if (recipe == null) return new Response("You've not learnt to cook " + name);
        boolean isPossible = recipe.getFoodType().isValidIngredient(fridge);
        if (!isPossible) return new Response("Ingredients not found in the refrigerator.");
        if (!player.getInventory().isInventoryHaveCapacity(recipe.getFoodType())) {
            return new Response("You don't have enough space in your backpack");
        }
        player.removeEnergy(3);
        recipe.getFoodType().removeIngredients(fridge);
        player.getInventory().addProductToBackPack(recipe.getFoodType(), 1);
        return new Response(recipe.getFoodType().getName() + " cooked successfully.");
    }

    private Response isStoreOpen() {
        StoreType storeType = app.getCurrentGame().getCurrentPlayer().getStoreType();
        if(new TimeAndDate(0,storeType.getOpenDoorTime()).compareDailyTime(app.getCurrentGame().getTimeAndDate()) > 0 &&
               new TimeAndDate(0,storeType.getCloseDoorTime()).compareDailyTime(app.getCurrentGame().getTimeAndDate()) < 0) {
            return new Response("Store closed.");
        } return new Response("", true);
    }

    public Response showAllProducts() {
        Response response = isStoreOpen();
        if (!response.shouldBeBack()) return response;
        Player player = app.getCurrentGame().getCurrentPlayer();
        return player.getStoreType().showAllProducts();
    }

    public Response showAllAvailableProducts() {
        Response response = isStoreOpen();
        if (!response.shouldBeBack()) return response;
        Player player = app.getCurrentGame().getCurrentPlayer();
        return player.getStoreType().showAvailableProducts();
    }

    public Response purchase(String name, String count) {
        Response response = isStoreOpen();
        if (!response.shouldBeBack()) return response;
        Player player = app.getCurrentGame().getCurrentPlayer();
        return player.getStoreType().purchase(name, Integer.parseInt(count));
    }
}
