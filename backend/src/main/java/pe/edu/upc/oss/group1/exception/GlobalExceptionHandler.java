package pe.edu.upc.oss.group1.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones para toda la API.
 *
 * @RestControllerAdvice: Indica que esta clase maneja excepciones
 * de todos los @RestController de la aplicación.
 *
 * Beneficios:
 * - Centralización: Un solo lugar para manejar errores
 * - Consistencia: Todas las respuestas de error tienen el mismo formato
 * - Limpieza: Los controllers no tienen try-catch
 * - Logging: Podemos loggear todos los errores aquí
 *
 * @Slf4j: Logger automático de Lombok
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Maneja ResourceNotFoundException (HTTP 404).
     * Se lanza cuando un recurso no se encuentra en la BD.
     *
     * Ejemplo: Buscar marca con ID que no existe.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        log.error("Recurso no encontrado: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("RESOURCE_NOT_FOUND")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Maneja DuplicateResourceException (HTTP 409 Conflict).
     * Se lanza cuando se intenta crear un recurso que ya existe.
     *
     * Ejemplo: Crear marca con código que ya existe.
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResource(DuplicateResourceException ex) {
        log.error("Recurso duplicado: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .error("DUPLICATE_RESOURCE")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Maneja BusinessValidationException (HTTP 400 Bad Request).
     * Se lanza cuando una operación viola una regla de negocio.
     *
     * Ejemplo: Intentar asignar un dispositivo que ya está asignado.
     */
    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ErrorResponse> handleBusinessValidation(BusinessValidationException ex) {
        log.error("Validación de negocio fallida: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("BUSINESS_VALIDATION_ERROR")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Maneja MethodArgumentNotValidException (HTTP 400 Bad Request).
     * Se lanza automáticamente cuando falla la validación de @Valid en DTOs.
     *
     * Ejemplo: Enviar código vacío en CatMarcaRequest.
     *
     * Esta excepción contiene múltiples errores de validación (uno por cada campo),
     * por eso los agrupamos en la lista 'details'.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        log.error("Errores de validación: {}", ex.getBindingResult().getFieldErrors());

        // Extraer todos los mensajes de error de validación
        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("VALIDATION_ERROR")
                .message("Errores de validación en la solicitud")
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Maneja cualquier otra excepción no esperada (HTTP 500 Internal Server Error).
     *
     * Este es el catch-all para errores que no anticipamos.
     * Es importante NO exponer detalles internos al cliente en producción.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Error interno no manejado: ", ex);

        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("INTERNAL_SERVER_ERROR")
                .message("Ha ocurrido un error interno. Por favor contacte al administrador.")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
