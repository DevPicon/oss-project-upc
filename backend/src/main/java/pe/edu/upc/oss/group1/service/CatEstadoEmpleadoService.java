package pe.edu.upc.oss.group1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoEmpleado;
import pe.edu.upc.oss.group1.exception.DuplicateResourceException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.CatEstadoEmpleadoRepository;

import java.util.List;

/**
 * Servicio para la gestión del catálogo de estados de empleado.
 * Implementa lógica de negocio y validaciones.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CatEstadoEmpleadoService {

    private final CatEstadoEmpleadoRepository catEstadoEmpleadoRepository;

    /**
     * Crea un nuevo estado de empleado.
     * Valida que el código no esté duplicado.
     */
    public CatEstadoEmpleado create(CatEstadoEmpleado estadoEmpleado) {
        log.debug("Creando estado de empleado con código: {}", estadoEmpleado.getCodigo());

        if (catEstadoEmpleadoRepository.existsByCodigo(estadoEmpleado.getCodigo())) {
            throw new DuplicateResourceException("Estado de empleado", "código", estadoEmpleado.getCodigo());
        }

        CatEstadoEmpleado saved = catEstadoEmpleadoRepository.save(estadoEmpleado);
        log.info("Estado de empleado creado exitosamente con ID: {}", saved.getId());
        return saved;
    }

    /**
     * Obtiene todos los estados de empleado.
     */
    @Transactional(readOnly = true)
    public List<CatEstadoEmpleado> findAll() {
        log.debug("Obteniendo todos los estados de empleado");
        return catEstadoEmpleadoRepository.findAll();
    }

    /**
     * Obtiene solo los estados activos.
     */
    @Transactional(readOnly = true)
    public List<CatEstadoEmpleado> findAllActive() {
        log.debug("Obteniendo estados de empleado activos");
        return catEstadoEmpleadoRepository.findByActivoTrue();
    }

    /**
     * Busca un estado de empleado por ID.
     */
    @Transactional(readOnly = true)
    public CatEstadoEmpleado findById(Integer id) {
        log.debug("Buscando estado de empleado con ID: {}", id);
        return catEstadoEmpleadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado de empleado", "id", id));
    }

    /**
     * Busca un estado de empleado por código.
     */
    @Transactional(readOnly = true)
    public CatEstadoEmpleado findByCodigo(String codigo) {
        log.debug("Buscando estado de empleado con código: {}", codigo);
        return catEstadoEmpleadoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Estado de empleado", "código", codigo));
    }

    /**
     * Actualiza un estado de empleado existente.
     * Valida que el código no esté duplicado (si se modifica).
     */
    public CatEstadoEmpleado update(Integer id, CatEstadoEmpleado estadoEmpleado) {
        log.debug("Actualizando estado de empleado con ID: {}", id);

        CatEstadoEmpleado existing = findById(id);

        // Validar código duplicado solo si cambió
        if (!existing.getCodigo().equals(estadoEmpleado.getCodigo()) &&
            catEstadoEmpleadoRepository.existsByCodigo(estadoEmpleado.getCodigo())) {
            throw new DuplicateResourceException("Estado de empleado", "código", estadoEmpleado.getCodigo());
        }

        existing.setCodigo(estadoEmpleado.getCodigo());
        existing.setNombre(estadoEmpleado.getNombre());
        existing.setDescripcion(estadoEmpleado.getDescripcion());
        existing.setActivo(estadoEmpleado.getActivo());

        CatEstadoEmpleado updated = catEstadoEmpleadoRepository.save(existing);
        log.info("Estado de empleado actualizado exitosamente con ID: {}", id);
        return updated;
    }

    /**
     * Elimina lógicamente un estado de empleado (soft delete).
     * Establece activo = false en lugar de eliminar físicamente.
     */
    public void delete(Integer id) {
        log.debug("Eliminando (soft delete) estado de empleado con ID: {}", id);

        CatEstadoEmpleado estadoEmpleado = findById(id);
        estadoEmpleado.setActivo(false);
        catEstadoEmpleadoRepository.save(estadoEmpleado);

        log.info("Estado de empleado eliminado lógicamente con ID: {}", id);
    }

    /**
     * Busca estados por nombre (búsqueda parcial).
     */
    @Transactional(readOnly = true)
    public List<CatEstadoEmpleado> search(String nombre) {
        log.debug("Buscando estados de empleado con nombre: {}", nombre);
        return catEstadoEmpleadoRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
