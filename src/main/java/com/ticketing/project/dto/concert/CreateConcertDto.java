package com.ticketing.project.dto.concert;

import com.ticketing.project.entity.Location;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateConcertDto {

    @NotNull
    private Long locationId;

    @NotEmpty
    private String title;

    @NotNull
    private LocalDateTime concertAt;

    @NotNull
    private LocalDateTime openAt;

    @NotNull
    private LocalDateTime closeAt;

}
