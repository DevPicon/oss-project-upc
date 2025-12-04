package pe.edu.upc.oss.group1.exception;

/**
 * Excepción lanzada cuando un recurso no se encuentra en la base de datos.
 * Ejemplo: Buscar una marca con ID que no existe.
 *
 * Esta excepción será capturada por el GlobalExceptionHandler
 * y devuelta como HTTP 404 Not Found.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s no encontrado con %s: '%s'", resourceName, fieldName, fieldValue));
    }
}
