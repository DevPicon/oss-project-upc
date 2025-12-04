package pe.edu.upc.oss.group1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.ReemplazoDispositivo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad ReemplazoDispositivo.
 * Proporciona operaciones CRUD y consultas personalizadas para reemplazos de dispositivos.
 */
@Repository
public interface ReemplazoDispositivoRepository extends JpaRepository<ReemplazoDispositivo, Integer> {

    /**
     * Retorna todos los reemplazos de un dispositivo original.
     */
    List<ReemplazoDispositivo> findByDispositivoOriginalId(Integer dispositivoId);

    /**
     * Retorna todos los reemplazos donde se usó un dispositivo como reemplazo.
     */
    List<ReemplazoDispositivo> findByDispositivoReemplazoId(Integer dispositivoId);

    /**
     * Retorna todos los reemplazos de un empleado.
     */
    List<ReemplazoDispositivo> findByEmpleadoId(Integer empleadoId);

    /**
     * Retorna todos los reemplazos de una asignación.
     */
    List<ReemplazoDispositivo> findByAsignacionOriginalId(Integer asignacionId);

    /**
     * Retorna reemplazos con un estado específico.
     */
    @Query("SELECT r FROM ReemplazoDispositivo r WHERE r.estadoReemplazo.codigo = :codigoEstado")
    List<ReemplazoDispositivo> findByEstadoCodigo(@Param("codigoEstado") String codigoEstado);

    /**
     * Retorna todos los reemplazos pendientes.
     */
    @Query("SELECT r FROM ReemplazoDispositivo r WHERE r.estadoReemplazo.codigo = 'PENDIENTE'")
    List<ReemplazoDispositivo> findReemplazosPendientes();

    /**
     * Retorna reemplazos en un rango de fechas.
     */
    @Query("SELECT r FROM ReemplazoDispositivo r WHERE " +
           "r.fechaReemplazo BETWEEN :desde AND :hasta")
    List<ReemplazoDispositivo> findReemplazosByPeriodo(
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta);

    /**
     * Retorna un reemplazo con todas sus relaciones cargadas.
     */
    @Query("SELECT DISTINCT r FROM ReemplazoDispositivo r " +
           "JOIN FETCH r.dispositivoOriginal " +
           "JOIN FETCH r.dispositivoReemplazo " +
           "JOIN FETCH r.empleado " +
           "JOIN FETCH r.asignacionOriginal " +
           "JOIN FETCH r.motivoReemplazo " +
           "JOIN FETCH r.estadoReemplazo " +
           "JOIN FETCH r.usuarioRegistra " +
           "WHERE r.id = :id")
    Optional<ReemplazoDispositivo> findByIdWithRelations(@Param("id") Integer id);

    /**
     * Cuenta reemplazos por motivo.
     */
    @Query("SELECT COUNT(r) FROM ReemplazoDispositivo r WHERE r.motivoReemplazo.id = :motivoId")
    Long countByMotivoReemplazo(@Param("motivoId") Integer motivoId);
}
