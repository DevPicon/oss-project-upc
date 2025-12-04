package pe.edu.upc.oss.group1.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO para agregar un detalle a una solicitud de devolución.
 */
@Getter
@Setter
public class DetalleDevolucionRequest {

    @NotNull(message = "La solicitud de devolución es obligatoria")
    private Integer solicitudDevolucionId;

    @NotNull(message = "El dispositivo es obligatorio")
    private Integer dispositivoId;

    @NotNull(message = "La condición de devolución es obligatoria")
    private Integer condicionDevolucionId;

    @Size(max = 500, message = "Las observaciones deben tener máximo 500 caracteres")
    private String observaciones;
}
