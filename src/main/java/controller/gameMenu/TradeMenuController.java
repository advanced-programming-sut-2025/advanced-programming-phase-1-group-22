package controller.gameMenu;

import controller.MenuController;
import model.dto.TradePriceDto;
import model.dto.TradeProductDto;
import model.records.Response;
import service.TradeService;

public class TradeMenuController extends MenuController {
    TradeService tradeService = TradeService.getInstance();

    public Response startTrade(String... params) {
        return tradeService.startTrade();
    }

    public Response trade(String... params) {
        String username = params[0];
        String type = params[1];
        String item = params[2];
        int amount = Integer.parseInt(params[3]);
        if (params.length == 5) {
            int price = Integer.parseInt(params[4]);
            TradePriceDto tradeDto = TradePriceDto.builder().username(username).item(item).amount(amount).price(price).build();
            if (type.equals("offer")) {
                return tradeService.tradePriceOffer(tradeDto);
            }
            return tradeService.tradePriceRequest(tradeDto);
        }
        if (params.length == 6) {
            String targetItem = params[5];
            int targetAmount = Integer.parseInt(params[6]);
            TradeProductDto tradeDto = TradeProductDto.builder().username(username).item(item).amount(amount)
                    .targetItem(targetItem).targetAmount(targetAmount).build();
            if (type.equals("offer")) {
                return tradeService.tradeProductOffer(tradeDto);
            }
            return tradeService.tradeProductRequest(tradeDto);
        } else {
            return new Response("just one way for pay should be selected");
        }

    }

    public Response tradeList(String... params) {
        return tradeService.tradeList();
    }

    public Response tradeHistory(String... params) {
        return tradeService.tradeHistory();
    }

    public Response tradeResponse(String... params) {
        return null;
    }


}
