package pe.edu.upc.oss.group1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.DetalleDevolucion;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad DetalleDevolucion.
 * Proporciona operaciones CRUD y consultas personalizadas para detalles de devoluciones.
 */
@Repository
public interface DetalleDevolucionRepository extends JpaRepository<DetalleDevolucion, Integer> {

    /**
     * Retorna todos los detalles de una solicitud de devolución.
     */
    List<DetalleDevolucion> findBySolicitudDevolucionId(Integer solicitudId);

    /**
     * Retorna el detalle de un dispositivo en una solicitud específica.
     */
    Optional<DetalleDevolucion> findBySolicitudDevolucionIdAndDispositivoId(
            Integer solicitudId,
            Integer dispositivoId);

    /**
     * Retorna detalles con una condición específica.
     */
    @Query("SELECT d FROM DetalleDevolucion d WHERE d.condicionDevolucion.codigo = :codigoCondicion")
    List<DetalleDevolucion> findByCondicionCodigo(@Param("codigoCondicion") String codigoCondicion);

    /**
     * Retorna detalles con todas sus relaciones cargadas.
     */
    @Query("SELECT DISTINCT d FROM DetalleDevolucion d " +
           "JOIN FETCH d.solicitudDevolucion " +
           "JOIN FETCH d.dispositivo " +
           "JOIN FETCH d.asignacion " +
           "JOIN FETCH d.condicionDevolucion " +
           "WHERE d.id = :id")
    Optional<DetalleDevolucion> findByIdWithRelations(@Param("id") Integer id);

    /**
     * Cuenta detalles por condición de devolución.
     */
    @Query("SELECT COUNT(d) FROM DetalleDevolucion d WHERE d.condicionDevolucion.id = :condicionId")
    Long countByCondicionDevolucion(@Param("condicionId") Integer condicionId);

    /**
     * Verifica si un dispositivo ya está en una solicitud de devolución.
     */
    boolean existsBySolicitudDevolucionIdAndDispositivoId(Integer solicitudId, Integer dispositivoId);
}
