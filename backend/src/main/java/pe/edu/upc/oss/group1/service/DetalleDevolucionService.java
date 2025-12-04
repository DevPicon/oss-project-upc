package pe.edu.upc.oss.group1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.oss.group1.entity.AsignacionDispositivo;
import pe.edu.upc.oss.group1.entity.DetalleDevolucion;
import pe.edu.upc.oss.group1.entity.Dispositivo;
import pe.edu.upc.oss.group1.entity.SolicitudDevolucion;
import pe.edu.upc.oss.group1.exception.BusinessValidationException;
import pe.edu.upc.oss.group1.exception.DuplicateResourceException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.AsignacionDispositivoRepository;
import pe.edu.upc.oss.group1.repository.DetalleDevolucionRepository;

import java.util.List;

/**
 * Servicio para gestión de detalles de devolución.
 * Maneja el registro de dispositivos específicos en solicitudes de devolución.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DetalleDevolucionService {

    private final DetalleDevolucionRepository detalleRepository;
    private final SolicitudDevolucionService solicitudService;
    private final DispositivoService dispositivoService;
    private final AsignacionDispositivoRepository asignacionRepository;

    /**
     * Retorna todos los detalles de una solicitud.
     */
    @Transactional(readOnly = true)
    public List<DetalleDevolucion> findBySolicitud(Integer solicitudId) {
        log.debug("Buscando detalles de la solicitud ID: {}", solicitudId);
        return detalleRepository.findBySolicitudDevolucionId(solicitudId);
    }

    /**
     * Busca un detalle por su ID.
     */
    @Transactional(readOnly = true)
    public DetalleDevolucion findById(Integer id) {
        log.debug("Buscando detalle de devolución con ID: {}", id);
        return detalleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Detalle de devolución no encontrado con ID: " + id));
    }

    /**
     * Busca un detalle con todas sus relaciones cargadas.
     */
    @Transactional(readOnly = true)
    public DetalleDevolucion findByIdWithRelations(Integer id) {
        log.debug("Buscando detalle ID {} con relaciones", id);
        return detalleRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Detalle de devolución no encontrado con ID: " + id));
    }

    /**
     * Agrega un dispositivo a una solicitud de devolución.
     */
    public DetalleDevolucion agregar(DetalleDevolucion detalle) {
        log.info("Agregando dispositivo ID {} a solicitud ID {}",
                detalle.getDispositivo().getId(),
                detalle.getSolicitudDevolucion().getId());

        SolicitudDevolucion solicitud = solicitudService.findById(
                detalle.getSolicitudDevolucion().getId());

        if (!solicitud.isPendiente()) {
            throw new BusinessValidationException(
                    "Solo se pueden agregar dispositivos a solicitudes pendientes");
        }

        Dispositivo dispositivo = dispositivoService.findById(detalle.getDispositivo().getId());

        if (detalleRepository.existsBySolicitudDevolucionIdAndDispositivoId(
                solicitud.getId(), dispositivo.getId())) {
            throw new DuplicateResourceException(
                    "El dispositivo ya está en la solicitud de devolución");
        }

        AsignacionDispositivo asignacionActiva = asignacionRepository
                .findAsignacionActivaByDispositivo(dispositivo.getId())
                .orElseThrow(() -> new BusinessValidationException(
                        "El dispositivo no tiene una asignación activa"));

        if (!asignacionActiva.getEmpleado().getId().equals(solicitud.getEmpleado().getId())) {
            throw new BusinessValidationException(
                    "El dispositivo no está asignado al empleado de la solicitud");
        }

        detalle.setAsignacion(asignacionActiva);

        if (detalle.getCondicionDevolucion() == null) {
            throw new BusinessValidationException("La condición de devolución es obligatoria");
        }

        DetalleDevolucion saved = detalleRepository.save(detalle);

        log.info("Dispositivo agregado exitosamente a la solicitud");
        return saved;
    }

    /**
     * Actualiza la condición de devolución de un detalle.
     */
    public DetalleDevolucion updateCondicion(Integer detalleId, DetalleDevolucion detalle) {
        log.info("Actualizando condición del detalle ID: {}", detalleId);

        DetalleDevolucion existing = findByIdWithRelations(detalleId);

        if (!existing.getSolicitudDevolucion().isPendiente()) {
            throw new BusinessValidationException(
                    "No se puede modificar un detalle de una solicitud completada");
        }

        existing.setCondicionDevolucion(detalle.getCondicionDevolucion());
        existing.setObservaciones(detalle.getObservaciones());

        return detalleRepository.save(existing);
    }

    /**
     * Elimina un detalle de una solicitud de devolución.
     */
    public void delete(Integer detalleId) {
        log.info("Eliminando detalle de devolución ID: {}", detalleId);

        DetalleDevolucion detalle = findByIdWithRelations(detalleId);

        if (!detalle.getSolicitudDevolucion().isPendiente()) {
            throw new BusinessValidationException(
                    "No se pueden eliminar detalles de solicitudes completadas");
        }

        detalleRepository.delete(detalle);

        log.info("Detalle eliminado exitosamente");
    }
}
