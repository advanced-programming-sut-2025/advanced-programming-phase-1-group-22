package io.github.some_example_name.service;

import io.github.some_example_name.model.Game;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.dto.TradePriceDto;
import io.github.some_example_name.model.dto.TradeProductDto;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.relations.Trade;
import io.github.some_example_name.utils.App;

import java.util.Map;

public class TradeService {
    Game game = App.getInstance().getCurrentGame();
    Player currentPlayer = game.getCurrentPlayer();
    RelationService relationService = RelationService.getInstance();
    private static TradeService instance;

    private TradeService() {
    }

    public static TradeService getInstance() {
        if (instance == null) {
            instance = new TradeService();
        }
        return instance;
    }


    public Response startTrade() {
        Game game = App.getInstance().getCurrentGame();
        StringBuilder stringBuilder = new StringBuilder();
        for (Player player : game.getPlayers()) {
            stringBuilder.append(player.toString());
        }
        return new Response(stringBuilder.toString());
    }


    public Response tradePriceOffer(TradePriceDto tradeDto) {
        Game game = App.getInstance().getCurrentGame();
        Player currentPlayer = game.getCurrentPlayer();
        String username = tradeDto.username();
        String itemName = tradeDto.item();
        int amount = tradeDto.amount();
        int price = tradeDto.price();
        int availableItemNum = 0;
        Salable item = null;
        Player player = relationService.getPlayer(username);
        if (player == null) {
            return new Response("Player with given username not found");
        }
        if (amount < 1) {
            return new Response("the amount must natural number");
        }
        for (Map.Entry<Salable, Integer> salableIntegerEntry : currentPlayer.getInventory().getProducts().entrySet()) {
            if (salableIntegerEntry.getKey().getName().equals(itemName)) {
                item = salableIntegerEntry.getKey();
                availableItemNum = salableIntegerEntry.getValue();
            }
        }
        if (item == null) {
            return new Response("item not found");
        }
        if (availableItemNum < amount) {
            return new Response("the amount of " + itemName + " is not enough");
        }
        Trade trade1 = new Trade(player, currentPlayer, itemName, amount, price, true);
        Trade trade2 = new Trade(player, currentPlayer, itemName, amount, price, false);
        player.notify(new Response("%s offered you a new trade".formatted(currentPlayer.getUser().getUsername())));
        player.getGootenTradeList().add(trade1);
        currentPlayer.getGootenTradeList().add(trade2);
        return new Response("trade offer added with price requirement");
    }

    public Response tradeProductOffer(TradeProductDto tradeDto) {
        Game game = App.getInstance().getCurrentGame();
        Player currentPlayer = game.getCurrentPlayer();
        String username = tradeDto.username();
        String itemName = tradeDto.item();
        int amount = tradeDto.amount();
        int targetAmount = tradeDto.targetAmount();
        String targetItem = tradeDto.targetItem();
        int availableItemNum = 0;
        Salable item = null;
        Player player = relationService.getPlayer(username);
        if (player == null) {
            return new Response("Player with given username not found");
        }
        if (amount < 1) {
            return new Response("the amount must natural number");
        }
        if (targetAmount < 1) {
            return new Response("the targetAmount should be greater than 0");
        }
        for (Map.Entry<Salable, Integer> salableIntegerEntry : currentPlayer.getInventory().getProducts().entrySet()) {
            if (salableIntegerEntry.getKey().getName().equals(itemName)) {
                item = salableIntegerEntry.getKey();
                availableItemNum = salableIntegerEntry.getValue();
            }
        }
        if (item == null) {
            return new Response("item not found");
        }
        if (availableItemNum < amount) {
            return new Response("the amount of " + itemName + " is not enough");
        }
        Trade trade1 = new Trade(player, currentPlayer, itemName, amount, targetAmount, targetItem, true);
        Trade trade2 = new Trade(player, currentPlayer, itemName, amount, targetAmount, targetItem, false);
        player.notify(new Response("%s offered you a new trade".formatted(currentPlayer.getUser().getUsername())));
        player.getGootenTradeList().add(trade1);
        currentPlayer.getGootenTradeList().add(trade2);
        return new Response("trade offer added with product requirement");
    }

    public Response tradePriceRequest(TradePriceDto tradeDto) {
        Game game = App.getInstance().getCurrentGame();
        Player currentPlayer = game.getCurrentPlayer();
        String username = tradeDto.username();
        String itemName = tradeDto.item();
        int amount = tradeDto.amount();
        int price = tradeDto.price();
        Player player = relationService.getPlayer(username);
        if (player == null) {
            return new Response("Player with given username not found");
        }
        if (amount < 1) {
            return new Response("the amount must natural number");
        }
        if (price > currentPlayer.getAccount().getGolds()) {
            return new Response("you don't have enough golds");
        }
        Trade trade1 = new Trade(currentPlayer, player, itemName, amount, price, true);
        Trade trade2 = new Trade(currentPlayer, player, itemName, amount, price, false);
        player.notify(new Response("%s requested you a new trade".formatted(currentPlayer.getUser().getUsername())));
        player.getGootenTradeList().add(trade1);
        currentPlayer.getGootenTradeList().add(trade2);
        return new Response("trade request added with price offering");
    }

