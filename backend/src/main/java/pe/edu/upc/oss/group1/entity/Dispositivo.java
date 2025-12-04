package pe.edu.upc.oss.group1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upc.oss.group1.entity.catalogo.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * Dispositivo IT de la organización.
 * Representa activos tecnológicos que pueden ser asignados a empleados.
 */
@Entity
@Table(name = "dispositivo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "codigo_activo", length = 50, unique = true, nullable = false)
    private String codigoActivo;

    @Column(name = "numero_serie", length = 100, unique = true)
    private String numeroSerie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_dispositivo", nullable = false)
    private CatTipoDispositivo tipoDispositivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_marca", nullable = false)
    private CatMarca marca;

    @Column(name = "modelo", length = 100)
    private String modelo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado_dispositivo", nullable = false)
    private CatEstadoDispositivo estadoDispositivo;

    @Column(name = "especificaciones", columnDefinition = "TEXT")
    private String especificaciones;

    @Column(name = "fecha_adquisicion")
    private LocalDate fechaAdquisicion;

    @Column(name = "valor_adquisicion", precision = 10, scale = 2)
    private BigDecimal valorAdquisicion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor")
    private CatProveedor proveedor;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @OneToMany(mappedBy = "dispositivo")
    private List<AsignacionDispositivo> asignaciones = new ArrayList<>();

    @OneToMany(mappedBy = "dispositivoOriginal")
    private List<ReemplazoDispositivo> reemplazosComoOriginal = new ArrayList<>();

    @OneToMany(mappedBy = "dispositivoReemplazo")
    private List<ReemplazoDispositivo> reemplazosComoReemplazo = new ArrayList<>();

    @OneToMany(mappedBy = "dispositivo")
    private List<DetalleDevolucion> detallesDevolucion = new ArrayList<>();

    @OneToMany(mappedBy = "dispositivo")
    private List<HistorialDispositivo> historial = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    /**
     * Calcula la antigüedad del dispositivo en años desde su fecha de adquisición.
     */
    public Integer getAntiguedadEnAnios() {
        if (fechaAdquisicion == null) {
            return 0;
        }
        return Period.between(fechaAdquisicion, LocalDate.now()).getYears();
    }

    /**
     * Verifica si el dispositivo está disponible para ser asignado.
     */
    public boolean isDisponibleParaAsignacion() {
        return estadoDispositivo != null &&
               estadoDispositivo.getDisponibleAsignacion() != null &&
               estadoDispositivo.getDisponibleAsignacion();
    }

    /**
     * Genera una descripción corta del dispositivo para visualización.
     */
    public String getDescripcionCorta() {
        StringBuilder descripcion = new StringBuilder();
        if (marca != null) {
            descripcion.append(marca.getNombre()).append(" ");
        }
        if (modelo != null) {
            descripcion.append(modelo);
        }
        return descripcion.toString().trim();
    }
}
