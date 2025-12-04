package pe.edu.upc.oss.group1.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO para devolver información de un usuario en la API.
 * NO expone el password por seguridad.
 */
@Getter
@Setter
@Builder
public class UsuarioResponse {

    private Integer id;
    private String username;
    private String email;
    private String nombreCompleto;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}
