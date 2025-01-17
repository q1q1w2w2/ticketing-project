package com.ticketing.project.execption.handler;

import com.ticketing.project.dto.common.ApiResponse;
import com.ticketing.project.execption.auth.TokenValidationException;
import com.ticketing.project.execption.location.LocationNotFoundException;
import com.ticketing.project.execption.reservation.NoAvailableSeatException;
import com.ticketing.project.execption.reservation.ReservationNotFoundException;
import com.ticketing.project.execption.reservation.SingleTicketPerUserException;
import com.ticketing.project.execption.user.InvalidOwnerException;
import com.ticketing.project.execption.user.UserAlreadyExistException;
import com.ticketing.project.execption.user.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.ticketing.project.execption.messages.ErrorMessages.*;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneralException(Exception e) {
        return createErrorResponse(e, INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserAlreadyExistException(UserAlreadyExistException e) {
        return createErrorResponse(e, CONFLICT, e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentialsException(BadCredentialsException e) {
        return createErrorResponse(e, UNAUTHORIZED, INVALID_CREDENTIAL);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFoundException(UserNotFoundException e) {
        return createErrorResponse(e, NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(TokenValidationException.class)
    public ResponseEntity<ApiResponse<Object>> handleTokenValidationException(TokenValidationException e) {
        return createErrorResponse(e, BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(LocationNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleLocationNotFoundException(LocationNotFoundException e) {
        return createErrorResponse(e, NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(NoAvailableSeatException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoAvailableSeatException(NoAvailableSeatException e) {
        return createErrorResponse(e, BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleReservationNotFoundException(ReservationNotFoundException e) {
        return createErrorResponse(e, NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(InvalidOwnerException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidOwnerException(InvalidOwnerException e) {
        return createErrorResponse(e, BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(SingleTicketPerUserException.class)
    public ResponseEntity<ApiResponse<Object>> handleSingleTicketPerUserException(SingleTicketPerUserException e) {
        return createErrorResponse(e, BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(e.getMessage());
        return createErrorResponse(e, BAD_REQUEST, message);
    }

    private ResponseEntity<ApiResponse<Object>> createErrorResponse(Exception e, HttpStatus status, String message) {
        log.error("[{}] 발생: {}", e.getClass().getSimpleName(), message);
        ApiResponse<Object> response = ApiResponse.error(status, message);
        return ResponseEntity.status(status).body(response);
    }


}
