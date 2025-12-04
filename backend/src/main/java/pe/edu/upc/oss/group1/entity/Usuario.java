package pe.edu.upc.oss.group1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Usuario del sistema con credenciales de acceso.
 * Usuarios pueden realizar asignaciones y recibir devoluciones de dispositivos.
 */
@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", length = 50, unique = true, nullable = false)
    private String username;

    @Column(name = "password_hash", length = 255, nullable = false)
    private String passwordHash;

    @Column(name = "email", length = 100, unique = true, nullable = false)
    private String email;

    @Column(name = "nombre_completo", length = 200, nullable = false)
    private String nombreCompleto;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @OneToMany(mappedBy = "usuarioAsigna")
    private List<AsignacionDispositivo> asignacionesRealizadas = new ArrayList<>();

    @OneToMany(mappedBy = "usuarioRecibe")
    private List<AsignacionDispositivo> devolucionesRecibidas = new ArrayList<>();

    @OneToMany(mappedBy = "usuarioRegistra")
    private List<ReemplazoDispositivo> reemplazosRegistrados = new ArrayList<>();

    @OneToMany(mappedBy = "usuarioSolicita")
    private List<SolicitudDevolucion> solicitudesRealizadas = new ArrayList<>();

    @OneToMany(mappedBy = "usuarioRecibe")
    private List<SolicitudDevolucion> solicitudesRecibidas = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    private List<HistorialDispositivo> registrosHistorial = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    public boolean isActivo() {
        return Boolean.TRUE.equals(activo);
    }
}
