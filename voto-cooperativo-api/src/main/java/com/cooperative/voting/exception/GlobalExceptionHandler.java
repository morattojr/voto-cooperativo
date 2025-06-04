package com.cooperative.voting.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(VotingException.class)
    public ResponseEntity<Map<String, Object>> handleVotingException(VotingException ex) {
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", ex.getStatus(),
                "error", "Erro na votação",
                "message", ex.getMessage()
        );

        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        ex.printStackTrace(); // DEBUG: imprima a exceção no console
        System.out.println("Exceção capturada no handler genérico: " + ex.getClass().getName());

        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 500,
                "error", "Erro interno do servidor",
                "message", "Ocorreu um erro inesperado. Tente novamente mais tarde."
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}
