package pe.edu.upc.oss.group1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.oss.group1.entity.Dispositivo;
import pe.edu.upc.oss.group1.entity.Empleado;
import pe.edu.upc.oss.group1.entity.HistorialDispositivo;
import pe.edu.upc.oss.group1.entity.Usuario;
import pe.edu.upc.oss.group1.entity.catalogo.CatTipoMovimiento;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.HistorialDispositivoRepository;
import pe.edu.upc.oss.group1.repository.catalogo.CatTipoMovimientoRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para gestión del historial de dispositivos.
 * Proporciona auditoría completa de movimientos y cambios.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class HistorialDispositivoService {

    private final HistorialDispositivoRepository historialRepository;
    private final CatTipoMovimientoRepository tipoMovimientoRepository;

    /**
     * Retorna el historial completo de un dispositivo.
     */
    @Transactional(readOnly = true)
    public List<HistorialDispositivo> findByDispositivo(Integer dispositivoId) {
        log.debug("Buscando historial del dispositivo ID: {}", dispositivoId);
        return historialRepository.findByDispositivoIdOrderByFechaMovimientoDesc(dispositivoId);
    }

    /**
     * Retorna el historial de un dispositivo con paginación.
     */
    @Transactional(readOnly = true)
    public Page<HistorialDispositivo> findByDispositivo(Integer dispositivoId, Pageable pageable) {
        log.debug("Buscando historial del dispositivo ID {} con paginación", dispositivoId);
        return historialRepository.findByDispositivoIdOrderByFechaMovimientoDesc(dispositivoId, pageable);
    }

    /**
     * Retorna los últimos N movimientos de un dispositivo.
     */
    @Transactional(readOnly = true)
    public List<HistorialDispositivo> findUltimosMovimientos(Integer dispositivoId, int cantidad) {
        log.debug("Buscando últimos {} movimientos del dispositivo ID {}", cantidad, dispositivoId);
        return historialRepository.findUltimosMovimientos(dispositivoId, PageRequest.of(0, cantidad));
    }

    /**
     * Registra una asignación en el historial.
     */
    public HistorialDispositivo registrarAsignacion(Dispositivo dispositivo, Empleado empleado, Usuario usuario) {
        log.info("Registrando asignación en historial para dispositivo ID: {}", dispositivo.getId());

        CatTipoMovimiento tipoAsignacion = tipoMovimientoRepository.findByCodigo("ASIGNACION")
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de movimiento ASIGNACION no encontrado"));

        HistorialDispositivo historial = new HistorialDispositivo();
        historial.setDispositivo(dispositivo);
        historial.setTipoMovimiento(tipoAsignacion);
        historial.setUsuario(usuario);
        historial.setDescripcion(String.format("Asignado a %s", empleado.getNombreCompleto()));
        historial.setFechaMovimiento(LocalDateTime.now());

        return historialRepository.save(historial);
    }

    /**
     * Registra una devolución en el historial.
     */
    public HistorialDispositivo registrarDevolucion(Dispositivo dispositivo, Empleado empleado, Usuario usuario) {
        log.info("Registrando devolución en historial para dispositivo ID: {}", dispositivo.getId());

        CatTipoMovimiento tipoDevolucion = tipoMovimientoRepository.findByCodigo("DEVOLUCION")
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de movimiento DEVOLUCION no encontrado"));

        HistorialDispositivo historial = new HistorialDispositivo();
        historial.setDispositivo(dispositivo);
        historial.setTipoMovimiento(tipoDevolucion);
        historial.setUsuario(usuario);
        historial.setDescripcion(String.format("Devuelto por %s", empleado.getNombreCompleto()));
        historial.setFechaMovimiento(LocalDateTime.now());

        return historialRepository.save(historial);
    }

    /**
     * Registra un reemplazo en el historial.
     */
    public HistorialDispositivo registrarReemplazo(Dispositivo dispositivoOriginal,
                                                    Dispositivo dispositivoReemplazo,
                                                    Empleado empleado,
                                                    Usuario usuario,
                                                    String motivo) {
        log.info("Registrando reemplazo en historial para dispositivo ID: {}", dispositivoOriginal.getId());

        CatTipoMovimiento tipoReemplazo = tipoMovimientoRepository.findByCodigo("REEMPLAZO")
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de movimiento REEMPLAZO no encontrado"));

        HistorialDispositivo historial = new HistorialDispositivo();
        historial.setDispositivo(dispositivoOriginal);
        historial.setTipoMovimiento(tipoReemplazo);
        historial.setUsuario(usuario);
        historial.setDescripcion(String.format("Reemplazado por %s. Motivo: %s. Empleado: %s",
                dispositivoReemplazo.getCodigoActivo(),
                motivo,
                empleado.getNombreCompleto()));
        historial.setFechaMovimiento(LocalDateTime.now());

        return historialRepository.save(historial);
    }

    /**
     * Registra un cambio de estado en el historial.
     */
    public HistorialDispositivo registrarCambioEstado(Dispositivo dispositivo,
                                                       String estadoAnterior,
                                                       String estadoNuevo,
                                                       Usuario usuario,
                                                       String descripcion) {
        log.info("Registrando cambio de estado en historial para dispositivo ID: {}", dispositivo.getId());

        CatTipoMovimiento tipoCambio = tipoMovimientoRepository.findByCodigo("CAMBIO_ESTADO")
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de movimiento CAMBIO_ESTADO no encontrado"));

        HistorialDispositivo historial = new HistorialDispositivo();
        historial.setDispositivo(dispositivo);
        historial.setTipoMovimiento(tipoCambio);
        historial.setUsuario(usuario);
        historial.setDescripcion(descripcion != null ? descripcion :
                String.format("Estado cambiado de %s a %s", estadoAnterior, estadoNuevo));
        historial.setDatosAnteriores(estadoAnterior);
        historial.setDatosNuevos(estadoNuevo);
        historial.setFechaMovimiento(LocalDateTime.now());

        return historialRepository.save(historial);
    }

    /**
     * Registra un mantenimiento en el historial.
     */
    public HistorialDispositivo registrarMantenimiento(Dispositivo dispositivo,
                                                        Usuario usuario,
                                                        String descripcion) {
        log.info("Registrando mantenimiento en historial para dispositivo ID: {}", dispositivo.getId());

        CatTipoMovimiento tipoMantenimiento = tipoMovimientoRepository.findByCodigo("MANTENIMIENTO")
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de movimiento MANTENIMIENTO no encontrado"));

        HistorialDispositivo historial = new HistorialDispositivo();
        historial.setDispositivo(dispositivo);
        historial.setTipoMovimiento(tipoMantenimiento);
        historial.setUsuario(usuario);
        historial.setDescripcion(descripcion);
        historial.setFechaMovimiento(LocalDateTime.now());

        return historialRepository.save(historial);
    }

    /**
     * Registra un movimiento genérico en el historial.
     */
    public HistorialDispositivo registrarMovimiento(Dispositivo dispositivo,
                                                     String codigoTipoMovimiento,
                                                     Usuario usuario,
                                                     String descripcion) {
        log.info("Registrando movimiento {} en historial para dispositivo ID: {}",
                codigoTipoMovimiento, dispositivo.getId());

        CatTipoMovimiento tipoMovimiento = tipoMovimientoRepository.findByCodigo(codigoTipoMovimiento)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tipo de movimiento no encontrado: " + codigoTipoMovimiento));

        HistorialDispositivo historial = new HistorialDispositivo();
        historial.setDispositivo(dispositivo);
        historial.setTipoMovimiento(tipoMovimiento);
        historial.setUsuario(usuario);
        historial.setDescripcion(descripcion);
        historial.setFechaMovimiento(LocalDateTime.now());

        return historialRepository.save(historial);
    }
}
