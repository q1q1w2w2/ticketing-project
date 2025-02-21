package com.ticketing.project.controller;

import com.ticketing.project.dto.reservation.CreateReservationDto;
import com.ticketing.project.dto.reservation.ReservationResponseDto;
import com.ticketing.project.entity.User;
import com.ticketing.project.service.QueueService;
import com.ticketing.project.service.ReservationService;
import com.ticketing.project.service.UserService;
import com.ticketing.project.util.aop.LogUserAction;
import com.ticketing.project.util.common.ApiResponse;
import com.ticketing.project.util.common.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ticketing.project.util.common.ApiResponseUtil.*;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final QueueService queueService;

    @PostMapping("/queue/{concertId}")
    public ResponseEntity<ApiResponse<Object>> joinQueue(@PathVariable Long concertId, @LoginUser User user) {
        boolean isAlreadyJoin = queueService.joinQueue(user, concertId);
        String message = isAlreadyJoin ? "이미 대기열에 참가중입니다." : "대기열에 참가했습니다.";
        return createResponse(OK, message);
    }

    @PostMapping("/leave/{concertId}")
    public ResponseEntity<ApiResponse<Object>> leaveQueue(@PathVariable Long concertId, @LoginUser User user) {
        queueService.leaveQueue(user, concertId);
        return createResponse(OK, "대기열에서 나갔습니다.");
    }

    @PostMapping
    @LogUserAction("콘서트 예매")
    public ResponseEntity<ApiResponse<ReservationResponseDto>> requestTicketing(@Valid @RequestBody CreateReservationDto reservationDto) {
        ReservationResponseDto result = reservationService.ticketing(reservationDto.getConcertId());
        return createResponse(CREATED, "예매가 완료되었습니다.", result);
    }

    @PatchMapping
    @LogUserAction("콘서트 취소")
    public ResponseEntity<ApiResponse<Object>> cancelReservation(@RequestParam Long id, @LoginUser User user) {
        reservationService.cancelReservation(id, user);
        return createResponse(OK, "취소가 완료되었습니다.");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReservationResponseDto>>> getReservations(@LoginUser User user) {
        List<ReservationResponseDto> reservations = reservationService.getReservations(user);
        return createResponse(OK, reservations);
    }
}
