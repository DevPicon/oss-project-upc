package pe.edu.upc.oss.group1.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para crear o actualizar un dispositivo.
 */
@Getter
@Setter
public class DispositivoRequest {

    @NotBlank(message = "El código de activo es obligatorio")
    @Size(max = 50, message = "El código de activo debe tener máximo 50 caracteres")
    private String codigoActivo;

    @NotBlank(message = "El número de serie es obligatorio")
    @Size(max = 100, message = "El número de serie debe tener máximo 100 caracteres")
    private String numeroSerie;

    @NotNull(message = "El tipo de dispositivo es obligatorio")
    private Integer tipoDispositivoId;

    @NotNull(message = "La marca es obligatoria")
    private Integer marcaId;

    @Size(max = 100, message = "El modelo debe tener máximo 100 caracteres")
    private String modelo;

    @Size(max = 500, message = "Las especificaciones deben tener máximo 500 caracteres")
    private String especificaciones;

    private LocalDate fechaAdquisicion;

    private BigDecimal valorAdquisicion;

    @NotNull(message = "El proveedor es obligatorio")
    private Integer proveedorId;

    @Size(max = 500, message = "Las observaciones deben tener máximo 500 caracteres")
    private String observaciones;

    @NotNull(message = "El estado del dispositivo es obligatorio")
    private Integer estadoDispositivoId;
}
