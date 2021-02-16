package com.github.oliverpavey.messageboard.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ApiExceptions {

    @Getter
    public static abstract class BaseApiException extends RuntimeException {
        private final HttpStatus httpStatus;
        private final String message;
        public BaseApiException(HttpStatus httpStatus, String message) {
            this.httpStatus = httpStatus;
            this.message = message;
        }
    }

    public static class DataNotFound extends BaseApiException {
        public DataNotFound(String message) {
            super(HttpStatus.NOT_FOUND, message);
        }
    }

    public static class BadRequest extends BaseApiException {
        public BadRequest(String message) {
            super(HttpStatus.PRECONDITION_FAILED, message);
        }
    }

    public static class DataInvalid extends BaseApiException {
        public DataInvalid(String message) {
            super(HttpStatus.NOT_ACCEPTABLE, message);
        }
    }
}
