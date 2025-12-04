package pe.edu.upc.oss.group1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.oss.group1.entity.catalogo.CatTipoDispositivo;
import pe.edu.upc.oss.group1.exception.DuplicateResourceException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.catalogo.CatTipoDispositivoRepository;

import java.util.List;

/**
 * Servicio para la gestión del catálogo de tipos de dispositivo.
 * Implementa lógica de negocio y validaciones.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CatTipoDispositivoService {

    private final CatTipoDispositivoRepository catTipoDispositivoRepository;

    /**
     * Crea un nuevo tipo de dispositivo.
     * Valida que el código no esté duplicado.
     */
    public CatTipoDispositivo create(CatTipoDispositivo tipoDispositivo) {
        log.debug("Creando tipo de dispositivo con código: {}", tipoDispositivo.getCodigo());

        if (catTipoDispositivoRepository.existsByCodigo(tipoDispositivo.getCodigo())) {
            throw new DuplicateResourceException("Tipo de dispositivo", "código", tipoDispositivo.getCodigo());
        }

        CatTipoDispositivo saved = catTipoDispositivoRepository.save(tipoDispositivo);
        log.info("Tipo de dispositivo creado exitosamente con ID: {}", saved.getId());
        return saved;
    }

    /**
     * Obtiene todos los tipos de dispositivo.
     */
    @Transactional(readOnly = true)
    public List<CatTipoDispositivo> findAll() {
        log.debug("Obteniendo todos los tipos de dispositivo");
        return catTipoDispositivoRepository.findAll();
    }

    /**
     * Obtiene solo los tipos activos.
     */
    @Transactional(readOnly = true)
    public List<CatTipoDispositivo> findAllActive() {
        log.debug("Obteniendo tipos de dispositivo activos");
        return catTipoDispositivoRepository.findByActivoTrue();
    }

    /**
     * Busca un tipo de dispositivo por ID.
     */
    @Transactional(readOnly = true)
    public CatTipoDispositivo findById(Integer id) {
        log.debug("Buscando tipo de dispositivo con ID: {}", id);
        return catTipoDispositivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de dispositivo", "id", id));
    }

    /**
     * Busca un tipo de dispositivo por código.
     */
    @Transactional(readOnly = true)
    public CatTipoDispositivo findByCodigo(String codigo) {
        log.debug("Buscando tipo de dispositivo con código: {}", codigo);
        return catTipoDispositivoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de dispositivo", "código", codigo));
    }

    /**
     * Actualiza un tipo de dispositivo existente.
     * Valida que el código no esté duplicado (si se modifica).
     */
    public CatTipoDispositivo update(Integer id, CatTipoDispositivo tipoDispositivo) {
        log.debug("Actualizando tipo de dispositivo con ID: {}", id);

        CatTipoDispositivo existing = findById(id);

        // Validar código duplicado solo si cambió
        if (!existing.getCodigo().equals(tipoDispositivo.getCodigo()) &&
            catTipoDispositivoRepository.existsByCodigo(tipoDispositivo.getCodigo())) {
            throw new DuplicateResourceException("Tipo de dispositivo", "código", tipoDispositivo.getCodigo());
        }

        existing.setCodigo(tipoDispositivo.getCodigo());
        existing.setNombre(tipoDispositivo.getNombre());
        existing.setDescripcion(tipoDispositivo.getDescripcion());
        existing.setRequiereSerie(tipoDispositivo.getRequiereSerie());
        existing.setActivo(tipoDispositivo.getActivo());

        CatTipoDispositivo updated = catTipoDispositivoRepository.save(existing);
        log.info("Tipo de dispositivo actualizado exitosamente con ID: {}", id);
        return updated;
    }

    /**
     * Elimina lógicamente un tipo de dispositivo (soft delete).
     */
    public void delete(Integer id) {
        log.debug("Eliminando (soft delete) tipo de dispositivo con ID: {}", id);

        CatTipoDispositivo tipoDispositivo = findById(id);
        tipoDispositivo.setActivo(false);
        catTipoDispositivoRepository.save(tipoDispositivo);

        log.info("Tipo de dispositivo eliminado lógicamente con ID: {}", id);
    }

    /**
     * Busca tipos por nombre (búsqueda parcial).
     */
    @Transactional(readOnly = true)
    public List<CatTipoDispositivo> search(String nombre) {
        log.debug("Buscando tipos de dispositivo con nombre: {}", nombre);
        return catTipoDispositivoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Obtiene tipos que requieren número de serie.
     */
    @Transactional(readOnly = true)
    public List<CatTipoDispositivo> findTiposConSerie() {
        log.debug("Obteniendo tipos de dispositivo que requieren serie");
        return catTipoDispositivoRepository.findByRequiereSerieTrue();
    }
}
