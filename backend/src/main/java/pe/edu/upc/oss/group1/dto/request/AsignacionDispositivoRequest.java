package pe.edu.upc.oss.group1.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO para crear una asignación de dispositivo a empleado.
 */
@Getter
@Setter
public class AsignacionDispositivoRequest {

    @NotNull(message = "El dispositivo es obligatorio")
    private Integer dispositivoId;

    @NotNull(message = "El empleado es obligatorio")
    private Integer empleadoId;

    private LocalDate fechaAsignacion;

    @Size(max = 500, message = "Las observaciones deben tener máximo 500 caracteres")
    private String observacionesAsignacion;

    @NotNull(message = "El usuario que asigna es obligatorio")
    private Integer usuarioAsignaId;
}