    public Response tradeProductRequest(TradeProductDto tradeDto) {
        Game game = App.getInstance().getCurrentGame();
        Player currentPlayer = game.getCurrentPlayer();
        String username = tradeDto.username();
        String itemName = tradeDto.item();
        int amount = tradeDto.amount();
        int targetAmount = tradeDto.targetAmount();
        String targetItem = tradeDto.targetItem();
        Player player = relationService.getPlayer(username);
        if (player == null) {
            return new Response("Player with given username not found");
        }
        if (amount < 1) {
            return new Response("the amount must natural number");
        }
        if (targetAmount < 1) {
            return new Response("the targetAmount should be greater than 0");
        }
        int availableItemNum = 0;
        Salable item = null;
        for (Map.Entry<Salable, Integer> salableIntegerEntry : currentPlayer.getInventory().getProducts().entrySet()) {
            if (salableIntegerEntry.getKey().getName().equals(targetItem)) {
                item = salableIntegerEntry.getKey();
                availableItemNum = salableIntegerEntry.getValue();
            }
        }
        if (availableItemNum < targetAmount) {
            return new Response("the amount of " + targetItem + " is not enough");
        }
        Trade trade1 = new Trade(currentPlayer, player, itemName, amount, targetAmount, targetItem, true);
        Trade trade2 = new Trade(currentPlayer, player, itemName, amount, targetAmount, targetItem, false);
        player.notify(new Response("%s requested you a new trade".formatted(currentPlayer.getUser().getUsername())));
        player.getGootenTradeList().add(trade1);
        currentPlayer.getGootenTradeList().add(trade2);
        return new Response("trade request added with product offering");
    }

    public Response tradeList() {
        Game game = App.getInstance().getCurrentGame();
        Player currentPlayer = game.getCurrentPlayer();
        StringBuilder stringBuilder = new StringBuilder();
        for (Trade trade : currentPlayer.getGootenTradeList()) {
            if (trade.getIShouldAnswer() && !trade.getIsAnswered()) {
                stringBuilder.append(trade.toString()).append("\n");
            }
        }
        return new Response(stringBuilder.toString());
    }

    public Response tradeHistory() {
        Game game = App.getInstance().getCurrentGame();
        Player currentPlayer = game.getCurrentPlayer();
        StringBuilder stringBuilder = new StringBuilder();
        for (Trade trade : currentPlayer.getGootenTradeList()) {
            if (!trade.getIShouldAnswer()) {
                stringBuilder.append(trade.toString()).append("\n");
            }
        }
        return new Response(stringBuilder.toString());
    }

