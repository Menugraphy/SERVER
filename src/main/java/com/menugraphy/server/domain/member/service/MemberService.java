package com.menugraphy.server.domain.member.service;

import com.menugraphy.server.domain.food.repository.CategoryRepository;
import com.menugraphy.server.domain.food.repository.FoodRepository;
import com.menugraphy.server.domain.food.repository.TypeRepository;
import com.menugraphy.server.domain.member.model.dto.AvoidanceListRequest;
import com.menugraphy.server.domain.member.model.dto.AvoidedTypeRequest;
import com.menugraphy.server.domain.member.model.dto.LoginResponse;
import com.menugraphy.server.domain.member.model.entity.FoodAvoidance;
import com.menugraphy.server.domain.member.model.entity.Member;
import com.menugraphy.server.domain.member.model.entity.MemberLike;
import com.menugraphy.server.domain.member.model.enums.SocialType;
import com.menugraphy.server.domain.member.repository.FoodAvoidanceRepository;
import com.menugraphy.server.domain.member.repository.MemberLikeRepository;
import com.menugraphy.server.domain.member.repository.MemberRepository;
import com.menugraphy.server.global.auth.MemberAuthentication;
import com.menugraphy.server.global.auth.PrincipalHandler;
import com.menugraphy.server.global.auth.jwt.JwtTokenProvider;
import com.menugraphy.server.global.exception.CustomException;
import com.menugraphy.server.global.exception.ErrorType;
import com.menugraphy.server.global.external.client.dto.MemberInfoResponse;
import com.menugraphy.server.global.external.client.service.GoogleSocialService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final CategoryRepository categoryRepository;
    private final TypeRepository typeRepository;
    private final MemberRepository memberRepository;
    private final FoodAvoidanceRepository foodAvoidanceRepository;
    private final MemberLikeRepository memberLikeRepository;
    private final FoodRepository foodRepository;
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

    @Transactional
    public void saveAvoidedTypes(final AvoidanceListRequest avoidanceListRequest) {
        if (avoidanceListRequest.avoidanceList() == null || avoidanceListRequest.avoidanceList().isEmpty()) {
            return;
        }

        Member member = memberRepository.findMemberByIdOrThrow(principalHandler.getUserIdFromPrincipal());
        validateAvoidanceList(avoidanceListRequest);

        List<FoodAvoidance> foodAvoidanceList = avoidanceListRequest.avoidanceList().stream()
                .map(request -> FoodAvoidance.builder()
                        .memberId(member.getId())
                        .categoryId(request.categoryId())
                        .typeId(request.typeId())
                        .build())
                .toList();

        foodAvoidanceRepository.saveAll(foodAvoidanceList);
    }

    private void validateAvoidanceList(AvoidanceListRequest avoidanceListRequest) {
        for (AvoidedTypeRequest avoidedTypeRequest : avoidanceListRequest.avoidanceList()) {
            if (!categoryRepository.existsById(avoidedTypeRequest.categoryId())) {
                throw new CustomException(ErrorType.NOT_FOUND_CATEGORY_ERROR);
            }
            if (!typeRepository.existsByCategoryIdAndId(avoidedTypeRequest.categoryId(), avoidedTypeRequest.typeId())) {
                throw new CustomException(ErrorType.NOT_FOUND_TYPE_ERROR);
            }
        }
    }

    @Transactional
    public void createLike(final Long foodId) {
        if (!foodRepository.existsById(foodId)) {
            throw new CustomException(ErrorType.NOT_FOUND_FOOD_ID_ERROR);
        }

        Member member = memberRepository.findMemberByIdOrThrow(principalHandler.getUserIdFromPrincipal());

        if (memberLikeRepository.existsByMemberIdAndFoodId(member.getId(), foodId)) {
            throw new CustomException(ErrorType.ALREADY_LIKED_FOOD_ERROR);
        }

        MemberLike memberLike = MemberLike.builder()
                .memberId(member.getId())
                .foodId(foodId)
                .build();

        memberLikeRepository.save(memberLike);
    }

    @Transactional
    public void removeLike(final Long foodId) {
        if (!foodRepository.existsById(foodId)) {
            throw new CustomException(ErrorType.NOT_FOUND_FOOD_ID_ERROR);
        }

        Member member = memberRepository.findMemberByIdOrThrow(principalHandler.getUserIdFromPrincipal());

        memberLikeRepository.deleteByMemberIdAndFoodId(member.getId(), foodId);
    }
}
