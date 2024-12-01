package com.menugraphy.server.domain.menu.model.dto;

import com.menugraphy.server.domain.menu.model.vo.MenuOrderHistory;
import java.util.List;

public record OrderHistoryResponse(
        String title,
        String orderedAt,
        int totalPrice,
        String localizedTotalPrice,
        List<MenuOrderHistory> menuOrderList
) {

    public static OrderHistoryResponse of(String title, String orderedAt, int totalPrice, String localizedTotalPrice,
                                          List<MenuOrderHistory> menuOrderList) {
        return new OrderHistoryResponse(title, orderedAt, totalPrice, localizedTotalPrice, menuOrderList);
    }
}
