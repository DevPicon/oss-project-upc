package pe.edu.upc.oss.group1.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatEstadoSolicitudResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para devolver información de una solicitud de devolución en la API.
 */
@Getter
@Setter
@Builder
public class SolicitudDevolucionResponse {

    private Integer id;
    private EmpleadoResponse empleado;
    private LocalDate fechaSolicitud;
    private LocalDate fechaTerminoEmpleado;
    private LocalDate fechaDevolucionProgramada;
    private LocalDate fechaDevolucionReal;
    private CatEstadoSolicitudResponse estadoSolicitud;
    private String observaciones;
    private UsuarioResponse usuarioSolicita;
    private UsuarioResponse usuarioRecibe;
    private Boolean pendiente;
    private Boolean completada;
    private LocalDateTime fechaCreacion;
}
