package pe.edu.upc.oss.group1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.oss.group1.entity.catalogo.CatPuesto;
import pe.edu.upc.oss.group1.exception.DuplicateResourceException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.CatPuestoRepository;

import java.util.List;

/**
 * Servicio para la gestión del catálogo de puestos de trabajo.
 * Implementa lógica de negocio y validaciones.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CatPuestoService {

    private final CatPuestoRepository catPuestoRepository;

    /**
     * Crea un nuevo puesto.
     * Valida que el código no esté duplicado.
     */
    public CatPuesto create(CatPuesto puesto) {
        log.debug("Creando puesto con código: {}", puesto.getCodigo());

        if (catPuestoRepository.existsByCodigo(puesto.getCodigo())) {
            throw new DuplicateResourceException("Puesto", "código", puesto.getCodigo());
        }

        CatPuesto saved = catPuestoRepository.save(puesto);
        log.info("Puesto creado exitosamente con ID: {}", saved.getId());
        return saved;
    }

    /**
     * Obtiene todos los puestos.
     */
    @Transactional(readOnly = true)
    public List<CatPuesto> findAll() {
        log.debug("Obteniendo todos los puestos");
        return catPuestoRepository.findAll();
    }

    /**
     * Obtiene solo los puestos activos.
     */
    @Transactional(readOnly = true)
    public List<CatPuesto> findAllActive() {
        log.debug("Obteniendo puestos activos");
        return catPuestoRepository.findByActivoTrue();
    }

    /**
     * Busca un puesto por ID.
     */
    @Transactional(readOnly = true)
    public CatPuesto findById(Integer id) {
        log.debug("Buscando puesto con ID: {}", id);
        return catPuestoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Puesto", "id", id));
    }

    /**
     * Busca un puesto por código.
     */
    @Transactional(readOnly = true)
    public CatPuesto findByCodigo(String codigo) {
        log.debug("Buscando puesto con código: {}", codigo);
        return catPuestoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Puesto", "código", codigo));
    }

    /**
     * Actualiza un puesto existente.
     * Valida que el código no esté duplicado (si se modifica).
     */
    public CatPuesto update(Integer id, CatPuesto puesto) {
        log.debug("Actualizando puesto con ID: {}", id);

        CatPuesto existing = findById(id);

        // Validar código duplicado solo si cambió
        if (!existing.getCodigo().equals(puesto.getCodigo()) &&
            catPuestoRepository.existsByCodigo(puesto.getCodigo())) {
            throw new DuplicateResourceException("Puesto", "código", puesto.getCodigo());
        }

        existing.setCodigo(puesto.getCodigo());
        existing.setNombre(puesto.getNombre());
        existing.setDescripcion(puesto.getDescripcion());
        existing.setArea(puesto.getArea());
        existing.setActivo(puesto.getActivo());

        CatPuesto updated = catPuestoRepository.save(existing);
        log.info("Puesto actualizado exitosamente con ID: {}", id);
        return updated;
    }

    /**
     * Elimina lógicamente un puesto (soft delete).
     */
    public void delete(Integer id) {
        log.debug("Eliminando (soft delete) puesto con ID: {}", id);

        CatPuesto puesto = findById(id);
        puesto.setActivo(false);
        catPuestoRepository.save(puesto);

        log.info("Puesto eliminado lógicamente con ID: {}", id);
    }

    /**
     * Busca puestos por nombre (búsqueda parcial).
     */
    @Transactional(readOnly = true)
    public List<CatPuesto> search(String nombre) {
        log.debug("Buscando puestos con nombre: {}", nombre);
        return catPuestoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Obtiene puestos de un área específica.
     */
    @Transactional(readOnly = true)
    public List<CatPuesto> findByArea(Integer areaId) {
        log.debug("Obteniendo puestos del área ID: {}", areaId);
        return catPuestoRepository.findByAreaId(areaId);
    }

    /**
     * Obtiene puestos activos de un área específica.
     */
    @Transactional(readOnly = true)
    public List<CatPuesto> findActiveByArea(Integer areaId) {
        log.debug("Obteniendo puestos activos del área ID: {}", areaId);
        return catPuestoRepository.findByAreaIdAndActivoTrue(areaId);
    }
}
