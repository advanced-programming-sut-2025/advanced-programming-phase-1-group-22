package model.dto;

import lombok.Builder;

@Builder
public record TradePriceDto(String username, String type, String item, int amount, int price) {
}
