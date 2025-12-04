package pe.edu.upc.oss.group1.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatAreaResponse;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatEstadoEmpleadoResponse;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatPuestoResponse;
import pe.edu.upc.oss.group1.dto.catalogo.response.CatSedeResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para devolver información de un empleado en la API.
 */
@Getter
@Setter
@Builder
public class EmpleadoResponse {

    private Integer id;
    private String codigoEmpleado;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombreCompleto;
    private CatAreaResponse area;
    private CatPuestoResponse puesto;
    private CatSedeResponse sede;
    private String email;
    private String telefono;
    private LocalDate fechaIngreso;
    private CatEstadoEmpleadoResponse estadoEmpleado;
    private LocalDateTime fechaCreacion;
}
