package org.example.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //Parametros inválidos manuales
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> manejarIllegalArgument(IllegalArgumentException e) {
        log.warn("Error de parametros: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

        //Validaciones @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        log.warn("Error de validacion: {}", ex.getMessage());
        return ResponseEntity.badRequest().body("Datos invalidos");
    }


    // JSON mal formado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleJsonError(
            HttpMessageNotReadableException ex) {

        log.warn("JSON mal formado: {}", ex.getMessage());

        return ResponseEntity
                .badRequest()
                .body("JSON inválido");
    }

    // Tipo de dato incorrecto (ej: id="abc")
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        log.warn("Tipo de dato incorrecto: {}", ex.getMessage());

        return ResponseEntity
                .badRequest()
                .body("Tipo de dato inválido en parámetros");
    }

    // Parámetro faltante
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParam(
            MissingServletRequestParameterException ex) {

        log.warn("Parámetro faltante: {}", ex.getParameterName());

        return ResponseEntity
                .badRequest()
                .body("Falta el parámetro: " + ex.getParameterName());
    }

    // =========================
    // 🟡 404 - NEGOCIO
    // =========================

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Object> manejarNoEncontrado(
            RecursoNoEncontradoException e) {

        log.warn("Recurso no encontrado: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    // Endpoint inexistente
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Object> handleNoResourceFound(
            NoResourceFoundException e) {

        log.warn("Endpoint no encontrado: {}", e.getResourcePath());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "status", 404,
                        "error", "NOT_FOUND",
                        "message", "Endpoint no encontrado"
                ));
    }

    // =========================
    // 🔴 503 - BASE DE DATOS
    // =========================

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> manejarErrorDB(
            DataAccessException e) {

        log.error("Error en la base de datos", e);

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Servicio temporalmente no disponible");
    }

    // =========================
    // 🔴 500 - GENÉRICO
    // =========================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> manejarErrorGeneral(
            Exception e) {

        log.error("Error interno del servidor", e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor");
    }

}
