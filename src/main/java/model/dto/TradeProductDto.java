package model.dto;

import lombok.Builder;

@Builder
public record TradeProductDto(String username, String type, String item, Integer amount, String targetItem,
                              int targetAmount) {
}
