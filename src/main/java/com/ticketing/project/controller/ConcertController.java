package com.ticketing.project.controller;

import com.ticketing.project.dto.common.ApiResponse;
import com.ticketing.project.dto.concert.ConcertResponseDto;
import com.ticketing.project.dto.concert.CreateConcertDto;
import com.ticketing.project.entity.Concert;
import com.ticketing.project.service.ConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/concert")
public class ConcertController {

    private final ConcertService concertService;

    @PostMapping
    public ResponseEntity<ApiResponse<ConcertResponseDto>> saveConcert(@RequestBody CreateConcertDto concertDto) {
        Concert concert = concertService.saveConcert(concertDto);

        ApiResponse<ConcertResponseDto> response = ApiResponse.success(CREATED, "콘서트가 등록되었습니다.", new ConcertResponseDto(concert));
        return ResponseEntity.status(CREATED).body(response);
    }
}
