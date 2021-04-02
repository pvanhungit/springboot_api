package com.awesome.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ExceptionEntity {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp = new Timestamp(System.currentTimeMillis()).toLocalDateTime();

    private String message;

    private List<?> errors;

    private String path =
            ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest()
                    .getServletPath();

    private String errorCode;

    public ExceptionEntity(String errorCode, String message, List<?> errors) {
        this.message = message;
        this.errorCode = errorCode;
        this.errors = errors;
    }
}
