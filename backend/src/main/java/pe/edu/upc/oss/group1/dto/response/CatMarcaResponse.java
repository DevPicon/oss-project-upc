package pe.edu.upc.oss.group1.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO para devolver información de una marca en la API.
 *
 * Este DTO controla exactamente qué información se expone al cliente.
 * NO exponemos la entity directamente por seguridad y desacoplamiento.
 *
 * @Builder: Lombok genera patrón builder para construcción fluida.
 * Ejemplo: CatMarcaResponse.builder().id(1).codigo("DELL").build()
 */
@Getter
@Setter
@Builder
public class CatMarcaResponse {

    /**
     * ID único de la marca en la base de datos.
     */
    private Integer id;

    /**
     * Código único de la marca.
     */
    private String codigo;

    /**
     * Nombre completo de la marca.
     */
    private String nombre;

    /**
     * Indica si la marca está activa.
     */
    private Boolean activo;

    /**
     * Fecha en que se creó el registro.
     * Útil para auditoría.
     */
    private LocalDateTime fechaCreacion;
}
