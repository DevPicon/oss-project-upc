package pe.edu.upc.oss.group1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoSolicitud;
import pe.edu.upc.oss.group1.exception.DuplicateResourceException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.catalogo.CatEstadoSolicitudRepository;

import java.util.List;

/**
 * Servicio para la gestión del catálogo de estados de solicitud.
 * Implementa lógica de negocio y validaciones.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CatEstadoSolicitudService {

    private final CatEstadoSolicitudRepository catEstadoSolicitudRepository;

    /**
     * Crea un nuevo estado de solicitud.
     * Valida que el código no esté duplicado.
     */
    public CatEstadoSolicitud create(CatEstadoSolicitud estadoSolicitud) {
        log.debug("Creando estado de solicitud con código: {}", estadoSolicitud.getCodigo());

        if (catEstadoSolicitudRepository.existsByCodigo(estadoSolicitud.getCodigo())) {
            throw new DuplicateResourceException("Estado de solicitud", "código", estadoSolicitud.getCodigo());
        }

        CatEstadoSolicitud saved = catEstadoSolicitudRepository.save(estadoSolicitud);
        log.info("Estado de solicitud creado exitosamente con ID: {}", saved.getId());
        return saved;
    }

    /**
     * Obtiene todos los estados de solicitud.
     */
    @Transactional(readOnly = true)
    public List<CatEstadoSolicitud> findAll() {
        log.debug("Obteniendo todos los estados de solicitud");
        return catEstadoSolicitudRepository.findAll();
    }

    /**
     * Obtiene solo los estados activos.
     */
    @Transactional(readOnly = true)
    public List<CatEstadoSolicitud> findAllActive() {
        log.debug("Obteniendo estados de solicitud activos");
        return catEstadoSolicitudRepository.findByActivoTrue();
    }

    /**
     * Busca un estado de solicitud por ID.
     */
    @Transactional(readOnly = true)
    public CatEstadoSolicitud findById(Integer id) {
        log.debug("Buscando estado de solicitud con ID: {}", id);
        return catEstadoSolicitudRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado de solicitud", "id", id));
    }

    /**
     * Busca un estado de solicitud por código.
     */
    @Transactional(readOnly = true)
    public CatEstadoSolicitud findByCodigo(String codigo) {
        log.debug("Buscando estado de solicitud con código: {}", codigo);
        return catEstadoSolicitudRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Estado de solicitud", "código", codigo));
    }

    /**
     * Actualiza un estado de solicitud existente.
     * Valida que el código no esté duplicado (si se modifica).
     */
    public CatEstadoSolicitud update(Integer id, CatEstadoSolicitud estadoSolicitud) {
        log.debug("Actualizando estado de solicitud con ID: {}", id);

        CatEstadoSolicitud existing = findById(id);

        // Validar código duplicado solo si cambió
        if (!existing.getCodigo().equals(estadoSolicitud.getCodigo()) &&
            catEstadoSolicitudRepository.existsByCodigo(estadoSolicitud.getCodigo())) {
            throw new DuplicateResourceException("Estado de solicitud", "código", estadoSolicitud.getCodigo());
        }

        existing.setCodigo(estadoSolicitud.getCodigo());
        existing.setNombre(estadoSolicitud.getNombre());
        existing.setDescripcion(estadoSolicitud.getDescripcion());
        existing.setActivo(estadoSolicitud.getActivo());

        CatEstadoSolicitud updated = catEstadoSolicitudRepository.save(existing);
        log.info("Estado de solicitud actualizado exitosamente con ID: {}", id);
        return updated;
    }

    /**
     * Elimina lógicamente un estado de solicitud (soft delete).
     */
    public void delete(Integer id) {
        log.debug("Eliminando (soft delete) estado de solicitud con ID: {}", id);

        CatEstadoSolicitud estadoSolicitud = findById(id);
        estadoSolicitud.setActivo(false);
        catEstadoSolicitudRepository.save(estadoSolicitud);

        log.info("Estado de solicitud eliminado lógicamente con ID: {}", id);
    }

    /**
     * Busca estados por nombre (búsqueda parcial).
     */
    @Transactional(readOnly = true)
    public List<CatEstadoSolicitud> search(String nombre) {
        log.debug("Buscando estados de solicitud con nombre: {}", nombre);
        return catEstadoSolicitudRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
