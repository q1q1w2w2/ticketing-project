package com.ticketing.project.service;

import com.ticketing.project.dto.user.SignupDto;
import com.ticketing.project.entity.User;
import com.ticketing.project.enums.Role;
import com.ticketing.project.execption.user.UserAlreadyExistException;
import com.ticketing.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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
}
