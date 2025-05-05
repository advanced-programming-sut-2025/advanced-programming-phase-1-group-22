package controller.gameMenu;

import model.records.Response;
import model.relations.NPC;
import service.RelationService;


public class RelationShipController {
    private final RelationService relationService = RelationService.getInstance();

    public Response showFriendShips(String... params) {
        return relationService.showMyFriendShips();
    }

    public Response talkToAnotherPlayer(String... params) {
        String username = params[0];
        String message = params[1];
        return relationService.talkToAnotherPlayer(username, message);
    }

    public Response showTalkHistory(String... params) {
        String username = params[0];
        return relationService.showTalkHistories(username);
    }

    public Response giveGift(String... params) {
        String username = params[0];
        String item = params[1];
        int amount = Integer.parseInt(params[2]);
        return relationService.giveGift(username, item, amount);
    }

    public Response showGottedGifts(String... params) {
        return relationService.showGottenGifts();
    }

    public Response rateGift(String... params) {
      int item = Integer.parseInt(params[0]);
        int rate = Integer.parseInt(params[1]);
        return relationService.rateGift(item, rate);
    }

    public Response showGiftHistory(String... params) {
        String username = params[0];
        return relationService.showGiftHistory(username);
    }

    public Response hug(String... params) {
        String username = params[0];
        return relationService.hug(username);
    }

    public Response giveFlower(String... params) {
        String username = params[0];
        return relationService.giveGift(username, "flower", 1);
    }

    public Response marry(String... params) {
        String username = params[0];
        String ring = params[1];
        return relationService.marry(username, ring);
    }

    public Response respond(String... params) {
        String answer = params[0];
        boolean accept;
        if (answer.equals("accept")) {
            accept = true;
        } else if (answer.equals("reject")) {
            accept = false;
        } else {
            return new Response("invalid answer");
        }
        String username = params[1];
        return relationService.Respond(accept, username);
    }

    public Response meetNpc(String... params) {
        String npcName = params[0];
        return relationService.meetNpc(npcName);
    }

    public Response giftNpc(String... params) {
        String npcName = params[0];
        String item = params[1];
        return relationService.giftNPC(npcName, item);
    }

    public Response showNpcFriendship(String... params) {
        return relationService.showNpcFriendship();
    }

    public Response questsList(String... params) {
        return relationService.questsList();
    }

    public Response doMission(String... params) {
        int missionId = Integer.parseInt(params[0]);
        return relationService.doMission(missionId);
    }
}
