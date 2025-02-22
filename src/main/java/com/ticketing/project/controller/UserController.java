package com.ticketing.project.controller;

import com.ticketing.project.dto.user.SignupDto;
import com.ticketing.project.dto.user.SignupResponseDto;
import com.ticketing.project.entity.User;
import com.ticketing.project.service.user.UserService;
import com.ticketing.project.util.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ticketing.project.util.common.ApiResponseUtil.*;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<SignupResponseDto>> signUp(@Valid @RequestBody SignupDto signupDto) {
        User user = userService.signup(signupDto);
        return createResponse(CREATED, "회원가입이 완료되었습니다.", new SignupResponseDto(user));
    }
}
