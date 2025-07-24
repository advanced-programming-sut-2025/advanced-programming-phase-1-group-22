package io.github.some_example_name.common.model.dto;

import lombok.Builder;

@Builder
public record TradePriceDto(String username, String type, String item, int amount, int price) {
}
