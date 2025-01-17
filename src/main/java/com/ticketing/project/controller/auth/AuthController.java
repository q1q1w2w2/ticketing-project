package com.ticketing.project.controller.auth;

import com.ticketing.project.dto.ApiResponse;
import com.ticketing.project.dto.auth.LoginDto;
import com.ticketing.project.dto.auth.TokenDto;
import com.ticketing.project.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        ApiResponse<TokenDto> response = ApiResponse.success(OK, "로그인 되었습니다.", tokens);
        return ResponseEntity.status(OK).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<TokenDto>> logout(@Valid @RequestBody TokenDto tokenDto) throws Exception {
        authService.logout(tokenDto);

        ApiResponse<TokenDto> response = ApiResponse.success(OK, "로그아웃 되었습니다.");
        return ResponseEntity.status(OK).body(response);
    }
}
