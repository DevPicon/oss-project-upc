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
 * Catálogo de tipos de dispositivo.
 * Ejemplos: LAPTOP, DESKTOP, MONITOR, TECLADO, MOUSE
 */
@Entity
@Table(name = "cat_tipo_dispositivo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CatTipoDispositivo extends BaseEntity {

    @Column(name = "codigo", length = 20, unique = true, nullable = false)
    private String codigo;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "requiere_serie", nullable = false)
    private Boolean requiereSerie = true;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    public boolean isActivo() {
        return Boolean.TRUE.equals(activo);
    }

    public boolean requiereSerie() {
        return Boolean.TRUE.equals(requiereSerie);
    }
}
