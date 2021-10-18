package com.programmers.springbootboard.error;

import com.programmers.springbootboard.common.converter.ResponseConverter;
import com.programmers.springbootboard.error.exception.DuplicationArgumentException;
import com.programmers.springbootboard.error.exception.InvalidArgumentException;
import com.programmers.springbootboard.error.exception.InvalidMediaTypeException;
import com.programmers.springbootboard.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ResponseConverter responseConverter;

    // TODO 중복된 로직을 빼보자!! --> enum으로 진행하자!!! 그럼 exception 코드 불필요한거 지울 수 있음!! exception을 instanceof로 찾아내서 ㄲ
    @ExceptionHandler(InvalidArgumentException.class) // 배열로 선언해서 다 갖고가버리기!
    protected ResponseEntity<ErrorResponseDto> handleInvalidArgumentException(InvalidArgumentException exception) {
        return responseConverter.toResponseEntity(
                HttpStatus.BAD_REQUEST,
                ErrorMessage.of(exception.getMessage())
        );
    }

    @ExceptionHandler(DuplicationArgumentException.class)
    protected ResponseEntity<ErrorResponseDto> handleDuplicationArgumentException(DuplicationArgumentException exception) {
        return responseConverter.toResponseEntity(
                HttpStatus.BAD_REQUEST,
                ErrorMessage.of(exception.getMessage())
        );
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ErrorResponseDto> handleNotFoundException(NotFoundException exception) {
        return responseConverter.toResponseEntity(
                HttpStatus.BAD_REQUEST,
                ErrorMessage.of(exception.getMessage())
        );
    }

    @ExceptionHandler(InvalidMediaTypeException.class)
    protected ResponseEntity<ErrorResponseDto> handleInvalidMediaTypeException() {
        return responseConverter.toResponseEntity(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                ErrorMessage.UNSUPPORTED_MEDIA_TYPE
        );
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponseDto> handleException(Exception e) {
        return responseConverter.toResponseEntity(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorMessage.INTERNAL_SERVER_ERROR
        );
    }
}
