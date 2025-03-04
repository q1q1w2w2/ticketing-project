package com.ticketing.project.controller;

import com.ticketing.project.dto.concert.ConcertResponseDto;
import com.ticketing.project.dto.concert.CreateConcertDto;
import com.ticketing.project.entity.Concert;
import com.ticketing.project.service.concert.ConcertService;
import com.ticketing.project.util.aop.LogUserAction;
import com.ticketing.project.util.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
        return createResponse(OK, concertService.getConcert(concertId));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ConcertResponseDto>>> getConcerts() {
        return createResponse(OK, concertService.getConcerts());
    }

    @GetMapping
    public ResponseEntity getPagedConcerts(@RequestParam(defaultValue = "0") int page) {
        return createResponse(OK, concertService.getPagedConcerts(page));
    }
}
