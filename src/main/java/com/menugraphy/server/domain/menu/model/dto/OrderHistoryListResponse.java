package com.menugraphy.server.domain.menu.model.dto;

import java.util.List;

public record OrderHistoryListResponse(

        List<OrderHistoryResponse> orderHistoryList
) {

    public static OrderHistoryListResponse of(List<OrderHistoryResponse> orderHistoryList) {
        return new OrderHistoryListResponse(orderHistoryList);
    }
}
