package pe.edu.upc.oss.group1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.catalogo.CatTipoMovimiento;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para el catálogo de tipos de movimiento.
 * Proporciona métodos de consulta derivados automáticamente por Spring Data JPA.
 */
@Repository
public interface CatTipoMovimientoRepository extends JpaRepository<CatTipoMovimiento, Integer> {

    /**
     * Busca un tipo de movimiento por su código único.
     * @param codigo código del tipo de movimiento
     * @return Optional con el tipo encontrado o vacío
     */
    Optional<CatTipoMovimiento> findByCodigo(String codigo);

    /**
     * Verifica si existe un tipo con el código dado.
     * @param codigo código a verificar
     * @return true si existe, false si no
     */
    boolean existsByCodigo(String codigo);

    /**
     * Retorna solo los tipos activos.
     * @return lista de tipos con activo = true
     */
    List<CatTipoMovimiento> findByActivoTrue();

    /**
     * Busca tipos por nombre (búsqueda parcial, insensible a mayúsculas).
     * @param nombre texto a buscar en el nombre
     * @return lista de tipos que coinciden
     */
    List<CatTipoMovimiento> findByNombreContainingIgnoreCase(String nombre);
}
