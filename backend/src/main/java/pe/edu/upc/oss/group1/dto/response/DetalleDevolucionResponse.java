package pe.edu.upc.oss.group1.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatCondicionDevolucionResponse;

import java.time.LocalDateTime;

/**
 * DTO para devolver información de un detalle de devolución en la API.
 */
@Getter
@Setter
@Builder
public class DetalleDevolucionResponse {

    private Integer id;
    private SolicitudDevolucionResponse solicitudDevolucion;
    private DispositivoResponse dispositivo;
    private AsignacionDispositivoResponse asignacion;
    private CatCondicionDevolucionResponse condicionDevolucion;
    private String observaciones;
    private LocalDateTime fechaCreacion;
}
