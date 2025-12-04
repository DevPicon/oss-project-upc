package pe.edu.upc.oss.group1.dto.catalogo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para proveedores.
 * Incluye todos los campos de la entidad.
 */
@Getter
@Setter
@Builder
public class CatProveedorResponse {

    private Integer id;
    private String codigo;
    private String razonSocial;
    private String ruc;
    private String email;
    private String telefono;
    private String direccion;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}
