package pe.edu.upc.oss.group1.repository.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.catalogo.CatMarca;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operaciones de base de datos sobre CatMarca.
 *
 * Spring Data JPA genera automáticamente la implementación de:
 * - findAll(), findById(), save(), deleteById(), etc.
 *
 * Métodos custom se declaran aquí y Spring genera la query automáticamente
 * basándose en el nombre del método (Query Derivation).
 */
@Repository
public interface CatMarcaRepository extends JpaRepository<CatMarca, Integer> {

    /**
     * Busca una marca por su código único.
     * Útil para validaciones de duplicados.
     *
     * Query generada: SELECT * FROM cat_marca WHERE codigo = ?
     */
    Optional<CatMarca> findByCodigo(String codigo);

    /**
     * Verifica si existe una marca con el código dado.
     * Más eficiente que findByCodigo() cuando solo necesitas saber si existe.
     *
     * Query generada: SELECT COUNT(*) > 0 FROM cat_marca WHERE codigo = ?
     */
    boolean existsByCodigo(String codigo);

    /**
     * Obtiene todas las marcas activas.
     * Útil para listar opciones en formularios.
     *
     * Query generada: SELECT * FROM cat_marca WHERE activo = true
     */
    List<CatMarca> findByActivoTrue();

    /**
     * Busca marcas cuyo nombre contenga el texto dado (ignora mayúsculas).
     * Útil para búsquedas tipo-ahead en el frontend.
     *
     * Query generada: SELECT * FROM cat_marca WHERE LOWER(nombre) LIKE LOWER(CONCAT('%', ?, '%'))
     */
    List<CatMarca> findByNombreContainingIgnoreCase(String nombre);
}
