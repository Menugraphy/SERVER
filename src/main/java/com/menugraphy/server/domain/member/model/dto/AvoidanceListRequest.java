package com.menugraphy.server.domain.member.model.dto;

import jakarta.validation.Valid;
import java.util.List;

public record AvoidanceListRequest(
        List<@Valid AvoidedTypeRequest> avoidanceList
) {

}
