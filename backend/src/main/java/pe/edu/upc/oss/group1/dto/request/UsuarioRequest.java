package pe.edu.upc.oss.group1.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO para crear o actualizar un usuario del sistema.
 */
@Getter
@Setter
public class UsuarioRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 100, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(min = 3, max = 200, message = "El nombre completo debe tener entre 3 y 200 caracteres")
    private String nombreCompleto;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo = true;
}
