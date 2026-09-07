package com.hospital.schedulingservice.infra.controller.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.hospital.schedulingservice.domain.appointment.AppointmentConflictException;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger log
            = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<Map<String, Object>> handleOptimisticLocking(
            ObjectOptimisticLockingFailureException exception) {

        log.error(
                "Conflito de concorrência ao persistir agendamento",
                exception
        );

        return buildResponse(
                HttpStatus.CONFLICT,
                "O agendamento foi alterado ou removido por outra operação."
        );
    }

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAppointmentNotFound(AppointmentNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(AppointmentConflictException.class)
    public ResponseEntity<Map<String, Object>> handleAppointmentConflict(
            AppointmentConflictException exception) {

        log.warn("Conflito de agendamento: {}", exception.getMessage());

        Map<String, Object> response = new LinkedHashMap<>(buildResponseBody(
                HttpStatus.CONFLICT,
                exception.getMessage()
        ));

        if (exception.getSuggestedAppointmentDate() != null) {
            response.put(
                    "suggestedAppointmentDate",
                    exception.getSuggestedAppointmentDate()
            );
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException exception) {

        String message = exception.getBindingResult().getFieldError() != null
                ? exception.getBindingResult().getFieldError().getDefaultMessage()
                : "Dados inválidos";

        log.warn("Validação da requisição de agendamento rejeitada: {}", message);

        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleArgumentTypeMismatch(
            MethodArgumentTypeMismatchException exception) {

        log.warn(
                "Parâmetro inválido na requisição: name={}, value={}",
                exception.getName(),
                exception.getValue()
        );

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "ID informado em formato inválido"
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        log.error("Erro inesperado ao processar o agendamento", ex);

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro inesperado ao processar a solicitação"
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(
            DataIntegrityViolationException exception) {

        log.error("Erro de integridade ao salvar agendamento", exception);

        return buildResponse(
                HttpStatus.CONFLICT,
                "Não foi possível salvar o agendamento. Verifique os dados informados."
        );
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(buildResponseBody(status, message));
    }

    private Map<String, Object> buildResponseBody(HttpStatus status, String message) {
        return Map.of(
                "timestamp", Instant.now().toString(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message,
                "path", "/api/v1/appointments"
        );
    }
}
