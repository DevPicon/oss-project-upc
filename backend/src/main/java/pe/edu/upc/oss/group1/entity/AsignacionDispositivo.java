package pe.edu.upc.oss.group1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoAsignacion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Asignación de un dispositivo a un empleado.
 * Registra quién asignó el dispositivo, cuándo, y cuándo fue devuelto.
 */
@Entity
@Table(name = "asignacion_dispositivo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AsignacionDispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dispositivo", nullable = false)
    private Dispositivo dispositivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado", nullable = false)
    private Empleado empleado;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDate fechaAsignacion;

    @Column(name = "fecha_devolucion")
    private LocalDate fechaDevolucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_asigna", nullable = false)
    private Usuario usuarioAsigna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_recibe")
    private Usuario usuarioRecibe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado_asignacion", nullable = false)
    private CatEstadoAsignacion estadoAsignacion;

    @Column(name = "observaciones_asignacion", columnDefinition = "TEXT")
    private String observacionesAsignacion;

    @Column(name = "observaciones_devolucion", columnDefinition = "TEXT")
    private String observacionesDevolucion;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @OneToMany(mappedBy = "asignacionOriginal")
    private List<ReemplazoDispositivo> reemplazos = new ArrayList<>();

    @OneToMany(mappedBy = "asignacion")
    private List<DetalleDevolucion> detallesDevolucion = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (fechaAsignacion == null) {
            fechaAsignacion = LocalDate.now();
        }
    }

    /**
     * Verifica si la asignación está actualmente activa.
     */
    public boolean isActiva() {
        return estadoAsignacion != null && "ACTIVA".equals(estadoAsignacion.getCodigo());
    }

    /**
     * Verifica si la asignación ya fue devuelta.
     */
    public boolean isDevuelta() {
        return fechaDevolucion != null;
    }

    /**
     * Calcula el número de días que el dispositivo ha estado o estuvo asignado.
     */
    public Integer getDiasAsignado() {
        if (fechaAsignacion == null) {
            return 0;
        }
        LocalDate fechaFin = fechaDevolucion != null ? fechaDevolucion : LocalDate.now();
        return (int) ChronoUnit.DAYS.between(fechaAsignacion, fechaFin);
    }

    /**
     * Verifica si la asignación ha excedido un número determinado de días.
     */
    public boolean haExcedidoDias(int dias) {
        return getDiasAsignado() > dias;
    }
}
