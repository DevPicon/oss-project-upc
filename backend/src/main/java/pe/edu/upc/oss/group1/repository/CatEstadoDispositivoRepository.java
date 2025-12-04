package pe.edu.upc.oss.group1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoDispositivo;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para el catálogo de estados de dispositivo.
 * Incluye métodos para filtrar estados disponibles para asignación.
 */
@Repository
public interface CatEstadoDispositivoRepository extends JpaRepository<CatEstadoDispositivo, Integer> {

    /**
     * Busca un estado de dispositivo por su código único.
     * @param codigo código del estado
     * @return Optional con el estado encontrado o vacío
     */
    Optional<CatEstadoDispositivo> findByCodigo(String codigo);

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
    List<CatEstadoDispositivo> findByActivoTrue();

    /**
     * Busca estados por nombre (búsqueda parcial, insensible a mayúsculas).
     * @param nombre texto a buscar en el nombre
     * @return lista de estados que coinciden
     */
    List<CatEstadoDispositivo> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca estados disponibles para asignación.
     * @return lista de estados con disponibleAsignacion = true
     */
    List<CatEstadoDispositivo> findByDisponibleAsignacionTrue();
}
