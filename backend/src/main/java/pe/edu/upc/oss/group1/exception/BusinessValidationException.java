package pe.edu.upc.oss.group1.exception;

/**
 * Excepción lanzada cuando una operación viola una regla de negocio.
 * Ejemplo: Intentar asignar un dispositivo que ya está asignado.
 *
 * Esta excepción será capturada por el GlobalExceptionHandler
 * y devuelta como HTTP 400 Bad Request.
 */
public class BusinessValidationException extends RuntimeException {

    public BusinessValidationException(String message) {
        super(message);
    }
}
