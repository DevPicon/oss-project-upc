package pe.edu.upc.oss.group1.dto.catalogo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para tipos de dispositivo.
 * Incluye todos los campos de la entidad.
 */
@Getter
@Setter
@Builder
public class CatTipoDispositivoResponse {

    private Integer id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Boolean requiereSerie;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}
