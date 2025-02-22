package com.ticketing.project.controller;

import com.ticketing.project.dto.auth.LoginDto;
import com.ticketing.project.dto.auth.TokenDto;
import com.ticketing.project.service.auth.AuthService;
import com.ticketing.project.util.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ticketing.project.util.common.ApiResponseUtil.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenDto>> login(@Valid @RequestBody LoginDto loginDto) throws Exception {
        TokenDto tokens = authService.login(loginDto);
        return createResponse(OK, "로그인 되었습니다.", tokens);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<TokenDto>> logout(@Valid @RequestBody TokenDto tokenDto) throws Exception {
        authService.logout(tokenDto);
        return createResponse(OK, "로그아웃 되었습니다.");
    }
}
