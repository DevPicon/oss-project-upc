package pe.edu.upc.oss.group1.repository.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.catalogo.CatSede;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operaciones de base de datos sobre CatSede.
 */
@Repository
public interface CatSedeRepository extends JpaRepository<CatSede, Integer> {

    Optional<CatSede> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<CatSede> findByActivoTrue();

    List<CatSede> findByNombreContainingIgnoreCase(String nombre);
}
