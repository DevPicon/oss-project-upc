package pe.edu.upc.oss.group1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.Empleado;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Empleado.
 * Proporciona operaciones CRUD y consultas personalizadas para empleados.
 */
@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

    /**
     * Busca un empleado por su código único.
     */
    Optional<Empleado> findByCodigoEmpleado(String codigoEmpleado);

    /**
     * Busca un empleado por su email único.
     */
    Optional<Empleado> findByEmail(String email);

    /**
     * Retorna todos los empleados de un área específica.
     */
    List<Empleado> findByAreaId(Integer areaId);

    /**
     * Retorna todos los empleados de una sede específica.
     */
    List<Empleado> findBySedeId(Integer sedeId);

    /**
     * Retorna todos los empleados con un estado específico.
     */
    List<Empleado> findByEstadoEmpleadoCodigo(String codigoEstado);

    /**
     * Retorna todos los empleados activos.
     */
    @Query("SELECT e FROM Empleado e WHERE e.estadoEmpleado.codigo = 'ACTIVO'")
    List<Empleado> findAllActivos();

    /**
     * Búsqueda de empleados por texto en nombre, apellidos o email con paginación.
     */
    @Query("SELECT e FROM Empleado e WHERE " +
           "LOWER(e.nombre) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.apellidoPaterno) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.apellidoMaterno) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.codigoEmpleado) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Empleado> searchEmpleados(@Param("search") String search, Pageable pageable);

    /**
     * Verifica si existe un empleado con el código dado.
     */
    boolean existsByCodigoEmpleado(String codigoEmpleado);

    /**
     * Verifica si existe un empleado con el email dado.
     */
    boolean existsByEmail(String email);

    /**
     * Retorna empleados con asignaciones cargadas (evita N+1).
     */
    @Query("SELECT DISTINCT e FROM Empleado e " +
           "LEFT JOIN FETCH e.asignaciones a " +
           "WHERE e.id = :id")
    Optional<Empleado> findByIdWithAsignaciones(@Param("id") Integer id);

    /**
     * Retorna empleados de un área con sus relaciones cargadas.
     */
    @Query("SELECT DISTINCT e FROM Empleado e " +
           "JOIN FETCH e.area " +
           "JOIN FETCH e.puesto " +
           "JOIN FETCH e.sede " +
           "JOIN FETCH e.estadoEmpleado " +
           "WHERE e.area.id = :areaId")
    List<Empleado> findByAreaIdWithRelations(@Param("areaId") Integer areaId);
}
