package com.ticketing.project.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDto {

    @NotEmpty(message = "accessToken은 비어있을 수 없습니다.")
    private String accessToken;

    @NotEmpty(message = "refreshToken은 비어있을 수 없습니다.")
    private String refreshToken;

}
