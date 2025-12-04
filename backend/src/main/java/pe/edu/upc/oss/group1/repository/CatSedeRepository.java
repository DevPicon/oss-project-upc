package pe.edu.upc.oss.group1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.catalogo.CatSede;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para el catálogo de sedes.
 * Permite consultar sedes por ubicación geográfica.
 */
@Repository
public interface CatSedeRepository extends JpaRepository<CatSede, Integer> {

    /**
     * Busca una sede por su código único.
     * @param codigo código de la sede
     * @return Optional con la sede encontrada o vacío
     */
    Optional<CatSede> findByCodigo(String codigo);

    /**
     * Verifica si existe una sede con el código dado.
     * @param codigo código a verificar
     * @return true si existe, false si no
     */
    boolean existsByCodigo(String codigo);

    /**
     * Retorna solo las sedes activas.
     * @return lista de sedes con activo = true
     */
    List<CatSede> findByActivoTrue();

    /**
     * Busca sedes por nombre (búsqueda parcial, insensible a mayúsculas).
     * @param nombre texto a buscar en el nombre
     * @return lista de sedes que coinciden
     */
    List<CatSede> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca sedes por ciudad.
     * @param ciudad nombre de la ciudad
     * @return lista de sedes en la ciudad
     */
    List<CatSede> findByCiudad(String ciudad);

    /**
     * Busca sedes por país.
     * @param pais nombre del país
     * @return lista de sedes en el país
     */
    List<CatSede> findByPais(String pais);
}
