package com.mh.match.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

//    @ExceptionHandler(value = { ConstraintViolationException.class, DataIntegrityViolationException.class})
//    protected ResponseEntity<ErrorResponse> handleDataException() {
//        log.error("handleDataException throw Exception : {}", DUPLICATE_RESOURCE);
//        return ErrorResponse.toResponseEntity(DUPLICATE_RESOURCE);
//    }

//    @ExceptionHandler(value = {ConstraintViolationException.class})
//    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException e, WebRequest request) {
//        return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
//    }

    @ExceptionHandler(value = { CustomException.class })
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }
}
