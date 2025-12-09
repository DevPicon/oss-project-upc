package pe.edu.upc.oss.group1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upc.oss.group1.entity.catalogo.CatArea;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoEmpleado;
import pe.edu.upc.oss.group1.entity.catalogo.CatPuesto;
import pe.edu.upc.oss.group1.entity.catalogo.CatSede;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Empleado de la organización.
 * Representa a las personas que pueden tener dispositivos asignados.
 */
@Entity
@Table(name = "empleado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "codigo_empleado", length = 20, unique = true, nullable = false)
    private String codigoEmpleado;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "apellido_paterno", length = 100, nullable = false)
    private String apellidoPaterno;

    @Column(name = "apellido_materno", length = 100)
    private String apellidoMaterno;

    @Column(name = "email", length = 100, unique = true, nullable = false)
    private String email;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_area", nullable = false)
    private CatArea area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_puesto", nullable = false)
    private CatPuesto puesto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sede", nullable = false)
    private CatSede sede;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    @Column(name = "fecha_termino")
    private LocalDate fechaTermino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado_empleado", nullable = false)
    private CatEstadoEmpleado estadoEmpleado;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @OneToMany(mappedBy = "empleado")
    private List<AsignacionDispositivo> asignaciones = new ArrayList<>();

    @OneToMany(mappedBy = "empleado")
    private List<ReemplazoDispositivo> reemplazos = new ArrayList<>();

    @OneToMany(mappedBy = "empleado")
    private List<SolicitudDevolucion> solicitudesDevolucion = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    /**
     * Retorna el nombre completo del empleado concatenando nombre y apellidos.
     */
    public String getNombreCompleto() {
        StringBuilder nombreCompleto = new StringBuilder();
        if (nombre != null) {
            nombreCompleto.append(nombre);
        }
        if (apellidoPaterno != null) {
            nombreCompleto.append(" ").append(apellidoPaterno);
        }
        if (apellidoMaterno != null && !apellidoMaterno.isBlank()) {
            nombreCompleto.append(" ").append(apellidoMaterno);
        }
        return nombreCompleto.toString().trim();
    }

    /**
     * Verifica si el empleado está activo según su estado.
     */
    public boolean isActivo() {
        return estadoEmpleado != null && "ACTIVO".equals(estadoEmpleado.getCodigo());
    }

    /**
     * Verifica si el empleado está cesado o ha terminado su relación laboral.
     */
    public boolean isCesado() {
        return fechaTermino != null && !fechaTermino.isAfter(LocalDate.now());
    }
}
