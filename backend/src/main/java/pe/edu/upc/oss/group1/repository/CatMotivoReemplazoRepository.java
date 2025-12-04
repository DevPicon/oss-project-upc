package pe.edu.upc.oss.group1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.catalogo.CatMotivoReemplazo;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para el catálogo de motivos de reemplazo.
 * Proporciona métodos de consulta derivados automáticamente por Spring Data JPA.
 */
@Repository
public interface CatMotivoReemplazoRepository extends JpaRepository<CatMotivoReemplazo, Integer> {

    /**
     * Busca un motivo de reemplazo por su código único.
     * @param codigo código del motivo
     * @return Optional con el motivo encontrado o vacío
     */
    Optional<CatMotivoReemplazo> findByCodigo(String codigo);

    /**
     * Verifica si existe un motivo con el código dado.
     * @param codigo código a verificar
     * @return true si existe, false si no
     */
    boolean existsByCodigo(String codigo);

    /**
     * Retorna solo los motivos activos.
     * @return lista de motivos con activo = true
     */
    List<CatMotivoReemplazo> findByActivoTrue();

    /**
     * Busca motivos por nombre (búsqueda parcial, insensible a mayúsculas).
     * @param nombre texto a buscar en el nombre
     * @return lista de motivos que coinciden
     */
    List<CatMotivoReemplazo> findByNombreContainingIgnoreCase(String nombre);
}
