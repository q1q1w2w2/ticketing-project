package com.ticketing.project.dto.location;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateLocationDto {

    @NotEmpty
    private String locationName;

    @NotEmpty
    private String address;

    @Min(value = 1)
    private int totalSeat;
}
