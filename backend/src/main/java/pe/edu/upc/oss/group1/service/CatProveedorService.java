package pe.edu.upc.oss.group1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.oss.group1.entity.catalogo.CatProveedor;
import pe.edu.upc.oss.group1.exception.DuplicateResourceException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.catalogo.CatProveedorRepository;

import java.util.List;

/**
 * Servicio para la gestión del catálogo de proveedores.
 * Implementa lógica de negocio y validaciones.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CatProveedorService {

    private final CatProveedorRepository catProveedorRepository;

    /**
     * Crea un nuevo proveedor.
     * Valida que el código y RUC no estén duplicados.
     */
    public CatProveedor create(CatProveedor proveedor) {
        log.debug("Creando proveedor con código: {}", proveedor.getCodigo());

        if (catProveedorRepository.existsByCodigo(proveedor.getCodigo())) {
            throw new DuplicateResourceException("Proveedor", "código", proveedor.getCodigo());
        }

        if (catProveedorRepository.existsByRuc(proveedor.getRuc())) {
            throw new DuplicateResourceException("Proveedor", "RUC", proveedor.getRuc());
        }

        CatProveedor saved = catProveedorRepository.save(proveedor);
        log.info("Proveedor creado exitosamente con ID: {}", saved.getId());
        return saved;
    }

    /**
     * Obtiene todos los proveedores.
     */
    @Transactional(readOnly = true)
    public List<CatProveedor> findAll() {
        log.debug("Obteniendo todos los proveedores");
        return catProveedorRepository.findAll();
    }

    /**
     * Obtiene solo los proveedores activos.
     */
    @Transactional(readOnly = true)
    public List<CatProveedor> findAllActive() {
        log.debug("Obteniendo proveedores activos");
        return catProveedorRepository.findByActivoTrue();
    }

    /**
     * Busca un proveedor por ID.
     */
    @Transactional(readOnly = true)
    public CatProveedor findById(Integer id) {
        log.debug("Buscando proveedor con ID: {}", id);
        return catProveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor", "id", id));
    }

    /**
     * Busca un proveedor por código.
     */
    @Transactional(readOnly = true)
    public CatProveedor findByCodigo(String codigo) {
        log.debug("Buscando proveedor con código: {}", codigo);
        return catProveedorRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor", "código", codigo));
    }

    /**
     * Busca un proveedor por RUC.
     */
    @Transactional(readOnly = true)
    public CatProveedor findByRuc(String ruc) {
        log.debug("Buscando proveedor con RUC: {}", ruc);
        return catProveedorRepository.findByRuc(ruc)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor", "RUC", ruc));
    }

    /**
     * Actualiza un proveedor existente.
     * Valida que el código y RUC no estén duplicados (si se modifican).
     */
    public CatProveedor update(Integer id, CatProveedor proveedor) {
        log.debug("Actualizando proveedor con ID: {}", id);

        CatProveedor existing = findById(id);

        // Validar código duplicado solo si cambió
        if (!existing.getCodigo().equals(proveedor.getCodigo()) &&
            catProveedorRepository.existsByCodigo(proveedor.getCodigo())) {
            throw new DuplicateResourceException("Proveedor", "código", proveedor.getCodigo());
        }

        // Validar RUC duplicado solo si cambió
        if (!existing.getRuc().equals(proveedor.getRuc()) &&
            catProveedorRepository.existsByRuc(proveedor.getRuc())) {
            throw new DuplicateResourceException("Proveedor", "RUC", proveedor.getRuc());
        }

        existing.setCodigo(proveedor.getCodigo());
        existing.setRazonSocial(proveedor.getRazonSocial());
        existing.setRuc(proveedor.getRuc());
        existing.setEmail(proveedor.getEmail());
        existing.setTelefono(proveedor.getTelefono());
        existing.setDireccion(proveedor.getDireccion());
        existing.setActivo(proveedor.getActivo());

        CatProveedor updated = catProveedorRepository.save(existing);
        log.info("Proveedor actualizado exitosamente con ID: {}", id);
        return updated;
    }

    /**
     * Elimina lógicamente un proveedor (soft delete).
     */
    public void delete(Integer id) {
        log.debug("Eliminando (soft delete) proveedor con ID: {}", id);

        CatProveedor proveedor = findById(id);
        proveedor.setActivo(false);
        catProveedorRepository.save(proveedor);

        log.info("Proveedor eliminado lógicamente con ID: {}", id);
    }

    /**
     * Busca proveedores por razón social (búsqueda parcial).
     */
    @Transactional(readOnly = true)
    public List<CatProveedor> search(String razonSocial) {
        log.debug("Buscando proveedores con razón social: {}", razonSocial);
        return catProveedorRepository.findByRazonSocialContainingIgnoreCase(razonSocial);
    }
}
