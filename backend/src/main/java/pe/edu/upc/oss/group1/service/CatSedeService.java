package pe.edu.upc.oss.group1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.oss.group1.entity.catalogo.CatSede;
import pe.edu.upc.oss.group1.exception.DuplicateResourceException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.CatSedeRepository;

import java.util.List;

/**
 * Servicio para la gestión del catálogo de sedes.
 * Implementa lógica de negocio y validaciones.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CatSedeService {

    private final CatSedeRepository catSedeRepository;

    /**
     * Crea una nueva sede.
     * Valida que el código no esté duplicado.
     */
    public CatSede create(CatSede sede) {
        log.debug("Creando sede con código: {}", sede.getCodigo());

        if (catSedeRepository.existsByCodigo(sede.getCodigo())) {
            throw new DuplicateResourceException("Sede", "código", sede.getCodigo());
        }

        CatSede saved = catSedeRepository.save(sede);
        log.info("Sede creada exitosamente con ID: {}", saved.getId());
        return saved;
    }

    /**
     * Obtiene todas las sedes.
     */
    @Transactional(readOnly = true)
    public List<CatSede> findAll() {
        log.debug("Obteniendo todas las sedes");
        return catSedeRepository.findAll();
    }

    /**
     * Obtiene solo las sedes activas.
     */
    @Transactional(readOnly = true)
    public List<CatSede> findAllActive() {
        log.debug("Obteniendo sedes activas");
        return catSedeRepository.findByActivoTrue();
    }

    /**
     * Busca una sede por ID.
     */
    @Transactional(readOnly = true)
    public CatSede findById(Integer id) {
        log.debug("Buscando sede con ID: {}", id);
        return catSedeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sede", "id", id));
    }

    /**
     * Busca una sede por código.
     */
    @Transactional(readOnly = true)
    public CatSede findByCodigo(String codigo) {
        log.debug("Buscando sede con código: {}", codigo);
        return catSedeRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Sede", "código", codigo));
    }

    /**
     * Actualiza una sede existente.
     * Valida que el código no esté duplicado (si se modifica).
     */
    public CatSede update(Integer id, CatSede sede) {
        log.debug("Actualizando sede con ID: {}", id);

        CatSede existing = findById(id);

        // Validar código duplicado solo si cambió
        if (!existing.getCodigo().equals(sede.getCodigo()) &&
            catSedeRepository.existsByCodigo(sede.getCodigo())) {
            throw new DuplicateResourceException("Sede", "código", sede.getCodigo());
        }

        existing.setCodigo(sede.getCodigo());
        existing.setNombre(sede.getNombre());
        existing.setDireccion(sede.getDireccion());
        existing.setCiudad(sede.getCiudad());
        existing.setPais(sede.getPais());
        existing.setActivo(sede.getActivo());

        CatSede updated = catSedeRepository.save(existing);
        log.info("Sede actualizada exitosamente con ID: {}", id);
        return updated;
    }

    /**
     * Elimina lógicamente una sede (soft delete).
     */
    public void delete(Integer id) {
        log.debug("Eliminando (soft delete) sede con ID: {}", id);

        CatSede sede = findById(id);
        sede.setActivo(false);
        catSedeRepository.save(sede);

        log.info("Sede eliminada lógicamente con ID: {}", id);
    }

    /**
     * Busca sedes por nombre (búsqueda parcial).
     */
    @Transactional(readOnly = true)
    public List<CatSede> search(String nombre) {
        log.debug("Buscando sedes con nombre: {}", nombre);
        return catSedeRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Obtiene sedes por ciudad.
     */
    @Transactional(readOnly = true)
    public List<CatSede> findByCiudad(String ciudad) {
        log.debug("Obteniendo sedes de la ciudad: {}", ciudad);
        return catSedeRepository.findByCiudad(ciudad);
    }

    /**
     * Obtiene sedes por país.
     */
    @Transactional(readOnly = true)
    public List<CatSede> findByPais(String pais) {
        log.debug("Obteniendo sedes del país: {}", pais);
        return catSedeRepository.findByPais(pais);
    }
}
