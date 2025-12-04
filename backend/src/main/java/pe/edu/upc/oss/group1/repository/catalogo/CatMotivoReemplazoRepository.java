package pe.edu.upc.oss.group1.repository.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.catalogo.CatMotivoReemplazo;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operaciones de base de datos sobre CatMotivoReemplazo.
 */
@Repository
public interface CatMotivoReemplazoRepository extends JpaRepository<CatMotivoReemplazo, Integer> {

    Optional<CatMotivoReemplazo> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<CatMotivoReemplazo> findByActivoTrue();

    List<CatMotivoReemplazo> findByNombreContainingIgnoreCase(String nombre);
}
