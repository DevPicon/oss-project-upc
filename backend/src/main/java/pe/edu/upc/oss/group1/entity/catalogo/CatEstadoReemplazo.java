package pe.edu.upc.oss.group1.entity.catalogo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upc.oss.group1.entity.BaseEntity;

/**
 * Catálogo de estados del proceso de reemplazo.
 * Ejemplos: PENDIENTE, APROBADO, EN_PROCESO, COMPLETADO, RECHAZADO
 */
@Entity
@Table(name = "cat_estado_reemplazo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CatEstadoReemplazo extends BaseEntity {

    @Column(name = "codigo", length = 20, unique = true, nullable = false)
    private String codigo;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    public boolean isActivo() {
        return Boolean.TRUE.equals(activo);
    }
}
