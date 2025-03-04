package com.ticketing.project.service.concert;

import com.ticketing.project.dto.concert.ConcertResponseDto;
import com.ticketing.project.dto.concert.CreateConcertDto;
import com.ticketing.project.entity.Concert;
import com.ticketing.project.entity.Location;
import com.ticketing.project.execption.concert.ConcertAlreadyCancelException;
import com.ticketing.project.execption.concert.ConcertNotFoundException;
import com.ticketing.project.execption.concert.InvalidConcertTimeException;
import com.ticketing.project.execption.location.LocationNotFoundException;
import com.ticketing.project.repository.ConcertRepository;
import com.ticketing.project.repository.LocationRepository;
import com.ticketing.project.service.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        validateSchedule(dto, LocalDateTime.now());

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

    public ConcertResponseDto getConcert(Long concertId) {
        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(ConcertNotFoundException::new);
        return new ConcertResponseDto(concert);
    }

    public List<ConcertResponseDto> getConcerts() {
        List<Concert> concerts = concertRepository.findAllByStatusNotIn(List.of(CANCELLED, FINISHED));
        return concerts.stream()
                .map(ConcertResponseDto::new)
                .toList();
    }

    public Page<ConcertResponseDto> getPagedConcerts(int page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<Concert> concerts = concertRepository.findPagedConcertsByStatusNotIn(List.of(CANCELLED, FINISHED), pageRequest);
        return concerts.map(ConcertResponseDto::new);
    }

    private void validateSchedule(CreateConcertDto dto, LocalDateTime now) {
        if (!dto.getOpenAt().isAfter(now)) {
            throw new InvalidConcertTimeException("오픈 시간은 현재 시간 이후여야 합니다.");
        }
        if (!dto.getCloseAt().isAfter(now) && !dto.getCloseAt().isAfter(dto.getOpenAt())) {
            throw new InvalidConcertTimeException("마감 시간은 오픈 시간 이후여야 합니다.");
        }
        if(!dto.getConcertAt().isAfter(dto.getCloseAt())) {
            throw new InvalidConcertTimeException("콘서트 시간은 마감 시간 이후여야 합니다.");
        }
    }
}
