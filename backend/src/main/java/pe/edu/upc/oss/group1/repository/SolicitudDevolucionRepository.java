package pe.edu.upc.oss.group1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.SolicitudDevolucion;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad SolicitudDevolucion.
 * Proporciona operaciones CRUD y consultas personalizadas para solicitudes de devolución.
 */
@Repository
public interface SolicitudDevolucionRepository extends JpaRepository<SolicitudDevolucion, Integer> {

    /**
     * Retorna todas las solicitudes de un empleado.
     */
    List<SolicitudDevolucion> findByEmpleadoId(Integer empleadoId);

    /**
     * Retorna solicitudes con un estado específico.
     */
    @Query("SELECT s FROM SolicitudDevolucion s WHERE s.estadoSolicitud.codigo = :codigoEstado")
    List<SolicitudDevolucion> findByEstadoCodigo(@Param("codigoEstado") String codigoEstado);

    /**
     * Retorna todas las solicitudes pendientes.
     */
    @Query("SELECT s FROM SolicitudDevolucion s WHERE s.estadoSolicitud.codigo = 'PENDIENTE'")
    List<SolicitudDevolucion> findSolicitudesPendientes();

    /**
     * Retorna todas las solicitudes pendientes con paginación.
     */
    @Query("SELECT s FROM SolicitudDevolucion s WHERE s.estadoSolicitud.codigo = 'PENDIENTE'")
    Page<SolicitudDevolucion> findSolicitudesPendientes(Pageable pageable);

    /**
     * Retorna solicitudes atrasadas (fecha programada pasada y sin devolución real).
     */
    @Query("SELECT s FROM SolicitudDevolucion s WHERE " +
           "s.fechaDevolucionReal IS NULL AND " +
           "s.fechaDevolucionProgramada < CURRENT_DATE")
    List<SolicitudDevolucion> findSolicitudesAtrasadas();

    /**
     * Retorna solicitudes en un rango de fechas de solicitud.
     */
    @Query("SELECT s FROM SolicitudDevolucion s WHERE " +
           "s.fechaSolicitud BETWEEN :desde AND :hasta")
    List<SolicitudDevolucion> findSolicitudesByPeriodo(
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta);

    /**
     * Retorna una solicitud con todas sus relaciones cargadas.
     */
    @Query("SELECT DISTINCT s FROM SolicitudDevolucion s " +
           "JOIN FETCH s.empleado " +
           "JOIN FETCH s.estadoSolicitud " +
           "JOIN FETCH s.usuarioSolicita " +
           "LEFT JOIN FETCH s.usuarioRecibe " +
           "WHERE s.id = :id")
    Optional<SolicitudDevolucion> findByIdWithRelations(@Param("id") Integer id);

    /**
     * Retorna una solicitud con sus detalles cargados.
     */
    @Query("SELECT DISTINCT s FROM SolicitudDevolucion s " +
           "LEFT JOIN FETCH s.detalles d " +
           "WHERE s.id = :id")
    Optional<SolicitudDevolucion> findByIdWithDetalles(@Param("id") Integer id);

    /**
     * Retorna solicitudes de un usuario específico.
     */
    List<SolicitudDevolucion> findByUsuarioSolicitaId(Integer usuarioId);

    /**
     * Cuenta solicitudes por estado.
     */
    @Query("SELECT COUNT(s) FROM SolicitudDevolucion s WHERE s.estadoSolicitud.id = :estadoId")
    Long countByEstadoSolicitud(@Param("estadoId") Integer estadoId);
}
