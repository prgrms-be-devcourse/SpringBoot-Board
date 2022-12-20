package com.ys.board.common;

import com.ys.board.common.exception.EntityNotFoundException;
import com.ys.board.common.response.ErrorResponse;
import com.ys.board.domain.user.DuplicateNameException;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestGlobalExceptionHandler {


    @ExceptionHandler(DuplicateNameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> duplicateEmailHandle(DuplicateNameException e,
        HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timeStamp(LocalDateTime.now())
            .message(e.getMessage())
            .requestUrl(request.getRequestURI())
            .method(request.getMethod())
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> illegalArgumentHandle(IllegalArgumentException e,
        HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timeStamp(LocalDateTime.now())
            .message(e.getMessage())
            .requestUrl(request.getRequestURI())
            .method(request.getMethod())
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> entityNotFoundHandle(EntityNotFoundException e,
        HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timeStamp(LocalDateTime.now())
            .message(e.getMessage())
            .requestUrl(request.getRequestURI())
            .method(request.getMethod())
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> defaultHandle(MethodArgumentNotValidException e, HttpServletRequest request) {

        ErrorResponse errorResponse = makeErrorResponseWithBindingResult(e.getBindingResult());
        errorResponse.setRequestUrl(request.getRequestURI());
        errorResponse.setMethod(request.getMethod());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private ErrorResponse makeErrorResponseWithBindingResult(BindingResult bindingResult) {
        String code = null;

        if (bindingResult.getFieldError() == null) {
            code = bindingResult.getFieldErrors().toString();
        } else {
            code = Objects.requireNonNull(bindingResult.getFieldError()).getCode();
        }

        StringBuilder builder = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("] - ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" . input value : [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]. ");
        }

        return ErrorResponse.builder()
            .timeStamp(LocalDateTime.now())
            .message(builder.toString())
            .build();
    }
}