    public Response tradeResponse(boolean accept, int tradeId) {
        Game game = App.getInstance().getCurrentGame();
        Player currentPlayer = game.getCurrentPlayer();
        Trade trade1 = null;
        for (Trade trade : currentPlayer.getGootenTradeList()) {
            if (trade.getId().equals(tradeId)) {
                trade1 = trade;
            }
        }
        if (trade1 == null) {
            return new Response("tradeId not found");
        }
        if (trade1.getIsAnswered()) {
            return new Response("you have answered this trade already");
        }
        if (!accept) {
            trade1.setIsAnswered(true);
            trade1.setIsSuccessfulled(false);
            if (trade1.getCustomer().equals(currentPlayer)) {
                relationService.changeFriendShipLevelDown(relationService.getFriendShipBetweenTwoActors(trade1.getTrader()), 30);
            } else {
                relationService.changeFriendShipLevelDown(relationService.getFriendShipBetweenTwoActors(trade1.getCustomer()), 30);

            }
            return new Response("trade rejected successfully");
        }
        if (trade1.getCustomer().equals(currentPlayer)) {
            Map.Entry<Salable, Integer> itemFromInventory = trade1.getTrader().getItemFromInventory(trade1.getSalable());
            if (trade1.getPrice() != null) {
                if (currentPlayer.getAccount().getGolds() < trade1.getPrice()) {
                    return new Response("you don't have enough golds");
                }
                currentPlayer.getAccount().setGolds(currentPlayer.getAccount().getGolds() - trade1.getPrice());
                trade1.getTrader().getAccount().setGolds(currentPlayer.getAccount().getGolds() + trade1.getPrice());
                currentPlayer.getInventory().addProductToBackPack(itemFromInventory.getKey(), trade1.getQuantity());
                trade1.getTrader().getInventory().getProducts().replace(itemFromInventory.getKey(), itemFromInventory.getValue() - trade1.getQuantity());
                if (itemFromInventory.getValue() - trade1.getQuantity() == 0) {
                    trade1.getTrader().getInventory().getProducts().remove(itemFromInventory.getKey());
                }
                trade1.setIsAnswered(true);
                relationService.changeFriendShipLevelUp(relationService.getFriendShipBetweenTwoActors(trade1.getTrader()), 50);
                trade1.setIsSuccessfulled(true);
                return new Response("trade accepted successfully");
            } else {
                Map.Entry<Salable, Integer> itemFromInventory1 = currentPlayer.getItemFromInventory(trade1.getRequiredItem());
                if (itemFromInventory1 == null || itemFromInventory1.getValue() < trade1.getQuantityRequired()) {
                    trade1.setIsAnswered(true);
                    trade1.setIsSuccessfulled(false);
                    relationService.changeFriendShipLevelDown(relationService.getFriendShipBetweenTwoActors(trade1.getTrader()), 30);
                    return new Response("RequiredItem not found");
                }
                trade1.getTrader().getInventory().addProductToBackPack(itemFromInventory1.getKey(), trade1.getQuantity());
                currentPlayer.getInventory().getProducts().replace(itemFromInventory1.getKey(), itemFromInventory1.getValue() - trade1.getQuantity());
                if (itemFromInventory.getValue() - trade1.getQuantity() == 0) {
                    trade1.getTrader().getInventory().getProducts().remove(itemFromInventory1.getKey());
                }
                currentPlayer.getInventory().addProductToBackPack(itemFromInventory.getKey(), trade1.getQuantity());
                trade1.getTrader().getInventory().getProducts().replace(itemFromInventory.getKey(), itemFromInventory.getValue() - trade1.getQuantity());
                if (itemFromInventory.getValue() - trade1.getQuantity() == 0) {
                    trade1.getTrader().getInventory().getProducts().remove(itemFromInventory.getKey());
                }
                trade1.setIsAnswered(true);
                trade1.setIsSuccessfulled(true);
                relationService.changeFriendShipLevelUp(relationService.getFriendShipBetweenTwoActors(trade1.getTrader()), 50);
                return new Response("trade accepted successfully");
            }

        } else {
            Map.Entry<Salable, Integer> itemFromInventory = currentPlayer.getItemFromInventory(trade1.getSalable());
            if (itemFromInventory == null || itemFromInventory.getValue() < trade1.getQuantity()) {
                trade1.setIsAnswered(true);
                trade1.setIsSuccessfulled(false);
                relationService.changeFriendShipLevelDown(relationService.getFriendShipBetweenTwoActors(trade1.getCustomer()), 30);
                return new Response("you don't have enough Item to sell");
            }
            if (trade1.getPrice() != null) {
                currentPlayer.getAccount().setGolds(currentPlayer.getAccount().getGolds() + trade1.getPrice());
                trade1.getTrader().getAccount().setGolds(currentPlayer.getAccount().getGolds() - trade1.getPrice());
                trade1.getTrader().getInventory().addProductToBackPack(itemFromInventory.getKey(), trade1.getQuantity());
                currentPlayer.getInventory().getProducts().replace(itemFromInventory.getKey(), itemFromInventory.getValue() - trade1.getQuantity());
                if (itemFromInventory.getValue() - trade1.getQuantity() == 0) {
                    trade1.getTrader().getInventory().getProducts().remove(itemFromInventory.getKey());
                }
                trade1.setIsAnswered(true);
                trade1.setIsSuccessfulled(true);
                relationService.changeFriendShipLevelUp(relationService.getFriendShipBetweenTwoActors(trade1.getCustomer()), 50);
                return new Response("trade accepted successfully");
            } else {
                Map.Entry<Salable, Integer> itemFromInventory1 = trade1.getTrader().getItemFromInventory(trade1.getRequiredItem());

                currentPlayer.getInventory().addProductToBackPack(itemFromInventory1.getKey(), trade1.getQuantity());
                trade1.getTrader().getInventory().getProducts().replace(itemFromInventory1.getKey(), itemFromInventory1.getValue() - trade1.getQuantity());
                if (itemFromInventory.getValue() - trade1.getQuantity() == 0) {
                    trade1.getTrader().getInventory().getProducts().remove(itemFromInventory1.getKey());
                }
                trade1.getTrader().getInventory().addProductToBackPack(itemFromInventory.getKey(), trade1.getQuantity());
                currentPlayer.getInventory().getProducts().replace(itemFromInventory.getKey(), itemFromInventory.getValue() - trade1.getQuantity());
                if (itemFromInventory.getValue() - trade1.getQuantity() == 0) {
                    trade1.getTrader().getInventory().getProducts().remove(itemFromInventory.getKey());
                }
                trade1.setIsAnswered(true);
                trade1.setIsSuccessfulled(true);
                relationService.changeFriendShipLevelUp(relationService.getFriendShipBetweenTwoActors(trade1.getCustomer()), 50);
                return new Response("trade accepted successfully");
            }
        }
    }
}
