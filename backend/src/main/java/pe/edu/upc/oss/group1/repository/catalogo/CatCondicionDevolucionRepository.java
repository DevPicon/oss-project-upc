package pe.edu.upc.oss.group1.repository.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.catalogo.CatCondicionDevolucion;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para el catálogo de condiciones de devolución.
 * Proporciona métodos de consulta derivados automáticamente por Spring Data JPA.
 */
@Repository
public interface CatCondicionDevolucionRepository extends JpaRepository<CatCondicionDevolucion, Integer> {

    /**
     * Busca una condición de devolución por su código único.
     * @param codigo código de la condición
     * @return Optional con la condición encontrada o vacío
     */
    Optional<CatCondicionDevolucion> findByCodigo(String codigo);

    /**
     * Verifica si existe una condición con el código dado.
     * @param codigo código a verificar
     * @return true si existe, false si no
     */
    boolean existsByCodigo(String codigo);

    /**
     * Retorna solo las condiciones activas.
     * @return lista de condiciones con activo = true
     */
    List<CatCondicionDevolucion> findByActivoTrue();

    /**
     * Busca condiciones por nombre (búsqueda parcial, insensible a mayúsculas).
     * @param nombre texto a buscar en el nombre
     * @return lista de condiciones que coinciden
     */
    List<CatCondicionDevolucion> findByNombreContainingIgnoreCase(String nombre);
}
