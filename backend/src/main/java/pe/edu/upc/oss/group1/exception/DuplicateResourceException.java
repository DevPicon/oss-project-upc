package pe.edu.upc.oss.group1.exception;

/**
 * Excepción lanzada cuando se intenta crear un recurso que ya existe.
 * Ejemplo: Crear una marca con un código que ya existe.
 *
 * Esta excepción será capturada por el GlobalExceptionHandler
 * y devuelta como HTTP 409 Conflict.
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s ya existe con %s: '%s'", resourceName, fieldName, fieldValue));
    }
}
