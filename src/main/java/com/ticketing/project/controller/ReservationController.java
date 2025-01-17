package com.ticketing.project.controller;

import com.ticketing.project.dto.common.ApiResponse;
import com.ticketing.project.dto.reservation.CreateReservationDto;
import com.ticketing.project.dto.reservation.ReservationResponseDto;
import com.ticketing.project.entity.Ticket;
import com.ticketing.project.entity.User;
import com.ticketing.project.service.ReservationService;
import com.ticketing.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReservationResponseDto>> createReservation(@RequestBody CreateReservationDto reservationDto) {
        User user = userService.getCurrentUser();
        ReservationResponseDto result = reservationService.ticketing(reservationDto.getConcertId(), user);

        ApiResponse<ReservationResponseDto> response = ApiResponse.success(CREATED, "예매가 완료되었습니다.", result);
        return ResponseEntity.status(CREATED).body(response);
    }

    @PatchMapping
    public ResponseEntity cancelReservation(@RequestParam Long id) {
        User user = userService.getCurrentUser();
        reservationService.cancelReservation(id, user);

        ApiResponse<Object> response = ApiResponse.success(OK, "취소가 완료되었습니다.");
        return ResponseEntity.status(OK).body(response);
    }
}
