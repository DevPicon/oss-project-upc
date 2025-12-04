package pe.edu.upc.oss.group1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.oss.group1.entity.catalogo.CatMotivoReemplazo;
import pe.edu.upc.oss.group1.exception.DuplicateResourceException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.catalogo.CatMotivoReemplazoRepository;

import java.util.List;

/**
 * Servicio para la gestión del catálogo de motivos de reemplazo.
 * Implementa lógica de negocio y validaciones.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CatMotivoReemplazoService {

    private final CatMotivoReemplazoRepository catMotivoReemplazoRepository;

    /**
     * Crea un nuevo motivo de reemplazo.
     * Valida que el código no esté duplicado.
     */
    public CatMotivoReemplazo create(CatMotivoReemplazo motivoReemplazo) {
        log.debug("Creando motivo de reemplazo con código: {}", motivoReemplazo.getCodigo());

        if (catMotivoReemplazoRepository.existsByCodigo(motivoReemplazo.getCodigo())) {
            throw new DuplicateResourceException("Motivo de reemplazo", "código", motivoReemplazo.getCodigo());
        }

        CatMotivoReemplazo saved = catMotivoReemplazoRepository.save(motivoReemplazo);
        log.info("Motivo de reemplazo creado exitosamente con ID: {}", saved.getId());
        return saved;
    }

    /**
     * Obtiene todos los motivos de reemplazo.
     */
    @Transactional(readOnly = true)
    public List<CatMotivoReemplazo> findAll() {
        log.debug("Obteniendo todos los motivos de reemplazo");
        return catMotivoReemplazoRepository.findAll();
    }

    /**
     * Obtiene solo los motivos activos.
     */
    @Transactional(readOnly = true)
    public List<CatMotivoReemplazo> findAllActive() {
        log.debug("Obteniendo motivos de reemplazo activos");
        return catMotivoReemplazoRepository.findByActivoTrue();
    }

    /**
     * Busca un motivo de reemplazo por ID.
     */
    @Transactional(readOnly = true)
    public CatMotivoReemplazo findById(Integer id) {
        log.debug("Buscando motivo de reemplazo con ID: {}", id);
        return catMotivoReemplazoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Motivo de reemplazo", "id", id));
    }

    /**
     * Busca un motivo de reemplazo por código.
     */
    @Transactional(readOnly = true)
    public CatMotivoReemplazo findByCodigo(String codigo) {
        log.debug("Buscando motivo de reemplazo con código: {}", codigo);
        return catMotivoReemplazoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Motivo de reemplazo", "código", codigo));
    }

    /**
     * Actualiza un motivo de reemplazo existente.
     * Valida que el código no esté duplicado (si se modifica).
     */
    public CatMotivoReemplazo update(Integer id, CatMotivoReemplazo motivoReemplazo) {
        log.debug("Actualizando motivo de reemplazo con ID: {}", id);

        CatMotivoReemplazo existing = findById(id);

        // Validar código duplicado solo si cambió
        if (!existing.getCodigo().equals(motivoReemplazo.getCodigo()) &&
            catMotivoReemplazoRepository.existsByCodigo(motivoReemplazo.getCodigo())) {
            throw new DuplicateResourceException("Motivo de reemplazo", "código", motivoReemplazo.getCodigo());
        }

        existing.setCodigo(motivoReemplazo.getCodigo());
        existing.setNombre(motivoReemplazo.getNombre());
        existing.setDescripcion(motivoReemplazo.getDescripcion());
        existing.setActivo(motivoReemplazo.getActivo());

        CatMotivoReemplazo updated = catMotivoReemplazoRepository.save(existing);
        log.info("Motivo de reemplazo actualizado exitosamente con ID: {}", id);
        return updated;
    }

    /**
     * Elimina lógicamente un motivo de reemplazo (soft delete).
     */
    public void delete(Integer id) {
        log.debug("Eliminando (soft delete) motivo de reemplazo con ID: {}", id);

        CatMotivoReemplazo motivoReemplazo = findById(id);
        motivoReemplazo.setActivo(false);
        catMotivoReemplazoRepository.save(motivoReemplazo);

        log.info("Motivo de reemplazo eliminado lógicamente con ID: {}", id);
    }

    /**
     * Busca motivos por nombre (búsqueda parcial).
     */
    @Transactional(readOnly = true)
    public List<CatMotivoReemplazo> search(String nombre) {
        log.debug("Buscando motivos de reemplazo con nombre: {}", nombre);
        return catMotivoReemplazoRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
