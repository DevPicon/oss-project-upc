package pe.edu.upc.oss.group1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.oss.group1.entity.Dispositivo;
import pe.edu.upc.oss.group1.exception.BusinessValidationException;
import pe.edu.upc.oss.group1.exception.DuplicateResourceException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.DispositivoRepository;
import pe.edu.upc.oss.group1.repository.catalogo.CatEstadoDispositivoRepository;
import pe.edu.upc.oss.group1.entity.catalogo.CatEstadoDispositivo;


import java.util.List;

/**
 * Servicio para gestión de dispositivos IT.
 * Proporciona operaciones CRUD y lógica de negocio para activos tecnológicos.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DispositivoService {

    private final DispositivoRepository dispositivoRepository;
    private final CatEstadoDispositivoRepository catEstadoDispositivoRepository;

    /**
     * Retorna todos los dispositivos.
     */
    @Transactional(readOnly = true)
    public List<Dispositivo> findAll() {
        log.debug("Buscando todos los dispositivos");
        return dispositivoRepository.findAll();
    }

    /**
     * Retorna dispositivos disponibles para asignación.
     */
    @Transactional(readOnly = true)
    public List<Dispositivo> findDisponibles() {
        log.debug("Buscando dispositivos disponibles");
        return dispositivoRepository.findDispositivosDisponibles();
    }

    /**
     * Retorna dispositivos disponibles con paginación.
     */
    @Transactional(readOnly = true)
    public Page<Dispositivo> findDisponibles(Pageable pageable) {
        log.debug("Buscando dispositivos disponibles con paginación");
        return dispositivoRepository.findDispositivosDisponibles(pageable);
    }

    /**
     * Busca un dispositivo por su ID.
     */
    @Transactional(readOnly = true)
    public Dispositivo findById(Integer id) {
        log.debug("Buscando dispositivo con ID: {}", id);
        return dispositivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo no encontrado con ID: " + id));
    }

    /**
     * Busca un dispositivo por su código de activo.
     */
    @Transactional(readOnly = true)
    public Dispositivo findByCodigoActivo(String codigo) {
        log.debug("Buscando dispositivo por código: {}", codigo);
        return dispositivoRepository.findByCodigoActivo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo no encontrado con código: " + codigo));
    }

    /**
     * Busca un dispositivo con sus relaciones cargadas.
     */
    @Transactional(readOnly = true)
    public Dispositivo findByIdWithRelations(Integer id) {
        log.debug("Buscando dispositivo ID {} con relaciones", id);
        return dispositivoRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo no encontrado con ID: " + id));
    }

    /**
     * Busca un dispositivo con su historial cargado.
     */
    @Transactional(readOnly = true)
    public Dispositivo findByIdWithHistorial(Integer id) {
        log.debug("Buscando dispositivo ID {} con historial", id);
        return dispositivoRepository.findByIdWithHistorial(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo no encontrado con ID: " + id));
    }

    /**
     * Busca dispositivos por tipo.
     */
    @Transactional(readOnly = true)
    public List<Dispositivo> findByTipo(Integer tipoId) {
        log.debug("Buscando dispositivos del tipo ID: {}", tipoId);
        return dispositivoRepository.findByTipoDispositivoIdWithRelations(tipoId);
    }

    /**
     * Búsqueda de dispositivos con paginación.
     */
    @Transactional(readOnly = true)
    public Page<Dispositivo> search(String searchTerm, Pageable pageable) {
        log.debug("Buscando dispositivos con término: {}", searchTerm);
        return dispositivoRepository.searchDispositivos(searchTerm, pageable);
    }

    /**
     * Crea un nuevo dispositivo.
     */
    public Dispositivo create(Dispositivo dispositivo) {
        log.info("Creando nuevo dispositivo: {}", dispositivo.getCodigoActivo());

        if (dispositivoRepository.existsByCodigoActivo(dispositivo.getCodigoActivo())) {
            throw new DuplicateResourceException("Ya existe un dispositivo con el código: " + dispositivo.getCodigoActivo());
        }

        if (dispositivo.getNumeroSerie() != null &&
            dispositivoRepository.existsByNumeroSerie(dispositivo.getNumeroSerie())) {
            throw new DuplicateResourceException("Ya existe un dispositivo con el número de serie: " + dispositivo.getNumeroSerie());
        }

        validateDispositivoData(dispositivo);

        return dispositivoRepository.save(dispositivo);
    }

    /**
     * Actualiza un dispositivo existente.
     */
    public Dispositivo update(Integer id, Dispositivo dispositivo) {
        log.info("Actualizando dispositivo con ID: {}", id);

        Dispositivo existing = findById(id);

        if (!existing.getCodigoActivo().equals(dispositivo.getCodigoActivo()) &&
            dispositivoRepository.existsByCodigoActivo(dispositivo.getCodigoActivo())) {
            throw new DuplicateResourceException("Ya existe un dispositivo con el código: " + dispositivo.getCodigoActivo());
        }

        if (dispositivo.getNumeroSerie() != null &&
            !dispositivo.getNumeroSerie().equals(existing.getNumeroSerie()) &&
            dispositivoRepository.existsByNumeroSerie(dispositivo.getNumeroSerie())) {
            throw new DuplicateResourceException("Ya existe un dispositivo con el número de serie: " + dispositivo.getNumeroSerie());
        }

        validateDispositivoData(dispositivo);

        existing.setCodigoActivo(dispositivo.getCodigoActivo());
        existing.setNumeroSerie(dispositivo.getNumeroSerie());
        existing.setTipoDispositivo(dispositivo.getTipoDispositivo());
        existing.setMarca(dispositivo.getMarca());
        existing.setModelo(dispositivo.getModelo());
        existing.setEstadoDispositivo(dispositivo.getEstadoDispositivo());
        existing.setEspecificaciones(dispositivo.getEspecificaciones());
        existing.setFechaAdquisicion(dispositivo.getFechaAdquisicion());
        existing.setValorAdquisicion(dispositivo.getValorAdquisicion());
        existing.setProveedor(dispositivo.getProveedor());
        existing.setObservaciones(dispositivo.getObservaciones());

        return dispositivoRepository.save(existing);
    }

    /**
     * Actualiza solo el estado y observación de un dispositivo.
     */
    public Dispositivo updateEstado(Integer id, Integer estadoId, String observacion) {
        log.info("Actualizando estado de dispositivo ID: {} a Estado ID: {}", id, estadoId);

        Dispositivo existing = findById(id);

        CatEstadoDispositivo nuevoEstado = catEstadoDispositivoRepository.findById(estadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Estado de dispositivo no encontrado con ID: " + estadoId));

        existing.setEstadoDispositivo(nuevoEstado);
        
        if (observacion != null) {
            existing.setObservaciones(observacion);
        }

        return dispositivoRepository.save(existing);
    }

    /**
     * Elimina un dispositivo.
     */
    public void delete(Integer id) {
        log.warn("Eliminando dispositivo con ID: {}", id);
        Dispositivo dispositivo = findById(id);
        dispositivoRepository.delete(dispositivo);
    }

    /**
     * Valida los datos del dispositivo.
     */
    private void validateDispositivoData(Dispositivo dispositivo) {
        if (dispositivo.getTipoDispositivo() == null) {
            throw new BusinessValidationException("El tipo de dispositivo es obligatorio");
        }

        if (dispositivo.getMarca() == null) {
            throw new BusinessValidationException("La marca es obligatoria");
        }

        if (dispositivo.getEstadoDispositivo() == null) {
            throw new BusinessValidationException("El estado del dispositivo es obligatorio");
        }

        if (dispositivo.getValorAdquisicion() != null &&
            dispositivo.getValorAdquisicion().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new BusinessValidationException("El valor de adquisición no puede ser negativo");
        }
    }
}
