package pe.edu.upc.oss.group1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.oss.group1.entity.AsignacionDispositivo;
import pe.edu.upc.oss.group1.entity.Dispositivo;
import pe.edu.upc.oss.group1.entity.Empleado;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoAsignacion;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoDispositivo;
import pe.edu.upc.oss.group1.exception.BusinessValidationException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.AsignacionDispositivoRepository;
import pe.edu.upc.oss.group1.repository.catalogo.CatEstadoAsignacionRepository;
import pe.edu.upc.oss.group1.repository.catalogo.CatEstadoDispositivoRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para gestión de asignaciones de dispositivos a empleados.
 * Implementa la lógica de negocio compleja para asignación y devolución.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AsignacionDispositivoService {

    private final AsignacionDispositivoRepository asignacionRepository;
    private final EmpleadoService empleadoService;
    private final DispositivoService dispositivoService;
    private final HistorialDispositivoService historialService;
    private final CatEstadoAsignacionRepository estadoAsignacionRepository;
    private final CatEstadoDispositivoRepository estadoDispositivoRepository;

    /**
     * Retorna todas las asignaciones.
     */
    @Transactional(readOnly = true)
    public List<AsignacionDispositivo> findAll() {
        log.debug("Buscando todas las asignaciones");
        return asignacionRepository.findAll();
    }

    /**
     * Retorna todas las asignaciones activas.
     */
    @Transactional(readOnly = true)
    public List<AsignacionDispositivo> findAllActivas() {
        log.debug("Buscando asignaciones activas");
        return asignacionRepository.findAllAsignacionesActivas();
    }

    /**
     * Retorna todas las asignaciones activas con paginación.
     */
    @Transactional(readOnly = true)
    public Page<AsignacionDispositivo> findAllActivas(Pageable pageable) {
        log.debug("Buscando asignaciones activas con paginación");
        return asignacionRepository.findAllAsignacionesActivas(pageable);
    }

    /**
     * Busca una asignación por su ID.
     */
    @Transactional(readOnly = true)
    public AsignacionDispositivo findById(Integer id) {
        log.debug("Buscando asignación con ID: {}", id);
        return asignacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación no encontrada con ID: " + id));
    }

    /**
     * Busca una asignación con todas sus relaciones cargadas.
     */
    @Transactional(readOnly = true)
    public AsignacionDispositivo findByIdWithRelations(Integer id) {
        log.debug("Buscando asignación ID {} con relaciones", id);
        return asignacionRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación no encontrada con ID: " + id));
    }

    /**
     * Retorna asignaciones de un empleado.
     */
    @Transactional(readOnly = true)
    public List<AsignacionDispositivo> findByEmpleado(Integer empleadoId) {
        log.debug("Buscando asignaciones del empleado ID: {}", empleadoId);
        return asignacionRepository.findByEmpleadoId(empleadoId);
    }

    /**
     * Retorna asignaciones activas de un empleado.
     */
    @Transactional(readOnly = true)
    public List<AsignacionDispositivo> findActivasByEmpleado(Integer empleadoId) {
        log.debug("Buscando asignaciones activas del empleado ID: {}", empleadoId);
        return asignacionRepository.findAsignacionesActivasByEmpleado(empleadoId);
    }

    /**
     * Retorna el historial de asignaciones de un dispositivo.
     */
    @Transactional(readOnly = true)
    public List<AsignacionDispositivo> findByDispositivo(Integer dispositivoId) {
        log.debug("Buscando asignaciones del dispositivo ID: {}", dispositivoId);
        return asignacionRepository.findByDispositivoId(dispositivoId);
    }

    /**
     * Cuenta dispositivos asignados a un empleado.
     */
    @Transactional(readOnly = true)
    public Long countDispositivosAsignados(Integer empleadoId) {
        log.debug("Contando dispositivos asignados al empleado ID: {}", empleadoId);
        return asignacionRepository.countDispositivosAsignadosByEmpleado(empleadoId);
    }

    /**
     * Crea una nueva asignación de dispositivo a empleado.
     */
    public AsignacionDispositivo crear(AsignacionDispositivo asignacion) {
        log.info("Creando asignación de dispositivo ID {} a empleado ID {}",
                asignacion.getDispositivo().getId(),
                asignacion.getEmpleado().getId());

        Empleado empleado = empleadoService.findById(asignacion.getEmpleado().getId());
        if (!empleado.isActivo()) {
            throw new BusinessValidationException("El empleado no está activo");
        }

        Dispositivo dispositivo = dispositivoService.findById(asignacion.getDispositivo().getId());
        if (!dispositivo.isDisponibleParaAsignacion()) {
            throw new BusinessValidationException("El dispositivo no está disponible para asignación");
        }

        asignacionRepository.findAsignacionActivaByDispositivo(dispositivo.getId())
                .ifPresent(a -> {
                    throw new BusinessValidationException("El dispositivo ya está asignado");
                });

        // Ensure full entities are set to avoid issues with mappers accessing null fields
        asignacion.setEmpleado(empleado);
        asignacion.setDispositivo(dispositivo);

        CatEstadoAsignacion estadoActiva = estadoAsignacionRepository.findByCodigo("ACTIVA")
                .orElseThrow(() -> new ResourceNotFoundException("Estado ACTIVA no encontrado"));

        asignacion.setEstadoAsignacion(estadoActiva);
        asignacion.setFechaAsignacion(LocalDate.now());

        AsignacionDispositivo saved = asignacionRepository.save(asignacion);

        CatEstadoDispositivo estadoAsignado = estadoDispositivoRepository.findByCodigo("ASIGNADO")
                .orElseThrow(() -> new ResourceNotFoundException("Estado ASIGNADO no encontrado"));
        dispositivo.setEstadoDispositivo(estadoAsignado);

        historialService.registrarAsignacion(dispositivo, empleado, asignacion.getUsuarioAsigna());

        log.info("Asignación creada exitosamente con ID: {}", saved.getId());
        return asignacionRepository.findByIdWithRelations(saved.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Asignación no encontrada con ID: " + saved.getId()));
    }

    /**
     * Registra la devolución de un dispositivo asignado.
     */
    public AsignacionDispositivo registrarDevolucion(Integer asignacionId, String observaciones, Integer usuarioRecibeId) {
        log.info("Registrando devolución de asignación ID: {}", asignacionId);

        AsignacionDispositivo asignacion = asignacionRepository.findByIdWithRelations(asignacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación no encontrada con ID: " + asignacionId));

        if (!asignacion.isActiva()) {
            throw new BusinessValidationException("La asignación no está activa");
        }

        CatEstadoAsignacion estadoDevuelta = estadoAsignacionRepository.findByCodigo("DEVUELTA")
                .orElseThrow(() -> new ResourceNotFoundException("Estado DEVUELTA no encontrado"));

        asignacion.setEstadoAsignacion(estadoDevuelta);
        asignacion.setFechaDevolucion(LocalDate.now());
        asignacion.setObservacionesDevolucion(observaciones);

        if (usuarioRecibeId != null) {
            asignacion.getUsuarioRecibe().setId(usuarioRecibeId);
        }

        AsignacionDispositivo updated = asignacionRepository.save(asignacion);

        Dispositivo dispositivo = asignacion.getDispositivo();
        CatEstadoDispositivo estadoDisponible = estadoDispositivoRepository.findByCodigo("DISPONIBLE")
                .orElseThrow(() -> new ResourceNotFoundException("Estado DISPONIBLE no encontrado"));
        dispositivo.setEstadoDispositivo(estadoDisponible);

        historialService.registrarDevolucion(dispositivo, asignacion.getEmpleado(), asignacion.getUsuarioRecibe());

        log.info("Devolución registrada exitosamente");
        return updated;
    }

    /**
     * Cancela una asignación activa.
     */
    public void cancelar(Integer asignacionId, String motivo) {
        log.info("Cancelando asignación ID: {}", asignacionId);

        AsignacionDispositivo asignacion = asignacionRepository.findByIdWithRelations(asignacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación no encontrada con ID: " + asignacionId));

        if (!asignacion.isActiva()) {
            throw new BusinessValidationException("La asignación no está activa");
        }

        CatEstadoAsignacion estadoCancelada = estadoAsignacionRepository.findByCodigo("CANCELADA")
                .orElseThrow(() -> new ResourceNotFoundException("Estado CANCELADA no encontrado"));

        asignacion.setEstadoAsignacion(estadoCancelada);
        asignacion.setObservacionesDevolucion(motivo);

        asignacionRepository.save(asignacion);

        Dispositivo dispositivo = asignacion.getDispositivo();
        CatEstadoDispositivo estadoDisponible = estadoDispositivoRepository.findByCodigo("DISPONIBLE")
                .orElseThrow(() -> new ResourceNotFoundException("Estado DISPONIBLE no encontrado"));
        dispositivo.setEstadoDispositivo(estadoDisponible);

        log.info("Asignación cancelada exitosamente");
    }

    /**
     * Elimina una asignación.
     */
    public void delete(Integer id) {
        log.warn("Eliminando asignación con ID: {}", id);
        AsignacionDispositivo asignacion = findById(id);
        asignacionRepository.delete(asignacion);
    }
}
