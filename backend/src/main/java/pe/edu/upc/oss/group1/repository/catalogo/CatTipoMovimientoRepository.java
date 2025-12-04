package pe.edu.upc.oss.group1.repository.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.catalogo.CatTipoMovimiento;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operaciones de base de datos sobre CatTipoMovimiento.
 */
@Repository
public interface CatTipoMovimientoRepository extends JpaRepository<CatTipoMovimiento, Integer> {

    Optional<CatTipoMovimiento> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<CatTipoMovimiento> findByActivoTrue();

    List<CatTipoMovimiento> findByNombreContainingIgnoreCase(String nombre);
}
