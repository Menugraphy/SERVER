package com.menugraphy.server.domain.member.controller;

import com.menugraphy.server.domain.member.model.dto.AvoidanceListRequest;
import com.menugraphy.server.domain.member.model.dto.LikedFoodListResponse;
import com.menugraphy.server.domain.member.model.dto.LoginRequest;
import com.menugraphy.server.domain.member.model.dto.LoginResponse;
import com.menugraphy.server.domain.member.model.enums.SocialType;
import com.menugraphy.server.domain.member.service.MemberService;
import com.menugraphy.server.global.external.client.dto.MemberInfoResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        SocialType socialType = SocialType.fromValue(loginRequest.socialType());

        return ResponseEntity.ok(memberService.createAccessToken(socialType, loginRequest.idToken()));
    }

    @PostMapping("/token")
    public ResponseEntity<LoginResponse> getToken(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        SocialType socialType = SocialType.fromValue(loginRequest.socialType());

        return ResponseEntity.ok(memberService.getTokenDto(MemberInfoResponse.of(socialType, loginRequest.idToken())));
    }

    @PostMapping("/avoided-types")
    public ResponseEntity<Void> postAvoidanceList(
            @Valid @RequestBody AvoidanceListRequest avoidanceList
    ) {
        memberService.saveAvoidedTypes(avoidanceList);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/likes/{foodId}")
    public ResponseEntity<Void> postLike(
            @Positive(message = "음식 Id는 양수여야 합니다.")
            @Validated @PathVariable Long foodId
    ) {
        memberService.createLike(foodId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/likes/{foodId}")
    public ResponseEntity<Void> deleteLike(
            @Positive(message = "음식 Id는 양수여야 합니다.")
            @Validated @PathVariable Long foodId
    ) {
        memberService.removeLike(foodId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/likes")
    public ResponseEntity<LikedFoodListResponse> getLikes() {
        return ResponseEntity.ok(memberService.fetchLikes());
    }
}
