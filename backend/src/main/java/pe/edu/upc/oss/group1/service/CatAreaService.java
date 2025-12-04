package pe.edu.upc.oss.group1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.oss.group1.entity.catalogo.CatArea;
import pe.edu.upc.oss.group1.exception.DuplicateResourceException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.CatAreaRepository;

import java.util.List;

/**
 * Servicio para la gestión del catálogo de áreas organizacionales.
 * Implementa lógica de negocio y validaciones para la jerarquía de áreas.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CatAreaService {

    private final CatAreaRepository catAreaRepository;

    /**
     * Crea una nueva área.
     * Valida que el código no esté duplicado.
     */
    public CatArea create(CatArea area) {
        log.debug("Creando área con código: {}", area.getCodigo());

        if (catAreaRepository.existsByCodigo(area.getCodigo())) {
            throw new DuplicateResourceException("Área", "código", area.getCodigo());
        }

        CatArea saved = catAreaRepository.save(area);
        log.info("Área creada exitosamente con ID: {}", saved.getId());
        return saved;
    }

    /**
     * Obtiene todas las áreas.
     */
    @Transactional(readOnly = true)
    public List<CatArea> findAll() {
        log.debug("Obteniendo todas las áreas");
        return catAreaRepository.findAll();
    }

    /**
     * Obtiene solo las áreas activas.
     */
    @Transactional(readOnly = true)
    public List<CatArea> findAllActive() {
        log.debug("Obteniendo áreas activas");
        return catAreaRepository.findByActivoTrue();
    }

    /**
     * Busca un área por ID.
     */
    @Transactional(readOnly = true)
    public CatArea findById(Integer id) {
        log.debug("Buscando área con ID: {}", id);
        return catAreaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Área", "id", id));
    }

    /**
     * Busca un área por código.
     */
    @Transactional(readOnly = true)
    public CatArea findByCodigo(String codigo) {
        log.debug("Buscando área con código: {}", codigo);
        return catAreaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Área", "código", codigo));
    }

    /**
     * Actualiza un área existente.
     * Valida que el código no esté duplicado (si se modifica).
     */
    public CatArea update(Integer id, CatArea area) {
        log.debug("Actualizando área con ID: {}", id);

        CatArea existing = findById(id);

        // Validar código duplicado solo si cambió
        if (!existing.getCodigo().equals(area.getCodigo()) &&
            catAreaRepository.existsByCodigo(area.getCodigo())) {
            throw new DuplicateResourceException("Área", "código", area.getCodigo());
        }

        existing.setCodigo(area.getCodigo());
        existing.setNombre(area.getNombre());
        existing.setDescripcion(area.getDescripcion());
        existing.setAreaSuperior(area.getAreaSuperior());
        existing.setActivo(area.getActivo());

        CatArea updated = catAreaRepository.save(existing);
        log.info("Área actualizada exitosamente con ID: {}", id);
        return updated;
    }

    /**
     * Elimina lógicamente un área (soft delete).
     */
    public void delete(Integer id) {
        log.debug("Eliminando (soft delete) área con ID: {}", id);

        CatArea area = findById(id);
        area.setActivo(false);
        catAreaRepository.save(area);

        log.info("Área eliminada lógicamente con ID: {}", id);
    }

    /**
     * Busca áreas por nombre (búsqueda parcial).
     */
    @Transactional(readOnly = true)
    public List<CatArea> search(String nombre) {
        log.debug("Buscando áreas con nombre: {}", nombre);
        return catAreaRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Obtiene áreas raíz (sin área superior).
     */
    @Transactional(readOnly = true)
    public List<CatArea> findAreasRaiz() {
        log.debug("Obteniendo áreas raíz");
        return catAreaRepository.findByAreaSuperiorIsNull();
    }

    /**
     * Obtiene sub-áreas de un área específica.
     */
    @Transactional(readOnly = true)
    public List<CatArea> findSubAreas(Integer areaSuperiorId) {
        log.debug("Obteniendo sub-áreas del área ID: {}", areaSuperiorId);
        return catAreaRepository.findByAreaSuperiorId(areaSuperiorId);
    }
}
