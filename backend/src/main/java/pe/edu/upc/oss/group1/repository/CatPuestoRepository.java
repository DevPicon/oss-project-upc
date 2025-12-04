package pe.edu.upc.oss.group1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.catalogo.CatPuesto;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para el catálogo de puestos de trabajo.
 * Incluye métodos para consultar puestos por área.
 */
@Repository
public interface CatPuestoRepository extends JpaRepository<CatPuesto, Integer> {

    /**
     * Busca un puesto por su código único.
     * @param codigo código del puesto
     * @return Optional con el puesto encontrado o vacío
     */
    Optional<CatPuesto> findByCodigo(String codigo);

    /**
     * Verifica si existe un puesto con el código dado.
     * @param codigo código a verificar
     * @return true si existe, false si no
     */
    boolean existsByCodigo(String codigo);

    /**
     * Retorna solo los puestos activos.
     * @return lista de puestos con activo = true
     */
    List<CatPuesto> findByActivoTrue();

    /**
     * Busca puestos por nombre (búsqueda parcial, insensible a mayúsculas).
     * @param nombre texto a buscar en el nombre
     * @return lista de puestos que coinciden
     */
    List<CatPuesto> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca puestos por área.
     * @param areaId ID del área
     * @return lista de puestos del área
     */
    List<CatPuesto> findByAreaId(Integer areaId);

    /**
     * Busca puestos activos por área.
     * @param areaId ID del área
     * @return lista de puestos activos del área
     */
    List<CatPuesto> findByAreaIdAndActivoTrue(Integer areaId);
}
