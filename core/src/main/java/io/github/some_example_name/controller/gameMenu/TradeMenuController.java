package io.github.some_example_name.controller.gameMenu;

import io.github.some_example_name.controller.MenuController;
import io.github.some_example_name.model.dto.TradePriceDto;
import io.github.some_example_name.model.dto.TradeProductDto;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.service.TradeService;

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
        if (params[5] == null || params[6] == null) {
            int price = Integer.parseInt(params[4]);
            TradePriceDto tradeDto = TradePriceDto.builder().username(username).item(item).amount(amount).price(price).build();
            if (type.equals("offer")) {
                return tradeService.tradePriceOffer(tradeDto);
            }
            return tradeService.tradePriceRequest(tradeDto);
        } else if (params[4]==null) {
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
        boolean accept = params[0].equals("accept");
        int tradeId = Integer.parseInt(params[1]);
        return tradeService.tradeResponse(accept, tradeId);
    }


}
