package pe.edu.upc.oss.group1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoReemplazo;
import pe.edu.upc.oss.group1.exception.DuplicateResourceException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.catalogo.CatEstadoReemplazoRepository;

import java.util.List;

/**
 * Servicio para la gestión del catálogo de estados de reemplazo.
 * Implementa lógica de negocio y validaciones.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CatEstadoReemplazoService {

    private final CatEstadoReemplazoRepository catEstadoReemplazoRepository;

    /**
     * Crea un nuevo estado de reemplazo.
     * Valida que el código no esté duplicado.
     */
    public CatEstadoReemplazo create(CatEstadoReemplazo estadoReemplazo) {
        log.debug("Creando estado de reemplazo con código: {}", estadoReemplazo.getCodigo());

        if (catEstadoReemplazoRepository.existsByCodigo(estadoReemplazo.getCodigo())) {
            throw new DuplicateResourceException("Estado de reemplazo", "código", estadoReemplazo.getCodigo());
        }

        CatEstadoReemplazo saved = catEstadoReemplazoRepository.save(estadoReemplazo);
        log.info("Estado de reemplazo creado exitosamente con ID: {}", saved.getId());
        return saved;
    }

    /**
     * Obtiene todos los estados de reemplazo.
     */
    @Transactional(readOnly = true)
    public List<CatEstadoReemplazo> findAll() {
        log.debug("Obteniendo todos los estados de reemplazo");
        return catEstadoReemplazoRepository.findAll();
    }

    /**
     * Obtiene solo los estados activos.
     */
    @Transactional(readOnly = true)
    public List<CatEstadoReemplazo> findAllActive() {
        log.debug("Obteniendo estados de reemplazo activos");
        return catEstadoReemplazoRepository.findByActivoTrue();
    }

    /**
     * Busca un estado de reemplazo por ID.
     */
    @Transactional(readOnly = true)
    public CatEstadoReemplazo findById(Integer id) {
        log.debug("Buscando estado de reemplazo con ID: {}", id);
        return catEstadoReemplazoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado de reemplazo", "id", id));
    }

    /**
     * Busca un estado de reemplazo por código.
     */
    @Transactional(readOnly = true)
    public CatEstadoReemplazo findByCodigo(String codigo) {
        log.debug("Buscando estado de reemplazo con código: {}", codigo);
        return catEstadoReemplazoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Estado de reemplazo", "código", codigo));
    }

    /**
     * Actualiza un estado de reemplazo existente.
     * Valida que el código no esté duplicado (si se modifica).
     */
    public CatEstadoReemplazo update(Integer id, CatEstadoReemplazo estadoReemplazo) {
        log.debug("Actualizando estado de reemplazo con ID: {}", id);

        CatEstadoReemplazo existing = findById(id);

        // Validar código duplicado solo si cambió
        if (!existing.getCodigo().equals(estadoReemplazo.getCodigo()) &&
            catEstadoReemplazoRepository.existsByCodigo(estadoReemplazo.getCodigo())) {
            throw new DuplicateResourceException("Estado de reemplazo", "código", estadoReemplazo.getCodigo());
        }

        existing.setCodigo(estadoReemplazo.getCodigo());
        existing.setNombre(estadoReemplazo.getNombre());
        existing.setDescripcion(estadoReemplazo.getDescripcion());
        existing.setActivo(estadoReemplazo.getActivo());

        CatEstadoReemplazo updated = catEstadoReemplazoRepository.save(existing);
        log.info("Estado de reemplazo actualizado exitosamente con ID: {}", id);
        return updated;
    }

    /**
     * Elimina lógicamente un estado de reemplazo (soft delete).
     */
    public void delete(Integer id) {
        log.debug("Eliminando (soft delete) estado de reemplazo con ID: {}", id);

        CatEstadoReemplazo estadoReemplazo = findById(id);
        estadoReemplazo.setActivo(false);
        catEstadoReemplazoRepository.save(estadoReemplazo);

        log.info("Estado de reemplazo eliminado lógicamente con ID: {}", id);
    }

    /**
     * Busca estados por nombre (búsqueda parcial).
     */
    @Transactional(readOnly = true)
    public List<CatEstadoReemplazo> search(String nombre) {
        log.debug("Buscando estados de reemplazo con nombre: {}", nombre);
        return catEstadoReemplazoRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
