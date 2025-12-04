package pe.edu.upc.oss.group1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoDispositivo;
import pe.edu.upc.oss.group1.exception.DuplicateResourceException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.CatEstadoDispositivoRepository;

import java.util.List;

/**
 * Servicio para la gestión del catálogo de estados de dispositivo.
 * Implementa lógica de negocio y validaciones.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CatEstadoDispositivoService {

    private final CatEstadoDispositivoRepository catEstadoDispositivoRepository;

    /**
     * Crea un nuevo estado de dispositivo.
     * Valida que el código no esté duplicado.
     */
    public CatEstadoDispositivo create(CatEstadoDispositivo estadoDispositivo) {
        log.debug("Creando estado de dispositivo con código: {}", estadoDispositivo.getCodigo());

        if (catEstadoDispositivoRepository.existsByCodigo(estadoDispositivo.getCodigo())) {
            throw new DuplicateResourceException("Estado de dispositivo", "código", estadoDispositivo.getCodigo());
        }

        CatEstadoDispositivo saved = catEstadoDispositivoRepository.save(estadoDispositivo);
        log.info("Estado de dispositivo creado exitosamente con ID: {}", saved.getId());
        return saved;
    }

    /**
     * Obtiene todos los estados de dispositivo.
     */
    @Transactional(readOnly = true)
    public List<CatEstadoDispositivo> findAll() {
        log.debug("Obteniendo todos los estados de dispositivo");
        return catEstadoDispositivoRepository.findAll();
    }

    /**
     * Obtiene solo los estados activos.
     */
    @Transactional(readOnly = true)
    public List<CatEstadoDispositivo> findAllActive() {
        log.debug("Obteniendo estados de dispositivo activos");
        return catEstadoDispositivoRepository.findByActivoTrue();
    }

    /**
     * Busca un estado de dispositivo por ID.
     */
    @Transactional(readOnly = true)
    public CatEstadoDispositivo findById(Integer id) {
        log.debug("Buscando estado de dispositivo con ID: {}", id);
        return catEstadoDispositivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado de dispositivo", "id", id));
    }

    /**
     * Busca un estado de dispositivo por código.
     */
    @Transactional(readOnly = true)
    public CatEstadoDispositivo findByCodigo(String codigo) {
        log.debug("Buscando estado de dispositivo con código: {}", codigo);
        return catEstadoDispositivoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Estado de dispositivo", "código", codigo));
    }

    /**
     * Actualiza un estado de dispositivo existente.
     * Valida que el código no esté duplicado (si se modifica).
     */
    public CatEstadoDispositivo update(Integer id, CatEstadoDispositivo estadoDispositivo) {
        log.debug("Actualizando estado de dispositivo con ID: {}", id);

        CatEstadoDispositivo existing = findById(id);

        // Validar código duplicado solo si cambió
        if (!existing.getCodigo().equals(estadoDispositivo.getCodigo()) &&
            catEstadoDispositivoRepository.existsByCodigo(estadoDispositivo.getCodigo())) {
            throw new DuplicateResourceException("Estado de dispositivo", "código", estadoDispositivo.getCodigo());
        }

        existing.setCodigo(estadoDispositivo.getCodigo());
        existing.setNombre(estadoDispositivo.getNombre());
        existing.setDescripcion(estadoDispositivo.getDescripcion());
        existing.setDisponibleAsignacion(estadoDispositivo.getDisponibleAsignacion());
        existing.setActivo(estadoDispositivo.getActivo());

        CatEstadoDispositivo updated = catEstadoDispositivoRepository.save(existing);
        log.info("Estado de dispositivo actualizado exitosamente con ID: {}", id);
        return updated;
    }

    /**
     * Elimina lógicamente un estado de dispositivo (soft delete).
     */
    public void delete(Integer id) {
        log.debug("Eliminando (soft delete) estado de dispositivo con ID: {}", id);

        CatEstadoDispositivo estadoDispositivo = findById(id);
        estadoDispositivo.setActivo(false);
        catEstadoDispositivoRepository.save(estadoDispositivo);

        log.info("Estado de dispositivo eliminado lógicamente con ID: {}", id);
    }

    /**
     * Busca estados por nombre (búsqueda parcial).
     */
    @Transactional(readOnly = true)
    public List<CatEstadoDispositivo> search(String nombre) {
        log.debug("Buscando estados de dispositivo con nombre: {}", nombre);
        return catEstadoDispositivoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Obtiene estados disponibles para asignación.
     */
    @Transactional(readOnly = true)
    public List<CatEstadoDispositivo> findEstadosDisponibles() {
        log.debug("Obteniendo estados disponibles para asignación");
        return catEstadoDispositivoRepository.findByDisponibleAsignacionTrue();
    }
}
