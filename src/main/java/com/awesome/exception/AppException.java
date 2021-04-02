package com.awesome.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
public class AppException extends RuntimeException {
    private static final long serialVersionUID = -7806022902430164987L;
    private String code;
    private HttpStatus httpStatus;
    private List<?> errors;

    public AppException(HttpStatus httpStatus, String code, List<?> errors) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.errors = errors;
    }
}
