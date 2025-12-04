package pe.edu.upc.oss.group1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.oss.group1.entity.Empleado;
import pe.edu.upc.oss.group1.exception.BusinessValidationException;
import pe.edu.upc.oss.group1.exception.DuplicateResourceException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.EmpleadoRepository;

import java.util.List;

/**
 * Servicio para gestión de empleados.
 * Proporciona operaciones CRUD y lógica de negocio para empleados.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    /**
     * Retorna todos los empleados.
     */
    @Transactional(readOnly = true)
    public List<Empleado> findAll() {
        log.debug("Buscando todos los empleados");
        return empleadoRepository.findAll();
    }

    /**
     * Retorna solo los empleados activos.
     */
    @Transactional(readOnly = true)
    public List<Empleado> findAllActivos() {
        log.debug("Buscando empleados activos");
        return empleadoRepository.findAllActivos();
    }

    /**
     * Busca un empleado por su ID.
     */
    @Transactional(readOnly = true)
    public Empleado findById(Integer id) {
        log.debug("Buscando empleado con ID: {}", id);
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con ID: " + id));
    }

    /**
     * Busca un empleado por su código.
     */
    @Transactional(readOnly = true)
    public Empleado findByCodigoEmpleado(String codigo) {
        log.debug("Buscando empleado por código: {}", codigo);
        return empleadoRepository.findByCodigoEmpleado(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con código: " + codigo));
    }

    /**
     * Busca empleados por área.
     */
    @Transactional(readOnly = true)
    public List<Empleado> findByArea(Integer areaId) {
        log.debug("Buscando empleados del área ID: {}", areaId);
        return empleadoRepository.findByAreaIdWithRelations(areaId);
    }

    /**
     * Busca empleados por sede.
     */
    @Transactional(readOnly = true)
    public List<Empleado> findBySede(Integer sedeId) {
        log.debug("Buscando empleados de la sede ID: {}", sedeId);
        return empleadoRepository.findBySedeId(sedeId);
    }

    /**
     * Búsqueda de empleados con paginación.
     */
    @Transactional(readOnly = true)
    public Page<Empleado> search(String searchTerm, Pageable pageable) {
        log.debug("Buscando empleados con término: {}", searchTerm);
        return empleadoRepository.searchEmpleados(searchTerm, pageable);
    }

    /**
     * Busca un empleado con sus asignaciones cargadas.
     */
    @Transactional(readOnly = true)
    public Empleado findByIdWithAsignaciones(Integer id) {
        log.debug("Buscando empleado ID {} con asignaciones", id);
        return empleadoRepository.findByIdWithAsignaciones(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con ID: " + id));
    }

    /**
     * Crea un nuevo empleado.
     */
    public Empleado create(Empleado empleado) {
        log.info("Creando nuevo empleado: {}", empleado.getCodigoEmpleado());

        if (empleadoRepository.existsByCodigoEmpleado(empleado.getCodigoEmpleado())) {
            throw new DuplicateResourceException("Ya existe un empleado con el código: " + empleado.getCodigoEmpleado());
        }

        if (empleadoRepository.existsByEmail(empleado.getEmail())) {
            throw new DuplicateResourceException("Ya existe un empleado con el email: " + empleado.getEmail());
        }

        validateEmpleadoData(empleado);

        return empleadoRepository.save(empleado);
    }

    /**
     * Actualiza un empleado existente.
     */
    public Empleado update(Integer id, Empleado empleado) {
        log.info("Actualizando empleado con ID: {}", id);

        Empleado existing = findById(id);

        if (!existing.getCodigoEmpleado().equals(empleado.getCodigoEmpleado()) &&
            empleadoRepository.existsByCodigoEmpleado(empleado.getCodigoEmpleado())) {
            throw new DuplicateResourceException("Ya existe un empleado con el código: " + empleado.getCodigoEmpleado());
        }

        if (!existing.getEmail().equals(empleado.getEmail()) &&
            empleadoRepository.existsByEmail(empleado.getEmail())) {
            throw new DuplicateResourceException("Ya existe un empleado con el email: " + empleado.getEmail());
        }

        validateEmpleadoData(empleado);

        existing.setCodigoEmpleado(empleado.getCodigoEmpleado());
        existing.setNombre(empleado.getNombre());
        existing.setApellidoPaterno(empleado.getApellidoPaterno());
        existing.setApellidoMaterno(empleado.getApellidoMaterno());
        existing.setEmail(empleado.getEmail());
        existing.setTelefono(empleado.getTelefono());
        existing.setArea(empleado.getArea());
        existing.setPuesto(empleado.getPuesto());
        existing.setSede(empleado.getSede());
        existing.setFechaIngreso(empleado.getFechaIngreso());
        existing.setFechaTermino(empleado.getFechaTermino());
        existing.setEstadoEmpleado(empleado.getEstadoEmpleado());

        return empleadoRepository.save(existing);
    }

    /**
     * Elimina un empleado.
     */
    public void delete(Integer id) {
        log.warn("Eliminando empleado con ID: {}", id);
        Empleado empleado = findById(id);
        empleadoRepository.delete(empleado);
    }

    /**
     * Valida los datos del empleado.
     */
    private void validateEmpleadoData(Empleado empleado) {
        if (empleado.getArea() == null) {
            throw new BusinessValidationException("El área es obligatoria");
        }

        if (empleado.getPuesto() == null) {
            throw new BusinessValidationException("El puesto es obligatorio");
        }

        if (empleado.getSede() == null) {
            throw new BusinessValidationException("La sede es obligatoria");
        }

        if (empleado.getEstadoEmpleado() == null) {
            throw new BusinessValidationException("El estado del empleado es obligatorio");
        }

        if (empleado.getFechaTermino() != null &&
            empleado.getFechaTermino().isBefore(empleado.getFechaIngreso())) {
            throw new BusinessValidationException("La fecha de término no puede ser anterior a la fecha de ingreso");
        }
    }
}
