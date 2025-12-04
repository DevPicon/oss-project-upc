package pe.edu.upc.oss.group1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Clase base para todas las entities del sistema.
 * Proporciona campos comunes como id y fecha_creacion.
 *
 * Al usar @MappedSuperclass, esta clase no se mapea a una tabla,
 * sino que sus campos se incluyen en las tablas de las clases hijas.
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    /**
     * Este método se ejecuta automáticamente antes de persistir la entity.
     * Establece la fecha de creación al momento actual.
     */
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}
