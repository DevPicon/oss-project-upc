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
 * Catálogo de proveedores de dispositivos.
 */
@Entity
@Table(name = "cat_proveedor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CatProveedor extends BaseEntity {

    @Column(name = "codigo", length = 20, unique = true, nullable = false)
    private String codigo;

    @Column(name = "razon_social", length = 200, nullable = false)
    private String razonSocial;

    @Column(name = "nombre_comercial", length = 200)
    private String nombreComercial;

    @Column(name = "ruc", length = 11)
    private String ruc;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    public boolean isActivo() {
        return Boolean.TRUE.equals(activo);
    }
}
