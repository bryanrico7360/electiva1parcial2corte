
package com.example.demo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Gestiona globalmente las excepciones que ocurren en la API REST.
 *
 * <p>Centraliza el tratamiento de errores de validación, solicitudes
 * inválidas, recursos inexistentes y conflictos de integridad de datos,
 * devolviendo respuestas HTTP con mensajes descriptivos.</p>
 *
 * @author
 * @version 1.0
 */
@Slf4j
@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    /**
     * Gestiona los errores producidos por las validaciones de los datos
     * recibidos en una solicitud.
     *
     * @param ex excepción que contiene los errores de validación
     * @return respuesta HTTP 400 con la descripción del primer error
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidacion(
            MethodArgumentNotValidException ex) {

        String mensaje = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": "
                        + error.getDefaultMessage())
                .findFirst()
                .orElse("Los datos enviados no son válidos.");

        log.warn("Error de validación: {}", mensaje);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", "Error de validación",
                        "mensaje", mensaje
                ));
    }

    /**
     * Gestiona los errores causados por argumentos inválidos.
     *
     * @param ex excepción que describe el argumento inválido
     * @return respuesta HTTP 400 con el motivo del error
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> manejarArgumentoInvalido(
            IllegalArgumentException ex) {

        String mensaje = ex.getMessage() != null
                ? ex.getMessage()
                : "Los datos enviados no son válidos.";

        log.warn("Solicitud inválida: {}", mensaje);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", "Solicitud inválida",
                        "mensaje", mensaje
                ));
    }

    /**
     * Gestiona los errores ocasionados al buscar un recurso inexistente.
     *
     * @param ex excepción que contiene el mensaje del recurso no encontrado
     * @return respuesta HTTP 404 con la descripción del error
     */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> manejarRecursoNoEncontrado(
            RecursoNoEncontradoException ex) {

        log.warn("Recurso no encontrado: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", "Recurso no encontrado",
                        "mensaje", ex.getMessage()
                ));
    }

    /**
     * Gestiona los conflictos de integridad de los datos en la base de datos.
     *
     * @param ex excepción producida por una violación de integridad
     * @return respuesta HTTP 409 con un mensaje descriptivo del conflicto
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> manejarIntegridad(
            DataIntegrityViolationException ex) {

        log.warn("Conflicto de integridad en la base de datos.");

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error", "Conflicto de integridad",
                        "mensaje", "La operación entra en conflicto con los datos relacionados."
                ));
    }
}
