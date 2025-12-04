package pe.edu.upc.oss.group1.repository.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoReemplazo;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operaciones de base de datos sobre CatEstadoReemplazo.
 */
@Repository
public interface CatEstadoReemplazoRepository extends JpaRepository<CatEstadoReemplazo, Integer> {

    Optional<CatEstadoReemplazo> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<CatEstadoReemplazo> findByActivoTrue();

    List<CatEstadoReemplazo> findByNombreContainingIgnoreCase(String nombre);
}
