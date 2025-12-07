package pe.edu.upc.oss.group1.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO para actualización parcial del estado de un dispositivo.
 */
@Getter
@Setter
public class DispositivoEstadoRequest {

    @NotNull(message = "El ID del estado es obligatorio")
    private Integer estadoId;

    private String observacion;
}
