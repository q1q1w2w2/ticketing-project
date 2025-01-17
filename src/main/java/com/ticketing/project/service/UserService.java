package com.ticketing.project.service;

import com.ticketing.project.dto.user.SignupDto;
import com.ticketing.project.entity.User;
import com.ticketing.project.util.enums.Role;
import com.ticketing.project.execption.user.UserAlreadyExistException;
import com.ticketing.project.execption.user.UserNotFoundException;
import com.ticketing.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User signup(SignupDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("이미 가입되어 있는 이메일입니다");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .tel(dto.getTel())
                .role(Role.ROLE_USER.toString())
                .build();

        return userRepository.save(user);
    }

    public User getCurrentUser() {
        String email = getEmailFromAuthentication();
        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    public String getEmailFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails userDetails) {
                String username = userDetails.getUsername();
                log.info("현재 사용자: {}", username);
                return username;
            } else if (principal instanceof String) {
                return (String) principal;
            }
        }
        throw new UserNotFoundException();
    }
}
