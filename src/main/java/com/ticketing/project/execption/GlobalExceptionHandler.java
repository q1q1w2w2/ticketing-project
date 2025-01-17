package com.ticketing.project.execption;

import com.ticketing.project.dto.ApiResponse;
import com.ticketing.project.execption.user.UserAlreadyExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.ticketing.project.execption.messages.ErrorMessages.INVALID_CREDENTIAL;
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
