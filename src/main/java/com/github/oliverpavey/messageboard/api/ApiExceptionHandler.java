package com.github.oliverpavey.messageboard.api;

import com.github.oliverpavey.messageboard.api.ApiExceptions.BaseApiException;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Data
    @Builder
    public static class ErrorMessage {
        private int status;
        private String message;
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception ex) {

        try {
            if (ex instanceof BaseApiException) {
                final BaseApiException apiException = (BaseApiException) ex;
                final HttpStatus httpStatus = apiException.getHttpStatus();
                final ErrorMessage errorMessage = ErrorMessage.builder()
                        .status(httpStatus.value()).message(apiException.getMessage()).build();
                return ResponseEntity.status(httpStatus.value()).body(errorMessage);

            } else {
                log.warn("Unexpected error during HttpRequest.", ex);
                final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                final ErrorMessage errorMessage = ErrorMessage.builder()
                        .status(httpStatus.value()).message(ex.getMessage()).build();
                return ResponseEntity.status(httpStatus.value()).body(errorMessage);
            }

        } catch (Exception e) {
            log.warn("Error during HTTP Error Handling.", e);
            final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            return ResponseEntity.status(httpStatus).body(ErrorMessage.builder()
                    .status(httpStatus.value()).message("Error in HTTP Error Handling.").build());
        }
    }
}
