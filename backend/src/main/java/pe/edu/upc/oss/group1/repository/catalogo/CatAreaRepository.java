package pe.edu.upc.oss.group1.repository.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.catalogo.CatArea;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para el catálogo de áreas organizacionales.
 * Incluye métodos para consultar la jerarquía de áreas.
 */
@Repository
public interface CatAreaRepository extends JpaRepository<CatArea, Integer> {

    /**
     * Busca un área por su código único.
     * @param codigo código del área
     * @return Optional con el área encontrada o vacío
     */
    Optional<CatArea> findByCodigo(String codigo);

    /**
     * Verifica si existe un área con el código dado.
     * @param codigo código a verificar
     * @return true si existe, false si no
     */
    boolean existsByCodigo(String codigo);

    /**
     * Retorna solo las áreas activas.
     * @return lista de áreas con activo = true
     */
    List<CatArea> findByActivoTrue();

    /**
     * Busca áreas por nombre (búsqueda parcial, insensible a mayúsculas).
     * @param nombre texto a buscar en el nombre
     * @return lista de áreas que coinciden
     */
    List<CatArea> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca áreas raíz (sin área superior).
     * @return lista de áreas sin padre
     */
    List<CatArea> findByAreaSuperiorIsNull();

    /**
     * Busca sub-áreas de un área dada.
     * @param areaSuperiorId ID del área padre
     * @return lista de áreas hijas
     */
    List<CatArea> findByAreaSuperiorId(Integer areaSuperiorId);
}
