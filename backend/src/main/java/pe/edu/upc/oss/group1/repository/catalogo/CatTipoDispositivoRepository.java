package pe.edu.upc.oss.group1.repository.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oss.group1.entity.catalogo.CatTipoDispositivo;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para el catálogo de tipos de dispositivo.
 * Incluye métodos para filtrar por requisito de número de serie.
 */
@Repository
public interface CatTipoDispositivoRepository extends JpaRepository<CatTipoDispositivo, Integer> {

    /**
     * Busca un tipo de dispositivo por su código único.
     * @param codigo código del tipo de dispositivo
     * @return Optional con el tipo encontrado o vacío
     */
    Optional<CatTipoDispositivo> findByCodigo(String codigo);

    /**
     * Verifica si existe un tipo de dispositivo con el código dado.
     * @param codigo código a verificar
     * @return true si existe, false si no
     */
    boolean existsByCodigo(String codigo);

    /**
     * Retorna solo los tipos de dispositivo activos.
     * @return lista de tipos con activo = true
     */
    List<CatTipoDispositivo> findByActivoTrue();

    /**
     * Busca tipos por nombre (búsqueda parcial, insensible a mayúsculas).
     * @param nombre texto a buscar en el nombre
     * @return lista de tipos que coinciden
     */
    List<CatTipoDispositivo> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca tipos que requieren número de serie.
     * @return lista de tipos con requiereSerie = true
     */
    List<CatTipoDispositivo> findByRequiereSerieTrue();
}
