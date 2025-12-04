package pe.edu.upc.oss.group1.dto.catalogo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para áreas.
 * Incluye referencia al área superior sin cargar toda la jerarquía.
 */
@Getter
@Setter
@Builder
public class CatAreaResponse {

    private Integer id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Integer areaSuperiorId;
    private String areaSuperiorNombre;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}
