package pe.edu.upc.oss.group1.repository.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoAsignacion;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operaciones de base de datos sobre CatEstadoAsignacion.
 */
@Repository
public interface CatEstadoAsignacionRepository extends JpaRepository<CatEstadoAsignacion, Integer> {

    Optional<CatEstadoAsignacion> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<CatEstadoAsignacion> findByActivoTrue();

    List<CatEstadoAsignacion> findByNombreContainingIgnoreCase(String nombre);
}
