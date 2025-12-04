package pe.edu.upc.oss.group1.controller.catalogo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oss.group1.dto.mapper.CatMarcaMapper;
import pe.edu.upc.oss.group1.dto.request.CatMarcaRequest;
import pe.edu.upc.oss.group1.dto.response.CatMarcaResponse;
import pe.edu.upc.oss.group1.entity.catalogo.CatMarca;
import pe.edu.upc.oss.group1.service.catalogo.CatMarcaService;

import java.util.List;

/**
 * Controller REST para operaciones CRUD sobre el catálogo de marcas.
 *
 * Base URL: /api/v1/catalogos/marcas
 *
 * Endpoints:
 * - GET    /api/v1/catalogos/marcas          → Listar todas las marcas
 * - GET    /api/v1/catalogos/marcas/activas  → Listar solo marcas activas
 * - GET    /api/v1/catalogos/marcas/{id}     → Obtener marca por ID
 * - POST   /api/v1/catalogos/marcas          → Crear nueva marca
 * - PUT    /api/v1/catalogos/marcas/{id}     → Actualizar marca
 * - DELETE /api/v1/catalogos/marcas/{id}     → Eliminar (desactivar) marca
 * - GET    /api/v1/catalogos/marcas/search?q={nombre} → Buscar por nombre
 *
 * @RestController: Combina @Controller + @ResponseBody
 * Todos los métodos devuelven JSON automáticamente.
 *
 * @RequestMapping: Define el prefijo de URL para todos los endpoints.
 *
 * @RequiredArgsConstructor: Inyección de dependencias vía constructor (Lombok).
 *
 * @Slf4j: Logger automático.
 */
@RestController
@RequestMapping("/api/v1/catalogos/marcas")
@RequiredArgsConstructor
@Slf4j
public class CatMarcaController {

    private final CatMarcaService catMarcaService;

    /**
     * GET /api/v1/catalogos/marcas
     *
     * Obtiene todas las marcas (activas e inactivas).
     *
     * @return Lista de marcas
     */
    @GetMapping
    public ResponseEntity<List<CatMarcaResponse>> findAll() {
        log.info("GET /api/v1/catalogos/marcas - Obteniendo todas las marcas");

        List<CatMarca> marcas = catMarcaService.findAll();
        List<CatMarcaResponse> response = CatMarcaMapper.toResponseList(marcas);

        log.info("Se encontraron {} marcas", response.size());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/catalogos/marcas/activas
     *
     * Obtiene solo las marcas activas.
     * Útil para formularios donde solo se deben mostrar opciones disponibles.
     *
     * @return Lista de marcas activas
     */
    @GetMapping("/activas")
    public ResponseEntity<List<CatMarcaResponse>> findAllActive() {
        log.info("GET /api/v1/catalogos/marcas/activas - Obteniendo marcas activas");

        List<CatMarca> marcas = catMarcaService.findAllActive();
        List<CatMarcaResponse> response = CatMarcaMapper.toResponseList(marcas);

        log.info("Se encontraron {} marcas activas", response.size());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/catalogos/marcas/{id}
     *
     * Obtiene una marca específica por su ID.
     *
     * @param id ID de la marca
     * @return Marca encontrada
     * @throws ResourceNotFoundException si no existe (manejada por GlobalExceptionHandler)
     */
    @GetMapping("/{id}")
    public ResponseEntity<CatMarcaResponse> findById(@PathVariable Integer id) {
        log.info("GET /api/v1/catalogos/marcas/{} - Obteniendo marca por ID", id);

        CatMarca marca = catMarcaService.findById(id);
        CatMarcaResponse response = CatMarcaMapper.toResponse(marca);

        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/v1/catalogos/marcas
     *
     * Crea una nueva marca.
     *
     * @param request Datos de la marca a crear
     * @return Marca creada con HTTP 201 Created
     * @throws DuplicateResourceException si el código ya existe
     *
     * @Valid: Valida el request según las anotaciones en CatMarcaRequest.
     * Si falla, lanza MethodArgumentNotValidException (HTTP 400).
     *
     * Body ejemplo:
     * {
     *   "codigo": "DELL",
     *   "nombre": "Dell",
     *   "activo": true
     * }
     */
    @PostMapping
    public ResponseEntity<CatMarcaResponse> create(@Valid @RequestBody CatMarcaRequest request) {
        log.info("POST /api/v1/catalogos/marcas - Creando marca con código: {}", request.getCodigo());

        CatMarca marca = CatMarcaMapper.toEntity(request);
        CatMarca createdMarca = catMarcaService.create(marca);
        CatMarcaResponse response = CatMarcaMapper.toResponse(createdMarca);

        log.info("Marca creada exitosamente con ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/v1/catalogos/marcas/{id}
     *
     * Actualiza una marca existente.
     *
     * @param id      ID de la marca a actualizar
     * @param request Datos actualizados
     * @return Marca actualizada con HTTP 200 OK
     * @throws ResourceNotFoundException   si la marca no existe
     * @throws DuplicateResourceException si el nuevo código ya existe
     *
     * Body ejemplo:
     * {
     *   "codigo": "DELL",
     *   "nombre": "Dell Technologies",
     *   "activo": true
     * }
     */
    @PutMapping("/{id}")
    public ResponseEntity<CatMarcaResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody CatMarcaRequest request) {

        log.info("PUT /api/v1/catalogos/marcas/{} - Actualizando marca", id);

        CatMarca marca = CatMarcaMapper.toEntity(request);
        CatMarca updatedMarca = catMarcaService.update(id, marca);
        CatMarcaResponse response = CatMarcaMapper.toResponse(updatedMarca);

        log.info("Marca actualizada exitosamente con ID: {}", id);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/v1/catalogos/marcas/{id}
     *
     * Elimina (desactiva) una marca.
     *
     * IMPORTANTE: Es una eliminación lógica (soft delete), NO física.
     * Solo se marca la marca como inactiva (activo = false).
     *
     * Esto es necesario para mantener integridad referencial con dispositivos
     * que tienen esta marca asignada.
     *
     * @param id ID de la marca a eliminar
     * @return HTTP 204 No Content
     * @throws ResourceNotFoundException si la marca no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("DELETE /api/v1/catalogos/marcas/{} - Eliminando (desactivando) marca", id);

        catMarcaService.delete(id);

        log.info("Marca desactivada exitosamente con ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/v1/catalogos/marcas/search?q={nombre}
     *
     * Busca marcas cuyo nombre contenga el texto dado (case-insensitive).
     * Útil para autocomplete en el frontend.
     *
     * @param nombre Texto a buscar
     * @return Lista de marcas que coinciden
     *
     * Ejemplo: /api/v1/catalogos/marcas/search?q=dell
     * Devuelve: [{"id": 1, "codigo": "DELL", "nombre": "Dell", ...}]
     */
    @GetMapping("/search")
    public ResponseEntity<List<CatMarcaResponse>> search(@RequestParam("q") String nombre) {
        log.info("GET /api/v1/catalogos/marcas/search?q={} - Buscando marcas por nombre", nombre);

        List<CatMarca> marcas = catMarcaService.searchByNombre(nombre);
        List<CatMarcaResponse> response = CatMarcaMapper.toResponseList(marcas);

        log.info("Se encontraron {} marcas que contienen '{}'", response.size(), nombre);
        return ResponseEntity.ok(response);
    }
}
