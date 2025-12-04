package pe.edu.upc.oss.group1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upc.oss.group1.entity.catalogo.CatCondicionDevolucion;

import java.time.LocalDateTime;

/**
 * Detalle de un dispositivo específico en una solicitud de devolución.
 * Registra la condición en que fue devuelto el dispositivo.
 */
@Entity
@Table(name = "detalle_devolucion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleDevolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud_devolucion", nullable = false)
    private SolicitudDevolucion solicitudDevolucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dispositivo", nullable = false)
    private Dispositivo dispositivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_asignacion", nullable = false)
    private AsignacionDispositivo asignacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_condicion_devolucion", nullable = false)
    private CatCondicionDevolucion condicionDevolucion;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    /**
     * Verifica si el dispositivo fue devuelto en buenas condiciones.
     */
    public boolean isCondicionBuena() {
        return condicionDevolucion != null && "BUENO".equals(condicionDevolucion.getCodigo());
    }

    /**
     * Verifica si el dispositivo fue devuelto dañado.
     */
    public boolean isCondicionDanada() {
        return condicionDevolucion != null &&
               (condicionDevolucion.getCodigo().contains("DANADO") ||
                condicionDevolucion.getCodigo().contains("MALO"));
    }
}
