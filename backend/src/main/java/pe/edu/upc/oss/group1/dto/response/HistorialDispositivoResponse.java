package pe.edu.upc.oss.group1.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatTipoMovimientoResponse;

import java.time.LocalDateTime;

/**
 * DTO para devolver información del historial de un dispositivo en la API.
 */
@Getter
@Setter
@Builder
public class HistorialDispositivoResponse {

    private Integer id;
    private DispositivoResponse dispositivo;
    private CatTipoMovimientoResponse tipoMovimiento;
    private UsuarioResponse usuario;
    private LocalDateTime fechaMovimiento;
    private String descripcion;
    private String datosAnteriores;
    private String datosNuevos;
}
