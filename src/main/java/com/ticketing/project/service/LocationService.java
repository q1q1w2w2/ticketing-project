package com.ticketing.project.service;

import com.ticketing.project.dto.location.CreateLocationDto;
import com.ticketing.project.entity.Location;
import com.ticketing.project.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationService {

    private final LocationRepository locationRepository;

    @Transactional
    public Location saveLocation(CreateLocationDto dto) {
        Location location = Location.builder()
                .locationName(dto.getLocationName())
                .address(dto.getAddress())
                .totalSeat(dto.getTotalSeat())
                .build();

        return locationRepository.save(location);
    }

}
