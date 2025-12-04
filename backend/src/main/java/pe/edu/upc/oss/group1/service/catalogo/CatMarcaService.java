package pe.edu.upc.oss.group1.service.catalogo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.oss.group1.entity.catalogo.CatMarca;
import pe.edu.upc.oss.group1.exception.DuplicateResourceException;
import pe.edu.upc.oss.group1.exception.ResourceNotFoundException;
import pe.edu.upc.oss.group1.repository.catalogo.CatMarcaRepository;

import java.util.List;

/**
 * Servicio que contiene la lógica de negocio para CatMarca.
 *
 * @Transactional: Todas las operaciones son transaccionales.
 * Si algo falla, se hace rollback automático.
 *
 * @RequiredArgsConstructor: Lombok genera constructor con campos final.
 * Permite inyección de dependencias vía constructor (recomendado).
 *
 * @Slf4j: Lombok genera logger automáticamente.
 * Podemos usar log.info(), log.error(), etc.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CatMarcaService {

    private final CatMarcaRepository catMarcaRepository;

    /**
     * Obtiene todas las marcas.
     *
     * @return Lista de todas las marcas (activas e inactivas)
     */
    @Transactional(readOnly = true)
    public List<CatMarca> findAll() {
        log.debug("Buscando todas las marcas");
        return catMarcaRepository.findAll();
    }

    /**
     * Obtiene solo las marcas activas.
     * Útil para formularios donde solo quieres mostrar marcas disponibles.
     *
     * @return Lista de marcas activas
     */
    @Transactional(readOnly = true)
    public List<CatMarca> findAllActive() {
        log.debug("Buscando marcas activas");
        return catMarcaRepository.findByActivoTrue();
    }

    /**
     * Busca una marca por su ID.
     *
     * @param id ID de la marca
     * @return Marca encontrada
     * @throws ResourceNotFoundException si no existe
     */
    @Transactional(readOnly = true)
    public CatMarca findById(Integer id) {
        log.debug("Buscando marca con ID: {}", id);
        return catMarcaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Marca no encontrada con ID: {}", id);
                    return new ResourceNotFoundException("Marca", "id", id);
                });
    }

    /**
     * Busca una marca por su código.
     *
     * @param codigo Código de la marca
     * @return Marca encontrada
     * @throws ResourceNotFoundException si no existe
     */
    @Transactional(readOnly = true)
    public CatMarca findByCodigo(String codigo) {
        log.debug("Buscando marca con código: {}", codigo);
        return catMarcaRepository.findByCodigo(codigo)
                .orElseThrow(() -> {
                    log.error("Marca no encontrada con código: {}", codigo);
                    return new ResourceNotFoundException("Marca", "código", codigo);
                });
    }

    /**
     * Crea una nueva marca.
     *
     * Validaciones:
     * - El código no debe existir previamente
     * - Los campos obligatorios deben estar presentes
     *
     * @param marca Marca a crear
     * @return Marca creada con ID asignado
     * @throws DuplicateResourceException si el código ya existe
     */
    public CatMarca create(CatMarca marca) {
        log.info("Creando nueva marca con código: {}", marca.getCodigo());

        // Validar que el código no exista
        if (catMarcaRepository.existsByCodigo(marca.getCodigo())) {
            log.error("Intento de crear marca con código duplicado: {}", marca.getCodigo());
            throw new DuplicateResourceException("Marca", "código", marca.getCodigo());
        }

        // Si no se especifica, marca como activa por defecto
        if (marca.getActivo() == null) {
            marca.setActivo(true);
        }

        CatMarca savedMarca = catMarcaRepository.save(marca);
        log.info("Marca creada exitosamente con ID: {}", savedMarca.getId());
        return savedMarca;
    }

    /**
     * Actualiza una marca existente.
     *
     * Validaciones:
     * - La marca debe existir
     * - Si se cambia el código, el nuevo código no debe existir
     *
     * @param id    ID de la marca a actualizar
     * @param marca Datos actualizados
     * @return Marca actualizada
     * @throws ResourceNotFoundException   si la marca no existe
     * @throws DuplicateResourceException si el nuevo código ya existe
     */
    public CatMarca update(Integer id, CatMarca marca) {
        log.info("Actualizando marca con ID: {}", id);

        // Verificar que la marca exista
        CatMarca existingMarca = findById(id);

        // Si cambió el código, verificar que el nuevo no exista
        if (!existingMarca.getCodigo().equals(marca.getCodigo())) {
            if (catMarcaRepository.existsByCodigo(marca.getCodigo())) {
                log.error("Intento de actualizar a código duplicado: {}", marca.getCodigo());
                throw new DuplicateResourceException("Marca", "código", marca.getCodigo());
            }
        }

        // Actualizar campos
        existingMarca.setCodigo(marca.getCodigo());
        existingMarca.setNombre(marca.getNombre());
        existingMarca.setActivo(marca.getActivo());

        CatMarca updatedMarca = catMarcaRepository.save(existingMarca);
        log.info("Marca actualizada exitosamente con ID: {}", id);
        return updatedMarca;
    }

    /**
     * Elimina lógicamente una marca (soft delete).
     * No se elimina físicamente de la BD, solo se marca como inactiva.
     *
     * Esto es importante para mantener integridad referencial con dispositivos
     * que ya tienen esta marca asignada.
     *
     * @param id ID de la marca a eliminar
     * @throws ResourceNotFoundException si la marca no existe
     */
    public void delete(Integer id) {
        log.info("Eliminando (desactivando) marca con ID: {}", id);

        CatMarca marca = findById(id);
        marca.setActivo(false);
        catMarcaRepository.save(marca);

        log.info("Marca desactivada exitosamente con ID: {}", id);
    }

    /**
     * Busca marcas por nombre (búsqueda parcial, case-insensitive).
     * Útil para autocomplete en el frontend.
     *
     * @param nombre Texto a buscar en el nombre
     * @return Lista de marcas que coinciden
     */
    @Transactional(readOnly = true)
    public List<CatMarca> searchByNombre(String nombre) {
        log.debug("Buscando marcas que contengan: {}", nombre);
        return catMarcaRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
