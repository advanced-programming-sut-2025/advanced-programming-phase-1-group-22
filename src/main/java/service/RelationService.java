package service;

import model.*;
import model.enums.Gender;
import model.records.Response;
import model.relations.Account;
import model.relations.Friendship;
import model.relations.Gift;
import model.relations.Player;
import model.structure.stores.PierreShop;
import utils.App;

import java.util.Map;

public class RelationService {
    private static RelationService instance;

    private RelationService() {
    }

    public static RelationService getInstance() {
        if (instance == null) {
            instance = new RelationService();
        }
        return instance;
    }

    Game game = App.getInstance().getCurrentGame();
    Player currentPlayer = game.getCurrentPlayer();

    public Response showMyFriendShips() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Friendship friendship : game.getFriendships()) {
            if (friendship.getFirstPlayer().equals(currentPlayer)) {
                stringBuilder.append(friendship.toString());
            }
        }
        return new Response(stringBuilder.toString());
    }

    public Response talkToAnotherPlayer(String username, String message) {
        Player anotherPlayer = getPlayer(username);
        if (anotherPlayer == null) {
            return new Response("Player with that username not found");
        }
        if (!twoActorsAreNeighbors(currentPlayer, anotherPlayer)) {
            return new Response("You can't talk to the other player");
        }
        Friendship friendship = getFriendShipBetweenTwoActors(anotherPlayer);
        friendship.getDialogs().put(message, currentPlayer);
        if (currentPlayer.getCouple().equals(anotherPlayer)) {
            friendship.setXp(friendship.getXp() + 50);
        }
        friendship.setXp(friendship.getXp() + 20);
        anotherPlayer.notify(new Response("%s called you!".formatted(currentPlayer.getUser().getUsername())));
        return new Response("message sent successfully");
    }


    public Response showTalkHistories(String username) {
        Player player = getPlayer(username);
        if (player == null) {
            return new Response("Player with that username not found");
        }
        Friendship friendShipBetweenTwoActors = getFriendShipBetweenTwoActors(player);
        if (friendShipBetweenTwoActors == null) {
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(friendShipBetweenTwoActors.getDialogs());
        return new Response(stringBuilder.toString());
    }

    private Friendship getFriendShipBetweenTwoActors(Player anotherPlayer) {
        for (Friendship friendship : game.getFriendships()) {
            if ((friendship.getFirstPlayer().equals(currentPlayer) &&
                    friendship.getSecondPlayer().equals(anotherPlayer)) || (friendship.getSecondPlayer().equals(currentPlayer) &&
                    friendship.getFirstPlayer().equals(anotherPlayer))) {
                return friendship;
            }
        }
        return null;
    }

    private boolean twoActorsAreNeighbors(Player currentPlayer, Player anotherPlayer) {
        Tile tile = currentPlayer.getTiles().get(0);
        Tile anotherTile = anotherPlayer.getTiles().get(0);
        return (((tile.getX() == anotherTile.getX() + 1 || tile.getX() == anotherTile.getX() - 1)
                && (tile.getY() == anotherTile.getY() + 1 || tile.getY() == anotherTile.getY() - 1)));
    }

    private Player getPlayer(String username) {
        Player anotherPlayer = null;
        for (Player player : game.getPlayers()) {
            if (player.getUser().equals(username)) {
                anotherPlayer = player;
            }
        }
        return anotherPlayer;
    }

    public Response giveGift(String username, String itemName, int amount) {
        Player player = getPlayer(username);
        Salable gift = null;
        if (player == null) {
            return new Response("Player with that username not found");
        }
        boolean areNeighbors = twoActorsAreNeighbors(currentPlayer, player);
        if (!areNeighbors) {
            return new Response("the other player is not next You");
        }
        for (Map.Entry<Salable, Integer> salableIntegerEntry : currentPlayer.getInventory().getProducts().entrySet()) {
            if (salableIntegerEntry.getKey().getName().equals(itemName) && salableIntegerEntry.getValue() >= amount) {
                gift = salableIntegerEntry.getKey();
            }
        }
        if (gift == null) {
            return new Response("You don't enough amount of the item : " + itemName);
        }
        player.getInventory().getProducts().put(gift, amount);
        Friendship friendShipBetweenTwoActors = getFriendShipBetweenTwoActors(player);
        friendShipBetweenTwoActors.getGifts().add(new Gift(currentPlayer, player, gift));
        player.notify(new Response("%s sent you a gift".formatted(currentPlayer.getUser().getUsername())));
        if (gift instanceof Flower) {
            if (friendShipBetweenTwoActors.getXp() == 100) {
                friendShipBetweenTwoActors.setFriendShipLevel(friendShipBetweenTwoActors.getFriendShipLevel() + 1);
            }
        }

        return Response.empty();
    }

    public Response showGottenGifts() {
        Friendship friendship1 = null;
        StringBuilder stringBuilder = new StringBuilder();
        for (Friendship friendship : game.getFriendships()) {
            if (friendship.getFirstPlayer().equals(currentPlayer) || friendship.getSecondPlayer().equals(currentPlayer)) {
                friendship1 = friendship;
                for (Gift gift : friendship1.getGifts()) {
                    if (gift.getGifting().equals(currentPlayer)) {
                        stringBuilder.append(gift);
                    }
                }
            }
        }

        return new Response(stringBuilder.toString());
    }

    public Response rateGift(String giftId, int rate) {
        Gift giftFlag = null;
        if (rate > 5 || rate < 1) {
            return new Response("rate should be between 1 and 5");
        }
        for (Friendship friendship : game.getFriendships()) {
            for (Gift gift : friendship.getGifts()) {
                giftFlag = gift;
                if (gift.getGiftId().equals(giftId)) {
                    gift.setRate(rate);
                    friendship.setXp(friendship.getXp() + (rate - 3) * 30 + 15);
                }
            }
        }
        if (giftFlag == null) {
            return new Response("invalid gift number");
        }
        return Response.empty();
    }

    public Response showGiftHistory(String username) {
        StringBuilder stringBuilder = new StringBuilder();
        Player player = getPlayer(username);
        Friendship friendShipBetweenTwoActors = getFriendShipBetweenTwoActors(player);
        if (player == null) {
            return new Response("Player with that username not found");
        }
        stringBuilder.append(friendShipBetweenTwoActors.getGifts());
        return new Response(stringBuilder.toString());
    }

    public Response hug(String username) {
        Player player = getPlayer(username);
        if (player == null) {
            return new Response("Player with that username not found");
        }
        boolean areNeighbors = twoActorsAreNeighbors(currentPlayer, player);
        if (!areNeighbors) {
            return new Response("the other player is not next You");
        }
        Friendship friendShipBetweenTwoActors = getFriendShipBetweenTwoActors(player);
        if (friendShipBetweenTwoActors.getFriendShipLevel() < 2) {
            return new Response("you are not in that level of friendship");
        }
        friendShipBetweenTwoActors.setXp(friendShipBetweenTwoActors.getXp() + 60);
        return Response.empty();
    }

    public Response marry(String username, String ring) {
        Player player = getPlayer(username);
        if (player == null) {
            return new Response("Player with that username not found");
        }
        if (currentPlayer.getUser().getGender().equals(Gender.FEMALE)) {
            return new Response("let he asks first please");
        }
        if (player.getUser().getGender().equals(Gender.MALE)) {
            return new Response("find a girl please!");
        }
        Friendship friendShipBetweenTwoActors = getFriendShipBetweenTwoActors(player);
        if (friendShipBetweenTwoActors.getFriendShipLevel() < 3) {
            return new Response("you are not in that level of friendship");
        }
        if (!twoActorsAreNeighbors(currentPlayer, player)) {
            return new Response("the other player is not next You");
        }
        Salable wRing = null;
        for (Map.Entry<Salable, Integer> salableIntegerEntry : currentPlayer.getInventory().getProducts().entrySet()) {
            if (salableIntegerEntry.getKey().getName().equals(ring)) {
                wRing = salableIntegerEntry.getKey();
            }
        }
        if (wRing == null) {
            return new Response("first by a ring");
        }
        player.notify(new Response("Do you marry to %s".formatted(currentPlayer.getUser().getUsername())));

        return Response.empty();
    }

    public Response Respond(boolean accept, String username) {
        Player player = getPlayer(username);
        Friendship friendShipBetweenTwoActors = getFriendShipBetweenTwoActors(player);
        if (!accept) {
            friendShipBetweenTwoActors.setFriendShipLevel(0);
            player.setDaysOfSadness(7);
            return Response.empty();
        }
        Salable wRing = null;
        for (Map.Entry<Salable, Integer> salableIntegerEntry : player.getInventory().getProducts().entrySet()) {
            if (salableIntegerEntry.getKey().getName().equals(PierreShop.WEDDING_RING.getName())) {
                wRing = salableIntegerEntry.getKey();
                player.getInventory().getProducts().remove(wRing);
                currentPlayer.getInventory().addProductToBackPack(wRing, 1);
            }
        }
        friendShipBetweenTwoActors.setFriendShipLevel(4);
        player.setCouple(currentPlayer);
        currentPlayer.setCouple(player);
        for (Farm farm : game.getVillage().getFarms()) {
            if (farm.getPlayers().contains(player)) {
                farm.getPlayers().add(currentPlayer);
            }
            if (farm.getPlayers().contains(currentPlayer)) {
                farm.getPlayers().add(player);
            }
        }
        Account wifeAccount = currentPlayer.getAccount();
        Account husbandAccount = player.getAccount();
        husbandAccount.setGolds(husbandAccount.getGolds() + wifeAccount.getGolds());
        wifeAccount = husbandAccount;
        return null;
    }
}