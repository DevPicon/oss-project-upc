package pe.edu.upc.oss.group1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.HistorialDispositivo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad HistorialDispositivo.
 * Proporciona operaciones CRUD y consultas personalizadas para el historial de dispositivos.
 */
@Repository
public interface HistorialDispositivoRepository extends JpaRepository<HistorialDispositivo, Integer> {

    /**
     * Retorna todo el historial de un dispositivo ordenado por fecha descendente.
     */
    List<HistorialDispositivo> findByDispositivoIdOrderByFechaMovimientoDesc(Integer dispositivoId);

    /**
     * Retorna el historial de un dispositivo con paginación.
     */
    Page<HistorialDispositivo> findByDispositivoIdOrderByFechaMovimientoDesc(
            Integer dispositivoId,
            Pageable pageable);

    /**
     * Retorna registros de historial de un tipo específico.
     */
    @Query("SELECT h FROM HistorialDispositivo h WHERE h.tipoMovimiento.codigo = :codigoTipo " +
           "ORDER BY h.fechaMovimiento DESC")
    List<HistorialDispositivo> findByTipoMovimientoCodigo(@Param("codigoTipo") String codigoTipo);

    /**
     * Retorna registros de historial de un usuario.
     */
    List<HistorialDispositivo> findByUsuarioIdOrderByFechaMovimientoDesc(Integer usuarioId);

    /**
     * Retorna registros de historial en un rango de fechas.
     */
    @Query("SELECT h FROM HistorialDispositivo h WHERE " +
           "h.fechaMovimiento BETWEEN :desde AND :hasta " +
           "ORDER BY h.fechaMovimiento DESC")
    List<HistorialDispositivo> findByPeriodo(
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta);

    /**
     * Retorna el historial de un dispositivo con un tipo de movimiento específico.
     */
    @Query("SELECT h FROM HistorialDispositivo h WHERE " +
           "h.dispositivo.id = :dispositivoId AND " +
           "h.tipoMovimiento.codigo = :codigoTipo " +
           "ORDER BY h.fechaMovimiento DESC")
    List<HistorialDispositivo> findByDispositivoAndTipoMovimiento(
            @Param("dispositivoId") Integer dispositivoId,
            @Param("codigoTipo") String codigoTipo);

    /**
     * Retorna un registro de historial con todas sus relaciones cargadas.
     */
    @Query("SELECT DISTINCT h FROM HistorialDispositivo h " +
           "JOIN FETCH h.dispositivo " +
           "JOIN FETCH h.tipoMovimiento " +
           "JOIN FETCH h.usuario " +
           "WHERE h.id = :id")
    Optional<HistorialDispositivo> findByIdWithRelations(@Param("id") Integer id);

    /**
     * Cuenta registros de historial por tipo de movimiento.
     */
    @Query("SELECT COUNT(h) FROM HistorialDispositivo h WHERE h.tipoMovimiento.id = :tipoId")
    Long countByTipoMovimiento(@Param("tipoId") Integer tipoId);

    /**
     * Retorna los últimos N movimientos de un dispositivo.
     */
    @Query("SELECT h FROM HistorialDispositivo h WHERE h.dispositivo.id = :dispositivoId " +
           "ORDER BY h.fechaMovimiento DESC")
    List<HistorialDispositivo> findUltimosMovimientos(
            @Param("dispositivoId") Integer dispositivoId,
            Pageable pageable);
}
