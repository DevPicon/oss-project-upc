package pe.edu.upc.oss.group1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.oss.group1.entity.catalogo.CatCondicionDevolucion;
import pe.edu.upc.oss.group1.exception.DuplicateResourceException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.catalogo.CatCondicionDevolucionRepository;

import java.util.List;

/**
 * Servicio para la gestión del catálogo de condiciones de devolución.
 * Implementa lógica de negocio y validaciones.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CatCondicionDevolucionService {

    private final CatCondicionDevolucionRepository catCondicionDevolucionRepository;

    /**
     * Crea una nueva condición de devolución.
     * Valida que el código no esté duplicado.
     */
    public CatCondicionDevolucion create(CatCondicionDevolucion condicionDevolucion) {
        log.debug("Creando condición de devolución con código: {}", condicionDevolucion.getCodigo());

        if (catCondicionDevolucionRepository.existsByCodigo(condicionDevolucion.getCodigo())) {
            throw new DuplicateResourceException("Condición de devolución", "código", condicionDevolucion.getCodigo());
        }

        CatCondicionDevolucion saved = catCondicionDevolucionRepository.save(condicionDevolucion);
        log.info("Condición de devolución creada exitosamente con ID: {}", saved.getId());
        return saved;
    }

    /**
     * Obtiene todas las condiciones de devolución.
     */
    @Transactional(readOnly = true)
    public List<CatCondicionDevolucion> findAll() {
        log.debug("Obteniendo todas las condiciones de devolución");
        return catCondicionDevolucionRepository.findAll();
    }

    /**
     * Obtiene solo las condiciones activas.
     */
    @Transactional(readOnly = true)
    public List<CatCondicionDevolucion> findAllActive() {
        log.debug("Obteniendo condiciones de devolución activas");
        return catCondicionDevolucionRepository.findByActivoTrue();
    }

    /**
     * Busca una condición de devolución por ID.
     */
    @Transactional(readOnly = true)
    public CatCondicionDevolucion findById(Integer id) {
        log.debug("Buscando condición de devolución con ID: {}", id);
        return catCondicionDevolucionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Condición de devolución", "id", id));
    }

    /**
     * Busca una condición de devolución por código.
     */
    @Transactional(readOnly = true)
    public CatCondicionDevolucion findByCodigo(String codigo) {
        log.debug("Buscando condición de devolución con código: {}", codigo);
        return catCondicionDevolucionRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Condición de devolución", "código", codigo));
    }

    /**
     * Actualiza una condición de devolución existente.
     * Valida que el código no esté duplicado (si se modifica).
     */
    public CatCondicionDevolucion update(Integer id, CatCondicionDevolucion condicionDevolucion) {
        log.debug("Actualizando condición de devolución con ID: {}", id);

        CatCondicionDevolucion existing = findById(id);

        // Validar código duplicado solo si cambió
        if (!existing.getCodigo().equals(condicionDevolucion.getCodigo()) &&
            catCondicionDevolucionRepository.existsByCodigo(condicionDevolucion.getCodigo())) {
            throw new DuplicateResourceException("Condición de devolución", "código", condicionDevolucion.getCodigo());
        }

        existing.setCodigo(condicionDevolucion.getCodigo());
        existing.setNombre(condicionDevolucion.getNombre());
        existing.setDescripcion(condicionDevolucion.getDescripcion());
        existing.setActivo(condicionDevolucion.getActivo());

        CatCondicionDevolucion updated = catCondicionDevolucionRepository.save(existing);
        log.info("Condición de devolución actualizada exitosamente con ID: {}", id);
        return updated;
    }

    /**
     * Elimina lógicamente una condición de devolución (soft delete).
     */
    public void delete(Integer id) {
        log.debug("Eliminando (soft delete) condición de devolución con ID: {}", id);

        CatCondicionDevolucion condicionDevolucion = findById(id);
        condicionDevolucion.setActivo(false);
        catCondicionDevolucionRepository.save(condicionDevolucion);

        log.info("Condición de devolución eliminada lógicamente con ID: {}", id);
    }

    /**
     * Busca condiciones por nombre (búsqueda parcial).
     */
    @Transactional(readOnly = true)
    public List<CatCondicionDevolucion> search(String nombre) {
        log.debug("Buscando condiciones de devolución con nombre: {}", nombre);
        return catCondicionDevolucionRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
