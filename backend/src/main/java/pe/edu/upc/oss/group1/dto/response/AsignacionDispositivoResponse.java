package pe.edu.upc.oss.group1.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatEstadoAsignacionResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para devolver información de una asignación de dispositivo en la API.
 */
@Getter
@Setter
@Builder
public class AsignacionDispositivoResponse {

    private Integer id;
    private DispositivoResponse dispositivo;
    private EmpleadoResponse empleado;
    private LocalDate fechaAsignacion;
    private LocalDate fechaDevolucion;
    private CatEstadoAsignacionResponse estadoAsignacion;
    private String observacionesAsignacion;
    private String observacionesDevolucion;
    private UsuarioResponse usuarioAsigna;
    private UsuarioResponse usuarioRecibe;
    private Integer diasAsignado;
    private Boolean activa;
    private LocalDateTime fechaCreacion;
}
