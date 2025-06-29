package io.github.some_example_name.model.dto;

import lombok.Builder;

@Builder
public record TradeProductDto(String username, String type, String item, Integer amount, String targetItem,
                              int targetAmount) {
}
