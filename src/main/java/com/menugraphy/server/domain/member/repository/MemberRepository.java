package com.menugraphy.server.domain.member.repository;

import com.menugraphy.server.domain.member.model.entity.Member;
import com.menugraphy.server.domain.member.model.enums.SocialType;
import com.menugraphy.server.global.exception.CustomException;
import com.menugraphy.server.global.exception.ErrorType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    default Member findBySocialTypeAndSocialIdOrThrow(SocialType socialType, String socialId) {
        return findBySocialTypeAndSocialId(socialType, socialId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_MEMBER_ERROR));
    }
}
