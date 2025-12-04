package pe.edu.upc.oss.group1.repository.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoSolicitud;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operaciones de base de datos sobre CatEstadoSolicitud.
 */
@Repository
public interface CatEstadoSolicitudRepository extends JpaRepository<CatEstadoSolicitud, Integer> {

    Optional<CatEstadoSolicitud> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<CatEstadoSolicitud> findByActivoTrue();

    List<CatEstadoSolicitud> findByNombreContainingIgnoreCase(String nombre);
}
