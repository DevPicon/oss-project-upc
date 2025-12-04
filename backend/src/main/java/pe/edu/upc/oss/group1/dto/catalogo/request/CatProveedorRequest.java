package pe.edu.upc.oss.group1.dto.catalogo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO para crear/actualizar proveedores.
 * Incluye validaciones con Bean Validation.
 */
@Getter
@Setter
public class CatProveedorRequest {

    @NotBlank(message = "El código es obligatorio")
    @Size(min = 2, max = 20, message = "El código debe tener entre 2 y 20 caracteres")
    private String codigo;

    @NotBlank(message = "La razón social es obligatoria")
    @Size(min = 2, max = 200, message = "La razón social debe tener entre 2 y 200 caracteres")
    private String razonSocial;

    @NotBlank(message = "El RUC es obligatorio")
    @Size(min = 11, max = 11, message = "El RUC debe tener 11 caracteres")
    private String ruc;

    @Email(message = "El email debe ser válido")
    @Size(max = 100, message = "El email no debe exceder 100 caracteres")
    private String email;

    @Size(max = 20, message = "El teléfono no debe exceder 20 caracteres")
    private String telefono;

    @Size(max = 200, message = "La dirección no debe exceder 200 caracteres")
    private String direccion;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo = true;
}
