package com.menugraphy.server.domain.menu.model.entity;

import com.menugraphy.server.domain.menu.model.vo.MenuPrice;
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
@Table(name = "menu_board")
public class MenuBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "before_image", unique = true)
    private String beforeImage;

    @Column(name = "after_image", unique = true)
    private String afterImage;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "menu_price_list", columnDefinition = "jsonb")
    private List<MenuPrice> menuPriceList;

    @Builder
    private MenuBoard(
            final String beforeImage,
            final String afterImage,
            final List<MenuPrice> menuPriceList
    ) {
        this.beforeImage = beforeImage;
        this.afterImage = afterImage;
        this.menuPriceList = menuPriceList;
    }
}
