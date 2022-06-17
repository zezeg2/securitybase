package com.example.securitybase.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ApiExceptionControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionPayload processValidationError(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        return new ExceptionPayload(ExceptionCode.INVALID_INPUT_VALUE, bindingResult);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ExceptionPayload processValidationError(IllegalArgumentException exception) {
        return new ExceptionPayload(ExceptionCode.INVALID_INPUT_VALUE);
    }


}
