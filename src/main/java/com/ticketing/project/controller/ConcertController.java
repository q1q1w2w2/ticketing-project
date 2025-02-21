package com.ticketing.project.controller;

import com.ticketing.project.dto.concert.ConcertResponseDto;
import com.ticketing.project.dto.concert.CreateConcertDto;
import com.ticketing.project.entity.Concert;
import com.ticketing.project.service.ConcertService;
import com.ticketing.project.util.aop.LogUserAction;
import com.ticketing.project.util.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ticketing.project.util.common.ApiResponseUtil.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/concert")
public class ConcertController {

    private final ConcertService concertService;

    @PostMapping
    @LogUserAction("콘서트 등록")
    public ResponseEntity<ApiResponse<ConcertResponseDto>> saveConcert(@Valid @RequestBody CreateConcertDto concertDto) {
        Concert concert = concertService.saveConcert(concertDto);
        return createResponse(CREATED, "콘서트가 등록되었습니다.", new ConcertResponseDto(concert));
    }

    @PatchMapping("/cancel")
    @LogUserAction("콘서트 취소")
    public ResponseEntity<ApiResponse<Object>> cancelConcert(@RequestParam Long concertId) {
        concertService.cancelConcert(concertId);
        return createResponse(OK, "콘서트가 취소되었습니다.");
    }

    @GetMapping("/{concertId}")
    public ResponseEntity<ApiResponse<ConcertResponseDto>> getConcert(@PathVariable Long concertId) {
        ConcertResponseDto concert = concertService.getConcert(concertId);
        return createResponse(OK, concert);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ConcertResponseDto>>> getConcerts() {
        List<ConcertResponseDto> concerts = concertService.getConcerts();
        return createResponse(OK, concerts);
    }
}
