package pe.edu.upc.oss.group1.dto.catalogo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para estados de asignación.
 * Incluye todos los campos de la entidad.
 */
@Getter
@Setter
@Builder
public class CatEstadoAsignacionResponse {

    private Integer id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}
