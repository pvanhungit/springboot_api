package com.awesome.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class RestErrorHandler extends ResponseEntityExceptionHandler {
    private final String INFO_MSG = "[Exception] %s\t%s";

    protected ExceptionEntity getErrorResponseEntity(AppException e) {
        String msg = ExceptionUtils.getMessageKey(e.getCode());
        logError(e);
        return new ExceptionEntity(e.getCode(), msg, e.getErrors());
    }

    @ExceptionHandler(AppException.class)
    protected ResponseEntity<Object> handleException(AppException e) {
        ExceptionEntity errorResponse = getErrorResponseEntity(e);
        return new ResponseEntity<>(errorResponse, e.getHttpStatus());
    }

    /**
     * handle exception for invalid request body data by using annotation @Validated
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatus status, final WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ExceptionEntity errorResponse = getErrorResponseEntity(
                new AppException(status, "EX60001", new ArrayList(errors.entrySet())
                ));

        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * handle exception for invalid parameters of query string of url data by using annotation @Validated
     */
    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex,
                                                         HttpHeaders headers,
                                                         HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ExceptionEntity errorResponse = getErrorResponseEntity(
                new AppException(status, "EX60001", new ArrayList(errors.entrySet())
                ));

        return new ResponseEntity<>(errorResponse, status);
    }

    private void logError(Exception ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        logger.error(sw.toString());
    }

    // catch all exceptions
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex) {
        AppException errors = new AppException(HttpStatus.INTERNAL_SERVER_ERROR, null, Arrays.asList(ex.getStackTrace()));
        ExceptionEntity errorResponse = getErrorResponseEntity(errors);
        logError(ex);

        return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
