package com.menugraphy.server.domain.member.repository;

import com.menugraphy.server.domain.member.model.entity.FoodAvoidance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodAvoidanceRepository extends JpaRepository<FoodAvoidance, Long> {

    boolean existsByMemberIdAndTypeId(Long memberId, Long typeId);

}
