package com.ticketing.project.service;

import com.ticketing.project.dto.concert.CreateConcertDto;
import com.ticketing.project.entity.Concert;
import com.ticketing.project.entity.Location;
import com.ticketing.project.execption.concert.ConcertAlreadyCancelException;
import com.ticketing.project.execption.concert.ConcertNotFoundException;
import com.ticketing.project.execption.location.LocationNotFoundException;
import com.ticketing.project.repository.ConcertRepository;
import com.ticketing.project.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ticketing.project.util.enums.ConcertStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final LocationRepository locationRepository;
    private final ReservationService reservationService;

    @Transactional
    public Concert saveConcert(CreateConcertDto dto) {
        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(LocationNotFoundException::new);
        Concert concert = Concert.builder()
                .title(dto.getTitle())
                .concertAt(dto.getConcertAt())
                .openAt(dto.getOpenAt())
                .closeAt(dto.getCloseAt())
                .location(location)
                .totalAmount(location.getTotalSeat())
                .status(SCHEDULED)
                .build();

        return concertRepository.save(concert);
    }

    @Transactional
    public void cancelConcert(Long concertId) {
        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(ConcertNotFoundException::new);
        if(concert.getStatus() == CANCELLED) {
            throw new ConcertAlreadyCancelException();
        }
        concert.changeStatus(CANCELLED);

        reservationService.cancelReservations(concert);
    }

}
