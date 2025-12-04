package pe.edu.upc.oss.group1.entity.catalogo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upc.oss.group1.entity.BaseEntity;

/**
 * Catálogo de puestos de trabajo vinculados a áreas.
 * Un puesto pertenece a un área específica.
 * Ejemplo: "Desarrollador Senior" pertenece al área "TI-Desarrollo"
 */
@Entity
@Table(name = "cat_puesto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CatPuesto extends BaseEntity {

    @Column(name = "codigo", length = 20, unique = true, nullable = false)
    private String codigo;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    /**
     * Área a la que pertenece este puesto.
     * LAZY para no cargar el área automáticamente si no se necesita.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_area")
    private CatArea area;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    public boolean isActivo() {
        return Boolean.TRUE.equals(activo);
    }
}
