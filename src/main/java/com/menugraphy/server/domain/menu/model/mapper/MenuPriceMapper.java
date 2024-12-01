package com.menugraphy.server.domain.menu.model.mapper;

import com.menugraphy.server.domain.menu.model.vo.MenuPrice;
import com.menugraphy.server.global.external.ai.dto.MenuResultResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MenuPriceMapper {

    @Mapping(target = "foodId", source = "menuId")
    MenuPrice toMenuPrice(MenuResultResponse menuResultResponse);
}
