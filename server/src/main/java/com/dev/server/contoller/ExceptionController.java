package com.dev.server.contoller;

import com.dev.server.dto.ErrorResponseDTO;
import com.dev.server.exception.GeneralException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(GeneralException.class)
    private ResponseEntity<ErrorResponseDTO> handleException(GeneralException e, HttpServletRequest request) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setTimestamp(new Date());
        errorResponseDTO.setStatus(e.getHttpStatus().value());
        errorResponseDTO.setError(e.getMessage());
        errorResponseDTO.setPath(request.getRequestURI());
        return new ResponseEntity<>(errorResponseDTO, e.getHttpStatus());
    }
}

