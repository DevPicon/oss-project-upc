package pe.edu.upc.oss.group1.repository.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.catalogo.CatCondicionDevolucion;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operaciones de base de datos sobre CatCondicionDevolucion.
 */
@Repository
public interface CatCondicionDevolucionRepository extends JpaRepository<CatCondicionDevolucion, Integer> {

    Optional<CatCondicionDevolucion> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<CatCondicionDevolucion> findByActivoTrue();

    List<CatCondicionDevolucion> findByNombreContainingIgnoreCase(String nombre);
}
