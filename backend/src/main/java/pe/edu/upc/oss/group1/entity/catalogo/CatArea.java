package pe.edu.upc.oss.group1.entity.catalogo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upc.oss.group1.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Catálogo de áreas organizacionales con estructura jerárquica.
 * Permite modelar áreas padre e hijas (ejemplo: TI > TI-Desarrollo > TI-Dev-Backend).
 *
 * La auto-relación permite crear árboles organizacionales de cualquier profundidad.
 */
@Entity
@Table(name = "cat_area")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CatArea extends BaseEntity {

    @Column(name = "codigo", length = 20, unique = true, nullable = false)
    private String codigo;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    /**
     * Auto-relación: Área superior/padre en la jerarquía.
     * LAZY para evitar cargar toda la jerarquía automáticamente.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_area_superior")
    private CatArea areaSuperior;

    /**
     * Relación inversa: Sub-áreas que pertenecen a esta área.
     * mappedBy indica que areaSuperior es el dueño de la relación.
     */
    @OneToMany(mappedBy = "areaSuperior")
    private List<CatArea> subAreas = new ArrayList<>();

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    public boolean isActivo() {
        return Boolean.TRUE.equals(activo);
    }

    /**
     * Verifica si esta área es raíz (no tiene área superior).
     */
    public boolean isAreaRaiz() {
        return areaSuperior == null;
    }

    /**
     * Verifica si esta área tiene sub-áreas.
     */
    public boolean tieneSubAreas() {
        return subAreas != null && !subAreas.isEmpty();
    }
}
