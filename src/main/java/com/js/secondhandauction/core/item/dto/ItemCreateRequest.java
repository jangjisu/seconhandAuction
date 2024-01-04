package com.js.secondhandauction.core.item.dto;

import com.js.secondhandauction.core.item.domain.Item;

public record ItemCreateRequest (
    String item,
    int regPrice,
    long regId
) {
    public Item toEntity() {
        return Item.builder()
                .item(item)
                .regPrice(regPrice)
                .regId(regId)
                .build();
    }

}
