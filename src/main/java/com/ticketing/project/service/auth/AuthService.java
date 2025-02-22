package com.ticketing.project.service.auth;

import com.ticketing.project.dto.auth.LoginDto;
import com.ticketing.project.dto.auth.TokenDto;
import com.ticketing.project.execption.auth.TokenValidationException;
import com.ticketing.project.repository.UserRepository;
import com.ticketing.project.util.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static com.ticketing.project.util.jwt.TokenProvider.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public TokenDto login(LoginDto dto) throws Exception {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String email = dto.getEmail();
        String authority = authenticate.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = tokenProvider.generateAccessToken(email, authority);
        String refreshToken = tokenProvider.generateRefreshToken(email);
        return new TokenDto(accessToken, refreshToken);
    }

    @Transactional
    public void logout(TokenDto tokenDto) throws Exception {
        String refreshToken = tokenDto.getRefreshToken();
        String accessToken = tokenDto.getAccessToken();
        if (!tokenProvider.validateToken(accessToken) || !tokenProvider.validateToken(refreshToken)) {
            throw new TokenValidationException();
        }

        String email = tokenProvider.extractEmailFromToken(refreshToken);
        String redisKey = REFRESH_TOKEN_PREFIX + email;

        Boolean hasKey = redisTemplate.hasKey(redisKey);
        if (hasKey != null && hasKey) {
            redisTemplate.delete( REFRESH_TOKEN_PREFIX + email);
            log.info("로그아웃 되었습니다.");
        } else {
            throw new TokenValidationException("토큰이 존재하지 않습니다.");
        }
    }
}
