package pe.edu.upc.oss.group1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoAsignacion;
import pe.edu.upc.oss.group1.exception.DuplicateResourceException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.CatEstadoAsignacionRepository;

import java.util.List;

/**
 * Servicio para la gestión del catálogo de estados de asignación.
 * Implementa lógica de negocio y validaciones.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CatEstadoAsignacionService {

    private final CatEstadoAsignacionRepository catEstadoAsignacionRepository;

    /**
     * Crea un nuevo estado de asignación.
     * Valida que el código no esté duplicado.
     */
    public CatEstadoAsignacion create(CatEstadoAsignacion estadoAsignacion) {
        log.debug("Creando estado de asignación con código: {}", estadoAsignacion.getCodigo());

        if (catEstadoAsignacionRepository.existsByCodigo(estadoAsignacion.getCodigo())) {
            throw new DuplicateResourceException("Estado de asignación", "código", estadoAsignacion.getCodigo());
        }

        CatEstadoAsignacion saved = catEstadoAsignacionRepository.save(estadoAsignacion);
        log.info("Estado de asignación creado exitosamente con ID: {}", saved.getId());
        return saved;
    }

    /**
     * Obtiene todos los estados de asignación.
     */
    @Transactional(readOnly = true)
    public List<CatEstadoAsignacion> findAll() {
        log.debug("Obteniendo todos los estados de asignación");
        return catEstadoAsignacionRepository.findAll();
    }

    /**
     * Obtiene solo los estados activos.
     */
    @Transactional(readOnly = true)
    public List<CatEstadoAsignacion> findAllActive() {
        log.debug("Obteniendo estados de asignación activos");
        return catEstadoAsignacionRepository.findByActivoTrue();
    }

    /**
     * Busca un estado de asignación por ID.
     */
    @Transactional(readOnly = true)
    public CatEstadoAsignacion findById(Integer id) {
        log.debug("Buscando estado de asignación con ID: {}", id);
        return catEstadoAsignacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado de asignación", "id", id));
    }

    /**
     * Busca un estado de asignación por código.
     */
    @Transactional(readOnly = true)
    public CatEstadoAsignacion findByCodigo(String codigo) {
        log.debug("Buscando estado de asignación con código: {}", codigo);
        return catEstadoAsignacionRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Estado de asignación", "código", codigo));
    }

    /**
     * Actualiza un estado de asignación existente.
     * Valida que el código no esté duplicado (si se modifica).
     */
    public CatEstadoAsignacion update(Integer id, CatEstadoAsignacion estadoAsignacion) {
        log.debug("Actualizando estado de asignación con ID: {}", id);

        CatEstadoAsignacion existing = findById(id);

        // Validar código duplicado solo si cambió
        if (!existing.getCodigo().equals(estadoAsignacion.getCodigo()) &&
            catEstadoAsignacionRepository.existsByCodigo(estadoAsignacion.getCodigo())) {
            throw new DuplicateResourceException("Estado de asignación", "código", estadoAsignacion.getCodigo());
        }

        existing.setCodigo(estadoAsignacion.getCodigo());
        existing.setNombre(estadoAsignacion.getNombre());
        existing.setDescripcion(estadoAsignacion.getDescripcion());
        existing.setActivo(estadoAsignacion.getActivo());

        CatEstadoAsignacion updated = catEstadoAsignacionRepository.save(existing);
        log.info("Estado de asignación actualizado exitosamente con ID: {}", id);
        return updated;
    }

    /**
     * Elimina lógicamente un estado de asignación (soft delete).
     */
    public void delete(Integer id) {
        log.debug("Eliminando (soft delete) estado de asignación con ID: {}", id);

        CatEstadoAsignacion estadoAsignacion = findById(id);
        estadoAsignacion.setActivo(false);
        catEstadoAsignacionRepository.save(estadoAsignacion);

        log.info("Estado de asignación eliminado lógicamente con ID: {}", id);
    }

    /**
     * Busca estados por nombre (búsqueda parcial).
     */
    @Transactional(readOnly = true)
    public List<CatEstadoAsignacion> search(String nombre) {
        log.debug("Buscando estados de asignación con nombre: {}", nombre);
        return catEstadoAsignacionRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
