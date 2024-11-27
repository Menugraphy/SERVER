package com.menugraphy.server.domain.member.service;

import com.menugraphy.server.domain.member.model.dto.LoginResponse;
import com.menugraphy.server.domain.member.model.entity.Member;
import com.menugraphy.server.domain.member.model.enums.SocialType;
import com.menugraphy.server.domain.member.repository.MemberRepository;
import com.menugraphy.server.global.auth.MemberAuthentication;
import com.menugraphy.server.global.auth.PrincipalHandler;
import com.menugraphy.server.global.auth.jwt.JwtTokenProvider;
import com.menugraphy.server.global.exception.CustomException;
import com.menugraphy.server.global.exception.ErrorType;
import com.menugraphy.server.global.external.client.dto.MemberInfoResponse;
import com.menugraphy.server.global.external.client.service.GoogleSocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PrincipalHandler principalHandler;
    private final GoogleSocialService googleSocialService;

    @Transactional
    public LoginResponse createAccessToken(
            final SocialType socialType,
            final String idTokenString
    ) {
        return getTokenDto(fetchMemberInfo(socialType, idTokenString));
    }

    private MemberInfoResponse fetchMemberInfo(
            final SocialType socialType,
            final String idTokenString
    ) {
        if (socialType == SocialType.GOOGLE) {
            return googleSocialService.login(socialType, idTokenString);
        }

        throw new CustomException(ErrorType.INVALID_SOCIAL_TYPE_ERROR);
    }

    public LoginResponse getTokenDto(final MemberInfoResponse memberInfoResponse) {
        Member member;

        try {
            if (isExistingMember(memberInfoResponse.socialType(), memberInfoResponse.socialId())) {
                member = memberRepository.findBySocialTypeAndSocialIdOrThrow(
                        memberInfoResponse.socialType(),
                        memberInfoResponse.socialId()
                );
            } else {
                member = Member.builder()
                        .socialType(memberInfoResponse.socialType())
                        .socialId(memberInfoResponse.socialId())
                        .build();

                member = memberRepository.save(member);
            }
        } catch (DataIntegrityViolationException e) {
            member = memberRepository.findBySocialTypeAndSocialIdOrThrow(
                    memberInfoResponse.socialType(),
                    memberInfoResponse.socialId()
            );
        }

        return getTokenByMemberId(member.getId());
    }

    private boolean isExistingMember(
            final SocialType socialType,
            final String socialId
    ) {
        return memberRepository.findBySocialTypeAndSocialId(socialType, socialId).isPresent();
    }

    public LoginResponse getTokenByMemberId(final Long id) {
        MemberAuthentication memberAuthentication = new MemberAuthentication(id, null, null);

        return LoginResponse.of(jwtTokenProvider.issueAccessToken(memberAuthentication));
    }
}
