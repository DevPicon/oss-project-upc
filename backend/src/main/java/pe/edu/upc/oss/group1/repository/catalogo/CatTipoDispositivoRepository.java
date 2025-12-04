package pe.edu.upc.oss.group1.repository.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.catalogo.CatTipoDispositivo;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operaciones de base de datos sobre CatTipoDispositivo.
 */
@Repository
public interface CatTipoDispositivoRepository extends JpaRepository<CatTipoDispositivo, Integer> {

    Optional<CatTipoDispositivo> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<CatTipoDispositivo> findByActivoTrue();

    List<CatTipoDispositivo> findByNombreContainingIgnoreCase(String nombre);
}
