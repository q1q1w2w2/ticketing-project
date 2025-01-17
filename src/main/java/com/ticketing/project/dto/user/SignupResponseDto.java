package com.ticketing.project.dto.user;

import com.ticketing.project.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SignupResponseDto {

    private Long userId;
    private String email;
    private String username;
    private LocalDateTime createdAt;

    public SignupResponseDto(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.createdAt = user.getCreatedAt();
    }
}
