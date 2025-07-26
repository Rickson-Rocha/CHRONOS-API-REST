package br.com.idus.chronos.controller.handler;


import br.com.idus.chronos.dto.customerros.CustomError;
import br.com.idus.chronos.dto.customerros.ValidationError;
import br.com.idus.chronos.service.exceptions.BaseApplicationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Instant;

@ControllerAdvice
@Slf4j
@ResponseBody
@Tag(name = "Error Handling", description = "Global exception handling for the API")
public class ControllerExceptionHandler {

    @Operation(hidden = true)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomError> handleParsingException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        CustomError err = new CustomError(
                Instant.now(),
                status.value(),
                "Erro ao processar JSON: verifique se os valores estão corretos.",
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }
    @Operation(hidden = true)
    @ExceptionHandler(BaseApplicationException.class)
    public ResponseEntity<CustomError> handleBaseApplicationExceptions(BaseApplicationException ex, HttpServletRequest request) {
        HttpStatus status = ex.getStatus();

        if (status.is4xxClientError()) {
            log.warn("Handled exception: {} - Status: {} - URI: {}", ex.getMessage(), status, request.getRequestURI());
        } else {
            log.error("Unhandled server exception: {} - Status: {} - URI: {}", ex.getMessage(), status, request.getRequestURI(), ex);
        }

        CustomError err = new CustomError(
                Instant.now(),
                status.value(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    @Operation(hidden = true)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomError> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError err = new ValidationError(Instant.now(), status.value(), "validation erros", request.getRequestURI());
        for (FieldError f : ex.getFieldErrors()) {
            err.addErro(f.getField(), f.getDefaultMessage()
            );
        }
        return ResponseEntity.status(status).body(err);
    }

//    @Operation(hidden = true)
//    @ExceptionHandler(UsernameNotFoundException.class)
//    public ResponseEntity<CustomError> handleUsernameNotFound(UsernameNotFoundException ex, HttpServletRequest request) {
//
//        HttpStatus status = HttpStatus.UNAUTHORIZED;
//        CustomError err = new CustomError(
//                Instant.now(),
//                status.value(),
//                "Credencial inválida: o usuário do token não foi encontrado no sistema.",
//                request.getRequestURI()
//        );
//        log.warn("Tentativa de acesso com usuário não registrado: {}", ex.getMessage());
//        return ResponseEntity.status(status).body(err);
//    }

}