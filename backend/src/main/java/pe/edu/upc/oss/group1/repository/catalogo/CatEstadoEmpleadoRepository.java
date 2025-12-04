package pe.edu.upc.oss.group1.repository.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoEmpleado;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operaciones de base de datos sobre CatEstadoEmpleado.
 */
@Repository
public interface CatEstadoEmpleadoRepository extends JpaRepository<CatEstadoEmpleado, Integer> {

    Optional<CatEstadoEmpleado> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<CatEstadoEmpleado> findByActivoTrue();

    List<CatEstadoEmpleado> findByNombreContainingIgnoreCase(String nombre);
}
