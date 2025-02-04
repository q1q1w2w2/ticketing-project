package com.ticketing.project.controller;

import com.ticketing.project.dto.common.ApiResponse;
import com.ticketing.project.dto.ticket.TicketResponseDto;
import com.ticketing.project.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ticket")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponse<TicketResponseDto>> getTicket(@PathVariable String uuid) {
        TicketResponseDto ticket = ticketService.findByUuid(uuid);

        ApiResponse<TicketResponseDto> response = ApiResponse.success(OK, ticket);
        return ResponseEntity.ok(response);
    }
}
