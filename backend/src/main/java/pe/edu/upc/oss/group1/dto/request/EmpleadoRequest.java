package pe.edu.upc.oss.group1.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO para crear o actualizar un empleado.
 */
@Getter
@Setter
public class EmpleadoRequest {

    @NotBlank(message = "El código de empleado es obligatorio")
    @Size(max = 20, message = "El código debe tener máximo 20 caracteres")
    private String codigoEmpleado;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre debe tener máximo 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(max = 100, message = "El apellido paterno debe tener máximo 100 caracteres")
    private String apellidoPaterno;

    @Size(max = 100, message = "El apellido materno debe tener máximo 100 caracteres")
    private String apellidoMaterno;

    @NotNull(message = "El área es obligatoria")
    private Integer areaId;

    @NotNull(message = "El puesto es obligatorio")
    private Integer puestoId;

    @NotNull(message = "La sede es obligatoria")
    private Integer sedeId;

    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email debe tener máximo 100 caracteres")
    private String email;

    @Size(max = 20, message = "El teléfono debe tener máximo 20 caracteres")
    private String telefono;

    private LocalDate fechaIngreso;

    @NotNull(message = "El estado del empleado es obligatorio")
    private Integer estadoEmpleadoId;
}
