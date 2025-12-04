package pe.edu.upc.oss.group1.repository.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoAsignacion;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para el catálogo de estados de asignación.
 * Proporciona métodos de consulta derivados automáticamente por Spring Data JPA.
 */
@Repository
public interface CatEstadoAsignacionRepository extends JpaRepository<CatEstadoAsignacion, Integer> {

    /**
     * Busca un estado de asignación por su código único.
     * @param codigo código del estado
     * @return Optional con el estado encontrado o vacío
     */
    Optional<CatEstadoAsignacion> findByCodigo(String codigo);

    /**
     * Verifica si existe un estado con el código dado.
     * @param codigo código a verificar
     * @return true si existe, false si no
     */
    boolean existsByCodigo(String codigo);

    /**
     * Retorna solo los estados activos.
     * @return lista de estados con activo = true
     */
    List<CatEstadoAsignacion> findByActivoTrue();

    /**
     * Busca estados por nombre (búsqueda parcial, insensible a mayúsculas).
     * @param nombre texto a buscar en el nombre
     * @return lista de estados que coinciden
     */
    List<CatEstadoAsignacion> findByNombreContainingIgnoreCase(String nombre);
}
