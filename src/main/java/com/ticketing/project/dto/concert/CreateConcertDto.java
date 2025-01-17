package com.ticketing.project.dto.concert;

import com.ticketing.project.entity.Location;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateConcertDto {

    @NotEmpty
    private Long locationId;

    @NotEmpty
    private String title;

    @NotEmpty
    private LocalDateTime concertAt;

    @NotEmpty
    private LocalDateTime openAt;

    @NotEmpty
    private LocalDateTime closeAt;

}
