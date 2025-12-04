package pe.edu.upc.oss.group1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoReemplazo;
import pe.edu.upc.oss.group1.entity.catalogo.CatMotivoReemplazo;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Reemplazo de un dispositivo asignado por otro.
 * Ocurre cuando un dispositivo falla o requiere actualización mientras está asignado.
 */
@Entity
@Table(name = "reemplazo_dispositivo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReemplazoDispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_asignacion_original", nullable = false)
    private AsignacionDispositivo asignacionOriginal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dispositivo_original", nullable = false)
    private Dispositivo dispositivoOriginal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dispositivo_reemplazo", nullable = false)
    private Dispositivo dispositivoReemplazo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado", nullable = false)
    private Empleado empleado;

    @Column(name = "fecha_reemplazo", nullable = false)
    private LocalDate fechaReemplazo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_motivo_reemplazo", nullable = false)
    private CatMotivoReemplazo motivoReemplazo;

    @Column(name = "descripcion_motivo", columnDefinition = "TEXT")
    private String descripcionMotivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_registra", nullable = false)
    private Usuario usuarioRegistra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado_reemplazo", nullable = false)
    private CatEstadoReemplazo estadoReemplazo;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (fechaReemplazo == null) {
            fechaReemplazo = LocalDate.now();
        }
    }

    /**
     * Verifica si el reemplazo está completado.
     */
    public boolean isCompletado() {
        return estadoReemplazo != null && "COMPLETADO".equals(estadoReemplazo.getCodigo());
    }

    /**
     * Verifica si el reemplazo está pendiente.
     */
    public boolean isPendiente() {
        return estadoReemplazo != null && "PENDIENTE".equals(estadoReemplazo.getCodigo());
    }
}
