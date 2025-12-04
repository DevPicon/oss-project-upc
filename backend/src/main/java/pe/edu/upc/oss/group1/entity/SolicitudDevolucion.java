package pe.edu.upc.oss.group1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoSolicitud;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Solicitud de devolución de dispositivos asignados a un empleado.
 * Típicamente se genera cuando un empleado cesa o cambia de área.
 */
@Entity
@Table(name = "solicitud_devolucion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDevolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado", nullable = false)
    private Empleado empleado;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDate fechaSolicitud;

    @Column(name = "fecha_termino_empleado", nullable = false)
    private LocalDate fechaTerminoEmpleado;

    @Column(name = "fecha_devolucion_programada", nullable = false)
    private LocalDate fechaDevolucionProgramada;

    @Column(name = "fecha_devolucion_real")
    private LocalDate fechaDevolucionReal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado_solicitud", nullable = false)
    private CatEstadoSolicitud estadoSolicitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_solicita", nullable = false)
    private Usuario usuarioSolicita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_recibe")
    private Usuario usuarioRecibe;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @OneToMany(mappedBy = "solicitudDevolucion")
    private List<DetalleDevolucion> detalles = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (fechaSolicitud == null) {
            fechaSolicitud = LocalDate.now();
        }
    }

    /**
     * Verifica si la solicitud está pendiente de atención.
     */
    public boolean isPendiente() {
        return estadoSolicitud != null && "PENDIENTE".equals(estadoSolicitud.getCodigo());
    }

    /**
     * Verifica si la solicitud fue completada.
     */
    public boolean isCompletada() {
        return estadoSolicitud != null && "COMPLETADA".equals(estadoSolicitud.getCodigo());
    }

    /**
     * Verifica si la devolución está atrasada comparando con la fecha programada.
     */
    public boolean isAtrasada() {
        if (fechaDevolucionReal != null) {
            return false;
        }
        return LocalDate.now().isAfter(fechaDevolucionProgramada);
    }

    /**
     * Retorna el número de dispositivos en esta solicitud.
     */
    public int getCantidadDispositivos() {
        return detalles != null ? detalles.size() : 0;
    }
}
