package io.github.some_example_name.service;

import io.github.some_example_name.model.*;
import io.github.some_example_name.model.enums.Gender;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.model.relations.*;
import io.github.some_example_name.model.structure.stores.PierreShop;
import io.github.some_example_name.model.tools.Tool;
import io.github.some_example_name.utils.App;

import java.util.Iterator;
import java.util.Map;

public class RelationService {
    private NPC lastTalkedNPC = null;
    private static RelationService instance;
    Player currentPlayer;
    Game game;

    private RelationService() {
    }

    public static RelationService getInstance() {
        if (instance == null) {
            instance = new RelationService();
        }
        return instance;
    }


    public Response showMyFriendShips() {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
        StringBuilder stringBuilder = new StringBuilder();
        for (Friendship friendship : game.getFriendships()) {
            if (friendship.getFirstPlayer().equals(currentPlayer) || friendship.getSecondPlayer().equals(currentPlayer)) {
                stringBuilder.append(friendship.toString());
            }
        }
        return new Response(stringBuilder.toString());
    }

    public Response talkToAnotherPlayer(String username, String message) {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
        Player anotherPlayer = getPlayer(username);
        if (anotherPlayer == null) {
            return new Response("Player with that username not found");
        }
        if (!twoActorsAreNeighbors(currentPlayer, anotherPlayer, 1)) {
            return new Response("You can't talk to the other player");
        }
        Friendship friendship = getFriendShipBetweenTwoActors(anotherPlayer);
        friendship.getDialogs().put(message, currentPlayer);
        if (currentPlayer.getCouple() != null) {
            if (currentPlayer.getCouple().equals(anotherPlayer)) {
                changeFriendShipLevelUp(friendship, 50);
            }
            if (currentPlayer.getCouple().equals(anotherPlayer)) {
                changeFriendShipLevelUp(friendship, 20);
            }
        }
        currentPlayer.changeEnergy(50);
        anotherPlayer.changeEnergy(50);
        anotherPlayer.notify(new Response("%s called you!".formatted(currentPlayer.getUser().getUsername())));
        return new Response("message sent successfully");
    }


