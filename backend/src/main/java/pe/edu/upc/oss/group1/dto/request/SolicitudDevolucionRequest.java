package pe.edu.upc.oss.group1.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO para crear una solicitud de devolución de dispositivos.
 */
@Getter
@Setter
public class SolicitudDevolucionRequest {

    @NotNull(message = "El empleado es obligatorio")
    private Integer empleadoId;

    @NotNull(message = "La fecha de término del empleado es obligatoria")
    private LocalDate fechaTerminoEmpleado;

    @NotNull(message = "La fecha de devolución programada es obligatoria")
    private LocalDate fechaDevolucionProgramada;

    @Size(max = 500, message = "Las observaciones deben tener máximo 500 caracteres")
    private String observaciones;

    @NotNull(message = "El usuario que solicita es obligatorio")
    private Integer usuarioSolicitaId;
}
