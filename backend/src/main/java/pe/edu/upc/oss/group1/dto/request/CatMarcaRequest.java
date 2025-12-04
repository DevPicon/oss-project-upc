package pe.edu.upc.oss.group1.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO para crear o actualizar una marca.
 *
 * Validaciones con Bean Validation:
 * - @NotBlank: No puede ser null, vacío o solo espacios en blanco
 * - @Size: Longitud mínima y máxima
 * - @NotNull: No puede ser null (pero puede estar vacío)
 *
 * Estos errores son capturados automáticamente por Spring
 * y devueltos como HTTP 400 Bad Request.
 */
@Getter
@Setter
public class CatMarcaRequest {

    /**
     * Código único de la marca.
     * Ejemplo: "DELL", "HP", "LENOVO"
     */
    @NotBlank(message = "El código es obligatorio")
    @Size(min = 2, max = 20, message = "El código debe tener entre 2 y 20 caracteres")
    private String codigo;

    /**
     * Nombre completo de la marca.
     * Ejemplo: "Dell", "Hewlett-Packard"
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    /**
     * Indica si la marca está activa.
     * Por defecto es true si no se especifica.
     */
    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo = true;
}
