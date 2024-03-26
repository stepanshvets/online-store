package com.dev.server.contoller;

import com.dev.server.dto.ErrorResponseDto;
import com.dev.server.exception.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Date;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(GeneralException.class)
    private ResponseEntity<ErrorResponseDto> handleException(GeneralException e, HttpServletRequest request) {
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto();
        errorResponseDTO.setTimestamp(new Date());
        errorResponseDTO.setStatus(e.getHttpStatus().value());
        errorResponseDTO.setError(e.getMessage());
        errorResponseDTO.setPath(request.getRequestURI());
        return new ResponseEntity<>(errorResponseDTO, e.getHttpStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    private ResponseEntity<ErrorResponseDto> handleConstraintViolationException(
            ConstraintViolationException e, HttpServletRequest request) {
//        Javax
//        Controller lawyer: @Validated class + @RequestParam @NotBlank param - validate request parameters
//        Service lawyer: @Validated class + @Valid dto - validate dto

        StringBuilder message = new StringBuilder();
        for (ConstraintViolation<?> constraintViolation: e.getConstraintViolations()) {
            String s = null;
            for (Path.Node node : constraintViolation.getPropertyPath()) {
                s =  node.getName();
            }
            message.append(s)
                    .append(" ")
                    .append(constraintViolation.getMessage())
                    .append(";");
        }

        ErrorResponseDto errorResponseDTO = new ErrorResponseDto();
        errorResponseDTO.setTimestamp(new Date());
        errorResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponseDTO.setError(message.toString());
        errorResponseDTO.setPath(request.getRequestURI());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e, HttpServletRequest request) {
//        Spring
//        Controller lawyer: @Valid dto - validate dto

        StringBuilder message = new StringBuilder();
        for (FieldError error: e.getBindingResult().getFieldErrors())
            message.append(error.getField()).append(" ").append(error.getDefaultMessage());

        ErrorResponseDto errorResponseDTO = new ErrorResponseDto();
        errorResponseDTO.setTimestamp(new Date());
        errorResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponseDTO.setError(message.toString());
        errorResponseDTO.setPath(request.getRequestURI());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }
}


