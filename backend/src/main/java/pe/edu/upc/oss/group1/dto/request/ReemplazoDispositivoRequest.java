package pe.edu.upc.oss.group1.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO para crear una solicitud de reemplazo de dispositivo.
 */
@Getter
@Setter
public class ReemplazoDispositivoRequest {

    @NotNull(message = "La asignación original es obligatoria")
    private Integer asignacionOriginalId;

    @NotNull(message = "El dispositivo de reemplazo es obligatorio")
    private Integer dispositivoReemplazoId;

    @NotNull(message = "El motivo de reemplazo es obligatorio")
    private Integer motivoReemplazoId;

    private LocalDate fechaReemplazo;

    @Size(max = 500, message = "La descripción debe tener máximo 500 caracteres")
    private String descripcionMotivo;

    @NotNull(message = "El usuario que registra es obligatorio")
    private Integer usuarioRegistraId;
}
