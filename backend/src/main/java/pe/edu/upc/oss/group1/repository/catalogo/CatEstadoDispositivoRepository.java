package pe.edu.upc.oss.group1.repository.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoDispositivo;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operaciones de base de datos sobre CatEstadoDispositivo.
 */
@Repository
public interface CatEstadoDispositivoRepository extends JpaRepository<CatEstadoDispositivo, Integer> {

    Optional<CatEstadoDispositivo> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<CatEstadoDispositivo> findByActivoTrue();

    List<CatEstadoDispositivo> findByNombreContainingIgnoreCase(String nombre);
}
