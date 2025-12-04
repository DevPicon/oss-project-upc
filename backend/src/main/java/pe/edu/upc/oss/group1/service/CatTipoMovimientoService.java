package pe.edu.upc.oss.group1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.oss.group1.entity.catalogo.CatTipoMovimiento;
import pe.edu.upc.oss.group1.exception.DuplicateResourceException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.CatTipoMovimientoRepository;

import java.util.List;

/**
 * Servicio para la gestión del catálogo de tipos de movimiento.
 * Implementa lógica de negocio y validaciones.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CatTipoMovimientoService {

    private final CatTipoMovimientoRepository catTipoMovimientoRepository;

    /**
     * Crea un nuevo tipo de movimiento.
     * Valida que el código no esté duplicado.
     */
    public CatTipoMovimiento create(CatTipoMovimiento tipoMovimiento) {
        log.debug("Creando tipo de movimiento con código: {}", tipoMovimiento.getCodigo());

        if (catTipoMovimientoRepository.existsByCodigo(tipoMovimiento.getCodigo())) {
            throw new DuplicateResourceException("Tipo de movimiento", "código", tipoMovimiento.getCodigo());
        }

        CatTipoMovimiento saved = catTipoMovimientoRepository.save(tipoMovimiento);
        log.info("Tipo de movimiento creado exitosamente con ID: {}", saved.getId());
        return saved;
    }

    /**
     * Obtiene todos los tipos de movimiento.
     */
    @Transactional(readOnly = true)
    public List<CatTipoMovimiento> findAll() {
        log.debug("Obteniendo todos los tipos de movimiento");
        return catTipoMovimientoRepository.findAll();
    }

    /**
     * Obtiene solo los tipos activos.
     */
    @Transactional(readOnly = true)
    public List<CatTipoMovimiento> findAllActive() {
        log.debug("Obteniendo tipos de movimiento activos");
        return catTipoMovimientoRepository.findByActivoTrue();
    }

    /**
     * Busca un tipo de movimiento por ID.
     */
    @Transactional(readOnly = true)
    public CatTipoMovimiento findById(Integer id) {
        log.debug("Buscando tipo de movimiento con ID: {}", id);
        return catTipoMovimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de movimiento", "id", id));
    }

    /**
     * Busca un tipo de movimiento por código.
     */
    @Transactional(readOnly = true)
    public CatTipoMovimiento findByCodigo(String codigo) {
        log.debug("Buscando tipo de movimiento con código: {}", codigo);
        return catTipoMovimientoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de movimiento", "código", codigo));
    }

    /**
     * Actualiza un tipo de movimiento existente.
     * Valida que el código no esté duplicado (si se modifica).
     */
    public CatTipoMovimiento update(Integer id, CatTipoMovimiento tipoMovimiento) {
        log.debug("Actualizando tipo de movimiento con ID: {}", id);

        CatTipoMovimiento existing = findById(id);

        // Validar código duplicado solo si cambió
        if (!existing.getCodigo().equals(tipoMovimiento.getCodigo()) &&
            catTipoMovimientoRepository.existsByCodigo(tipoMovimiento.getCodigo())) {
            throw new DuplicateResourceException("Tipo de movimiento", "código", tipoMovimiento.getCodigo());
        }

        existing.setCodigo(tipoMovimiento.getCodigo());
        existing.setNombre(tipoMovimiento.getNombre());
        existing.setDescripcion(tipoMovimiento.getDescripcion());
        existing.setActivo(tipoMovimiento.getActivo());

        CatTipoMovimiento updated = catTipoMovimientoRepository.save(existing);
        log.info("Tipo de movimiento actualizado exitosamente con ID: {}", id);
        return updated;
    }

    /**
     * Elimina lógicamente un tipo de movimiento (soft delete).
     */
    public void delete(Integer id) {
        log.debug("Eliminando (soft delete) tipo de movimiento con ID: {}", id);

        CatTipoMovimiento tipoMovimiento = findById(id);
        tipoMovimiento.setActivo(false);
        catTipoMovimientoRepository.save(tipoMovimiento);

        log.info("Tipo de movimiento eliminado lógicamente con ID: {}", id);
    }

    /**
     * Busca tipos por nombre (búsqueda parcial).
     */
    @Transactional(readOnly = true)
    public List<CatTipoMovimiento> search(String nombre) {
        log.debug("Buscando tipos de movimiento con nombre: {}", nombre);
        return catTipoMovimientoRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
