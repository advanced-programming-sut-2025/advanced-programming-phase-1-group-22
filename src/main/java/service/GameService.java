package service;

import model.*;
import model.abilitiy.Ability;
import model.animal.Animal;
import model.animal.AnimalType;
import model.animal.Fish;
import model.cook.Food;
import model.craft.CraftType;
import model.enums.Season;
import model.animal.FishType;
import model.cook.FoodType;
import model.craft.Craft;
import model.enums.Weather;
import model.gameSundry.Sundry;
import model.gameSundry.SundryType;
import model.products.*;
import model.products.TreesAndFruitsAndSeeds.*;
import model.products.AnimalProductType;
import model.products.Hay;
import model.products.Product;
import model.products.TreesAndFruitsAndSeeds.FruitType;
import model.receipe.CookingRecipe;
import model.receipe.CraftingRecipe;
import model.records.Response;
import model.relations.Player;
import model.shelter.FarmBuilding;
import model.shelter.FarmBuildingType;
import model.source.*;
import model.shelter.ShippingBin;
import model.source.CropType;
import model.source.MineralType;
import model.source.MixedSeedsType;
import model.source.SeedType;
import model.structure.Stone;
import model.structure.Structure;
import model.structure.Trunk;
import model.structure.farmInitialElements.Cottage;
import model.structure.farmInitialElements.GreenHouse;
import model.structure.farmInitialElements.Lake;
import model.structure.stores.*;
import model.tools.*;
import model.structure.stores.BlackSmithUpgrade;
import model.structure.stores.Store;
import model.structure.stores.StoreType;
import model.tools.BackPack;
import model.tools.Tool;
import repository.UserRepository;
import repository.UserRepositoryImpl;
import saveGame.GameSaver;
import saveGame.GameSerializer;
import utils.App;
import utils.HibernateUtil;
import utils.PasswordHasher;
import variables.Session;
import view.Menu;
import view.ViewRender;

import java.util.*;

public class GameService {
    private static volatile GameService instance;
    App app = App.getInstance();
    UserRepository<User> userRepository;
    private ViewRender viewRender;

    public GameService() {
        userRepository = new UserRepositoryImpl(HibernateUtil.getEntityManagerFactory().createEntityManager());
    }

    public static GameService getInstance() {
        if (instance == null) {        // Second check (inside lock)
            instance = new GameService();
            instance.viewRender = new ViewRender();
        }
        return instance;
    }

    public Response exitGame() {
        if (app.getCurrentGame().getCurrentPlayer().getUser() != Session.getCurrentUser()) {
            return new Response("You are not allowed to exit; the player who has started the app.getCurrentGame() can" +
                    " end it");
        }
        String filePath = Session.getCurrentUser().getUsername();
        filePath += ".bin";
        GameSerializer.saveGame(App.getInstance().getCurrentGame(), filePath);
        for (Player player : App.getInstance().getCurrentGame().getPlayers()) {
            User user = player.getUser();
            user.setIsPlaying(filePath);
            userRepository.save(player.getUser());
        }
        Session.setCurrentMenu(Menu.MAIN);
        return new Response("Exited from game.", true);
    }

    public Response terminateGame() {
        for (int i = 1; i < App.getInstance().getCurrentGame().getPlayers().size(); i++) {
            switch (viewRender.getResponse().message()) {
                case "Y":
                    break;
                case "n":
                    return new Response("Termination failed!");
                default:
                    System.out.println("Are you in favor termination? Y/n");
            }
        }
        App.getInstance().getGames().remove(app.getCurrentGame());
        Session.setCurrentMenu(Menu.MAIN);
        for (Player player : app.getCurrentGame().getPlayers()) {
            player.getUser().setIsPlaying(null);
            player.getUser().setNumberOfPlayedGames(player.getUser().getNumberOfPlayedGames() + 1);
            player.getUser().setHighestMoneyEarned(
                    Math.max(player.getUser().getHighestMoneyEarned(), player.getAccount().getGolds())
            );
            userRepository.save(player.getUser());
        }

        return new Response("The game is terminated", true);
    }

    public Response nextTurn() {
        Player player = app.getCurrentGame().getCurrentPlayer();
        player.setEnergyPerTurn(player.getMaxEnergyPerTurn());
        if (player.getBuff() != null) {
            if (!player.getBuff().nextHour()) {
                player.getBuff().defectBuff(player);
                player.setBuff(null);
            }
        }
        app.getCurrentGame().nextPlayer();
        if (app.getCurrentGame().getCurrentPlayer().getIsFainted()) return nextTurn();
        Session.setCurrentMenu(app.getCurrentGame().getCurrentPlayer().getCurrentMenu());
        return new Response("It's next player's turn", true);
    }

    public Response time() {
        String result = app.getCurrentGame().getTimeAndDate().getHour() + ":";
        result += app.getCurrentGame().getTimeAndDate().getMinute();
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
        for (int i = 0; i < hours * app.getCurrentGame().getPlayers().size(); i++) {
            nextTurn();
        }
        return new Response(x + " hours passed", true);
    }

