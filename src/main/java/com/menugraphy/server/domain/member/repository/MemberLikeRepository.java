package com.menugraphy.server.domain.member.repository;

import com.menugraphy.server.domain.member.model.entity.MemberLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberLikeRepository extends JpaRepository<MemberLike, Long> {

    boolean existsByMemberIdAndFoodId(long memberId, long foodId);

    void deleteByMemberIdAndFoodId(long memberId, long foodId);
}
