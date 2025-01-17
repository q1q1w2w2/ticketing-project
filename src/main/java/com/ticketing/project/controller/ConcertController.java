package com.ticketing.project.controller;

import com.ticketing.project.dto.common.ApiResponse;
import com.ticketing.project.dto.concert.ConcertResponseDto;
import com.ticketing.project.dto.concert.CreateConcertDto;
import com.ticketing.project.entity.Concert;
import com.ticketing.project.service.ConcertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/concert")
public class ConcertController {

    private final ConcertService concertService;

    @PostMapping
    public ResponseEntity<ApiResponse<ConcertResponseDto>> saveConcert(@Valid @RequestBody CreateConcertDto concertDto) {
        Concert concert = concertService.saveConcert(concertDto);

        ApiResponse<ConcertResponseDto> response = ApiResponse.success(CREATED, "콘서트가 등록되었습니다.", new ConcertResponseDto(concert));
        return ResponseEntity.status(CREATED).body(response);
    }

    @PatchMapping("/cancel")
    public ResponseEntity cancelConcert(@RequestParam Long concertId) {
        concertService.cancelConcert(concertId);

        ApiResponse<Object> response = ApiResponse.success(OK, "콘서트가 취소되었습니다.");
        return ResponseEntity.status(OK).body(response);
    }
}
