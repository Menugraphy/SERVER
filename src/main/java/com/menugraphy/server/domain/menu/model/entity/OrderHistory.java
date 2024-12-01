package com.menugraphy.server.domain.menu.model.entity;

import com.menugraphy.server.domain.menu.model.vo.MenuOrder;
import com.menugraphy.server.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_history")
public class OrderHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private long memberId;

    @Column(name = "menu_board_id", nullable = false)
    private long menuBoardId;

    @Column(name = "title")
    private String title;

    @Column(name = "restaurant_address")
    private String restaurantAddress;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "menu_order_list", columnDefinition = "jsonb")
    private List<MenuOrder> menuOrderList;

    @Column(name = "total_amount")
    private int totalAmount;

    @Builder
    private OrderHistory(
            final long memberId,
            final long menuBoardId,
            final String title,
            final String restaurantAddress,
            final List<MenuOrder> menuOrderList,
            final int totalAmount
    ) {
        this.memberId = memberId;
        this.menuBoardId = menuBoardId;
        this.title = title;
        this.restaurantAddress = restaurantAddress;
        this.menuOrderList = menuOrderList;
        this.totalAmount = totalAmount;
    }
}