    public Response showTalkHistories(String username) {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
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

    Friendship getFriendShipBetweenTwoActors(Actor anotherPlayer) {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
        for (Friendship friendship : game.getFriendships()) {
            if ((friendship.getFirstPlayer().equals(currentPlayer) &&
                    friendship.getSecondPlayer().equals(anotherPlayer)) || (friendship.getSecondPlayer().equals(currentPlayer) &&
                    friendship.getFirstPlayer().equals(anotherPlayer))) {
                return friendship;
            }
        }
        return null;
    }

    boolean twoActorsAreNeighbors(Actor currentPlayer, Actor anotherPlayer, int dis) {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
        Tile tile = currentPlayer.getTiles().get(0);
        Tile anotherTile = anotherPlayer.getTiles().get(0);
        return (((tile.getX() <= anotherTile.getX() + dis && tile.getX() >= anotherTile.getX() - dis)
                && (tile.getY() <= anotherTile.getY() + dis || tile.getY() >= anotherTile.getY() - dis)));
    }

    Player getPlayer(String username) {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
        Player anotherPlayer = null;
        for (Player player : game.getPlayers()) {
            if (player.getUser().getUsername().equals(username)) {
                anotherPlayer = player;
            }
        }
        return anotherPlayer;
    }

    public Response giveGift(String username, String itemName, int amount) {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
        Player player = getPlayer(username);
        Salable gift = null;
        if (player == null) {
            return new Response("Player with that username not found");
        }
        boolean areNeighbors = twoActorsAreNeighbors(currentPlayer, player, 1);
//        if (!areNeighbors) {
//            return new Response("the other player is not next You");
//        }
        for (Map.Entry<Salable, Integer> salableIntegerEntry : currentPlayer.getInventory().getProducts().entrySet()) {
            if (salableIntegerEntry.getKey().getName().equals(itemName) && salableIntegerEntry.getValue() >= amount) {
                gift = salableIntegerEntry.getKey();
                break;
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
        currentPlayer.changeEnergy(50);
        player.changeEnergy(50);
        return new Response("gift gived successfully");
    }

    public Response showGottenGifts() {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
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

    public Response rateGift(int giftId, int rate) {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
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
                } else {
                    return new Response("gift id is not correct");
                }
            }
        }
        if (giftFlag == null) {
            return new Response("invalid gift number");
        }
        return new Response("rate gift successfully");
    }

    public Response showGiftHistory(String username) {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
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
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
        Player player = getPlayer(username);
        if (player == null) {
            return new Response("Player with that username not found");
        }
        boolean areNeighbors = twoActorsAreNeighbors(currentPlayer, player, 1);
//        if (!areNeighbors) {
//            return new Response("the other player is not next You");
//        }
        Friendship friendShipBetweenTwoActors = getFriendShipBetweenTwoActors(player);
        if (friendShipBetweenTwoActors.getFriendShipLevel() < 2) {
            return new Response("you are not in that level of friendship");
        }
        changeFriendShipLevelUp(friendShipBetweenTwoActors, 60);
        currentPlayer.changeEnergy(50);
        player.changeEnergy(50);
        return Response.empty();
    }

    public Response marry(String username, String ring) {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
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
//        if (friendShipBetweenTwoActors.getFriendShipLevel() < 3) {
//            return new Response("you are not in that level of friendship");
//        }
//        if (!twoActorsAreNeighbors(currentPlayer, player, 1)) {
//            return new Response("the other player is not next You");
//        }
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

        return new Response("request sent");
    }

    public Response Respond(boolean accept, String username) {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
        Player player = getPlayer(username);
        Friendship friendShipBetweenTwoActors = getFriendShipBetweenTwoActors(player);

        if (!accept) {
            friendShipBetweenTwoActors.setFriendShipLevel(0);
            player.setDaysOfSadness(7);
            return new Response("reject marriage to " + player.getUser().getUsername());
        }

        // Use Iterator to safely remove items during iteration
        Iterator<Map.Entry<Salable, Integer>> iterator = player.getInventory().getProducts().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Salable, Integer> entry = iterator.next();
            if (entry.getKey().getName().equals(PierreShop.WEDDING_RING.getName())) {
                Salable wRing = entry.getKey();
                iterator.remove();  // Safe removal using iterator
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

        return new Response("Happy accepting marriage ask");
    }

    public Response meetNpc(String npcName) {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
        NPCType npcType = null;
        for (NPCType value : NPCType.values()) {
            if (value.getName().equals(npcName)) {
                npcType = value;
            }
        }
        NPC npc1 = null;
        for (NPC npc : game.getNpcs()) {
            if (npc.getType().equals(npcType)) {
                npc1 = npc;
            }
        }
        if (npc1 == null) {
            return new Response("npc not found");
        }
        boolean areNeighbors = twoActorsAreNeighbors(currentPlayer, npc1, 2);
        if (!areNeighbors) {
            return new Response("the other player is not next You");
        }
        lastTalkedNPC = npc1;
        Friendship friendShipBetweenTwoActors = getFriendShipBetweenTwoActors(npc1);
        Dialog dialog = Dialog.getDialog(game.getTimeAndDate(), friendShipBetweenTwoActors.getFriendShipLevel());
        changeFriendShipLevelUp(friendShipBetweenTwoActors, 20);
        return new Response(dialog.getDialog());
    }

    public void changeFriendShipLevelUp(Friendship friendShipBetweenTwoActors, int x) {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
        if (!friendShipBetweenTwoActors.getLastSeen().getDay().equals(game.getTimeAndDate().getDay())) {
            friendShipBetweenTwoActors.setLastSeen(game.getTimeAndDate());
            friendShipBetweenTwoActors.setXp(friendShipBetweenTwoActors.getXp() + x);
            if (friendShipBetweenTwoActors.getXp() > 100) {
                friendShipBetweenTwoActors.setXp(friendShipBetweenTwoActors.getXp() - 100);
                friendShipBetweenTwoActors.setFriendShipLevel(friendShipBetweenTwoActors.getFriendShipLevel() + 1);
            }
        }
        if (friendShipBetweenTwoActors.getFriendShipLevel() == 1) {
            friendShipBetweenTwoActors.setTimeFromGettingFirstLevel(game.getTimeAndDate());
        }
        if (friendShipBetweenTwoActors.getFriendShipLevel() == 3 && (friendShipBetweenTwoActors.getFirstPlayer() instanceof NPC || friendShipBetweenTwoActors.getSecondPlayer() instanceof NPC)) {
            friendShipBetweenTwoActors.setXp(Math.min(friendShipBetweenTwoActors.getXp(), 199));
            return;
        }
        if (friendShipBetweenTwoActors.getFriendShipLevel() == 4) {
            friendShipBetweenTwoActors.setXp(Math.min(friendShipBetweenTwoActors.getXp(), 99));

        }
    }

    public void changeFriendShipLevelDown(Friendship friendShipBetweenTwoActors, int x) {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
        if (friendShipBetweenTwoActors.getFriendShipLevel() == 0)
            return;
        if (!friendShipBetweenTwoActors.getLastSeen().getDay().equals(game.getTimeAndDate().getDay())) {
            friendShipBetweenTwoActors.setLastSeen(game.getTimeAndDate());
            friendShipBetweenTwoActors.setXp(100 - x);
            if (friendShipBetweenTwoActors.getXp() < 0) {
                friendShipBetweenTwoActors.setFriendShipLevel(friendShipBetweenTwoActors.getFriendShipLevel() - 1);
                friendShipBetweenTwoActors.setXp(friendShipBetweenTwoActors.getXp() + 100);
            }
        }
    }

    public Response giftNPC(String npcName, String item) {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
        NPCType npcType = null;
        for (NPCType value : NPCType.values()) {
            if (value.getName().equals(npcName)) {
                npcType = value;
            }
        }
        NPC npc1 = null;
        for (NPC npc : game.getNpcs()) {
            if (npc.getType().equals(npcType)) {
                npc1 = npc;
            }
        }
        if (npc1 == null) {
            return new Response("npc not found");
        }
        boolean areNeighbors = twoActorsAreNeighbors(currentPlayer, npc1, 2);
        if (!areNeighbors) {
            return new Response("the other player is not next You");
        }
        Friendship friendShipBetweenTwoActors = getFriendShipBetweenTwoActors(npc1);
        Map.Entry<Salable, Integer> itemFromInventory = currentPlayer.getItemFromInventory(item);
        if (itemFromInventory == null) {
            return new Response("item not found");
        }
        Salable gift = itemFromInventory.getKey();
        if (itemFromInventory.getValue() < 1) {
            return new Response("you don't have this gift");
        }
        if (gift instanceof Tool) {
            return new Response("gift can not be tool!");
        }
        boolean isFavorite = false;
        for (Salable favorite : npcType.getFavorites()) {
            if (favorite.getName().equals(gift.getName())) {
                changeFriendShipLevelUp(friendShipBetweenTwoActors, 200);
                isFavorite = true;
            }
        }
        if (isFavorite) {
            changeFriendShipLevelUp(friendShipBetweenTwoActors, 50);
        }
        currentPlayer.getInventory().deleteProductFromBackPack(itemFromInventory.getKey(), currentPlayer, 1);
        return new Response("gift gived successfully");
    }

    public Response showNpcFriendship() {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
        StringBuilder stringBuilder = new StringBuilder();
        for (Friendship friendship : game.getFriendships()) {
            if (friendship.getSecondPlayer() instanceof NPC || friendship.getFirstPlayer() instanceof NPC) {
                stringBuilder.append(friendship);
            }
        }
        return new Response(stringBuilder.toString());
    }

    public Response questsList() {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
        StringBuilder stringBuilder = new StringBuilder();
        Friendship friendShipBetweenTwoActors = getFriendShipBetweenTwoActors(lastTalkedNPC);
        int level = friendShipBetweenTwoActors.getFriendShipLevel();
        if (lastTalkedNPC.getType().getMissions().isEmpty()) {
            return new Response("you have no missions");
        }
        if (level < 1) {
            stringBuilder.append(lastTalkedNPC.getType().getMissions().get(0));
        }
        if (level < 2) {
            stringBuilder.append(lastTalkedNPC.getType().getMissions().get(1));
            if (game.getTimeAndDate().getSeason().ordinal() - friendShipBetweenTwoActors.getTimeFromGettingFirstLevel().getSeason().ordinal() >= lastTalkedNPC.getType().getMissionSeasonDis()) {
                stringBuilder.append(lastTalkedNPC.getType().getMissions().get(2));
            }
        }
        if (level >= 2) {
            stringBuilder.append(lastTalkedNPC.getType().getMissions());
        }

        return new Response(stringBuilder.toString());
    }

    public Response friendShip_CH(int n) {
        Friendship friendShipBetweenTwoActors = getFriendShipBetweenTwoActors(lastTalkedNPC);
        friendShipBetweenTwoActors.setFriendShipLevel(n);
        return new Response("friendship changed to 3");
    }

    public Response doMission(int missionId) {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
        if (missionId < 1 || missionId > 3) {
            return new Response("missionId invalid");
        }
        Mission mission = lastTalkedNPC.getType().getMissions().get(missionId - 1);
        if (mission.getDoer() != null) {
            return new Response("mission is already done");
        }
        boolean areNeighbors = twoActorsAreNeighbors(currentPlayer, lastTalkedNPC, 2);
        if (!areNeighbors) {
            return new Response("the other player is not next You");
        }

        boolean canPrepare = true;
        Map<Salable, Integer> request = mission.getRequest();
        Map<Salable, Integer> reward = mission.getReward();
        for (Map.Entry<Salable, Integer> salableIntegerEntry : request.entrySet()) {
            if (!currentPlayer.getInventory().getProducts().containsKey(salableIntegerEntry.getKey())) {
                canPrepare = false;
                break;
            }
            if (currentPlayer.getInventory().getProducts().get(salableIntegerEntry.getKey()) < salableIntegerEntry.getValue()) {
                canPrepare = false;
                break;
            }
        }
        if (!canPrepare) {
            return new Response("you don't have all required items");
        }
        for (Map.Entry<Salable, Integer> salableIntegerEntry : request.entrySet()) {
            int value = salableIntegerEntry.getValue();
            Salable salable = salableIntegerEntry.getKey();
            int amount = currentPlayer.getInventory().getProducts().get(salable);
            currentPlayer.getInventory().getProducts().replace(salable, amount - value);
        }
        Friendship friendShipBetweenTwoActors = getFriendShipBetweenTwoActors(lastTalkedNPC);
        int mult = 1;
        mult = friendShipBetweenTwoActors.getFriendShipLevel() == 2 ? 2 : 1;
        for (Map.Entry<Salable, Integer> salableIntegerEntry : reward.entrySet()) {
            int value = salableIntegerEntry.getValue();
            Salable salable = salableIntegerEntry.getKey();
            int amount = currentPlayer.getInventory().getProducts().getOrDefault(salable, 0);
            currentPlayer.getInventory().getProducts().replace(salable, amount + mult * value);
        }
        mission.setDoer(currentPlayer);
        return new Response("mission completed");
    }
}
