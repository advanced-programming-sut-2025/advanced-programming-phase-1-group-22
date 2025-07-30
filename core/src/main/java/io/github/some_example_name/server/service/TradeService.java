package io.github.some_example_name.server.service;

import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.common.model.Game;
import io.github.some_example_name.common.model.NotificationType;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.dto.TradePriceDto;
import io.github.some_example_name.common.model.dto.TradeProductDto;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.relations.Trade;
import io.github.some_example_name.common.utils.App;

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
        player.notify(new Response("%s offered you a new trade".formatted(currentPlayer.getUser().getUsername())),
            NotificationType.TRADE, currentPlayer);
        GameClient.getInstance().setTrade(trade1);
        trade2.getTrader().getGootenTradeList().add(trade2);
        trade2.getCustomer().getGootenTradeList().add(trade2);
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
        player.notify
            (new Response("%s offered you a new trade".formatted(currentPlayer.getUser().getUsername())),
                NotificationType.TRADE, currentPlayer);
        GameClient.getInstance().setTrade(trade1);
        trade2.getTrader().getGootenTradeList().add(trade2);
        trade2.getCustomer().getGootenTradeList().add(trade2);
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
        player.notify(new Response("%s requested you a new trade".formatted(currentPlayer.getUser().getUsername())),
            NotificationType.TRADE, currentPlayer);
        GameClient.getInstance().setTrade(trade1);
        trade2.getTrader().getGootenTradeList().add(trade2);
        trade2.getCustomer().getGootenTradeList().add(trade2);
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
        player.notify(new Response("%s requested you a new trade".formatted(currentPlayer.getUser().getUsername())),
            NotificationType.TRADE, currentPlayer);
        GameClient.getInstance().setTrade(trade1);
        trade2.getTrader().getGootenTradeList().add(trade2);
        trade2.getCustomer().getGootenTradeList().add(trade2);
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
            GameClient.getInstance().rejectTrade(trade1);
            rejectTrade(trade1.getCustomer(), trade1.getId());
            return new Response("trade rejected successfully");
        }

        if (trade1.getCustomer().equals(currentPlayer)) {
            Map.Entry<Salable, Integer> itemFromInventory = trade1.getTrader().getItemFromInventory(trade1.getSalable());
            if (trade1.getPrice() != null) {
                if (currentPlayer.getAccount().getGolds() < trade1.getPrice()) {
                    return new Response("you don't have enough golds");
                }
                GameClient.getInstance().acceptTrade(trade1.getTrader(), tradeId, true);
                return new Response("", true);
            } else {
                Map.Entry<Salable, Integer> itemFromInventory1 = currentPlayer.getItemFromInventory(trade1.getRequiredItem());
                if (itemFromInventory1 == null || itemFromInventory1.getValue() < trade1.getQuantityRequired()) {
                    return new Response("RequiredItem not found");
                }
                GameClient.getInstance().acceptTrade(trade1.getTrader(), tradeId, true);
                return new Response("", true);
            }
        } else {
            Map.Entry<Salable, Integer> itemFromInventory = currentPlayer.getItemFromInventory(trade1.getSalable());
            if (itemFromInventory == null || itemFromInventory.getValue() < trade1.getQuantity()) {
                return new Response("you don't have enough Item to sell");
            }
            GameClient.getInstance().acceptTrade(trade1.getTrader(), tradeId, true);
            return new Response("", true);
        }
    }

    public void rejectTrade(Player customer, int id) {
        for (Trade trade : customer.getGootenTradeList()) {
            if (trade.getId() == id) {
                trade.setIsAnswered(true);
                trade.setIsSuccessfulled(false);
                relationService.changeFriendShipLevelDown(relationService.getFriendShipBetweenTwoActors(trade.getTrader(), trade.getCustomer()), 30);
                return;
            }
        }
    }

    public void addTradeByServer(Player customer, Player trader, String salable, Integer quantity, String requiredItem,
                            Integer requiredQuantity, Integer price, Integer id) {
        Trade trade;
        if (price == null) {
            trade = new Trade(customer, trader, salable, quantity, requiredQuantity, requiredItem, true);
        } else {
            trade = new Trade(customer, trader, salable, quantity, price, true);
        }
        trade.setId(id);
        trader.getGootenTradeList().add(trade);
        customer.getGootenTradeList().add(trade);
    }

    public void handleAccept(Player customer, int id) {
        for (Trade trade : customer.getGootenTradeList()) {
            if (trade.getId() == id) {
                if (trade.getTrader().equals(currentPlayer)) {
                    Map.Entry<Salable, Integer> itemFromInventory = trade.getTrader().getItemFromInventory(trade.getSalable());
                    if (itemFromInventory == null || itemFromInventory.getValue() < trade.getQuantity()) {
                        GameClient.getInstance().rejectTradeByState(trade);
                        rejectTradeByState(trade.getCustomer(), trade.getId());
                        return;
                    }
                    if (itemFromInventory.getValue() - trade.getQuantity() == 0) {
                        trade.getTrader().getInventory().getProducts().remove(itemFromInventory.getKey());
                    } else {
                        trade.getTrader().getInventory().getProducts().replace(itemFromInventory.getKey(), itemFromInventory.getValue() - trade.getQuantity());
                    }

                    trade.setIsAnswered(true);
                    relationService.changeFriendShipLevelUp(relationService.getFriendShipBetweenTwoActors(trade.getTrader(), trade.getCustomer()), 50);
                    trade.setIsSuccessfulled(true);
                    GameClient.getInstance().sendTrade(trade.getCustomer(), trade, trade.getQuantity(), itemFromInventory.getKey(), true);
                } else {
                    if (trade.getPrice() != null) {
                        if (trade.getCustomer().getAccount().getGolds() < trade.getPrice()) {
                            GameClient.getInstance().rejectTradeByState(trade);
                            rejectTradeByState(trade.getCustomer(), trade.getId());
                            return;
                        }
                        trade.getCustomer().getAccount().setGolds(trade.getCustomer().getAccount().getGolds() - trade.getPrice());
                        GameClient.getInstance().sendTrade(trade.getCustomer(), trade, trade.getPrice(), null, true);
                    } else {
                        Map.Entry<Salable, Integer> itemFromInventory1 = trade.getCustomer().getItemFromInventory(trade.getRequiredItem());
                        if (itemFromInventory1 == null || itemFromInventory1.getValue() < trade.getQuantityRequired()) {
                            GameClient.getInstance().rejectTradeByState(trade);
                            rejectTradeByState(trade.getCustomer(), trade.getId());
                            return;
                        }
                        if (itemFromInventory1.getValue() - trade.getQuantityRequired() == 0) {
                            trade.getTrader().getInventory().getProducts().remove(itemFromInventory1.getKey());
                        } else {
                            trade.getCustomer().getInventory().getProducts().replace(itemFromInventory1.getKey(), itemFromInventory1.getValue() - trade.getQuantity());
                        }

                        GameClient.getInstance().sendTrade(trade.getCustomer(), trade, trade.getQuantityRequired(), itemFromInventory1.getKey(), true);
                    }
                    trade.setIsAnswered(true);
                    trade.setIsSuccessfulled(true);
                    relationService.changeFriendShipLevelUp(relationService.getFriendShipBetweenTwoActors(trade.getTrader(), trade.getCustomer()), 50);
                }
                return;
            }
        }
    }

    public void rejectTradeByState(Player customer, Integer id) {
        for (Trade trade : customer.getGootenTradeList()) {
            if (trade.getId() == id) {
                trade.setIsAnswered(true);
                relationService.changeFriendShipLevelUp(relationService.getFriendShipBetweenTwoActors(trade.getTrader(), trade.getCustomer()), -50);
                trade.setIsSuccessfulled(false);
            }
        }

    }

    public void sendTrade(Player customer, Integer id, Salable salable, Integer count, boolean shouldRespond) {
        for (Trade trade : customer.getGootenTradeList()) {
            if (trade.getId().equals(id)) {
                currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
                if (salable == null) {
                    currentPlayer.getAccount().setGolds(currentPlayer.getAccount().getGolds() + count);
                } else {
                    currentPlayer.getInventory().addProductToBackPack(salable, count);
                }
                if (shouldRespond) {
                    if (trade.getCustomer().equals(currentPlayer)) {
                        if (trade.getPrice() != null) {
                            trade.getCustomer().getAccount().setGolds(trade.getCustomer().getAccount().getGolds() - trade.getPrice());
                            trade.setIsAnswered(true);
                            trade.setIsSuccessfulled(true);
                            relationService.changeFriendShipLevelUp(relationService.getFriendShipBetweenTwoActors(trade.getTrader(), trade.getCustomer()), 50);
                            GameClient.getInstance().sendTrade(trade.getTrader(), trade, trade.getPrice(), null, false);
                        } else {
                            Map.Entry<Salable, Integer> itemFromInventory = trade.getCustomer().getItemFromInventory(trade.getRequiredItem());
                            trade.setIsAnswered(true);
                            trade.setIsSuccessfulled(true);
                            if (itemFromInventory.getValue() - trade.getQuantityRequired() == 0) {
                                trade.getTrader().getInventory().getProducts().remove(itemFromInventory.getKey());
                            } else {
                                trade.getCustomer().getInventory().getProducts().replace(itemFromInventory.getKey(), itemFromInventory.getValue() - trade.getQuantity());
                            }
                            relationService.changeFriendShipLevelUp(relationService.getFriendShipBetweenTwoActors(trade.getTrader(), trade.getCustomer()), 50);
                            GameClient.getInstance().sendTrade(trade.getTrader(), trade, trade.getQuantityRequired(), itemFromInventory.getKey(), false);
                        }
                    } else {
                        Map.Entry<Salable, Integer> itemFromInventory = trade.getTrader().getItemFromInventory(trade.getSalable());
                        trade.getTrader().getInventory().getProducts().replace(itemFromInventory.getKey(), itemFromInventory.getValue() - trade.getQuantity());
                        if (itemFromInventory.getValue() - trade.getQuantity() == 0) {
                            trade.getTrader().getInventory().getProducts().remove(itemFromInventory.getKey());
                        }
                        trade.setIsAnswered(true);
                        trade.setIsSuccessfulled(true);
                        relationService.changeFriendShipLevelUp(relationService.getFriendShipBetweenTwoActors(trade.getTrader(), trade.getCustomer()), 50);
                        GameClient.getInstance().sendTrade(trade.getCustomer(), trade, trade.getQuantity(), itemFromInventory.getKey(), false);
                    }
                }
            }
        }
    }
}
