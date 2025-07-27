package io.github.some_example_name.server.service;

import io.github.some_example_name.client.controller.WorldController;
import io.github.some_example_name.client.view.GameView;
import io.github.some_example_name.client.view.mainMenu.DoYouMarryMePopUp;
import io.github.some_example_name.common.model.*;
import io.github.some_example_name.common.model.gameSundry.Sundry;
import io.github.some_example_name.common.model.gameSundry.SundryType;
import io.github.some_example_name.common.model.relations.*;
import io.github.some_example_name.common.model.enums.Gender;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.model.structure.Structure;
import io.github.some_example_name.common.model.structure.stores.PierreShop;
import io.github.some_example_name.common.model.tools.Tool;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.utils.GameAsset;

import java.util.*;

public class RelationService {
    private NPC lastTalkedNPC = null;
    private static RelationService instance;
    Player currentPlayer;
    Game game;
    private Tile origin;

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
//        if (!twoActorsAreNeighbors(currentPlayer, anotherPlayer, 1)) {
//            return new Response("You can't talk to the other player");
//        }
        Friendship friendship = getFriendShipBetweenWithActor(anotherPlayer);
        friendship.getDialogs().add(new Entry<>(message, currentPlayer));
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
        anotherPlayer.notify(
            new Response("%s called you!".formatted(currentPlayer.getUser().getUsername())),
            NotificationType.TALK,
            currentPlayer
        );
        return new Response("message sent successfully", true);
    }

    public Response talkToAnotherPlayer(Player anotherPlayer, String message) {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
//        Player anotherPlayer = getPlayer(username);
//        if (anotherPlayer == null) {
//            return new Response("Player with that username not found");
//        }
//        if (!twoActorsAreNeighbors(currentPlayer, anotherPlayer, 1)) {
//            return new Response("You can't talk to the other player");
//        }
        Friendship friendship = getFriendShipBetweenWithActor(anotherPlayer);
        friendship.getDialogs().add(new Entry<>(message, currentPlayer));
        if (currentPlayer.getCouple() != null) {
            if (currentPlayer.getCouple().equals(anotherPlayer)) {
                changeFriendShipLevelUp(friendship, 50);
            }
            if (currentPlayer.getCouple().equals(anotherPlayer)) {
                changeFriendShipLevelUp(friendship, 20);
            }
        }
        anotherPlayer.notify(
            new Response("%s called you!".formatted(currentPlayer.getUser().getUsername())),
            NotificationType.TALK, currentPlayer
        );
        return new Response("message sent successfully", true);
    }


    public Response showTalkHistories(String username) {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
        Player player = getPlayer(username);
        if (player == null) {
            return new Response("Player with that username not found");
        }
        Friendship friendShipBetweenTwoActors = getFriendShipBetweenWithActor(player);
        if (friendShipBetweenTwoActors == null) {
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(friendShipBetweenTwoActors.getDialogs());
        return new Response(stringBuilder.toString());
    }

    public Friendship getFriendShipBetweenTwoActors(Player currentPlayer, Actor anotherPlayer) {
        game = App.getInstance().getCurrentGame();
        for (Friendship friendship : game.getFriendships()) {
            if ((friendship.getFirstPlayer().equals(currentPlayer) &&
                friendship.getSecondPlayer().equals(anotherPlayer)) || (friendship.getSecondPlayer().equals(currentPlayer) &&
                friendship.getFirstPlayer().equals(anotherPlayer))) {
                return friendship;
            }
        }
        return null;
    }
    public Friendship getFriendShipBetweenWithActor(Actor anotherPlayer) {
        return getFriendShipBetweenTwoActors(App.getInstance().getCurrentGame().getCurrentPlayer(), anotherPlayer);
    }

    public boolean twoActorsAreNeighbors(Actor currentPlayer, Actor anotherPlayer, int dis) {
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
//        boolean areNeighbors = twoActorsAreNeighbors(currentPlayer, player, 1);
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
        currentPlayer.getInventory().deleteProductFromBackPack(gift, currentPlayer, amount);
        gift = gift.copy();
        player.getInventory().getProducts().put(gift, amount);
        Friendship friendShipBetweenTwoActors = getFriendShipBetweenWithActor(player);
        if (gift instanceof Flower) {
            if (friendShipBetweenTwoActors.getXp() == 100) {
                friendShipBetweenTwoActors.setFriendShipLevel(friendShipBetweenTwoActors.getFriendShipLevel() + 1);
            }
        } else {
            friendShipBetweenTwoActors.getGifts().add(new Gift(currentPlayer, player, amount, gift, friendShipBetweenTwoActors.getGifts().size()));
            player.notify(new Response("%s sent you a gift".formatted(currentPlayer.getUser().getUsername())),
                NotificationType.GIFT, currentPlayer);
        }
        if (player.getCouple() == currentPlayer) {
            currentPlayer.changeEnergy(50);
            player.changeEnergy(50);
        }
        return new Response("Gift gave successfully", true);
    }

    public Response giveGift(Player player, Salable gift, int amount) {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
//        boolean areNeighbors = twoActorsAreNeighbors(currentPlayer, player, 1);
//        if (!areNeighbors) {
//            return new Response("the other player is not next You");
//        }
        if (!currentPlayer.getInventory().getProducts().containsKey(gift)
            || currentPlayer.getInventory().getProducts().get(gift) < amount) {
            return new Response("You don't enough amount of the item : " + gift.getName());
        }
        player.getInventory().getProducts().put(gift, amount);
        currentPlayer.getInventory().deleteProductFromBackPack(gift, currentPlayer, amount);
        gift = gift.copy();
        Friendship friendship = getFriendShipBetweenWithActor(player);
        friendship.getGifts().add(new Gift(currentPlayer, player, amount, gift, friendship.getGifts().size()));
        player.notify(new Response("%s sent you a gift".formatted(currentPlayer.getUser().getUsername())),
            NotificationType.GIFT, currentPlayer);
        if (gift instanceof Flower) {
            if (friendship.getFriendShipLevel() == 2 && friendship.getXp() >= xpNeededForChangeLevel(friendship)) {
                friendship.setFriendShipLevel(friendship.getFriendShipLevel() + 1);
                friendship.setXp(0);
            }
        }
        if (currentPlayer.getCouple() == player) {
            currentPlayer.changeEnergy(50);
            player.changeEnergy(50);
        }
        return new Response("Gift gave successfully", true);
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
                    if (gift.getGiven().equals(currentPlayer)) {
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
        return new Response("rate gift successfully", true);
    }

    public Response rateGift(Friendship friendship, Gift gift, int rate) {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
        if (rate > 5 || rate < 1) {
            return new Response("rate should be between 1 and 5");
        }

        gift.setRate(rate);
        friendship.setXp(friendship.getXp() + (rate - 3) * 30 + 15);
        return new Response("rate gift successfully", true);
    }

    public Response showGiftHistory(String username) {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
        StringBuilder stringBuilder = new StringBuilder();
        Player player = getPlayer(username);
        Friendship friendShipBetweenTwoActors = getFriendShipBetweenWithActor(player);
        if (player == null) {
            return new Response("Player with that username not found");
        }
        stringBuilder.append(friendShipBetweenTwoActors.getGifts());
        return new Response(stringBuilder.toString());
    }

    public Direction handleInteractionInit(Player requester, Player requested) {
        Direction direction = Direction.getByXAndY(
            requested.getTiles().get(0).getX() - requester.getTiles().get(0).getX(),
            requested.getTiles().get(0).getY() - requester.getTiles().get(0).getY()
        );
        if (direction == null) return null;
        if (requested.getUser().getUsername().equals(App.getInstance().getCurrentGame().getCurrentPlayer().getUser().getUsername())) {
            GameView.captureInput = false;
        }
        requested.setLazyDirection(direction.reverse());
        requester.setLazyDirection(direction);
        return direction;
    }

    public void drawFlower(Player requester, Player requested) {
        Direction direction = handleInteractionInit(requester, requested);
        if (direction == null) return;
        WorldController.getInstance().drawFlower(direction, requester);
    }

    public void handleHug(Player requester, Player requested) {
        Direction direction = handleInteractionInit(requester, requested);
        if (direction == null) return;
        origin = requester.getTiles().get(0);
        requester.setDirection(direction);
        requester.setDirChanged(true);
        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
            @Override
            public void run() {
                requester.getTiles().clear();
                requester.getTiles().addAll(requested.getTiles());
            }
        }, 0.3f);

        boolean flag = false;
        int x = -1, y = -1;
        List<Structure> structures = App.getInstance().getCurrentGame().getVillage().getStructures();
        for (Structure player1 : structures) {
            if (player1 == requester) break;
            if (requested == player1) {
                flag = true;
                break;
            }
        }

        for (int i = 0; i < structures.size(); i++) {
            Structure player1 = structures.get(i);
            if (player1 == requester) x = i;
            if (requested == player1) y = i;
        }
        if (flag) {
            Collections.swap(structures, x, y);
        }
        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
            @Override
            public void run() {
                requester.getSprites().get(0).setOffset(new Tuple<>( -0.1f, 0.1f));
                requester.setLazyDirection(Direction.SOUTH);
                requested.setLazyDirection(Direction.NORTH);
            }
        }, 0.8f);
        boolean finalFlag = flag;
        int finalX = x;
        int finalY = y;
        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
            @Override
            public void run() {
                requester.getSprites().get(0).setOffset(new Tuple<>( 0f, 0f));
                requester.setDirection(direction);
                requester.setDirChanged(true);
                requester.getTiles().clear();
                requester.getTiles().add(origin);
            }
        }, 3.3f);
        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
            @Override
            public void run() {
                requested.setLazyDirection(direction.reverse());
                GameView.captureInput = true;
                if (finalFlag) {
                    Collections.swap(structures, finalX, finalY);
                }
            }
        }, 3.6f);
    }

    public Response hug(String username) {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
        Player player = getPlayer(username);
        if (player == null) {
            return new Response("Player with that username not found");
        }
        boolean areNeighbors = twoActorsAreNeighbors(currentPlayer, player, 1);
        if (!areNeighbors) {
            return new Response("the other player is not next You");
        }
        Friendship friendShipBetweenTwoActors = getFriendShipBetweenWithActor(player);
//        if (friendShipBetweenTwoActors.getFriendShipLevel() < 2) {
//            return new Response("you are not in that level of friendship");
//        }
        changeFriendShipLevelUp(friendShipBetweenTwoActors, 60);
        currentPlayer.changeEnergy(50);
        player.changeEnergy(50);
        return new Response("", true);
    }

    public void handleAskMarriage(Player requester, Player requested) {
        if (requested.getUser().getUsername().equals(App.getInstance().getCurrentGame().getCurrentPlayer().getUser().getUsername())) {
            GameView.captureInput = false;
        }
        origin = requester.getTiles().get(0);
        Tile dest = App.getInstance().getCurrentGame().tiles[requested.getTiles().get(0).getX()]
            [requested.getTiles().get(0).getY() - 1];
        Direction dir = Direction.getByXAndY(
            dest.getX() - requester.getTiles().get(0).getX(),
            dest.getY() - requester.getTiles().get(0).getY()
        );
        if (dir == null) {
            if (dest.getY() - requester.getTiles().get(0).getY() == -2 &&
                Math.abs(dest.getX() - requester.getTiles().get(0).getX()) <= 1) {
                dir = Direction.SOUTH;
            } else {
                return;
            }
        }
        requester.setDirection(dir);
        requester.setDirChanged(true);
        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
            @Override
            public void run() {
                requester.getTiles().clear();
                requester.getTiles().add(dest);
                Direction direction = handleInteractionInit(requester, requested);
                if (direction == null) {
                    requester.getTiles().clear();
                    requester.getTiles().add(origin);
                    return;
                }
                requester.setDirection(direction);
                com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
                    @Override
                    public void run() {
                        requester.setProposal();
                    }
                }, 0.5f);
                if (requested.getUser().getUsername().equals(App.getInstance().getCurrentGame().getCurrentPlayer().getUser().getUsername())) {
                    com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
                        @Override
                        public void run() {
                            DoYouMarryMePopUp doYouMarryMePopUp = new DoYouMarryMePopUp();
                            doYouMarryMePopUp.setPlayer(requester);
                            doYouMarryMePopUp.createMenu(GameView.stage, GameAsset.SKIN, WorldController.getInstance());
                        }
                    }, 1);
                }
            }
        }, 0.2f);
    }

    public void handleYes(Player requester, Player requested) {
        Respond(true, requester, requested);
        requester.setDirection(Direction.NORTH);
        requester.getTiles().clear();
        requester.getTiles().addAll(requested.getTiles());
        requester.getSprites().get(0).setOffset(new Tuple<>(-0.4f, 0f));
        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
            @Override
            public void run() {
                requester.setLazyDirection(Direction.EAST);
                requested.setLazyDirection(Direction.WEST);
                WorldController.getInstance().drawHeart(requested);
                GameView.captureInput = true;
            }
        }, 0.5f);
    }


    public void handleNo(Player requester, Player requested) {
        Respond(false, requester, requested);
        Direction direction = Direction.getByXAndY(-requested.getTiles().get(0).getX() + origin.getX(),
            -requested.getTiles().get(0).getY() + origin.getY());
        if (direction == null) return;
        requested.setLazyDirection(direction.reverse());
        requester.setDirection(Direction.SOUTH);
        requester.setDirection(direction);
        requester.getTiles().clear();
        requester.getTiles().add(App.getInstance().getCurrentGame().getTiles()[origin.getX()][origin.getY()]);
        GameView.captureInput = true;
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
        if (currentPlayer.getCouple() != null) {
            return new Response("You are already engaged!");
        }
        if (player.getCouple() != null) {
            return new Response("She's engaged, " + player.getCouple().getName() + " would be mad at you.");
        }
        Friendship friendShipBetweenTwoActors = getFriendShipBetweenWithActor(player);
//        if (friendShipBetweenTwoActors.getFriendShipLevel() < 3) {
//            return new Response("you are not in that level of friendship");
//        }
        if (!twoActorsAreNeighbors(currentPlayer, player, 1)) {
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
        player.notify(new Response("Do you marry to %s".formatted(currentPlayer.getUser().getUsername())),
            NotificationType.MARRIAGE, currentPlayer);

        return new Response("request sent", true);
    }

    public Response Respond(boolean accept, Player requested, Player requester) {
        game = App.getInstance().getCurrentGame();
        Friendship friendShipBetweenTwoActors = getFriendShipBetweenTwoActors(requested, requester);

        if (!accept) {
            friendShipBetweenTwoActors.setFriendShipLevel(0);
            requested.setDaysOfSadness(7);
            return new Response("reject marriage to " + requested.getUser().getUsername(), true);
        }

        if (requested.getUser().getUsername().equals(App.getInstance().getCurrentGame().getCurrentPlayer().getUser().getUsername())) {
            Salable wRing = new Sundry(SundryType.WEDDING_RING);
            requested.getInventory().addProductToBackPack(wRing, 1);
        }
        if (requester.getUser().getUsername().equals(App.getInstance().getCurrentGame().getCurrentPlayer().getUser().getUsername())) {
            Salable wRing = requester.getInventory().findProductInBackPackByNAme(SundryType.WEDDING_RING.getName());
            requester.getInventory().deleteProductFromBackPack(wRing, requester, 1);
        }
        friendShipBetweenTwoActors.setFriendShipLevel(4);
        requested.setCouple(requester);
        requester.setCouple(requested);

        for (Farm farm : game.getVillage().getFarms()) {
            if (farm.getPlayers().contains(requested)) {
                farm.getPlayers().add(requester);
            }
            if (farm.getPlayers().contains(requester)) {
                farm.getPlayers().add(requested);
            }
        }

        //todo handle money exchange
        Account wifeAccount = requester.getAccount();
        Account husbandAccount = requested.getAccount();
        husbandAccount.setGolds(husbandAccount.getGolds() + wifeAccount.getGolds());
        requester.setAccount(husbandAccount);

        return new Response("Happy accepting marriage ask", true);
    }

    public Response meetNpc(NPC npc) {
//        game = App.getInstance().getCurrentGame();
//        currentPlayer = game.getCurrentPlayer();
//        NPCType npcType = null;
//        for (NPCType value : NPCType.values()) {
//            if (value.getName().equals(npcName)) {
//                npcType = value;
//            }
//        }
//        NPC npc1 = null;
//        for (NPC npc : game.getNpcs()) {
//            if (npc.getType().equals(npcType)) {
//                npc1 = npc;
//            }
//        }
//        if (npc1 == null) {
//            return new Response("npc not found");
//        }
//        boolean areNeighbors = twoActorsAreNeighbors(currentPlayer, npc1, 2);
//        if (!areNeighbors) {
//            return new Response("the other player is not next You");
//        }
        lastTalkedNPC = npc;
        Friendship friendShipBetweenTwoActors = getFriendShipBetweenWithActor(npc);
        Dialog dialog = Dialog.getDialog(game.getTimeAndDate(), friendShipBetweenTwoActors.getFriendShipLevel());
        friendShipBetweenTwoActors.setXp(Math.min(friendShipBetweenTwoActors.getXp() + 20, 799));
        return new Response(dialog.getDialog(), true);
    }

    public Integer xpNeededForChangeLevel(Friendship friendship) {
        return 100 * (friendship.getFriendShipLevel() + 1);
    }

    public void changeFriendShipLevelUp(Friendship friendShipBetweenTwoActors, int x) {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
        if (!friendShipBetweenTwoActors.getLastSeen().getDay().equals(game.getTimeAndDate().getDay())) {
            friendShipBetweenTwoActors.setLastSeen(game.getTimeAndDate());
            friendShipBetweenTwoActors.setXp(friendShipBetweenTwoActors.getXp() + x);
            if (friendShipBetweenTwoActors.getXp() > xpNeededForChangeLevel(friendShipBetweenTwoActors)) {
                friendShipBetweenTwoActors.setXp(friendShipBetweenTwoActors.getXp() - xpNeededForChangeLevel(friendShipBetweenTwoActors));
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

    public Response giftNPC(NPC npc, String item) {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
//        NPCType npcType = null;
//        for (NPCType value : NPCType.values()) {
//            if (value.getName().equals(npcName)) {
//                npcType = value;
//            }
//        }
//        NPC npc1 = null;
//        for (NPC npc : game.getNpcs()) {
//            if (npc.getType().equals(npcType)) {
//                npc1 = npc;
//            }
//        }
//        if (npc1 == null) {
//            return new Response("npc not found");
//        }
//        boolean areNeighbors = twoActorsAreNeighbors(currentPlayer, npc1, 2);
//        if (!areNeighbors) {
//            return new Response("the other player is not next You");
//        }
        Friendship friendShipBetweenTwoActors = getFriendShipBetweenWithActor(npc);
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
        for (Salable favorite : npc.getType().getFavorites()) {
            if (favorite.getName().equals(item)) {
                friendShipBetweenTwoActors.setXp(Math.min(friendShipBetweenTwoActors.getXp() + 200, 799));
            }
        }
        if (!npc.isGiftedToday()) {
            friendShipBetweenTwoActors.setXp(Math.min(friendShipBetweenTwoActors.getXp() + 50, 799));
            npc.setGiftedToday(true);
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

    public Friendship getFriendShip(Actor actor1, Actor actor2) {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
        for (Friendship friendship : game.getFriendships()) {
            if ((friendship.getSecondPlayer().equals(actor1) && friendship.getFirstPlayer().equals(actor2)) ||
                (friendship.getSecondPlayer().equals(actor2) && friendship.getFirstPlayer().equals(actor1))) {
                return friendship;
            }
        }
        return null;
    }

    public Friendship getFriendshipOfNPC(Player player, NPCType npcType) {
        game = App.getInstance().getCurrentGame();
        for (Friendship friendship : game.getFriendships()) {
            if (friendship.getSecondPlayer().equals(player)) {
                if (friendship.getFirstPlayer() instanceof NPC npc) {
                    if (npc.getType().equals(npcType)) return friendship;
                }
            }
            if (friendship.getFirstPlayer().equals(player)) {
                if (friendship.getSecondPlayer() instanceof NPC npc) {
                    if (npc.getType().equals(npcType)) return friendship;
                }
            }
        }
        return null;
    }

    public Map<Friendship, Actor> getFriendShips(Actor actor) {
        Map<Friendship, Actor> friendships = new HashMap<>();
        for (Friendship friendship : App.getInstance().getCurrentGame().getFriendships()) {
            if (friendship.getFirstPlayer().equals(actor)) {
                friendships.put(friendship, friendship.getSecondPlayer());
            } else if (friendship.getSecondPlayer().equals(actor)) {
                friendships.put(friendship, friendship.getFirstPlayer());
            }
        }
        return friendships;
    }

    public Map<Mission, Boolean> questsList() {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();
        Map<Mission, Boolean> questList = new HashMap<>();
        for (NPCType value : NPCType.values()) {
            for (Mission mission : value.getMissions()) {
                mission.setRequester(value);
            }
        }
        for (Structure structure : App.getInstance().getCurrentGame().getVillage().getStructures()) {
            if (structure instanceof NPC npc) {
                Friendship friendShipBetweenTwoActors = getFriendShipBetweenWithActor(npc);
                int level = friendShipBetweenTwoActors.getFriendShipLevel();
                if (level < 1) {
                    questList.put(npc.getType().getMissions().get(0), true);
                    questList.put(npc.getType().getMissions().get(1), false);
                    questList.put(npc.getType().getMissions().get(2), false);
                }
                if (level < 2) {
                    questList.put(npc.getType().getMissions().get(0), true);
                    questList.put(npc.getType().getMissions().get(1), true);
                    questList.put(npc.getType().getMissions().get(2), false);
                    if (game.getTimeAndDate().getSeason().ordinal() - friendShipBetweenTwoActors.getTimeFromGettingFirstLevel().getSeason().ordinal() >= npc.getType().getMissionSeasonDis()) {
                        questList.put(npc.getType().getMissions().get(0), true);
                        questList.put(npc.getType().getMissions().get(1), true);
                        questList.put(npc.getType().getMissions().get(2), true);
                    }
                }
                if (level >= 2) {
                    questList.put(npc.getType().getMissions().get(0), true);
                    questList.put(npc.getType().getMissions().get(1), true);
                    questList.put(npc.getType().getMissions().get(2), true);
                }
            }
        }
        return questList;
    }

    public Map<Mission, Boolean> getMissions(NPC npc) {
        Map<Mission, Boolean> missions = new HashMap<>();
        game = App.getInstance().getCurrentGame();
        Friendship friendShipBetweenTwoActors = getFriendShipBetweenWithActor(npc);
        int level = friendShipBetweenTwoActors.getFriendShipLevel();
        if (level < 1) {
            missions.put(npc.getType().getMissions().get(0), true);
            missions.put(npc.getType().getMissions().get(1), false);
            missions.put(npc.getType().getMissions().get(2), false);
        }
        if (level < 2) {
            missions.put(npc.getType().getMissions().get(0), true);
            missions.put(npc.getType().getMissions().get(1), true);
            missions.put(npc.getType().getMissions().get(2), false);
            if (game.getTimeAndDate().getSeason().ordinal() - friendShipBetweenTwoActors.getTimeFromGettingFirstLevel().getSeason().ordinal() >= npc.getType().getMissionSeasonDis()) {
                missions.put(npc.getType().getMissions().get(0), true);
                missions.put(npc.getType().getMissions().get(1), true);
                missions.put(npc.getType().getMissions().get(2), true);
            }
        }
        if (level >= 2) {
            missions.put(npc.getType().getMissions().get(0), true);
            missions.put(npc.getType().getMissions().get(1), true);
            missions.put(npc.getType().getMissions().get(2), true);
        }

        return missions;
    }

    public Response friendShip_CH(int n) {
        Friendship friendShipBetweenTwoActors = getFriendShipBetweenWithActor(lastTalkedNPC);
        friendShipBetweenTwoActors.setFriendShipLevel(n);
        return new Response("friendship changed to 3");
    }

    public Response completeMission(Mission mission, NPC npc) {
        game = App.getInstance().getCurrentGame();
        currentPlayer = game.getCurrentPlayer();

        if (mission.getDoer() != null) {
            return new Response("mission is already done");
        }

        boolean canPrepare = true;
        Map<Salable, Integer> request = mission.getRequest();
        Map<Salable, Integer> reward = mission.getReward();
        for (Map.Entry<Salable, Integer> salableIntegerEntry : request.entrySet()) {
            Salable salable = currentPlayer.getInventory().getProductFromBackPack(salableIntegerEntry.getKey().getName());
            if (salable == null) {
                canPrepare = false;
                break;
            }
            if (currentPlayer.getInventory().getProducts().get(salable) < salableIntegerEntry.getValue()) {
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
            currentPlayer.getInventory().justDelete(salable, value);
        }
        Friendship friendShipBetweenTwoActors = getFriendShipBetweenWithActor(npc);
        int multi;
        multi = friendShipBetweenTwoActors.getFriendShipLevel() == 2 ? 2 : 1;
        for (Map.Entry<Salable, Integer> salableIntegerEntry : reward.entrySet()) {
            int value = salableIntegerEntry.getValue();
            Salable salable = salableIntegerEntry.getKey();
            currentPlayer.getInventory().addProductToBackPack(salable, multi * value);
        }
        mission.setDoer(currentPlayer);
        return new Response("mission completed");
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
        Friendship friendShipBetweenTwoActors = getFriendShipBetweenWithActor(lastTalkedNPC);
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
