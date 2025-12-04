package pe.edu.upc.oss.group1.dto.catalogo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO para crear/actualizar estados de solicitud.
 * Incluye validaciones con Bean Validation.
 */
@Getter
@Setter
public class CatEstadoSolicitudRequest {

    @NotBlank(message = "El código es obligatorio")
    @Size(min = 2, max = 20, message = "El código debe tener entre 2 y 20 caracteres")
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no debe exceder 500 caracteres")
    private String descripcion;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo = true;
}
