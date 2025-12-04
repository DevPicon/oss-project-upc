package pe.edu.upc.oss.group1.repository.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.catalogo.CatProveedor;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operaciones de base de datos sobre CatProveedor.
 */
@Repository
public interface CatProveedorRepository extends JpaRepository<CatProveedor, Integer> {

    Optional<CatProveedor> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<CatProveedor> findByActivoTrue();

    List<CatProveedor> findByNombreContainingIgnoreCase(String nombre);
}
