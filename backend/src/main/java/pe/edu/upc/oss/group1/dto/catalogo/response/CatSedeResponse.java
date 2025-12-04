package pe.edu.upc.oss.group1.dto.catalogo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para sedes.
 * Incluye todos los campos de la entidad.
 */
@Getter
@Setter
@Builder
public class CatSedeResponse {

    private Integer id;
    private String codigo;
    private String nombre;
    private String direccion;
    private String ciudad;
    private String pais;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}
