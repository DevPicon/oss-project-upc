package pe.edu.upc.oss.group1.repository.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.catalogo.CatPuesto;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operaciones de base de datos sobre CatPuesto.
 */
@Repository
public interface CatPuestoRepository extends JpaRepository<CatPuesto, Integer> {

    Optional<CatPuesto> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<CatPuesto> findByActivoTrue();

    List<CatPuesto> findByNombreContainingIgnoreCase(String nombre);
}
