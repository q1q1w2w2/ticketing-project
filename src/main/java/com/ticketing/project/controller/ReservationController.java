package com.ticketing.project.controller;

import com.ticketing.project.dto.common.ApiResponse;
import com.ticketing.project.dto.reservation.CreateReservationDto;
import com.ticketing.project.dto.reservation.ReservationResponseDto;
import com.ticketing.project.entity.User;
import com.ticketing.project.service.QueueService;
import com.ticketing.project.service.ReservationService;
import com.ticketing.project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final QueueService queueService;
    private final UserService userService;

    @PostMapping("/queue/{concertId}")
    public ResponseEntity<ApiResponse<Object>> joinQueue(@PathVariable Long concertId) {
        User user = userService.getCurrentUser();
        boolean isAlreadyJoin = queueService.joinQueue(user, concertId);

        String message = isAlreadyJoin ? "이미 대기열에 참가중입니다." : "대기열에 참가했습니다.";
        ApiResponse<Object> response = ApiResponse.success(OK, message);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/leave/{concertId}")
    public ResponseEntity<ApiResponse<Object>> leaveQueue(@PathVariable Long concertId) {
        User user = userService.getCurrentUser();
        queueService.leaveQueue(user, concertId);

        ApiResponse<Object> response = ApiResponse.success(OK, "대기열에서 나갔습니다.");
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReservationResponseDto>> requestTicketing(@Valid @RequestBody CreateReservationDto reservationDto) {
        ReservationResponseDto result = reservationService.ticketing(reservationDto.getConcertId());

        ApiResponse<ReservationResponseDto> response = ApiResponse.success(CREATED, "예매가 완료되었습니다.", result);
        return ResponseEntity.status(CREATED).body(response);
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<Object>> cancelReservation(@RequestParam Long id) {
        User user = userService.getCurrentUser();
        reservationService.cancelReservation(id, user);

        ApiResponse<Object> response = ApiResponse.success(OK, "취소가 완료되었습니다.");
        return ResponseEntity.status(OK).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReservationResponseDto>>> getReservations() {
        User user = userService.getCurrentUser();
        List<ReservationResponseDto> reservations = reservationService.getReservations(user);

        ApiResponse<List<ReservationResponseDto>> response = ApiResponse.success(OK, reservations);
        return ResponseEntity.status(OK).body(response);
    }
}
