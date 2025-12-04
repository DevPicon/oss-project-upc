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
 * Entity que representa el catálogo de marcas de dispositivos.
 * Ejemplos: Dell, HP, Lenovo, Apple, etc.
 *
 * Esta es una tabla de catálogo simple sin relaciones complejas,
 * ideal para empezar el desarrollo de la Fase 2.
 */
@Entity
@Table(name = "cat_marca")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CatMarca extends BaseEntity {

    /**
     * Código único de la marca (ej: "DELL", "HP")
     * Se usa para búsquedas y referencias en código.
     */
    @Column(name = "codigo", length = 20, unique = true, nullable = false)
    private String codigo;

    /**
     * Nombre completo de la marca (ej: "Dell", "Hewlett-Packard")
     */
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    /**
     * Indica si la marca está activa en el sistema.
     * Las marcas inactivas no se muestran en formularios pero se mantienen
     * por integridad referencial con dispositivos existentes.
     */
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    /**
     * Método de utilidad para verificar si la marca está activa.
     * Evita NullPointerException si activo es null.
     */
    public boolean isActivo() {
        return Boolean.TRUE.equals(activo);
    }
}
