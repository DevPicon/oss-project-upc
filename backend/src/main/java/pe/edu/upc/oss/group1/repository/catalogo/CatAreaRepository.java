package pe.edu.upc.oss.group1.repository.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.catalogo.CatArea;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operaciones de base de datos sobre CatArea.
 */
@Repository
public interface CatAreaRepository extends JpaRepository<CatArea, Integer> {

    Optional<CatArea> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<CatArea> findByActivoTrue();

    List<CatArea> findByNombreContainingIgnoreCase(String nombre);
}
