package pe.edu.upc.oss.group1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upc.oss.group1.entity.catalogo.CatTipoMovimiento;

import java.time.LocalDateTime;

/**
 * Historial de movimientos y cambios de un dispositivo.
 * Proporciona auditoría completa de todas las operaciones realizadas sobre el dispositivo.
 */
@Entity
@Table(name = "historial_dispositivo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistorialDispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dispositivo", nullable = false)
    private Dispositivo dispositivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_movimiento", nullable = false)
    private CatTipoMovimiento tipoMovimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "descripcion", columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @Column(name = "datos_anteriores", columnDefinition = "TEXT")
    private String datosAnteriores;

    @Column(name = "datos_nuevos", columnDefinition = "TEXT")
    private String datosNuevos;

    @Column(name = "fecha_movimiento", nullable = false)
    private LocalDateTime fechaMovimiento;

    @PrePersist
    protected void onCreate() {
        if (fechaMovimiento == null) {
            fechaMovimiento = LocalDateTime.now();
        }
    }

    /**
     * Verifica si este registro de historial es de tipo asignación.
     */
    public boolean isAsignacion() {
        return tipoMovimiento != null && "ASIGNACION".equals(tipoMovimiento.getCodigo());
    }

    /**
     * Verifica si este registro de historial es de tipo devolución.
     */
    public boolean isDevolucion() {
        return tipoMovimiento != null && "DEVOLUCION".equals(tipoMovimiento.getCodigo());
    }

    /**
     * Verifica si este registro de historial es de tipo mantenimiento.
     */
    public boolean isMantenimiento() {
        return tipoMovimiento != null && "MANTENIMIENTO".equals(tipoMovimiento.getCodigo());
    }
}
