package pe.edu.upc.oss.group1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.oss.group1.entity.*;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoAsignacion;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoDispositivo;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoReemplazo;
import pe.edu.upc.oss.group1.exception.BusinessValidationException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.AsignacionDispositivoRepository;
import pe.edu.upc.oss.group1.repository.ReemplazoDispositivoRepository;
import pe.edu.upc.oss.group1.repository.catalogo.CatEstadoAsignacionRepository;
import pe.edu.upc.oss.group1.repository.catalogo.CatEstadoDispositivoRepository;
import pe.edu.upc.oss.group1.repository.catalogo.CatEstadoReemplazoRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para gestión de reemplazos de dispositivos.
 * Maneja la lógica compleja de reemplazo durante asignaciones activas.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReemplazoDispositivoService {

    private final ReemplazoDispositivoRepository reemplazoRepository;
    private final AsignacionDispositivoRepository asignacionRepository;
    private final DispositivoService dispositivoService;
    private final HistorialDispositivoService historialService;
    private final CatEstadoReemplazoRepository estadoReemplazoRepository;
    private final CatEstadoDispositivoRepository estadoDispositivoRepository;
    private final CatEstadoAsignacionRepository estadoAsignacionRepository;

    /**
     * Retorna todos los reemplazos.
     */
    @Transactional(readOnly = true)
    public List<ReemplazoDispositivo> findAll() {
        log.debug("Buscando todos los reemplazos");
        return reemplazoRepository.findAll();
    }

    /**
     * Retorna reemplazos pendientes.
     */
    @Transactional(readOnly = true)
    public List<ReemplazoDispositivo> findPendientes() {
        log.debug("Buscando reemplazos pendientes");
        return reemplazoRepository.findReemplazosPendientes();
    }

    /**
     * Busca un reemplazo por su ID.
     */
    @Transactional(readOnly = true)
    public ReemplazoDispositivo findById(Integer id) {
        log.debug("Buscando reemplazo con ID: {}", id);
        return reemplazoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reemplazo no encontrado con ID: " + id));
    }

    /**
     * Busca un reemplazo con todas sus relaciones cargadas.
     */
    @Transactional(readOnly = true)
    public ReemplazoDispositivo findByIdWithRelations(Integer id) {
        log.debug("Buscando reemplazo ID {} con relaciones", id);
        return reemplazoRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reemplazo no encontrado con ID: " + id));
    }

    /**
     * Retorna reemplazos de un empleado.
     */
    @Transactional(readOnly = true)
    public List<ReemplazoDispositivo> findByEmpleado(Integer empleadoId) {
        log.debug("Buscando reemplazos del empleado ID: {}", empleadoId);
        return reemplazoRepository.findByEmpleadoId(empleadoId);
    }

    /**
     * Crea una solicitud de reemplazo.
     */
    public ReemplazoDispositivo crear(ReemplazoDispositivo reemplazo) {
        log.info("Creando solicitud de reemplazo para asignación ID: {}",
                reemplazo.getAsignacionOriginal().getId());

        AsignacionDispositivo asignacion = asignacionRepository.findByIdWithRelations(
                reemplazo.getAsignacionOriginal().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Asignación no encontrada"));

        if (!asignacion.isActiva()) {
            throw new BusinessValidationException("La asignación no está activa");
        }

        Dispositivo dispositivoReemplazo = dispositivoService.findById(
                reemplazo.getDispositivoReemplazo().getId());

        if (!dispositivoReemplazo.isDisponibleParaAsignacion()) {
            throw new BusinessValidationException("El dispositivo de reemplazo no está disponible");
        }

        if (asignacion.getDispositivo().getId().equals(dispositivoReemplazo.getId())) {
            throw new BusinessValidationException(
                    "El dispositivo de reemplazo no puede ser el mismo que el original");
        }

        CatEstadoReemplazo estadoPendiente = estadoReemplazoRepository.findByCodigo("PENDIENTE")
                .orElseThrow(() -> new ResourceNotFoundException("Estado PENDIENTE no encontrado"));

        reemplazo.setEstadoReemplazo(estadoPendiente);
        reemplazo.setFechaReemplazo(LocalDate.now());
        reemplazo.setDispositivoOriginal(asignacion.getDispositivo());

        ReemplazoDispositivo saved = reemplazoRepository.save(reemplazo);

        log.info("Solicitud de reemplazo creada con ID: {}", saved.getId());
        return saved;
    }

    /**
     * Ejecuta un reemplazo aprobado.
     */
    public ReemplazoDispositivo ejecutarReemplazo(Integer reemplazoId) {
        log.info("Ejecutando reemplazo ID: {}", reemplazoId);

        ReemplazoDispositivo reemplazo = findByIdWithRelations(reemplazoId);

        if (!reemplazo.isPendiente()) {
            throw new BusinessValidationException("El reemplazo no está en estado pendiente");
        }

        AsignacionDispositivo asignacionOriginal = reemplazo.getAsignacionOriginal();
        Dispositivo dispositivoOriginal = reemplazo.getDispositivoOriginal();
        Dispositivo dispositivoReemplazo = reemplazo.getDispositivoReemplazo();

        CatEstadoAsignacion estadoDevuelta = estadoAsignacionRepository.findByCodigo("DEVUELTA")
                .orElseThrow(() -> new ResourceNotFoundException("Estado DEVUELTA no encontrado"));
        asignacionOriginal.setEstadoAsignacion(estadoDevuelta);
        asignacionOriginal.setFechaDevolucion(LocalDate.now());
        asignacionRepository.save(asignacionOriginal);

        AsignacionDispositivo nuevaAsignacion = new AsignacionDispositivo();
        nuevaAsignacion.setDispositivo(dispositivoReemplazo);
        nuevaAsignacion.setEmpleado(asignacionOriginal.getEmpleado());
        nuevaAsignacion.setFechaAsignacion(LocalDate.now());
        nuevaAsignacion.setUsuarioAsigna(reemplazo.getUsuarioRegistra());

        CatEstadoAsignacion estadoActiva = estadoAsignacionRepository.findByCodigo("ACTIVA")
                .orElseThrow(() -> new ResourceNotFoundException("Estado ACTIVA no encontrado"));
        nuevaAsignacion.setEstadoAsignacion(estadoActiva);
        nuevaAsignacion.setObservacionesAsignacion(
                "Asignado por reemplazo del dispositivo " + dispositivoOriginal.getCodigoActivo());
        asignacionRepository.save(nuevaAsignacion);

        CatEstadoDispositivo estadoDisponible = estadoDispositivoRepository.findByCodigo("DISPONIBLE")
                .orElseThrow(() -> new ResourceNotFoundException("Estado DISPONIBLE no encontrado"));
        dispositivoOriginal.setEstadoDispositivo(estadoDisponible);

        CatEstadoDispositivo estadoAsignado = estadoDispositivoRepository.findByCodigo("ASIGNADO")
                .orElseThrow(() -> new ResourceNotFoundException("Estado ASIGNADO no encontrado"));
        dispositivoReemplazo.setEstadoDispositivo(estadoAsignado);

        CatEstadoReemplazo estadoCompletado = estadoReemplazoRepository.findByCodigo("COMPLETADO")
                .orElseThrow(() -> new ResourceNotFoundException("Estado COMPLETADO no encontrado"));
        reemplazo.setEstadoReemplazo(estadoCompletado);

        ReemplazoDispositivo updated = reemplazoRepository.save(reemplazo);

        historialService.registrarReemplazo(
                dispositivoOriginal,
                dispositivoReemplazo,
                reemplazo.getEmpleado(),
                reemplazo.getUsuarioRegistra(),
                reemplazo.getMotivoReemplazo().getNombre());

        historialService.registrarAsignacion(
                dispositivoReemplazo,
                reemplazo.getEmpleado(),
                reemplazo.getUsuarioRegistra());

        log.info("Reemplazo ejecutado exitosamente");
        return updated;
    }

    /**
     * Cancela una solicitud de reemplazo.
     */
    public void cancelar(Integer reemplazoId, String motivo) {
        log.info("Cancelando reemplazo ID: {}", reemplazoId);

        ReemplazoDispositivo reemplazo = findById(reemplazoId);

        if (!reemplazo.isPendiente()) {
            throw new BusinessValidationException("Solo se pueden cancelar reemplazos pendientes");
        }

        CatEstadoReemplazo estadoCancelado = estadoReemplazoRepository.findByCodigo("CANCELADO")
                .orElseThrow(() -> new ResourceNotFoundException("Estado CANCELADO no encontrado"));

        reemplazo.setEstadoReemplazo(estadoCancelado);
        reemplazo.setDescripcionMotivo(reemplazo.getDescripcionMotivo() + " | Cancelado: " + motivo);

        reemplazoRepository.save(reemplazo);

        log.info("Reemplazo cancelado exitosamente");
    }
}
