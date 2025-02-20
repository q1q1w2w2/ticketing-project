package com.ticketing.project.controller;

import com.ticketing.project.dto.location.CreateLocationDto;
import com.ticketing.project.dto.location.LocationResponseDto;
import com.ticketing.project.entity.Location;
import com.ticketing.project.service.LocationService;
import com.ticketing.project.util.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ticketing.project.util.common.ApiResponseUtil.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/location")
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<ApiResponse<LocationResponseDto>> saveLocation(@Valid @RequestBody CreateLocationDto locationDto) {
        Location location = locationService.saveLocation(locationDto);
        return createResponse(CREATED, "공연장이 저장되었습니다.", new LocationResponseDto(location));
    }
}
