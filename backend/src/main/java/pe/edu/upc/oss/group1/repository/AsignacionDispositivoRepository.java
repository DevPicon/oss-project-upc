package pe.edu.upc.oss.group1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.AsignacionDispositivo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad AsignacionDispositivo.
 * Proporciona operaciones CRUD y consultas personalizadas para asignaciones de dispositivos.
 */
@Repository
public interface AsignacionDispositivoRepository extends JpaRepository<AsignacionDispositivo, Integer> {

    /**
     * Retorna todas las asignaciones de un empleado.
     */
    List<AsignacionDispositivo> findByEmpleadoId(Integer empleadoId);

    /**
     * Retorna todas las asignaciones de un dispositivo.
     */
    List<AsignacionDispositivo> findByDispositivoId(Integer dispositivoId);

    /**
     * Retorna asignaciones activas de un empleado.
     */
    @Query("SELECT a FROM AsignacionDispositivo a WHERE " +
           "a.empleado.id = :empleadoId AND " +
           "a.estadoAsignacion.codigo = 'ACTIVA'")
    List<AsignacionDispositivo> findAsignacionesActivasByEmpleado(@Param("empleadoId") Integer empleadoId);

    /**
     * Retorna la asignación activa de un dispositivo específico.
     */
    @Query("SELECT a FROM AsignacionDispositivo a WHERE " +
           "a.dispositivo.id = :dispositivoId AND " +
           "a.estadoAsignacion.codigo = 'ACTIVA'")
    Optional<AsignacionDispositivo> findAsignacionActivaByDispositivo(@Param("dispositivoId") Integer dispositivoId);

    /**
     * Retorna todas las asignaciones activas del sistema.
     */
    @Query("SELECT a FROM AsignacionDispositivo a WHERE a.estadoAsignacion.codigo = 'ACTIVA'")
    List<AsignacionDispositivo> findAllAsignacionesActivas();

    /**
     * Retorna todas las asignaciones activas con paginación.
     */
    @Query("SELECT a FROM AsignacionDispositivo a WHERE a.estadoAsignacion.codigo = 'ACTIVA'")
    Page<AsignacionDispositivo> findAllAsignacionesActivas(Pageable pageable);

    /**
     * Retorna asignaciones en un rango de fechas.
     */
    @Query("SELECT a FROM AsignacionDispositivo a WHERE " +
           "a.fechaAsignacion BETWEEN :desde AND :hasta")
    List<AsignacionDispositivo> findAsignacionesByPeriodo(
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta);

    /**
     * Retorna asignaciones devueltas en un rango de fechas.
     */
    @Query("SELECT a FROM AsignacionDispositivo a WHERE " +
           "a.fechaDevolucion BETWEEN :desde AND :hasta")
    List<AsignacionDispositivo> findDevolucionesByPeriodo(
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta);

    /**
     * Retorna una asignación con todas sus relaciones cargadas.
     */
    @Query("SELECT DISTINCT a FROM AsignacionDispositivo a " +
           "JOIN FETCH a.dispositivo d " +
           "JOIN FETCH a.empleado e " +
           "JOIN FETCH a.estadoAsignacion " +
           "JOIN FETCH a.usuarioAsigna " +
           "LEFT JOIN FETCH a.usuarioRecibe " +
           "WHERE a.id = :id")
    Optional<AsignacionDispositivo> findByIdWithRelations(@Param("id") Integer id);

    /**
     * Cuenta el número de dispositivos asignados a un empleado.
     */
    @Query("SELECT COUNT(a) FROM AsignacionDispositivo a WHERE " +
           "a.empleado.id = :empleadoId AND a.estadoAsignacion.codigo = 'ACTIVA'")
    Long countDispositivosAsignadosByEmpleado(@Param("empleadoId") Integer empleadoId);

    /**
     * Retorna asignaciones con reemplazos.
     */
    @Query("SELECT DISTINCT a FROM AsignacionDispositivo a " +
           "LEFT JOIN FETCH a.reemplazos r " +
           "WHERE a.id = :id")
    Optional<AsignacionDispositivo> findByIdWithReemplazos(@Param("id") Integer id);
}
