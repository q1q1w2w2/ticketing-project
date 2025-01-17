package com.ticketing.project.dto.location;

import com.ticketing.project.entity.Location;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LocationResponseDto {

    private String locationName;
    private String address;
    private int totalSeat;
    private LocalDateTime createdAt;

    public LocationResponseDto(Location location) {
        this.locationName = location.getLocationName();
        this.address = location.getAddress();
        this.totalSeat = location.getTotalSeat();
        this.createdAt = location.getCreatedAt();
    }
}