    public Response C_AdvanceDate(String x) {
        int days = Integer.parseInt(x);
        for (int i = 0; i < days * app.getCurrentGame().getPlayers().size() * 13; i++) {
            nextTurn();
        }
        return new Response(x + " days passed", true);
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
        app.getCurrentGame().getVillage().setTomorrowWeather(weather);
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
            if (farm.getPlayers().get(0).equals(app.getCurrentGame().getCurrentPlayer())) {
                break;
            }
        }
        if (farm == null) return new Response("Please enter the farm you want to build the greenhouse in.");
        GreenHouse greenHouse = farm.getGreenHouse();
        if (greenHouse.isBuilt()) {
            return new Response("Greenhouse already built.");
        }
        switch (greenHouse.areIngredientsAvailable(app.getCurrentGame().getCurrentPlayer())) {
            case 1 -> {
                return new Response("You need 1000 golds to build it");
            }
            case 2 -> {
                return new Response("You need 500 wood to build it");
            }
        }
        greenHouse.build(farm);
        return new Response("The greenhouse is built", true);
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
        Farm destFarm = null;
        for (Farm farm : app.getCurrentGame().getVillage().getFarms()) {
            if (farm.isPairInFarm(new Pair(x1, y1))) {
                destFarm = farm;
                if (farm.getPlayers().contains(app.getCurrentGame().getCurrentPlayer())) break;
                return new Response("You are not allowed to enter this farm");
            }
        }
        WalkingStrategy walkingStrategy = new WalkingStrategy();
        Player player = app.getCurrentGame().getCurrentPlayer();
        int energy = walkingStrategy.calculateEnergy(
                new Pair(player.getTiles().get(0).getX(), player.getTiles().get(0).getY()), new Pair(x1, y1)
        );
        if (energy == -1) return new Response("No path available");
        String confirmation;
        while (true) {
            System.out.println("Energy needed: " + energy + "\nY/n");
            confirmation = viewRender.getResponse().message();
            if (confirmation.equals("Y")) break;
            if (confirmation.equals("n")) return new Response("You didn't moved");
        }
        if (player.getEnergy() < energy) {
            player.walkTillFaint(walkingStrategy.getDistances(), new Pair(x1, y1));
            player.faint();
            walkingStrategy.getDistances().clear();
            nextTurn();
            return new Response("Not enough energy; you fainted");
        }
        walkingStrategy.getDistances().clear();
        player.removeEnergy(energy);
        player.getTiles().clear();
        player.getTiles().add(app.getCurrentGame().tiles[x1][y1]);
        setMenu(player, destFarm);
        if (player.getEnergyPerTurn() <= 0) {
            nextTurn();
        }
        return new Response("Moved to the tile.", true);
    }

    public Response helpReadingMap() {
        String resp = "" +
                "\uD83E\uDDCD\tYou\n" +
                "👴\tNPC\n\nTile Types:\n" +
                "🌿\tGrass\n" +
                "🌸\tFlower\n" +
                "❄️\uFE0F\tSnow\n" +
                "▫️\uFE0F\tFlat\n" +
                "\uD83E\uDDF1\tPath\n" +
                "🚧\tFence\n" +
                "🚪\tDoor\n" +
                "\uD83D\uDFEB️\tPlowed\n" +
                "\uD83C\uDF29️\tthundered\n" +
                "Buildings:\n" +
                "\uD83C\uDFEC\tStore\n" +
                "\uD83C\uDFE1\ttNPC House\n" +
                "\uD83C\uDF0A\tFountain\n" +
                "\uD83C\uDFE1\ttCottage\n" +
                "\uD83C\uDF0A\ttLake\n" +
                "\uD83D\uDDFF\tQuarry\n" +
                "🚧\tGreenhouse\n" +
                "🏢\tBuilt Greenhouse\n" +
                "\uD83C\uDFDA️\tFarm Building\n" +
                "\uD83C\uDF33\tTrunk\n" +
                "🌲\tTree\n" +
                "🗿\tStone\n" +
                "🐄\tAnimal\n" +
                "🔨\tCraft\n" +
                "🥚\tAnimalProduct\n" +
                "\uD83D\uDDF3️\tShipping Bin\n" +
                "🌾\tCrop\n" +
                "🔷\tMineral\n" +
                "🌱\tMixed Seed\n" +
                "🫘\tSeed\n";
        return new Response(resp, true);
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
        return new Response("Account: " + getCurrentPlayer().getAccount().getGolds() + "\n\n" +
                getCurrentPlayer().getInventory().showInventory(), true);
    }

    public Response removeFromPlayerInventory(String itemName, boolean haveItemNumber, int... itemNumbers) {
        Player currentPlayer = getCurrentPlayer();
        Salable currentProduct = getProductFromInventory(currentPlayer, itemName.trim());
        if (currentProduct == null) {
            return new Response("the inventory does not contain this item");
        }
        if (!haveItemNumber) {
            currentPlayer.getInventory().deleteProductFromBackPack(currentProduct, currentPlayer,
                    currentPlayer.getInventory().getProducts().get(currentProduct));
            return new Response("you delete " + itemName + " completely", true);
        }
        int itemNumber = Math.min(itemNumbers[0], currentPlayer.getInventory().getProducts().get(currentProduct));
        currentPlayer.getInventory().deleteProductFromBackPack(currentProduct, currentPlayer, itemNumber);
        return new Response(itemNumber + " of " + itemName + "removed", true);
    }

    public Response toolEquip(String name) {
        Player currentPlayer = getCurrentPlayer();
        Tool currentTool = getToolFromPlayerInventory(name.trim(), currentPlayer);
        if (currentTool == null) {
            return new Response("there is not a tool with this name in inventory");
        }
        currentPlayer.setCurrentCarrying(currentTool);
        return new Response("you carrying " + name + " now", true);
    }

    public Response showCurrentTool() {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer.getCurrentCarrying() == null || !(currentPlayer.getCurrentCarrying() instanceof Tool)) {
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
        if (currentTool == null) {
            return new Response("there is not a tool with this name in inventory");
        }

        Tool upgradeTool = currentTool.getToolByLevel(currentTool.getLevel() + 1);

        if (upgradeTool == null) {
            return new Response("upgrade is not available");
        }

        if (!isPlayerInStore(StoreType.BLACK_SMITH)) {
            return new Response("you have to be in blackSmith store to upgrade tools");
        }

        BlackSmithUpgrade blackSmithUpgrade = BlackSmithUpgrade.getUpgradeByTool(upgradeTool);

        if (blackSmithUpgrade == null) {
            return new Response("this tool do not have any upgrade in  blackSmith");
        }

        if (!playerHaveEnoughResourceToUpgrade(currentPlayer, blackSmithUpgrade)) {
            return new Response("you do not have enough resource to upgrade tool");
        }

        if (blackSmithUpgrade.getDailySold() == blackSmithUpgrade.getDailyLimit()) {
            return new Response("Not enough in stock!");
        }
        upgradeTool(currentPlayer, blackSmithUpgrade, currentTool, upgradeTool);
        return new Response("the tool upgrade to " + upgradeTool.getName(), true);
    }

    public Response useTool(String direction) {
        Player currentPlayer = getCurrentPlayer();
        Direction currentDirection = Direction.getByName(direction);
        if (currentDirection == null) {
            return new Response("use valid direction");
        }
        Tool currentTool = getCurrentTool(currentPlayer);
        if (currentTool == null) {
            return new Response("you do not carrying any tool");
        } else if (currentTool.getEnergy(currentPlayer) > currentPlayer.getEnergy()) {
            return new Response("you do not have enough energy to use this tool");
        }
        Tile currentTile = getTileByXAndY(currentPlayer.getTiles().get(0).getX() + currentDirection.getXTransmit(),
                currentPlayer.getTiles().get(0).getY() + currentDirection.getYTransmit());
        if (currentTile == null) {
            return new Response("out of bond");
        }
        return new Response(currentTool.useTool(currentPlayer, currentTile), true);
    }

    public Response pickFromFloor(String direction) {
        Player currentPlayer = getCurrentPlayer();
        Direction currentDirection = Direction.getByName(direction);
        if (currentDirection == null) {
            return new Response("wrong direction");
        }
        Tile currentTile = getTileByXAndY(currentPlayer.getTiles().getFirst().getX() + currentDirection.getXTransmit(),
                currentPlayer.getTiles().getFirst().getY() + currentDirection.getYTransmit());
        if (currentTile == null) {
            return new Response("out of bound");
        }
        List<Structure> structures = App.getInstance().getCurrentGame().getVillage().findStructuresByTile(currentTile);
        Structure currentStructure = null;
        for (Structure structure : structures) {
            if (structure.getIsPickable()) {
                currentStructure = structure;
            }
        }
        if (currentStructure == null) {
            return new Response("there is nothing to pick on the floor");
        }
        boolean flag = tryToPickUp(currentPlayer, currentStructure);
        if (!flag) {
            return new Response("your backpack is full");
        }
        return new Response("you picked up a " + ((Salable) currentStructure).getName(), true);
    }

    public Response fishing(String name) {
        Player player = getCurrentPlayer();
        Tool currentTool;
        currentTool = (Tool) player.getInventory().getProductFromBackPack(name.trim());
        if (currentTool == null) {
            return new Response("you do not carrying fishing pole");
        }
        player.setCurrentCarrying(currentTool);
        if (currentTool.getEnergy(player) > player.getEnergy()) {
            return new Response("you do not have enough energy to use this tool");
        }
        Tile currentTile = isThereAnyLakeAround(player);
        if (currentTile == null) {
            return new Response("you have to be around the lake to fishing");
        }
        return new Response(currentTool.useTool(player, currentTile), true);
    }

    public Response build(String name, int x, int y) {
        Player currentPlayer = getCurrentPlayer();
        CarpenterShopFarmBuildings carpenterShopFarmBuildings = CarpenterShopFarmBuildings.getFromName(name);
        if (carpenterShopFarmBuildings == null) {
            return new Response("there is no farm building with this name");
        }
        if (!isPlayerInStore(StoreType.CARPENTER_SHOP)) {
            return new Response("you have to be in Carpenter store to build");
        }
        Farm currentFarm = getPlayerMainFarm(currentPlayer);
        if (!thisIsInFarm(currentFarm, x, y)) {
            return new Response("you have to chose a place in your farm");
        } else if (!playerHaveEnoughResourceToBuild(currentPlayer, carpenterShopFarmBuildings)) {
            return new Response("you do not have enough resource to build");
        }
        if (carpenterShopFarmBuildings.getDailySold() == carpenterShopFarmBuildings.getDailyLimit()) {
            return new Response("Not enough in stock!");
        }
        Structure structure;
        if (carpenterShopFarmBuildings.getFarmBuildingType().equals(FarmBuildingType.SHIPPING_BIN)) {
            structure = new ShippingBin();
        } else {
            structure = new FarmBuilding(carpenterShopFarmBuildings.getFarmBuildingType());
            ;
        }
        String message = buildStructureInAPlace(carpenterShopFarmBuildings,
                currentFarm, structure, carpenterShopFarmBuildings.getFarmBuildingType().getHeight(),
                carpenterShopFarmBuildings.getFarmBuildingType().getWidth(), x, y);
        if (message.contains("not")) {
            return new Response(message);
        }
        if (structure instanceof ShippingBin) {
            currentPlayer.getShippingBinList().add((ShippingBin) structure);
        }
        payForBuild(carpenterShopFarmBuildings, currentPlayer);
        return new Response(message, true);
    }

    public Response buyAnimal(String animalType, String name) {
        Player currentPlayer = getCurrentPlayer();
        MarnieShopAnimal marnieShopAnimal = MarnieShopAnimal.getFromName(animalType);
        if (marnieShopAnimal == null) {
            return new Response("there is no such animal");
        } else if (!isPlayerInStore(StoreType.MARNIE_SHOP)) {
            return new Response("you have to be in Marnie Shop Animal to buy animals");
        }
        if (marnieShopAnimal.getDailySold() == marnieShopAnimal.getDailyLimit()) {
            return new Response("Not enough in stock!");
        }
        Farm currentFarm = getPlayerMainFarm(currentPlayer);
        if (!isAnimalNameUnique(currentPlayer, name)) {
            return new Response("there is a animal with this name");
        } else if (!playerHaveEnoughResourceToBuyAnimal(marnieShopAnimal, currentPlayer)) {
            return new Response("you do not have enough resource to buy animal");
        }
        Animal animal = new Animal(marnieShopAnimal.getAnimalType(), name);
        animal.setOwner(currentPlayer);
        if (animal.getAnimalType().getIsBarnAnimal()) {
            return new Response(addNewBarnAnimal(currentFarm, animal, currentPlayer, marnieShopAnimal));
        }
        return new Response(addNewCoopAnimal(currentFarm, animal, currentPlayer, marnieShopAnimal), true);
    }

    public Response pet(String name) {
        Player currentPlayer = getCurrentPlayer();
        Animal currentAnimal = getAnimalAroundPlayer(currentPlayer, name);
        if (currentAnimal == null) {
            return new Response("you have to be around animal to pet it");
        } else if (!currentPlayer.getAnimals().contains(currentAnimal)) {
            return new Response("you only can pet your own animals");
        }
        if (!currentAnimal.getPet()) {
            currentAnimal.setPet(true);
            currentAnimal.changeFriendShip(15);
            return new Response("you pet " + name, true);
        }
        return new Response("you already pet this animal");
    }

    public Response setFriendShip(String name, int count) {
        Player currentPlayer = getCurrentPlayer();
        Animal currrentAnimal = getAnimalWithName(name, currentPlayer);
        if (currrentAnimal == null) {
            return new Response("you do not have such animal");
        }
        currrentAnimal.setRelationShipQuality(count);
        return new Response("friendShip upgrade to " + count, true);
    }

    public Response showAnimals() {
        Player currentPlayer = getCurrentPlayer();
        return new Response(makeStringTokenShowAnimals(currentPlayer), true);
    }

    public Response shepherdAnimals(String name, int x, int y) {
        Player currentPlayer = getCurrentPlayer();
        Animal currentAnimal = getAnimalWithName(name, currentPlayer);
        if (currentAnimal == null) {
            return new Response("you do not have animal with such name");
        }
        Tile currentTile = getTileByXAndY(x, y);
        if (currentTile == null) {
            return new Response("out of bound");
        }
        List<Farm> farms = getPlayerFarms(currentPlayer);
        if (!isPlayerAccessTile(currentTile, farms)) {
            return new Response("your animal can not be in that tile!");
        } else if (!tileIsAvailableForAnimal(currentTile, farms)) {
            return new Response("you can not put your animal in this tile");
        }
        currentAnimal.setTiles(List.of(currentTile));
        currentAnimal.setIsAnimalStayOutAllNight(!isAnimalInBarnOrCage(currentAnimal, farms));
        if (!currentAnimal.getIsFeed()) {
            currentAnimal.setIsFeed(true);
            currentAnimal.changeFriendShip(8);
        }
        return new Response("animal shepherd successfully", true);
    }

    public Response feedHay(String name) {
        Player currentPlayer = getCurrentPlayer();
        Animal currentAnimal = getAnimalWithName(name, currentPlayer);
        if (currentAnimal == null) {
            return new Response("you do not have animal with such name");
        }
        Hay hay = getPlayerHay(currentPlayer);
        if (hay == null) {
            return new Response("you do not have any hay");
        }
        if (currentAnimal.getIsFeed()) {
            return new Response("animal already eaten");
        }
        currentAnimal.setIsFeed(true);
        currentAnimal.changeFriendShip(8);
        return new Response(currentAnimal.getName() + " eat hay!", true);
    }

    public Response sellAnimal(String name) {
        Player currentPlayer = getCurrentPlayer();
        Animal currentAnimal = getAnimalWithName(name, currentPlayer);
        if (currentAnimal == null) {
            return new Response("you do not have animal with such name");
        }
        int price = (int) (currentAnimal.getSellPrice() * calculateAnimalCoefficientPrice(currentAnimal));
        int oldGold = currentPlayer.getAccount().getGolds();
        currentPlayer.getAccount().setGolds(oldGold + price);
        currentPlayer.getAnimals().remove(currentAnimal);
        removeAnimalFromVillage(currentAnimal, getPlayerFarms(currentPlayer));
        return new Response("you sell this animal " + price, true);
    }

    public Response produces() {
        Player currentPlayer = getCurrentPlayer();
        return new Response(makeTokenShowAnimalProduce(currentPlayer), true);
    }

    public Response collectProduce(String name) {
        Player currentPlayer = getCurrentPlayer();
        Animal currentAnimal = getAnimalWithName(name, currentPlayer);
        if (currentAnimal == null) {
            return new Response("you do not have animal with such name");
        } else if (!isPlayerNearAnimal(currentPlayer, currentAnimal)) {
            return new Response("you need to be next to animal to collect produce");
        }
        return new Response(collectProduce(currentAnimal, currentPlayer), true);
    }

    public Response craftInfo(String name) {
        Harvestable harvestableType = getHarvestableType(name);
        if (harvestableType == null) {
            return new Response("there is no harvestable with this name");
        }
        return new Response(harvestableType.craftInfo(), true);
    }

    public Response plantSeed(String name, String direction) {
        Player currentPlayer = getCurrentPlayer();
        Direction currentDirection = Direction.getByName(direction);
        if (currentDirection == null) {
            return new Response("use valid direction");
        }
        Tile currentTile = getTileByXAndY(currentPlayer.getTiles().get(0).getX() + currentDirection.getXTransmit(),
                currentPlayer.getTiles().get(0).getY() + currentDirection.getYTransmit());
        if (currentTile == null) {
            return new Response("out of bound");
        } else if (currentTile.getIsFilled()) {
            return new Response("this tile is not available for farming");
        } else if (!currentTile.getTileType().equals(TileType.PLOWED)) {
            return new Response("you should plow the tile first");
        }
        Salable salable = getProductFromInventory(currentPlayer, name);
        Seed seed;
        if (salable == null) {
            return new Response("you do not have this seed in your inventory");
        }
        if (salable instanceof MixedSeeds) {
            MixedSeeds mixedSeeds = (MixedSeeds) salable;
            if (!isThereGreenHouseForHarvest(currentTile) &&
                    !mixedSeeds.getMixedSeedsType().getSeason().equals(App.getInstance().getCurrentGame().getTimeAndDate().getSeason()) &&
                    !mixedSeeds.getMixedSeedsType().getSeason().equals(Season.SPECIAL)) {
                return new Response("you should use this seed in " + mixedSeeds.getMixedSeedsType().getSeason());
            }
            seed = generateSeedOfMixedSeed(mixedSeeds.getMixedSeedsType());
        } else if (salable instanceof Craft && name.equalsIgnoreCase("mystic tree seeds")) {
            seed = new Seed(SeedType.MYSTIC_TREE_SEEDS);
        } else if (salable instanceof Seed) {
            seed = (Seed) getProductFromInventory(currentPlayer, name);
        } else {
            return new Response("you do not have this seed in your inventory");
        }
        if (!isThereGreenHouseForHarvest(currentTile) &&
                !seed.getSeedType().getSeason().equals(App.getInstance().getCurrentGame().getTimeAndDate().getSeason())
                && !seed.getSeedType().getSeason().equals(Season.SPECIAL)) {
            return new Response("you should use this seed in " + seed.getSeedType().getSeason());
        }
        HarvestAbleProduct harvestableProduct = getHarvestableFromSeed(seed.getSeedType());
        if (harvestableProduct == null) {
            return new Response("this seed is not valid");
        }
        Farm currentFarm = getPlayerInWitchFarm(currentPlayer);
        if (currentFarm == null) {
            return new Response("you should plant in a farm");
        }
        currentTile.setIsFilled(true);
        harvestableProduct.setTiles(List.of(currentTile));
        TimeAndDate timeAndDate = new TimeAndDate();
        timeAndDate.setDay(App.getInstance().getCurrentGame().getTimeAndDate().getDay());
        harvestableProduct.setStartPlanting(timeAndDate);
        currentFarm.getStructures().add(harvestableProduct);
        if (isThereGreenHouseForHarvest(currentTile)) {
            harvestableProduct.setInGreenHouse(true);
        } else if (harvestableProduct instanceof Crop &&
                ((Crop) harvestableProduct).getCropType().isCanBecomeGiant()) {
            if (giantCrop(currentPlayer, (Crop) harvestableProduct)) {
                setScareCrowAndSprinklerForAll();
                currentPlayer.getInventory().deleteProductFromBackPack(getProductFromInventory(currentPlayer, name), currentPlayer, 1);
                return new Response("you plant and it become a giant crop", true);
            }
        }
        setScareCrowAndSprinklerForAll();
        currentPlayer.getInventory().deleteProductFromBackPack(getProductFromInventory(currentPlayer, name), currentPlayer, 1);
        return new Response("you plant successfully", true);
    }

    public Response showPlant(int x, int y) {
        Player currentPlayer = getCurrentPlayer();
        Tile currentTile = getTileByXAndY(x, y);
        if (currentTile == null) {
            return new Response("out of bound");
        }
//        if (!isTileInPlayerFarms(getPlayerFarms(currentPlayer), currentTile)) {
//            return new Response("you can not access other farms");
//        }
        HarvestAbleProduct harvestAbleProduct = findHarvestable(currentTile);
        if (harvestAbleProduct == null) {
            return new Response("there is no harvestable in this tile");
        }
        return new Response(makeTokenShowHarvestable(harvestAbleProduct), true);
    }

    public Response fertilize(String fertilize, String direction) {
        Player currentPlayer = getCurrentPlayer();
        Sundry currentFertilize = (Sundry) currentPlayer.getInventory().getProductFromBackPack(fertilize);
        if (currentFertilize == null) {
            return new Response("this item is not exist in the inventory");
        }
        if (!isThisFertilize(currentFertilize)) {
            return new Response("there is no fertilize with this name");
        }
        Direction currentDirection = Direction.getByName(direction);
        if (currentDirection == null) {
            return new Response("use valid direction");
        }
        Tile currentTile = getTileByXAndY(currentPlayer.getTiles().get(0).getX() + currentDirection.getXTransmit(),
                currentPlayer.getTiles().get(0).getY() + currentDirection.getYTransmit());
        if (currentTile == null) {
            return new Response("out of bound");
        }
        HarvestAbleProduct harvestAbleProduct = findHarvestable(currentTile);
        if (harvestAbleProduct == null) {
            return new Response("there is no harvestable in this tile");
        }
        harvestAbleProduct.setFertilized(true);
        currentPlayer.getInventory().deleteProductFromBackPack(currentFertilize, currentPlayer, 1);
        harvestAbleProduct.getFertilizes().add(currentFertilize.getSundryType());
        return new Response("you successfully fertilize " + harvestAbleProduct.getName(), true);
    }

    public Response howMuchWater() {
        Player currentPlayer = getCurrentPlayer();
        Tool currentTool = getCurrentTool(currentPlayer);
        if (currentTool == null || !(currentTool instanceof WateringCan)) {
            return new Response("you do not carrying watering can");
        }
        return new Response("remain water: " + ((WateringCan) currentTool).getRemain(), true);
    }

    public Response showAbility() {
        Player currentPlayer = getCurrentPlayer();
        return new Response(makeStringTokenAbility(currentPlayer), true);
    }

    private String makeStringTokenAbility(Player player) {
        StringBuilder token = new StringBuilder();
        for (Map.Entry<Ability, Integer> abilityIntegerEntry : player.getAbilities().entrySet()) {
            token.append(abilityIntegerEntry.getKey().toString().toLowerCase()).append(" :\n").
                    append("    xp: ").append(abilityIntegerEntry.getValue()).append("\n").
                    append("    level: ").append(player.getAbilityLevel(abilityIntegerEntry.getKey())).append("\n");
        }
        return token.toString();
    }

    private Player getCurrentPlayer() {
        return App.getInstance().getCurrentGame().getCurrentPlayer();
    }

    private Salable getProductFromInventory(Player currentPlayer, String itemName) {
        Salable currentProduct = null;
        for (Map.Entry<Salable, Integer> salableIntegerEntry : currentPlayer.getInventory().getProducts().entrySet()) {
            if (salableIntegerEntry.getKey().getName().equalsIgnoreCase(itemName)) {
                currentProduct = salableIntegerEntry.getKey();
            }
        }

        return currentProduct;
    }

    private Tool getToolFromPlayerInventory(String name, Player player) {
        Tool currentTool = null;
        for (Map.Entry<Salable, Integer> salableIntegerEntry : player.getInventory().getProducts().entrySet()) {
            if (salableIntegerEntry.getKey().getName().equalsIgnoreCase(name) &&
                    salableIntegerEntry.getKey() instanceof Tool) {
                currentTool = (Tool) salableIntegerEntry.getKey();
            }
        }
        return currentTool;
    }

    private void setMenu(Player player, Farm farm) {
        if (farm == null) {
            Village village = app.getCurrentGame().getVillage();
            for (Structure structure : village.getStructures()) {
                if (structure instanceof Store) {
                    for (Tile tile : structure.getTiles()) {
                        if (player.getTiles().get(0) == tile) {
                            player.setStoreType(((Store) structure).getStoreType());
                            player.setCurrentMenu(Menu.STORE_MENU);
                            Session.setCurrentMenu(Menu.STORE_MENU);
                            return;
                        }
                    }
                }
            }
            Session.setCurrentMenu(Menu.GAME_MAIN_MENU);
            return;
        }
        Cottage cottage = farm.getCottage();
        for (Tile tile : cottage.getTiles()) {
            if (player.getTiles().get(0) == tile) {
                player.setCurrentMenu(Menu.COTTAGE);
                Session.setCurrentMenu(Menu.COTTAGE);
                return;
            }
        }
        player.setCurrentMenu(Menu.GAME_MAIN_MENU);
        Session.setCurrentMenu(Menu.GAME_MAIN_MENU);

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

    private Boolean isPlayerInStore(StoreType storeType) {
        return App.getInstance().getCurrentGame().getCurrentPlayer().getStoreType() == storeType;
    }

    private Boolean playerHaveEnoughResourceToUpgrade(Player player, BlackSmithUpgrade blackSmithUpgrade) {
        if (blackSmithUpgrade.getCost() > player.getAccount().getGolds()) {
            return false;
        }
        for (Map.Entry<Salable, Integer> productIntegerEntry : blackSmithUpgrade.getIngredients().entrySet()) {
            Salable salable = player.getInventory().getProductFromBackPack(productIntegerEntry.getKey().getName());
            if (salable == null) {
                return false;
            }
            if (player.getInventory().getProducts().get(salable) < productIntegerEntry.getValue()) {
                return false;
            }
        }
        return true;
    }

    private void upgradeTool(Player player, BlackSmithUpgrade blackSmithUpgrade, Tool oldtool, Tool upgradeTool) {
        int oldGold = player.getAccount().getGolds();
        player.getAccount().setGolds(oldGold - blackSmithUpgrade.getCost());

        for (Map.Entry<Salable, Integer> productIntegerEntry : blackSmithUpgrade.getIngredients().entrySet()) {
            Salable salable = player.getInventory().getProductFromBackPack(productIntegerEntry.getKey().getName());
            player.getInventory().deleteProductFromBackPack(salable, player, productIntegerEntry.getValue());
        }
        if (oldtool instanceof WateringCan) {
            ((WateringCan) oldtool).setWateringCanType((WateringCanType) upgradeTool);
        } else {
            player.getInventory().getProducts().remove(oldtool);
            player.getInventory().getProducts().put(upgradeTool, 1);
        }
    }

    private Tool getCurrentTool(Player player) {
        if (player.getCurrentCarrying() == null ||
                !(player.getCurrentCarrying() instanceof Tool)) {
            return null;
        }
        return (Tool) player.getCurrentCarrying();
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

    private boolean tryToPickUp(Player player, Structure structure) {
        if (player.getInventory().isInventoryHaveCapacity((Salable) structure)) {
            player.getInventory().addProductToBackPack((Salable) structure, 1);
            for (Tile structureTile : structure.getTiles()) {
                structureTile.setIsFilled(false);
            }
            App.getInstance().getCurrentGame().getVillage().removeStructure(structure);
            return true;
        }
        return false;
    }

    private Tile isThereAnyLakeAround(Player player) {
        for (Direction value : Direction.values()) {
            Tile currentTile = getTileByXAndY(player.getTiles().get(0).getX() + value.getXTransmit(),
                    player.getTiles().get(0).getY() + value.getYTransmit());
            if (currentTile != null) {
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
            if (farm.getPlayers().get(0).equals(player)) {
                return farm;
            }
        }
        return null;
    }

    private List<Farm> getPlayerFarms(Player player) {
        List<Farm> farms = new ArrayList<>();
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            if (farm.getPlayers().contains(player)) {
                farms.add(farm);
            }
        }
        return farms;
    }

    private boolean thisIsInFarm(Farm farm, int x, int y) {
        for (Tile tile : farm.getTiles()) {
            if (tile.getX() == x && tile.getY() == y) {
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
            Salable salable = player.getInventory().getProductFromBackPack(productIntegerEntry.getKey().getName());
            if (salable == null) {
                return false;
            }
            if (player.getInventory().getProducts().get(salable) < productIntegerEntry.getValue()) {
                return false;
            }
        }
        return true;
    }

    private String buildStructureInAPlace(CarpenterShopFarmBuildings carpenterShopFarmBuildings, Farm farm, Structure structure, int length, int width, int x, int y) {
        Tile[][] tiles1 = app.getCurrentGame().tiles;
        List<Tile> tiles2 = new ArrayList<>();
        boolean flag = true;
        for (int j = x; j < x + length; j++) {
            for (int k = y; k < y + width; k++) {
                if (tiles1[j][k].getIsFilled()) {
                    flag = false;
                } else {
                    tiles2.add(tiles1[j][k]);
                }
            }
        }
        if (flag) {
            for (Tile tile : tiles2) {
                tile.setIsFilled(true);
                if (!carpenterShopFarmBuildings.equals(CarpenterShopFarmBuildings.SHIPPING_BIN) &&
                        !carpenterShopFarmBuildings.equals(CarpenterShopFarmBuildings.WELL)) {
                    tile.setIsPassable(true);
                }
            }
            structure.getTiles().addAll(tiles2);
            farm.getStructures().add(structure);
            return "this building successfully add to your farm";
        }
        tiles2.clear();
        return "this structure can not be here";
    }

    private void payForBuild(CarpenterShopFarmBuildings carpenterShopFarmBuildings, Player player) {
        int oldGold = player.getAccount().getGolds();
        player.getAccount().setGolds(oldGold - carpenterShopFarmBuildings.getPrice());
        for (Map.Entry<Product, Integer> productIntegerEntry : carpenterShopFarmBuildings.getCost().entrySet()) {
            Salable salable = player.getInventory().getProductFromBackPack(productIntegerEntry.getKey().getName());
            player.getInventory().deleteProductFromBackPack(salable, player, productIntegerEntry.getValue());
        }
    }

    private boolean isAnimalNameUnique(Player player, String name) {
        for (Animal animal : player.getAnimals()) {
            if (animal.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    private String addNewBarnAnimal(Farm farm, Animal animal, Player player,
                                    MarnieShopAnimal marnieShopAnimal) {
        for (Structure structure : farm.getStructures()) {
            if (structure instanceof FarmBuilding) {
                if (((FarmBuilding) structure).getFarmBuildingType().getIsBarn()) {
                    if (((FarmBuilding) structure).canAddNewAnimal()) {
                        ((FarmBuilding) structure).getAnimals().add(animal);
                        int oldGold = player.getAccount().getGolds();
                        player.getAccount().setGolds(oldGold - marnieShopAnimal.getPrice());
                        animal.setTiles(List.of(Objects.requireNonNull(getAFreeTileInBarnOrCoop((FarmBuilding) structure))));
                        player.getAnimals().add(animal);
                        farm.getStructures().add(animal);
                        return "a/an " + animal.getAnimalType().getName() + " added successfully";
                    }
                }
            }
        }
        return "your barn is full";
    }

    private String addNewCoopAnimal(Farm farm, Animal animal, Player player,
                                    MarnieShopAnimal marnieShopAnimal) {
        for (Structure structure : farm.getStructures()) {
            if (structure instanceof FarmBuilding) {
                if (((FarmBuilding) structure).getFarmBuildingType().getIsCoop()) {
                    if (((FarmBuilding) structure).canAddNewAnimal()) {
                        ((FarmBuilding) structure).getAnimals().add(animal);
                        int oldGold = player.getAccount().getGolds();
                        player.getAccount().setGolds(oldGold - marnieShopAnimal.getPrice());
                        animal.setTiles(List.of(Objects.requireNonNull(getAFreeTileInBarnOrCoop((FarmBuilding) structure))));
                        player.getAnimals().add(animal);
                        farm.getStructures().add(animal);
                        return "a/an " + animal.getAnimalType().getName() + " added successfully";
                    }
                }
            }
        }
        return "your coop is full";
    }

    private Tile getAFreeTileInBarnOrCoop(FarmBuilding farmBuilding) {
        for (Tile tile : farmBuilding.getTiles()) {
            List<Structure> structures = App.getInstance().getCurrentGame().getVillage().findStructuresByTile(tile);
            boolean flag = true;
            for (Structure structure : structures) {
                if (structure instanceof Animal) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                return tile;
            }
        }
        return null;
    }

    private boolean playerHaveEnoughResourceToBuyAnimal(MarnieShopAnimal marnieShopAnimal, Player player) {
        return marnieShopAnimal.getPrice() <= player.getAccount().getGolds();
    }

    private Animal getAnimalAroundPlayer(Player player, String name) {
        for (Animal animal : player.getAnimals()) {
            if (animal.getName().equalsIgnoreCase(name)) {
                for (Direction value : Direction.values()) {
                    if (player.getTiles().getFirst().getX() + value.getXTransmit() == animal.getTiles().getFirst().getX() &&
                            player.getTiles().getFirst().getY() + value.getYTransmit() == animal.getTiles().getFirst().getY()) {
                        return animal;
                    }
                }
            }
        }
        return null;
    }

    private Animal getAnimalWithName(String name, Player player) {
        for (Animal animal : player.getAnimals()) {
            if (animal.getName().equalsIgnoreCase(name)) {
                return animal;
            }
        }
        return null;
    }

    private String makeStringTokenShowAnimals(Player player) {
        StringBuilder token = new StringBuilder();
        token.append("animals:\n");
        for (Animal animal : player.getAnimals()) {
            token.append("name: ").append(animal.getName()).append("\n");
            token.append("is feed today: ").append(animal.getIsFeed()).append("\n");
            token.append("is pet today: ").append(animal.getPet()).append("\n");
            token.append("friendShip: ").append(animal.getRelationShipQuality()).append("\n");
        }
        return token.toString();
    }

    private boolean isPlayerAccessTile(Tile tile, List<Farm> farms) {
        for (Farm farm : farms) {
            if (farm.getTiles().contains(tile)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAnimalInBarnOrCage(Animal animal, List<Farm> farms) {
        for (Farm farm : farms) {
            for (Structure structure : farm.getStructures()) {
                if (structure instanceof FarmBuilding && structure.getTiles().contains(animal.getTiles().get(0))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean tileIsAvailableForAnimal(Tile tile, List<Farm> farms) {
        for (Farm farm : farms) {
            for (Structure structure : farm.getStructures()) {
                if (structure.getTiles().contains(tile)) {
                    return structure instanceof FarmBuilding &&
                            (((FarmBuilding) structure).getFarmBuildingType().getIsCoop() ||
                                    ((FarmBuilding) structure).getFarmBuildingType().getIsBarn());
                }
            }
        }
        return true;
    }

    private Hay getPlayerHay(Player player) {
        Hay hay = null;
        for (Map.Entry<Salable, Integer> salableIntegerEntry : player.getInventory().getProducts().entrySet()) {
            if (salableIntegerEntry.getKey() instanceof Hay) {
                hay = (Hay) salableIntegerEntry.getKey();
                player.getInventory().deleteProductFromBackPack(hay, player, 1);
                break;
            }
        }
        return hay;
    }

    private double calculateAnimalCoefficientPrice(Animal animal) {
        int friendShip = animal.getRelationShipQuality();
        return ((double) friendShip / 1000) + 0.3;
    }

    private void removeAnimalFromVillage(Animal animal, List<Farm> farms) {
        for (Farm farm : farms) {
            farm.getStructures().remove(animal);
        }
        for (Farm farm : farms) {
            for (Structure structure : farm.getStructures()) {
                if (structure instanceof FarmBuilding) {
                    ((FarmBuilding) structure).getAnimals().remove(animal);
                }
            }
        }
    }

    private String makeTokenShowAnimalProduce(Player player) {
        StringBuilder token = new StringBuilder();
        token.append("remain products: \n");
        for (Animal animal : player.getAnimals()) {
            if (animal.getTodayProduct() != null) {
                token.append(animal.getName()).append(": \n");
                token.append("    ").append(animal.getTodayProduct().getName()).append(" quality : ").append(animal.getTodayProduct().getProductQuality()).append("\n");
            }
        }
        return token.toString();
    }

    private boolean isPlayerNearAnimal(Player player, Animal animal) {
        for (Direction value : Direction.values()) {
            if (player.getTiles().get(0).getX() + value.getXTransmit() == animal.getTiles().get(0).getX() &&
                    player.getTiles().get(0).getY() + value.getYTransmit() == animal.getTiles().get(0).getY()) {
                return true;
            }
        }
        return false;
    }

    private String collectProduce(Animal animal, Player player) {
        if (animal.getAnimalType().equals(AnimalType.COW) ||
                animal.getAnimalType().equals(AnimalType.GOAT)) {
            if (player.getCurrentCarrying() != null && player.getCurrentCarrying() instanceof MilkPail) {
                return ((Tool) player.getCurrentCarrying()).useTool(player, animal.getTiles().get(0));
            }
            return "you do not carrying needed tool";
        }
        if (animal.getAnimalType().equals(AnimalType.SHEEP)) {
            if (player.getCurrentCarrying() != null && player.getCurrentCarrying() instanceof Shear) {
                return ((Tool) player.getCurrentCarrying()).useTool(player, animal.getTiles().get(0));
            }
            return "you do not carrying needed tool";
        }
        if (animal.getAnimalType().equals(AnimalType.PIG)) {
            if (!isAnimalInBarnOrCage(animal, getPlayerFarms(player))) {
                if (animal.getTodayProduct() != null) {
                    AnimalProduct animalProduct = animal.getTodayProduct();
                    if (player.getInventory().isInventoryHaveCapacity(animalProduct)) {
                        player.getInventory().addProductToBackPack(animalProduct, 1);
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
        if (animal.getTodayProduct() != null) {
            AnimalProduct animalProduct = animal.getTodayProduct();
            if (player.getInventory().isInventoryHaveCapacity(animalProduct)) {
                player.getInventory().addProductToBackPack(animalProduct, 1);
                animal.setTodayProduct(null);
                return "you collect produce of " + animal.getName() + ": " + animalProduct.getName() +
                        " with quality: " + animalProduct.getProductQuality();
            }
            return "your inventory is full so you can not produce";
        }
        return "this animal do not have produce today";
    }

    private Harvestable getHarvestableType(String name) {
        for (FruitType value : FruitType.values()) {
            if (value.getName().equalsIgnoreCase(name)) {
                return value;
            }
        }
        for (CropType value : CropType.values()) {
            if (value.getName().equalsIgnoreCase(name)) {
                return value;
            }
        }
        for (TreeType value : TreeType.values()) {
            if (value.getName().equalsIgnoreCase(name)) {
                return value;
            }
        }
        return null;
    }

    public Response placeItem(String itemName, String direction) {
        Player player = app.getCurrentGame().getCurrentPlayer();
        Salable product = player.getInventory().findProductInBackPackByNAme(itemName);
        if (product == null) return new Response(itemName + " not found in your backpack.");
        Direction dir = Direction.getByName(direction);
        if (dir == null) return new Response(direction + " not recognized as a direction.");
        Tile tile = app.getCurrentGame().tiles[player.getTiles().get(0).getX() + dir.getXTransmit()]
                [player.getTiles().get(0).getY() + dir.getYTransmit()];
        if (tile.getIsFilled()) return new Response("The tile you're trying to put the item on, is filled");
        if (!(product instanceof Structure))
            return new Response(itemName + " Cannot be put on ground"); //TODO Some objects are not structure but must be put on ground
        player.getInventory().deleteProductFromBackPack(product, player, 1);
        ((Structure) product).getTiles().add(tile);
        Farm currentFarm = getPlayerInWitchFarm(player);
        if (currentFarm == null) return new Response("You can only place something in a farm");
        if (product instanceof Craft) {
            switch (((Craft) product).getCraftType()) {
                case BOMB:
                case CHERRY_BOMB:
                case MEGA_BOMB: {
                    List<Tile> affectedTiles = ((Craft) product).getAffectedTiles();
                    for (Tile tile1 : affectedTiles) {
                        tile1.setIsPassable(true);
                        tile1.setIsFilled(false);
                    }
                    List<Structure> farmStructures = new ArrayList<>(currentFarm.getStructures());
                    for (Structure structure : farmStructures) {
                        boolean flag = false;
                        for (Tile structureTile : structure.getTiles()) {
                            if (affectedTiles.contains(structureTile)) {
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) continue;
                        if (structure instanceof Tree) currentFarm.getStructures().remove(structure);
                        if (structure instanceof Crop) currentFarm.getStructures().remove(structure);
                        if (structure instanceof Stone) currentFarm.getStructures().remove(structure);
                        if (structure instanceof Mineral) currentFarm.getStructures().remove(structure);
                        if (structure instanceof Seed) currentFarm.getStructures().remove(structure);
                        if (structure instanceof MixedSeeds) currentFarm.getStructures().remove(structure);
                        if (structure instanceof Trunk) currentFarm.getStructures().remove(structure);
                        if (structure instanceof FarmBuilding) currentFarm.getStructures().remove(structure);
                        if (structure instanceof Animal) currentFarm.getStructures().remove(structure);
                        if (structure instanceof ShippingBin) currentFarm.getStructures().remove(structure);
                    }
                    return new Response("Bombing completed.");
                }
                case GRASS_STARTER: {
                    ((Craft) product).getTiles().get(0).setTileType(TileType.GRASS);
                    return new Response(itemName + " is put on the ground.", true);
                }
            }
        }
        currentFarm.getStructures().add((Structure) product);
        setScareCrowAndSprinklerForAll();
        tile.setIsFilled(true); //TODO not always is filled;
        return new Response(itemName + " is put on the ground.", true);
    }

    public Response C_AddItem(String name, String count) { //TODO conflict of product and productType
        BackPack inventory = app.getCurrentGame().getCurrentPlayer().getInventory();
        Salable salable = null;
        for (FishType value : FishType.values()) {
            if (name.equalsIgnoreCase(value.getName())) salable = new Fish(value, ProductQuality.NORMAL);
        }
        if (salable == null) {
            for (FoodType value : FoodType.values()) {
                if (name.equalsIgnoreCase(value.getName())) salable = new Food(value);
            }
        }

        if (salable == null) {
            for (CraftType value : CraftType.values()) {
                if (name.equalsIgnoreCase(value.getName())) salable = new Craft(value, null, null);
            }
        }

        if (salable == null) {
            for (FruitType value : FruitType.values()) {
                if (name.equalsIgnoreCase(value.getName())) salable = new Fruit(value);
            }
        }

        if (salable == null) {
            for (MadeProductType value : MadeProductType.values()) {
                if (name.equalsIgnoreCase(value.getName())) salable = new MadeProduct(value);
            }
        }

        if (salable == null) {
            for (AnimalProductType value : AnimalProductType.values()) {
                if (name.equalsIgnoreCase(value.getName())) salable = new AnimalProduct(value);
            }
        }
        if (salable == null) {
            for (SundryType value : SundryType.values()) {
                if (name.equalsIgnoreCase(value.getName())) salable = new Sundry(value);
            }
        }
        if (salable == null) {
            for (CropType value : CropType.values()) {
                if (name.equalsIgnoreCase(value.getName())) salable = new Crop(value);
            }
        }

        if (salable == null) {
            for (MineralType value : MineralType.values()) {
                if (name.equals(value.getName())) salable = new Mineral(value);
            }
        }

        if (salable == null) {
            for (MixedSeedsType value : MixedSeedsType.values()) {
                if (name.equalsIgnoreCase(value.getName())) salable = new MixedSeeds(value);
            }
        }

        if (salable == null) {
            for (SeedType value : SeedType.values()) {
                if (name.equalsIgnoreCase(value.getName())) salable = new Seed(value);
            }
        }

        if (salable == null) {
            for (Pickaxe value : Pickaxe.values()) {
                if (name.equalsIgnoreCase(value.getName())) salable = value;
            }
        }

        if (salable == null) {
            for (Axe value : Axe.values()) {
                if (name.equalsIgnoreCase(value.getName())) salable = value;
            }
        }

        if (salable == null) {
            for (FishingPole value : FishingPole.values()) {
                if (name.equalsIgnoreCase(value.getName())) salable = value;
            }
        }

        if (salable == null) {
            for (Hoe value : Hoe.values()) {
                if (name.equalsIgnoreCase(value.getName())) salable = value;
            }
        }

        if (salable == null) {
            for (BackPackType value : BackPackType.values()) {
                if (name.equalsIgnoreCase(value.getName())) {
                    App.getInstance().getCurrentGame().getCurrentPlayer().getInventory().setBackPackType(value);
                    return new Response("Backpack is of type " + name + " now.");
                }
            }
        }

        if (salable == null) {
            if (name.equalsIgnoreCase("milkpail")) salable = MilkPail.getInstance();
        }

        if (salable == null) {
            if (name.equalsIgnoreCase("scythe")) salable = new Scythe();
        }

        if (salable == null) {
            for (Shear value : Shear.values()) {
                if (name.equalsIgnoreCase(value.getName())) salable = value;
            }
        }

        if (salable == null) {
            for (TrashCan value : TrashCan.values()) {
                if (name.equalsIgnoreCase(value.getName())) salable = value;
            }
        }

        if (salable == null) {
            for (WateringCanType value : WateringCanType.values()) {
                if (name.equalsIgnoreCase(value.getName())) salable = new WateringCan(value);
            }
        }

        if (salable == null) {
            if (name.equalsIgnoreCase("hay")) salable = new Hay();
        }
        if (salable == null) {
            if (name.equalsIgnoreCase("flower")) salable = new Flower();
        }

        if (salable == null) {
            for (CraftingRecipe value : CraftingRecipe.values()) {
                if (name.equalsIgnoreCase(value.getName())) {
                    Map<CraftingRecipe, Boolean> recipes = app.getCurrentGame().getCurrentPlayer().getCraftingRecipes();
                    if (!recipes.containsKey(value) || !recipes.get(value)) {
                        recipes.put(value, true);
                    }
                    return new Response("You learnt the recipe of " + name);
                }
            }
        }

        if (salable == null) {
            for (CookingRecipe value : CookingRecipe.values()) {
                if (name.equalsIgnoreCase(value.getName())) {
                    Map<CookingRecipe, Boolean> recipes = app.getCurrentGame().getCurrentPlayer().getCookingRecipes();
                    if (!recipes.containsKey(value) || !recipes.get(value)) {
                        recipes.put(value, true);
                    }
                    return new Response("You learnt the recipe of " + name);
                }
            }
        }

        if (salable == null) return new Response(name + " cannot be added to the backpack");
        if (!inventory.isInventoryHaveCapacity(salable)) return new Response("Backpack hasn't enough space");
        inventory.addProductToBackPack(salable, Integer.parseInt(count));
        return new Response(name + " x" + count + " added to backpack.", true);
    }

    private HarvestAbleProduct getHarvestableFromSeed(SeedType seedType) {
        for (TreeType value : TreeType.values()) {
            if (value.getSource() != null && value.getSource().equals(seedType) &&
                    !value.getIsForaging()) {
                return new Tree(value);
            }
        }
        for (CropType value : CropType.values()) {
            if (value.getSource() != null && value.getSource().equals(seedType)) {
                return new Crop(value);
            }
        }
        return null;
    }

    private Farm getPlayerInWitchFarm(Player player) {
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            if (farm.getTiles().contains(player.getTiles().get(0))) {
                return farm;
            }
        }
        return null;
    }

    private HarvestAbleProduct findHarvestable(Tile tile) {
        List<Structure> structures = App.getInstance().getCurrentGame().getVillage().findStructuresByTile(tile);
        for (Structure structure : structures) {
            if (structure instanceof HarvestAbleProduct) {
                return (HarvestAbleProduct) structure;
            }
        }
        return null;
    }

    private boolean isTileInPlayerFarms(List<Farm> farms, Tile tile) {
        for (Farm farm : farms) {
            if (farm.getTiles().contains(tile)) {
                return true;
            }
        }
        return false;
    }

    private String makeTokenShowHarvestable(HarvestAbleProduct harvestAbleProduct) {
        String token = "";
        token += "Name:" + harvestAbleProduct.getName() + "\n";
        token += "Remain days until can harvest: " + harvestAbleProduct.remainDaysUntilCanHarvest() + "\n";
        token += "Regrowth level: " + harvestAbleProduct.calculateRegrowthLevel() + "\n";
        token += "isWatered today: " + harvestAbleProduct.getIsWaterToday() + "\n";
        token += "isFertilized: " + harvestAbleProduct.getIsFertilized() + "\n";
        return token;
    }

    private Seed generateSeedOfMixedSeed(MixedSeedsType mixedSeedsType) {
        Random random = new Random();
        int randNumber = random.nextInt();
        return new Seed(mixedSeedsType.getSeedTypeList().get(randNumber % mixedSeedsType.getSeedTypeList().size()));
    }

    private boolean giantCrop(Player player, Crop crop) {
        Crop newCrop = new Crop(crop.getCropType());
        newCrop.setIsGiant(true);

        Tile tile1 = getTileByXAndY(crop.getTiles().get(0).getX(), crop.getTiles().get(0).getY() + 1);
        Crop crop1 = getCropInTile(tile1);
        Tile tile2 = getTileByXAndY(crop.getTiles().get(0).getX() + 1, crop.getTiles().get(0).getY() + 1);
        Crop crop2 = getCropInTile(tile2);
        Tile tile3 = getTileByXAndY(crop.getTiles().get(0).getX() + 1, crop.getTiles().get(0).getY());
        Crop crop3 = getCropInTile(tile3);
        if (canBeGiant(crop, crop1, crop2, crop3)) {
            makeGiant(player, newCrop, crop.getTiles().get(0), crop, crop1, crop2, crop3);
            newCrop.setStartPlanting(findMinimumTime(crop.getStartPlanting(), crop1.getStartPlanting(),
                    crop2.getStartPlanting(), crop3.getStartPlanting()));
            newCrop.setIsGiant(true);
            return true;
        }

        Tile tile4 = getTileByXAndY(crop.getTiles().get(0).getX() + 1, crop.getTiles().get(0).getY() - 1);
        Crop crop4 = getCropInTile(tile4);
        Tile tile5 = getTileByXAndY(crop.getTiles().get(0).getX(), crop.getTiles().get(0).getY() - 1);
        Crop crop5 = getCropInTile(tile5);
        if (canBeGiant(crop, crop3, crop4, crop5)) {
            makeGiant(player, newCrop, crop5.getTiles().get(0), crop, crop3, crop4, crop5);
            newCrop.setStartPlanting(findMinimumTime(crop.getStartPlanting(), crop3.getStartPlanting(),
                    crop4.getStartPlanting(), crop5.getStartPlanting()));
            newCrop.setIsGiant(true);
            return true;
        }

        Tile tile6 = getTileByXAndY(crop.getTiles().get(0).getX() - 1, crop.getTiles().get(0).getY() - 1);
        Crop crop6 = getCropInTile(tile6);
        Tile tile7 = getTileByXAndY(crop.getTiles().get(0).getX() - 1, crop.getTiles().get(0).getY());
        Crop crop7 = getCropInTile(tile7);
        if (canBeGiant(crop, crop5, crop6, crop7)) {
            makeGiant(player, newCrop, crop6.getTiles().get(0), crop, crop5, crop6, crop7);
            newCrop.setStartPlanting(findMinimumTime(crop.getStartPlanting(), crop5.getStartPlanting(),
                    crop6.getStartPlanting(), crop7.getStartPlanting()));
            newCrop.setIsGiant(true);
            return true;
        }

        Tile tile8 = getTileByXAndY(crop.getTiles().get(0).getX() - 1, crop.getTiles().get(0).getY() + 1);
        Crop crop8 = getCropInTile(tile8);
        if (canBeGiant(crop, crop1, crop7, crop8)) {
            makeGiant(player, newCrop, crop7.getTiles().get(0), crop, crop1, crop7, crop8);
            newCrop.setStartPlanting(findMinimumTime(crop.getStartPlanting(), crop1.getStartPlanting(),
                    crop7.getStartPlanting(), crop8.getStartPlanting()));
            newCrop.setIsGiant(true);
            return true;
        }
        return false;
    }

    private Crop getCropInTile(Tile tile) {
        List<Structure> structures = App.getInstance().getCurrentGame().getVillage().findStructuresByTile(tile);
        for (Structure structure : structures) {
            if (structure instanceof Crop) {
                return (Crop) structure;
            }
        }
        return null;
    }

    private void makeGiant(Player player, Crop newCrop, Tile tile, Crop... crops) {
        for (Crop crop : crops) {
            if (crop.getIsFertilized()) {
                newCrop.setFertilized(true);
            }
            if (crop.getIsWaterToday()) {
                newCrop.setIsWaterToday(true);
            }
        }
        for (Crop crop : crops) {
            App.getInstance().getCurrentGame().getVillage().removeStructure(crop);
        }
        setStructureInAPlace(getPlayerInWitchFarm(player), newCrop, 2, 2, tile.getX(), tile.getY());
    }

    private void setStructureInAPlace(Farm farm, Structure structure, int length, int width, int x, int y) {
        Tile[][] tiles1 = app.getCurrentGame().tiles;
        List<Tile> tiles2 = new ArrayList<>();
        for (int j = x; j < x + length; j++) {
            for (int k = y; k < y + width; k++) {
                tiles1[j][k].setIsFilled(true);
                tiles2.add(tiles1[j][k]);
            }
        }
        structure.getTiles().addAll(tiles2);
        farm.getStructures().add(structure);
    }

    private boolean canBeGiant(Crop... crops) {
        for (Crop crop : crops) {
            if (crop == null) {
                return false;
            }
            if (crop.getIsGiant()) {
                return false;
            }
        }
        return true;
    }

    private TimeAndDate findMinimumTime(TimeAndDate... timeAndDates) {
        TimeAndDate minimum = App.getInstance().getCurrentGame().getTimeAndDate();
        for (TimeAndDate timeAndDate : timeAndDates) {
            if (timeAndDate.getDay() < minimum.getDay()) {
                minimum = timeAndDate;
            }
        }
        return minimum;
    }

    public Response artisanUse(String name, String item1, String item2) {
        if ("honey".equalsIgnoreCase(item1)) {
            int b = 5;
        }
        Player player = app.getCurrentGame().getCurrentPlayer();
        Craft craft = findCraft(name);
        if (craft == null) {
            return new Response("No artisan called " + name + " nearby.");
        }
        Salable product1 = null, product2 = null;
        if (item1 != null && !item1.isEmpty()) {
            for (Salable value : player.getInventory().getProducts().keySet()) {
                if (item1.equalsIgnoreCase(value.getName())) {
                    product1 = value;
                    break;
                }
            }
        }
        if (item1 != null && product1 == null) return new Response(item1 + " not found in your backpack.");
        if (item2 != null && !item2.isEmpty()) {
            for (Salable value : player.getInventory().getProducts().keySet()) {
                if (item2.equalsIgnoreCase(value.getName())) {
                    product2 = value;
                    break;
                }
            }
            if (product2 == null) return new Response(item2 + " not found in your backpack.");
        }

        MadeProductType madeProductType = null;
        for (MadeProductType value : MadeProductType.values()) {
            if (value.getCraft() == craft.getCraftType()) {
                Response isArtisanValid = product1 == null ? new Response("", true) : value.isIngredientsValid(product1,
                        player.getInventory().countProductFromBackPack(product1.getName()),
                        product2 != null);
                if (isArtisanValid.shouldBeBack()) {
                    madeProductType = value;
                    break;
                }
            }
        }
        if (madeProductType == null) return new Response("Items given are not suitable for the craft");
        if (craft.getMadeProduct() != null) return new Response("Craft already in queue.");
        if (product1 != null) {
            product1 = player.getInventory().findProductInBackPackByNAme(product1.getName());
            player.getInventory().deleteProductFromBackPack(product1, player, madeProductType.countIngredient());
        }
        if (product2 != null) {
            product2 = player.getInventory().findProductInBackPackByNAme(MadeProductType.COAL.getName());
            player.getInventory().deleteProductFromBackPack(product2, player, 1);
        }
        craft.setMadeProduct(new MadeProduct(madeProductType, product1));
        craft.setETA(madeProductType.calcETA(product1));
        return new Response("The item will be ready in due time.");
    }

    public Response artisanGet(String name) {
        Player player = app.getCurrentGame().getCurrentPlayer();
        Craft craft = findCraft(name);
        if (craft == null) {
            return new Response("No artisan called " + name + " nearby.");
        }
        if (craft.getMadeProduct() == null) {
            return new Response("No queue underway.");
        }
        if (!player.getInventory().isInventoryHaveCapacity(craft.getMadeProduct())) {
            return new Response("Backpack is full.");
        }
        if (craft.getETA().compareTime(app.getCurrentGame().getTimeAndDate()) < 0) {
            return new Response("Still not ready!");
        }
        player.getInventory().addProductToBackPack(craft.getMadeProduct(), 1);
        craft.setETA(null);
        craft.setMadeProduct(null);
        return new Response("The artisan collected", true);
    }

    public Response C_AddDollars(String count) {
        app.getCurrentGame().getCurrentPlayer().getAccount().removeGolds(-Integer.parseInt(count));
        return new Response(count + "$ added to your account.", true);
    }

    public Response sell(String name, String count) {
        Player player = app.getCurrentGame().getCurrentPlayer();
        ShippingBin shippingBin = null;
        for (ShippingBin bin : player.getShippingBinList()) {
            for (Tile tile : bin.getTiles()) {
                if (Math.abs(tile.getX() - player.getTiles().get(0).getX()) < 2 &&
                        Math.abs(tile.getY() - player.getTiles().get(0).getY()) < 2) {
                    shippingBin = bin;
                    break;
                }
            }
            if (shippingBin != null) break;
        }
        if (shippingBin == null) {
            return new Response("You are not next to any shipping bins");
        }
        Salable salable = player.getInventory().findProductInBackPackByNAme(name);
        if (salable == null) return new Response("Item not found in your backpack");
        if (player.getInventory().countProductFromBackPack(salable.getName()) < Integer.parseInt(count)) {
            return new Response("Not enough in your backpack");
        }
        if (false) return new Response("Item not salable"); //TODO checking not salable
        player.getInventory().deleteProductFromBackPack(salable, player, Integer.parseInt(count));
        shippingBin.add(salable, Integer.parseInt(count));
        return new Response("Item(s) is(are) put in the bin");
    }

    public Response sellAll(String name) {
        Player player = app.getCurrentGame().getCurrentPlayer();
        ShippingBin shippingBin = null;
        for (ShippingBin bin : player.getShippingBinList()) {
            for (Tile tile : bin.getTiles()) {
                if (Math.abs(tile.getX() - player.getTiles().get(0).getX()) < 2 &&
                        Math.abs(tile.getY() - player.getTiles().get(0).getY()) < 2) {
                    shippingBin = bin;
                    break;
                }
            }
            if (shippingBin != null) break;
        }
        if (shippingBin == null) {
            return new Response("You are not next to any shipping bins");
        }
        Salable salable = player.getInventory().findProductInBackPackByNAme(name);
        if (salable == null) return new Response("Item not found in your backpack");
        int count = player.getInventory().countProductFromBackPack(salable.getName());
        if (salable.getSellPrice() == 0) return new Response("Item not salable");
        player.getInventory().deleteProductFromBackPack(salable, player, count);
        shippingBin.add(salable, count);
        return new Response("Item(s) is(are) put in the bin");
    }

    private void setScareCrowAffect(Craft craft) {
        List<Tile> tiles = craft.getCraftType().getTilesAffected(craft.getTiles().get(0));
        for (Tile tile : tiles) {
            List<Structure> structures = App.getInstance().getCurrentGame().getVillage().findStructuresByTile(tile);
            for (Structure structure : structures) {
                if (structure instanceof HarvestAbleProduct) {
                    ((HarvestAbleProduct) structure).setAroundScareCrow(true);
                }
            }
        }
    }

    private void setSprinklerAffect(Craft craft) {
        List<Tile> tiles = craft.getCraftType().getTilesAffected(craft.getTiles().get(0));
        for (Tile tile : tiles) {
            List<Structure> structures = App.getInstance().getCurrentGame().getVillage().findStructuresByTile(tile);
            for (Structure structure : structures) {
                if (structure instanceof HarvestAbleProduct) {
                    ((HarvestAbleProduct) structure).setAroundSprinkler(true);
                }
            }
        }
    }

    private void setScareCrowAndSprinklerForAll() {
        for (Farm farm : App.getInstance().getCurrentGame().getVillage().getFarms()) {
            for (Structure structure : farm.getStructures()) {
                if (structure instanceof Craft && (((Craft) structure).getCraftType().equals(CraftType.SCARE_CROW) ||
                        ((Craft) structure).getCraftType().equals(CraftType.DELUXE_SCARECROW))) {
                    setScareCrowAffect((Craft) structure);
                } else if (structure instanceof Craft && (((Craft) structure).getCraftType().equals(CraftType.SPRINKLER) ||
                        ((Craft) structure).getCraftType().equals(CraftType.QUALITY_SPRINKLER) ||
                        ((Craft) structure).getCraftType().equals(CraftType.IRIDIUM_SPRINKLER))) {
                    setSprinklerAffect((Craft) structure);
                }
            }
        }
    }

    private boolean isThereGreenHouseForHarvest(Tile tile) {
        List<Structure> structures = App.getInstance().getCurrentGame().getVillage().findStructuresByTile(tile);
        for (Structure structure : structures) {
            if (structure instanceof GreenHouse) {
                return !((GreenHouse) structure).getPool().getTiles().contains(tile) && ((GreenHouse) structure).isBuilt();
            }
        }
        return false;
    }

    private boolean isThisFertilize(Salable salable) {
        if (salable instanceof Sundry) {
            if (((Sundry) salable).getSundryType().equals(SundryType.BASIC_RETAINING_SOIL) ||
                    ((Sundry) salable).getSundryType().equals(SundryType.QUALITY_RETAINING_SOIL) ||
                    ((Sundry) salable).getSundryType().equals(SundryType.DELUXE_RETAINING_SOIL) ||
                    ((Sundry) salable).getSundryType().equals(SundryType.SPEED_GROW)) {
                return true;
            }
            return false;
        }
        return false;
    }

    public Response eat(String foodName) {
        Player player = app.getCurrentGame().getCurrentPlayer();
        Salable food = player.getInventory().findProductInBackPackByNAme(foodName);
        if (food == null) return new Response(foodName + " not found in your backpack.");
        if (food.getContainingEnergy() == 0) return new Response(foodName + " is not edible");
        player.getInventory().deleteProductFromBackPack(food, player, 1);
        player.changeEnergy(food.getContainingEnergy());
        if (food instanceof Food) {
            if (((Food) food).getFoodType().getBuff() != null) {
                if (player.getBuff() != null) {
                    player.getBuff().defectBuff(player);
                }
                player.setBuff((Buff) ((Food) food).getFoodType().getBuff().clone());
                player.getBuff().affectBuff(player);
            }
        }
        return new Response(foodName + " is eaten now.");
    }

    public Response buffShow() {
        Player player = app.getCurrentGame().getCurrentPlayer();
        Buff buff = player.getBuff();
        if (buff == null) return new Response("no buff " + player.getMaxEnergy(), true);
        return new Response("" + buff.getBuffImpact() + " " + buff.getMaxPower() + " " + buff.getAbility()
                + " " + player.getMaxEnergy(), true);
    }

    private void updateRecipes() {
        Player player = app.getCurrentGame().getCurrentPlayer();
        switch (player.getAbilityLevel(Ability.MINING)) {
            case 4:
            case 3:
                player.getCraftingRecipes().put(CraftingRecipe.MEGA_BOMB_RECIPE, true);
            case 2:
                player.getCraftingRecipes().put(CraftingRecipe.BOMB_RECIPE, true);
            case 1: {
                player.getCraftingRecipes().put(CraftingRecipe.CHERRY_BOMB_RECIPE, true);
                player.getCookingRecipes().put(CookingRecipe.MINERS_TREAT_RECIPE, true);
            }
        }
        switch (player.getAbilityLevel(Ability.FARMING)) {
            case 4:
            case 3: {
                player.getCraftingRecipes().put(CraftingRecipe.IRIDIUM_SPRINKLER_RECIPE, true);
                player.getCraftingRecipes().put(CraftingRecipe.KEG_RECIPE, true);
                player.getCraftingRecipes().put(CraftingRecipe.LOOM_RECIPE, true);
                player.getCraftingRecipes().put(CraftingRecipe.OIL_MAKER_RECIPE, true);
            }
            case 2: {
                player.getCraftingRecipes().put(CraftingRecipe.QUALITY_SPRINKLER_RECIPE, true);
                player.getCraftingRecipes().put(CraftingRecipe.DELUXE_SCARECROW_RECIPE, true);
                player.getCraftingRecipes().put(CraftingRecipe.CHEESE_PRESS_RECIPE, true);
                player.getCraftingRecipes().put(CraftingRecipe.PRESERVES_JAR_RECIPE, true);
            }
            case 1: {
                player.getCraftingRecipes().put(CraftingRecipe.SPRINKLER_RECIPE, true);
                player.getCraftingRecipes().put(CraftingRecipe.BEE_HOUSE_RECIPE, true);
                player.getCookingRecipes().put(CookingRecipe.FARMERS_LUNCH_RECIPE, true);
            }
        }
        switch (player.getAbilityLevel(Ability.FORAGING)) {
            case 4:
                player.getCraftingRecipes().put(CraftingRecipe.MYSTIC_TREE_SEED_RECIPE, true);
            case 3:
                player.getCookingRecipes().put(CookingRecipe.SURVIVAL_BURGER_RECIPE, true);
            case 2:
                player.getCookingRecipes().put(CookingRecipe.VEGETABLE_MEDLEY_RECIPE, true);
            case 1:
                player.getCraftingRecipes().put(CraftingRecipe.CHARCOAL_KILN_RECIPE, true);
        }

        switch (player.getAbilityLevel(Ability.FISHING)) {
            case 4:
            case 3:
                player.getCookingRecipes().put(CookingRecipe.SEAFOAM_PUDDING_RECIPE, true);
            case 2:
                player.getCookingRecipes().put(CookingRecipe.DISH_O_THE_SEA_RECIPE, true);
        }
    }

    public Response craftingShowRecipes() {
        updateRecipes();
        Player player = app.getCurrentGame().getCurrentPlayer();
        Map<CraftingRecipe, Boolean> craftingRecipeList = player.getCraftingRecipes();
        if (craftingRecipeList.isEmpty()) return new Response("No recipe found you loser.");
        StringBuilder response = new StringBuilder("Crafting recipes you've learnt so far: \n");
        for (CraftingRecipe craftingRecipe : craftingRecipeList.keySet()) {
            if (craftingRecipeList.get(craftingRecipe)) {
                response.append(craftingRecipe.toString()).append("\n");
            }
        }
        return new Response(response.toString(), true);
    }

    public Response cookingShowRecipes() {
        updateRecipes();
        Player player = app.getCurrentGame().getCurrentPlayer();
        Map<CookingRecipe, Boolean> cookingRecipeList = player.getCookingRecipes();
        if (cookingRecipeList.isEmpty()) return new Response("No recipe found you loser.");
        StringBuilder response = new StringBuilder("Cooking recipes you've learnt so far: \n");
        for (CookingRecipe cookingRecipe : cookingRecipeList.keySet()) {
            if (cookingRecipeList.get(cookingRecipe)) {
                response.append(cookingRecipe.toString()).append("\n");
            }
        }
        return new Response(response.append("\n").toString(), true);
    }

    public Response craftingCraft(String name) {
        updateRecipes();
        Player player = app.getCurrentGame().getCurrentPlayer();
        CraftingRecipe recipe = player.findCraftingRecipe(name);
        if (recipe == null) return new Response("You've not learnt to craft " + name);
        Response isPossible = recipe.getCraft().isCraftingPossible(player);
        if (!isPossible.shouldBeBack()) return isPossible;
        if (!player.getInventory().isInventoryHaveCapacity(new Craft(recipe.getCraft(), null, null))) {
            return new Response("You don't have enough space in your backpack");
        }
        if (player.getEnergy() < 2) {
            player.faint();
            nextTurn();
            return new Response("Not enough energy; you fainted");
        }
        player.removeEnergy(2);
        recipe.getCraft().removeIngredients(player);
        player.getInventory().addProductToBackPack(new Craft(recipe.getCraft(), null, null), 1);
        if (player.getEnergyPerTurn() <= 0) nextTurn();
        return new Response(recipe.getCraft().getName() + " crafted successfully.");
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

    public Response fridgeShow() {
        Fridge fridge = app.getCurrentGame().findFarm().getFridge();
        return new Response(fridge.showInventory(), true);
    }


    public Response cookingRefrigeratorPut(String name) {
        Player player = app.getCurrentGame().getCurrentPlayer();
        Fridge fridge = app.getCurrentGame().findFarm().getFridge();
        Salable product = player.getInventory().getProductFromBackPack(name);
        if (product == null) {
            return new Response(name + " not found in the backpack");
        }
        if (product.getContainingEnergy() == 0) {
            return new Response("Can't put the inedible items in the refrigerator.");
        }
        fridge.addProduct(product, player.getInventory().countProductFromBackPack(product.getName()));
        player.getInventory().deleteProductFromBackPack(product, player,
                player.getInventory().countProductFromBackPack(product.getName()));
        return new Response("Put down.");
    }

    public Response cookingPrepare(String name) {
        updateRecipes();
        Player player = app.getCurrentGame().getCurrentPlayer();
        Fridge fridge = app.getCurrentGame().findFarm().getFridge();
        CookingRecipe recipe = player.findCookingRecipe(name + " recipe");
        if (recipe == null) return new Response("You've not learnt to cook " + name);
        boolean isPossible = recipe.getIngredients().isValidIngredient(fridge, player);
        if (!isPossible) return new Response("Ingredients not found in the refrigerator.");
        if (!player.getInventory().isInventoryHaveCapacity(recipe.getIngredients())) {
            return new Response("You don't have enough space in your backpack or fridge");
        }
        if (player.getEnergy() < 3) {
            player.faint();
            nextTurn();
            return new Response("Not enough energy; you fainted");
        }
        player.removeEnergy(3);
        recipe.getIngredients().removeIngredients(fridge, player);
        player.getInventory().addProductToBackPack(new Food(recipe.getIngredients()), 1);
        if (player.getEnergyPerTurn() <= 0) nextTurn();
        return new Response(recipe.getIngredients().getName() + " cooked successfully.");
    }

    private Response isStoreOpen() {
        StoreType storeType = app.getCurrentGame().getCurrentPlayer().getStoreType();
        if (new TimeAndDate(0, storeType.getOpenDoorTime()).compareDailyTime(app.getCurrentGame().getTimeAndDate()) < 0 ||
                new TimeAndDate(0, storeType.getCloseDoorTime()).compareDailyTime(app.getCurrentGame().getTimeAndDate()) > 0) {
            return new Response("Store closed.");
        }
        return new Response("", true);
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

    private Craft findCraft(String name) {
        Player player = app.getCurrentGame().getCurrentPlayer();
        Farm farm = app.getCurrentGame().findFarm();
        for (Structure structure : farm.getStructures()) {
            if (structure instanceof Craft) {
                if (((Craft) structure).getName().equalsIgnoreCase(name)) {
                    Pair pair = new Pair(structure.getTiles().getFirst().getX(), structure.getTiles().getFirst().getY());
                    Pair origin = new Pair(player.getTiles().getFirst().getX(), player.getTiles().getFirst().getY());
                    if (Math.abs(pair.getX() - origin.getX()) <= 1 && Math.abs(pair.getY() - origin.getY()) <= 1) {
                        return (Craft) structure;
                    }
                }
            }
        }
        return null;
    }
}
