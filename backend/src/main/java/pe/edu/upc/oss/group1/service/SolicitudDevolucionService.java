package pe.edu.upc.oss.group1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.oss.group1.entity.Empleado;
import pe.edu.upc.oss.group1.entity.SolicitudDevolucion;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoSolicitud;
import pe.edu.upc.oss.group1.exception.BusinessValidationException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.SolicitudDevolucionRepository;
import pe.edu.upc.oss.group1.repository.catalogo.CatEstadoSolicitudRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para gestión de solicitudes de devolución de dispositivos.
 * Maneja el proceso de devolución por cese o cambio de empleado.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SolicitudDevolucionService {

    private final SolicitudDevolucionRepository solicitudRepository;
    private final EmpleadoService empleadoService;
    private final CatEstadoSolicitudRepository estadoSolicitudRepository;

    /**
     * Retorna todas las solicitudes de devolución.
     */
    @Transactional(readOnly = true)
    public List<SolicitudDevolucion> findAll() {
        log.debug("Buscando todas las solicitudes de devolución");
        return solicitudRepository.findAll();
    }

    /**
     * Retorna solicitudes pendientes.
     */
    @Transactional(readOnly = true)
    public List<SolicitudDevolucion> findPendientes() {
        log.debug("Buscando solicitudes pendientes");
        return solicitudRepository.findSolicitudesPendientes();
    }

    /**
     * Retorna solicitudes pendientes con paginación.
     */
    @Transactional(readOnly = true)
    public Page<SolicitudDevolucion> findPendientes(Pageable pageable) {
        log.debug("Buscando solicitudes pendientes con paginación");
        return solicitudRepository.findSolicitudesPendientes(pageable);
    }

    /**
     * Retorna solicitudes atrasadas.
     */
    @Transactional(readOnly = true)
    public List<SolicitudDevolucion> findAtrasadas() {
        log.debug("Buscando solicitudes atrasadas");
        return solicitudRepository.findSolicitudesAtrasadas();
    }

    /**
     * Busca una solicitud por su ID.
     */
    @Transactional(readOnly = true)
    public SolicitudDevolucion findById(Integer id) {
        log.debug("Buscando solicitud de devolución con ID: {}", id);
        return solicitudRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Solicitud de devolución no encontrada con ID: " + id));
    }

    /**
     * Busca una solicitud con todas sus relaciones cargadas.
     */
    @Transactional(readOnly = true)
    public SolicitudDevolucion findByIdWithRelations(Integer id) {
        log.debug("Buscando solicitud ID {} con relaciones", id);
        return solicitudRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Solicitud de devolución no encontrada con ID: " + id));
    }

    /**
     * Busca una solicitud con sus detalles cargados.
     */
    @Transactional(readOnly = true)
    public SolicitudDevolucion findByIdWithDetalles(Integer id) {
        log.debug("Buscando solicitud ID {} con detalles", id);
        return solicitudRepository.findByIdWithDetalles(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Solicitud de devolución no encontrada con ID: " + id));
    }

    /**
     * Retorna solicitudes de un empleado.
     */
    @Transactional(readOnly = true)
    public List<SolicitudDevolucion> findByEmpleado(Integer empleadoId) {
        log.debug("Buscando solicitudes del empleado ID: {}", empleadoId);
        return solicitudRepository.findByEmpleadoId(empleadoId);
    }

    /**
     * Crea una nueva solicitud de devolución.
     */
    public SolicitudDevolucion crear(SolicitudDevolucion solicitud) {
        log.info("Creando solicitud de devolución para empleado ID: {}",
                solicitud.getEmpleado().getId());

        Empleado empleado = empleadoService.findById(solicitud.getEmpleado().getId());

        validateSolicitudData(solicitud);

        CatEstadoSolicitud estadoPendiente = estadoSolicitudRepository.findByCodigo("PENDIENTE")
                .orElseThrow(() -> new ResourceNotFoundException("Estado PENDIENTE no encontrado"));

        solicitud.setEstadoSolicitud(estadoPendiente);
        solicitud.setFechaSolicitud(LocalDate.now());

        SolicitudDevolucion saved = solicitudRepository.save(solicitud);

        log.info("Solicitud de devolución creada con ID: {}", saved.getId());
        return saved;
    }

    /**
     * Actualiza una solicitud de devolución.
     */
    public SolicitudDevolucion update(Integer id, SolicitudDevolucion solicitud) {
        log.info("Actualizando solicitud de devolución con ID: {}", id);

        SolicitudDevolucion existing = findById(id);

        if (existing.isCompletada()) {
            throw new BusinessValidationException("No se puede modificar una solicitud completada");
        }

        validateSolicitudData(solicitud);

        existing.setFechaTerminoEmpleado(solicitud.getFechaTerminoEmpleado());
        existing.setFechaDevolucionProgramada(solicitud.getFechaDevolucionProgramada());
        existing.setObservaciones(solicitud.getObservaciones());

        return solicitudRepository.save(existing);
    }

    /**
     * Completa una solicitud de devolución.
     */
    public SolicitudDevolucion completar(Integer solicitudId, Integer usuarioRecibeId) {
        log.info("Completando solicitud de devolución ID: {}", solicitudId);

        SolicitudDevolucion solicitud = findByIdWithDetalles(solicitudId);

        if (!solicitud.isPendiente()) {
            throw new BusinessValidationException("La solicitud no está en estado pendiente");
        }

        if (solicitud.getDetalles().isEmpty()) {
            throw new BusinessValidationException(
                    "La solicitud no tiene dispositivos registrados en el detalle");
        }

        CatEstadoSolicitud estadoCompletada = estadoSolicitudRepository.findByCodigo("COMPLETADA")
                .orElseThrow(() -> new ResourceNotFoundException("Estado COMPLETADA no encontrado"));

        solicitud.setEstadoSolicitud(estadoCompletada);
        solicitud.setFechaDevolucionReal(LocalDate.now());

        if (usuarioRecibeId != null) {
            solicitud.getUsuarioRecibe().setId(usuarioRecibeId);
        }

        SolicitudDevolucion updated = solicitudRepository.save(solicitud);

        log.info("Solicitud completada exitosamente");
        return updated;
    }

    /**
     * Cancela una solicitud de devolución.
     */
    public void cancelar(Integer solicitudId, String motivo) {
        log.info("Cancelando solicitud de devolución ID: {}", solicitudId);

        SolicitudDevolucion solicitud = findById(solicitudId);

        if (!solicitud.isPendiente()) {
            throw new BusinessValidationException("Solo se pueden cancelar solicitudes pendientes");
        }

        CatEstadoSolicitud estadoCancelada = estadoSolicitudRepository.findByCodigo("CANCELADA")
                .orElseThrow(() -> new ResourceNotFoundException("Estado CANCELADA no encontrado"));

        solicitud.setEstadoSolicitud(estadoCancelada);
        solicitud.setObservaciones(solicitud.getObservaciones() + " | Cancelada: " + motivo);

        solicitudRepository.save(solicitud);

        log.info("Solicitud cancelada exitosamente");
    }

    /**
     * Valida los datos de la solicitud.
     */
    private void validateSolicitudData(SolicitudDevolucion solicitud) {
        if (solicitud.getFechaTerminoEmpleado() == null) {
            throw new BusinessValidationException("La fecha de término del empleado es obligatoria");
        }

        if (solicitud.getFechaDevolucionProgramada() == null) {
            throw new BusinessValidationException("La fecha de devolución programada es obligatoria");
        }

        if (solicitud.getFechaDevolucionProgramada().isBefore(solicitud.getFechaTerminoEmpleado())) {
            throw new BusinessValidationException(
                    "La fecha de devolución programada no puede ser anterior a la fecha de término");
        }

        if (solicitud.getUsuarioSolicita() == null) {
            throw new BusinessValidationException("El usuario que solicita es obligatorio");
        }
    }
}
