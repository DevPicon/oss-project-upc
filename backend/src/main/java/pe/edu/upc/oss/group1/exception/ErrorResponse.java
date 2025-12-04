package pe.edu.upc.oss.group1.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Estructura estándar para respuestas de error en la API.
 *
 * Todos los errores devuelven este formato consistente,
 * facilitando el manejo de errores en el frontend.
 *
 * Ejemplo de respuesta:
 * {
 *   "status": 404,
 *   "error": "RESOURCE_NOT_FOUND",
 *   "message": "Marca no encontrada con id: 123",
 *   "details": null,
 *   "timestamp": "2025-01-15T10:30:00"
 * }
 */
@Getter
@Setter
@Builder
public class ErrorResponse {

    /**
     * Código HTTP del error (404, 400, 409, 500, etc.)
     */
    private Integer status;

    /**
     * Código de error en mayúsculas (RESOURCE_NOT_FOUND, VALIDATION_ERROR, etc.)
     * Útil para el frontend hacer switch-case sobre errores específicos.
     */
    private String error;

    /**
     * Mensaje descriptivo del error para mostrar al usuario.
     */
    private String message;

    /**
     * Lista de detalles adicionales (opcional).
     * Útil para errores de validación con múltiples campos.
     * Ejemplo: ["El email es obligatorio", "El código debe tener entre 2 y 20 caracteres"]
     */
    private List<String> details;

    /**
     * Timestamp de cuándo ocurrió el error.
     * Útil para debugging y logs.
     */
    private LocalDateTime timestamp;
}
