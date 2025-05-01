package com.project.blog_application.Exceptions;

import com.project.blog_application.payloads.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> userNotFoundExceptionResponse(UserNotFoundException ex){
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.toString(), ex.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ExceptionResponse> invalidPasswordExceptionResponse(InvalidPasswordException ex){
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.toString(), ex.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> methodArgumentTypeMismatchExceptionResponse(MethodArgumentTypeMismatchException ex){
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.toString(), ex.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ExceptionResponse> noResourceFoundExceptionResponse(NoResourceFoundException ex){
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.toString(), ex.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> methodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String, String> exceptionDetails = new LinkedHashMap<>();

        exceptionDetails.put("Exception", "MethodArgumentNotValidException: invalid input provided");
        exceptionDetails.put("FieldErrorsCount", String.valueOf(ex.getFieldErrorCount()));
        exceptionDetails.put("GlobalErrorsCount", String.valueOf(ex.getGlobalErrorCount()));
        ex.getBindingResult().getAllErrors().forEach(error-> {
            String fieldName = ((FieldError)error).getField();
            String fieldErrorMsg = error.getDefaultMessage();
            exceptionDetails.put(fieldName, fieldErrorMsg);
        });

        return new ResponseEntity<>(exceptionDetails, HttpStatus.BAD_REQUEST);
    }

}
