package pe.edu.upc.oss.group1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.Dispositivo;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Dispositivo.
 * Proporciona operaciones CRUD y consultas personalizadas para dispositivos IT.
 */
@Repository
public interface DispositivoRepository extends JpaRepository<Dispositivo, Integer> {

    /**
     * Busca un dispositivo por su código de activo único.
     */
    Optional<Dispositivo> findByCodigoActivo(String codigoActivo);

    /**
     * Busca un dispositivo por su número de serie único.
     */
    Optional<Dispositivo> findByNumeroSerie(String numeroSerie);

    /**
     * Retorna todos los dispositivos de un tipo específico.
     */
    List<Dispositivo> findByTipoDispositivoId(Integer tipoId);

    /**
     * Retorna todos los dispositivos de una marca específica.
     */
    List<Dispositivo> findByMarcaId(Integer marcaId);

    /**
     * Retorna todos los dispositivos con un estado específico.
     */
    List<Dispositivo> findByEstadoDispositivoCodigo(String codigoEstado);

    /**
     * Retorna dispositivos disponibles para asignación.
     */
    @Query("SELECT d FROM Dispositivo d WHERE d.estadoDispositivo.disponibleAsignacion = true")
    List<Dispositivo> findDispositivosDisponibles();

    /**
     * Retorna dispositivos disponibles para asignación con paginación.
     */
    @Query("SELECT d FROM Dispositivo d WHERE d.estadoDispositivo.disponibleAsignacion = true")
    Page<Dispositivo> findDispositivosDisponibles(Pageable pageable);

    /**
     * Búsqueda de dispositivos por texto en código, serie, modelo.
     */
    @Query("SELECT d FROM Dispositivo d WHERE " +
           "LOWER(d.codigoActivo) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(d.numeroSerie) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(d.modelo) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Dispositivo> searchDispositivos(@Param("search") String search, Pageable pageable);

    /**
     * Verifica si existe un dispositivo con el código de activo dado.
     */
    boolean existsByCodigoActivo(String codigoActivo);

    /**
     * Verifica si existe un dispositivo con el número de serie dado.
     */
    boolean existsByNumeroSerie(String numeroSerie);

    /**
     * Retorna un dispositivo con sus asignaciones cargadas (evita N+1).
     */
    @Query("SELECT DISTINCT d FROM Dispositivo d " +
           "LEFT JOIN FETCH d.asignaciones a " +
           "WHERE d.id = :id")
    Optional<Dispositivo> findByIdWithAsignaciones(@Param("id") Integer id);

    /**
     * Retorna un dispositivo con su historial cargado.
     */
    @Query("SELECT DISTINCT d FROM Dispositivo d " +
           "LEFT JOIN FETCH d.historial h " +
           "WHERE d.id = :id")
    Optional<Dispositivo> findByIdWithHistorial(@Param("id") Integer id);

    /**
     * Retorna un dispositivo con todas sus relaciones cargadas.
     */
    @Query("SELECT DISTINCT d FROM Dispositivo d " +
           "JOIN FETCH d.tipoDispositivo " +
           "JOIN FETCH d.marca " +
           "JOIN FETCH d.estadoDispositivo " +
           "LEFT JOIN FETCH d.proveedor " +
           "WHERE d.id = :id")
    Optional<Dispositivo> findByIdWithRelations(@Param("id") Integer id);

    /**
     * Retorna dispositivos por tipo con relaciones cargadas.
     */
    @Query("SELECT DISTINCT d FROM Dispositivo d " +
           "JOIN FETCH d.tipoDispositivo " +
           "JOIN FETCH d.marca " +
           "JOIN FETCH d.estadoDispositivo " +
           "WHERE d.tipoDispositivo.id = :tipoId")
    List<Dispositivo> findByTipoDispositivoIdWithRelations(@Param("tipoId") Integer tipoId);
}
