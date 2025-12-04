package pe.edu.upc.oss.group1.repository.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoReemplazo;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para el catálogo de estados de reemplazo.
 * Proporciona métodos de consulta derivados automáticamente por Spring Data JPA.
 */
@Repository
public interface CatEstadoReemplazoRepository extends JpaRepository<CatEstadoReemplazo, Integer> {

    /**
     * Busca un estado de reemplazo por su código único.
     * @param codigo código del estado
     * @return Optional con el estado encontrado o vacío
     */
    Optional<CatEstadoReemplazo> findByCodigo(String codigo);

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
    List<CatEstadoReemplazo> findByActivoTrue();

    /**
     * Busca estados por nombre (búsqueda parcial, insensible a mayúsculas).
     * @param nombre texto a buscar en el nombre
     * @return lista de estados que coinciden
     */
    List<CatEstadoReemplazo> findByNombreContainingIgnoreCase(String nombre);
}
