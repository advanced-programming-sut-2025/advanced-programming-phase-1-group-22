package service;

import model.Game;
import model.Salable;
import model.dto.TradePriceDto;
import model.dto.TradeProductDto;
import model.records.Response;
import model.relations.Player;
import model.relations.Trade;
import utils.App;

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
        StringBuilder stringBuilder = new StringBuilder();
        for (Player player : game.getPlayers()) {
            stringBuilder.append(player.toString());
        }
        return new Response(stringBuilder.toString());
    }


    public Response tradePriceOffer(TradePriceDto tradeDto) {
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
        return Response.empty();
    }

    public Response tradeProductOffer(TradeProductDto tradeDto) {
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
        return Response.empty();
    }

    public Response tradePriceRequest(TradePriceDto tradeDto) {
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
        return Response.empty();
    }

    public Response tradeProductRequest(TradeProductDto tradeDto) {
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
        Trade trade1 = new Trade(currentPlayer, player, itemName, amount, targetAmount, targetItem, true);
        Trade trade2 = new Trade(currentPlayer, player, itemName, amount, targetAmount, targetItem, false);
        player.notify(new Response("%s requested you a new trade".formatted(currentPlayer.getUser().getUsername())));
        player.getGootenTradeList().add(trade1);
        currentPlayer.getGootenTradeList().add(trade2);
        return Response.empty();
    }

    public Response tradeList() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Trade trade : currentPlayer.getGootenTradeList()) {
            if (trade.getIShouldAnswer()) {
                stringBuilder.append(trade.toString());
            }
        }
        return new Response(stringBuilder.toString());
    }

    public Response tradeHistory() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Trade trade : currentPlayer.getGootenTradeList()) {
            if (!trade.getIShouldAnswer()) {
                stringBuilder.append(trade.toString());
            }
        }
        return new Response(stringBuilder.toString());
    }

    public Response tradeResponse(boolean accept, int tradeId) {
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
                trade1.setIsAnswered(true);
                trade1.setIsSuccessfulled(true);
            } else {
                Map.Entry<Salable, Integer> itemFromInventory1 = currentPlayer.getItemFromInventory(trade1.getRequiredItem());
                if (itemFromInventory1 == null || itemFromInventory1.getValue() < trade1.getQuantityRequired()) {
                    trade1.setIsAnswered(true);
                    trade1.setIsSuccessfulled(false);
                    return new Response("RequiredItem not found");
                }
                trade1.getTrader().getInventory().addProductToBackPack(itemFromInventory1.getKey(), trade1.getQuantity());
                currentPlayer.getInventory().getProducts().replace(itemFromInventory1.getKey(), itemFromInventory1.getValue() - trade1.getQuantity());
                currentPlayer.getInventory().addProductToBackPack(itemFromInventory.getKey(), trade1.getQuantity());
                trade1.getTrader().getInventory().getProducts().replace(itemFromInventory.getKey(), itemFromInventory.getValue() - trade1.getQuantity());
                trade1.setIsAnswered(true);
                trade1.setIsSuccessfulled(true);
            }

        } else {
            Map.Entry<Salable, Integer> itemFromInventory = currentPlayer.getItemFromInventory(trade1.getSalable());
            if (itemFromInventory == null || itemFromInventory.getValue() < trade1.getQuantity()) {
                trade1.setIsAnswered(true);
                trade1.setIsSuccessfulled(false);
                return new Response("you don't have enough Item to sell");
            }
            if (trade1.getPrice() != null) {
                currentPlayer.getAccount().setGolds(currentPlayer.getAccount().getGolds() + trade1.getPrice());
                trade1.getTrader().getAccount().setGolds(currentPlayer.getAccount().getGolds() - trade1.getPrice());
                trade1.getTrader().getInventory().addProductToBackPack(itemFromInventory.getKey(), trade1.getQuantity());
                currentPlayer.getInventory().getProducts().replace(itemFromInventory.getKey(), itemFromInventory.getValue() - trade1.getQuantity());
                trade1.setIsAnswered(true);
                trade1.setIsSuccessfulled(true);
            } else {
                Map.Entry<Salable, Integer> itemFromInventory1 = trade1.getTrader().getItemFromInventory(trade1.getRequiredItem());

                currentPlayer.getInventory().addProductToBackPack(itemFromInventory1.getKey(), trade1.getQuantity());
                trade1.getTrader().getInventory().getProducts().replace(itemFromInventory1.getKey(), itemFromInventory1.getValue() - trade1.getQuantity());
                trade1.getTrader().getInventory().addProductToBackPack(itemFromInventory.getKey(), trade1.getQuantity());
                currentPlayer.getInventory().getProducts().replace(itemFromInventory.getKey(), itemFromInventory.getValue() - trade1.getQuantity());
                trade1.setIsAnswered(true);
                trade1.setIsSuccessfulled(true);
            }
        }
        return Response.empty();
    }
}
