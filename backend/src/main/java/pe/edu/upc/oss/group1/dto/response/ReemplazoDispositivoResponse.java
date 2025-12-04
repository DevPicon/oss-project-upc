package pe.edu.upc.oss.group1.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatEstadoReemplazoResponse;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatMotivoReemplazoResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para devolver información de un reemplazo de dispositivo en la API.
 */
@Getter
@Setter
@Builder
public class ReemplazoDispositivoResponse {

    private Integer id;
    private AsignacionDispositivoResponse asignacionOriginal;
    private DispositivoResponse dispositivoOriginal;
    private DispositivoResponse dispositivoReemplazo;
    private EmpleadoResponse empleado;
    private CatMotivoReemplazoResponse motivoReemplazo;
    private LocalDate fechaReemplazo;
    private String descripcionMotivo;
    private CatEstadoReemplazoResponse estadoReemplazo;
    private UsuarioResponse usuarioRegistra;
    private Boolean pendiente;
    private LocalDateTime fechaCreacion;
}
