package pe.edu.upc.oss.group1.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatEstadoDispositivoResponse;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatProveedorResponse;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatTipoDispositivoResponse;
import pe.edu.upc.oss.group1.dto.response.CatMarcaResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para devolver información de un dispositivo en la API.
 */
@Getter
@Setter
@Builder
public class DispositivoResponse {

    private Integer id;
    private String codigoActivo;
    private String numeroSerie;
    private CatTipoDispositivoResponse tipoDispositivo;
    private CatMarcaResponse marca;
    private String modelo;
    private String especificaciones;
    private LocalDate fechaAdquisicion;
    private BigDecimal valorAdquisicion;
    private CatProveedorResponse proveedor;
    private String observaciones;
    private CatEstadoDispositivoResponse estadoDispositivo;
    private Integer antiguedadEnAnios;
    private Boolean disponibleParaAsignacion;
    private LocalDateTime fechaCreacion;
}
