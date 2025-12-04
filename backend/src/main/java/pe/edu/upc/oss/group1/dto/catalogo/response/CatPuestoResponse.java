package pe.edu.upc.oss.group1.dto.catalogo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para puestos.
 * Incluye referencia al área sin cargar toda la entidad.
 */
@Getter
@Setter
@Builder
public class CatPuestoResponse {

    private Integer id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Integer areaId;
    private String areaNombre;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}
