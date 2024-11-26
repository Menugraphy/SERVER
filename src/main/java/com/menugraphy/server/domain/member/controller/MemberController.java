package com.menugraphy.server.domain.member.controller;

import com.menugraphy.server.domain.member.model.dto.LoginRequest;
import com.menugraphy.server.domain.member.model.dto.LoginResponse;
import com.menugraphy.server.domain.member.model.enums.SocialType;
import com.menugraphy.server.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
